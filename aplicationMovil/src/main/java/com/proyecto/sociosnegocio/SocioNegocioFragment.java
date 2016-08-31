package com.proyecto.sociosnegocio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.GrupoSocioNegocioBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.TipoDocBean;
import com.proyecto.bean.TipoPersonaBean;
import com.proyecto.bean.ZonaBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class SocioNegocioFragment extends Fragment {

	private View v = null;
	private Context contexto;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private ConstruirAlert alert = new ConstruirAlert();
	private int iconId = Variables.idIconRightBlue36dp;
	
	
	// OBJETO A REGISTRAR
	private SocioNegocioBean objSN = null;

	// RESPUESTA FINAL (SI ES QUE REGISTRA EN EL SERVICE)
	private String res = "";

	// LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ListView lvGeneral = null;
	private ListView lvDirecciones = null;
	private ListView lvContacto = null;
	private ListView lvCondicionesPago = null;

	// LISTAS PERSONALIZABLES (ITEM DEL LISTVIEW)
	private ArrayList<FormatCustomListView> searchResults = null;
	private ArrayList<FormatCustomListView> searchResults1 = null;
	private ArrayList<FormatCustomListView> searchResults2 = null;
	private ArrayList<FormatCustomListView> searchResults3 = null;
	private ArrayList<FormatCustomListView> searchResults4 = null;

	// Nro de contactos PARA EL DETALLE
	public static ArrayList<ContactoBean> listaDetalleContactos = new ArrayList<ContactoBean>();
	public static ArrayList<DireccionBean> listaDirecciones = new ArrayList<DireccionBean>();
	public static int utilId = 1;
	public static int utilId2 = 1;
	public static int contactId = 1;
	public static int directionIdFiscal = 1;
	public static int directionIdEntrega = 1;

	// Objeto que tomar� al ser seleccionado (Ayuda al update del select item
	// con popup
	private FormatCustomListView fullObject = null;
	private FormatCustomListView fullObject2 = null;
	private int posicion = 0;
	
	//Preferencias
	private SharedPreferences pref;
	private String codigoEmpleado;
	private String nombreEmpleado;
	private String idDispositivo;
	private String claveMovil;

	//Listas desde sqlite
	private ArrayList<CondicionPagoBean> listaCondicionPago = null;
	private ArrayList<GrupoSocioNegocioBean> listaGruposSocioNegocio = null;
	private ArrayList<ListaPrecioBean> listaPrecios = null;
	private ArrayList<IndicadorBean> listaIndicadores = null;
	private ArrayList<ZonaBean> listaZonas = null;
	
	// Seleccionada de combos
	private GrupoSocioNegocioBean grupoSel = null;
	private TipoPersonaBean tipoPerSel = null;
	private TipoDocBean tipoDocSel = null;
	private ListaPrecioBean listaPreSel = null;
	private CondicionPagoBean condPagoSel = null;
	private IndicadorBean indicadorSel = null;
	private ZonaBean zonaSel = null;
	
	private String nroDoc = "";
	private String nombreRazSoc = "";

	// RECIBE LOS PAR�METROS DESDE EL FRAGMENT CORRESPONDIENTE
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
 
			Bundle bundle = intent.getExtras();

			if (bundle != null) {

				if (intent.getAction().equals("event-send-contact-to-bp")) {

					String contact = bundle.getString("defaultContact");

					// Capturar el objeto (que refleja la selecci�n estado doc)
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvContacto
							.getItemAtPosition(0);
					fullObject.setData(contact);
					searchResults4.set(0, fullObject);

					lvContacto.invalidateViews();

				} else if (intent.getAction().equals(
						"event-send-direction-to-bp")) {

					String direction = bundle.getString("defaultDirection");

					// Capturar el objeto (que refleja la selecci�n estado doc)
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvDirecciones
							.getItemAtPosition(0);
					fullObject.setData(direction);
					searchResults1.set(0, fullObject);

					lvDirecciones.invalidateViews();

				} else if (intent.getAction().equals(
						"event-send-contact-to-list")) {

				}

			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.socio_negocio, viewGroup, false);

		v = view;
		contexto = view.getContext();
		
		boolean val = cargarListas();
		
		if(!val){
			getActivity().finish();
			return view;
		}
		
		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");
		nombreEmpleado = pref.getString(Variables.NOMBRE_EMPLEADO, "");
		idDispositivo = Secure.getString(getActivity().getContentResolver(),
				Secure.ANDROID_ID);
		
		Select select = new Select(contexto);
		int ClaveMovilInt = select.numeroCorrelativoRegistro("CNT");
		Date date = new Date();
		String fullDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(date);
		claveMovil = idDispositivo + "-"+fullDate+ "-" + ClaveMovilInt;
		select.close();

		// registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
		IntentFilter filter = new IntentFilter("event-send-contact-to-bp");
		filter.addAction("event-send-contact-to-list");
		filter.addAction("event-send-direction-to-bp");
		filter.addAction("event-send-direction-to-list");
		LocalBroadcastManager.getInstance(contexto).registerReceiver(
				myLocalBroadcastReceiver, filter);

		// LLENAR EL LISTADO DE DATOS QUE COMPONEN EL REGISTRO DE SOCIO DE
		// NEGOCIO
		llenarListaTitulo();
		llenarListaDirecciones();
		llenarListaCondPago();
		llenarListaGeneral();
		llenarListaContacto();
		

		// 1. PROGRAMAR EL CLICK HACIA EL BLOQUE PRINCIPAL PARA CADA DATO
		lvPrincipal
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						construirAlert("principal", position);
					}

				});

		lvGeneral.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				construirAlert("general", position);
			}

		});
		// //

		// PROGRAMAR EL CLICK PARA CADA LINEA DEL BLOQUE DE CONTACTOS
		lvContacto
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						construirAlert("contactos", position);
					}

				});
		// //////

		// 2. PROGRAMAR EL CLICK PARA CADA LINEA DEL BLOQUE DE DIRECCIONES
		lvDirecciones
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						construirAlert("direcciones", position);
					}

				});
		// //////

		// 3. PROGRAMAR EL CLICK PARA CADA LINEA DEL BLOQUE DE DIRECCIONES
		lvCondicionesPago
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						construirAlert("condPago", position);
					}

				});

		// //////

		buildFirstAlert(contexto, 1, "Tipo de persona", searchResults,
				lvPrincipal);

		setHasOptionsMenu(true);
		return view;

	}

	
	private boolean cargarListas(){
		
		listaCondicionPago = new ArrayList<CondicionPagoBean>();
		listaGruposSocioNegocio = new ArrayList<GrupoSocioNegocioBean>();
		listaPrecios = new ArrayList<ListaPrecioBean>();
		listaIndicadores = new ArrayList<IndicadorBean>();
		listaZonas = new ArrayList<ZonaBean>();
		
		Select select = new Select(contexto);
		listaCondicionPago = select.listaCondicionPago();
		
		if(listaCondicionPago.size()>0)
			condPagoSel = listaCondicionPago.get(0);
		else{
			Toast.makeText(contexto, "Uno o mas datos maestros no han sido cargados", Toast.LENGTH_LONG).show();
			select.close();
			return false;
		}
		
		
		listaGruposSocioNegocio = select.listaGrupoSocioNegocio();
		grupoSel = listaGruposSocioNegocio.get(0);
		listaPrecios = select.listaPrecios();
		listaPreSel = listaPrecios.get(0);
		listaIndicadores = select.listaIndicadores();
		indicadorSel = listaIndicadores.get(0);
		listaZonas = select.listaZona();
		zonaSel = listaZonas.get(0);
		select.close();
		
		return true;
		
	}
	
	
	
	public void buildFirstAlert(final Context contexto, final int pos,
			String titulo, final ArrayList<FormatCustomListView> searchResults,
			final ListView lv) {

		// PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
		posicion = pos;
		// Capturar el objeto (row - fila)
		Object o = lvPrincipal.getItemAtPosition(pos);
		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView) o;
		//

		ArrayList<TipoPersonaBean> listaCombo = new ArrayList<TipoPersonaBean>();
		TipoPersonaBean m = new TipoPersonaBean();
		listaCombo = m.lista(contexto);

		// Spinner
		final Spinner spnTipoPersona = new Spinner(contexto);

		ArrayAdapter<TipoPersonaBean> adapter = new ArrayAdapter<TipoPersonaBean>(
				contexto, android.R.layout.simple_list_item_1, listaCombo);
		spnTipoPersona.setAdapter(adapter);
		spnTipoPersona.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				// MONEDA SELECCIONADA
				tipoPerSel = new TipoPersonaBean();
				tipoPerSel = (TipoPersonaBean) parent.getItemAtPosition(pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Tipo de persona");

		alert.setView(spnTipoPersona);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				fullObject.setData(tipoPerSel.getDescripcion());
				searchResults.set(posicion, fullObject);

				if (tipoPerSel.getDescripcion().equalsIgnoreCase("Natural")) {

					llenarListaTitulo2();
					nroDoc = "";

				} else {

					if (searchResults.size() > 9) {

						llenarListaTitulo();
						nroDoc = "";

					}

				}

				lvPrincipal.invalidateViews();

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}

	public void construirAlertConRetorno(final Context contexto,
			final int posicion, String titulo,
			final ArrayList<FormatCustomListView> searchResults,
			final ListView lv, String tipo) {

		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView) lv.getItemAtPosition(posicion);

		fullObject2 = new FormatCustomListView();
		fullObject2 = (FormatCustomListView) lv.getItemAtPosition(4);

		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle(titulo);

		final EditText edt = new EditText(contexto);
		alert.setView(edt);

		if (nroDoc != "") {

			edt.setText(nroDoc);

		}

		edt.setFocusableInTouchMode(true);
		edt.requestFocus();

		InputFilter[] FilterArray = new InputFilter[1];

		if (tipo.equals("ruc")) {
			FilterArray[0] = new InputFilter.LengthFilter(11);
		} else
			FilterArray[0] = new InputFilter.LengthFilter(8);

		edt.setFilters(FilterArray);
		edt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressLint("DefaultLocale")
			public void onClick(DialogInterface dialog, int whichButton) {

				if (edt.getText().toString().length() > 0) {

					InputMethodManager imm = (InputMethodManager) contexto
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

					fullObject.setData(edt.getText().toString());
					searchResults.set(posicion, fullObject);
					nroDoc = edt.getText().toString();

					lv.invalidateViews();

				}

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						 InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
				            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
					}
				});

		edt.requestFocus();
		InputMethodManager imm = (InputMethodManager) contexto
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

		alert.show();

	}

	protected void construirAlert(String bloque, int position) {

		// Alerts del bloque principal
		if (bloque.equals("principal")) {

			if (searchResults.size() == 9) {

				if (position == 0) {


				} else if (position == 1) {

					buildFirstAlert(contexto, 1, "Tipo de persona",
							searchResults, lvPrincipal);

				} else if (position == 2) {

					// PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
					posicion = position;
					// Capturar el objeto (row - fila)
					Object o = lvPrincipal.getItemAtPosition(position);
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) o;
					//

					ArrayList<TipoDocBean> listaCombo = new ArrayList<TipoDocBean>();
					TipoDocBean m = new TipoDocBean();
					listaCombo = m.listaDoc(contexto, tipoPerSel.getCodigo());

					// Spinner
					final Spinner spnTipoDoc = new Spinner(contexto);

					ArrayAdapter<TipoDocBean> adapter = new ArrayAdapter<TipoDocBean>(
							contexto, android.R.layout.simple_list_item_1,
							listaCombo);
					spnTipoDoc.setAdapter(adapter);
					spnTipoDoc
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View arg1,
										int pos, long arg3) {
									// MONEDA SELECCIONADA
									tipoDocSel = new TipoDocBean();
									tipoDocSel = (TipoDocBean) parent
											.getItemAtPosition(pos);

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Tipo de documento");

					alert.setView(spnTipoDoc);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject.setData(tipoDocSel
											.getDescripcion());
									searchResults.set(posicion, fullObject);
									lvPrincipal.invalidateViews();
									refreshTipoDocumentoIndicador();
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 3) {

					construirAlertConRetorno(contexto, position,
							"N� documento", searchResults, lvPrincipal, "ruc");

				} else if (position == 5) {

					alert.construirAlert(contexto, position,
							"Nombre o raz�n social", searchResults, lvPrincipal , "text");

				} else if (position == 6) {

					alert.construirAlert(contexto, position,
							"Nombre comercial", searchResults, lvPrincipal, "text");

				} else if (position == 7) {

					posicion = position;

					// Capturar el objeto
					Object o = lvPrincipal.getItemAtPosition(position);
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) o;

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Grupo");

					// Spinner
					final Spinner spnGrupo = new Spinner(contexto);

					ArrayAdapter<GrupoSocioNegocioBean> adapter = new ArrayAdapter<GrupoSocioNegocioBean>(
							contexto, android.R.layout.simple_list_item_1,
							listaGruposSocioNegocio);
					spnGrupo.setAdapter(adapter);
					spnGrupo.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							// MONEDA SELECCIONADA
							grupoSel = new GrupoSocioNegocioBean();
							grupoSel = (GrupoSocioNegocioBean) parent
									.getItemAtPosition(pos);

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

					alert.setView(spnGrupo);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// Do something with value!
									fullObject.setData(grupoSel.toString());
									searchResults.set(posicion, fullObject);
									lvPrincipal.invalidateViews();
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				}

			} else if (searchResults.size() == 13) {

				if (position == 0) {
					
				} else if (position == 1) {

					buildFirstAlert(contexto, 0, "Tipo de persona",
							searchResults, lvPrincipal);

				} else if (position == 2) {

					// PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
					posicion = position;
					// Capturar el objeto (row - fila)
					Object o = lvPrincipal.getItemAtPosition(position);
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) o;
					//

					ArrayList<TipoDocBean> listaCombo = new ArrayList<TipoDocBean>();
					TipoDocBean m = new TipoDocBean();
					listaCombo = m.listaDoc(contexto, tipoPerSel.getCodigo());

					// Spinner
					final Spinner spnTipoDoc = new Spinner(contexto);

					ArrayAdapter<TipoDocBean> adapter = new ArrayAdapter<TipoDocBean>(
							contexto, android.R.layout.simple_list_item_1,
							listaCombo);
					spnTipoDoc.setAdapter(adapter);
					spnTipoDoc
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View arg1,
										int pos, long arg3) {
									
									tipoDocSel = new TipoDocBean();
									tipoDocSel = (TipoDocBean) parent
											.getItemAtPosition(pos);

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Tipo de documento");

					alert.setView(spnTipoDoc);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject.setData(tipoDocSel
											.getDescripcion());
									searchResults.set(posicion, fullObject);
									lvPrincipal.invalidateViews();
									refreshTipoDocumentoIndicador();
									
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 3) {

					construirAlertConRetorno(contexto, position,
							"N� documento", searchResults, lvPrincipal, "dni");

				} else if (position == 5) {

				} else if (position == 6) {

					alert.construirAlert(contexto, position,
							"Nombre comercial", searchResults, lvPrincipal, "text");

				} else if (position == 7) {

					posicion = position;

					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(posicion);

					fullObject2 = new FormatCustomListView();
					fullObject2 = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(5);

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Apellido Paterno");

					final EditText edt = new EditText(contexto);
					alert.setView(edt);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject
											.setData(edt.getText().toString());
									searchResults.set(posicion, fullObject);

									nombreRazSoc = edt.getText().toString();

									fullObject2.setData(nombreRazSoc);
									searchResults.set(5, fullObject2);

									lvPrincipal.invalidateViews();

								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 8) {

					posicion = position;

					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(posicion);

					fullObject2 = new FormatCustomListView();
					fullObject2 = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(5);

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Apellido Materno");

					final EditText edt = new EditText(contexto);
					alert.setView(edt);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject
											.setData(edt.getText().toString());
									searchResults.set(posicion, fullObject);

									if (nombreRazSoc != "") {

										nombreRazSoc = nombreRazSoc + " "
												+ edt.getText().toString();

										fullObject2.setData(nombreRazSoc);
										searchResults.set(5, fullObject2);

										lvPrincipal.invalidateViews();

									} else {
										edt.setError("Ingrese apellido paterno primero");
									}

								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 9) {

					posicion = position;

					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(posicion);

					fullObject2 = new FormatCustomListView();
					fullObject2 = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(5);

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Primer nombre");

					final EditText edt = new EditText(contexto);
					alert.setView(edt);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject
											.setData(edt.getText().toString());
									searchResults.set(posicion, fullObject);

									if (nombreRazSoc != "") {

										nombreRazSoc = nombreRazSoc + ", "
												+ edt.getText().toString();

										fullObject2.setData(nombreRazSoc);
										searchResults.set(5, fullObject2);

										lvPrincipal.invalidateViews();

									} else {
										edt.setError("Ingrese apellidos primero");
									}

								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 10) {

					posicion = position;

					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(posicion);

					fullObject2 = new FormatCustomListView();
					fullObject2 = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(5);

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Segundo Nombre");

					final EditText edt = new EditText(contexto);
					alert.setView(edt);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int whichButton) {

									fullObject
											.setData(edt.getText().toString());
									searchResults.set(posicion, fullObject);

									if (nombreRazSoc != "") {

										nombreRazSoc = nombreRazSoc + " "
												+ edt.getText().toString();

										fullObject2.setData(nombreRazSoc);
										searchResults.set(5, fullObject2);

										lvPrincipal.invalidateViews();

									} else {
										edt.setError("Ingrese apellidos primero");
									}

								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				} else if (position == 11) {

					posicion = position;

					// Capturar el objeto
					Object o = lvPrincipal.getItemAtPosition(position);
					fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) o;

					AlertDialog.Builder alert = new AlertDialog.Builder(
							contexto);
					alert.setTitle("Grupo");

					// Spinner
					final Spinner spnGrupo = new Spinner(contexto);

					ArrayAdapter<GrupoSocioNegocioBean> adapter = new ArrayAdapter<GrupoSocioNegocioBean>(
							contexto, android.R.layout.simple_list_item_1,
							listaGruposSocioNegocio);
					spnGrupo.setAdapter(adapter);
					spnGrupo.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							// MONEDA SELECCIONADA
							grupoSel = new GrupoSocioNegocioBean();
							grupoSel = (GrupoSocioNegocioBean) parent
									.getItemAtPosition(pos);

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

					alert.setView(spnGrupo);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// Do something with value!
									fullObject.setData(grupoSel.toString());
									searchResults.set(posicion, fullObject);
									lvPrincipal.invalidateViews();
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();

				}

			}

		}

		else if (bloque.equals("direcciones")) {

			construirAlertDirecciones(position);

		} else if (bloque.equals("general")) {

			construirAlertGeneral(position);

		} else if (bloque.equals("contactos")) {

			construirAlertContactos(position);

		} else {

			construirAlertCondPago(position);

		}

		// FIN DE IF PRINCIPAL

	}

	private void construirAlertContactos(int position) {

		if (position == 0) {

			Fragment fragment = new ListaContactosFragment();

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.hide(this);
			transaction.add(R.id.box, fragment);
			transaction.addToBackStack(null);
			transaction.commit();

		}

	}

	private void construirAlertDirecciones(int position) {

		if (position == 0) {

			Fragment fragment = new ListaDireccionesFragment();

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.hide(this);
			transaction.add(R.id.box, fragment);
			transaction.addToBackStack(null);
			transaction.commit();

		}

	}

	private void construirAlertGeneral(int position) {

		if (position == 0) {

			alert.construirAlert(contexto, position, "Tel�fono 1",
					searchResults3, lvGeneral, "numeric");

		} else if (position == 1) {

			alert.construirAlert(contexto, position, "Tel�fono 2",
					searchResults3, lvGeneral, "numeric");

		} else if (position == 2) {

			alert.construirAlert(contexto, position, "Tel�fono m�vil",
					searchResults3, lvGeneral, "numeric");

		} else if (position == 3) {

			alert.construirAlert(contexto, position, "Correo electr�nico",
					searchResults3, lvGeneral , "text");

		}

	}

	// CONSTRUYENDO EL ALERT DE CADA LINEA PARA EL BLOQUE DE CONDICIONES DE PAGO
	private void construirAlertCondPago(int position) {

		if (position == 0) {

			posicion = position;
			// Capturar el objeto
			Object o = lvCondicionesPago.getItemAtPosition(position);
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) o;
			//

			// Spinner
			final Spinner spnCondPago = new Spinner(contexto);

			ArrayAdapter<CondicionPagoBean> adap = new ArrayAdapter<CondicionPagoBean>(
					contexto, android.R.layout.simple_list_item_1, listaCondicionPago);

			spnCondPago.setAdapter(adap);

			spnCondPago.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View arg1,
						int pos, long arg3) {

					condPagoSel = new CondicionPagoBean();
					condPagoSel = (CondicionPagoBean) parent
							.getItemAtPosition(pos);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle("Condici�n de Pago");

			// Set an EditText view to get user input
			alert.setView(spnCondPago);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							// Do something with value!
							fullObject.setData(condPagoSel
									.getDescripcionCondicion());
							searchResults2.set(posicion, fullObject);
							lvCondicionesPago.invalidateViews();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();

		}else if (position == 1) {

			posicion = position;
			// Capturar el objeto
			Object o = lvCondicionesPago.getItemAtPosition(position);
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) o;
			//

			// Spinner
			final Spinner spn = new Spinner(contexto);

			ArrayAdapter<ListaPrecioBean> adap = new ArrayAdapter<ListaPrecioBean>(
					contexto, android.R.layout.simple_list_item_1, listaPrecios);

			spn.setAdapter(adap);

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View arg1,
						int pos, long arg3) {

					listaPreSel = new ListaPrecioBean();
					listaPreSel = (ListaPrecioBean) parent
							.getItemAtPosition(pos);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle("Lista de precios");

			// Set an EditText view to get user input
			alert.setView(spn);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							// Do something with value!
							fullObject.setData(listaPreSel
									.getNombre());
							searchResults2.set(posicion, fullObject);
							lvCondicionesPago.invalidateViews();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();

		}else if (position == 2) {

			posicion = position;
			// Capturar el objeto
			Object o = lvCondicionesPago.getItemAtPosition(position);
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) o;
			//

			// Spinner
			final Spinner spn = new Spinner(contexto);

			ArrayAdapter<IndicadorBean> adap = new ArrayAdapter<IndicadorBean>(
					contexto, android.R.layout.simple_list_item_1, listaIndicadores);

			spn.setAdapter(adap);

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View arg1,
						int pos, long arg3) {

					indicadorSel = new IndicadorBean();
					indicadorSel = (IndicadorBean) parent
							.getItemAtPosition(pos);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle("Indicador");

			// Set an EditText view to get user input
			alert.setView(spn);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							// Do something with value!
							fullObject.setData(indicadorSel
									.getNombre());
							searchResults2.set(posicion, fullObject);
							lvCondicionesPago.invalidateViews();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();

		}else if (position == 3) {

			posicion = position;
			// Capturar el objeto
			Object o = lvCondicionesPago.getItemAtPosition(position);
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) o;
			//

			// Spinner
			final Spinner spn = new Spinner(contexto);

			ArrayAdapter<ZonaBean> adap = new ArrayAdapter<ZonaBean>(
					contexto, android.R.layout.simple_list_item_1, listaZonas);

			spn.setAdapter(adap);

			spn.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View arg1,
						int pos, long arg3) {

					zonaSel = new ZonaBean();
					zonaSel = (ZonaBean) parent
							.getItemAtPosition(pos);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle("Zona");

			// Set an EditText view to get user input
			alert.setView(spn);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							// Do something with value!
							fullObject.setData(zonaSel
									.getNombre());
							searchResults2.set(posicion, fullObject);
							lvCondicionesPago.invalidateViews();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();

		}

	}
	
	private void refreshTipoDocumentoIndicador(){
		
		if(tipoDocSel.getCodigo().equalsIgnoreCase("1")){
			
			indicadorSel = listaIndicadores.get(1);
			
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvCondicionesPago.getItemAtPosition(2);
			fullObject.setData(indicadorSel.getNombre());
			searchResults2.set(posicion, fullObject);
			lvCondicionesPago.invalidateViews();
			
		}else if(tipoDocSel.getCodigo().equalsIgnoreCase("6")){
			
			indicadorSel = listaIndicadores.get(0);
			
			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvCondicionesPago.getItemAtPosition(2);
			fullObject.setData(indicadorSel.getNombre());
			searchResults2.set(posicion, fullObject);
			lvCondicionesPago.invalidateViews();
		}
		
	}
	

	// METODOS PARA LLENAR LAS LISTAS DE "MENUS" EN LA PANTALLA PRINCIPAL DEL
	// SOCIO DE NEGOCIO
	private void llenarListaTitulo() {

		searchResults = new ArrayList<FormatCustomListView>();

		lvPrincipal = (ListView) v.findViewById(R.id.lvPrinSN);

		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo de cliente");
		sr1.setData("Lead");
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo de persona");
		sr1.setIcon(iconId);
		sr1.setData("Jur�dica");
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo documento");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("N� documento");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("C�digo");
		sr1.setData(claveMovil);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Nombre o raz�n social");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Nombre comercial");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Grupo");
		sr1.setData(listaGruposSocioNegocio.get(0).getGroupName());
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Moneda");
		sr1.setData("Monedas (todas)");
		searchResults.add(sr1);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvPrincipal.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);

	}

	private void llenarListaTitulo2() {

		searchResults = new ArrayList<FormatCustomListView>();

		lvPrincipal = (ListView) v.findViewById(R.id.lvPrinSN);

		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo de cliente");
		sr1.setData("Lead");
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo de persona");
		sr1.setIcon(iconId);
		sr1.setData("Natural");
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tipo documento");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("N� documento");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("C�digo");
		sr1.setData(claveMovil);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Nombre o raz�n social");
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Nombre comercial");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Apellido Paterno");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Apellido Materno");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Primer Nombre");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Segundo nombre");
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Grupo");
		sr1.setData(listaGruposSocioNegocio.get(0).getGroupName());
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Moneda");
		sr1.setData("Monedas (todas)");
		searchResults.add(sr1);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvPrincipal.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);

	}

	private void llenarListaGeneral() {

		searchResults3 = new ArrayList<FormatCustomListView>();

		lvGeneral = (ListView) v.findViewById(R.id.lvGeneralSN);

		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Tel�fono 1");
		sr1.setIcon(iconId);
		searchResults3.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tel�fono 2");
		sr1.setIcon(iconId);
		searchResults3.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Tel�fono m�vil");
		sr1.setIcon(iconId);
		searchResults3.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Correo electr�nico");
		sr1.setIcon(iconId);
		searchResults3.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Empleado del departamento de ventas");
		sr1.setData(nombreEmpleado);
		searchResults3.add(sr1);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults3);

		lvGeneral.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvGeneral);

	}

	private void llenarListaContacto() {

		searchResults4 = new ArrayList<FormatCustomListView>();

		lvContacto = (ListView) v.findViewById(R.id.lvContactoSN);

		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Contacto Principal");
		sr1.setIcon(iconId);
		searchResults4.add(sr1);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults4);

		lvContacto.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvContacto);

	}

	private void llenarListaDirecciones() {

		searchResults1 = new ArrayList<FormatCustomListView>();
		lvDirecciones = (ListView) v.findViewById(R.id.lvDireccionesSN);

		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Direcci�n Principal");
		sr1.setIcon(iconId);
		searchResults1.add(sr1);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults1);
		lvDirecciones.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvDirecciones);

	}

	private void llenarListaCondPago() {

		searchResults2 = new ArrayList<FormatCustomListView>();

		lvCondicionesPago = (ListView) v.findViewById(R.id.lvConPagSN);

		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Condici�n de pago");
		sr.setIcon(iconId);
		sr.setData(listaCondicionPago.get(0).getDescripcionCondicion());
		searchResults2.add(sr);

		sr = new FormatCustomListView();
		sr.setTitulo("Lista de precios");
		sr.setData(listaPrecios.get(0).getNombre());
		sr.setIcon(iconId);
		searchResults2.add(sr);

		sr = new FormatCustomListView();
		sr.setTitulo("Indicador");
		sr.setIcon(iconId);
		sr.setData(listaIndicadores.get(0).getNombre());
		searchResults2.add(sr);
		
		sr = new FormatCustomListView();
		sr.setTitulo("Zona");
		sr.setIcon(iconId);
		sr.setData(listaZonas.get(0).getNombre());
		searchResults2.add(sr);

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults2);
		lvCondicionesPago.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvCondicionesPago);

	}

	// METODOS PARA LLENAR LAS LISTAS DE "MENUS" EN LA PANTALLA PRINCIPAL DEL
	// SOCIO DE NEGOCIO

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//Reset utils
		contactId = 1;
		directionIdFiscal = 1;
		directionIdEntrega = 1;
		utilId = 1;
		utilId2 = 1;
		listaDetalleContactos.clear();
		listaDirecciones.clear();
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 16908332:
			getActivity().finish();
			return true;
		case R.id.action_registrar:
			
			if (searchResults.get(2).getData() == null
					|| searchResults.get(2).getData().equals("")) {

				Toast.makeText(contexto,
						"Seleccione el tipo de documento del socio de negocio",
						Toast.LENGTH_SHORT).show();
		
			}else if (searchResults.get(3).getData() == null
					|| searchResults.get(3).getData().equals("")) {

				Toast.makeText(contexto,
						"Ingrese el n�mero de documento del socio de negocio",
						Toast.LENGTH_SHORT).show();

			}else if(searchResults.get(5).getData() == null
					|| searchResults.get(5).getData().equals("")){
				Toast.makeText(contexto,
						"Raz�n social es requerida",
						Toast.LENGTH_SHORT).show();
			}
//				else if(listaDirecciones.size() == 0){
//			
//				Toast.makeText(contexto,
//						"Debe agregar una direcci�n",
//						Toast.LENGTH_LONG).show();
//				
//			}
			else {

				Insert insert = new Insert(contexto);
				
				// Bloque titulo
				objSN = new SocioNegocioBean();

				if (searchResults.size() == 9) {

					objSN.setTipoCliente("L");
					if (tipoPerSel != null)
						objSN.setTipoPersona(tipoPerSel.getCodigo());
					else
						objSN.setTipoPersona("TPJ");
					if (tipoDocSel != null)
						objSN.setTipoDoc(tipoDocSel.getCodigo());
					else
						objSN.setTipoDoc("0");
					objSN.setNroDoc(searchResults.get(3).getData());
					objSN.setCodigo(claveMovil);
					objSN.setNombRazSoc(searchResults.get(5).getData());
					objSN.setNomCom(searchResults.get(6).getData());
					if (grupoSel != null)
						objSN.setGrupo(grupoSel.getGroupCode());
					else
						objSN.setGrupo(listaGruposSocioNegocio.get(0).getGroupCode());
					objSN.setMoneda("#");

				} else {

					objSN.setTipoCliente("L");
					if (tipoPerSel != null)
						objSN.setTipoPersona(tipoPerSel.getCodigo());
					else
						objSN.setTipoPersona("TPJ");
					if (tipoDocSel != null)
						objSN.setTipoDoc(tipoDocSel.getCodigo());
					else
						objSN.setTipoDoc("0");
					objSN.setNroDoc(searchResults.get(3).getData());
					objSN.setCodigo(searchResults.get(4).getData());
					objSN.setNombRazSoc(searchResults.get(5).getData());
					objSN.setNomCom(searchResults.get(6).getData());
					objSN.setApePat(searchResults.get(7).getData());
					objSN.setApeMat(searchResults.get(8).getData());
					objSN.setPriNom(searchResults.get(9).getData());
					objSN.setSegNom(searchResults.get(10).getData());
					if (grupoSel != null)
						objSN.setGrupo(grupoSel.getGroupCode());
					else
						objSN.setGrupo(listaGruposSocioNegocio.get(0).getGroupCode());
					objSN.setMoneda("#");

				}

				// Bloque general
				objSN.setTlf1(searchResults3.get(0).getData());
				objSN.setTlf2(searchResults3.get(1).getData());
				objSN.setTlfMov(searchResults3.get(2).getData());
				objSN.setCorreo(searchResults3.get(3).getData());
				objSN.setEmpleadoVentas(codigoEmpleado);
				objSN.setDireccionFiscal(searchResults1.get(0).getData());
			
				// Contactos
				objSN.setContactos(listaDetalleContactos);
				
				int contadorDireccionFiscal = 0;
				int contadorDireccionEntrega = 0;
				
				if(listaDirecciones.size() >0){
					for (DireccionBean direccion : listaDirecciones) {
						if(!direccion.getTipoDireccion().equalsIgnoreCase("B"))
							contadorDireccionEntrega++;
						else
							contadorDireccionFiscal++;
					}
					
					if(contadorDireccionFiscal == 0){
						Toast.makeText(contexto,
								"Debe agregar una direcci�n fiscal",
								Toast.LENGTH_LONG).show();
						return true;
					}
					
					if(contadorDireccionEntrega == 0){
				
						DireccionBean bean = null;
						for (DireccionBean direccion : listaDirecciones) {
							bean = new DireccionBean();
							bean.setTipoDireccion("S");
							bean.setIDDireccion("Entrega1");
							bean.setPais(direccion.getPais());
							bean.setDepartamento(direccion.getDepartamento());
							bean.setProvincia(direccion.getProvincia());
							bean.setDistrito(direccion.getDistrito());
							bean.setCalle(direccion.getCalle());
							bean.setReferencia(direccion.getReferencia());
							bean.setPrincipal(false);
							break;
						}
						
						listaDirecciones.add(bean);
					}
					
					// Direcciones
					objSN.setDirecciones(listaDirecciones);
				}
				
				
				// Bloque condiciones de pago
				if (condPagoSel != null)
					objSN.setCondPago(condPagoSel.getNumeroCondicion());
				else
					objSN.setCondPago(listaCondicionPago.get(0).getNumeroCondicion());
				if(listaPreSel != null)
					objSN.setListaPrecio(listaPreSel.getCodigo());
				if(zonaSel != null)
					objSN.setZona(zonaSel.getCodigo());
				if(indicadorSel != null)
					objSN.setIndicador(indicadorSel.getCodigo());
				objSN.setCreadoMovil("Y");
				objSN.setClaveMovil(claveMovil);
				objSN.setValidoenPedido("Y");
				objSN.setEstadoRegistroMovil(getResources().getString(R.string.LOCAL));
				objSN.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
				
				//Mandar el objeto a registro
				boolean res = insert.insertSocioNegocio(objSN);

				if(res){
					
					//Actualizar el correlativo para la clave movil
					insert.updateCorrelativo("CNT");
					insert.close();
					
					//Mostrar el mensaje de �xito
					Toast.makeText(contexto, "Socio de Negocio registrado",
							Toast.LENGTH_LONG).show();
					
					//Reset utils
					contactId = 1;
					directionIdFiscal = 1;
					directionIdEntrega = 1;
					utilId = 1;
					utilId2 = 1;
					
					
					//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE SOCIOS DE NEGOCIO
		        	 Intent localBroadcastIntent = new Intent("event-send-register-bp-ok");
		        	 Activity activity = getActivity();
		        	 if(activity != null){
		        		 LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
			             myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
			             
			          // Registro en el webservice
							// COMPROBAR EL ESTADO DE LA RED MOVIL DE DATOS
							boolean wifi = Connectivity.isConnectedWifi(contexto);
							boolean movil = Connectivity.isConnectedMobile(contexto);
							boolean isConnectionFast = Connectivity.isConnectedFast(contexto);

							if (wifi || movil && isConnectionFast) {

								new TareaRegistroBP().execute();

							} else {

								listaDetalleContactos.clear();
								listaDirecciones.clear();

							}
			             
			           //eliminar instancia para liberar memoria y cerrar fragment
						FragmentManager manager = getFragmentManager();
						FragmentTransaction transaction = manager.beginTransaction();
						transaction.remove(this);
						transaction.commit();
						getActivity().finish();
			             
		        	 }else{
		        		 Toast.makeText(contexto, "No activity attach", Toast.LENGTH_SHORT).show();
		        	 }
					
					
				}else{
					Toast.makeText(contexto, "No se registr� el socio de negocio, compruebe los datos", 
							Toast.LENGTH_LONG).show();
				}
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	

	;

	private class TareaRegistroBP extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... arg0) {

			InvocaWS ws = new InvocaWS(contexto);

			res = ws.AgregarSocioNegocio(objSN, listaDetalleContactos,
					listaDirecciones);

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {

			if (res == null || res.equalsIgnoreCase("anytype{}")) {

				Insert insert = new Insert(contexto);
				insert.updateEstadoSocioNegocio(objSN.getClaveMovil());
				insert.close();

				Toast.makeText(contexto,
						"Enviado al servidor",
						Toast.LENGTH_LONG).show();
				listaDetalleContactos.clear();
				listaDirecciones.clear();

			} else {

				Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();
				listaDetalleContactos.clear();
				listaDirecciones.clear();

			}

		}

	}

}
