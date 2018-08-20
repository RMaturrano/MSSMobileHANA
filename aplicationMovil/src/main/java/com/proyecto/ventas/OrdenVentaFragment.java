package com.proyecto.ventas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.GrupoUnidadMedidaBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.ClienteDAO;
import com.proyecto.dao.ImpuestoDAO;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.database.Update;
import com.proyecto.sociosnegocio.ClienteBuscarActivity;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;
import com.proyecto.sociosnegocio.util.ContactoBuscarBean;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;

public class OrdenVentaFragment extends Fragment {
	
	public static boolean shouldThreadContinueoV = false;
	public static String KEY_PAR_CLIENTE = "cliente";

	private String res = "";
	private String action = "";
	private String claveVenta = "";

	//PARA EL REGISTRO LOCAL
	private OrdenVentaBean ordBean = null;
	private OrdenVentaDetalleBean ordDetBean = null;
	private ArrayList<OrdenVentaDetalleBean> listaOrdDet = null;
	//PARA EL REGISTRO LOCAL

	private Location mCurrentLocation = null;
	private View v = null;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private ConstruirAlert alert = new ConstruirAlert();
	
	//Preferencias
	private SharedPreferences pref;
	private String codigoEmpleado;
	private String nombreEmpleado;
	private String idDispositivo;
	private String nroOrd = "";
	private String tipoDocumento = "";

	private ListViewCustomAdapterTwoLinesAndImg adapter;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ListView lvContenido = null;
	private ListView lvLogistica = null;
	private ListView lvFinanzas = null;
	private ListView lvCalculos = null;
	private ListView lvDirecciones = null;
	private RadioGroup rg = null;
	private MonedaBean monSel = null;

	boolean wifi = false;
	boolean movil = false;
	boolean isConnectionFast = false;

	//Nro de art�culos PARA EL DETALLE
	public static ArrayList<ArticuloBean> listaDetalleArticulos = new ArrayList<ArticuloBean>();
	
	//CONTACTOS DEL SOCIO DE NEGOCIO ACTUAL
	public static ArrayList<ContactoBean> listaContactoSN = new ArrayList<ContactoBean>();
	
	//DIRECCIONES DEL SOCIO DE NEGOCIO ACTUAL
	public static ArrayList<DireccionBean> listaDireccionSN = new ArrayList<DireccionBean>();
	
	//LISTA DE PRECIOS DEL SOCIO DE NEGOCIO ACTUAL
	private String codigoListaPrecioSN;
	
	//CONDICIONES DE PAGO DEL SOCIO DE NEGOCIO ACTUAL
	private String codigoCondicionPagoSN;
	
	//INDICADOR DEL SOCIO DE NEGOCIO ACTUAL
	private String codigoIndicadorSN;
	
	//Variables para los totales
	public static double totalAntesDescuento = 0;
	public static double porcentajeDescuento = 0;
	public static double totalDescuento = 0;
	public static double totalImpuesto = 0;
	public static double totalGeneral = 0;
	
	//LISTAS PERSONALIZABLES (ITEM fDEL LISTVIEW) (Utilizando el formato de socio de negocio para acortar c�digo)
	private ArrayList<FormatCustomListView> searchResults = null;
	private ArrayList<FormatCustomListView> searchResults1 = null;
	private ArrayList<FormatCustomListView> searchResults2 = null;
	private ArrayList<FormatCustomListView> searchResults3 = null;
	private ArrayList<FormatCustomListView> searchResults4 = null;
			
	//Objeto que tomar� al ser seleccionado (Ayuda al update del select item con popup
	private FormatCustomListView fullObject = null;
	private int posicion = 0;
	private String claveMovil = "";
	private String estadoRegistroMovil = "";
	
	//Listas desde sqlite
	private ArrayList<MonedaBean> listaMonedas;
	private ArrayList<CondicionPagoBean> listaCondicionPago = null;
	private ArrayList<ListaPrecioBean> listaPrecios = null;
	private ArrayList<IndicadorBean> listaIndicadores = null;
			
	//SELECCIONES
	private CondicionPagoBean condPagoSel = null;
	private CondicionPagoBean condPagoInicial = null;
	private IndicadorBean indicadorSel = null;
	public static ListaPrecioBean listaPrecioSel = null;
	private ClienteBuscarBean mClienteSeleccionado = null;
	private ContactoBuscarBean mContactoSeleccionado = null;
	private ContactoBuscarBean mContactoTempSeleccionado = null;

	private DireccionBuscarBean mDireccionFiscalSeleccionada = null;
	private DireccionBuscarBean mDireccionFiscalTempSeleccionada = null;

	private DireccionBuscarBean mDireccionEntregaSeleccionada = null;
	private DireccionBuscarBean mDireccionEntregaTempSeleccionada = null;
	
	//region BROADCASTRECEIVER
	//RECIBE LOS PAR�METROS DESDE EL FRAGMENT CORRESPONDIENTE
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {

		  	try{
				Bundle bundle = intent.getExtras();

				if (bundle != null) {

					if(intent.getAction().equals("event-send-bp-to-ordr")){

						String cod = bundle.getString("cod");
						String name = bundle.getString("name");
						String[] extras = bundle.getString("extras").toString().split(",");

						if(!extras[0].equals("") && !extras[0].equalsIgnoreCase("anytype{}")){
							codigoListaPrecioSN = extras[0];
							for (ListaPrecioBean bean : listaPrecios) {
								if(bean.getCodigo().equals(codigoListaPrecioSN)){
									listaPrecioSel = bean;
									break;
								}
							}
				        /*	Object o = lvFinanzas.getItemAtPosition(2);
			        		fullObject = new FormatCustomListView();
			            	fullObject = (FormatCustomListView)o;
			            	fullObject.setData(listaPrecioSel.getNombre());
			            	searchResults4.set(2, fullObject);
			            	lvFinanzas.invalidateViews();	*/
						}else{
							listaPrecioSel = null;
				        /*	Object o = lvFinanzas.getItemAtPosition(2);
			        		fullObject = new FormatCustomListView();
			            	fullObject = (FormatCustomListView)o;
			            	fullObject.setData("");
			            	searchResults4.set(2, fullObject);	*/
							lvFinanzas.invalidateViews();
						}


						if(!extras[1].equals("") && !extras[1].equalsIgnoreCase("anytype{}")){
							codigoCondicionPagoSN = extras[1];
							for (CondicionPagoBean bean : listaCondicionPago) {
								if(bean.getNumeroCondicion().equals(codigoCondicionPagoSN)){
									condPagoSel = bean;
									condPagoInicial = bean;
									break;
								}
							}
							Object o = lvFinanzas.getItemAtPosition(0);
							fullObject = new FormatCustomListView();
							fullObject = (FormatCustomListView)o;
							fullObject.setData(condPagoSel.getDescripcionCondicion());
							if(!condPagoSel.getDescripcionCondicion().equalsIgnoreCase("CONTADO")){
								fullObject.setIcon(iconId);
							}
							searchResults4.set(0, fullObject);
							lvFinanzas.invalidateViews();
						}else{
							condPagoSel = null;
							Object o = lvFinanzas.getItemAtPosition(0);
							fullObject = new FormatCustomListView();
							fullObject = (FormatCustomListView)o;
							fullObject.setData("");
							searchResults4.set(0, fullObject);
							lvFinanzas.invalidateViews();
						}

						if(extras.length > 2 && !extras[2].equals("") && !extras[2].equalsIgnoreCase("anytype{}")){
							codigoIndicadorSN = extras[2];
							for (IndicadorBean bean : listaIndicadores) {
								if(bean.getCodigo().equals(codigoIndicadorSN)){
									indicadorSel = bean;
									break;
								}
							}
							Object o = lvFinanzas.getItemAtPosition(1);
							fullObject = new FormatCustomListView();
							fullObject = (FormatCustomListView)o;
							fullObject.setData(indicadorSel.getNombre());
							searchResults4.set(1, fullObject);
							lvFinanzas.invalidateViews();
						}else{
							indicadorSel = null;
							Object o = lvFinanzas.getItemAtPosition(1);
							fullObject = new FormatCustomListView();
							fullObject = (FormatCustomListView)o;
							fullObject.setData("");
							searchResults4.set(1, fullObject);
							lvFinanzas.invalidateViews();
						}

						//Direccion fiscal
						if(extras.length >3) {
							if (!extras[3].equals("") && !extras[3].equalsIgnoreCase("anytype{}")) {



							} else {
								Object o = lvDirecciones.getItemAtPosition(0);
								fullObject = new FormatCustomListView();
								fullObject = (FormatCustomListView) o;
								fullObject.setData("");
								searchResults2.set(0, fullObject);
								lvDirecciones.invalidateViews();
							}
						}

						//Capturar el objeto (que refleja la selecci�n estado doc)
						Object o = lvPrincipal.getItemAtPosition(1);
						fullObject = new FormatCustomListView();
						fullObject = (FormatCustomListView)o;
						fullObject.setData(cod);
						searchResults.set(1, fullObject);

						Object o2 = lvPrincipal.getItemAtPosition(2);
						fullObject = new FormatCustomListView();
						fullObject = (FormatCustomListView)o2;
						fullObject.setData(name);
						searchResults.set(2, fullObject);
//
						llenarListaContenido();
						lvPrincipal.invalidateViews();
					}else if(intent.getAction().equals("event-send-lines-to-order")){

						//Capturar el objeto
						Object o = lvContenido.getItemAtPosition(0);
						FormatCustomListView fullObject1 = (FormatCustomListView)o;
						fullObject1.setData(listaDetalleArticulos.size() + " articulos");
						searchResults1.set(0, fullObject1);

						lvContenido.invalidateViews();
						llenarListaTotales();
					}
				}
			}catch (Exception e){
		  		showToast(e.getMessage());
			}
		 }
		  
		  
	};
	//endregion
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.pedido_cliente, viewGroup, false);
        
	    clearLists();
	    v = view;
        contexto = view.getContext();
        boolean val = cargarListas();
		
		if(!val){
			getActivity().finish();
			return view;
		}
        
        lvContenido = (ListView) v.findViewById(R.id.lvContPedido);
        
        pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");
		nombreEmpleado = pref.getString(Variables.NOMBRE_EMPLEADO, "");
		idDispositivo = Secure.getString(getActivity().getContentResolver(),
				Secure.ANDROID_ID);

        //LLENAR EL LISTADO DE DATOS QUE COMPONEN LA ORDEN DE VENTA
        llenarListaOrdTit();
        llenarListaDirecciones();
        llenarListaFinanzas();
        
        Intent myIntent = getActivity().getIntent();
        if (myIntent.getStringExtra("action") != null) {
			action = myIntent.getStringExtra("action");
			claveVenta = myIntent.getStringExtra("clave");
			construirDataEditar();
		}
      
        
        //PROGRAMAR EL CLICK HACIA EL BLOQUE PRINCIPAL PARA CADA DATO
        lvPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlert("principal",position);
            }
        
		});
		////
        
        lvContenido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlert("articulos",position);
            }
        
		});
        ////
        
       lvDirecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlert("direcciones",position);
            }
        
		});
       
       
        
        
        lvFinanzas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlert("finanzas",position);
            }
        
		});  			
       
        
        setHasOptionsMenu(true);

        return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		try{
			wifi = Connectivity.isConnectedWifi(contexto);
			movil = Connectivity.isConnectedMobile(contexto);
			isConnectionFast = Connectivity.isConnectedFast(contexto);
			mCurrentLocation = getCurrentLocation();

			if(getActivity().getIntent().getExtras() != null){
				if(getActivity().getIntent().getExtras().containsKey(KEY_PAR_CLIENTE) &&
						getActivity().getIntent().getStringExtra(KEY_PAR_CLIENTE) != null){
					String codigoCliente = getActivity().getIntent().getStringExtra(KEY_PAR_CLIENTE);
					onClientSelected(new ClienteDAO().buscarPorCodigo(codigoCliente));
				}
			}

		}catch (Exception e){
			showToast("onStart() > " + e.getMessage());
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		try{
			//registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
			IntentFilter filter = new IntentFilter("event-send-lines-to-order");
			filter.addAction("event-send-bp-to-ordr");
			LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver, filter);
		}catch (Exception e){
			showToast(e.getMessage());
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		try{
			LocalBroadcastManager.getInstance(contexto).unregisterReceiver(myLocalBroadcastReceiver);
		}catch (Exception e){
			showToast(e.getMessage());
		}
	}

	public void onClientSelected(ClienteBuscarBean cliente){

		try{
			mClienteSeleccionado = cliente;
			updateRowListPrincipal(1,mClienteSeleccionado.getCodigo());
			updateRowListPrincipal(2, mClienteSeleccionado.getNombre());

			if(mClienteSeleccionado.getListaPrecio() != null){
				listaPrecioSel = mClienteSeleccionado.getListaPrecio();
			}else
				listaPrecioSel = null;

			if(mClienteSeleccionado.getCondicionPago() != null){
				condPagoSel = mClienteSeleccionado.getCondicionPago();
				condPagoInicial = condPagoSel;
				updateRowListFinanzas(0, condPagoSel.getDescripcionCondicion());
			}else{
				condPagoSel = null;
				updateRowListFinanzas(0, "");
			}

			if(mClienteSeleccionado.getIndicador() != null){
				indicadorSel = mClienteSeleccionado.getIndicador();
				updateRowListFinanzas(1, indicadorSel.getNombre());
			}else{
				indicadorSel = null;
				updateRowListFinanzas(1,"");
			}

			if(mClienteSeleccionado.getDireccionFiscalCodigo() != null) {
				mDireccionFiscalSeleccionada = new DireccionBuscarBean();
				mDireccionFiscalSeleccionada.setCalle(mClienteSeleccionado.getDireccionFiscalNombre());
				mDireccionFiscalSeleccionada.setTipo(Constantes.TIPO_DIRECCION_FISCAL);
				mDireccionFiscalSeleccionada.setCodigo(mClienteSeleccionado.getDireccionFiscalCodigo());
				updateRowListDirecciones(0, mDireccionFiscalSeleccionada.getCalle());
			}

			autoSeleccionarDireccionMasCercana();
			llenarListaContenido();

		}catch (Exception e){
			mClienteSeleccionado = null;
			showToast("Seleccion de cliente fallida - " + e.getMessage());
		}finally{
			lvFinanzas.invalidateViews();
			lvPrincipal.invalidateViews();
			lvDirecciones.invalidateViews();
        }
	}

	private void updateRowListPrincipal(int position, String dataVal){
		Object o2 = lvPrincipal.getItemAtPosition(position);
		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView)o2;
		fullObject.setData(dataVal);
		searchResults.set(position, fullObject);
	}

	private void updateRowListDirecciones(int position, String dataVal){
		Object o2 = lvDirecciones.getItemAtPosition(position);
		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView)o2;
		fullObject.setData(dataVal);
		searchResults2.set(position, fullObject);
	}

	private void updateRowListFinanzas(int position, String dataVal){
		Object o2 = lvFinanzas.getItemAtPosition(position);
		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView)o2;
		fullObject.setData(dataVal);
		searchResults4.set(position, fullObject);
	}

	private boolean cargarListas(){
		
		boolean pass = true;
		
		listaMonedas = new ArrayList<MonedaBean>();
		listaCondicionPago = new ArrayList<CondicionPagoBean>();
		listaIndicadores = new ArrayList<IndicadorBean>();
		listaPrecios = new ArrayList<ListaPrecioBean>();
		
		Select select = new Select(contexto);
		
		listaMonedas = select.listaMonedas();
		listaCondicionPago = select.listaCondicionPago();
		listaIndicadores = select.listaIndicadores();
		listaPrecios = select.listaPrecios();
		
		select.close();
		
		if(listaMonedas.size()>0  && listaCondicionPago.size()>0 && listaIndicadores.size()>0){
			pass = true;
		}else{
			Toast.makeText(contexto, "Uno o mas datos maestros no han sido cargados", Toast.LENGTH_LONG).show();
			select.close();
			pass = false;
		}
		
		return pass;
		
	}
	
	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) 
	private void llenarListaOrdTit(){
		
		//FECHA ACTUAL
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String currentDate = dateFormat.format(date);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			c.add(Calendar.DATE, 1);
		}
		date = c.getTime();

		String dispatcherDay = dateFormat.format(date);
    	//FECHA ACTUAL
    	
    	
    	//correlativo
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		Cursor rs= db.rawQuery("select NUM_COR" +
				" from TB_COR WHERE COD_COR ='ORD'", null); 
		
		while (rs.moveToNext()) {		
			nroOrd = String.valueOf(rs.getInt(0));
		}
		rs.close();

		String fullDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.ENGLISH).format(date);
		String nroDoc = idDispositivo +"-"+fullDate+"-" + nroOrd;
		//correlativo
		
		
		searchResults = new ArrayList<FormatCustomListView>();
		lvPrincipal = (ListView) v.findViewById(R.id.lvPrinPedido);

		FormatCustomListView sr = new FormatCustomListView();
    	sr.setTitulo("Numero de documento");
    	sr.setData(nroDoc);
    	searchResults.add(sr);
		
		sr = new FormatCustomListView();
    	sr.setTitulo("Codigo de SN");
    	if(action.equals("")){
    		sr.setIcon(iconId);
    	}
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Nombre de SN");
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Contacto");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Moneda");
    	sr.setData(listaMonedas.get(0).getDescripcion());
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Empleado del departamento de ventas");
    	sr.setData(nombreEmpleado);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Comentarios");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Fecha contable");
    	sr.setData(currentDate);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Fecha de entrega");
    	sr.setData(dispatcherDay);
		sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	/*sr = new FormatCustomListView();
    	sr.setTitulo("Referencia");
    	sr.setIcon(iconId);
    	searchResults.add(sr);	*/
    	
    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
    	lvPrincipal.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}
	
	private void llenarListaFinanzas(){
		
	    
	    searchResults4 = new ArrayList<FormatCustomListView>();

	    lvFinanzas = (ListView) v.findViewById(R.id.lvFinPedido);
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Condicion pago");
		//sr.setIcon(iconId);
		searchResults4.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Indicador");
    	searchResults4.add(sr);
    	
    /*	sr = new FormatCustomListView();
    	sr.setTitulo("Lista de precios");
    	searchResults4.add(sr);
    	*/

    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults4);
    	lvFinanzas.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvFinanzas);
	    
	}

	private void llenarListaDirecciones(){
		
		searchResults2 = new ArrayList<FormatCustomListView>();

	    lvDirecciones = (ListView) v.findViewById(R.id.lvLogPedido);

	    FormatCustomListView sr = new FormatCustomListView();
    	sr.setTitulo("Direccion fiscal");
    	sr.setIcon(iconId);
    	searchResults2.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Direccion de entrega");
    	sr.setIcon(iconId);
    	searchResults2.add(sr);
    	
    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults2);
    	lvDirecciones.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvDirecciones);
		
	}
	
	private void llenarListaTotales(){
		
		searchResults3 = new ArrayList<FormatCustomListView>();

		lvCalculos = (ListView) v.findViewById(R.id.lvCalculos);
		
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Total antes del descuento");
		sr.setData(String.valueOf(totalAntesDescuento));
		searchResults3.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("% descuento");
    	sr.setData(String.valueOf(porcentajeDescuento));
    	searchResults3.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Descuento");
    	sr.setData(String.valueOf(totalDescuento));
    	searchResults3.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Impuesto");
    	sr.setData(String.valueOf(totalImpuesto));
    	searchResults3.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Total");
    	sr.setData(String.valueOf(totalGeneral));
    	searchResults3.add(sr);
    	
    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults3);
    	lvCalculos.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvCalculos);
		
	}
	
	private void llenarListaContenido(){

		searchResults1 = new ArrayList<FormatCustomListView>();
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Articulos");
		sr.setData(listaDetalleArticulos.size() + " articulos");
		sr.setIcon(iconId);
		searchResults1.add(sr);
    	
        adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults1);
        lvContenido.setAdapter(adapter);
        DynamicHeight.setListViewHeightBasedOnChildren(lvContenido);
		
	}

	private String obtenerFecha(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		String currentDate = dateFormat.format(new Date());
		return currentDate;
	}
	
	//CONSTRUIR ALERTS
	private void construirAlert(String bloque, final int position){
		
		
		//Alerts del bloque principal
		if(bloque.equals("principal")){
			
			if(position == 1){
				
				if(action.equals("")){

					/*
					BuscarSNFragment fragment = new BuscarSNFragment();
					
	                FragmentManager manager = getFragmentManager();
	                manager.saveFragmentInstanceState(this);
	                FragmentTransaction transaction = manager.beginTransaction();
	                transaction.hide(this);
	                transaction.add(R.id.box, fragment);
	                transaction.addToBackStack(null);
	                transaction.commit();
	                
	                getActivity().setTitle("Buscar Socio de Negocio");
	                */

					getActivity().startActivityForResult(new Intent(v.getContext(), ClienteBuscarActivity.class),
							ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE);
				}
				
			}else if(position == 3){
				
				if(mClienteSeleccionado != null){
					if(mClienteSeleccionado.getContactos().size() > 0) {

						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
								v.getContext(), android.R.layout.select_dialog_singlechoice
						);

						for (ContactoBuscarBean bean: mClienteSeleccionado.getContactos()) {
							arrayAdapter.add(bean.getCodigo() + " - " + bean.getNombre());
						}

						final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
						alertDialog.setTitle("Contactos del cliente");
						alertDialog.setCancelable(false);
						alertDialog.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mContactoTempSeleccionado = mClienteSeleccionado.getContactos().get(which);
							}
						});
						alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(mContactoTempSeleccionado != null) {
									mContactoSeleccionado = mContactoTempSeleccionado;
									updateRowListPrincipal(position, mContactoSeleccionado.getNombre());
									lvPrincipal.invalidateViews();
								}
								mContactoTempSeleccionado = null;
							}
						});
						alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mContactoTempSeleccionado = null;
							}
						});
						alertDialog.show();

					}else
						showToast("El cliente no tiene contactos registrados.");
				}else
					showToast("Primero, debe seleccionar a un cliente.");
				
			}else if(position == 4){
				
//				//Moneda
//				posicion = position;
//				
//				//Capturar el objeto
//				Object o = lvPrincipal.getItemAtPosition(position);
//        		fullObject = new FormatCustomListView();
//            	fullObject = (FormatCustomListView)o;
//				
//				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//        		alert.setTitle("Moneda");
//
//        		
//        		//Spinner
//        		final Spinner spnMon = new Spinner(contexto);
//        		
//        		ArrayAdapter<MonedaBean> adapter = new ArrayAdapter<MonedaBean>(contexto, 
//						android.R.layout.simple_list_item_1,
//						listaMonedas);
//        		spnMon.setAdapter(adapter);
//        		spnMon.setOnItemSelectedListener(new OnItemSelectedListener() {
//    				
//					@Override
//					public void onItemSelected(AdapterView<?> parent,
//							View arg1, int pos, long arg3) {
//						//MONEDA SELECCIONADA
//						monSel = new MonedaBean();
//						monSel = (MonedaBean) parent.getItemAtPosition(pos);
//						
//					}
//
//					@Override
//					public void onNothingSelected(
//							AdapterView<?> arg0) {
//						
//						
//					}
//				});
//        		
//        		alert.setView(spnMon);
//        		
//        		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//        		public void onClick(DialogInterface dialog, int whichButton) {
//
//        		  // Do something with value!
//        			fullObject.setData(monSel.toString());
//					searchResults.set(posicion, fullObject);
//					lvPrincipal.invalidateViews();
//        		  }
//        		});
//
//        		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        		  public void onClick(DialogInterface dialog, int whichButton) {
//        		    // Canceled.
//        		  }
//        		});
//
//        		alert.show();
//				
			}else if (position == 5){
				
			}else if (position == 6){
				
				alert.construirAlert(contexto, position, "Comentarios", searchResults, lvPrincipal, "text",160);
				
			}else if (position == 7){
				
			//	alert.construirAlertDatePicker(contexto, position, "Fecha contable", searchResults, lvPrincipal);
				
			}else if(position == 8){
				
				alert.construirAlertDatePicker(contexto, position, "Fecha de entrega", searchResults, lvPrincipal);
			
			}else if(position == 9){
				
				alert.construirAlert(contexto, position, "Referencia", searchResults, lvPrincipal, "text",100);
				
			}
			
			////fin bloque 1
			
		}else if(bloque.equals("articulos")){
			
			contruirAlertArticulos(position);
			
		}else if(bloque.equals("logistica")){
			
			construirAlertLogistica(position);
			
		}else if(bloque.equals("direcciones")){
			
			construirAlertDirecciones(position);
			
		}else if(bloque.equals("finanzas")){
			
			construirAlertFinanzas(position);
		}
		
	}
	//FIN CONSTRUIR ALERTS
	
	////Alert direcciones 
	private void construirAlertDirecciones(final int position){
		
		if(position == 0){

			try{
				if(mClienteSeleccionado != null){
					if(mClienteSeleccionado.getDirecciones().size() > 0) {

						final List<DireccionBuscarBean> arrayChoose = new ArrayList<>();
						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(),
								android.R.layout.select_dialog_singlechoice);
						for (DireccionBuscarBean d: mClienteSeleccionado.getDirecciones()) {
							if(d.getTipo() != null && d.getTipo().equals(Constantes.TIPO_DIRECCION_FISCAL)) {
								arrayAdapter.add(d.getCalle());
								arrayChoose.add(d);
							}
						}

						final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
						alertDialog.setTitle("Direcciones del cliente");
						alertDialog.setCancelable(false);
						alertDialog.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mDireccionFiscalTempSeleccionada = arrayChoose.get(which);
							}
						});
						alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (mDireccionFiscalTempSeleccionada != null) {
									mDireccionFiscalSeleccionada = mDireccionFiscalTempSeleccionada;
									updateRowListDirecciones(0, mDireccionFiscalSeleccionada.getCalle());
									lvDirecciones.invalidateViews();
								}
								mDireccionFiscalTempSeleccionada = null;
							}
						});
						alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mDireccionFiscalTempSeleccionada = null;
							}
						});
						alertDialog.show();

					}else
						showToast("El cliente no tiene direcciones registradas.");
				}else
					showToast("Primero, debe seleccionar a un cliente.");
			}catch(Exception e){
				showToast("Ocurrio un error obteniendo las direcciones del cliente " + e.getMessage());
			}
		}else if(position == 1){

			//alert.construirAlert(contexto, position, "Direcci�n de entrega", searchResults2, lvDirecciones, "text",250);
			try{
				if(mClienteSeleccionado != null){
					if(mClienteSeleccionado.getDirecciones().size() > 0) {

						final List<DireccionBuscarBean> arrayChoose = new ArrayList<>();
						final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(v.getContext(),
								android.R.layout.select_dialog_singlechoice);
						for (DireccionBuscarBean d: mClienteSeleccionado.getDirecciones()) {
							if(d.getTipo() != null && d.getTipo().equals(Constantes.TIPO_DIRECCION_ENTREGA)){
								arrayAdapter.add(d.getCalle());
								arrayChoose.add(d);
							}
						}

						final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
						alertDialog.setTitle("Direcciones del cliente");
						alertDialog.setCancelable(false);
						alertDialog.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mDireccionEntregaTempSeleccionada = arrayChoose.get(which);
							}
						});
						alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (mDireccionEntregaTempSeleccionada != null) {
									mDireccionEntregaSeleccionada = mDireccionEntregaTempSeleccionada;
									updateRowListDirecciones(1, mDireccionEntregaSeleccionada.getCalle());
									lvDirecciones.invalidateViews();
								}
								mDireccionEntregaTempSeleccionada = null;
							}
						});
						alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mDireccionEntregaTempSeleccionada = null;
							}
						});
						alertDialog.show();

					}else
						showToast("El cliente no tiene direcciones registradas.");
				}else
					showToast("Primero, debe seleccionar a un cliente.");
			}catch(Exception e){
				showToast("Ocurrio un error obteniendo las direcciones del cliente " + e.getMessage());
			}

		}
	}
	
	
	private void contruirAlertArticulos(int position){
		
		if(position==0){
			
				Fragment fragment = new ListaArtFragment();
				
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(this);
                transaction.add(R.id.box, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
		}
	}

	private void construirAlertLogistica(int position){
		
		if(position == 0){
			
			alert.construirAlert(contexto, position, "Codigo de entrega", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 1){
			
			alert.construirAlert(contexto, position, "Direccion de despacho", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 2){
			
			alert.construirAlert(contexto, position, "Codigo de factura", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 3){
			
			alert.construirAlert(contexto, position, "Direccion de factura", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 4){
			
			alert.construirAlert(contexto, position, "Situacion entre.", searchResults2, lvLogistica, "text",200);
			
		}
		
		
	}
	
	
	private void construirAlertFinanzas(int position){
		
		if(position == 0){
			/*
			if(!condPagoInicial.getDescripcionCondicion().equalsIgnoreCase("CONTADO")){
				posicion = position;
				//Capturar el objeto
				Object o = lvFinanzas.getItemAtPosition(position);
	    		fullObject = new FormatCustomListView();
	        	fullObject = (FormatCustomListView)o;
	        	//
	        	
	        	//Spinner
	    		final Spinner spnCondPago = new Spinner(contexto);
	    		
	    		
	    		ArrayAdapter<CondicionPagoBean> adap= new ArrayAdapter<CondicionPagoBean>(contexto, 
		        		android.R.layout.simple_list_item_1,
		        		listaCondicionPago);
	    		
	    		spnCondPago.setAdapter(adap);
	    		
	    		spnCondPago.setOnItemSelectedListener(new OnItemSelectedListener() {
					
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						
						CondicionPagoBean util = (CondicionPagoBean) parent.getItemAtPosition(pos);
						if(util.getDescripcionCondicion().equalsIgnoreCase("CONTADO")||
								util.getDescripcionCondicion().equalsIgnoreCase(condPagoInicial.getDescripcionCondicion())){
							condPagoSel = new CondicionPagoBean();
							condPagoSel = (CondicionPagoBean) parent.getItemAtPosition(pos);
						}
						
					}

					@Override
					public void onNothingSelected(
							AdapterView<?> arg0) {
						
					}
				});
	        	
	        	
				
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
	    		alert.setTitle("Condicion de Pago");

	    		// Set an EditText view to get user input 
	    		alert.setView(spnCondPago);
	    		
	    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {

	    		  // Do something with value!
	    			if(condPagoSel.getDescripcionCondicion().equalsIgnoreCase("CONTADO")||
	    					condPagoSel.getDescripcionCondicion().equalsIgnoreCase(condPagoInicial.getDescripcionCondicion())){
	    				fullObject.setData(condPagoSel.getDescripcionCondicion());
	    				searchResults4.set(posicion, fullObject);
	    				lvFinanzas.invalidateViews();
	    			}else{
	    				Toast.makeText(contexto, "No puede seleccionar esa condicion de pago", Toast.LENGTH_SHORT).show();
	    			}
					
	    		  }
	    		});

	    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		  public void onClick(DialogInterface dialog, int whichButton) {
	    		    // Canceled.
	    		  }
	    		});

	    		alert.show();
			}
			*/
		}else if(position == 1){
			
//			posicion = position;
//			//Capturar el objeto
//			Object o = lvFinanzas.getItemAtPosition(position);
//    		fullObject = new FormatCustomListView();
//        	fullObject = (FormatCustomListView)o;
//        	//
//        	
//        	//Spinner
//    		final Spinner spnCondPago = new Spinner(contexto);
//    		ArrayAdapter<IndicadorBean> adap= new ArrayAdapter<IndicadorBean>(contexto, 
//	        		android.R.layout.simple_list_item_1,
//	        		listaIndicadores);
//    		
//    		spnCondPago.setAdapter(adap);
//    		
//    		spnCondPago.setOnItemSelectedListener(new OnItemSelectedListener() {
//				
//				@Override
//				public void onItemSelected(AdapterView<?> parent,
//						View arg1, int pos, long arg3) {
//					
//					indicadorSel = new IndicadorBean();
//					indicadorSel = (IndicadorBean) parent.getItemAtPosition(pos);
//					
//				}
//
//				@Override
//				public void onNothingSelected(
//						AdapterView<?> arg0) {
//					
//				}
//			});
//        	
//        	
//			
//			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//    		alert.setTitle("Indicador");
//
//    		// Set an EditText view to get user input 
//    		alert.setView(spnCondPago);
//    		
//    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//    		public void onClick(DialogInterface dialog, int whichButton) {
//
//    		  // Do something with value!
//    			fullObject.setData(indicadorSel.getNombre());
//    			searchResults4.set(posicion, fullObject);
//				lvFinanzas.invalidateViews();
//    		  }
//    		});
//
//    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//    		  public void onClick(DialogInterface dialog, int whichButton) {
//    		    // Canceled.
//    		  }
//    		});
//
//    		alert.show();
			
		}else if(position == 2){
			
//			posicion = position;
//			//Capturar el objeto
//			Object o = lvFinanzas.getItemAtPosition(position);
//    		fullObject = new FormatCustomListView();
//        	fullObject = (FormatCustomListView)o;
//        	//
//        	
//        	//Spinner
//    		final Spinner spnCondPago = new Spinner(contexto);
//    		ArrayAdapter<ListaPrecioBean> adap= new ArrayAdapter<ListaPrecioBean>(contexto, 
//	        		android.R.layout.simple_list_item_1,
//	        		listaPrecios);
//    		
//    		spnCondPago.setAdapter(adap);
//    		
//    		spnCondPago.setOnItemSelectedListener(new OnItemSelectedListener() {
//				
//				@Override
//				public void onItemSelected(AdapterView<?> parent,
//						View arg1, int pos, long arg3) {
//					
//					listaPrecioSel = new ListaPrecioBean();
//					listaPrecioSel = (ListaPrecioBean) parent.getItemAtPosition(pos);
//					
//				}
//
//				@Override
//				public void onNothingSelected(
//						AdapterView<?> arg0) {
//					
//				}
//			});
//        	
//        	
//			
//			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//    		alert.setTitle("Lista de precios");
//
//    		// Set an EditText view to get user input 
//    		alert.setView(spnCondPago);
//    		
//    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//    		public void onClick(DialogInterface dialog, int whichButton) {
//
//    		  // Do something with value!
//    			fullObject.setData(listaPrecioSel.getNombre());
//    			searchResults4.set(posicion, fullObject);
//				lvFinanzas.invalidateViews();
//    		  }
//    		});
//
//    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//    		  public void onClick(DialogInterface dialog, int whichButton) {
//    		    // Canceled.
//    		  }
//    		});
//
//    		alert.show();
			
			
		}
		
	}
	
		
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.main, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	FragmentManager manager = getActivity().getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
    	
		switch (item.getItemId()) {
			case R.id.action_registrar:

				try{

				if (mClienteSeleccionado == null) {
					Toast.makeText(contexto, "Seleccione el socio de negocio", Toast.LENGTH_LONG).show();
					return false;
				} else if (searchResults.get(1).getData() == null || searchResults.get(1).getData().equals("")) {
					Toast.makeText(contexto, "Seleccione el socio de negocio", Toast.LENGTH_LONG).show();
					return false;
				} else if (listaDetalleArticulos.size() == 0) {
					Toast.makeText(contexto, "Agregue articulos a la orden", Toast.LENGTH_LONG).show();
					return false;
				} else if(mDireccionEntregaSeleccionada == null){
					Toast.makeText(contexto, "Seleccione una direccion de entrega", Toast.LENGTH_LONG).show();
					return false;
				}

				ordBean = new OrdenVentaBean();
				if (tipoDocumento.equals(""))
					ordBean.setTipoDoc("P");
				else
					ordBean.setTipoDoc(tipoDocumento);
				ordBean.setNumero(searchResults.get(0).getData());
				ordBean.setCodSN(searchResults.get(1).getData());
				ordBean.setNomSN(searchResults.get(2).getData());
				if (mContactoSeleccionado != null)
					ordBean.setContacto(String.valueOf(mContactoSeleccionado.getCodigo()));
				if (monSel != null)
					ordBean.setMoneda(monSel.getCodigo());
				else
					ordBean.setMoneda(listaMonedas.get(0).getCodigo());
				ordBean.setEmpVentas(codigoEmpleado);
				ordBean.setComentario(searchResults.get(6).getData());
				ordBean.setFecContable(StringDateCast.castDatetoDateWithoutSlash(searchResults.get(7).getData()));
				ordBean.setFecVen(StringDateCast.castDatetoDateWithoutSlash(searchResults.get(8).getData()));
				//ordBean.setReferencia(searchResults.get(9).getData());
				ordBean.setHoraCreacion(obtenerFecha("HH:mm"));
				ordBean.setModoOffLine((wifi || movil) ? "N" : "Y");

				mCurrentLocation = getCurrentLocation();

				if(mCurrentLocation != null){
					ordBean.setLatitud(String.valueOf(mCurrentLocation.getLatitude()));
					ordBean.setLongitud(String.valueOf(mCurrentLocation.getLongitude()));
					ordBean.setRangoDireccion(rangoDireccion(mCurrentLocation));
				}

				//Bloque direcciones
				if (mDireccionFiscalSeleccionada != null)
					ordBean.setDirFiscal(mDireccionFiscalSeleccionada.getCodigo());

				if (mDireccionEntregaSeleccionada != null)
					ordBean.setDirEntrega(mDireccionEntregaSeleccionada.getCodigo());


				//Bloque finanzas
				if (condPagoSel != null)
					ordBean.setCondPago(condPagoSel.getNumeroCondicion());
				if (indicadorSel != null)
					ordBean.setIndicador(indicadorSel.getCodigo());
				if (listaPrecioSel != null)
					ordBean.setListaPrecio(listaPrecioSel.getCodigo());


				//Bloque calculos
				if (searchResults3 != null)
					ordBean.setSubTotal(searchResults3.get(0).getData());
				else {
					Toast.makeText(contexto, "Confirme los articulos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
					return true;
				}
				if (searchResults3.get(1) != null)
					ordBean.setPorcDesc(Double.parseDouble(searchResults3.get(1).getData()));
				else {
					Toast.makeText(contexto, "Confirme los articulos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
					return true;
				}
				if (searchResults3.get(3) != null)
					ordBean.setTotDesc(Double.parseDouble(searchResults3.get(2).getData()));
				else {
					Toast.makeText(contexto, "Confirme los articulos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
					return true;
				}
				ordBean.setImpuesto(searchResults3.get(3).getData());
				ordBean.setTotal(searchResults3.get(4).getData());
				ordBean.setCreadoMovil("Y");

				Date date = new Date();
				String fullDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(date);
				if (action.equals(""))
					claveMovil = idDispositivo + "-" + fullDate + "-" + nroOrd;

				ordBean.setClaveMovil(claveMovil);
				ordBean.setClave(claveMovil);
				ordBean.setEstadoRegistroMovil(getResources().getString(R.string.LOCAL));

				listaOrdDet = new ArrayList<OrdenVentaDetalleBean>();
				//DETALLES
				for (ArticuloBean art : listaDetalleArticulos) {

					ordDetBean = new OrdenVentaDetalleBean();
					ordDetBean.setNroDocOrdV(ordBean.getClave());
					ordDetBean.setCodArt(art.getCod());
					ordDetBean.setDescripcion(art.getDesc());
					ordDetBean.setCodUM(art.getCodUM());
					ordDetBean.setAlmacen(art.getAlmacen());
					ordDetBean.setCantidad(art.getCant());
					ordDetBean.setListaPrecio(art.getCodigoListaPrecio());
					ordDetBean.setPrecio(art.getPre());
					ordDetBean.setDescuento(art.getDescuento());
					ordDetBean.setCodImp(art.getCodigoImpuesto());
					ordDetBean.setLinea(art.getUtilLinea());
					listaOrdDet.add(ordDetBean);

				}

				ordBean.setDetalles(listaOrdDet);

				if (action.equals("")) {

					ordBean.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));

					Insert insert = new Insert(contexto);
					boolean res = insert.insertOrdenVenta(ordBean);

					if (res) {

						//Actualizar el correlativo para la clave movil
						insert.updateCorrelativo("ORD");
						insert.close();

						Toast.makeText(contexto, "Orden de venta registrada", Toast.LENGTH_LONG).show();

						Activity activity = getActivity();

						if (activity != null) {

							//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE ORDENES DE VENTA
							Intent localBroadcastIntent = new Intent("event-send-register-ov-ok");
							LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(activity);
							myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);


							if (wifi || movil && isConnectionFast) {

								//new TareaRegistroOrd().execute();
								showToast("Enviando al servidor...");
								addDocument();

							} else {
								clearLists();
							}

							transaction.remove(this);
							transaction.commit();
							activity.finish();

						}


					} else {
						insert.close();
						Toast.makeText(contexto, "No se registro la orden de venta, compruebe los datos",
								Toast.LENGTH_LONG).show();
					}
				} else {

					if (!estadoRegistroMovil.equals(getResources().getString(R.string.LOCAL))) {
						ordBean.setClave(claveVenta);
						ordBean.setTransaccionMovil(getResources().getString(R.string.ACTUALIZAR_BORRADOR));
					} else {
						ordBean.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
					}


					//Actualizaci�n
					Update update = new Update(contexto);
					boolean res = update.updateOrdenVenta(ordBean);

					if (res) {
						update.close();
						Toast.makeText(contexto, "Orden de venta actualizada", Toast.LENGTH_LONG).show();


						Activity actividad = getActivity();

						if (actividad != null) {

							//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE ORDENES DE VENTA
							Intent localBroadcastIntent = new Intent("event-send-register-ov-ok");
							LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
							myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

							if (wifi || movil && isConnectionFast) {

								new TareaActualizarOrd().execute();

							} else {
								clearLists();
							}

							getActivity().finish();

						} else {
							Toast.makeText(contexto, "No attach activity.", Toast.LENGTH_SHORT).show();
						}

					} else {
						update.close();
						Toast.makeText(contexto, "No se actualizo la orden de venta, compruebe los datos",
								Toast.LENGTH_LONG).show();
					}

				}
			}
			catch(Exception e){
				showToast("Error intentando registrar la orden de venta - " + e.getMessage());
			}
		
            return true;
        case 16908332:
        	clearLists();
	        getActivity().finish();
           return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
	}
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	clearLists();
    }
    
    
    private void clearLists(){
    	listaDireccionSN.clear();
		listaContactoSN.clear();
		listaDetalleArticulos.clear();
//		MainVentas.action = "";
		claveMovil = "";
		tipoDocumento = "";
    }
 
    
    /**********************************************************
     ************************ EDICION *************************
     ***********************************************************/
    // SI SE QUIERE EDITAR CARGAR LOS DATOS INICIALES
    private void construirDataEditar() {
		
    	//Obtener la venta
    	Select select = new Select(contexto);
    	OrdenVentaBean bean = new OrdenVentaBean();
    	bean = select.obtenerOrdenVenta(claveVenta);
    	
    	
    	//Exponer los datos
		searchResults.get(0).setData(bean.getNumero());
		searchResults.get(1).setData(bean.getCodSN());
		searchResults.get(2).setData(select.obtenerNombreSocioNegocio(bean.getCodSN()));
		searchResults.get(3).setData(select.obtenerNombreContacto(bean.getCodSN(), bean.getContacto()));
		
		listaContactoSN = select.listaContactosOV(bean.getCodSN());
		
		for (MonedaBean moneda : listaMonedas) {
			if(moneda.getCodigo().equals(bean.getMoneda())){
				monSel = new MonedaBean();
				monSel = moneda;
				searchResults.get(4).setData(monSel.getDescripcion());
				break;
			}
		}
		searchResults.get(5).setData(nombreEmpleado);
		searchResults.get(6).setData(bean.getComentario());
		searchResults.get(7).setData(StringDateCast.castStringtoDate(bean.getFecContable()));
		searchResults.get(8).setData(StringDateCast.castStringtoDate(bean.getFecVen()));
		searchResults.get(9).setData(bean.getReferencia());
		
		/***/
		
		ArticuloBean articulo = null;
		GrupoUnidadMedidaBean gum = null;
		for (OrdenVentaDetalleBean line : bean.getDetalles()) {
			gum = new GrupoUnidadMedidaBean();
			gum = select.selectGrupoUMArticulo(line.getCodArt());
			
			articulo = new ArticuloBean();
			articulo.setCod(line.getCodArt());
			articulo.setDesc(select.obtenerNombreArticulo(line.getCodArt()));
			articulo.setGrupoArticulo(gum.getCodigo());
			articulo.setNombreGrupoArt(gum.getNombre());
			articulo.setCodUM(line.getCodUM());
			articulo.setNombreUnidadMedida(select.selectnombreUMArticulo(line.getCodArt()));
			articulo.setAlmacen(line.getAlmacen());
			articulo.setCant(line.getCantidad());
			articulo.setCodigoListaPrecio(line.getListaPrecio());
			articulo.setDescripcionListaPrecio(select.selectnombreListaPrecioArticulo(line.getListaPrecio()));
			articulo.setPre(line.getPrecio());
			articulo.setDescuento(line.getDescuento());
			articulo.setCodigoImpuesto(line.getCodImp());

			double tasaImp = new ImpuestoDAO().obtenerTasa(line.getCodImp());

			if(tasaImp == 0)
				articulo.setImpuesto(0);
			else
				articulo.setImpuesto(line.getImp());

			articulo.setUtilIcon(iconId);
			articulo.setUtilLinea(line.getLinea());
			listaDetalleArticulos.add(articulo);
		}
		llenarListaContenido();
		
		/***/
		
		//Direcci�n fiscal
	/*	direccionFiscalSel = new DireccionBean();
		direccionFiscalSel.setIDDireccion(bean.getDirFiscal());
		String[] descripcion = select.obtenerDescripcionDireccion(bean.getCodSN(), bean.getDirFiscal());
		if(descripcion != null){
			if(descripcion[0] != null){
				direccionFiscalSel.setCalle(descripcion[0]);
				direccionFiscalSel.setTipoDireccion("B");
				searchResults2.get(0).setData(direccionFiscalSel.getCalle());
			}else if(descripcion[1] != null && !descripcion[1].equals("")){
				direccionFiscalSel.setReferencia(descripcion[1]);
				direccionFiscalSel.setTipoDireccion("B");
				searchResults2.get(0).setData(direccionFiscalSel.getReferencia());
			}
		}
		
		//Direcci�n entrega
		direccionEntregaSel = new DireccionBean();
		direccionEntregaSel.setIDDireccion(bean.getDirEntrega());
		String[] descripcionE = select.obtenerDescripcionDireccion(bean.getCodSN(), bean.getDirEntrega());
		
		if(descripcionE != null){
			if(descripcionE[0] != null){
				direccionEntregaSel.setCalle(descripcionE[0]);
				direccionEntregaSel.setTipoDireccion("S");
				searchResults2.get(1).setData(direccionEntregaSel.getCalle());
			}else if(descripcionE[1] != null && !descripcionE[1].equals("")){
				direccionEntregaSel.setReferencia(descripcionE[1]);
				direccionEntregaSel.setTipoDireccion("S");
				searchResults2.get(1).setData(direccionEntregaSel.getReferencia());
			}
		}
		*/

		
		listaDireccionSN = select.listaDireccionesOV(bean.getCodSN());
		
		/***/
		
		for (CondicionPagoBean cp : listaCondicionPago) {
			if(cp.getNumeroCondicion().equals(bean.getCondPago())){
				condPagoSel = cp;
				searchResults4.get(0).setData(condPagoSel.getDescripcionCondicion());
				break;
			}
		}
		
		for (IndicadorBean in : listaIndicadores) {
			if(in.getCodigo().equals(bean.getIndicador())){
				indicadorSel = in;
				searchResults4.get(1).setData(indicadorSel.getNombre());
				break;
			}
		}
		
		for (ListaPrecioBean lp : listaPrecios) {
			if(lp.getCodigo().equals(bean.getListaPrecio())){
				listaPrecioSel = lp;
				//searchResults4.get(2).setData(listaPrecioSel.getNombre());
				break;
			}
		}
		
		/***/
		
		totalAntesDescuento = Double.parseDouble(bean.getSubTotal());
		porcentajeDescuento = bean.getPorcDesc();
		totalDescuento = bean.getTotDesc();
		totalImpuesto = Double.parseDouble(bean.getImpuesto());
    	totalGeneral = Double.parseDouble(bean.getTotal());
    	llenarListaTotales();
    	claveMovil = bean.getClaveMovil();
    	tipoDocumento = bean.getTipoDoc();
    	nroOrd = bean.getNumero();
    	estadoRegistroMovil = bean.getEstadoRegistroMovil();
		
		select.close();
		
	}



	//VOLLEY SENDING DOCUMENT
	private void addDocument(){

		final Insert insert = new Insert(contexto);
		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
		String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
		String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
		String sociedad = mSharedPreferences.getString("sociedades", "-1");
		String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

		ordBean.setDetalles(listaOrdDet);
		JSONObject jsonObject = OrdenVentaBean.transformOVToJSON(ordBean, sociedad);

		if(jsonObject != null){
			JsonObjectRequest jsonObjectRequest =
					new JsonObjectRequest(Request.Method.POST, ruta + "salesorder/addSalesOrder.xsjs", jsonObject,
							new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									try
									{
										if(response.getString("ResponseStatus").equals("Success")){
											insert.updateEstadoOrdenVenta(ordBean.getClaveMovil());
											showToast("Enviado al servidor con exito.");

										}else{
											String messageError = response.getJSONObject("Response")
													.getJSONObject("message")
													.getString("value");
											showToast(messageError);
										}

									}catch (Exception e){showToast("Response - " + e.getMessage());}finally {
										clearLists();
									}
								}
							},
							new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {showToast("VolleyError - " + error.getMessage());}
							}){
						@Override
						public Map<String, String> getHeaders() throws AuthFailureError {
							HashMap<String, String> headers = new HashMap<String, String>();
							headers.put("Content-Type", "application/json; charset=utf-8");
							return headers;
						}
					};
			VolleySingleton.getInstance(contexto).addToRequestQueue(jsonObjectRequest);
		}else{
			showToast("Error convirtiendo a JSON los datos del documento.");
		}
	}

	private  void showToast(String message){
		Toast.makeText(contexto, message, Toast.LENGTH_SHORT).show();
	}

	//region MAPS FUNCTIONS

	private Location getCurrentLocation(){
		try{
			LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					1);

			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
					1);

			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
					Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return null;
			}

			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mLocationListener);
			if (!this.checkGPSIsEnabled()) {
				showInfoAlert();
			} else {
				Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				return location;
			}

		/*if (location == null) {
			location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} */
		}catch (Exception e){
			showToast("getCurrentLocation() > " + e.getMessage());
			return null;
		}

		return null;
	}

	private String rangoDireccion(Location currentLocation){
		String rangoResult = Constantes.RANGO_NO_DISPONIBLE;

		try{

			if(mDireccionEntregaSeleccionada != null){
				if(mDireccionEntregaSeleccionada.getLatitud() != null && mDireccionEntregaSeleccionada.getLongitud() != null &&
						!TextUtils.isEmpty(mDireccionEntregaSeleccionada.getLatitud()) &&
						!TextUtils.isEmpty(mDireccionEntregaSeleccionada.getLongitud())){

					Location locationTo = new Location(GPS_PROVIDER);
					locationTo.setLatitude(Double.parseDouble(mDireccionEntregaSeleccionada.getLatitud()));
					locationTo.setLongitude(Double.parseDouble(mDireccionEntregaSeleccionada.getLongitud()));

					float distance = mCurrentLocation.distanceTo(locationTo);

					if(distance > Constantes.DEFAULT_RANGE)
						rangoResult = Constantes.FUERA_DE_RANGO;
					else
						rangoResult = Constantes.DENTRO_DE_RANGO;
				}
			}

		}catch(Exception e){
			showToast("rangoDireccion() > " + e.getMessage());
			return rangoResult;
		}

		return rangoResult;
	}

	private void autoSeleccionarDireccionMasCercana(){

		try{
			mCurrentLocation = getCurrentLocation();

			float bestDistance = -1;

			if(mClienteSeleccionado.getDirecciones() != null && mCurrentLocation != null){
				for (DireccionBuscarBean direccion: mClienteSeleccionado.getDirecciones()) {
					if(direccion.getTipo().equals(Constantes.TIPO_DIRECCION_ENTREGA) &&
							direccion.getLatitud() != null && direccion.getLongitud() != null &&
							!TextUtils.isEmpty(direccion.getLatitud()) && !TextUtils.isEmpty(direccion.getLongitud())){

						Location locationTo = new Location(GPS_PROVIDER);
						locationTo.setLatitude(Double.parseDouble(direccion.getLatitud()));
						locationTo.setLongitude(Double.parseDouble(direccion.getLongitud()));

						float distance = mCurrentLocation.distanceTo(locationTo);
						if(bestDistance == -1) {
							bestDistance = distance;
							mDireccionEntregaTempSeleccionada = direccion;
						}else{
							if(distance < bestDistance){
								bestDistance = distance;
								mDireccionEntregaTempSeleccionada = direccion;
							}
						}
					}
				}

				if(mDireccionEntregaTempSeleccionada != null){
					mDireccionEntregaSeleccionada = mDireccionEntregaTempSeleccionada;
					mDireccionEntregaTempSeleccionada = null;
					updateRowListDirecciones(1, mDireccionEntregaSeleccionada.getCalle());
					showToast("Autoseleccion de direccion mas cercana...");
				}
			}
		}catch (Exception e){
			showToast("autoSeleccionarDireccionMasCercana > " + e.getMessage());
		}
	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(final Location location) {
			//createOrUpdateMarkerByLocation(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};

	private boolean checkGPSIsEnabled() {
		try {
			int locationMode = 0;
			String locationProviders;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				try {
					locationMode = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

				} catch (Settings.SettingNotFoundException e) {
					showToast("checkGPSIsEnabled() SDKV>19 > " + e.getMessage());
					return false;
				}

				return locationMode != Settings.Secure.LOCATION_MODE_OFF;

			}else{
				locationProviders = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				return !TextUtils.isEmpty(locationProviders);
			}


		} catch (Exception e) {
			showToast("checkGPSIsEnabled() > " + e.getMessage());
			return false;
		}
	}

	private void showInfoAlert() {
		new android.support.v7.app.AlertDialog.Builder(getActivity())
				.setTitle("Señal GPS")
				.setMessage("No tienes señal GPS. Quieres habilitarla?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//El GPS no está activado
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				})
				.setNegativeButton("CANCEL", null)
				.show();
	}

	//endregion

;
	
	private class TareaRegistroOrd extends AsyncTask<String, Void, Object>{
		
		@Override
		protected Object doInBackground(String... arg0) {
			
			shouldThreadContinueoV = true;
			
			InvocaWS ws= new InvocaWS(contexto);

				res = ws.createOrder(ordBean, listaOrdDet);
	
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {

			if (res == null || res.equalsIgnoreCase("anytype{}")) {

				Insert insert = new Insert(contexto);
				insert.updateEstadoOrdenVenta(ordBean.getClaveMovil());
				insert.close();

				Toast.makeText(contexto,
						"Enviado al servidor",
						Toast.LENGTH_LONG).show();
				clearLists();

			} else {

				Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();
				clearLists();

			}
			
			shouldThreadContinueoV = false;

		}	
			
	}    

	
;
	private class TareaActualizarOrd extends AsyncTask<String, Void, Object>{
		
		@Override
		protected Object doInBackground(String... arg0) {
			
			shouldThreadContinueoV = true;
			
			InvocaWS ws= new InvocaWS(contexto);
			res = ws.createOrder(ordBean, listaOrdDet);
	
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {

			if (res == null || res.equalsIgnoreCase("anytype{}")) {

				Insert insert = new Insert(contexto);
				insert.updateEstadoOrdenVenta(ordBean.getClaveMovil());
				insert.close();

				Toast.makeText(contexto,
						"Enviado al servidor",
						Toast.LENGTH_LONG).show();
				clearLists();

			} else {

				Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();
				clearLists();

			}
			
			shouldThreadContinueoV = false;

		}	
			
	}  
	
	
}
