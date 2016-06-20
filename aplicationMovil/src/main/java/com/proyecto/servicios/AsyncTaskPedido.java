package com.proyecto.servicios;

import android.content.Context;
import android.os.AsyncTask;

import com.proyecto.database.Insert;
import com.proyecto.ws.InvocaWS;

public class AsyncTaskPedido extends AsyncTask<String, Void, Object>{

	private Insert insert; 
	private Context contexto;
	private String codEmp;
	
	public AsyncTaskPedido(Context contexto, String codEmp) {
		this.contexto = contexto;
		this.codEmp = codEmp;
		insert = new Insert(contexto);
	}
	
	@Override
	protected Object doInBackground(String... params) {

		InvocaWS ws = new InvocaWS(contexto);
		insert.insertOrUpdateOrdenVenta(ws.getOrders(codEmp));
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		insert.close();
		super.onPostExecute(result);
	}

}
