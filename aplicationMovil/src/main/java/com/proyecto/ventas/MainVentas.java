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
import com.proyecto.sociosnegocio.ClienteBuscarActivity;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;

public class MainVentas extends AppCompatActivity{
	
	private Fragment fragment = new OrdenVentaFragment();
	public static String action = "";
	public static String claveVenta = "";
	public static String codigoArticulo = "";
	public static int position = -1;
	public static String MAIN_FRAGMENT = "OrdenVentaFrg";
	
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
                        savedInstanceState, MAIN_FRAGMENT);
            }
            
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.box, fragment,MAIN_FRAGMENT);
            transaction.commit();

        }

        position = -1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE){
			ClienteBuscarBean mClienteSeleccionado = data.getParcelableExtra(ClienteBuscarActivity.KEY_PARAM_CLIENTE);
			OrdenVentaFragment ovFragment = (OrdenVentaFragment) getFragmentManager()
					.findFragmentByTag(MAIN_FRAGMENT);
			if(ovFragment != null) {
				ovFragment.onClientSelected(mClienteSeleccionado);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		getFragmentManager().putFragment(outState, "OrdenVentaFrg", fragment);
		
	}
	
	
	
	

}
