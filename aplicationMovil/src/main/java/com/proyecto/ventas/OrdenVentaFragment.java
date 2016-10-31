package com.proyecto.ventas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout.LayoutParams;
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
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.database.Update;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class OrdenVentaFragment extends Fragment{
	
	public static boolean shouldThreadContinueoV = false;

	private String res = "";
	private String action = "";
	private String claveVenta = "";
	
	//PARA EL REGISTRO LOCAL
	private OrdenVentaBean ordBean = null;
	private OrdenVentaDetalleBean ordDetBean = null;
	private ArrayList<OrdenVentaDetalleBean> listaOrdDet = null;
	//PARA EL REGISTRO LOCAL
	
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
	private DireccionBean direccionEntregaSel = null;
	private DireccionBean direccionFiscalSel = null;
	private ContactoBean contactoSel = null;
	
	//Nro de artículos PARA EL DETALLE
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
	
	//LISTAS PERSONALIZABLES (ITEM fDEL LISTVIEW) (Utilizando el formato de socio de negocio para acortar código)
	private ArrayList<FormatCustomListView> searchResults = null;
	private ArrayList<FormatCustomListView> searchResults1 = null;
	private ArrayList<FormatCustomListView> searchResults2 = null;
	private ArrayList<FormatCustomListView> searchResults3 = null;
	private ArrayList<FormatCustomListView> searchResults4 = null;
			
	//Objeto que tomarà al ser seleccionado (Ayuda al update del select item con popup
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
	
	
	//RECIBE LOS PARÀMETROS DESDE EL FRAGMENT CORRESPONDIENTE
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    
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

							  direccionFiscalSel = new DireccionBean();
							  for (DireccionBean bean : listaDireccionSN) {
								  if (bean.getTipoDireccion().equals("B")
										  && bean.getIDDireccion().equals(extras[3])) {

									  direccionFiscalSel = bean;
									  break;
								  }
							  }
							  Object o = lvDirecciones.getItemAtPosition(0);
							  fullObject = new FormatCustomListView();
							  fullObject = (FormatCustomListView) o;
							  if (direccionFiscalSel.getCalle() != null
									  && !direccionFiscalSel.getCalle().equalsIgnoreCase("anytype{}"))
								  fullObject.setData(direccionFiscalSel.getCalle());
							  else
								  fullObject.setData(direccionFiscalSel.getIDDireccion());
							  searchResults2.set(0, fullObject);
							  lvDirecciones.invalidateViews();

						  } else {
							  Object o = lvDirecciones.getItemAtPosition(0);
							  fullObject = new FormatCustomListView();
							  fullObject = (FormatCustomListView) o;
							  fullObject.setData("");
							  searchResults2.set(0, fullObject);
							  lvDirecciones.invalidateViews();
						  }
					  }
				        	
				        	//Capturar el objeto (que refleja la selección estado doc)
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
						FormatCustomListView fullObject1 = new FormatCustomListView();
						fullObject1 = (FormatCustomListView)o;
						fullObject1.setData(listaDetalleArticulos.size() + " artículos");
						searchResults1.set(0, fullObject1);
						
						lvContenido.invalidateViews();
						llenarListaTotales();
			        
		        }
		    }
		 }
		  
		  
	};

	
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
		
        //registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
		IntentFilter filter = new IntentFilter("event-send-lines-to-order");
        filter.addAction("event-send-bp-to-ordr");
        LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver, filter);
        
        
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
    	sr.setTitulo("Número de documento");
    	sr.setData(nroDoc);
    	searchResults.add(sr);
		
		sr = new FormatCustomListView();
    	sr.setTitulo("Código de SN");
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
    	sr.setTitulo("Fecha de vencimiento");
    	sr.setData(currentDate);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Referencia");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
    	lvPrincipal.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}
	
	private void llenarListaFinanzas(){
		
	    
	    searchResults4 = new ArrayList<FormatCustomListView>();

	    lvFinanzas = (ListView) v.findViewById(R.id.lvFinPedido);
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Condición pago");
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
    	sr.setTitulo("Dirección fiscal");
    	sr.setIcon(iconId);
    	searchResults2.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Dirección de entrega");
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
		sr.setTitulo("Artículos");
		sr.setData(listaDetalleArticulos.size() + " artículos");
		sr.setIcon(iconId);
		searchResults1.add(sr);
    	
        adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults1);
        lvContenido.setAdapter(adapter);
        DynamicHeight.setListViewHeightBasedOnChildren(lvContenido);
		
	}

	
	//CONSTRUIR ALERTS
	private void construirAlert(String bloque,int position){
		
		
		//Alerts del bloque principal
		if(bloque.equals("principal")){
			
			if(position == 1){
				
				if(action.equals("")){
					BuscarSNFragment fragment = new BuscarSNFragment();
					
	                FragmentManager manager = getFragmentManager();
	                manager.saveFragmentInstanceState(this);
	                FragmentTransaction transaction = manager.beginTransaction();
	                transaction.hide(this);
	                transaction.add(R.id.box, fragment);
	                transaction.addToBackStack(null);
	                transaction.commit();
	                
	                getActivity().setTitle("Buscar Socio de Negocio");
				}
				
			}else if(position == 3){
				
				//Contaacto
				//PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
				posicion = position;
				//Capturar el objeto (row - fila) 
				Object o = lvPrincipal.getItemAtPosition(position);
	    		fullObject = new FormatCustomListView();
	        	fullObject = (FormatCustomListView)o;
	        	//
				
	        	
	        	rg = new RadioGroup(contexto);
	        	
	        	if(listaContactoSN.size()>0){

	        		int id = 1;
	        		RadioButton rbt = null;
	        		for (ContactoBean bean : listaContactoSN) {
	        			
	        				rbt = new RadioButton(contexto);
		        			rbt.setId(id);
		        			
		        			rbt.setText(bean.toString());
		        			
		        			rbt.setGravity(Gravity.CENTER_VERTICAL);
		            		rg.addView(rbt);
		            		
		            		bean.setUtilId(id);
		            		id++;
		            		
						}
					
	        		
	        	}
	        	
	        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
	        	rg.setPadding(15, 0, 0, 0);
	        	rg.setLayoutParams(lp);
	        	rg.setGravity(Gravity.LEFT);
	        	
	        	
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
	    		alert.setTitle("Contacto");
	    		
	    		//AGREGAR EL VIEW AL POP UP
	    		alert.setView(rg);
	    		
	    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {

	    			for (ContactoBean bean : listaContactoSN) {
	    				
	    				if(bean.getUtilId() == rg.getCheckedRadioButtonId()){
	    					
	    					contactoSel = bean;
	    					if(bean.getPrimerNombre() != null && !bean.getPrimerNombre().equalsIgnoreCase("anytype{}") && 
		        					bean.getApeCon() != null && !bean.getApeCon().equalsIgnoreCase("anytype{}") )
	    						fullObject.setData(bean.getPrimerNombre()+" "+bean.getApeCon());
		        			else
		        				fullObject.setData(bean.getNomCon());
	    					
	    					
	    					searchResults.set(posicion, fullObject);
	    					lvPrincipal.invalidateViews();
	    					
	    				}
	    				
						
					}
	    			
	    		  }
	    		});

	    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		  public void onClick(DialogInterface dialog, int whichButton) {
	    		    // Canceled.
	    		  }
	    		});

	    		alert.show();
				
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
				
			//	alert.construirAlertDatePicker(contexto, position, "Fecha de vencimiento", searchResults, lvPrincipal);
			
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
	private void construirAlertDirecciones(int position){
		
		if(position == 0){
			
			//PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
			posicion = position;
			//Capturar el objeto (row - fila) 
			Object o = lvDirecciones.getItemAtPosition(position);
    		fullObject = new FormatCustomListView();
        	fullObject = (FormatCustomListView)o;
        	//
			
        	
        	rg = new RadioGroup(contexto);
        	
        	if(listaDireccionSN.size()>0){

        		int id = 1;
        		RadioButton rbt = null;
        		for (DireccionBean bean : listaDireccionSN) {
        			
        			if(bean.getTipoDireccion().equals("B")){
        				rbt = new RadioButton(contexto);
	        			rbt.setId(id);
	        			if(bean.getCalle() != null && !bean.getCalle().equalsIgnoreCase("anytype{}"))
	        				rbt.setText(bean.getCalle());
	        			else if(bean.getReferencia()!= null && !bean.getReferencia().equalsIgnoreCase("anytype{}"))
	        				rbt.setText(bean.getReferencia());
	        			else
	        				rbt.setText(bean.getIDDireccion());
	            		rbt.setGravity(Gravity.CENTER_VERTICAL);
	            		rg.addView(rbt);
	            		bean.setUtilId(id);
	            		id++;
        			}
        			
				}
				
        		
        	}
        	
        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
        	rg.setPadding(15, 0, 0, 0);
        	rg.setLayoutParams(lp);
        	rg.setGravity(Gravity.LEFT);
        	
        	
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
    		alert.setTitle("Dirección fiscal");
    		
    		//AGREGAR EL VIEW AL POP UP
    		alert.setView(rg);
    		
    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {

    			for (DireccionBean bean : listaDireccionSN) {
    				
    				if(bean.getUtilId() == rg.getCheckedRadioButtonId()){
    					
    					direccionFiscalSel = new DireccionBean();
    					direccionFiscalSel = bean;
    					
    					if(bean.getCalle() != null && !bean.getCalle().equalsIgnoreCase("anytype{}"))
    						fullObject.setData(bean.getCalle());
	        			else if(bean.getReferencia() != null && !bean.getReferencia().equalsIgnoreCase("anytype{}"))
	        				fullObject.setData(bean.getReferencia());
	        			else
	        				fullObject.setData(bean.getIDDireccion());
    					
    					searchResults2.set(posicion, fullObject);
    					lvDirecciones.invalidateViews();
    					
    				}
    				
				}
    			
    		  }
    		});

    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int whichButton) {
    		    // Canceled.
    		  }
    		});

    		alert.show();
			
			
		}else if(position == 1){
			
			boolean res = false;
			
        	final RadioGroup rg = new RadioGroup(contexto);
        	
        	if(listaDireccionSN.size()>0){

        		int id = 1;
        		RadioButton rbt = null;
        		for (DireccionBean bean : listaDireccionSN) {
        			
        			if(bean.getTipoDireccion().equals("S")){
        				if(!res)
        					res = true;
        				rbt = new RadioButton(contexto);
	        			rbt.setId(id);
	        			if(bean.getCalle() != null && !bean.getCalle().equalsIgnoreCase("anytype{}"))
	        				rbt.setText(bean.getCalle());
	        			else if(bean.getReferencia()!= null && !bean.getReferencia().equalsIgnoreCase("anytype{}"))
	        				rbt.setText(bean.getReferencia());
	        			else
	        				rbt.setText(bean.getIDDireccion());
	            		rbt.setGravity(Gravity.CENTER_VERTICAL);
	            		rg.addView(rbt);
	            		bean.setUtilId(id);
	            		id++;
        			}
        			
				}
				
        		
        	}
        	
        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
        	rg.setPadding(15, 0, 0, 0);
        	rg.setLayoutParams(lp);
        	rg.setGravity(Gravity.LEFT);
        	
        	
        	if(res){
        		//PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
				posicion = position;
				//Capturar el objeto (row - fila) 
				Object o = lvDirecciones.getItemAtPosition(position);
	    		fullObject = new FormatCustomListView();
	        	fullObject = (FormatCustomListView)o;
	        	//
	        	
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
	    		alert.setTitle("Dirección de entrega");
	    		
	    		//AGREGAR EL VIEW AL POP UP
	    		alert.setView(rg);
	    		
	    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {

	    			for (DireccionBean bean : listaDireccionSN) {
	    				
	    				if(bean.getTipoDireccion().equals("S")){
	    					if(bean.getUtilId() == rg.getCheckedRadioButtonId()){
		    					
		    					direccionEntregaSel = new DireccionBean();
		    					direccionEntregaSel = bean;
		    					
		    					if(bean.getCalle() != null && !bean.getCalle().equalsIgnoreCase("anytype{}"))
		    						fullObject.setData(bean.getCalle());
			        			else if(bean.getReferencia() != null && !bean.getReferencia().equalsIgnoreCase("anytype{}"))
			        				fullObject.setData(bean.getReferencia());
			        			else
			        				fullObject.setData(bean.getIDDireccion());
		    					
		    					searchResults2.set(posicion, fullObject);
		    					lvDirecciones.invalidateViews();
		    					
		    				}
	    				}
	    				
	    				
					}
	    			
	    		  }
	    		});

	    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		  public void onClick(DialogInterface dialog, int whichButton) {
	    		    // Canceled.
	    		  }
	    		});

	    		alert.show();
				
        	}else
        		alert.construirAlert(contexto, position, "Dirección de entrega", searchResults2, lvDirecciones, "text",250);
        	
        	
			
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
			
			alert.construirAlert(contexto, position, "Código de entrega", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 1){
			
			alert.construirAlert(contexto, position, "Dirección de despacho", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 2){
			
			alert.construirAlert(contexto, position, "Código de factura", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 3){
			
			alert.construirAlert(contexto, position, "Dirección de factura", searchResults2, lvLogistica, "text",200);
			
		}else if(position == 4){
			
			alert.construirAlert(contexto, position, "Situación entre.", searchResults2, lvLogistica, "text",200);
			
		}
		
		
	}
	
	
	private void construirAlertFinanzas(int position){
		
		if(position == 0){
			
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
	    		alert.setTitle("Condición de Pago");

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
	    				Toast.makeText(contexto, "No puede seleccionar esa condición de pago", Toast.LENGTH_SHORT).show();
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
    	
    	FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
    	
		switch (item.getItemId()) {
        case R.id.action_registrar:
        	
        if(searchResults.get(1).getData() == null || searchResults.get(1).getData().equals("")){
        	
        	Toast.makeText(contexto, "Seleccione el socio de negocio", Toast.LENGTH_LONG);
        	return false;
        	
        }else if(listaDetalleArticulos.size() == 0){
        	
        	Toast.makeText(contexto, "Añada artículos a la orden", Toast.LENGTH_LONG);
        	return false;
        	
        }
       
  
		ordBean = new OrdenVentaBean();
		if(tipoDocumento.equals(""))
			ordBean.setTipoDoc("P");
		else
			ordBean.setTipoDoc(tipoDocumento);
		ordBean.setNumero(searchResults.get(0).getData());
		ordBean.setCodSN(searchResults.get(1).getData());
		ordBean.setNomSN(searchResults.get(2).getData());
		if(contactoSel != null)
			ordBean.setContacto(contactoSel.getIdCon());
		if(monSel != null)
			ordBean.setMoneda(monSel.getCodigo());	
		else 
			ordBean.setMoneda(listaMonedas.get(0).getCodigo());
		ordBean.setEmpVentas(codigoEmpleado);
		ordBean.setComentario(searchResults.get(6).getData());
		ordBean.setFecContable(StringDateCast.castDatetoDateWithoutSlash(searchResults.get(7).getData()));
		ordBean.setFecVen(StringDateCast.castDatetoDateWithoutSlash(searchResults.get(8).getData()));
		ordBean.setReferencia(searchResults.get(9).getData());
		
		//Bloque direcciones
		if(direccionFiscalSel != null)
		ordBean.setDirFiscal(direccionFiscalSel.getIDDireccion());
		
		if(direccionEntregaSel != null)
		ordBean.setDirEntrega(direccionEntregaSel.getIDDireccion());
		
		
		//Bloque finanzas
		if(condPagoSel != null)
			ordBean.setCondPago(condPagoSel.getNumeroCondicion());
		if(indicadorSel != null)
			ordBean.setIndicador(indicadorSel.getCodigo());
		if(listaPrecioSel != null)
			ordBean.setListaPrecio(listaPrecioSel.getCodigo());
		
		
		//Bloque calculos
			if(searchResults3 != null)
				ordBean.setSubTotal(searchResults3.get(0).getData());
			else {
				Toast.makeText(contexto, "Confirme los artículos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
				return true;
			}
			if(searchResults3.get(1) != null)
				ordBean.setPorcDesc(Double.parseDouble(searchResults3.get(1).getData()));
			else {
				Toast.makeText(contexto, "Confirme los artículos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
				return true;
			}
			if(searchResults3.get(3) !=null)
				ordBean.setTotDesc(Double.parseDouble(searchResults3.get(2).getData()));
			else {
				Toast.makeText(contexto, "Confirme los artículos en el detalle para generar los totales del pedido.", Toast.LENGTH_SHORT).show();
				return true;
			}
			ordBean.setImpuesto(searchResults3.get(3).getData());
		ordBean.setTotal(searchResults3.get(4).getData());
		ordBean.setCreadoMovil("Y");
		
		Date date = new Date();
		String fullDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.ENGLISH).format(date);
		if(action.equals(""))
			claveMovil = idDispositivo +"-"+fullDate+"-" + nroOrd;
		
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
		
		if(action.equals("")){
			
			ordBean.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
			
			Insert insert = new Insert(contexto);
			boolean res = insert.insertOrdenVenta(ordBean);
			
			if(res){
				
				//Actualizar el correlativo para la clave movil
				insert.updateCorrelativo("ORD");
				insert.close();
				
				Toast.makeText(contexto, "Orden de venta registrada", Toast.LENGTH_LONG).show();
				
				Activity activity = getActivity();
				
				if(activity != null){
					
					//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE ORDENES DE VENTA
		       	 	Intent localBroadcastIntent = new Intent("event-send-register-ov-ok");
		            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(activity);
		            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
		            
		          //COMPROBAR EL ESTADO DE LA RED MOVIL DE DATOS
					 boolean wifi = Connectivity.isConnectedWifi(contexto);
				     boolean movil = Connectivity.isConnectedMobile(contexto);
				     boolean isConnectionFast = Connectivity.isConnectedFast(contexto);
				     
				     if(wifi || movil && isConnectionFast){
				        	
				    	 new TareaRegistroOrd().execute();	
				    	 
				     }else{
				    	 clearLists();
				     }
				     
				     transaction.remove(this);
					 transaction.commit();
				     activity.finish();
		            
				}
				
				
			}else{
				insert.close();
				Toast.makeText(contexto, "No se registró la orden de venta, compruebe los datos", 
						Toast.LENGTH_LONG).show();
			}
		}else{
			
			if(!estadoRegistroMovil.equals(getResources().getString(R.string.LOCAL))){
				ordBean.setClave(claveVenta);
				ordBean.setTransaccionMovil(getResources().getString(R.string.ACTUALIZAR_BORRADOR));
			}else{
				ordBean.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
			}
			
			
			//Actualización
			Update update = new Update(contexto);
			boolean res = update.updateOrdenVenta(ordBean);
			
			if(res){
				update.close();
				Toast.makeText(contexto, "Orden de venta actualizada", Toast.LENGTH_LONG).show();
				
				
				Activity actividad = getActivity();
				
				if(actividad != null){
					
					//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE ORDENES DE VENTA
		       	 	Intent localBroadcastIntent = new Intent("event-send-register-ov-ok");
		            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
		            
		          //COMPROBAR EL ESTADO DE LA RED MOVIL DE DATOS
					 boolean wifi = Connectivity.isConnectedWifi(contexto);
				     boolean movil = Connectivity.isConnectedMobile(contexto);
				     boolean isConnectionFast = Connectivity.isConnectedFast(contexto);
				     
				     if(wifi || movil && isConnectionFast){
				        	
				    	 new TareaActualizarOrd().execute();	
				    	 
				     }else{
				    	 clearLists();
				     }
				     
				     getActivity().finish();
					
				}else{
					Toast.makeText(contexto, "No attach activity.", Toast.LENGTH_SHORT).show();
				}
				
			}else{
				update.close();
				Toast.makeText(contexto, "No se actualizó la orden de venta, compruebe los datos", 
						Toast.LENGTH_LONG).show();
			}
			
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
			if(!line.getCodImp().equals("IGV_EXO"))
				articulo.setImpuesto(line.getImp());
			else
				articulo.setImpuesto(0);
			articulo.setUtilIcon(iconId);
			articulo.setUtilLinea(line.getLinea());
			listaDetalleArticulos.add(articulo);
		}
		llenarListaContenido();
		
		/***/
		
		//Dirección fiscal
		direccionFiscalSel = new DireccionBean();
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
		
		//Dirección entrega
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
