package com.proyecto.sociosnegocio;

import java.util.ArrayList;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.ContactoBean;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class ContactoSocioNegocio extends Fragment{

	private View v;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private ConstruirAlert alert = new ConstruirAlert();
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	
	private String tipoRegistro = "Nuevo";
	private String idContactoUpdate = "";
	
	FormatCustomListView fullObject = null;
	int posicion = 0;
	
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.socio_negocio_contacto, viewGroup, false);
	    
	        getActivity().setTitle("Contacto de socio de negocio");
	        
		    v = view;
	        contexto = view.getContext();
	        
	        
	        //LLENAR EL LISTADO DE DATOS QUE COMPONEN LA ORDEN DE VENTA
	        llenarListPrincipal();
	        
	        if(MainSocioNegocio.idContacto.equals("")){
	        	tipoRegistro = "Nuevo";
	        }else{
	        	tipoRegistro = "Actualizacion";
	        	idContactoUpdate = MainSocioNegocio.idContacto;
	        }
	        
	        //PROGRAMAR EL CLICK HACIA EL BLOQUE PRINCIPAL PARA CADA DATO
	        lvPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                                int position, long id) {
					construirAlert(position);
	        }
	
		});
		////
	    
	   
	    setHasOptionsMenu(true);
	
	    return view;
		
	}
	
	
	private void refreshId(){
		
		if(searchResults.get(1).getData() != null){
			searchResults.get(0).setData(searchResults.get(1).getData());
		}
		
		if(searchResults.get(2).getData() != null){
			String all = searchResults.get(0).getData();
			searchResults.get(0).setData(all + " " + searchResults.get(2).getData());
		}
		
		if(searchResults.get(3).getData() != null){
			String all = searchResults.get(0).getData();
			searchResults.get(0).setData(all + " " + searchResults.get(3).getData());
		}
		
	}
	
	
	private void construirAlert(int position){
		
		if(position == 1){
			
			construirAlert(position, "Primer nombre", searchResults, lvPrincipal);
			
		}else if(position == 2){
			
			if(searchResults.get(1).getData() != null)
				construirAlert(position, "Segundo nombre", searchResults, lvPrincipal);
			else
				Toast.makeText(contexto, "Ingrese el primer nombre", Toast.LENGTH_SHORT).show();

		}else if(position == 3){
			
			if(searchResults.get(1).getData() != null)
				construirAlert(position, "Apellidos", searchResults, lvPrincipal);
			else
				Toast.makeText(contexto, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
			
		}else if(position == 4){
			
			alert.construirAlert(contexto, position, "Dirección", searchResults, lvPrincipal, "text",100);
			
		}else if(position == 5){
			
			alert.construirAlert(contexto, position, "Teléfono 1", searchResults, lvPrincipal, "numeric",20);
			
		}else if(position == 6){
			
			alert.construirAlert(contexto, position, "Teléfono 2", searchResults, lvPrincipal, "numeric",20);
			
		}else if(position == 7){
			
			alert.construirAlert(contexto, position, "Teléfono móvil", searchResults, lvPrincipal, "numeric",50);
			
		}else if(position == 8){
			
			alert.construirAlert(contexto, position, "Correo electrónico", searchResults, lvPrincipal, "text",100);
			
		}else if(position == 9){
			
			alert.construirAlert(contexto, position, "Posición", searchResults, lvPrincipal, "text",90);
			
		}
		
	}
	
	
	public void construirAlert(final int posicion, String titulo,
			final ArrayList<FormatCustomListView> searchResults,
			final ListView lv){
		
		fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView) lv.getItemAtPosition(posicion);
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle(titulo);
		
		final EditText edt = new EditText(contexto);
		alert.setView(edt);
		
		edt.setFocusableInTouchMode(true);
		edt.requestFocus();
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(50);
		edt.setFilters(fArray);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		
				InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
				
				fullObject.setData(edt.getText().toString());
				searchResults.set(posicion, fullObject);
				refreshId();
				lv.invalidateViews();
			
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			// Check if no view has focus:
			  InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
			}
		});
		
		edt.requestFocus();
		
		InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		
		alert.show();
		
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    	inflater.inflate(R.menu.menu_art_ordventa, menu);
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
	        	
	            if(!tipoRegistro.equalsIgnoreCase("Actualizacion")){
	            	if(SocioNegocioFragment.contactId >1)
		            	SocioNegocioFragment.contactId --;
	            }
	            
	            MainSocioNegocio.idContacto = "";
	            getActivity().setTitle("Contactos (todos)");
	        	getFragmentManager().popBackStack();
        		
        		return true;
			
	        case R.id.action_aceptar:
	        	
	        	if(searchResults.get(0).getData() != "" && searchResults.get(0).getData() != null &&
	        			searchResults.get(1).getData() != "" && searchResults.get(1).getData() != null &&
	        			searchResults.get(3).getData() != "" && searchResults.get(3).getData() != null ){
	        		
	        		
	        		Bundle arguments = new Bundle();
		        	String cabe = searchResults.get(0).getData();
		        	String desc = searchResults.get(1).getData()+" "+searchResults.get(3).getData();
		        	
		    		arguments.putString("cabe", cabe);
		    	    arguments.putString("desc", desc);
		    	    
		    	    
		    	    //MANDAR POR DEBAJO HACIA SOOCIONEGOCIOFRAGMENT EL BEAN PARA LLENAR LA LISTA PRINCIPAL
		    	    ContactoBean bean = new ContactoBean();
		    	    bean.setIdCon(searchResults.get(0).getData());
		    	    bean.setNomCon(searchResults.get(0).getData());
		    	    bean.setPrimerNombre(searchResults.get(1).getData());
		    	    bean.setSegNomCon(searchResults.get(2).getData());
		    	    bean.setApeCon(searchResults.get(3).getData());
		    	    bean.setDireccion(searchResults.get(4).getData());
		    	    bean.setTel1Con(searchResults.get(5).getData());
		    	    bean.setTel2Con(searchResults.get(6).getData());
		    	    bean.setTelMovCon(searchResults.get(7).getData());
		    	    bean.setEmailCon(searchResults.get(8).getData());
		    	    bean.setPosicion(searchResults.get(9).getData());
		    	    if(SocioNegocioFragment.listaDetalleContactos.size()== 0){
		    	    	bean.setPrincipal(true);
		    	    }else
		    	    	bean.setPrincipal(false);
		    	    
		    	    bean.setUtilId(SocioNegocioFragment.utilId);
		    	    
		    	    if(tipoRegistro.equalsIgnoreCase("Nuevo")){
		    	    	SocioNegocioFragment.listaDetalleContactos.add(bean);
		    	    }else{
		    	    	for (int i = 0; i < SocioNegocioFragment.listaDetalleContactos.size(); i++) {
							
		    	    		if(SocioNegocioFragment.listaDetalleContactos.get(i).getIdCon().equals(idContactoUpdate)){
		    	    			SocioNegocioFragment.listaDetalleContactos.remove(i);
		    	    			SocioNegocioFragment.listaDetalleContactos.add(i, bean);
		    	    			break;
		    	    		}else
		    	    			i++;
		    	    		
						}
		    	    }
		    	    
		    	    SocioNegocioFragment.utilId++;
		    	    
		        	
		        	//MANDAR LOS PARÀMETROS EN LOCALBROADCAST INTENT
		        	 Intent localBroadcastIntent = new Intent("event-send-contact-to-list");
		             localBroadcastIntent.putExtras(arguments);
		             LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		             myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

		            transaction.remove(this);
		            transaction.commit();
		        	
		            getActivity().setTitle("Contactos (todos)");
		        	getFragmentManager().popBackStack();
		        	
		        	MainSocioNegocio.idContacto = "";
		    	    
	        	}else{
	        		
	        		Toast.makeText(contexto, "Nombres del contacto son necesarios", Toast.LENGTH_LONG).show();
	        		
	        	}
	        	
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	      
			}
			
	}
	
	
	private void llenarListPrincipal() {

		//correlativo
		SocioNegocioFragment.contactId ++;
		//correlativo
		
		searchResults = new ArrayList<FormatCustomListView>();

		lvPrincipal = (ListView) v.findViewById(R.id.lvContactoSN);
		
		ContactoBean contacto = null;
		if(!MainSocioNegocio.idContacto.equals("")){
			for (ContactoBean bean : SocioNegocioFragment.listaDetalleContactos) {
				if(bean.getIdCon().equals(MainSocioNegocio.idContacto)){
					contacto = new ContactoBean();
					contacto = bean;
					break;
				}
			}
		}
		
		FormatCustomListView sr1 = new FormatCustomListView();
    	sr1.setTitulo("ID contacto");
    	if(contacto != null && !contacto.getIdCon().equals(""))
			sr1.setData(contacto.getIdCon());
    	searchResults.add(sr1);

    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Primer nombre");
    	if(contacto != null && contacto.getPrimerNombre() != null)
			sr1.setData(contacto.getPrimerNombre());
    	sr1.setIcon(iconId);
    	searchResults.add(sr1);

    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Segundo nombre");
    	if(contacto != null && contacto.getSegNomCon() != null)
			sr1.setData(contacto.getSegNomCon());
    	sr1.setIcon(iconId);
    	searchResults.add(sr1);

    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Apellidos");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getApeCon() != null)
			sr1.setData(contacto.getApeCon());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Dirección");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getDireccion() != null)
			sr1.setData(contacto.getDireccion());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Teléfono 1");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getTel1Con() != null)
			sr1.setData(contacto.getTel1Con());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Teléfono 2");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getTel2Con() != null)
			sr1.setData(contacto.getTel2Con());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Teléfono móvil");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getTelMovCon() != null)
			sr1.setData(contacto.getTelMovCon());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Correo electrónico");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getEmailCon()!= null)
			sr1.setData(contacto.getEmailCon());
    	searchResults.add(sr1);
    	
    	sr1 = new FormatCustomListView();
    	sr1.setTitulo("Posición");
    	sr1.setIcon(iconId);
    	if(contacto != null && contacto.getPosicion()!= null)
			sr1.setData(contacto.getPosicion());
    	searchResults.add(sr1);
    	
    	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
        lvPrincipal.setAdapter(adapter);
		
	}
	
	
}
