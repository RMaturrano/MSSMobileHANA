package com.proyecto.cobranza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.proyect.movil.R;

public class MainCobranzas extends AppCompatActivity{
	
	private Fragment fragment = new CobranzaFragment();
	public static String numeroFactura = "";
	public static String action = "";
	public static String clavePago = "";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cobranza_cliente_box);
		
		Intent myIntent = getIntent(); // gets the previously created intent

		if (myIntent.getStringExtra("action") != null) {
			action = myIntent.getStringExtra("action");
			clavePago = myIntent.getStringExtra("clave");
		}
		
		//TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(myToolbar);
		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();
		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
		//TOOLBAR
		
		
		if (findViewById(R.id.box_cobranza) != null) {

            if (savedInstanceState != null) {
                
            	fragment = getSupportFragmentManager().getFragment(
                        savedInstanceState, "mFragmentCobranzas");
            	
            }
            
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.box_cobranza, fragment, "mFragmentCobranzas");
            transaction.commit();

        }
		
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mFragmentCobranzas", fragment);

	}
	
	

}
