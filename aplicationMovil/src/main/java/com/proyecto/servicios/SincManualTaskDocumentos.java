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

public class SincManualTaskDocumentos extends AsyncTask<String, String, Object> {

	private ProgressDialog pd;
	private Context contexto;
	
	//Utils
	private Insert insert; 

	public SincManualTaskDocumentos(ProgressDialog pd, Context contexto, String action) {
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
		
		//Envio
		ws.EnviarPedidoCliente(codigoEmpleado);

		//1. Lista ordenes de venta
		String[] progress = new String[2];
		progress[0] = "Obteniendo pedidos";
		progress[1] = "0";
		publishProgress(progress);
		res = insert.insertOrdenVenta(ws.getOrders(codigoEmpleado));
		if(res){
			Log.i("Orden venta","true");
			contador++;
		}
			
		
		//2. Lista facturas
		progress[0] = "Obteniendo facturas";
		progress[1] = "1";
		publishProgress(progress);
		if(checkRegistros("Facturas"))
			contador++;
		else{
			res = insert.insertFacturas(ws.getFacturas(codigoEmpleado));
			if(res){
				contador++;
				putRegistro("Facturas");
			}
		}


		//Envio
		ws.EnviarPagoCliente(codigoEmpleado);
		
		//3. Lista pagos
		progress[0] = "Obteniendo pagos recibidos";
		progress[1] = "2";
		publishProgress(progress);
		res = insert.insertPagoCliente(ws.getPagoCliente(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//4. Lista de estados de cuenta
		progress[0] = "Obteniendo estados de cuenta de socios";
		progress[1] = "3";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("EstadoCuentaSocio"))
				contador++;
			else{
				res = insert.insertEstadoCuentaCliente(ws.ObtenerEstadoCuentaSocios(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("EstadoCuentaSocio");
				}
			}
		}else{
			res = insert.insertEstadoCuentaCliente(ws.ObtenerEstadoCuentaSocios(codigoEmpleado));
			if(res){
				contador++;
			}
		}

		//4. Lista de notas de credito para reporte saldos por vendedor
		progress[0] = "Obteniendo notas de credito";
		progress[1] = "4";
		publishProgress(progress);
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("ReporteNC"))
				contador++;
			else{

				res = insert.insertNotaCredito(ws.ObtenerReporteNotaCredito(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("ReporteNC");
				}
			}
		}else{
			res = insert.insertNotaCredito(ws.ObtenerReporteNotaCredito(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		progress[0] = "Finalizado";
		progress[1] = "5";
		publishProgress(progress);
		
		return contador;

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		
		pd.setMessage(values[0]);
		
		if(Integer.parseInt(values[1])>= 1)
			pd.incrementProgressBy(1);
		
	}
	
	@Override
	protected void onPostExecute(Object result) {

		pd.dismiss();
		
		int res = (int) result;

		if(res == 5){
			Toast.makeText(contexto, "Carga de datos completa.",
					Toast.LENGTH_LONG).show();
		}else if( res < 5 && res > 0){
			Toast.makeText(contexto, "Se cargaron "+ res +" documentos. ",
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
		insert.db.execSQL("delete from TB_AUDITORIA where FechaInsercion <> CURRENT_DATE");
	}

	
}
