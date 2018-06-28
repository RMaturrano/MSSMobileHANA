package com.proyecto.entregas;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.devoluciones.DevolucionActivity;
import com.proyecto.entregas.adapter.tablayout.TBAdapterEntregaDetalle;
import com.proyecto.incidencias.IncidenciaActivity;

public class EntregaDetalleActivity extends AppCompatActivity {

    public static String KEY_PARAM_ENTREGA;

    private FloatingActionButton fabAddActions;
    private FloatingActionButton fabAddDevolucion;
    private FloatingActionButton fabAddIncidencia;
    private LinearLayout lytFabAddNotaCredito;
    private LinearLayout lytFabAddIncidencia;
    private boolean mFabExpanded;
    private EntregaBean mEntrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_detalle);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_ENTREGA)){
                mEntrega = getIntent().getExtras().getParcelable(KEY_PARAM_ENTREGA);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbEntregaDetalle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Info detallada");

        fabAddActions = (FloatingActionButton) findViewById(R.id.fabAddActionEntregaDetalle);
        fabAddDevolucion = (FloatingActionButton) findViewById(R.id.fabAddNotaCreditoEntregaDetalle);
        fabAddIncidencia = (FloatingActionButton) findViewById(R.id.fabAddIncidenciaEntregaDetalle);
        lytFabAddNotaCredito = (LinearLayout) findViewById(R.id.lytFabActionAddNotaCreditoEntregaDetalle);
        lytFabAddIncidencia = (LinearLayout) findViewById(R.id.lytFabAddIncidenciaEntregaDetalle);

        fabAddActions.setOnClickListener(onClickListenerFabActions);
        fabAddDevolucion.setOnClickListener(onClickListenerFabAddNotaCredito);
        fabAddIncidencia.setOnClickListener(onClickListenerFabAddIncidencia);
        closeSubMenusFab();

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_ENTREGA)){

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabEntregaDetalle);
                tabLayout.addTab(tabLayout.newTab().setText("Cabecera"));
                tabLayout.addTab(tabLayout.newTab().setText("Contenido"));
                tabLayout.addTab(tabLayout.newTab().setText("Logistica"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                final ViewPager viewPager = (ViewPager) findViewById(R.id.pgrEntregaDetalle);
                final TBAdapterEntregaDetalle adapter = new TBAdapterEntregaDetalle
                        (getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                        if(tab.getPosition() == 1){
                            fabAddActions.setVisibility(View.INVISIBLE);
                        }else{
                            fabAddActions.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
            }
        }
    }

    private View.OnClickListener onClickListenerFabActions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFabExpanded == true){
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        }
    };

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

    private View.OnClickListener onClickListenerFabAddNotaCredito = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EntregaDetalleActivity.this, DevolucionActivity.class);
            intent.putExtra(DevolucionActivity.KEY_PARAM_ENTREGA, mEntrega);
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickListenerFabAddIncidencia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EntregaDetalleActivity.this, IncidenciaActivity.class);
            intent.putExtra(IncidenciaActivity.KEY_PAR_ORIGEN, IncidenciaActivity.ENTREGA);
            intent.putExtra(IncidenciaActivity.KEY_PAR_CLIENTE, mEntrega.getSocioNegocio());
            startActivity(intent);
        }
    };

    private void closeSubMenusFab(){
        lytFabAddNotaCredito.setVisibility(View.INVISIBLE);
        lytFabAddIncidencia.setVisibility(View.INVISIBLE);

        fabAddActions.setImageResource(R.drawable.ic_add_white_36dp);
        mFabExpanded = false;
    }

    private void openSubMenusFab(){
        lytFabAddNotaCredito.setVisibility(View.VISIBLE);
        lytFabAddIncidencia.setVisibility(View.VISIBLE);

        fabAddActions.setImageResource(R.drawable.ic_close_24dp);
        mFabExpanded = true;
    }
}
