package com.proyecto.sociosnegocio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class SocioDireccionActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                        LocationListener,
                                                            GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    private FloatingActionButton fabMiLocation;
    private FloatingActionButton fabSaveLocation;
    private LocationManager locationManager;
    private Location currentLocation;
    private Marker markerMyLocation;
    private CameraPosition cameraZoom;
    private LatLng mLatLngDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socio_direccion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCliMap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabMiLocation = (FloatingActionButton) findViewById(R.id.fab);
        fabMiLocation.setOnClickListener(fabClickListener);

        fabSaveLocation = (FloatingActionButton) findViewById(R.id.fab2);
        fabSaveLocation.setOnClickListener(fabSaveLocationClickListener);

        if(getIntent().getExtras().containsKey("titulo")){
            setTitle(getIntent().getExtras().getString("titulo"));
        }

        if(getIntent().getExtras().containsKey("latitud") && !getIntent().getExtras().getString("latitud").isEmpty() &&
                getIntent().getExtras().containsKey("longitud") && !getIntent().getExtras().getString("longitud").isEmpty()){
            mLatLngDireccion = new LatLng(Double.parseDouble(getIntent().getExtras().getString("latitud")),
                    Double.parseDouble(getIntent().getExtras().getString("longitud")));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //MEJORAR ESTO, REQUEST FOR PERMISSION IN ANDROID API 23 A´+
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        //<----

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //Obtendra la localizaion actual segun parametros:
        //minTiem: Milisegundos; minDistance: Metros
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, this);

        updateLocation();
        createMarkerByRegisteredLocation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkGPSIsEnabled(){
        try {
            int gpsSigal = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if(gpsSigal == 0){
                return  false;
            }else{
                return  true;
            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private void showInfoAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Activar GPS")
                .setMessage("El GPS se encuentra deshabilitado. ¿Quieres activarlo?")
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

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateLocation();
        }
    };

    private View.OnClickListener fabSaveLocationClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(markerMyLocation == null){
                Toast.makeText(SocioDireccionActivity.this, "No se han obtenido datos de su ubicación actual.", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                intent.putExtra("latitud", String.valueOf(markerMyLocation.getPosition().latitude));
                intent.putExtra("longitud", String.valueOf(markerMyLocation.getPosition().longitude));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    private void updateLocation(){

        if(!checkGPSIsEnabled()){
            showInfoAlert();
        } else {
            if (ActivityCompat.checkSelfPermission(SocioDireccionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(SocioDireccionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            currentLocation = location;

            if (currentLocation != null) {
                createOrUpdateMarkerByLocation(currentLocation);
                zoomToLocation(currentLocation);
            }
        }
    }

    private void zoomToLocation(Location location){

        cameraZoom = new CameraPosition
                .Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)
                .bearing(120)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraZoom));
    }

    @Override
    public void onLocationChanged(Location location) {
        //createOrUpdateMarkerByLocation(location);
    }

    private void createOrUpdateMarkerByLocation(Location location){
        if(markerMyLocation == null){
            markerMyLocation = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .draggable(true)
                    .title("Mi posición actual"));

        }else{
            markerMyLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void createMarkerByRegisteredLocation(){
        if(mLatLngDireccion != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLatLngDireccion.latitude, mLatLngDireccion.longitude))
                    .title("Ubicación registrada"));
        }
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

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        markerMyLocation = marker;
        Toast.makeText(SocioDireccionActivity.this, "Nueva latLon " + marker.getPosition().latitude
                + ", " + marker.getPosition().longitude , Toast.LENGTH_LONG).show();
    }
}
