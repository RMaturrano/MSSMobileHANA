package com.proyecto.cobranza;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgFACT;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;

public class CobranzaListaFacturasFragment extends Fragment implements OnItemClickListener{
	
	
	private View v = null;
	private Context contexto;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private	ListView lvListaFacturas = null;
	private	ListView lvTotalFacturas = null;
	private ArrayList<FacturaBean> listaAdapter;
	private FacturaBean customListObjet = null;
	private	ListViewCustomAdapterFourRowAndImgFACT adapter;
	private int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
	private ArrayList<FormatCustomListView> searchResults;
	private FormatCustomListView sr;
	private ListViewCustomAdapterTwoLinesAndImg adapterTotales;
	private double total = 0;
	private RadioGroup rg = null;
	
	//Método de recepción de mensaje
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("event-set-total-invoice")){
					buildListInvoices();
					llenarListaTotales();
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cobranza_lista_facturas_frg, viewGroup, false);
		
	    v = view;
        contexto = view.getContext();
        lvListaFacturas = (ListView) v.findViewById(R.id.lvListaFacturasSN);
        lvTotalFacturas = (ListView) v.findViewById(R.id.lvTotalFacturas);

        buildListInvoices();
        llenarListaTotales();
        lvListaFacturas.setOnItemClickListener(this);
        
        setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			//Registrar los avisos
			IntentFilter filter = new IntentFilter("event-set-total-invoice");
			LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver, filter);
		}catch (Exception e){
			Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			LocalBroadcastManager.getInstance(contexto).unregisterReceiver(myLocalBroadcastReceiver);
		}catch (Exception e){
			Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void llenarListaTotales() {

		searchResults = new ArrayList<FormatCustomListView>();
		
		total = 0;
		for (FacturaBean f : CobranzaFragment.listaFacturasPagar) {
			total += Double.parseDouble(f.getUtilPagoTotal());
		}

		sr = new FormatCustomListView();
		sr.setTitulo("Pago Total");
		sr.setData(String.valueOf(total));
		searchResults.add(sr);
	
		adapterTotales = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
      	lvTotalFacturas.setAdapter(adapterTotales);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvTotalFacturas);
		
	}
	
	private void buildListInvoices() {
		
		if(CobranzaFragment.listaFacturasPagar.size()>0){
			listaAdapter = new ArrayList<FacturaBean>();
			for (FacturaBean bean : CobranzaFragment.listaFacturasPagar) {
				
				customListObjet = new FacturaBean();
				customListObjet.setUtilIcon(icon);
				customListObjet.setNumero(bean.getNumero());
				customListObjet.setReferencia(bean.getReferencia());
				customListObjet.setFechaContable(StringDateCast.castStringtoDate(bean.getFechaContable()));
				customListObjet.setTotal(bean.getTotal());
				customListObjet.setSaldo(bean.getSaldo());
				customListObjet.setUtilPagoTotal(bean.getUtilPagoTotal());
				listaAdapter.add(customListObjet);
				
			}
			
    		adapter = new ListViewCustomAdapterFourRowAndImgFACT(contexto, listaAdapter);
	        lvListaFacturas.setAdapter(adapter);
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent message = new Intent("event-set-action");
		message.putExtra("action", "editar");
		LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(contexto);
		mgr.sendBroadcast(message);

        MainCobranzas.numeroFactura = listaAdapter.get(position).getNumero();
    	FragmentManager manager = getFragmentManager();
    	FragmentTransaction transaction = manager.beginTransaction();	
    	Fragment fragment = new FacturaSocioNegocio();
        transaction.hide(this);
        transaction.add(R.id.box_cobranza, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        getActivity().setTitle("Factura");
		
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_lista_facturas_pend, menu);
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
    		
		case R.id.action_eliminar:
			
			rg = new RadioGroup(contexto);
        	int c = 0;
        	if(CobranzaFragment.listaFacturasPagar.size()>0){
        		
        		for (FacturaBean bean : CobranzaFragment.listaFacturasPagar) {
        			RadioButton rbt = null;
        			rbt = new RadioButton(contexto);
        			rbt.setId(c);
            		rbt.setText(bean.getReferencia());
            		rbt.setGravity(Gravity.CENTER_VERTICAL);
            		rg.addView(rbt);
            		c++;
        		}
        		
        	}
        	
        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
        	rg.setPadding(15, 0, 0, 0);
        	rg.setLayoutParams(lp);
        	rg.setGravity(Gravity.LEFT);
        	
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
    		alert.setTitle("Seleccione factura");
    		
    		//AGREGAR EL VIEW AL POP UP
    		alert.setView(rg);
    		
    		alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {

    			RadioButton rbt = (RadioButton) rg.getChildAt(rg.getCheckedRadioButtonId());
    			for (FacturaBean bean : CobranzaFragment.listaFacturasPagar) {
    				
    				if(bean.getReferencia().equals(rbt.getText().toString())){
    					CobranzaFragment.listaFacturasPagar.remove(bean);
    					listaAdapter.clear();
    					searchResults.clear();
    					lvListaFacturas.invalidateViews();
    					lvTotalFacturas.invalidateViews();
    					buildListInvoices();
    			        llenarListaTotales();
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
			
			return true;
    		
		case R.id.action_agregar:
    		
			Fragment fragment = new CobranzaListaFacturasPagoFragment();
			
            transaction.hide(this);
            transaction.add(R.id.box_cobranza, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            
            getActivity().setTitle("Lista de facturas pendientes");
    		
    		return true;
		case R.id.action_aceptar:
			
			Intent message = new Intent("event-send-total-pago-facturas");
			LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(contexto);
			mgr.sendBroadcast(message);
			
			transaction.remove(this);
            transaction.commit();
        	
            getActivity().setTitle("Cobranza preliminar");
        	getFragmentManager().popBackStack();
			
			return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
	}

}
