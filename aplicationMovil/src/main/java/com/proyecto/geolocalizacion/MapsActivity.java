package com.proyecto.geolocalizacion;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyect.movil.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_MAPAS = 200;
    public static final String KEY_PARAM_LATITUD = "latitud";
    public static final String KEY_PARAM_LONGITUD = "longitud";

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Marker mMarker;
    private CameraPosition mCameraZoom;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbMapActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default_aceptar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_aceptar:
                if(mMarker == null || mCurrentLocation == null){
                    showMessage("No ha seleccionado una ubicación.");
                }else{
                    Intent intent = new Intent();
                    intent.putExtra(KEY_PARAM_LATITUD, mCurrentLocation.getLatitude());
                    intent.putExtra(KEY_PARAM_LONGITUD, mCurrentLocation.getLongitude());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try{
            mMap = googleMap;
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setOnMapClickListener(mMapOnMapClickListener);
            mMap.setOnMarkerDragListener(mMarkerOnDragListener);

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mLocationListener);

            if (!this.checkGPSIsEnabled()) {
                showInfoAlert();
            } else {

                //Obtener los parametros de ubicacion existente
                if(getIntent().getExtras() != null){
                    if(getIntent().getExtras().containsKey(KEY_PARAM_LONGITUD) &&
                            getIntent().getExtras().containsKey(KEY_PARAM_LATITUD)){
                        if(mCurrentLocation == null)
                            mCurrentLocation = new Location(LocationManager.GPS_PROVIDER);
                        mCurrentLocation.setLatitude(getIntent().getDoubleExtra(KEY_PARAM_LATITUD, 0));
                        mCurrentLocation.setLongitude(getIntent().getDoubleExtra(KEY_PARAM_LONGITUD, 0));

                        createOrUpdateMarkerByLocation(mCurrentLocation);
                        zoomToLocation(mCurrentLocation);
                    }
                }else {
                    //De lo contrario, obtener los datos de la ubicación actual
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                    mCurrentLocation = location;

                    if (mCurrentLocation != null) {
                        createOrUpdateMarkerByLocation(mCurrentLocation);
                        zoomToLocation(mCurrentLocation);
                    } else {
                        showMessage("Ocurrió un error intentando obtener información sobre su ubicación actual...");
                    }
                }
            }
        }catch(Exception e){
            showMessage("OnMapready() > " + e.getMessage());
        }
    }

    //region FUNCIONES_PROPIAS
    private boolean checkGPSIsEnabled() {
        try {
            int locationMode = 0;
            String locationProviders;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                try {
                    locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    showMessage("checkGPSIsEnabled() SDKV>19 > " + e.getMessage());
                    return false;
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF;

            }else{
                locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }


        } catch (Exception e) {
            showMessage("checkGPSIsEnabled() > " + e.getMessage());
            return false;
        }
    }

    private void showInfoAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Señal GPS")
                .setMessage("No tienes señal GPS. Quieres habilitarla?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //El GPS no está activado
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void zoomToLocation(Location location){

        mCameraZoom = new CameraPosition
                .Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)
                .bearing(120)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraZoom));
    }

    private void createOrUpdateMarkerByLocation(Location location){
        if(mMarker == null){
            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true));
            mMarker.setTitle("Ubicación seleccionada");
            mMarker.setSnippet("Mantenga presionado para mover");
        }else{
            mMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //endregion

    //region LISTENER'S
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //createOrUpdateMarkerByLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private GoogleMap.OnMapClickListener mMapOnMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(mCurrentLocation == null){
                mCurrentLocation = new Location(LocationManager.GPS_PROVIDER);
            }
            mCurrentLocation.setLatitude(latLng.latitude);
            mCurrentLocation.setLongitude(latLng.longitude);

            createOrUpdateMarkerByLocation(mCurrentLocation);
            zoomToLocation(mCurrentLocation);
        }
    };

    private GoogleMap.OnMarkerDragListener mMarkerOnDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mCurrentLocation.setLatitude(marker.getPosition().latitude);
            mCurrentLocation.setLongitude(marker.getPosition().longitude);
            createOrUpdateMarkerByLocation(mCurrentLocation);
            zoomToLocation(mCurrentLocation);
        }
    };

    //endregion
}
