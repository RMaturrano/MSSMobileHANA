package com.proyecto.servicios;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.database.Delete;
import com.proyecto.database.Insert;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class ServicioSociosWithTask extends IntentService{
	
	private Context contexto;
	private Insert insert;
	private Delete delete;
	private InvocaWS ws;

	public ServicioSociosWithTask() {
		super("IntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (action.equals(Constants.ACTION_RUN_ISERVICE)) {
				
				handleActionRun();
				
			}
		}
	}
	
	private void handleActionRun() {

			contexto = getApplication().getApplicationContext();

				
					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(contexto);
					String codEmp = pref.getString(Variables.CODIGO_EMPLEADO, "");
					
					insert = new Insert(contexto);
					delete = new Delete(contexto);
					ws = new InvocaWS(contexto);
					
					ArrayList<SocioNegocioBean> lista = ws.getBusinessPartners(codEmp);
					if(lista != null){
//						delete.deleteSocioNegocio();
						insert.insertOrUpdateSocioNegocio(lista);
					}
				

	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
	}
	
}
