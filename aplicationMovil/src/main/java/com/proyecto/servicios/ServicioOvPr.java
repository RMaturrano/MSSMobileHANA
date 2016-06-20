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

public class ServicioOvPr extends IntentService{
	
	private Context contexto;
	private Insert insert;
//	private Delete delete;
	private InvocaWS ws;
	private boolean shouldContinue = false;

	public ServicioOvPr() {
		super("ServicioOvPr");
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
					
//					ws.EnviarPedidoCliente(codEmp);
//					ws.EnviarPagoCliente(codEmp);
					
					insert.insertOrUpdateOrdenVenta(ws.getOrders(codEmp));
					insert.insertOrUpdatePagoCliente(ws.getPagoCliente(codEmp));
					
					insert.close();
//					delete.close();
				}
				
				Thread.sleep(15000);
	
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
