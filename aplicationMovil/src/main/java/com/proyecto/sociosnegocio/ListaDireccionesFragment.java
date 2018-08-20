package com.proyecto.sociosnegocio;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.DireccionBean;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class ListaDireccionesFragment extends Fragment{

	private View v = null;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private int posicion = 0;
	private FormatCustomListView fullObject = null;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private	ListView lvDireccionPrincipal = null;
	private	ListView lvListaDirecciones = null;
	private	ArrayList<FormatCustomListView> searchResults_prin = new ArrayList<FormatCustomListView>();
	private	ArrayList<FormatCustomListView> searchResults_cont = new ArrayList<FormatCustomListView>();
	private	ListViewCustomAdapterTwoLinesAndImg adapter = null;
	private FormatCustomListView sr = null;
		
	//LISTA DETALLE
	private RadioGroup rg = null;
	private String idDirPrincipal = "";
	
	
	//RECEPCIÓN DE MENSAJES
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    
		    Bundle bundle = intent.getExtras();

	        if (bundle != null) {
	        	
	        	String cabe = bundle.getString("cabe");

	        	if(searchResults_cont.size() == 0){

	        		//LA PRIMERA VEZ QUE VIENE UN CONTACTO DESDE contactoSocioNegocio.java
	        		if(SocioNegocioFragment.listaDirecciones.size() == 1){
	        			
	        			for (DireccionBean bean : SocioNegocioFragment.listaDirecciones) {
							
	        				searchResults_cont.clear();
	        				
	        				if(bean.getUtilId() == 0){
	        					bean.setUtilId(SocioNegocioFragment.utilId2);
	        				}
	        				
	        				sr = new FormatCustomListView();
				    		sr.setTitulo(bean.getIDDireccion());
				    		
				    		if(bean.getNombreCalle() == null)
				    			sr.setData(bean.getReferencia());
				    		else
				    			sr.setData(bean.getNombreCalle());
				    		sr.setId(bean.getUtilId());
				    		sr.setIcon(iconId);
				    		searchResults_cont.add(sr);
				    		
				    		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_cont);
				    		lvListaDirecciones.setAdapter(adapter);
				    		
				    		if(bean.isPrincipal()){
				    			
				    			idDirPrincipal = bean.getIDDireccion();
				    			//asignar el primer valor por default al contacto principal
					    		fullObject = new FormatCustomListView();
					        	fullObject = (FormatCustomListView) lvDireccionPrincipal.getItemAtPosition(0);
					    		fullObject.setData(bean.getIDDireccion());
		    					searchResults_prin.set(0, fullObject);
		    					lvDireccionPrincipal.invalidateViews();
				    			
				    		}else{
								fullObject = new FormatCustomListView();
					        	fullObject = (FormatCustomListView) lvDireccionPrincipal.getItemAtPosition(0);
					        	fullObject.setData("");
								searchResults_prin.set(0, fullObject);
								lvDireccionPrincipal.invalidateViews();
							}
				    		
	        				
						}
	        			
	        			SocioNegocioFragment.utilId2 ++;
	        			
	        		}
	        		
	        		
	        	}else{
	        		searchResults_cont.clear();
	        		
		    		for (DireccionBean b : SocioNegocioFragment.listaDirecciones) {
		    			
						if(b.getIDDireccion().equals(cabe)){
							
    						b.setUtilId(SocioNegocioFragment.utilId2);
    						sr = new FormatCustomListView();
				    		sr.setTitulo(cabe);
				    		if(b.getNombreCalle() == null)
				    			sr.setData(b.getReferencia());
				    		else
				    			sr.setData(b.getNombreCalle());
				    		sr.setId(SocioNegocioFragment.utilId2);
				    		sr.setIcon(iconId);
				    		searchResults_cont.add(sr);
							
						}else{
							sr = new FormatCustomListView();
				    		sr.setTitulo(b.getIDDireccion());
				    		if(b.getNombreCalle() == null)
				    			sr.setData(b.getReferencia());
				    		else
				    			sr.setData(b.getNombreCalle());
				    		sr.setId(b.getUtilId());
				    		sr.setIcon(iconId);
				    		searchResults_cont.add(sr);
						}
						
					}
		    		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_cont);
			        lvListaDirecciones.setAdapter(adapter);
		    		SocioNegocioFragment.utilId2++;
	        		
	        	}

	        	
	        }
		 }
	};
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.socio_negocio_lista_direcciones, viewGroup, false);
		
        getActivity().setTitle("Direcciones (todas)");
        
	    v = view;
        contexto = view.getContext();
        lvListaDirecciones = (ListView) v.findViewById(R.id.lvListaDireccionesSN);

        llenarListaDireccionPrincipal();
        
        lvDireccionPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlertDirPrincipal(position);
  		          }

		});
        
        
        lvListaDirecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	
            	//Capturar el objeto (row - fila) 
    			Object o = lvListaDirecciones.getItemAtPosition(position);
        		fullObject = new FormatCustomListView();
            	fullObject = (FormatCustomListView)o;
            	
            	MainSocioNegocio.idDireccion = fullObject.getTitulo();
            	
            	FragmentManager manager = getFragmentManager();
            	FragmentTransaction transaction = manager.beginTransaction();
            	Fragment fragment = new DireccionSocioNegocio();
    			
                transaction.hide(ListaDireccionesFragment.this);
                transaction.add(R.id.box, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

		});
        
        
        //VALIDAR SI LA LISTA DE CONTACTOS YA EXISTE
        if(SocioNegocioFragment.listaDirecciones.size()>0){

        	dysplayListDirections();
        	
        }
		        
		setHasOptionsMenu(true);
		return view;
		
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			//registro DE MENSAJE
			LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver,
					new IntentFilter("event-send-direction-to-list"));
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

	private void construirAlertDirPrincipal(int position){
		
		if(position==0){
			
			//PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
			posicion = position;
			//Capturar el objeto (row - fila) 
			Object o = lvDireccionPrincipal.getItemAtPosition(position);
    		fullObject = new FormatCustomListView();
        	fullObject = (FormatCustomListView)o;
        	//
			
        	rg = new RadioGroup(contexto);
        	
        	if(searchResults_cont.size()>0){

        		RadioButton rbt = null;
        		for (int j = 0; j < searchResults_cont.size(); j++) {
				
        			rbt = new RadioButton(contexto);
        			rbt.setId(searchResults_cont.get(j).getId());
            		rbt.setText(searchResults_cont.get(j).getTitulo());
            		rbt.setGravity(Gravity.CENTER_VERTICAL);
            		rg.addView(rbt);
        			
				}
        		
        	}
        	
        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
        	rg.setPadding(15, 0, 0, 0);
        	rg.setLayoutParams(lp);
        	rg.setGravity(Gravity.LEFT);
        	
        	
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
    		alert.setTitle("Dirección principal");
    		
    		//AGREGAR EL VIEW AL POP UP
    		alert.setView(rg);
    		
    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {

    			rg.getChildAt(rg.getCheckedRadioButtonId());
    			
    			for (FormatCustomListView item : searchResults_cont) {
					
    				if(item.getId() == rg.getCheckedRadioButtonId()){
    					
    					idDirPrincipal = item.getTitulo();
    					fullObject.setData(item.getTitulo());
    					searchResults_prin.set(posicion, fullObject);
    					lvDireccionPrincipal.invalidateViews();
    					
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
			
		}
		
	}

	
	private void llenarListaDireccionPrincipal() {
		
		searchResults_prin = new ArrayList<FormatCustomListView>();

		lvDireccionPrincipal = (ListView) v.findViewById(R.id.lvDireccionPrincipalSN);
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Dirección principal");
		sr.setIcon(iconId);
	  	searchResults_prin.add(sr);
  	
	    adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_prin);
	      
	    lvDireccionPrincipal.setAdapter(adapter);
  	
	}
	
	
	private void dysplayListDirections(){
		
		if(SocioNegocioFragment.listaDirecciones.size()>0){
			searchResults_cont.clear();
			for (DireccionBean bean : SocioNegocioFragment.listaDirecciones) {
				
				if(bean.isPrincipal()){
				
					fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView) lvDireccionPrincipal.getItemAtPosition(0);
		        	fullObject.setData(bean.getIDDireccion());
					searchResults_prin.set(0, fullObject);
					lvDireccionPrincipal.invalidateViews();
					
				}else{
					fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView) lvDireccionPrincipal.getItemAtPosition(0);
		        	fullObject.setData("");
					searchResults_prin.set(0, fullObject);
					lvDireccionPrincipal.invalidateViews();
				}
				
				
				sr = new FormatCustomListView();
				sr.setId(bean.getUtilId());
	    		sr.setTitulo(bean.getIDDireccion());
	    		sr.setIcon(iconId);
	    		if(bean.getNombreCalle() == null)
	    			sr.setData(bean.getReferencia());
	        	else
	        		sr.setData(bean.getNombreCalle());
	    		searchResults_cont.add(sr);
	    		
			}
			
			adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_cont);
	        lvListaDirecciones.setAdapter(adapter);
	        
		}else{
			SocioNegocioFragment.directionIdFiscal = 1;
			SocioNegocioFragment.directionIdEntrega = 1;
			fullObject = new FormatCustomListView();
        	fullObject = (FormatCustomListView) lvDireccionPrincipal.getItemAtPosition(0);
    		fullObject.setData("");
			searchResults_prin.set(0, fullObject);
			lvDireccionPrincipal.invalidateViews();
			searchResults_cont.clear();
			lvListaDirecciones.invalidateViews();
		}
		
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_lst_dir, menu);
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
        	
            getActivity().setTitle("Socio de negocio");
        	getFragmentManager().popBackStack();
    		
    		return true;
    		
		case R.id.action_eliminar:
        	
			rg = new RadioGroup(contexto);
        	int c = 0;
        	if(SocioNegocioFragment.listaDirecciones.size()>0){
        		
        		for (DireccionBean bean : SocioNegocioFragment.listaDirecciones) {
        			RadioButton rbt = null;
        			rbt = new RadioButton(contexto);
        			rbt.setId(c);
            		rbt.setText(bean.getIDDireccion());
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
    		alert.setTitle("Seleccione dirección");
    		
    		//AGREGAR EL VIEW AL POP UP
    		alert.setView(rg);
    		
    		alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {

    			RadioButton rbt = (RadioButton) rg.getChildAt(rg.getCheckedRadioButtonId());
    			for (DireccionBean bean : SocioNegocioFragment.listaDirecciones) {
    				
    				if(bean.getIDDireccion().equals(rbt.getText().toString())){
    					SocioNegocioFragment.listaDirecciones.remove(bean);
    					dysplayListDirections();
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
        	
        	Fragment fragment = new DireccionSocioNegocio();
			
            transaction.hide(this);
            transaction.add(R.id.box, fragment, DireccionSocioNegocio.TAG_AGREGAR_DIRECCION);
            transaction.addToBackStack(null);
            transaction.commit();

        	return true;
        case R.id.action_aceptar:
        	
        	if(searchResults_prin.get(0).getData() != "" && searchResults_prin.get(0).getData() != null){
        		
        		Bundle arguments = new Bundle();
            	
            	String defaultDirectionSend = searchResults_prin.get(0).getData();
        		arguments.putString("defaultDirection", defaultDirectionSend);
        		
        		//ACTUALIZAR EL CONTACTO PRINCIPAL
        		for (DireccionBean b : SocioNegocioFragment.listaDirecciones) {
    				
    				if(b.getIDDireccion().equals(idDirPrincipal))
    					b.setPrincipal(true);
    				else
    					b.setPrincipal(false);
    				
    			}
            	
            	//MANDAR LOS PARÀMETROS EN LOCALBORADCAST INTENT
           	 	Intent localBroadcastIntent = new Intent("event-send-direction-to-bp");
                localBroadcastIntent.putExtras(arguments);
                LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
                myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

                FragmentManager manager2 = getFragmentManager();
            	FragmentTransaction transaction2 = manager2.beginTransaction();
            	transaction2.remove(this);
            	transaction2.commit();
                
            	getActivity().setTitle("Socio de negocio");
            	getFragmentManager().popBackStack();
        		
        	}else{
        		
        		Toast.makeText(contexto, "Seleccione agregar para añadir una nueva dirección", Toast.LENGTH_LONG).show();
        		
        	}
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
		
	}

	
}
