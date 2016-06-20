package com.proyecto.sociosnegocio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.proyect.movil.R;

public class MainSocioNegocio extends AppCompatActivity{
	
	private Fragment fragment = new SocioNegocioFragment();
	public static String idDireccion = "";
	public static String idContacto = "";
	public static String idDistrito = "";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socio_negocio_box);
        
		idDireccion = "";
		idContacto = "";
		
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
                
            	fragment = getSupportFragmentManager().getFragment(
                        savedInstanceState, "mFragmentSocioNuevo");
            	
            }
            
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.box, fragment, "mFragmentSocioNuevo");
            transaction.commit();

        }
		
	}




	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mFragmentSocioNuevo", fragment);

	}
	


}
