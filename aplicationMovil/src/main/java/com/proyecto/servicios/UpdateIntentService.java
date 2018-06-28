package com.proyecto.servicios;

import java.util.Calendar;
import java.util.Date;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.Delete;
import com.proyecto.database.Insert;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class UpdateIntentService extends IntentService {
		
	private Context contexto;
	private boolean shouldContinue = false;
	private int counter = 0;
	private Insert insert;
	private Delete delete;
	private InvocaWS ws;
	
	public UpdateIntentService() {
		super("UpdateIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (action.equals(Constants.ACTION_RUN_ISERVICE)) {
				
				shouldContinue = true;
				Toast.makeText(this, "Servicio iniciado...", Toast.LENGTH_LONG).show();
				handleActionRun();
				
			}
		}
	}

	/**
	 * Maneja la acci�n de ejecuci�n del servicio
	 */
	private void handleActionRun() {
		try {

			contexto = getApplication().getApplicationContext();

			// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
			boolean wifi = Connectivity.isConnectedWifi(contexto);
			boolean movil = Connectivity.isConnectedMobile(contexto);
			boolean isConnectionFast = Connectivity.isConnectedFast(contexto);

			if (wifi || movil) {
				while (wifi || movil && shouldContinue && isConnectionFast) {
					
					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(contexto);
					String codEmp = pref.getString(Variables.CODIGO_EMPLEADO, "");
					
					Date date = new Date();
				    Calendar c = Calendar.getInstance();
				    c.setTime(date);
				    
				    insert = new Insert(contexto);
					delete = new Delete(contexto);
					ws = new InvocaWS(contexto);
					
				    //Bloque de actualizacion masiva (Documentos y datos maestros, borra y vuelve a crear)
					if(c.get(Calendar.HOUR_OF_DAY) == 6){
					
						NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					
						// Se construye la notificaci�n
							NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto)
									.setSmallIcon(android.R.drawable.stat_sys_download)
									.setContentTitle("Business One: Sincronizando datos maestros")
									.setOngoing(true)
									.setOnlyAlertOnce(true)
									.setAutoCancel(false)
									.setProgress(0, 0, true)
									.setContentText("Procesando...").setColor(-1);
							// Poner en primer plano
							startForeground(1, builder.build());
							
							builder.setContentText("Enviando: socios de negocio");
							builder.setProgress(30, 0, false);
							notifyMgr.notify(1, builder.build());
							ws.EnviarSociosNegocio(codEmp);
							
							builder.setContentText("Obteniendo: socios de negocio");
							builder.setProgress(30, 1, true);
							notifyMgr.notify(1, builder.build());
							if(ws.getBusinessPartners(codEmp) != null){
//								delete.deleteSocioNegocio();
								insert.insertSocioNegocio(ws.getBusinessPartners(codEmp), "A");
							}
						
							builder.setContentText("Obteniendo: almacenes");
							builder.setProgress(30, 2, false);
							notifyMgr.notify(1, builder.build());
							insert.insertAlmacen(ws.getAlmacen(codEmp));
							
							builder.setContentText("Obteniendo: articulos");
							builder.setProgress(30, 3, false);
							notifyMgr.notify(1, builder.build());
							insert.insertArticulo(ws.getArticulos(codEmp));
							
							builder.setContentText("Obteniendo: cantidades");
							builder.setProgress(30, 4, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getCantidades(codEmp) != null){
								delete.deleteCantidad();
								insert.insertCantidad(ws.getCantidades(codEmp));
							}
							
							builder.setContentText("Obteniendo: condiciones de pago");
							builder.setProgress(30, 5, false);
							notifyMgr.notify(1, builder.build());
							insert.insertCondicionPago(ws.getCondicionPago(codEmp));
							
							builder.setContentText("Obteniendo: fabricantes");
							builder.setProgress(30, 6, false);
							notifyMgr.notify(1, builder.build());
							insert.insertFabricante(ws.getFabricantes(codEmp));
							
							builder.setContentText("Obteniendo: grupos de articulo");
							builder.setProgress(30, 7, false);
							notifyMgr.notify(1, builder.build());
							insert.insertGruposArticulo(ws.getGrupoArticulo(codEmp));
							
							builder.setContentText("Obteniendo: grupos de socios de negocio");
							builder.setProgress(30, 8, false);
							notifyMgr.notify(1, builder.build());
							insert.insertGruposSocioNegocio(ws.getGrupoSocioNegocio(codEmp));
							
							builder.setContentText("Obteniendo: grupos de unidades de medida");
							builder.setProgress(30, 9, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getGrupoUnidadMedida(codEmp) != null){
								delete.deleteGrupoUnidadMedida();
								insert.insertGruposUnidadMedida(ws.getGrupoUnidadMedida(codEmp));
							}
							
							builder.setContentText("Obteniendo: impuestos");
							builder.setProgress(30, 10, false);
							notifyMgr.notify(1, builder.build());
							insert.insertImpuesto(ws.getImpuesto(codEmp));
							
							builder.setContentText("Obteniendo: indicadores");
							builder.setProgress(30, 11, false);
							notifyMgr.notify(1, builder.build());
							insert.insertIndicador(ws.getIndicador(codEmp));
							
							builder.setContentText("Obteniendo: lista de precios");
							builder.setProgress(30, 12, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getListaPrecio(codEmp) != null){
								delete.deleteListaPrecios();
								insert.insertListaPrecio(ws.getListaPrecio(codEmp));
							}
							
							builder.setContentText("Obteniendo: precios");
							builder.setProgress(30, 13, false);
							notifyMgr.notify(1, builder.build());
							if(ws.ObtenerPrecios(codEmp) != null){
								delete.deletePrecios();
								insert.insertPrecios(ws.ObtenerPrecios(codEmp));
							}
							
							builder.setContentText("Obteniendo: monedas");
							builder.setProgress(30, 14, false);
							notifyMgr.notify(1, builder.build());
							insert.insertMoneda(ws.getMoneda(codEmp));
							
							builder.setContentText("Obteniendo: paises");
							builder.setProgress(30, 15, false);
							notifyMgr.notify(1, builder.build());
							insert.insertPais(ws.getPais(codEmp));
							
							builder.setContentText("Obteniendo: departamentos");
							builder.setProgress(30, 16, false);
							notifyMgr.notify(1, builder.build());
							insert.insertDepartamento(ws.ObtenerDepartamentos(codEmp));
							
							builder.setContentText("Obteniendo: provincias");
							builder.setProgress(30, 17, false);
							notifyMgr.notify(1, builder.build());
							insert.insertProvincias(ws.ObtenerProvincias(codEmp));
							
							builder.setContentText("Obteniendo: distritos");
							builder.setProgress(30, 18, false);
							notifyMgr.notify(1, builder.build());
							insert.insertDistritos(ws.ObtenerDistritos(codEmp));
							
							builder.setContentText("Obteniendo: calles");
							builder.setProgress(30, 19, false);
							notifyMgr.notify(1, builder.build());
							insert.insertCalles(ws.ObtenerCalles(codEmp));
							
							builder.setContentText("Obteniendo: unidades de medida");
							builder.setProgress(30, 20, false);
							notifyMgr.notify(1, builder.build());
							insert.insertUnidadMedida(ws.getUnidadMedida(codEmp));
							
							builder.setContentText("Obteniendo: zonas");
							builder.setProgress(30, 21, false);
							notifyMgr.notify(1, builder.build());
							insert.insertZonas(ws.getZona(codEmp));
							
							builder.setContentText("Obteniendo: cuentas");
							builder.setProgress(30, 22, false);
							notifyMgr.notify(1, builder.build());
							insert.insertCuentas(ws.ObtenerCuentas(codEmp));
							
							builder.setContentText("Obteniendo: bancos");
							builder.setProgress(30, 23, false);
							notifyMgr.notify(1, builder.build());
							insert.insertBancos(ws.ObtenerBancos(codEmp));
						
							//Documentos
							builder.setContentTitle("Business One: Sincronizando documentos");
							builder.setContentText("Enviando: pedidos de cliente");
							builder.setProgress(0, 0, true);
							notifyMgr.notify(1, builder.build());
							ws.EnviarPedidoCliente(codEmp);
							
							builder.setContentText("Enviando: pagos recibidos");
							builder.setProgress(5, 0, false);
							notifyMgr.notify(1, builder.build());
							ws.EnviarPagoCliente(codEmp);
							
							builder.setContentText("Obteniendo: pedidos de cliente");
							builder.setProgress(5, 1, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getOrders(codEmp) != null){
//								delete.deleteOrdenVenta();
								insert.insertOrdenVenta(ws.getOrders(codEmp));
							}
							
							builder.setContentText("Obteniendo: facturas de cliente");
							builder.setProgress(5, 2, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getFacturas(codEmp) != null){
								delete.deleteFacturas();
								insert.insertFacturas(ws.getFacturas(codEmp));
							}
							
							builder.setContentText("Obteniendo: pagos de cliente");
							builder.setProgress(5, 3, false);
							notifyMgr.notify(1, builder.build());
							if(ws.getPagoCliente(codEmp) != null){
//								delete.deletePagos();
								insert.insertPagoCliente(ws.getPagoCliente(codEmp));
							}
							
							builder.setContentText("Obteniendo: cuentas de socio de negocio");
							builder.setProgress(5, 4, false);
							notifyMgr.notify(1, builder.build());
							if(ws.ObtenerEstadoCuentaSocios(codEmp) != null){
								delete.deleteRegistroEstadoCuenta();
								insert.insertEstadoCuentaCliente(ws.ObtenerEstadoCuentaSocios(codEmp));
							}
							
							builder.setProgress(5, 5, false);
							notifyMgr.notify(1, builder.build());
							
							// Quitar de primer plano
							stopForeground(true);
							
							// Creaci�n del builder
							NotificationCompat.Builder builder2 = new NotificationCompat.Builder(
									this)
									.setSmallIcon(R.drawable.ic_sync_white_24dp)
									.setContentTitle("Business One")
									.setContentText(
											"Sincronizacion exitosa");
							notifyMgr.notify(2, builder2.build());
							
							counter++;
						
					}else{
						
//						ws.EnviarSociosNegocio(codEmp);
//						ws.EnviarPedidoCliente(codEmp);
//						ws.EnviarPagoCliente(codEmp);
//						
//						
						
						AsyncTaskSocios first_backGround = new AsyncTaskSocios(contexto, codEmp);
						first_backGround.execute();
						
						AsyncTaskPedido second_backGround = new AsyncTaskPedido(contexto, codEmp);
						second_backGround.execute();
						
						AsyncTaskPago third_backGround = new AsyncTaskPago(contexto, codEmp);
						third_backGround.execute();
						
					}
						
					insert.close();
					delete.close();
					Thread.sleep(60000*2);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		shouldContinue = false;
		Toast.makeText(contexto, "Servicio detenido...", Toast.LENGTH_SHORT).show();
		stopSelf();
	}

}
