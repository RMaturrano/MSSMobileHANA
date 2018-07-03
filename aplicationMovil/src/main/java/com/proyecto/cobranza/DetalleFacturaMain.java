package com.proyecto.cobranza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.proyect.movil.R;
import com.proyecto.dao.FacturaDAO;
import com.proyecto.entregas.EntregaDetalleActivity;
import com.proyecto.facturas.util.FacturaBuscarBean;
import com.proyecto.incidencias.IncidenciaActivity;
import com.proyecto.notacredito.NotaCreditoActivity;
import com.proyecto.utils.PagerAdapterDetFactura;

public class DetalleFacturaMain extends AppCompatActivity{

    private FloatingActionButton fabAddActions;
    private FloatingActionButton fabAddIncidencia;
    private FloatingActionButton fabAddNotaCredito;
    private LinearLayout lytFabAddIncidencia;
    private LinearLayout lytFabAddNotaCredito;
	public static String idNroFactura = null;
	public static String idSocioNegocio = null;
    private boolean mFabExpanded;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_factura_cliente);
		
		Intent myIntent = getIntent(); // gets the previously created intent
		
		if(myIntent.getStringExtra("id")!= null){
			idNroFactura = myIntent.getStringExtra("id");
		}

		//TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(myToolbar);
	    
	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();

	    // Enable the Up button
	    ab.setDisplayHomeAsUpEnabled(true);
	  //TOOLBAR

        fabAddActions = (FloatingActionButton) findViewById(R.id.fabAddActionFacturaDetalle);
        fabAddIncidencia = (FloatingActionButton) findViewById(R.id.fabAddIncidenciaFacturaDetalle);
        fabAddNotaCredito = (FloatingActionButton) findViewById(R.id.fabAddNotaCreditoFacturaDetalle);
        lytFabAddIncidencia = (LinearLayout) findViewById(R.id.lytFabAddIncidenciaFacturaDetalle);
        lytFabAddNotaCredito = (LinearLayout) findViewById(R.id.lytFabActionAddNotaCreditoFacturaDetalle);

        fabAddActions.setOnClickListener(onClickListenerFabActions);
        fabAddIncidencia.setOnClickListener(onClickListenerFabAddIncidencia);
        fabAddNotaCredito.setOnClickListener(onClickListenerFabAddNotaCredito);
        closeSubMenusFab();
	    
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Cabecera"));
        tabLayout.addTab(tabLayout.newTab().setText("Contenidos"));
        tabLayout.addTab(tabLayout.newTab().setText("Logística"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterDetFactura adapter = new PagerAdapterDetFactura
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
 
            }
 
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
 
            }
        });
 
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
        	case 16908332:
        		
            	finish();
        		
        		return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private View.OnClickListener onClickListenerFabAddIncidencia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DetalleFacturaMain.this, IncidenciaActivity.class);
            intent.putExtra(IncidenciaActivity.KEY_PAR_ORIGEN, IncidenciaActivity.FACTURA);
            intent.putExtra(IncidenciaActivity.KEY_PAR_FACTURA, idNroFactura);
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickListenerFabAddNotaCredito = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DetalleFacturaMain.this, NotaCreditoActivity.class);
            intent.putExtra(NotaCreditoActivity.KEY_PARAM_FACTURA, new FacturaDAO().buscar(idNroFactura));
            startActivity(intent);
        }
    };

    private void closeSubMenusFab(){
        lytFabAddIncidencia.setVisibility(View.INVISIBLE);
        lytFabAddNotaCredito.setVisibility(View.INVISIBLE);

        fabAddActions.setImageResource(R.drawable.ic_add_white_36dp);
        mFabExpanded = false;
    }

    private void openSubMenusFab(){
        lytFabAddIncidencia.setVisibility(View.VISIBLE);
        lytFabAddNotaCredito.setVisibility(View.VISIBLE);

        fabAddActions.setImageResource(R.drawable.ic_close_24dp);
        mFabExpanded = true;
    }
	
}
