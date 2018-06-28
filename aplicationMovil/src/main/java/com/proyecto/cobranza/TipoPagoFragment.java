package com.proyecto.cobranza;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.BancoBean;
import com.proyecto.bean.CuentaBean;
import com.proyecto.database.Select;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class TipoPagoFragment extends Fragment implements OnItemClickListener{

	private View v = null;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private FormatCustomListView sr = null;
	
	private ArrayList<CuentaBean> listaCuentas = null;
	private ArrayList<BancoBean> listaBancos = null;
	private CuentaBean cuentaSel = null;
	private BancoBean bancoSel = null;
	
	//Util
	private ConstruirAlert alert;
	private String tipoPagoSel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cobranza_medios_pago_frg, viewGroup, false);
		
	    v = view;
        contexto = view.getContext();
        lvPrincipal = (ListView) v.findViewById(R.id.lvMedioDePago);
        lvPrincipal.setOnItemClickListener(this);
        cargarListas();
        
        if(CobranzaFragment.tipoPago.getTipoPago() != null 
        		&& !CobranzaFragment.tipoPago.getTipoPago().equals("")){
        	tipoPagoSel = CobranzaFragment.tipoPago.getTipoPago();
        	if(tipoPagoSel.equalsIgnoreCase("Transferencia/Deposito")){
        		llenarListaTransferencia();
        		
        		for (CuentaBean cuenta : listaCuentas) {
					if(cuenta.getCodigo().equals(CobranzaFragment.tipoPago.getTransferenciaCuenta())){
						cuentaSel = cuenta;
						searchResults.get(1).setData(cuenta.getNombre());
						break;
					}
				}
        		searchResults.get(2).setData(CobranzaFragment.tipoPago.getTransferenciaReferencia());
        		searchResults.get(3).setData(String.valueOf(CobranzaFragment.totalPago));
        	}else if(tipoPagoSel.equalsIgnoreCase("Efectivo")){
        		llenarListaEfectivo();
        		
        		for (CuentaBean cuenta : listaCuentas) {
					if(cuenta.getCodigo().equals(CobranzaFragment.tipoPago.getEfectivoCuenta())){
						cuentaSel = cuenta;
						searchResults.get(1).setData(cuenta.getNombre());
						break;
					}
				}
        		searchResults.get(2).setData(String.valueOf(CobranzaFragment.totalPago));
        	}else if(tipoPagoSel.equalsIgnoreCase("Cheque")){
        		llenarListaCheque();
        		
        		for (CuentaBean cuenta : listaCuentas) {
					if(cuenta.getCodigo().equals(CobranzaFragment.tipoPago.getChequeCuenta())){
						cuentaSel = cuenta;
						searchResults.get(1).setData(cuenta.getNombre());
						break;
					}
				}
        		
        		for (BancoBean banco : listaBancos) {
					if(banco.getCodigo().equals(CobranzaFragment.tipoPago.getChequeBanco())){
						bancoSel = banco;
						searchResults.get(2).setData(bancoSel.getNombre());
						break;
					}
				}
        		searchResults.get(3).setData(CobranzaFragment.tipoPago.getChequeVencimiento());
        		searchResults.get(4).setData(String.valueOf(CobranzaFragment.totalPago));
        		searchResults.get(5).setData(CobranzaFragment.tipoPago.getChequeNumero());
        	}
        	
        	
        	lvPrincipal.invalidateViews();
        }else{
        	tipoPagoSel = "Efectivo";
        	CobranzaFragment.tipoPago.setTipoPago(tipoPagoSel);
        	llenarListaEfectivo();
        }
        
        setHasOptionsMenu(true);
		return view;
	}
	
	
	private void cargarListas(){
		
		listaCuentas = new ArrayList<CuentaBean>();
		
		Select select = new Select(contexto);
		listaCuentas = select.listaCuentas();
		listaBancos = select.listaBancos();
		cuentaSel = listaCuentas.get(0);
		bancoSel = listaBancos.get(0);
		
		select.close();
		
	}
	
	private void buildFirstAlert(){
		
		final String[] tipoPago = new String[3];
		tipoPago[0] = "Efectivo";
		tipoPago[1] = "Transferencia/Deposito";
		tipoPago[2] = "Cheque";
		
		int checkedItem = -1;
		if(tipoPagoSel != null){
			for (int i = 0; i < tipoPago.length; i++) {
				if(tipoPagoSel.equals(tipoPago[i])){
					checkedItem = i;
				}
			}
		}
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Seleccione el tipo de pago");
		alert.setSingleChoiceItems(tipoPago, checkedItem, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				tipoPagoSel = tipoPago[item];
			}
		});
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
				if(tipoPagoSel.equalsIgnoreCase("Transferencia/Deposito")){
					CobranzaFragment.tipoPago.setTipoPago(tipoPagoSel);
					llenarListaTransferencia();
				}
				else if(tipoPagoSel.equalsIgnoreCase("Efectivo")){
					CobranzaFragment.tipoPago.setTipoPago(tipoPagoSel);
					llenarListaEfectivo();
				}
				else if(tipoPagoSel.equalsIgnoreCase("Cheque")){
					CobranzaFragment.tipoPago.setTipoPago(tipoPagoSel);
					llenarListaCheque();
				}
		  }
		});


		alert.show();
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		alert = new ConstruirAlert();
		if(tipoPagoSel.equalsIgnoreCase("Transferencia/Deposito")){
			if(position == 0)
				buildFirstAlert();
			if(position == 1){
				
				// Capturar el objeto
				sr = new FormatCustomListView();
				sr = (FormatCustomListView) lvPrincipal.getItemAtPosition(position);
	
				AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Transferencia cuenta");
	
				// Spinner
				final Spinner spn = new Spinner(contexto);
	
				ArrayAdapter<CuentaBean> adapter = new ArrayAdapter<CuentaBean>(
						contexto, android.R.layout.simple_list_item_1,
						listaCuentas);
				spn.setAdapter(adapter);
				spn.setOnItemSelectedListener(new OnItemSelectedListener() {
	
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						// MONEDA SELECCIONADA
						cuentaSel = new CuentaBean();
						cuentaSel = (CuentaBean) parent
								.getItemAtPosition(pos);
	
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
	
					}
				});
	
				alert.setView(spn);
	
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
	
								// Do something with value!
								sr.setData(cuentaSel.getNombre());
								searchResults.set(1, sr);
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
				
			}else if(position == 2)
				alert.construirAlert(contexto, position, "Transferencia referencia", searchResults, lvPrincipal, "text",27);
			else if(position == 3)
				alert.construirAlert(contexto, position, "Transferencia importe", searchResults, lvPrincipal, "decimal",40);
		}else if(tipoPagoSel.equalsIgnoreCase("Efectivo")){
			
			if(position == 0)
				buildFirstAlert();
			else if(position == 1){
				
				// Capturar el objeto
				sr = new FormatCustomListView();
				sr = (FormatCustomListView) lvPrincipal.getItemAtPosition(position);
	
				AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Efectivo cuenta");
	
				// Spinner
				final Spinner spn = new Spinner(contexto);
	
				ArrayAdapter<CuentaBean> adapter = new ArrayAdapter<CuentaBean>(
						contexto, android.R.layout.simple_list_item_1,
						listaCuentas);
				spn.setAdapter(adapter);
				spn.setOnItemSelectedListener(new OnItemSelectedListener() {
	
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						// MONEDA SELECCIONADA
						cuentaSel = new CuentaBean();
						cuentaSel = (CuentaBean) parent
								.getItemAtPosition(pos);
	
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
	
					}
				});
	
				alert.setView(spn);
	
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
	
								// Do something with value!
								sr.setData(cuentaSel.getNombre());
								searchResults.set(1, sr);
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
				
				
			}else if(position == 2)
				alert.construirAlert(contexto, position, "Efectivo importe", searchResults, lvPrincipal, "decimal",40);
			
		}else if(tipoPagoSel.equalsIgnoreCase("Cheque")){
			
			if(position == 0)
				buildFirstAlert();
			else if(position == 1){
				
				// Capturar el objeto
				sr = new FormatCustomListView();
				sr = (FormatCustomListView) lvPrincipal.getItemAtPosition(position);
	
				AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Cheque cuenta");
	
				// Spinner
				final Spinner spn = new Spinner(contexto);
	
				ArrayAdapter<CuentaBean> adapter = new ArrayAdapter<CuentaBean>(
						contexto, android.R.layout.simple_list_item_1,
						listaCuentas);
				spn.setAdapter(adapter);
				spn.setOnItemSelectedListener(new OnItemSelectedListener() {
	
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						// MONEDA SELECCIONADA
						cuentaSel = new CuentaBean();
						cuentaSel = (CuentaBean) parent
								.getItemAtPosition(pos);
	
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
	
					}
				});
	
				alert.setView(spn);
	
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
	
								// Do something with value!
								sr.setData(cuentaSel.getNombre());
								searchResults.set(1, sr);
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
				
				
			}else if(position == 2){
				// Capturar el objeto
				sr = new FormatCustomListView();
				sr = (FormatCustomListView) lvPrincipal.getItemAtPosition(position);
	
				AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Banco");
	
				// Spinner
				final Spinner spn = new Spinner(contexto);
	
				ArrayAdapter<BancoBean> adapter = new ArrayAdapter<BancoBean>(
						contexto, android.R.layout.simple_list_item_1,
						listaBancos);
				spn.setAdapter(adapter);
				spn.setOnItemSelectedListener(new OnItemSelectedListener() {
	
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						// MONEDA SELECCIONADA
						bancoSel = new BancoBean();
						bancoSel = (BancoBean) parent
								.getItemAtPosition(pos);
	
					}
	
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
	
					}
				});
	
				alert.setView(spn);
	
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
	
								// Do something with value!
								sr.setData(bancoSel.getNombre());
								searchResults.set(2, sr);
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
			else if(position == 3)
				alert.construirAlertDatePicker(contexto, position, "Fecha vencimiento", searchResults, lvPrincipal);
			else if(position == 4)
				alert.construirAlert(contexto, position, "Importe", searchResults, lvPrincipal, "decimal",40);
			else if(position == 5)
				alert.construirAlert(contexto, position, "Número cheque", searchResults, lvPrincipal, "numeric", 10);
		}
		
	}
	
	
	
	private void llenarListaTransferencia() {

		searchResults = new ArrayList<FormatCustomListView>();
		
		sr = new FormatCustomListView();
		sr.setTitulo("Tipo de pago");
		sr.setIcon(iconId);
		sr.setData(tipoPagoSel);
		searchResults.add(sr);

		sr = new FormatCustomListView();
		sr.setTitulo("Transferencia cuenta");
		sr.setIcon(iconId);
		sr.setData(cuentaSel.getNombre());
		searchResults.add(sr);
		
		sr = new FormatCustomListView();
	  	sr.setTitulo("Transferencia referencia");
	  	sr.setIcon(iconId);
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Transferencia importe");
	  	sr.setIcon(iconId);
	  	sr.setData(String.valueOf(CobranzaFragment.totalPago));
	  	searchResults.add(sr);
	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
      	lvPrincipal.setAdapter(adapter);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}
	
	
	private void llenarListaEfectivo() {

		searchResults = new ArrayList<FormatCustomListView>();
		
		sr = new FormatCustomListView();
		sr.setTitulo("Tipo de pago");
		sr.setIcon(iconId);
		sr.setData(tipoPagoSel);
		searchResults.add(sr);

		sr = new FormatCustomListView();
		sr.setTitulo("Efectivo cuenta");
		sr.setIcon(iconId);
		sr.setData(cuentaSel.getNombre());
		searchResults.add(sr);
		
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Efectivo importe");
	  	sr.setIcon(iconId);
	  	sr.setData(String.valueOf(CobranzaFragment.totalPago));
	  	searchResults.add(sr);
	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
      	lvPrincipal.setAdapter(adapter);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}
	
	
	@SuppressLint("SimpleDateFormat") 
	private void llenarListaCheque() {

		//FECHA ACTUAL
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String currentDate = dateFormat.format(date);
		
		searchResults = new ArrayList<FormatCustomListView>();
		
		sr = new FormatCustomListView();
		sr.setTitulo("Tipo de pago");
		sr.setIcon(iconId);
		sr.setData(tipoPagoSel);
		searchResults.add(sr);
		
		sr = new FormatCustomListView();
		sr.setTitulo("Cuenta");
		sr.setIcon(iconId);
		sr.setData(cuentaSel.getNombre());
		searchResults.add(sr);
		
		sr = new FormatCustomListView();
		sr.setTitulo("Banco");
		sr.setIcon(iconId);
		sr.setData(bancoSel.getNombre());
		searchResults.add(sr);

		sr = new FormatCustomListView();
		sr.setTitulo("Vencimiento");
		sr.setIcon(iconId);
		sr.setData(currentDate);
		searchResults.add(sr);
		
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Importe");
	  	sr.setIcon(iconId);
	  	sr.setData(String.valueOf(CobranzaFragment.totalPago));
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
		sr.setTitulo("Cheque número");
		sr.setIcon(iconId);
		searchResults.add(sr);
	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
      	lvPrincipal.setAdapter(adapter);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
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
        	
            getActivity().setTitle("Cobranza preliminar");
        	getFragmentManager().popBackStack();
    		
    		return true;
		case R.id.action_aceptar:
			
			if(tipoPagoSel.equalsIgnoreCase("Transferencia/Deposito")){

				if(searchResults.get(2).getData() == null ||
						searchResults.get(2).getData().trim().equals("")){
					Toast.makeText(contexto,"Ingrese la referencia.",Toast.LENGTH_SHORT).show();
					return true;
				}

				CobranzaFragment.tipoPago.setTransferenciaCuenta(cuentaSel.getCodigo());
				CobranzaFragment.tipoPago.setTransferenciaReferencia(searchResults.get(2).getData());
				CobranzaFragment.tipoPago.setTransferenciaImporte(searchResults.get(3).getData());
			}else if(tipoPagoSel.equalsIgnoreCase("Efectivo")){
				CobranzaFragment.tipoPago.setEfectivoCuenta(cuentaSel.getCodigo());
				CobranzaFragment.tipoPago.setEfectivoImporte(searchResults.get(2).getData());
			}else if(tipoPagoSel.equalsIgnoreCase("Cheque")){
				CobranzaFragment.tipoPago.setChequeCuenta(cuentaSel.getCodigo());
				CobranzaFragment.tipoPago.setChequeBanco(bancoSel.getCodigo());
				CobranzaFragment.tipoPago.setChequeVencimiento(searchResults.get(3).getData());
				CobranzaFragment.tipoPago.setChequeImporte(searchResults.get(4).getData());
				if(searchResults.get(5).getData() != null && !searchResults.get(5).getData().toString().trim().equals("")) {
					long num = Long.parseLong(searchResults.get(5).getData().toString().trim());
					if(num <= Integer.MAX_VALUE && num >= Integer.MIN_VALUE)
						CobranzaFragment.tipoPago.setChequeNumero(searchResults.get(5).getData());
					else{
						Toast.makeText(contexto,"Número de cheque inválido, ingrese un número entre " + Integer.MIN_VALUE +
						" y " + Integer.MAX_VALUE,Toast.LENGTH_LONG).show();
						return true;
					}
				}else{
					Toast.makeText(contexto,"Ingrese el número de cheque",Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			
			//ENVIAR EL AVISO DEL TIPO DE PAGO SELECCIONADO
       	 	Intent localBroadcastIntent = new Intent("event-send-tipo-pago");
            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
            
            FragmentManager manager2 = getFragmentManager();
        	FragmentTransaction transaction2 = manager2.beginTransaction();
        	transaction2.remove(this);
        	transaction2.commit();
            
        	getActivity().setTitle("Cobranza preliminar");
        	getFragmentManager().popBackStack();
			
			return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
	}
	
	
}
