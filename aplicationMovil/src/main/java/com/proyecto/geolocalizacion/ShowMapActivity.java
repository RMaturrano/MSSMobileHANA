package com.proyecto.geolocalizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.entregas.fragments.ListaEntregaFragment;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.visitas.fragment.ListaVisitaHoyFragment;

import java.util.ArrayList;
import java.util.List;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String KEY_PARAM_LATITUD = "latitud";
    public static final String KEY_PARAM_LONGITUD = "longitud";

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private CameraPosition mCameraZoom;
    private LatLngBounds.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbShowMapActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_activity_show_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.showMap);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(GoogleMap googleMap) {
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
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMapLoadedCallback(onMapLoadedCallback);

        Intent openIntent = getIntent();

        if(openIntent != null && openIntent.getExtras() != null){

            //region DIRECCIONES_DESDE_VISITAS
            if(openIntent.getExtras().containsKey(ListaVisitaHoyFragment.KEY_PARM_DIRECTION)){
                ArrayList<DireccionBuscarBean> directions = openIntent.getParcelableArrayListExtra(ListaVisitaHoyFragment.KEY_PARM_DIRECTION);

                mBuilder = LatLngBounds.builder();

                for (DireccionBuscarBean dir: directions) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    //markerOptions.title(dir.getCodigoCliente());
                    //markerOptions.snippet(dir.getCodigo());

                    TextView text = new TextView(this);
                    text.setText(dir.getCodigoCliente() + "\n" + dir.getNombreCliente() + "\n" + dir.getCodigo());
                    text.setTextColor(Color.parseColor("#FAFAFA"));
                    text.setTextSize(18f);
                    text.setTypeface(null, Typeface.BOLD);
                    text.setGravity(Gravity.CENTER);
                    IconGenerator generator = new IconGenerator(this);
                    generator.setContentView(text);
                    generator.setStyle(IconGenerator.STYLE_RED);
                    Bitmap icon = generator.makeIcon();
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

                    LatLng latLng = new LatLng(Double.parseDouble(dir.getLatitud()), Double.parseDouble(dir.getLongitud()));
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions).showInfoWindow();
                    mBuilder.include(latLng);
                }
            }
            //endregion
            else if(openIntent.getExtras().containsKey(ListaEntregaFragment.KEY_PARM_DIRECTION)){
                List<EntregaBean> entregas = openIntent.getParcelableArrayListExtra(ListaEntregaFragment.KEY_PARM_DIRECTION);

                mBuilder = LatLngBounds.builder();

                for (EntregaBean entrega: entregas) {
                    MarkerOptions markerOptions = new MarkerOptions();

                    TextView text = new TextView(this);
                    text.setText(entrega.getSocioNegocio() + "\n" + entrega.getSocioNegocioNombre() + "\n" + entrega.getReferencia());
                    text.setTextColor(Color.parseColor("#FAFAFA"));
                    text.setTextSize(18f);
                    text.setTypeface(null, Typeface.BOLD);
                    text.setGravity(Gravity.CENTER);
                    IconGenerator generator = new IconGenerator(this);
                    generator.setContentView(text);
                    generator.setStyle(IconGenerator.STYLE_RED);
                    Bitmap icon = generator.makeIcon();
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

                    LatLng latLng = new LatLng(Double.parseDouble(entrega.getDireccionEntregaLatitud()),
                            Double.parseDouble(entrega.getDireccionEntregaLongitud()));
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions).showInfoWindow();
                    mBuilder.include(latLng);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    GoogleMap.OnMapLoadedCallback onMapLoadedCallback = new GoogleMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            if(mBuilder != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 30));
        }
    };

}
