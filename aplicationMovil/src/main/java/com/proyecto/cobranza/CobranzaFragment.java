package com.proyecto.cobranza;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.TipoPagoBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.database.Update;
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

public class CobranzaFragment extends Fragment{

	public static boolean shouldThreadContinue = false;
	
	//UTILS
	private View v = null;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private ConstruirAlert alert = new ConstruirAlert();
	private ArrayList<MonedaBean> listaMonedas;
	private String action = "";
	private String clavePago = "";
	private String estadoRegistroMovil = "";
	private String claveMovil = "";
	
	//Preferencias
	private SharedPreferences pref;
	private String codigoEmpleado;
	private String nombreEmpleado;
	private String idDispositivo;
	
	//LIST VIEW PARA CADA BLOQUE 
	private ListView lvTitulo = null;
	private ListView lvContenido = null;
	private String res = "";
	
	private ArrayList<FormatCustomListView> searchResults = null;
	private ArrayList<FormatCustomListView> searchResults1 = null;
	
	//CONTACTOS y direcciones DEL SOCIO DE NEGOCIO ACTUAL
	public static ArrayList<FacturaBean> listaFacturasSN = new ArrayList<FacturaBean>();
	public static ArrayList<FacturaBean> listaFacturasPagar = new ArrayList<FacturaBean>();
	public static TipoPagoBean tipoPago = new TipoPagoBean();
	
	//Objeto que tomar� al ser seleccionado (Ayuda al update del select item con popup
	private FormatCustomListView fullObject = null;
	private int posicion = 0;
	private MonedaBean monSel = null;	
	public static String currentDate;
	
	//Pago
	private PagoBean pago = null;
	
	//Util Database
	private Select select = null;
	private int nroPago = 0;
	public static double totalPago = 0;
	
	//RECIBE LOS PAR�METROS DESDE EL FRAGMENT CORRESPONDIENTE
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
			  @Override
			  public void onReceive(Context context, Intent intent) {
			    
				  if(intent.getAction().equals("custom-event-get-socio-negocio-cobranza")){
					  Bundle bundle = intent.getExtras();
				        if (bundle != null) {
				        	
				        	String cod = bundle.getString("cod");
					        String name = bundle.getString("name");
					  //      String extras = bundle.getString("extras");
					        	
					        //Settear el c�digo del socio de negocio seleccionado
								Object o = lvTitulo.getItemAtPosition(3);
				        		fullObject = new FormatCustomListView();
				            	fullObject = (FormatCustomListView)o;
				            	fullObject.setData(cod);
								searchResults.set(3, fullObject);
							
							//Settear el nombre
								Object o2 = lvTitulo.getItemAtPosition(4);
				        		fullObject = new FormatCustomListView();
				            	fullObject = (FormatCustomListView)o2;
				            	fullObject.setData(name);
								searchResults.set(4, fullObject);
								
								if(listaFacturasSN.size()>0){
									
									 llenarListaContenido();
									
					        		fullObject = new FormatCustomListView();
					            	fullObject = (FormatCustomListView) lvContenido.getItemAtPosition(0);
					            	fullObject.setData("0");
					            	searchResults1.set(0, fullObject);
					            	
					            	totalPago = 0;
					        		fullObject = new FormatCustomListView();
					            	fullObject = (FormatCustomListView) lvContenido.getItemAtPosition(1);
					            	fullObject.setData(String.valueOf(totalPago));
					            	searchResults1.set(1, fullObject);
					            	
					            	lvContenido.invalidateViews();
					            	
								}
								
								lvTitulo.invalidateViews();
				        	
				        }
				  }else if (intent.getAction().equals("event-send-tipo-pago")){
					  	fullObject = new FormatCustomListView();
		            	fullObject = (FormatCustomListView)lvTitulo.getItemAtPosition(7);;
		            	fullObject.setData(tipoPago.getTipoPago());
		            	searchResults.set(7, fullObject);
		            	lvTitulo.invalidateViews();
				  }else if(intent.getAction().equals("event-send-total-pago-facturas")){

					  mostrarTotal();
					 
				  }
			   
			 }
	};
	///////////////////
	
	private void mostrarTotal(){
		
		totalPago = 0;
		for (FacturaBean f : listaFacturasPagar) {
			totalPago += Double.parseDouble(f.getUtilPagoTotal());
		}
		
		tipoPago.setChequeImporte(String.valueOf(totalPago));
		tipoPago.setEfectivoImporte(String.valueOf(totalPago));
		tipoPago.setTransferenciaImporte(String.valueOf(totalPago));
		  
		fullObject = new FormatCustomListView();
      	fullObject = (FormatCustomListView) lvContenido.getItemAtPosition(0);
      	fullObject.setData(String.valueOf(listaFacturasPagar.size()));
      	searchResults1.set(0, fullObject);
		  
		fullObject = new FormatCustomListView();
      	fullObject = (FormatCustomListView) lvContenido.getItemAtPosition(1);
      	fullObject.setData(String.valueOf(totalPago));
      	searchResults1.set(1, fullObject);
      	
      	lvContenido.invalidateViews();
		
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.cobranza, viewGroup, false);
        
	        action = "";
	        listaFacturasSN.clear();
			listaFacturasPagar.clear();
			tipoPago.setTipoPago("");
			totalPago = 0;
			
		    v = view;
	        contexto = v.getContext();
	        
	        boolean val = cargarListas();
			
			if(!val){
				getActivity().finish();
				return view;
			}
	        
	        lvTitulo = (ListView) v.findViewById(R.id.lvTituloCO);
	        lvContenido = (ListView) v.findViewById(R.id.lvContenidoCO);
	        
	        pref = PreferenceManager
					.getDefaultSharedPreferences(contexto);
			codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");
			nombreEmpleado = pref.getString(Variables.NOMBRE_EMPLEADO, "");
			idDispositivo = Secure.getString(getActivity().getContentResolver(),
					Secure.ANDROID_ID);

	        //LLENAR EL LISTADO DE DATOS QUE COMPONEN EL PAGO
	        llenarListaTitulo();
	        
	        Intent myIntent = getActivity().getIntent();
	        if (myIntent.getStringExtra("action") != null) {
				action = myIntent.getStringExtra("action");
				clavePago = myIntent.getStringExtra("clave");
				construirDataEditar();
			}
	        
	        
	      //1. PROGRAMAR EL CLICK HACIA EL BLOQUE TITULO PARA CADA DATO
	        lvTitulo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                                    int position, long id) {
	    				construirAlert("titulo",position);
	            }
	
			});
			////
	        
	        
	        //2. PROGRAMAR EL CLICK PARA CADA LINEA DEL BLOQUE DE CONTENIDO
	        lvContenido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					construirAlert("contenido", position);
				}
	        	
			});

       
        setHasOptionsMenu(true);
        return view;
		
	}

	@Override
	public void onResume() {
		super.onResume();

		try{
			//REGISTRAR EL METODO PARA RECIBIR PARAMETROS DESDE UN FRAGMENT CON POPBACKSTACK
			IntentFilter filter = new IntentFilter("custom-event-get-socio-negocio-cobranza");
			filter.addAction("event-send-tipo-pago");
			filter.addAction("event-send-total-pago-facturas");
			LocalBroadcastManager.getInstance(contexto).registerReceiver(
					myLocalBroadcastReceiver, filter);
		}catch (Exception e){
			showMessage(e.getMessage());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			LocalBroadcastManager.getInstance(contexto).unregisterReceiver(myLocalBroadcastReceiver);
		}catch (Exception e){
			showMessage(e.getMessage());
		}
	}

	private boolean cargarListas(){
		
		boolean pass = true;
		
		listaMonedas = new ArrayList<MonedaBean>();
		
		Select select = new Select(contexto);
		listaMonedas = select.listaMonedas();
		
		if(listaMonedas.size()>0){
			monSel = listaMonedas.get(0);
		}else{
			Toast.makeText(contexto, "Uno o mas datos maestros no han sido cargados", Toast.LENGTH_LONG).show();
			select.close();
			pass = false;
		}
		
		return pass;
		
	}
	
	
	//CONSTRUIR LOS ALERT PARA CADA BLOQUE
	private void construirAlert(String bloque, int position) {
		
		if(bloque.equalsIgnoreCase("titulo")){
			
			if(position == 2){
				
			//	alert.construirAlertDatePicker(contexto, position, "Fecha contable", searchResults, lvTitulo);
				
			}else if(position == 3){
				
				if(action.equals("")){
					Fragment fragment = new BuscarSNFragmentCobranza();
					
	                FragmentManager manager = getFragmentManager();
	                FragmentTransaction transaction = manager.beginTransaction();
	                transaction.hide(this);
	                transaction.add(R.id.box_cobranza, fragment);
	                transaction.addToBackStack(null);
	                transaction.commit();
	                
	                getActivity().setTitle("Buscar Socio de Negocio");
				}
				
			}else if(position == 5){
				
				posicion = position;

				Object o = lvTitulo.getItemAtPosition(position);
        		fullObject = new FormatCustomListView();
            	fullObject = (FormatCustomListView)o;
            	//
				
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
        		alert.setTitle("Moneda");

        		// Set an EditText view to get user input 
        		//Spinner
        		final Spinner spnMoneda = new Spinner(contexto);
        		
        		ArrayAdapter<MonedaBean> adapter = new ArrayAdapter<MonedaBean>(contexto, 
						android.R.layout.simple_list_item_1,
						listaMonedas);
        		spnMoneda.setAdapter(adapter);
        		spnMoneda.setOnItemSelectedListener(new OnItemSelectedListener() {
    				
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						//MONEDA SELECCIONADA
						monSel = new MonedaBean();
						monSel = (MonedaBean) parent.getItemAtPosition(pos);
						
					}
					@Override
					public void onNothingSelected(
							AdapterView<?> arg0) {
						
					}
				});
        		
        		alert.setView(spnMoneda);
        		
        		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int whichButton) {

        		  // Do something with value!
        			//SeTEAR la data en la fila para vista
        			fullObject.setData(monSel.getDescripcion());
					searchResults.set(posicion, fullObject);
					lvTitulo.invalidateViews();
        			
        			
        		  }
        		});

        		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        		  public void onClick(DialogInterface dialog, int whichButton) {
        		    // Canceled.
        		  }
        		});

        		alert.show();
				
			}else if(position == 6){
				//GLOSA
				alert.construirAlert(contexto, position, "Glosa", searchResults, lvTitulo, "text",50);

			}else if(position == 7){
				
				if(listaFacturasSN.size() >0){
					// TIPO DE PAGO
					Fragment fragment = new TipoPagoFragment();
					
	                FragmentManager manager = getFragmentManager();
	                FragmentTransaction transaction = manager.beginTransaction();
	                transaction.hide(this);
	                transaction.add(R.id.box_cobranza, fragment);
	                transaction.addToBackStack(null);
	                transaction.commit();
	                
	                getActivity().setTitle("Tipo de pago");
				}
				
				
			}else if(position == 8){
				
				alert.construirAlert(contexto, position, "Comentarios", searchResults, lvTitulo, "text",160);
				
			}
			
		}else if(bloque.equalsIgnoreCase("contenido")){
			Fragment fragment = new CobranzaListaFacturasFragment();

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.hide(this);
			transaction.add(R.id.box_cobranza, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			getActivity().setTitle("Lista de facturas a pagar");
		}
		
		
	}


	private void llenarListaContenido() {

		searchResults1 = new ArrayList<FormatCustomListView>();

		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Nro Facturas");
		sr.setIcon(iconId);
		searchResults1.add(sr);
		
		sr = new FormatCustomListView();
		sr.setTitulo("Pago Total");
		searchResults1.add(sr);
    	
        adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults1);
        lvContenido.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvContenido);
		
	}

	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) 
	private void llenarListaTitulo() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	select = new Select(contexto);
    	nroPago = select.numeroCorrelativoRegistro("PAG");
    	select.close();
    	
    	currentDate = dateFormat.format(date);
    	
    	String fullDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.ENGLISH).format(date);
		
		searchResults = new ArrayList<FormatCustomListView>();

		FormatCustomListView sr = new FormatCustomListView();
    	sr.setTitulo("Numero");
    	sr.setData(idDispositivo+"-"+fullDate+"-"+nroPago);
    	searchResults.add(sr);
		
		sr = new FormatCustomListView();
    	sr.setTitulo("Empleado de ventas");
    	sr.setData(nombreEmpleado);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Fecha contable");
    	sr.setData(currentDate);
    	searchResults.add(sr);
		
		sr = new FormatCustomListView();
		sr.setTitulo("Codigo socio de negocio");
		sr.setIcon(iconId);
		searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Nombre socio de negocio");
    	searchResults.add(sr);
		
    	sr = new FormatCustomListView();
    	sr.setTitulo("Moneda");
    	sr.setData(monSel.getDescripcion());
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Glosa");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Tipo de pago");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Comentarios");
    	sr.setIcon(iconId);
    	searchResults.add(sr);
    	
    	
        adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
        lvTitulo.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvTitulo);
        
	}


	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	
	private void clearVariables(){
		listaFacturasSN.clear();
		listaFacturasPagar.clear();
		tipoPago.setTipoPago("");
		totalPago = 0;
		tipoPago.clear();
		claveMovil = "";
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearVariables();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
		
		switch (item.getItemId()) {
		case 16908332:
			
			 transaction.remove(this);
	         transaction.commit();
			getActivity().finish();
			clearVariables();
			
			return true;
		case R.id.action_registrar:

			if (listaFacturasPagar.size() == 0) {

				Toast.makeText(contexto,
						"Seleccione las facturas del socio de negocio",
						Toast.LENGTH_LONG).show();

			} else if(tipoPago.getTipoPago() == null || tipoPago.getTipoPago().equals("")){
				
				Toast.makeText(contexto,
						"Seleccione el tipo de pago",
						Toast.LENGTH_LONG).show();
				
			} else {

				Insert insert = new Insert(contexto);
				Select select = new Select(contexto);
				select.close();
				
				Date date = new Date();
				String fullDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.ENGLISH).format(date);
				// Bloque titulo
				if(action.equals(""))
					claveMovil = idDispositivo +"-"+fullDate+ "-" + nroPago;
				pago = new PagoBean();
				pago.setTipo("P");
				pago.setClave(claveMovil);
				pago.setNumero(searchResults.get(0).getData());
				pago.setSocioNegocio(searchResults.get(3).getData());
				pago.setEmpleadoVenta(codigoEmpleado);
				pago.setComentario(searchResults.get(8).getData());
				pago.setGlosa(searchResults.get(6).getData());
				pago.setFechaContable(StringDateCast.castDatetoDateWithoutSlash(searchResults.get(2).getData()));
				pago.setMoneda(monSel.getCodigo());
				
				
				if(tipoPago.getTipoPago().equalsIgnoreCase("Transferencia/Deposito")){
					pago.setTipoPago("T");
					pago.setTransferenciaCuenta(tipoPago.getTransferenciaCuenta());

					if(tipoPago.getTransferenciaReferencia() == null ||
							tipoPago.getTransferenciaReferencia().equals("")){
						Toast.makeText(contexto,"Debe ingresar el nro referencia en la ventana de pago.",Toast.LENGTH_SHORT).show();
						return true;
					}

					pago.setTransferenciaReferencia(tipoPago.getTransferenciaReferencia());

					if(Double.parseDouble(tipoPago.getTransferenciaImporte().trim()) <= 0){
						Toast.makeText(contexto,"El importe no puede ser cero.",Toast.LENGTH_SHORT).show();
						return true;
					}
					pago.setTransferenciaImporte(tipoPago.getTransferenciaImporte());
				}else if(tipoPago.getTipoPago().equalsIgnoreCase("Efectivo")){
					pago.setTipoPago("F");
					pago.setEfectivoCuenta(tipoPago.getEfectivoCuenta());

					if(Double.parseDouble(tipoPago.getEfectivoImporte().trim()) <= 0){
						Toast.makeText(contexto,"El importe no puede ser cero.",Toast.LENGTH_SHORT).show();
						return true;
					}
					pago.setEfectivoImporte(tipoPago.getEfectivoImporte());
				}else if(tipoPago.getTipoPago().equalsIgnoreCase("Cheque")){
					pago.setTipoPago("C");
					pago.setChequeCuenta(tipoPago.getChequeCuenta());
					pago.setChequeBanco(tipoPago.getChequeBanco());
					pago.setChequeVencimiento(StringDateCast.castDatetoDateWithoutSlash(tipoPago.getChequeVencimiento()));

					if(Double.parseDouble(tipoPago.getChequeImporte().trim()) <= 0){
						Toast.makeText(contexto,"El importe no puede ser cero.",Toast.LENGTH_SHORT).show();
						return true;
					}
					pago.setChequeImporte(tipoPago.getChequeImporte());
					pago.setChequeNumero(tipoPago.getChequeNumero());
				}
				
				pago.setCreadoMovil("Y");
				
				pago.setClaveMovil(claveMovil);
				pago.setEstadoRegistroMovil(getResources().getString(R.string.LOCAL));
				pago.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
				
				ArrayList<PagoDetalleBean> listaDetalle = new ArrayList<PagoDetalleBean>();
				PagoDetalleBean detalle;
				
				for (FacturaBean factura : listaFacturasPagar) {
					detalle  = new PagoDetalleBean();
					detalle.setFacturaCliente(factura.getClave());
					detalle.setImporte(factura.getUtilPagoTotal());
					listaDetalle.add(detalle);
					
					//Actualizar los saldos de las facturas pendientes
//					for (FacturaBean factPend : listaFacturasSN) {
//						if(factPend.getClave().equals(factura.getClave())){
//							factPend.setSaldo(String.valueOf(Double.parseDouble(factPend.getSaldo()) - 
//									Double.parseDouble(factura.getUtilPagoTotal())));
//							insert.updateSaldoFactura(factPend.getClave(), factPend.getSaldo());
//						}
//					}
				}
				pago.setLineas(listaDetalle);
				
				if(action.equals("")){
					
					//Mandar el objeto a registro
					boolean res = insert.insertPagoCliente(pago);

					if(res){
						
						//Actualizar el correlativo para la clave movil
						insert.updateCorrelativo("PAG");
						insert.close();
						
						//Mostrar el mensaje de �xito
						Toast.makeText(contexto, "Pago registrado",
								Toast.LENGTH_LONG).show();
						
						totalPago = 0;
						
						Activity actividad = getActivity();
						if(actividad != null){
							
							//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA DE PAGOS
				        	 Intent localBroadcastIntent = new Intent("event-send-pago-bp-ok");
				             LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
				             myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
							
				             
				          // Registro en el webservice
								// COMPROBAR EL ESTADO DE LA RED MOVIL DE DATOS
								boolean wifi = Connectivity.isConnectedWifi(contexto);
								boolean movil = Connectivity.isConnectedMobile(contexto);
								boolean isConnectionFast = Connectivity.isConnectedFast(contexto);

								if (wifi || movil && isConnectionFast) {

									sendIncomingPaymentToServer();
									//new TareaRegistroPago().execute();

								} else {
									listaFacturasSN.clear();
									tipoPago.setTipoPago("");
									totalPago = 0;
									tipoPago.clear();
								}	
				             
							
							//eliminar instancia para liberar memoria y cerrar fragment
							transaction.remove(this);
							transaction.commit();
							getActivity().finish();
							
						}else{
							
							Toast.makeText(contexto, "No activity Attach", Toast.LENGTH_SHORT).show();
							
						}
						
						
					}else{
						Toast.makeText(contexto, "No se registró el pago, compruebe los datos",
								Toast.LENGTH_LONG).show();
					}
				}else{
					
					if(!estadoRegistroMovil.equals(getResources().getString(R.string.LOCAL))){
						pago.setClave(clavePago);
						pago.setTransaccionMovil(getResources().getString(R.string.ACTUALIZAR_BORRADOR));
					}else{
						pago.setTransaccionMovil(getResources().getString(R.string.CREAR_BORRADOR));
					}
					
					
					//Actualizaci�n
					Update update = new Update(contexto);
					boolean res = update.updatePagoCliente(pago);
					
					if(res){
						update.close();
						Toast.makeText(contexto, "Pago actualizado", Toast.LENGTH_LONG).show();
						
						
						Activity actividad = getActivity();
						
						if(actividad != null){
							
							//ENVIAR UN MENSAJE DE AVISO DE REGISTRO NUEVO A LA LISTA
				       	 	Intent localBroadcastIntent = new Intent("event-send-pago-bp-ok");
				            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
				            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
				            
				            totalPago = 0;
				            
							 //COMPROBAR EL ESTADO DE LA RED MOVIL DE DATOS
							 boolean wifi = Connectivity.isConnectedWifi(contexto);
						     boolean movil = Connectivity.isConnectedMobile(contexto);
						     boolean isConnectionFast = Connectivity.isConnectedFast(contexto);
						     
						     if(wifi || movil && isConnectionFast){

						     	showMessage("Enviando al servidor...");
								 sendIncomingPaymentToServer();

						     	//new TareaRegistroPago().execute();
						    	 
						     }else{
						    	 listaFacturasSN.clear();
								 tipoPago.setTipoPago("");
								 totalPago = 0;
								 tipoPago.clear();
								 clearVariables();
						     }
						     
					     
					   //eliminar instancia para liberar memoria y cerrar fragment
							transaction.remove(this);
							transaction.commit();
							getActivity().finish();
							
						}else{
							Toast.makeText(contexto, "No attach to activity", Toast.LENGTH_SHORT).show();
						}
						
			            
					}else{
						update.close();
						Toast.makeText(contexto, "No se actualizó el pago, compruebe los datos",
								Toast.LENGTH_LONG).show();
					}
					
					
				}
				
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	
	/**********************************************************
     ************************ EDICION *************************
     ***********************************************************/
    // SI SE QUIERE EDITAR CARGAR LOS DATOS INICIALES
    private void construirDataEditar() {

    	try{
			//Obtener la venta
			Select select = new Select(contexto);
			PagoBean bean = new PagoBean();
			bean = select.obtenerPagoRecibido(clavePago);


			//Exponer los datos
			searchResults.get(0).setData(bean.getNumero());
			searchResults.get(2).setData(StringDateCast.castStringtoDate(bean.getFechaContable()));
			searchResults.get(3).setData(bean.getSocioNegocio());

			listaFacturasSN = select.listaFacturasPorSocio(bean.getSocioNegocio(), bean.getClave());

			searchResults.get(4).setData(select.obtenerNombreSocioNegocio(bean.getSocioNegocio()));

			for (MonedaBean moneda : listaMonedas) {
				if(moneda.getCodigo().equals(bean.getMoneda())){
					monSel = moneda;
					searchResults.get(5).setData(monSel.getDescripcion());
				}
			}

			searchResults.get(6).setData(bean.getGlosa());


			if(bean.getTipoPago().equalsIgnoreCase("T")){
				tipoPago.setTipoPago("Transferencia/Deposito");
				tipoPago.setTransferenciaCuenta(bean.getTransferenciaCuenta());
				tipoPago.setTransferenciaReferencia(bean.getTransferenciaReferencia());
				tipoPago.setTransferenciaImporte(bean.getTransferenciaImporte());
			}else if(bean.getTipoPago().equalsIgnoreCase("F")){
				tipoPago.setTipoPago("Efectivo");
				tipoPago.setEfectivoCuenta(bean.getEfectivoCuenta());
				tipoPago.setEfectivoImporte(bean.getEfectivoImporte());
			}else if(bean.getTipoPago().equalsIgnoreCase("C")){
				tipoPago.setTipoPago("Cheque");
				tipoPago.setChequeCuenta(bean.getChequeCuenta());
				tipoPago.setChequeBanco(bean.getChequeBanco());
				tipoPago.setChequeVencimiento(StringDateCast.castStringtoDate(bean.getChequeVencimiento()));
				tipoPago.setChequeImporte(bean.getChequeImporte());
				tipoPago.setChequeNumero(bean.getChequeNumero());
			}
			searchResults.get(7).setData(tipoPago.getTipoPago());
			searchResults.get(8).setData(bean.getComentario());

			/***/

			FacturaBean fact = null;
			for (PagoDetalleBean detalle : bean.getLineas()) {

				for (FacturaBean factura : listaFacturasSN) {
					fact = new FacturaBean();
					if(detalle.getFacturaCliente().equals(factura.getClave())){
						fact = factura;
						fact.setUtilPagoTotal(detalle.getImporte());
						listaFacturasPagar.add(fact);
//					factura.setSaldo(String.valueOf(Double.parseDouble(factura.getSaldo())+Double.parseDouble(detalle.getImporte())));
					}
				}

			}
			/***/

			estadoRegistroMovil = bean.getEstadoRegistroMovil();
			claveMovil = bean.getClaveMovil();

			llenarListaContenido();
			mostrarTotal();
			select.close();
		}catch (Exception e){
    		showMessage("construirDataEditar() > " + e.getMessage());
		}
	}
	

	private void showMessage(String message){
    	try{
			if(message != null)
				Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
		}catch(Exception e){

		}
	}

	private void sendIncomingPaymentToServer(){

		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
		String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
		String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
		String sociedad = mSharedPreferences.getString("sociedades", "-1");
		String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";
		JSONObject jsonObject = PagoBean.transformPRToJSON(pago, sociedad);

		//request to server
		JsonObjectRequest jsonObjectRequest =
				new JsonObjectRequest(Request.Method.POST, ruta + "incomingpayment/addIncomingPayment.xsjs", jsonObject,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try
								{
									if(response.getString("ResponseStatus").equals("Success")){

										Insert insert = new Insert(contexto);
										insert.updateEstadoPago(pago.getClaveMovil());
										insert.close();

										Toast.makeText(contexto,
												"Enviado al servidor",
												Toast.LENGTH_LONG).show();
										listaFacturasSN.clear();
										tipoPago.clear();
										clearVariables();

									}else{
										Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();
										listaFacturasSN.clear();
										tipoPago.clear();
										clearVariables();

										showMessage(response.getJSONObject("Response")
												.getJSONObject("message")
												.getString("value"));
									}

								}catch (Exception e){showMessage("Response - " + e.getMessage());}
							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								showMessage("VolleyError - " + error.getMessage());
							}
						}){
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						HashMap<String, String> headers = new HashMap<String, String>();
						headers.put("Content-Type", "application/json; charset=utf-8");
						return headers;
					}
				};
		VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);

	}

	;

	private class TareaRegistroPago extends AsyncTask<String, Void, Object> {

		@Override
		protected Object doInBackground(String... arg0) {
			
			shouldThreadContinue = true;

			InvocaWS ws = new InvocaWS(contexto);

			res = ws.AgregarPagoRecibido(pago);

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {

			if (res == null || res.equalsIgnoreCase("anytype{}")) {

				Insert insert = new Insert(contexto);
				insert.updateEstadoPago(pago.getClaveMovil());
				insert.close();

				Toast.makeText(contexto,
						"Enviado al servidor",
						Toast.LENGTH_LONG).show();
				listaFacturasSN.clear();
				tipoPago.clear();
				clearVariables();

			} else {

				Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();
				listaFacturasSN.clear();
				tipoPago.clear();
				clearVariables();

			}
			
			shouldThreadContinue = false;

		}

	}
	
}
