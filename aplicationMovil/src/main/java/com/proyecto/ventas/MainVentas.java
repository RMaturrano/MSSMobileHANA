package com.proyecto.ventas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.proyect.movil.R;

public class MainVentas extends AppCompatActivity{
	
	private Fragment fragment = new OrdenVentaFragment();
	public static String action = "";
	public static String claveVenta = "";
	public static String codigoArticulo = "";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedido_cliente_box);
		
		Intent myIntent = getIntent(); // gets the previously created intent

		if (myIntent.getStringExtra("action") != null) {
			action = myIntent.getStringExtra("action");
			claveVenta = myIntent.getStringExtra("clave");
		}
		
        
		//TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(myToolbar);
		// Get a support ActionBar corresponding to this toolbar
	    ActionBar ab = getSupportActionBar();
	    // Enable the Up button
	    ab.setDisplayHomeAsUpEnabled(true);
	  //TOOLBAR
		
        if (findViewById(R.id.box) != null) {

            if (savedInstanceState != null) {
                
            	fragment = getFragmentManager().getFragment(
                        savedInstanceState, "OrdenVentaFrg");
            	
            }
            
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.box, fragment,"OrdenVentaFrg");
            transaction.commit();

        }
		
	}
	
	
//	 private void attachFragment(Bundle savedInstanceState) {
//        if (savedInstanceState == null){
//           testFragment = TestFragment.newInstance();
//           getSupportFragmentManager().beginTransaction()
//           .replace(R.id.fragment_container, testFragment,"testFragment").commit();
//	    } else {
//	      testFragment =  (TestFragment)getSupportFragmentManager().findFragmentByTag("testFragment");
//	    }
//	 }
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		getFragmentManager().putFragment(outState, "OrdenVentaFrg", fragment);
		
	}
	
	
	
	

}
