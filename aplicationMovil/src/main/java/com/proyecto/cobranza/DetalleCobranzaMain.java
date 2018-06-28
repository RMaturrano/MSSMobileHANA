package com.proyecto.cobranza;

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
import com.proyecto.utils.PagerAdapterDetCobranza;
import com.proyecto.utils.Variables;

import org.json.JSONException;
import org.json.JSONObject;

public class DetalleCobranzaMain extends AppCompatActivity{

	public static String idNroCobranza = null;
	public static String idSocioNegocio = null;
	private Context contexto;
	private SharedPreferences pref;
	private String movilEditar = "";
	private String estadoTransaccion = "";
	private FloatingActionButton fabEditar;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_pago_cliente);
		contexto = this;
		
		Intent myIntent = getIntent(); // gets the previously created intent
		
		if(myIntent.getStringExtra("id")!= null){
			idNroCobranza = myIntent.getStringExtra("id");
		}
		
		if(myIntent.getStringExtra("estadoTransaccion") != null){
			estadoTransaccion = myIntent.getStringExtra("estadoTransaccion");
		}
		
		
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
        tabLayout.addTab(tabLayout.newTab().setText("Tipo de pago"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapterDetCobranza adapter = new PagerAdapterDetCobranza
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
        
     // FLOATING BUTTON
		fabEditar = (FloatingActionButton) findViewById(R.id.fab);
		fabEditar.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				
 				if(!CobranzaFragment.shouldThreadContinue){
 					Intent actualizarPago = new Intent(contexto, MainCobranzas.class);
 	 				actualizarPago.putExtra("action", "update");
 	 				actualizarPago.putExtra("clave", idNroCobranza);
 	            	startActivity(actualizarPago);
 	            	finish();
 				}else
 					Toast.makeText(contexto, "Los registros se están sincronizando, espere por favor...", Toast.LENGTH_SHORT).show();
 	
 				
 			}
 		});
		fabEditar.setVisibility(View.INVISIBLE);
	}


	@Override
	protected void onStart() {
		super.onStart();

		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);

		String permisosMenu = pref.getString(Variables.MENU_COBRANZAS, null);
		if (permisosMenu != null) {
			try {
				JSONObject permisos = new JSONObject(permisosMenu);
				movilEditar = permisos.getString(Variables.MOVIL_EDITAR);

				if(movilEditar != null && !movilEditar.trim().equals("")){
					if (!estadoTransaccion.equals("") && !movilEditar.equals("")) {
						if (movilEditar.equalsIgnoreCase("Y")
								&& Integer.parseInt(estadoTransaccion) <= 2) {
							fabEditar.setVisibility(FloatingActionButton.VISIBLE);
						}
					}
				}
			} catch (Exception e) {
				Toast.makeText(this, "Ocurrió un error intentando obtener los permisos del menú", Toast.LENGTH_SHORT).show();
			}
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
