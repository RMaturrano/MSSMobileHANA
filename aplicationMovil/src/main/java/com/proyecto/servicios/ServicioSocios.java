package com.proyecto.servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.Insert;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class ServicioSocios extends IntentService{
	
	private Context contexto;
	private Insert insert;
//	private Delete delete;
	private InvocaWS ws;
	private boolean shouldContinue = false;

	public ServicioSocios() {
		super("ServicioUOvPr");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (action.equals(Constants.ACTION_RUN_ISERVICE)) {
				
				shouldContinue = true;
				handleActionRun();
				
			}
		}
	}
	
	private void handleActionRun() {
		try {

			contexto = getApplication().getApplicationContext();

			while(shouldContinue) {
				
				if(getWifi() || getMovil() && getConnectionFast()){
					
					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(contexto);
					String codEmp = pref.getString(Variables.CODIGO_EMPLEADO, "");
					
					insert = new Insert(contexto);
//					delete = new Delete(contexto);
					ws = new InvocaWS(contexto);
					
					insert.insertOrUpdateSocioNegocio(ws.getBusinessPartnersLead(codEmp));
					
					insert.close();
//					delete.close();
				}
				
				Thread.sleep(60000);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		shouldContinue = false;
		Toast.makeText(contexto, "Servicio detenido...", Toast.LENGTH_SHORT).show();
		stopSelf();
	}
	
	
	
	private boolean getWifi(){
		return Connectivity.isConnectedWifi(contexto);
	}
	
	private boolean getMovil(){
		return Connectivity.isConnectedMobile(contexto);
	}
	
	private boolean getConnectionFast(){
		return Connectivity.isConnectedFast(contexto);
	}
	
}
