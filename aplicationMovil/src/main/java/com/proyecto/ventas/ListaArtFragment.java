package com.proyecto.ventas;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImg;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class ListaArtFragment extends Fragment implements OnItemClickListener{
	
	private View v = null;
	private Context contexto;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private	ListView lvPrincipal = null;
	private	ListView lvCalculos = null;
	private	ArrayList<FormatCustomListView> searchResults_c = new ArrayList<FormatCustomListView>();
	private	ListViewCustomAdapterFourRowAndImg adapter;
	private ListViewCustomAdapterTwoLinesAndImg adapter_totales;
	private FormatCustomListView sr = null;
	private int lines = 0;
	private double totalAntesDescuento = 0;
	private double impuesto = 0;
	private double totalFinal = 0;
	private double porcentajeDesc = 0;
	private double totalDesc = 0;
	
	//LISTA DETALLE
	private ArrayList<ArticuloBean> listaDetalle  = null;


	//RECEPCIÓN DE MENSAJES
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    
		    Bundle bundle = intent.getExtras();

	        if (bundle != null) {
	        	
	        	if(OrdenVentaFragment.listaDetalleArticulos.size()>0){

	            	listaDetalle = OrdenVentaFragment.listaDetalleArticulos;        	
	            	lvPrincipal.setVisibility(View.VISIBLE);
	            	adapter = new ListViewCustomAdapterFourRowAndImg(contexto, OrdenVentaFragment.listaDetalleArticulos);
		            lvPrincipal.setAdapter(adapter);
		            DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		            
		            totalAntesDescuento = 0;
		            impuesto = 0;
		            
		            //SETTEAR LOS TOTALES
		            calcularTotalAntesDescuento();
		            
	        		sr = new FormatCustomListView();
		    		sr.setTitulo("Total antes del descuento");
		    		sr.setData(String.valueOf(DoubleRound.round(totalAntesDescuento,2)));
		    		searchResults_c.set(0, sr);
		    		
		    		Object oPorDesc = lvCalculos.getItemAtPosition(1);
		    		sr = new FormatCustomListView();
		        	sr = (FormatCustomListView)oPorDesc;
		        	if(porcentajeDesc == 0)
		        		sr.setData("0.00");
		        	else
		        		sr.setData(String.valueOf(porcentajeDesc));
		        	searchResults_c.set(1, sr);
		        	
		        	Object oDesc = lvCalculos.getItemAtPosition(2);
		    		sr = new FormatCustomListView();
		        	sr = (FormatCustomListView)oDesc;
		        	if(totalDesc == 0)
		        		sr.setData("0.00");
		        	else
		        		sr.setData(String.valueOf(totalDesc));
		        	searchResults_c.set(2, sr);
		        	
		        	impuesto = Math.round((impuesto - (impuesto * porcentajeDesc)) * 100.0)/100.0;
		        	Object oImp = lvCalculos.getItemAtPosition(3);
		    		sr = new FormatCustomListView();
		        	sr = (FormatCustomListView)oImp;
		        	sr.setData(String.valueOf(impuesto));
		        	searchResults_c.set(3, sr);
		    		
		        	totalFinal = Math.round((totalAntesDescuento + impuesto) * 100.0)/100.0;
		    		Object oTotal = lvCalculos.getItemAtPosition(4);
		    		sr = new FormatCustomListView();
		        	sr = (FormatCustomListView)oTotal;
		        	sr.setData(String.valueOf(totalFinal));
		        	searchResults_c.set(4, sr);
		    		
		        	lvCalculos.invalidateViews();
		    		//SETTEAR LOS TOTALES
	            	
	            }
	        	
	        }
		 }
	};
	
	private void calcularTotalAntesDescuento(){
		
		totalAntesDescuento = 0;
		impuesto= 0;
		totalFinal = 0;
		if(OrdenVentaFragment.listaDetalleArticulos.size()>0){
			for (ArticuloBean bean : OrdenVentaFragment.listaDetalleArticulos) {
	        	
				totalAntesDescuento += Math.round((bean.getTotal()) * 100.0)/100.0;
				OrdenVentaFragment.totalAntesDescuento = totalAntesDescuento;
				impuesto += Math.round(((bean.getImpuesto() * bean.getTotal())) * 100.0)/100.0;
				OrdenVentaFragment.totalImpuesto = impuesto;
				totalFinal = Math.round((totalAntesDescuento + impuesto) * 100.0)/100.0;
				OrdenVentaFragment.totalGeneral = totalFinal;
			}
		}else{
			totalAntesDescuento = 0;
			OrdenVentaFragment.totalAntesDescuento = totalAntesDescuento;
			impuesto = 0;
			OrdenVentaFragment.totalImpuesto = impuesto;
		}
		
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pedido_cliente_lista_articulos, viewGroup, false);
        getActivity().setTitle("Contenido");
        
        
	    v = view;
        contexto = view.getContext();

		lvPrincipal = (ListView) v.findViewById(R.id.lvListaArticuloPedido);
		
		buildListCalculos();
		lvCalculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlert(position);
            }
        
		});
		//
        if(OrdenVentaFragment.listaDetalleArticulos.size()>0){

        	listaDetalle = OrdenVentaFragment.listaDetalleArticulos;        	
        	dysplayListArticles();
        	
        }
        //
		
        lvPrincipal.setOnItemClickListener(this);
        setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		try{
			//registro DE MENSAJE
			LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver,
					new IntentFilter("event-send-art-to-list"));
		}catch (Exception e){

		}
	}

	@Override
	public void onPause() {
		super.onPause();

		try{
			//registro DE MENSAJE
			LocalBroadcastManager.getInstance(contexto).unregisterReceiver(myLocalBroadcastReceiver);
		}catch (Exception e){

		}
	}

	private void construirAlert(int pos){
		
		if(pos == 1){
//			
//			posicion = pos;
//			//Capturar el objeto (row - fila) 
//			Object o = lvCalculos.getItemAtPosition(posicion);
//			sr = new FormatCustomListView();
//			sr = (FormatCustomListView)o;
//
//			//Spinner
//			final EditText edtDescuento = new EditText(contexto);
//			
//			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//			alert.setTitle("% Descuento");
//			alert.setView(edtDescuento);
//			edtDescuento.setFocusableInTouchMode(true);
//			edtDescuento.requestFocus();
//			
//			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//			
//				InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
//	            imm.hideSoftInputFromWindow(edtDescuento.getWindowToken(), 0);
//				
//	            porcentajeDesc = Double.parseDouble(edtDescuento.getText().toString())/100;
//	            
//				sr.setData(edtDescuento.getText().toString());
//				searchResults_c.set(posicion, sr);
//
//				totalDesc = Math.round((porcentajeDesc * totalAntesDescuento) * 100.0)/100.0;
//				Object oDesc = lvCalculos.getItemAtPosition(2);
//	    		sr = new FormatCustomListView();
//	        	sr = (FormatCustomListView)oDesc;
//	        	sr.setData(String.valueOf(totalDesc));
//	        	searchResults_c.set(2, sr);
//	        	
//	        	impuesto = Math.round((impuesto - (impuesto * porcentajeDesc)) * 100.0)/100.0;
//	        	Object oImp = lvCalculos.getItemAtPosition(3);
//	    		sr = new FormatCustomListView();
//	        	sr = (FormatCustomListView)oImp;
//	        	sr.setData(String.valueOf(impuesto));
//	        	searchResults_c.set(3, sr);
//	        	
//	        	totalFinal = Math.round((totalAntesDescuento - totalDesc + impuesto) * 100.0)/100.0;;
//	        	Object oTotal = lvCalculos.getItemAtPosition(4);
//	    		sr = new FormatCustomListView();
//	        	sr = (FormatCustomListView)oTotal;
//	        	sr.setData(String.valueOf(totalFinal));
//	        	searchResults_c.set(4, sr);
//	        	
//	        	lvCalculos.invalidateViews();
//				
//			  }
//			});
//
//			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			  public void onClick(DialogInterface dialog, int whichButton) {
//			    // Canceled.
//			  }
//			});
//
//			edtDescuento.requestFocus();
//	        InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
//	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//			alert.show();
			
		}
		
	}
	
	
	@SuppressLint("UseSparseArrays") 
	private void buildListCalculos() {

		searchResults_c = new ArrayList<FormatCustomListView>();

		lvCalculos = (ListView) v.findViewById(R.id.lvCalculos);
		
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Total antes del descuento");
		searchResults_c.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("% descuento");
    	searchResults_c.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Descuento");
    	searchResults_c.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Impuesto");
    	searchResults_c.add(sr);
    	
    	sr = new FormatCustomListView();
    	sr.setTitulo("Total");
    	searchResults_c.add(sr);
    	
    	adapter_totales = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_c);
    	lvCalculos.setAdapter(adapter_totales);
    	DynamicHeight.setListViewHeightBasedOnChildren(lvCalculos);
		
	}
	
	//LLENAR LA LISTA DE ARTICULOS SI ES QUE YA EXISTEN
	private void dysplayListArticles(){
		
		totalAntesDescuento = 0;
		impuesto = 0;
		totalFinal = 0;
		
		lvPrincipal.setVisibility(View.VISIBLE);
		if(listaDetalle.size()>0){
			
			listaDetalle = OrdenVentaFragment.listaDetalleArticulos;        
        	adapter = new ListViewCustomAdapterFourRowAndImg( contexto, listaDetalle);
            lvPrincipal.setAdapter(adapter);
            DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);

    		totalAntesDescuento = OrdenVentaFragment.totalAntesDescuento;
    		porcentajeDesc = OrdenVentaFragment.porcentajeDescuento;
    		totalDesc = OrdenVentaFragment.totalDescuento;
    		impuesto = OrdenVentaFragment.totalImpuesto;
    		totalFinal = OrdenVentaFragment.totalGeneral;
    	
		}else{
			lvPrincipal.setVisibility(View.GONE);
		}
    		
    		sr = new FormatCustomListView();
    		sr.setTitulo("Total antes del descuento");
    		sr.setData(String.valueOf(totalAntesDescuento));
    		searchResults_c.set(0, sr);

    		Object oDesc = lvCalculos.getItemAtPosition(1);
	    	sr = new FormatCustomListView();
	        sr = (FormatCustomListView)oDesc;
	        sr.setData(String.valueOf(porcentajeDesc));
	        searchResults_c.set(1, sr);
	
    		Object oTotDesc = lvCalculos.getItemAtPosition(2);
	    	sr = new FormatCustomListView();
	        sr = (FormatCustomListView)oTotDesc;
	        sr.setData(String.valueOf(totalDesc));
	        searchResults_c.set(2, sr);

    		Object oImp = lvCalculos.getItemAtPosition(3);
    		sr = new FormatCustomListView();
        	sr = (FormatCustomListView)oImp;
        	sr.setData(String.valueOf(impuesto));
        	searchResults_c.set(3, sr);
    		
    		Object oTotal = lvCalculos.getItemAtPosition(4);
    		sr = new FormatCustomListView();
        	sr = (FormatCustomListView)oTotal;
        	sr.setData(String.valueOf(totalFinal));
        	searchResults_c.set(4, sr);
    		
    		lvCalculos.invalidateViews();
    		//SETTEAR EL TOTAL
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.menu_lst_art, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
		
		switch (item.getItemId()) {
		
		case 16908332:
    		
            transaction.remove(this);
            transaction.commit();
        	
            getActivity().setTitle("Orden de venta");
        	getFragmentManager().popBackStack();
    		
    		return true;
		
        case R.id.action_agregar:

			if(OrdenVentaFragment.listaDetalleArticulos.size() <
					PreferenceManager.getDefaultSharedPreferences(contexto).getInt("MaxLineas",0)) {
				Fragment fragment = new ArticuloOrdVenta();

				transaction.hide(this);
				transaction.add(R.id.box, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}else{
				Toast.makeText(contexto,"Está sobrepasando la cantidad máxima de líneas permitidas por pedido",Toast.LENGTH_SHORT).show();
			}
    	    
        	return true;
        	
        case R.id.action_eliminar:
        	
        	final RadioGroup rg = new RadioGroup(contexto);
        	
        	if(OrdenVentaFragment.listaDetalleArticulos.size()>0){
        		int c = 0;
        		for (ArticuloBean articulo : OrdenVentaFragment.listaDetalleArticulos) {
        			RadioButton rbt = null;
        			rbt = new RadioButton(contexto);
        			rbt.setId(c);
            		rbt.setText(articulo.getDesc());
            		if(c == 0)
            			rbt.setSelected(true);
            		rbt.setGravity(Gravity.CENTER_VERTICAL);
            		rg.addView(rbt);
            		c++;
				}


	        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
				ScrollView scrollView = new ScrollView(contexto);
				scrollView.setLayoutParams(lp);
	        	rg.setPadding(15, 0, 0, 0);
	        	rg.setLayoutParams(lp);
	        	rg.setGravity(Gravity.LEFT);
	        	rg.setVerticalScrollBarEnabled(true);
	        	scrollView.addView(rg);

				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
	    		alert.setTitle("Seleccione artículo");
	    		
	    		//AGREGAR EL VIEW AL POP UP
	    		alert.setView(scrollView);
	    		
	    		alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	
	    			RadioButton rbt = (RadioButton) rg.getChildAt(rg.getCheckedRadioButtonId());
	    			for (ArticuloBean bean : OrdenVentaFragment.listaDetalleArticulos) {
	    				
	    				if(bean.getDesc().equals(rbt.getText().toString())){
	    					OrdenVentaFragment.listaDetalleArticulos.remove(bean);
	    					calcularTotalAntesDescuento();
	    					dysplayListArticles();
	    					break;
	    				}
	    				
					}
	    			
	    		  }
	    		});
	
	    		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	    		  public void onClick(DialogInterface dialog, int whichButton) {
	    		    // Canceled.
	    		  }
	    		});
	
	    		alert.show();
    		
        	}
    	    
        	return true;
        case R.id.action_aceptar:
        	
        	if(listaDetalle != null){
        		
        		Bundle arguments = new Bundle();
            	
            	lines = listaDetalle.size();
        		arguments.putString("lines", String.valueOf(lines));
        		
        		OrdenVentaFragment.totalAntesDescuento = totalAntesDescuento;
        		OrdenVentaFragment.porcentajeDescuento = porcentajeDesc;
        		OrdenVentaFragment.totalDescuento = totalDesc;
        		OrdenVentaFragment.totalImpuesto = impuesto;
        		OrdenVentaFragment.totalGeneral = totalFinal;
            	
            	//MANDAR LOS PARÀMETROS EN LOCALBORADCAST INTENT
           	 	Intent localBroadcastIntent = new Intent("event-send-lines-to-order");
                localBroadcastIntent.putExtras(arguments);
                LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
                myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

                FragmentManager manager2 = getFragmentManager();
            	FragmentTransaction transaction2 = manager2.beginTransaction();
            	transaction2.remove(this);
            	transaction2.commit();
                
            	getActivity().setTitle("Orden de venta");
            	getActivity().getFragmentManager().popBackStack();
        		
        	}else{
        		
        		Toast.makeText(contexto, "Agregue artículos al pedido", Toast.LENGTH_LONG).show();
        		
        	}
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
		
	}


	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        
        MainVentas.codigoArticulo= OrdenVentaFragment.listaDetalleArticulos.get(position).getCod();
		MainVentas.position = position;

		Fragment fragment = new ArticuloOrdVenta();
		FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(this);
        transaction.add(R.id.box, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
		
	}
	

}
