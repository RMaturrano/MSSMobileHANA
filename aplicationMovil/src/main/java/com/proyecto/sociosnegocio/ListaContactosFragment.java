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
import com.proyecto.bean.ContactoBean;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class ListaContactosFragment extends Fragment{

	public static String TAG_LISTA_CONTACTOS = "frgm_lst_contacts";

	private View v = null;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private int posicion = 0;
	private FormatCustomListView fullObject = null;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private	ListView lvContactoPrincipal = null;
	private	ListView lvListaContactos = null;
	private	ArrayList<FormatCustomListView> searchResults_prin = new ArrayList<FormatCustomListView>();
	private	ArrayList<FormatCustomListView> searchResults_cont = new ArrayList<FormatCustomListView>();
	private	ListViewCustomAdapterTwoLinesAndImg adapter;
	private FormatCustomListView sr = null;
		
	//LISTA DETALLE
	private ArrayList<ContactoBean> listaDetalle  = null;
	private RadioGroup rg = null;
	private String idContPrincipal = "";
	
	
	//region RECEPCIÓN DE MENSAJES
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
			  @Override
			  public void onReceive(Context context, Intent intent) {
			    
			    Bundle bundle = intent.getExtras();

		        if (bundle != null) {
		        	if(intent.getAction().equals("event-send-contact-to-list")){
			        
				        	if(searchResults_cont.size() == 0){
				        		
				        		//LA PRIMERA VEZ QUE VIENE UN CONTACTO DESDE contactoSocioNegocio.java
				        		if(SocioNegocioFragment.listaDetalleContactos.size() == 1){
				        			
				        			for (ContactoBean bean : SocioNegocioFragment.listaDetalleContactos) {
										
				        				if(bean.getUtilId() == 0){
				        					bean.setUtilId(SocioNegocioFragment.utilId);
				        				}
				        				
				        				sr = new FormatCustomListView();
							    		sr.setTitulo(bean.getIdCon());
							    		sr.setData(bean.toString());
							    		sr.setId(bean.getUtilId());
							    		searchResults_cont.add(sr);
							    		
							    		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_cont);
							    		lvListaContactos.setAdapter(adapter);
							    		
							    		if(bean.isPrincipal()){
							    			
							    			idContPrincipal = bean.getIdCon();
							    			//asignar el primer valor por default al contacto principal
								    		fullObject = new FormatCustomListView();
								        	fullObject = (FormatCustomListView) lvContactoPrincipal.getItemAtPosition(0);
								    		fullObject.setData(bean.toString());
					    					searchResults_prin.set(0, fullObject);
					    					lvContactoPrincipal.invalidateViews();
							    			
							    		}
									}
				        			
				        			SocioNegocioFragment.utilId ++;
				        		}
				        	}else{
				        		listaDetalle = SocioNegocioFragment.listaDetalleContactos;
					        	dysplayListContacts();
				        	}

		        	}else if(intent.getAction().equals("event-get-contact-from-directory")){

		        		String name = bundle.getString("name");
			        	String phone = bundle.getString("phone");
		        		
			        	ContactoBean bean = new ContactoBean();
			    	    bean.setIdCon(name);
			    	    bean.setNomCon(name);
			    	    bean.setTelMovCon(phone);
			    	    bean.setUtilId(SocioNegocioFragment.utilId);
			    	    
			    	    if(SocioNegocioFragment.listaDetalleContactos.size()== 0){
			    	    	bean.setPrincipal(true);
			    	    }else
			    	    	bean.setPrincipal(false);
			    	    
			    	    SocioNegocioFragment.listaDetalleContactos.add(bean);
			        	SocioNegocioFragment.contactId++;
			        	SocioNegocioFragment.utilId++;
			        	
			        	listaDetalle = SocioNegocioFragment.listaDetalleContactos;     
			        	dysplayListContacts();
			        	
		        	}
		        }
			 }
		};
	//endregion
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.socio_negocio_lista_contactos, viewGroup, false);
		
        getActivity().setTitle("Contactos (todos)");
        
	    v = view;
        contexto = view.getContext();
        lvListaContactos = (ListView) v.findViewById(R.id.lvListaContactosSN);
        
      //registro DE MENSAJE
        LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver,
			      new IntentFilter("event-send-contact-to-list"));

        llenarListaContactoPrincipal();
        
        lvContactoPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    				construirAlertContPrincipal(position);
            }

		});
        
        lvListaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	//Capturar el objeto (row - fila) 
    			Object o = lvListaContactos.getItemAtPosition(position);
        		fullObject = new FormatCustomListView();
            	fullObject = (FormatCustomListView)o;
            	
            	MainSocioNegocio.idContacto = fullObject.getTitulo();
            	
            	FragmentManager manager = getFragmentManager();
            	FragmentTransaction transaction = manager.beginTransaction();
            	Fragment fragment = new ContactoSocioNegocio();
    			
                transaction.hide(ListaContactosFragment.this);
                transaction.add(R.id.box, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
		});
        
        
        //VALIDAR SI LA LISTA DE CONTACTOS YA EXISTE
        if(SocioNegocioFragment.listaDetalleContactos.size()>0){

        	listaDetalle = SocioNegocioFragment.listaDetalleContactos;        	
        	dysplayListContacts();
        	
        }
        
        setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			//registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
			IntentFilter filter = new IntentFilter("event-send-contact-to-list");
			filter.addAction("event-get-contact-from-directory");
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

	private void construirAlertContPrincipal(int position){
		
		if(position==0){
			
			//PARA EL REGRESO DE LA INFO HACIA lA PANTALLA PRINCIPAL
			posicion = position;
			//Capturar el objeto (row - fila) 
			Object o = lvContactoPrincipal.getItemAtPosition(position);
    		fullObject = new FormatCustomListView();
        	fullObject = (FormatCustomListView)o;
        	//
        	rg = new RadioGroup(contexto);
        	
        	if(searchResults_cont.size()>0){

        		RadioButton rbt = null;
        		for (int j = 0; j < searchResults_cont.size(); j++) {
        			rbt = new RadioButton(contexto);
        			rbt.setId(searchResults_cont.get(j).getId());
            		rbt.setText(searchResults_cont.get(j).getData());
            		rbt.setGravity(Gravity.CENTER_VERTICAL);
            		rg.addView(rbt);
				}
        	}
        	
        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
        	rg.setPadding(15, 0, 0, 0);
        	rg.setLayoutParams(lp);
        	rg.setGravity(Gravity.LEFT);

			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
    		alert.setTitle("Contacto principal");
    		
    		//AGREGAR EL VIEW AL POP UP
    		alert.setView(rg);
    		
    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {

    			rg.getChildAt(rg.getCheckedRadioButtonId());
    			
    			for (FormatCustomListView item : searchResults_cont) {
					
    				if(item.getId() == rg.getCheckedRadioButtonId()){
    					
    					idContPrincipal = item.getTitulo();
    					fullObject.setData(item.getData());
    					searchResults_prin.set(posicion, fullObject);
    					lvContactoPrincipal.invalidateViews();
    					
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

	private void llenarListaContactoPrincipal() {
		
		searchResults_prin = new ArrayList<FormatCustomListView>();

		lvContactoPrincipal = (ListView) v.findViewById(R.id.lvContactoPrincipalSN);
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Contacto principal");
		sr.setIcon(iconId);
	  	searchResults_prin.add(sr);
  	
	    adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults_prin);
	      
	    lvContactoPrincipal.setAdapter(adapter);
  	
	}

	//LLENAR LA LISTA DE CONTACTOS SI ES QUE YA EXISTEN
	private void dysplayListContacts(){
			
			if(listaDetalle.size()>0){
				
				searchResults_cont.clear();
				
				for (ContactoBean bean : listaDetalle) {
					
					if(bean.isPrincipal()){
					
						idContPrincipal = bean.getIdCon();
						fullObject = new FormatCustomListView();
			        	fullObject = (FormatCustomListView) lvContactoPrincipal.getItemAtPosition(0);
			    		fullObject.setData(bean.getNomCon());
    					searchResults_prin.set(0, fullObject);
    					lvContactoPrincipal.invalidateViews();
						
					}else{
						fullObject = new FormatCustomListView();
			        	fullObject = (FormatCustomListView) lvContactoPrincipal.getItemAtPosition(0);
			    		fullObject.setData("");
    					searchResults_prin.set(0, fullObject);
    					lvContactoPrincipal.invalidateViews();
					}
					
					sr = new FormatCustomListView();
					sr.setId(bean.getUtilId());
		    		sr.setTitulo(bean.getIdCon());
		    		sr.setIcon(iconId);
		    		sr.setData(bean.getNomCon());
		    		searchResults_cont.add(sr);
		    		
				}
				
				adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_cont);
		        lvListaContactos.setAdapter(adapter);
				
			}else{
				fullObject = new FormatCustomListView();
	        	fullObject = (FormatCustomListView) lvContactoPrincipal.getItemAtPosition(0);
	    		fullObject.setData("");
				searchResults_prin.set(0, fullObject);
				lvContactoPrincipal.invalidateViews();
				searchResults_cont.clear();
				lvListaContactos.invalidateViews();
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
		
		case 16908332:
    		
            transaction.remove(this);
            transaction.commit();
        	
            getActivity().setTitle("Socio de negocio");
        	getFragmentManager().popBackStack();
    		
    		return true;
    		
		case R.id.action_eliminar:
			
			rg = new RadioGroup(contexto);
        	int c =0;
        	if(SocioNegocioFragment.listaDetalleContactos.size()>0){
        		
        		for (ContactoBean bean : SocioNegocioFragment.listaDetalleContactos) {
        			RadioButton rbt = null;
        			rbt = new RadioButton(contexto);
        			rbt.setId(c);
            		rbt.setText(bean.getNomCon());
            		rbt.setGravity(Gravity.CENTER_VERTICAL);
            		rg.addView(rbt);
            		c++;
        		}
        		
	        	
	        	
	        	LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);        	
	        	rg.setPadding(15, 0, 0, 0);
	        	rg.setLayoutParams(lp);
	        	rg.setGravity(Gravity.LEFT);
	        	
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
	    		alert.setTitle("Seleccione contacto");
	    		
	    		//AGREGAR EL VIEW AL POP UP
	    		alert.setView(rg);
	    		
	    		alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int whichButton) {
	
	    			RadioButton rbt = (RadioButton) rg.getChildAt(rg.getCheckedRadioButtonId());
	    			for (ContactoBean bean : SocioNegocioFragment.listaDetalleContactos) {
	    				
	    				if(bean.getNomCon().equals(rbt.getText().toString())){
	    					SocioNegocioFragment.listaDetalleContactos.remove(bean);
	    					dysplayListContacts();
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
		
        case R.id.action_agregar:
        	
        	
        	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(contexto);
            myAlertDialog.setTitle("Opciones de contacto de socio de negocio");
            myAlertDialog.setMessage("¿De donde desea agregarlo?");
            myAlertDialog.setPositiveButton("Agenda del telefono",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            
                        	Intent listContacts = new Intent(contexto, PhoneContactsListActivity.class);
                        	//startActivity(listContacts);
                        	getActivity().startActivityForResult(listContacts, PhoneContactsListActivity.REQUEST_CONTACT_TLF);
                        	
                        }
                    });

            myAlertDialog.setNegativeButton("Nuevo contacto",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                           
                        	FragmentManager manager = getFragmentManager();
                        	FragmentTransaction transaction = manager.beginTransaction();
                        	Fragment fragment = new ContactoSocioNegocio();
                			
                            transaction.hide(ListaContactosFragment.this);
                            transaction.add(R.id.box, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    });
            myAlertDialog.show();
        	
    	    
        	return true;
        	
        	
        case R.id.action_aceptar:
        	
        	if(searchResults_prin.get(0).getData() != "" && searchResults_prin.get(0).getData() != null){
        		
        		Bundle arguments = new Bundle();
            	
            	String defaultContactSend = searchResults_prin.get(0).getData();
        		arguments.putString("defaultContact", defaultContactSend);
        		
        		//ACTUALIZAR EL CONTACTO PRINCIPAL
        		for (ContactoBean b : SocioNegocioFragment.listaDetalleContactos) {
    				
    				if(b.getIdCon().equals(idContPrincipal))
    					b.setPrincipal(true);
    				else
    					b.setPrincipal(false);
    				
    			}
            	
            	//MANDAR LOS PARÀMETROS EN LOCALBORADCAST INTENT
           	 	Intent localBroadcastIntent = new Intent("event-send-contact-to-bp");
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
        		
        		Toast.makeText(contexto, "Seleccione agregar para añadir un nuevo contacto", Toast.LENGTH_LONG).show();
        		
        	}
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
      
		}
		
	}

	public void agregarNuevoContacto(String nombre, String telefono){
		try{
			ContactoBean bean = new ContactoBean();
			bean.setIdCon(nombre);
			bean.setNomCon(nombre);
			bean.setTelMovCon(telefono);
			bean.setUtilId(SocioNegocioFragment.utilId);

			if(SocioNegocioFragment.listaDetalleContactos.size()== 0){
				bean.setPrincipal(true);
			}else
				bean.setPrincipal(false);

			SocioNegocioFragment.listaDetalleContactos.add(bean);
			SocioNegocioFragment.contactId++;
			SocioNegocioFragment.utilId++;

			listaDetalle = SocioNegocioFragment.listaDetalleContactos;
			dysplayListContacts();
		}catch (Exception e){
			Toast.makeText(getActivity(), "agregarNuevoContacto > " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
