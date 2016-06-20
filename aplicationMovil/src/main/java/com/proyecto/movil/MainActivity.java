package com.proyecto.movil;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.proyect.movil.LoginActivity;
import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.EmpleadoBean;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.GrupoSocioNegocioBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.cobranza.MainCobranzas;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.MyDataBase;
import com.proyecto.informes.Informe;
import com.proyecto.preferences.SettingsMain;
import com.proyecto.servicios.UpdateIntentService;
import com.proyecto.sociosnegocio.ListaSocioNegocio;
import com.proyecto.utils.Variables;
import com.proyecto.ventas.ListaVentasMain;
import com.proyecto.ws.InvocaWS;

public class MainActivity extends AppCompatActivity {

	private ListView list;
	private Context contexto;
	private ProgressDialog pd = null;
	private CoordinatorLayout coordinator;

	// Cargas
	private ArrayList<GrupoSocioNegocioBean> listaGrupos = null;
	private ArrayList<EmpleadoBean> listaEmpleados = null;
	private ArrayList<MonedaBean> listaMonedas = null;
	private ArrayList<SocioNegocioBean> listaSociosNegocio = null;
	private ArrayList<CondicionPagoBean> listaCondicionPago = null;
	private ArrayList<AlmacenBean> listaAlmacen = null;
	private ArrayList<OrdenVentaBean> listaOrdenesVenta = null;
	private ArrayList<FacturaBean> listaFacturas = null;
	private ArrayList<ArticuloBean> listaArticulos = null;

	private String[] web = { "Conexión", "Socios de Negocio", "Actividades",
			"Ventas", "Cobranzas", "Informes", "Sincronizar" };
	private Integer[] imageId = { R.drawable.ic_settings_silver_36dp, 
								R.drawable.ic_account_circle_silver_36dp,
								R.drawable.ic_event_blue_36dp, 
								R.drawable.ic_shopping_cart_blue_36dp,
								R.drawable.ic_attach_money_blue_36dp, 
								R.drawable.ic_library_books_blue_36dp, 
								R.drawable.ic_sync_blue_48dp };
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contexto = this;

		// TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(myToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.logo_seidor_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
	//	myToolbar.setLogo(R.drawable.logo_seidor_menu);

		coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		
		ListaMenu adapter = new ListaMenu(MainActivity.this, web, imageId);
		list = (ListView) findViewById(R.id.listViewMenu);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {

					Intent conn = new Intent(getApplicationContext(),
							SettingsMain.class);
					startActivity(conn);

				} else if (position == 2) {

					calendarEventM();

				} else if (position == 1) {

					Intent lista = new Intent(getApplicationContext(),
							ListaSocioNegocio.class);
					startActivity(lista);

				} else if (position == 3) {
					Intent ordenVenta = new Intent(getApplicationContext(),
							ListaVentasMain.class);
					startActivity(ordenVenta);
				}

				else if (position == 4) {
					Intent cobranzaPreliminar = new Intent(
							getApplicationContext(), MainCobranzas.class);
					startActivity(cobranzaPreliminar);
				}

				else if (position == 5) {
					Intent informes = new Intent(getApplicationContext(),
							Informe.class);
					startActivity(informes);
				}

				else if (position == 6) {

					// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
					boolean wifi = Connectivity.isConnectedWifi(contexto);
					boolean movil = Connectivity.isConnectedMobile(contexto);
					boolean isConnectionFast = Connectivity
							.isConnectedFast(contexto);

					if (wifi) {

						if (isConnectionFast) {

							pd = ProgressDialog.show(contexto, "Sincronizando",
									"Por favor espere...", true, false);
							new TareaSincronizarGet().execute();

						} else {

						}
					} else if (movil) {

						if (isConnectionFast) {

							pd = ProgressDialog.show(contexto, "Sincronizando",
									"Por favor espere...", true, false);
							new TareaSincronizarGet().execute();

						} else {
							// Toast.makeText(contexto,
							// "Conectado a red MOVIL, La conexion es LENTA...",
							// Toast.LENGTH_LONG).show();
						}
					} else {

						Snackbar.make(coordinator,
								"No está conectado a ninguna red de datos...",
								Snackbar.LENGTH_LONG).show();

					}
					//

				}

			}
		});


		

	}



	@SuppressLint("NewApi") 
	private void calendarEventM() {
/*		Calendar calendarEvent = Calendar.getInstance();
		Intent i = new Intent(Intent.ACTION_EDIT);
		i.setType("vnd.android.cursor.item/event");
		i.putExtra("beginTime", calendarEvent.getTimeInMillis());
		i.putExtra("allDay", false);
		i.putExtra("rule", "FREQ=YEARLY");
		i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
		i.putExtra("title", "Editar el título de su actividad aquí");
		startActivity(i);			*/
		
		long startMillis = System.currentTimeMillis();
		Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
		builder.appendPath("time");
		ContentUris.appendId(builder, startMillis);
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
		startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_close_session:

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(contexto);
			SharedPreferences.Editor editor = pref.edit();
			editor.remove(Variables.CODIGO_EMPLEADO);
			editor.remove(Variables.PASSWORD_EMPLEADO);
			editor.putString("estadoServicio", "D");
			editor.commit();

			MyDataBase cn = new MyDataBase(contexto, null, null,
					MyDataBase.DATABASE_VERSION);
			SQLiteDatabase db = cn.getWritableDatabase();
			db.close();
			
			//DETENER EL SERVICIO
			Intent intent = new Intent(contexto, UpdateIntentService.class);
			stopService(intent);   
			
			//LANZAR LA PANTALLA DE LOGIN
			Intent login = new Intent(contexto, LoginActivity.class);
			startActivity(login);
			
			finish();

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// TAREAS ASYNC
	;

	// PRIMERA TAREA -> ENVIAR REGISTROS
	/*
	 * private class TareaSincronizarSend extends AsyncTask<String, Void,
	 * Object> {
	 * 
	 * @Override protected Object doInBackground(String... arg0) {
	 * 
	 * // InvocaWS ws = new InvocaWS();
	 * 
	 * // res = ws.pruebaDato();
	 * 
	 * return null; }
	 * 
	 * @Override protected void onPostExecute(Object result) {
	 * 
	 * Toast.makeText(contexto, "RES: " + res, Toast.LENGTH_LONG).show();
	 * 
	 * pd.dismiss();
	 * 
	 * }
	 * 
	 * }
	 */

	;

	// SEGUNDA TAREA -> OBTENER REGISTROS
	private class TareaSincronizarGet extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... arg0) {

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(contexto);

			String codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");

			InvocaWS ws = new InvocaWS(contexto);


			listaSociosNegocio = new ArrayList<SocioNegocioBean>();
			listaSociosNegocio = ws.getBusinessPartners("96");


			listaOrdenesVenta = new ArrayList<OrdenVentaBean>();
			listaOrdenesVenta = ws.getOrders(codigoEmpleado);

			listaFacturas = new ArrayList<FacturaBean>();
			listaFacturas = ws.getFacturas(codigoEmpleado);

			listaArticulos = new ArrayList<ArticuloBean>();
			listaArticulos = ws.getArticulos(codigoEmpleado);

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {

			// Registrar en la base local los registros obtenidos del servidor
			int database_version = MyDataBase.DATABASE_VERSION;
			MyDataBase cn = new MyDataBase(contexto, null, null,
					database_version);
			SQLiteDatabase db = cn.getWritableDatabase();

			int contador = 0;
			// LISTA 1
			if (listaGrupos != null) {
				for (GrupoSocioNegocioBean grupo : listaGrupos) {

					db.execSQL(
							"INSERT or IGNORE into TB_GROUP values(?,?,?)",
							new Object[] { grupo.getGroupCode(),
									grupo.getGroupName()});

				}
				contador++;
			}

			// LISTA 2
			if (listaEmpleados != null) {
				for (EmpleadoBean empleado : listaEmpleados) {

					db.execSQL(
							"INSERT or IGNORE into TB_SALES_EMP(COD_VEN, NOM_VEN) values(?,?)",
							new Object[] { empleado.getCodigoVendedor(),
									empleado.getNombreVendedor() });

				}
				contador++;
			}

			// LISTA 3
			if (listaMonedas != null) {
				for (MonedaBean moneda : listaMonedas) {

					db.execSQL(
							"INSERT or IGNORE into TB_CURRENCY values(?,?)",
							new Object[] { moneda.getCodigo(),
									moneda.getDescripcion() });

				}
				contador++;
			}

			// LISTA 4
			if (listaCondicionPago != null) {
				for (CondicionPagoBean condPago : listaCondicionPago) {

					db.execSQL(
							"INSERT or IGNORE into TB_CONDICION_PAGO values(?,?,?)",
							new Object[] { condPago.getNumeroCondicion(),
									condPago.getDescripcionCondicion(),
									condPago.getDiasExtra() });

				}
				contador++;
			}

			// LISTA 5
			if (listaAlmacen != null) {
				for (AlmacenBean almacenBean : listaAlmacen) {

					db.execSQL(
							"INSERT or IGNORE into TB_ALMACEN values(?,?)",
							new Object[] { almacenBean.getCodigo(),
									almacenBean.getDescripcion() });

				}
				contador++;
			}

			// LISTA 6
			if (listaSociosNegocio != null) {
				for (SocioNegocioBean socioNegocio : listaSociosNegocio) {

					int nro = 0;
					Cursor c = null;
					try {
						String query = "select count(*) from TB_BP where CODIGO_SN = ?";
						c = db.rawQuery(query,
								new String[] { socioNegocio.getCodigo() });
						if (c.moveToFirst()) {
							nro = c.getInt(0);
						}
					} finally {
						if (c != null) {
							c.close();
						}
					}

					if (nro == 0) {

						ContentValues regSN = new ContentValues();
						regSN.put("TIP_P", socioNegocio.getTipoPersona());
						regSN.put("TIPO_DOC", socioNegocio.getTipoDoc());
						regSN.put("CODIGO_SN", socioNegocio.getCodigo());
						regSN.put("NRO_DOC", socioNegocio.getNroDoc());
						regSN.put("NOM_RAZ_SOC", socioNegocio.getNombRazSoc());
						regSN.put("APE_P", socioNegocio.getApePat());
						regSN.put("APE_M", socioNegocio.getApeMat());
						regSN.put("PRI_NOM", socioNegocio.getPriNom());
						regSN.put("SEG_NOM", socioNegocio.getSegNom());
						regSN.put("NOM_COM", socioNegocio.getNomCom());
						regSN.put("GRUPO", socioNegocio.getGrupo());
						regSN.put("MONEDA", socioNegocio.getMoneda());
						regSN.put("TLF1", socioNegocio.getTlf1());
						regSN.put("TLF2", socioNegocio.getTlf2());
						regSN.put("TLF_MOV", socioNegocio.getTlfMov());
						regSN.put("CORREO", socioNegocio.getCorreo());
						regSN.put("COND_PAGO", socioNegocio.getCondPago());
						regSN.put("ESTADO", "SAP");
						regSN.put("TIPO_CLI", socioNegocio.getTipoCliente());
						regSN.put("EMP_VEN", socioNegocio.getEmpleadoVentas());
						regSN.put("LIM_CRE", socioNegocio.getLimCre());
						regSN.put("PRIORIDAD", socioNegocio.getPrioridad());

						long res = db.insert("TB_BP", null, regSN);

						if (res != -1) {

							for (ContactoBean contacto : socioNegocio
									.getContactos()) {

								if (!contacto.getIdCon().equalsIgnoreCase(
										"anytype{}")) {

									db.execSQL(
											"insert into TB_BP_CONT values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
											new Object[] {
													socioNegocio.getCodigo(),
													contacto.getIdCon(),
													contacto.getNomCon(),
													contacto.getSegNomCon(),
													contacto.getApeCon(),
													contacto.getDireccion(),
													contacto.getTel1Con(),
													contacto.getTel2Con(),
													contacto.getTelMovCon(),
													contacto.getEmailCon(),
													"SAP", "0",
													contacto.getPosicion() });

								}

							}

							for (DireccionBean direccion : socioNegocio
									.getDirecciones()) {

								if (!direccion.getIDDireccion()
										.equalsIgnoreCase("anytype{}")) {

									db.execSQL(
											"insert into TB_BP_DIR values(?,?,?,?,?,?,?,?,?,?)",
											new Object[] {
													socioNegocio.getCodigo(),
													direccion
															.getTipoDireccion(),
													direccion.getIDDireccion(),
													direccion.getPais(),
													direccion.getDepartamento(),
													direccion.getProvincia(),
													direccion.getDistrito(),
													direccion.getCalle(),
													"LOCAL", "0" });

								}

							}

						}

					}

				}
				contador++;
			}

			// LISTA 7

		

			// LISTA 9 - ARTICULOS
			if (listaArticulos != null) {
				for (ArticuloBean bean : listaArticulos) {
					db.execSQL(
							"INSERT or IGNORE into TB_ART values(?,?,?,?,?)",
							new Object[] { bean.getCod(), bean.getDesc(),
									bean.getStock(), bean.getCodProv(),
									bean.getCodUM() });

				}
				contador++;
			}

			db.close();
			pd.dismiss();

			switch (contador) {
			case 0:
				Snackbar.make(coordinator,
						"No se pudo establecer conexión con el servidor",
						Snackbar.LENGTH_LONG).show();
				break;
			case 8:
				Snackbar.make(coordinator, "Carga de datos exitosa",
						Snackbar.LENGTH_LONG).show();
				break;
			default:
				Snackbar.make(coordinator,
						"Carga incompleta, verifique su conexión",
						Snackbar.LENGTH_LONG).show();
				break;
			}

		}

	}
}
