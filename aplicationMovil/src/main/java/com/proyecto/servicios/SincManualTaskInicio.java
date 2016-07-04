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

public class SincManualTaskInicio extends AsyncTask<String, String, Object> {

	private ProgressDialog pd;
	private Context contexto;
	
	//Utils
	private Insert insert; 
	

	public SincManualTaskInicio(ProgressDialog pd, Context contexto, String action) {
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

		//1. Lista pais
		publishProgress("Obteniendo paises");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Pais"))
				contador++;
			else{
				res = insert.insertPais(ws.getPais(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Pais");
				}
			}
		}else{
			res = insert.insertPais(ws.getPais(codigoEmpleado));
			if(res)
				contador++;
		}


		
		//2. Lista departamento
		publishProgress("Obteniendo departamentos");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Departamento"))
				contador++;
			else{
				res = insert.insertDepartamento(ws.ObtenerDepartamentos(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Departamento");
				}
			}
		}else{
			res = insert.insertDepartamento(ws.ObtenerDepartamentos(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//3. Lista provincias
		publishProgress("Obteniendo provincias");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Provincia"))
				contador++;
			else{
				res = insert.insertProvincias(ws.ObtenerProvincias(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Provincia");
				}
			}
		}else{
			res = insert.insertProvincias(ws.ObtenerProvincias(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//4. Lista distritos
		publishProgress("Obteniendo distritos");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Distrito"))
				contador++;
			else{
				res = insert.insertDistritos(ws.ObtenerDistritos(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Distrito");
				}
			}
		}else{
			res = insert.insertDistritos(ws.ObtenerDistritos(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//5. Lista calles
		publishProgress("Obteniendo calles");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Calle"))
				contador++;
			else{
				res = insert.insertCalles(ws.ObtenerCalles(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Calle");
				}
			}
		}else{
			res = insert.insertCalles(ws.ObtenerCalles(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//6. Lista de bancos
		publishProgress("Obteniendo bancos");
		res = insert.insertBancos(ws.ObtenerBancos(codigoEmpleado));
		if(res){
			contador++;
		}

		//7. Lista de cuentas
		publishProgress("Obteniendo cuentas");
		res = insert.insertCuentas(ws.ObtenerCuentas(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//8. Lista MONEDAS
		publishProgress("Obteniendo monedas");
		res = insert.insertMoneda(ws.getMoneda(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//9. Condicion pago
		publishProgress("Obteniendo condiciones de pago");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("CondicionPago"))
				contador++;
			else{
				res = insert.insertCondicionPago(ws.getCondicionPago(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("CondicionPago");
				}
			}
		}else{
			res = insert.insertCondicionPago(ws.getCondicionPago(codigoEmpleado));
			if(res){
				contador++;
			}
		}



		//10. Lista indicadores
		publishProgress("Obteniendo indicadores");
		res = insert.insertIndicador(ws.getIndicador(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//11. Lista IMPUESTOS
		publishProgress("Obteniendo impuestos");
		res = insert.insertImpuesto(ws.getImpuesto(codigoEmpleado));
		if(res){
			contador++;
		}
		
		//12. Lista grupos socio negocio
		publishProgress("Obteniendo grupos de socios");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("GrupoSocio"))
				contador++;
			else{
				res = insert.insertGruposSocioNegocio(ws.getGrupoSocioNegocio(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("GrupoSocio");
				}
			}
		}else{
			res = insert.insertGruposSocioNegocio(ws.getGrupoSocioNegocio(codigoEmpleado));
			if(res){
				contador++;
			}
		}



		//13. Lista de zonas
		publishProgress("Obteniendo zonas");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Zona"))
				contador++;
			else{
				res = insert.insertZonas(ws.getZona(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Zona");
				}
			}
		}else{
			res = insert.insertZonas(ws.getZona(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//14. Lista fabricantes
		publishProgress("Obteniendo fabricantes");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("Fabricante"))
				contador++;
			else{
				res = insert.insertFabricante(ws.getFabricantes(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("Fabricante");
				}
			}
		}else{
			res = insert.insertFabricante(ws.getFabricantes(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//15. Lista grupos articulo
		publishProgress("Obteniendo grupos de articulo");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("GrupoArticulo"))
				contador++;
			else{
				res = insert.insertGruposArticulo(ws.getGrupoArticulo(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("GrupoArticulo");
				}
			}
		}else{
			res = insert.insertGruposArticulo(ws.getGrupoArticulo(codigoEmpleado));
			if(res){
				contador++;
			}
		}




		//16. Lista unidades de medida
		publishProgress("Obteniendo unidades de medida");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("UnidadMedida"))
				contador++;
			else{
				res = insert.insertUnidadMedida(ws.getUnidadMedida(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("UnidadMedida");
				}
			}
		}else{
			res = insert.insertUnidadMedida(ws.getUnidadMedida(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		
		//17. Lista grupos unidad de medida
		publishProgress("Obteniendo grupos de unidad de medida");
		if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("controlSincManual", false)){
			if(checkRegistros("GrupoUnidadMedida"))
				contador++;
			else{
				res = insert.insertGruposUnidadMedida(ws.getGrupoUnidadMedida(codigoEmpleado));
				if(res){
					contador++;
					putRegistro("GrupoUnidadMedida");
				}
			}
		}else{
			res = insert.insertGruposUnidadMedida(ws.getGrupoUnidadMedida(codigoEmpleado));
			if(res){
				contador++;
			}
		}


		return contador;

	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		
		pd.setMessage(values[0]);
		pd.incrementProgressBy(1);
		
	}
	

	@Override
	protected void onPostExecute(Object result) {

		pd.dismiss();
		int res = (int) result;
		
		if(res == 17){
			Toast.makeText(contexto, "Carga de datos exitosa",
					Toast.LENGTH_LONG).show();
		}else if( res <17 && res > 0){
			Toast.makeText(contexto, "Carga incompleta, verifique su conexión",
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
