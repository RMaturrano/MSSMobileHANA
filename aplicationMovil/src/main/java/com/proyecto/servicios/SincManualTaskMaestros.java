package com.proyecto.servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.proyecto.database.Insert;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class SincManualTaskMaestros extends AsyncTask<String, String, Object> {

	private ProgressDialog pd;
	private Context contexto;
	
	//Utils
	private Insert insert; 

	public SincManualTaskMaestros(ProgressDialog pd, Context contexto, String action) {
		this.pd = pd;
		this.contexto = contexto;
	}

	@Override
	protected Object doInBackground(String... params) {
		
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		String codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");

		InvocaWS ws = new InvocaWS(contexto);
		insert = new Insert(contexto);
		
		int contador = 0;
		boolean res = false;
		
		/******************************************************/
		/******************** NUEVO ORDEN *********************/
		/*****************************************************/
		/*****************************************************/
		
		//Envio
		ws.EnviarSociosNegocio(codigoEmpleado);
		
		//Recepcion

		//PRUEBA WEB SERVICE SOAP
		//1. Lista Socio de negocio
		String[] progress = new String[2];
		progress[0] = "Obteniendo socios de negocio";
		progress[1] = "0";
		publishProgress(progress);

		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Socios")){
				res = true;
				insert.insertSocioNegocio(ws.getBusinessPartnersLead(codigoEmpleado), "L");
			}
			else{
				res = insert.insertSocioNegocio(ws.getBusinessPartners(codigoEmpleado), "A");
				putRegistro("Socios");
			}
		}else
			res = insert.insertSocioNegocio(ws.getBusinessPartners(codigoEmpleado), "A");

		if(res){
			contador++;
		}

/*
		//PRUEBA WEB SERVICE REST
		//1. Lista Socio de negocio
		String[] progress = new String[2];
		progress[0] = "Obteniendo socios de negocio";
		progress[1] = "0";
		publishProgress(progress);

		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Socios"))
				res = insert.insertSocioNegocio(ws.getBusinessPartnersLead(codigoEmpleado), "L");
			else{
				res = insert.insertSocioNegocio(ws.getBusinessPartnerApi(codigoEmpleado), "A");
				putRegistro("Socios");
			}
		}else
			res = insert.insertSocioNegocio(ws.getBusinessPartnerApi(codigoEmpleado), "A");

		if(res){
			contador++;
		}
*/


		//2. Lista articulos
		progress[0] = "Obteniendo articulos";
		progress[1] = "1";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Articulos"))
				contador++;
			else{
				res = insert.insertArticulo(ws.getArticulos(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Articulos");
				}
			}
		}else{
			res = insert.insertArticulo(ws.getArticulos(codigoEmpleado));
			if(res)
				contador++;
		}

		
		//3. Almacenes
		progress[0] = "Obteniendo almacenes";
		progress[1] = "2";
		publishProgress(progress);
		res = insert.insertAlmacen(ws.getAlmacen(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//4. Cantidad
		progress[0] = "Obteniendo cantidades";
		progress[1] = "3";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Cantidades"))
				contador++;
			else{
				res = insert.insertCantidad(ws.getCantidades(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Cantidades");
				}
			}
		}else{
			res = insert.insertCantidad(ws.getCantidades(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//5. Lista de precios
		progress[0] = "Obteniendo lista de precios";
		progress[1] = "4";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("ListaPrecios"))
				contador++;
			else{
				res = insert.insertListaPrecio(ws.getListaPrecio(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("ListaPrecios");
				}
			}
		}else{
			res = insert.insertListaPrecio(ws.getListaPrecio(codigoEmpleado));
			if(res){
				contador++;
			}
		}



		//6. Precios
		progress[0] = "Obteniendo precios";
		progress[1] = "5";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Precios"))
				contador++;
			else{
				res = insert.insertPrecios(ws.ObtenerPrecios(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Precios");
				}
			}
		}else{
			res = insert.insertPrecios(ws.ObtenerPrecios(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		progress[0] = "Finalizado";
		progress[1] = "6";
		publishProgress(progress);
		
		return contador;

	}
	
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		
		pd.setIndeterminate(true);
		pd.setMessage(values[0]);
		
		if(Integer.parseInt(values[1])>=1){
			
			if(pd.isIndeterminate())
				pd.setIndeterminate(false);
			
			pd.incrementProgressBy(1);
		}
		
	}

	@Override
	protected void onPostExecute(Object result) {

		int contador = (int) result;
		
		pd.dismiss();
		
		if(contador == 6){
			Toast.makeText(contexto, "Carga de datos exitosa",
					Toast.LENGTH_LONG).show();
		}else if( contador < 6 && contador > 0){
			Toast.makeText(contexto, "Carga incompleta.",
					Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(contexto,
					"No se pudo establecer conexion con el servidor",
					Toast.LENGTH_LONG).show();
		}

	}


	private boolean checkRegistros(String tipoEntidad){

		Cursor cursorCheck = insert.db.rawQuery("SELECT Estado FROM TB_AUDITORIA " +
				"where TipoEntidad Like '"+tipoEntidad+"' " +
				"and FechaInsercion = CURRENT_DATE", null);

		if (cursorCheck.moveToFirst()) {
			do {
				if(cursorCheck.getString(cursorCheck.getColumnIndex("Estado")).equalsIgnoreCase("True")){
					return true;
				}
			} while (cursorCheck.moveToNext());
		}else{
			return false;
		}

		if (cursorCheck != null && !cursorCheck.isClosed())
			cursorCheck.close();

		return false;

	}


	private void putRegistro(String tipoEntidad){
		insert.db.execSQL("insert into TB_AUDITORIA(TipoEntidad,Estado) values('"+tipoEntidad+"','True')");
	}


}
