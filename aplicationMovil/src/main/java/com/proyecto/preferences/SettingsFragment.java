package com.proyecto.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings.Secure;

import com.proyect.movil.R;
import com.proyecto.servicios.Constants;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;

@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment implements
		OnPreferenceChangeListener {

	private SwitchPreference swPref;
	private Context contexto;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contexto = getActivity().getBaseContext();
		addPreferencesFromResource(R.xml.settings);

		// capturar el ID del dispositivo
		String id = Secure.getString(getActivity().getContentResolver(),
				Secure.ANDROID_ID);

		// Settear el ID en el campo correspondiente
		EditTextPreference edtPref = (EditTextPreference) findPreference("idDispositivo");
		edtPref.setSummary(id);
		edtPref.setEnabled(false);

	//	swPref = (SwitchPreference) findPreference("servicio");
	//	swPref.setOnPreferenceChangeListener(this);

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		boolean switched = !((SwitchPreference) preference).isChecked();
		
		if(switched){
			Intent intent = new Intent(contexto, ServicioOvPr.class);
			intent.setAction(Constants.ACTION_RUN_ISERVICE);
			getActivity().startService(intent);
			
	/*		Intent intent2 = new Intent(contexto, ServicioSocios.class);
			intent2.setAction(Constants.ACTION_RUN_ISERVICE);
			getActivity().startService(intent2);		*/
			
		}else{
			//DETENER EL SERVICIO
			Intent intent = new Intent(contexto, ServicioOvPr.class);
			intent.setAction("stop");
			getActivity().stopService(intent);  
			
	/*		Intent intent2 = new Intent(contexto, ServicioSocios.class);
			intent2.setAction("stop");
			getActivity().stopService(intent2);  		*/
		}

		swPref.setChecked(switched);

		return switched;
	}

}
