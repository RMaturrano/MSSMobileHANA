package com.proyecto.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.proyect.movil.R;
import com.proyecto.bean.EmpresaBean;
import com.proyecto.servicios.Constants;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment implements
		OnPreferenceChangeListener {

	private SwitchPreference swPref;
	private EditTextPreference mEdtServidor;
	private EditTextPreference mEdtPuerto;
	private ListPreference mListPref;
	private Context contexto;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contexto = getActivity().getBaseContext();
		addPreferencesFromResource(R.xml.settings);

		// capturar el ID del dispositivo
		String id = Secure.getString(getActivity().getContentResolver(),
				Secure.ANDROID_ID);

		mEdtServidor = (EditTextPreference) findPreference("ipServidor");
		mEdtPuerto = (EditTextPreference) findPreference("puertoServidor");

		// Settear el ID en el campo correspondiente
		EditTextPreference edtPref = (EditTextPreference) findPreference("idDispositivo");
		edtPref.setSummary(id);
		edtPref.setEnabled(false);

		mListPref = (ListPreference) findPreference("sociedades");
		setListPreferenceData();
		mListPref.setOnPreferenceClickListener(onListPreferenceClick);

	//	swPref = (SwitchPreference) findPreference("servicio");
	//	swPref.setOnPreferenceChangeListener(this);

	}

	@Override
	public void onStart() {
		super.onStart();
		setListPreferenceData();
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

	Preference.OnPreferenceClickListener onListPreferenceClick = new Preference.OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			setListPreferenceData();
			return true;
		}
	};

	private void setListPreferenceData(){

		final List<String> listEntries = new ArrayList<>();
		final List<String> listValues = new ArrayList<>();

		final String ws_ruta = "http://" + mEdtServidor.getText() + ":" +mEdtPuerto.getText() +
				  			   "/MSS_MOBILE/service/company/getCompany.xsjs";

		StringRequest stringRequest = new StringRequest(Request.Method.GET, ws_ruta,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							if(jsonObject.getString("ResponseStatus").equals("Success")){
								JSONArray jsonArray = jsonObject.getJSONObject("Response")
																			.getJSONObject("message")
																			.getJSONArray("value");
								int size = jsonArray.length();

								if(size > 0) {
									for (int i = 0; i < size; i++) {
										JSONObject jsonObj = jsonArray.getJSONObject(i);

										listEntries.add(jsonObj.getString("descripcion"));
										listValues.add(jsonObj.getString("id"));
									}
								}else{
									listEntries.add("No se obtuvieron resultados");
									listValues.add("-1");
								}

								mListPref.setEntries(listEntries.toArray(new CharSequence[listEntries.size()]));
								mListPref.setDefaultValue(1);
								mListPref.setEntryValues(listValues.toArray(new CharSequence[listValues.size()]));
							}
						} catch (Exception e){

							listEntries.add("No se obtuvieron resultados");
							listValues.add("-1");

							mListPref.setEntries(listEntries.toArray(new CharSequence[listEntries.size()]));
							mListPref.setDefaultValue(1);
							mListPref.setEntryValues(listValues.toArray(new CharSequence[listValues.size()]));

							Toast.makeText(contexto, "Se produjo un error al intentar procesar la respuesta del servidor: " + e.getMessage(), Toast.LENGTH_SHORT);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				listEntries.add("NO DATA FOUND");
				listValues.add("-1");

				mListPref.setEntries(listEntries.toArray(new CharSequence[listEntries.size()]));
				mListPref.setDefaultValue(1);
				mListPref.setEntryValues(listValues.toArray(new CharSequence[listValues.size()]));

				Toast.makeText(contexto, "Se produjo un error al intentar conectar con el servidor: " + error.getMessage(), Toast.LENGTH_SHORT);
			}
		});

		VolleySingleton.getInstance(contexto).addToRequestQueue(stringRequest);
	}

}
