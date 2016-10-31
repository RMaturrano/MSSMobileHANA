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
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgFACT_P;
import com.proyecto.utils.StringDateCast;

public class CobranzaListaFacturasPagoFragment extends Fragment implements OnItemClickListener{

	private View v = null;
	private Context contexto;
	private String numeroFactura = "";
	private String saldoFactura = "";
	private String claveFactura = "";
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private	ListView lvListaFacturas = null;
	private ArrayList<FacturaBean> listaAdapter;
	private FacturaBean customListObjet = null;
	private	ListViewCustomAdapterFourRowAndImgFACT_P adapter;
	private int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cobranza_lista_facturas_frg, viewGroup, false);
		
	    v = view;
        contexto = view.getContext();
        lvListaFacturas = (ListView) v.findViewById(R.id.lvListaFacturasSN);

        buildListInvoices();
        lvListaFacturas.setOnItemClickListener(this);
        
        setHasOptionsMenu(true);
		return view;
	}
	
	
	
	private void buildListInvoices() {
		
		if(CobranzaFragment.listaFacturasSN.size()>0){
			listaAdapter = new ArrayList<FacturaBean>();
			for (FacturaBean bean : CobranzaFragment.listaFacturasSN) {
				
				customListObjet = new FacturaBean();
				customListObjet.setUtilIcon(icon);
				customListObjet.setClave(bean.getClave());
				customListObjet.setNumero(bean.getNumero());
				customListObjet.setReferencia(bean.getReferencia());
				customListObjet.setFechaContable(StringDateCast.castStringtoDate(bean.getFechaContable()));
				customListObjet.setTotal(bean.getTotal());
				customListObjet.setSaldo(bean.getSaldo());
				customListObjet.setUtilPagoTotal(bean.getUtilPagoTotal());
				listaAdapter.add(customListObjet);
				
			}
			
    		adapter = new ListViewCustomAdapterFourRowAndImgFACT_P(contexto, listaAdapter);
	        lvListaFacturas.setAdapter(adapter);
			
		}
		
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		view.setSelected(true);
		
        numeroFactura = listaAdapter.get(position).getNumero();
        claveFactura = listaAdapter.get(position).getClave();
        saldoFactura = listaAdapter.get(position).getSaldo();

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_lista_facturas_sn, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentManager manager = getFragmentManager();
    	final FragmentTransaction transaction = manager.beginTransaction();
    	
		switch (item.getItemId()) {
		
		case android.R.id.home:
    		
            transaction.remove(this);
            transaction.commit();
        	
            getActivity().setTitle("Lista de facturas a pagar");
        	getFragmentManager().popBackStack();
    		
    		return true;
    		
		case R.id.action_details:
			
			if(!claveFactura.equals("")){
				Intent myIntent = new Intent(contexto,
						DetalleFacturaMain.class);
				myIntent.putExtra("id", claveFactura);
				startActivity(myIntent);
			}

			return true;

		case R.id.action_aceptar:
			
			if(numeroFactura.equals("") || saldoFactura.equals("")){
				Toast.makeText(contexto, "Seleccione una factura", Toast.LENGTH_LONG).show();
				return true;
			}
			
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle("Total a pagar");

			final EditText edt = new EditText(contexto);
			edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
			edt.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			alert.setView(edt);

			if (saldoFactura != "") {

				edt.setText(saldoFactura);

			}

			edt.setFocusableInTouchMode(true);
			edt.requestFocus();

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@SuppressLint("DefaultLocale")
				public void onClick(DialogInterface dialog, int whichButton) {

					if (edt.getText().toString().length() > 0) {

						double saldoFactura = 0;
						double totalPagar = DoubleRound.round(Double.parseDouble(edt.getText().toString()), 4);
						
						InputMethodManager imm = (InputMethodManager) contexto
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
						
						for (FacturaBean factura : CobranzaFragment.listaFacturasSN) {
							if(factura.getNumero().equals(numeroFactura)){

								saldoFactura = Double.parseDouble(factura.getSaldo());
								if(saldoFactura < totalPagar){
									totalPagar = saldoFactura;
								}
								
								boolean exists = false;
								for (FacturaBean b : CobranzaFragment.listaFacturasPagar) {
									if(b.getNumero().equals(factura.getNumero())){
										b.setUtilPagoTotal(String.valueOf(totalPagar));
										exists = true;
										break;
									}
								}
								
								if(!exists){
									//Agregar la factura a la lista de facturas a pagar
									factura.setUtilPagoTotal(String.valueOf(totalPagar));
									CobranzaFragment.listaFacturasPagar.add(factura);
								}
								
								break;
							}
						}

						//ENVIAR EL AVISO
			       	 	Intent localBroadcastIntent = new Intent("event-set-total-invoice");
			            LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
			            myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

			        	transaction.remove(CobranzaListaFacturasPagoFragment.this);
			            transaction.commit();
			            getActivity().setTitle("Lista de facturas");
			            getFragmentManager().popBackStack();
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
			
			return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
	}
	
	
}
