package com.proyecto.broadcast;

import com.proyecto.servicios.Constants;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent servicioOvPr = new Intent(context, ServicioOvPr.class);
		servicioOvPr.setAction(Constants.ACTION_RUN_ISERVICE);
	    context.startService(servicioOvPr);
	    
	 /*   Intent servicioSocios = new Intent(context, ServicioSocios.class);
	    servicioSocios.setAction(Constants.ACTION_RUN_ISERVICE);
	    context.startService(servicioSocios);		*/
		
	}
	
}
