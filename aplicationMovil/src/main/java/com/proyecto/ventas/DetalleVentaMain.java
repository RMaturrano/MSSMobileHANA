package com.proyecto.ventas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.utils.PagerAdapterDetOrd;
import com.proyecto.utils.Variables;

public class DetalleVentaMain extends AppCompatActivity{
	
	public static String idNroVenta = null;
	public static String idSocioNegocio = null;
	private Context contexto;
	private SharedPreferences pref;
	private String movilEditar = "";
	private String estado = "";
	private String estadoTransaccion = "";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_pedido_cliente);
		
		Intent myIntent = getIntent(); // gets the previously created intent
		
		if(myIntent.getStringExtra("id")!= null){
			idNroVenta = myIntent.getStringExtra("id");
		}
		
		if(myIntent.getStringExtra("estado") != null){
			estado = myIntent.getStringExtra("estado");
		}
		
		if(myIntent.getStringExtra("estadoTransaccion") != null){
			estadoTransaccion = myIntent.getStringExtra("estadoTransaccion");
		}
		
		contexto = this;
		
		//TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(myToolbar);
	    
	    // Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();

	    // Enable the Up button
	    ab.setDisplayHomeAsUpEnabled(true);
	  //TOOLBAR
	    
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Cabecera"));
        tabLayout.addTab(tabLayout.newTab().setText("Contenidos"));
        tabLayout.addTab(tabLayout.newTab().setText("Logística"));
        tabLayout.addTab(tabLayout.newTab().setText("Finanzas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterDetOrd adapter = new PagerAdapterDetOrd
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
        
        pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
        movilEditar = pref.getString(Variables.MOVIL_EDITAR, "");
 
        
      //FLOATING BUTTON EDITAR
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	if(!OrdenVentaFragment.shouldThreadContinueoV){
            		
            			Intent actualizarVenta = new Intent(contexto,MainVentas.class);
                    	actualizarVenta.putExtra("action", "update");
                    	actualizarVenta.putExtra("clave", idNroVenta);
                    	startActivity(actualizarVenta);
                    	finish();
                    	
            	}else
            		Toast.makeText(contexto, "Los registros se están sincronizando, espere un momento por favor", Toast.LENGTH_SHORT).show();
                
            }
        });
        
        if(!estadoTransaccion.equals("") && !movilEditar.equals("")){
        	 if(movilEditar.equalsIgnoreCase("Y")
             		&& Integer.parseInt(estadoTransaccion) <= 2){
             	fab.setVisibility(FloatingActionButton.VISIBLE);
             }else{
             	fab.setVisibility(FloatingActionButton.INVISIBLE);
             }
        }else{
        	fab.setVisibility(FloatingActionButton.INVISIBLE);  //CMBIAR A INVISIBLE
        }
       
        	
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
	
	

}
