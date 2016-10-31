package com.proyecto.cobranza;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;

public class FacturaSocioNegocio extends Fragment implements OnItemClickListener{
	
	private View v;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private FormatCustomListView sr = null;
	private FacturaBean facturaActual = null;
	
	//Parámetro
	private String claveFactura;
	
	//Util
	private String pagoTotal;
	
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.cobranza_socio_negocio_factura, viewGroup, false);
	    
	    v = view;
        contexto = view.getContext();
        lvPrincipal = (ListView) v.findViewById(R.id.lvFacturaSN);
        lvPrincipal.setOnItemClickListener(this);
	   
 		llenarListaFactura();
 		
	    setHasOptionsMenu(true);
	    return view;
		
	}
	
	
	private void llenarListaFactura() {
		
		if(!MainCobranzas.numeroFactura.equals("")){
			claveFactura = MainCobranzas.numeroFactura;
		}else
			return;
		
		searchResults = new ArrayList<FormatCustomListView>();
		for (FacturaBean bean : CobranzaFragment.listaFacturasSN) {
			if(bean.getNumero().equals(claveFactura)){
				
				facturaActual = bean;

			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Número Sunat");
			  	sr.setData(bean.getReferencia());
			  	searchResults.add(sr);
			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Referencia");
//			  	sr.setData(bean.getReferencia());
//			  	searchResults.add(sr);
//			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Lista de precio");
//			  	sr.setData(bean.getListaPrecio());
//			  	searchResults.add(sr);
		  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Contacto");
//			  	sr.setData(bean.getContacto());
//			  	searchResults.add(sr);
//			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Moneda");
			  	sr.setData(bean.getMoneda());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Comentario");
			  	sr.setData(bean.getComentario());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Fecha contable");
			  	sr.setData(StringDateCast.castStringtoDate(bean.getFechaContable()));
			  	searchResults.add(sr);
			
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Fecha de vencimiento");
			  	sr.setData(StringDateCast.castStringtoDate(bean.getFechaVencimiento()));
			  	searchResults.add(sr);
			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Dirección fiscal");
//			  	sr.setData(bean.getDireccionFiscal());
//			  	searchResults.add(sr);
//			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Dirección de entrega");
//			  	sr.setData(bean.getDireccionEntrega());
//			  	searchResults.add(sr);
//			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Condicion de pago");
//			  	sr.setData(bean.getCondicionPago());
//			  	searchResults.add(sr);
//			  	
//			  	sr = new FormatCustomListView();
//			  	sr.setTitulo("Indicador");
//			  	sr.setData(bean.getIndicador());
//			  	searchResults.add(sr);
			  	
				sr = new FormatCustomListView();
			  	sr.setTitulo("SubTotal");
			  	sr.setData(bean.getSubTotal());
			  	searchResults.add(sr);
			  	
				sr = new FormatCustomListView();
			  	sr.setTitulo("Descuento");
			  	sr.setData(bean.getDescuento());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Impuesto");
			  	sr.setData(bean.getImpuesto());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Total");
			  	sr.setData(bean.getTotal());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Saldo");
			  	sr.setData(bean.getSaldo());
			  	searchResults.add(sr);
			  	
			  	sr = new FormatCustomListView();
			  	sr.setTitulo("Pago Total");
			  	sr.setData(bean.getUtilPagoTotal());
			  	sr.setIcon(iconId);
			  	searchResults.add(sr);
			  	
			  	pagoTotal = bean.getSaldo();
			  	break;
			}
		}
		
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
      	lvPrincipal.setAdapter(adapter);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		
		if(position == 10){


				sr = new FormatCustomListView();
				sr = (FormatCustomListView) lvPrincipal.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
				alert.setTitle("Total a pagar");

				final EditText edt = new EditText(contexto);
				edt.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				alert.setView(edt);
				edt.setText(sr.getData());
				edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
				edt.setFocusableInTouchMode(true);
				edt.requestFocus();

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@SuppressLint("DefaultLocale")
					public void onClick(DialogInterface dialog, int whichButton) {

						if (edt.getText().toString().length() > 0) {

							InputMethodManager imm = (InputMethodManager) contexto
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

							double val = DoubleRound.round(Double.parseDouble(edt.getText().toString()),6);
							
							if(val> Double.parseDouble(pagoTotal)){
								sr.setData(pagoTotal);
								facturaActual.setUtilPagoTotal(pagoTotal);
							}else{
								sr.setData(String.valueOf(val));
								facturaActual.setUtilPagoTotal(String.valueOf(val));
							}
							searchResults.set(position, sr);
							lvPrincipal.invalidateViews();

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
		
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_lista_facturas, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();
    	
		switch (item.getItemId()) {
		
		case android.R.id.home:
    		
            transaction.remove(this);
            transaction.commit();
        	
            getActivity().setTitle("Lista de facturas pendientes");
        	getFragmentManager().popBackStack();
    		
    		return true;
		case R.id.action_aceptar:
			
			//ENVIAR EL AVISO
       	 	Intent localBroadcastIntent = new Intent("event-set-total-invoice");
            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
	
        	int index = CobranzaFragment.listaFacturasPagar.indexOf(facturaActual);
            CobranzaFragment.listaFacturasPagar.set(index, facturaActual);
            
        	transaction.remove(this);
            transaction.commit();
            getActivity().setTitle("Lista de facturas");
            getFragmentManager().popBackStack();
			
			return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
	}
	

}
