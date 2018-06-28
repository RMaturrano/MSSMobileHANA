package com.proyecto.sociosnegocio;

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
import com.proyecto.geolocalizacion.MapsActivity;

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == MapsActivity.REQUEST_MAPAS){
			if(data.getExtras().containsKey(MapsActivity.KEY_PARAM_LATITUD) &&
					data.getExtras().containsKey(MapsActivity.KEY_PARAM_LONGITUD)){
				DireccionSocioNegocio agregarDireccion = (DireccionSocioNegocio) getSupportFragmentManager()
						.findFragmentByTag(DireccionSocioNegocio.TAG_AGREGAR_DIRECCION);
				if(agregarDireccion != null) {
					agregarDireccion.actualizarUbicacion(data.getExtras().getDouble(MapsActivity.KEY_PARAM_LATITUD),
												 data.getExtras().getDouble(MapsActivity.KEY_PARAM_LONGITUD));
				}
			}
		}else if(resultCode == RESULT_OK && requestCode == PhoneContactsListActivity.REQUEST_CONTACT_TLF){
			if(data.getExtras().containsKey(PhoneContactsListActivity.KEY_PARAM_NAME) &&
					data.getExtras().containsKey(PhoneContactsListActivity.KEY_PARAM_PHONE)){
				ListaContactosFragment lstContactos = (ListaContactosFragment) getSupportFragmentManager()
						.findFragmentByTag(ListaContactosFragment.TAG_LISTA_CONTACTOS);
				if(lstContactos != null){
					lstContactos.agregarNuevoContacto(data.getExtras().getString(PhoneContactsListActivity.KEY_PARAM_NAME),
							data.getExtras().getString(PhoneContactsListActivity.KEY_PARAM_PHONE));
				}
			}
		}
	}
}
