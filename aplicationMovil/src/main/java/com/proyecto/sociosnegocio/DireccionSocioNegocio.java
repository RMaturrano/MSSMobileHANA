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

import com.proyect.movil.R;
import com.proyecto.bean.CalleBean;
import com.proyecto.bean.DepartamentoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.DistritoBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.TipoDireccionBean;
import com.proyecto.database.Select;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class DireccionSocioNegocio extends Fragment{
	
	private View v;
	private Context contexto;
	private int iconId = Variables.idIconRightBlue36dp;
	private TipoDireccionBean direccionSel = null;
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	
	private FormatCustomListView fullObject = null;
	private int posicion = 0;
	
	
	//Listas carga de sqlite
	private ArrayList<PaisBean> listaPais = null;
	private ArrayList<DepartamentoBean> listaDepartamentos = null;
	private ArrayList<ProvinciaBean> listaProvincias = null;
	private ArrayList<DistritoBean> listaDistritos = null;
	private ArrayList<CalleBean> listaCalles = null;
	
	private PaisBean paisSel = null;
	private DepartamentoBean departamentoSel = null;
	private ProvinciaBean provinciaSel = null;
	private DistritoBean distritoSel = null;
	private CalleBean calleSel = null;
	private String tipoRegistro = "Nuevo";
	
	//Si es una direccion a actualizar:
	private int utilId;
	
	//Método de recepción de mensaje
		private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals("custom-event-get-calle")){
					Bundle args = intent.getExtras();
					if(args != null){
						calleSel = new CalleBean();
						calleSel.setCodigo(args.getString("cod"));
						calleSel.setNombre(args.getString("name"));
						
						Object o = lvPrincipal.getItemAtPosition(6);
			    		fullObject = new FormatCustomListView();
			        	fullObject = (FormatCustomListView)o;
			        	fullObject.setData(calleSel.getNombre());
						searchResults.set(6, fullObject);
						lvPrincipal.invalidateViews();
					}
				}
			}
		};
	
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.socio_negocio_direccion, viewGroup, false);
	    
        getActivity().setTitle("Dirección de socio de negocio");
        
	    v = view;
        contexto = view.getContext();
        cargarListas();
        
        //Registrar los avisos
        IntentFilter filter = new IntentFilter("custom-event-get-calle");
        LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver, filter);
        
        if(MainSocioNegocio.idDireccion.equals("")){
        	tipoRegistro = "Nuevo";
        	llenarListaDireccion();
        	buildFirstAlert(0);
        }else{
        	tipoRegistro = "Actualizacion";
        	llenarListaDireccion();
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
	
	private void cargarListas(){
		
		listaPais = new ArrayList<PaisBean>();
		
		Select select = new Select(contexto);
		listaPais = select.listaPais();
		paisSel = listaPais.get(0);
		listaDepartamentos = select.listaDepartamentos(paisSel.getCodigo());
		for (DepartamentoBean departamento : listaDepartamentos) {
			if(departamento.getNombre().equalsIgnoreCase("Loreto")){
				departamentoSel = departamento;
				break;
			}
		}
		
		if(departamentoSel == null){
			departamentoSel = listaDepartamentos.get(0);
		}
		
		select.close();
		
	}
	
	private void cargarListaDepartamento(){
		
		listaPais = new ArrayList<PaisBean>();
		
		Select select = new Select(contexto);
		listaDepartamentos = select.listaDepartamentos(paisSel.getCodigo());
		select.close();
		
	}
	
	
	private void llenarListaDireccion() {
		
		searchResults = new ArrayList<FormatCustomListView>();

		lvPrincipal = (ListView) v.findViewById(R.id.lvDireccionSN);
		
		DireccionBean direccion = null;
		if(!MainSocioNegocio.idDireccion.equals("")){
			for (DireccionBean bean : SocioNegocioFragment.listaDirecciones) {
				if(bean.getIDDireccion().equals(MainSocioNegocio.idDireccion)){
					direccion = new DireccionBean();
					direccion = bean;
					utilId = direccion.getUtilId();
					break;
				}
			}
		}
		
		FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Tipo dirección");
		if(tipoRegistro.equalsIgnoreCase("Nuevo")){
			sr.setIcon(iconId);
		}
		if(direccion != null && direccion.getTipoDireccion().equals("B")){
			sr.setData("Fiscal");
		}else if(direccion != null && direccion.getTipoDireccion().equals("S")){
			sr.setData("Entrega");
		}
		searchResults.add(sr);
		
		sr = new FormatCustomListView();
	  	sr.setTitulo("Id dirección");
	  	if(direccion != null)
			sr.setData(direccion.getIDDireccion());
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("País");
	  	if(direccion != null){
	  		for (PaisBean pais : listaPais) {
				if(pais.getCodigo().equals(direccion.getPais())){
					paisSel = pais;
					sr.setData(paisSel.getNombre());
					cargarListaDepartamento();
					break;
				}
			}
	  	}
	  	else
	  		sr.setData(paisSel.getNombre());
	  	sr.setIcon(iconId);
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Departamento");
	  	sr.setIcon(iconId);
	  	if(direccion != null && direccion.getDepartamento() !=null){
	  		for (DepartamentoBean depa : listaDepartamentos) {
				if(depa.getCodigo().equals(direccion.getDepartamento())){
					departamentoSel = depa;
					sr.setData(depa.getNombre());
					break;
				}
			}
	  	}
	  	else
	  		sr.setData(departamentoSel.getNombre());
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Provincia");
	  	sr.setIcon(iconId);
	  	if(direccion != null && direccion.getProvincia()!=null){
	  		Select select = new Select(contexto);
    		listaProvincias = select.listaProvincias(departamentoSel.getCodigo());
    		select.close();
    		for (ProvinciaBean prov : listaProvincias) {
				if(prov.getCodigo().equals(direccion.getProvincia())){
					provinciaSel = prov;
					sr.setData(provinciaSel.getNombre());
					break;
				}
			}
	  	}
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Distrito");
	  	sr.setIcon(iconId);
	  	if(direccion != null && direccion.getDistrito() != null){
	  		Select select = new Select(contexto);
    		listaDistritos = select.listaDistritos(provinciaSel.getCodigo());
    		select.close();
    		for (DistritoBean dist : listaDistritos) {
    			if(dist.getCodigo().equals(direccion.getDistrito())){
    				distritoSel = dist;
    				sr.setData(distritoSel.getNombre());
    				break;
    			}
			}
	  	}
	  	searchResults.add(sr);
  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Calle");
	  	if(direccion != null && direccion.getCalle() != null){
	  		Select select = new Select(contexto);
    		listaCalles = select.listaCalles(distritoSel.getCodigo());
    		select.close();
    		for (CalleBean calle : listaCalles) {
				if(calle.getCodigo().equals(direccion.getCalle())){
					calleSel = calle;
					sr.setData(calleSel.getNombre());
					break;
				}
			}
	  	}
	  	sr.setIcon(iconId);
	  	searchResults.add(sr);
	  	
	  	sr = new FormatCustomListView();
	  	sr.setTitulo("Referencia");
	  	if(direccion != null && direccion.getReferencia() != null)
			sr.setData(direccion.getReferencia());
	  	sr.setIcon(iconId);
	  	searchResults.add(sr);
	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
      	lvPrincipal.setAdapter(adapter);
  		DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		
	}

	
	private void buildFirstAlert(int position){
		
		posicion = position;
		//Capturar el objeto (row - fila) 
		Object o = lvPrincipal.getItemAtPosition(position);
		fullObject = new FormatCustomListView();
    	fullObject = (FormatCustomListView)o;
    	//
		
		ArrayList<TipoDireccionBean> listaCombo = new ArrayList<TipoDireccionBean>();
		TipoDireccionBean m = new TipoDireccionBean();
		listaCombo = m.lista();
		
		//Spinner
		final Spinner spn = new Spinner(contexto);
		
		ArrayAdapter<TipoDireccionBean> adapter = new ArrayAdapter<TipoDireccionBean>(contexto, 
				android.R.layout.simple_list_item_1,
				listaCombo);
		spn.setAdapter(adapter);
		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View arg1, int pos, long arg3) {
			
				direccionSel = new TipoDireccionBean();
				direccionSel = (TipoDireccionBean) parent.getItemAtPosition(pos);
				
			}

			@Override
			public void onNothingSelected(
					AdapterView<?> arg0) {
				
			}
		});
		
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Tipo de dirección");

		alert.setView(spn);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
			FormatCustomListView fullObject_1 = new FormatCustomListView();
			fullObject_1 = (FormatCustomListView) lvPrincipal.getItemAtPosition(1);
			if(direccionSel.getCodigo().equalsIgnoreCase("B"))
				fullObject_1.setData(direccionSel.getDescripcion() + SocioNegocioFragment.directionIdFiscal);
			else
				fullObject_1.setData(direccionSel.getDescripcion() + SocioNegocioFragment.directionIdEntrega);
			searchResults.set(posicion, fullObject_1);

			fullObject.setData(direccionSel.getDescripcion());
			searchResults.set(posicion, fullObject);
			lvPrincipal.invalidateViews();
			
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
		
	}
	
	//CONSTRUYENDO EL ALERT DE CADA LINEA PARA EL BLOQUE DE DIRECCIONES
	private void construirAlert(int position){
			
			if(position == 0){
				
				if(tipoRegistro.equalsIgnoreCase("Nuevo"))
					buildFirstAlert(position);
				
			}else if(position== 1){
				
			}else if(position== 2){
				
				if(direccionSel != null){
					posicion = position;
					//Capturar el objeto (row - fila) 
					Object o = lvPrincipal.getItemAtPosition(position);
		    		fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView)o;
		        	//
					
		    		//Spinner
		    		final Spinner spn = new Spinner(contexto);
		    		
		    		ArrayAdapter<PaisBean> adapter = new ArrayAdapter<PaisBean>(contexto, 
							android.R.layout.simple_list_item_1,
							listaPais);
		    		spn.setAdapter(adapter);
		    		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
						
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							paisSel = new PaisBean();
							paisSel = (PaisBean) parent.getItemAtPosition(pos);
						}

						@Override
						public void onNothingSelected(
								AdapterView<?> arg0) {
							
						}
					});
					
					// TIPO DE DIRECCIÓN: B FISCAL, S ENTREGA
					
					AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		    		alert.setTitle("País");

		    		alert.setView(spn);
		    		
		    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {

		    			fullObject.setData(paisSel.getNombre());
						searchResults.set(posicion, fullObject);
						lvPrincipal.invalidateViews();
		    			
		    		  }
		    		});

		    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    		  public void onClick(DialogInterface dialog, int whichButton) {
		    		    // Canceled.
		    		  }
		    		});

		    		alert.show();
				}
				
			}else if(position== 3){
				
				if(paisSel != null){
					posicion = position;
					//Capturar el objeto (row - fila) 
					Object o = lvPrincipal.getItemAtPosition(position);
		    		fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView)o;
		        	//
					
		    		//Spinner
		    		final Spinner spn = new Spinner(contexto);
		    		
		    		ArrayAdapter<DepartamentoBean> adapter = new ArrayAdapter<DepartamentoBean>(contexto, 
							android.R.layout.simple_list_item_1,
							listaDepartamentos);
		    		spn.setAdapter(adapter);
		    		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
						
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							
							departamentoSel = new DepartamentoBean();
							departamentoSel = (DepartamentoBean) parent.getItemAtPosition(pos);
							
						}

						@Override
						public void onNothingSelected(
								AdapterView<?> arg0) {
							
						}
					});
					
					
					
					AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		    		alert.setTitle("Departamento");

		    		alert.setView(spn);
		    		
		    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {

		    			if(provinciaSel != null){
		    				FormatCustomListView fullObject_1 = new FormatCustomListView();
		    				fullObject_1 = (FormatCustomListView) lvPrincipal.getItemAtPosition(4);
		    				fullObject_1.setData("");
		    				searchResults.set(4, fullObject_1);
		    				listaProvincias.clear();
		    				provinciaSel = null;
		    				
		    				if(distritoSel != null){
		    					FormatCustomListView fullObject_2 = new FormatCustomListView();
		    					fullObject_2 = (FormatCustomListView) lvPrincipal.getItemAtPosition(5);
		    					fullObject_2.setData("");
			    				searchResults.set(5, fullObject_2);
			    				listaDistritos.clear();
			    				distritoSel = null;
		    				}
		    				
		    				if(calleSel != null){
		    					FormatCustomListView fullObject_3 = new FormatCustomListView();
		    					fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
		    					fullObject_3.setData("");
			    				searchResults.set(6, fullObject_3);
			    				calleSel = null;
		    				}
		    				
		    			}
		    			
		    			if(departamentoSel != null){
		    				fullObject.setData(departamentoSel.getNombre());
							searchResults.set(posicion, fullObject);
							lvPrincipal.invalidateViews();
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
				
			}else if(position== 4){
				
				if(paisSel != null){
					posicion = position;
					//Capturar el objeto (row - fila) 
					Object o = lvPrincipal.getItemAtPosition(position);
		    		fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView)o;
		        	//
					
		    		//Spinner
		    		final Spinner spn = new Spinner(contexto);
		    		Select select = new Select(contexto);
		    		listaProvincias = select.listaProvincias(departamentoSel.getCodigo());
		    		select.close();
		    		
		    		ArrayAdapter<ProvinciaBean> adapter = new ArrayAdapter<ProvinciaBean>(contexto, 
							android.R.layout.simple_list_item_1,
							listaProvincias);
		    		spn.setAdapter(adapter);
		    		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
						
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							
							provinciaSel = new ProvinciaBean();
							provinciaSel = (ProvinciaBean) parent.getItemAtPosition(pos);
							
						}

						@Override
						public void onNothingSelected(
								AdapterView<?> arg0) {
							
						}
					});
					
					
					
					AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		    		alert.setTitle("Provincia");

		    		alert.setView(spn);
		    		
		    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {
		    	
	    				if(distritoSel != null){
	    					FormatCustomListView fullObject_2 = new FormatCustomListView();
	    					fullObject_2 = (FormatCustomListView) lvPrincipal.getItemAtPosition(5);
	    					fullObject_2.setData("");
		    				searchResults.set(5, fullObject_2);
		    				listaDistritos.clear();
		    				distritoSel = null;
		    				
		    				if(calleSel != null){
		    					FormatCustomListView fullObject_3 = new FormatCustomListView();
		    					fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
		    					fullObject_3.setData("");
			    				searchResults.set(6, fullObject_3);
			    				calleSel = null;
		    				}
		    				
	    				}
		    				
		    			if(provinciaSel != null){
		    				fullObject.setData(provinciaSel.getNombre());
							searchResults.set(posicion, fullObject);
							lvPrincipal.invalidateViews();
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
				
			}else if(position== 5){
				
				if(provinciaSel != null){
					posicion = position;
					//Capturar el objeto (row - fila) 
					Object o = lvPrincipal.getItemAtPosition(position);
		    		fullObject = new FormatCustomListView();
		        	fullObject = (FormatCustomListView)o;
		        	//
					
		    		//Spinner
		    		final Spinner spn = new Spinner(contexto);
		    		Select select = new Select(contexto);
		    		listaDistritos = select.listaDistritos(provinciaSel.getCodigo());
		    		select.close();
		    		
		    		ArrayAdapter<DistritoBean> adapter = new ArrayAdapter<DistritoBean>(contexto, 
							android.R.layout.simple_list_item_1,
							listaDistritos);
		    		spn.setAdapter(adapter);
		    		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
						
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long arg3) {
							
							if(calleSel != null){
		    					FormatCustomListView fullObject_3 = new FormatCustomListView();
		    					fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
		    					fullObject_3.setData("");
			    				searchResults.set(6, fullObject_3);
			    				calleSel = null;
		    				}
							distritoSel = new DistritoBean();
							distritoSel = (DistritoBean) parent.getItemAtPosition(pos);
							
						}

						@Override
						public void onNothingSelected(
								AdapterView<?> arg0) {
							
						}
					});
					
					
					
					AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		    		alert.setTitle("Distrito");

		    		alert.setView(spn);
		    		
		    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {

		    			if(calleSel != null){
	    					FormatCustomListView fullObject_3 = new FormatCustomListView();
	    					fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
	    					fullObject_3.setData("");
		    				searchResults.set(6, fullObject_3);
	    				}
		    			
		    			if(distritoSel != null){
		    				fullObject.setData(distritoSel.getNombre());
							searchResults.set(posicion, fullObject);
							lvPrincipal.invalidateViews();
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
				
			}else if(position== 6){
				
				if(distritoSel != null){
					FragmentManager manager = getFragmentManager();
			    	FragmentTransaction transaction = manager.beginTransaction();
					Fragment fragment = new BuscarCalleFragment();
					MainSocioNegocio.idDistrito = distritoSel.getCodigo();
		            transaction.hide(this);
		            transaction.add(R.id.box, fragment);
		            transaction.addToBackStack(null);
		            transaction.commit();
				}
				
			}else if(position == 7){
				
				ConstruirAlert alert = new ConstruirAlert();
				alert.construirAlert(contexto, position, "Referencia", searchResults, lvPrincipal, "text");
				
			}
			
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
	        	
	            if(SocioNegocioFragment.directionIdFiscal >1)
	            	SocioNegocioFragment.directionIdFiscal --;
	            
	            if(SocioNegocioFragment.directionIdEntrega > 1)
	            	SocioNegocioFragment.directionIdEntrega --;
	            
	            MainSocioNegocio.idDireccion = "";
	            getActivity().setTitle("Direcciones (todas)");
	        	getFragmentManager().popBackStack();
        		
        		return true;
			
	        case R.id.action_aceptar:
	        	
	        	if(searchResults.get(0).getData() != "" && searchResults.get(0).getData() != null &&
	        			searchResults.get(1).getData() != "" && searchResults.get(1).getData() != null){
	        		
	        		
	        			Bundle arguments = new Bundle();
			        	String cabe = searchResults.get(1).getData();
			        	String desc = searchResults.get(0).getData();
			        	
			    		arguments.putString("cabe", cabe);
			    	    arguments.putString("desc", desc);
			    	    
			    	    
			    	    //MANDAR POR DEBAJO HACIA SOOCIONEGOCIOFRAGMENT EL BEAN PARA LLENAR LA LISTA PRINCIPAL
			    	    DireccionBean bean = new DireccionBean();
			    	    bean.setIDDireccion(searchResults.get(1).getData());
			    	    
			    	    if(paisSel != null)
			    	    	bean.setPais(paisSel.getCodigo());
			    	    if(departamentoSel != null)
			    	    	bean.setDepartamento(departamentoSel.getCodigo());
			    	    if(provinciaSel != null)
			    	    	bean.setProvincia(provinciaSel.getCodigo());
			    	    if(distritoSel != null)
			    	    	bean.setDistrito(distritoSel.getCodigo());
			    	    if(calleSel != null){
			    	    	bean.setCalle(calleSel.getCodigo());
			    	    	bean.setNombreCalle(calleSel.getNombre());
			    	    }
			    	    bean.setReferencia(searchResults.get(7).getData());
			    	    
			    	    if(SocioNegocioFragment.listaDirecciones.size()== 0){
			    	    	bean.setPrincipal(true);
			    	    }else
			    	    	bean.setPrincipal(false);
			    	    
			    	    if(tipoRegistro.equalsIgnoreCase("Nuevo")){
				    	    
				    	    bean.setTipoDireccion(direccionSel.getCodigo());
				    	    bean.setUtilId(SocioNegocioFragment.utilId2);
				    	    
				    	    SocioNegocioFragment.listaDirecciones.add(bean);
				    	    SocioNegocioFragment.utilId2++;
				    	    
				    	    if(direccionSel.getCodigo().equalsIgnoreCase("B"))
				    	    	SocioNegocioFragment.directionIdFiscal++;
				    	    else
				    	    	SocioNegocioFragment.directionIdEntrega++;
			    	    
			    	    }else if(tipoRegistro.equalsIgnoreCase("Actualizacion")){
		        			
			    	    	if(searchResults.get(0).getData().equalsIgnoreCase("Entrega"))
			    	    		bean.setTipoDireccion("S");
			    	    	else
			    	    		bean.setTipoDireccion("B");
			    	    	bean.setUtilId(utilId);
			    	    	
		        			for (int i = 0; i < SocioNegocioFragment.listaDirecciones.size(); i++) {
		        				if(SocioNegocioFragment.
		        						listaDirecciones.
		        						get(i).
		        						getIDDireccion().
		        						equals(bean.getIDDireccion())){
		        					SocioNegocioFragment.listaDirecciones.remove(i);
		        					SocioNegocioFragment.listaDirecciones.add(i, bean);
		        				}else
		        					i++;
							}
		        			
		        		}
			        	
			        	//MANDAR LOS PARÀMETROS EN LOCALBROADCAST INTENT
			        	 Intent localBroadcastIntent = new Intent("event-send-direction-to-list");
			             localBroadcastIntent.putExtras(arguments);
			             LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
			             myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);
			             

		            transaction.remove(this);
		            transaction.commit();
		        	
		            MainSocioNegocio.idDireccion = "";
		            getActivity().setTitle("Direcciones (todas)");
		        	getFragmentManager().popBackStack();
		    	    
	        	}else{
	        		
	        		Toast.makeText(contexto, "Seleccione el tipo de dirección", Toast.LENGTH_LONG).show();
	        		
	        	}
	        	
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	      
			}
			
	}

}
