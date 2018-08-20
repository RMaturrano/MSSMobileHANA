package com.proyecto.sociosnegocio;

import java.util.ArrayList;
import java.util.List;

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

import com.google.android.gms.maps.model.LatLng;
import com.proyect.movil.R;
import com.proyecto.bean.CalleBean;
import com.proyecto.bean.CanalBean;
import com.proyecto.bean.DepartamentoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.DistritoBean;
import com.proyecto.bean.GiroBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.TipoDireccionBean;
import com.proyecto.database.Select;
import com.proyecto.geolocalizacion.MapsActivity;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class DireccionSocioNegocio extends Fragment{

	public static final String TAG_AGREGAR_DIRECCION = "frgmnt_agregar_direccion";

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
	private List<CanalBean> listaCanales = null;
	private List<GiroBean> listaGiros = null;
	
	private PaisBean paisSel = null;
	private DepartamentoBean departamentoSel = null;
	private ProvinciaBean provinciaSel = null;
	private DistritoBean distritoSel = null;
	private CalleBean calleSel = null;
	private String tipoRegistro = "Nuevo";
	private LatLng mUbicacion = null;
	private CanalBean canalSel = null;
	private GiroBean giroSel = null;
	
	//Si es una direccion a actualizar:
	private int utilId;
	
	//region Método de recepción de mensaje
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
	//endregion

	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.socio_negocio_direccion, viewGroup, false);
	    
        getActivity().setTitle("Dirección de socio de negocio");
        
	    v = view;
        contexto = view.getContext();
        cargarListas();

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

	@Override
	public void onResume() {
		super.onResume();
		try{
			//Registrar los avisos
			IntentFilter filter = new IntentFilter("custom-event-get-calle");
			LocalBroadcastManager.getInstance(contexto).registerReceiver(myLocalBroadcastReceiver, filter);
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

	private void cargarListas(){

		try{
			listaPais = new ArrayList<PaisBean>();

			Select select = new Select(contexto);
			listaPais = select.listaPais();
			if(listaPais != null && listaPais.size() > 0)
				paisSel = listaPais.get(0);

			if(paisSel != null) {
				listaDepartamentos = select.listaDepartamentos(paisSel.getCodigo());
				for (DepartamentoBean departamento : listaDepartamentos) {
					if (departamento.getNombre().equalsIgnoreCase("Loreto")) {
						departamentoSel = departamento;
						break;
					}
				}
			}

			if(departamentoSel == null && listaDepartamentos != null && listaDepartamentos.size() > 0){
				departamentoSel = listaDepartamentos.get(0);
			}

			listaCanales = select.listaCanales();
			listaGiros = select.listaGiro();

			select.close();
		}catch (Exception e){
			showMessage("cargarListas() > " + e.getMessage());
		}
	}
	
	private void cargarListaDepartamento(){
		try{
			listaPais = new ArrayList<PaisBean>();

			Select select = new Select(contexto);
			listaDepartamentos = select.listaDepartamentos(paisSel.getCodigo());
			select.close();
		}catch (Exception e){
			showMessage("cargarListaDepartamento() > " + e.getMessage());
		}
	}

	private void llenarListaDireccion() {

		try{
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
			else if(paisSel != null)
				sr.setData(paisSel.getNombre());
			sr.setIcon(iconId);
			sr.setId(20);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setId(21);
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
			else if(departamentoSel != null)
				sr.setData(departamentoSel.getNombre());
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Provincia");
			sr.setId(22);
			sr.setIcon(iconId);
			if(direccion != null && direccion.getProvincia()!=null){
				Select select = new Select(contexto);
				listaProvincias = select.listaProvincias(departamentoSel.getCodigo());

				if(listaProvincias != null && listaProvincias.size() > 0) {
					for (ProvinciaBean prov : listaProvincias) {
						if (prov.getCodigo().equals(direccion.getProvincia())) {
							provinciaSel = prov;
							sr.setData(provinciaSel.getNombre());
							break;
						}
					}
				}else
					sr.setData(direccion.getProvincia());
			}
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Distrito");
			sr.setId(23);
			sr.setIcon(iconId);
			if(direccion != null && direccion.getDistrito() != null){
				if(provinciaSel != null) {
					Select select = new Select(contexto);
					listaDistritos = select.listaDistritos(provinciaSel.getCodigo());
					select.close();
					for (DistritoBean dist : listaDistritos) {
						if (dist.getCodigo().equals(direccion.getDistrito())) {
							distritoSel = dist;
							sr.setData(distritoSel.getNombre());
							break;
						}
					}
				}else
					sr.setData(direccion.getDistrito());
			}
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Calle");
			sr.setId(24);
			if(direccion != null && direccion.getCalle() != null){
				/*Select select = new Select(contexto);
				listaCalles = select.listaCalles(distritoSel.getCodigo());
				select.close();
				for (CalleBean calle : listaCalles) {
					if(calle.getCodigo().equals(direccion.getCalle())){
						calleSel = calle;
						sr.setData(calleSel.getNombre());
						break;
					}
				} */
				sr.setData(direccion.getCalle());
			}
			sr.setIcon(iconId);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Referencia");
			sr.setId(25);
			if(direccion != null && direccion.getReferencia() != null)
				sr.setData(direccion.getReferencia());
			sr.setIcon(iconId);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Latitud");
			sr.setId(26);
			if(direccion != null && direccion.getLatitud() != null)
				sr.setData(direccion.getLatitud());
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Longitud");
			if(direccion != null && direccion.getLongitud() != null)
				sr.setData(direccion.getLongitud());
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Ruta");
			sr.setId(27);
			if(direccion != null && direccion.getRuta() != null)
				sr.setData(direccion.getRuta());
			sr.setIcon(iconId);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Zona");
			sr.setId(28);
			if(direccion != null && direccion.getZona() != null)
				sr.setData(direccion.getZona());
			sr.setIcon(iconId);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Canal");
			sr.setId(29);
			if(direccion != null && direccion.getCanal() != null &&
					!direccion.getCanal().equals("")) {
				for (CanalBean c: listaCanales) {
					if(c.getCodigo().equals(direccion.getCanal())){
						canalSel = c;
						sr.setData(canalSel.getDescripcion());
						break;
					}
				}
			}
			sr.setIcon(iconId);
			searchResults.add(sr);

			sr = new FormatCustomListView();
			sr.setTitulo("Giro");
			sr.setId(30);
			if(direccion != null && direccion.getGiro() != null &&
					!direccion.getGiro().equals("")) {
				for (GiroBean g: listaGiros) {
					if(g.getCodigo().equals(direccion.getGiro())){
						giroSel = g;
						sr.setData(giroSel.getDescripcion());
						break;
					}
				}
			}
			sr.setIcon(iconId);
			searchResults.add(sr);

			adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
			lvPrincipal.setAdapter(adapter);
			DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);
		}catch (Exception e){
			showMessage("llenarListaDireccion() > " + e.getMessage() + " > Linea: " +
			e.getStackTrace()[0].getLineNumber());
		}
	}

	private void buildFirstAlert(int position){

		try{
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

		}catch (Exception e){
			showMessage("buildFirstAlert() > " + e.getMessage());
		}
	}
	
	//CONSTRUYENDO EL ALERT DE CADA LINEA PARA EL BLOQUE DE DIRECCIONES
	private void construirAlert(int position){

		try{
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
		    		if(listaProvincias != null && listaProvincias.size() > 0) {

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

								if (distritoSel != null) {
									FormatCustomListView fullObject_2 = new FormatCustomListView();
									fullObject_2 = (FormatCustomListView) lvPrincipal.getItemAtPosition(5);
									fullObject_2.setData("");
									searchResults.set(5, fullObject_2);
									listaDistritos.clear();
									distritoSel = null;

									if (calleSel != null) {
										FormatCustomListView fullObject_3 = new FormatCustomListView();
										fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
										fullObject_3.setData("");
										searchResults.set(6, fullObject_3);
										calleSel = null;
									}

								}

								if (provinciaSel != null) {
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
					}else{
						ConstruirAlert alert = new ConstruirAlert();
						alert.construirAlert(contexto, position, "Provincia", searchResults, lvPrincipal, "text",100);
					}
				}else{
					ConstruirAlert alert = new ConstruirAlert();
					alert.construirAlert(contexto, position, "Provincia", searchResults, lvPrincipal, "text",100);
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

		    		if(listaDistritos != null && listaDistritos.size() > 0) {

						ArrayAdapter<DistritoBean> adapter = new ArrayAdapter<DistritoBean>(contexto,
								android.R.layout.simple_list_item_1,
								listaDistritos);
						spn.setAdapter(adapter);
						spn.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
													   View arg1, int pos, long arg3) {

								if (calleSel != null) {
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

								if (calleSel != null) {
									FormatCustomListView fullObject_3 = new FormatCustomListView();
									fullObject_3 = (FormatCustomListView) lvPrincipal.getItemAtPosition(6);
									fullObject_3.setData("");
									searchResults.set(6, fullObject_3);
								}

								if (distritoSel != null) {
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
					}else{
						ConstruirAlert alert = new ConstruirAlert();
						alert.construirAlert(contexto, position, "Distrito", searchResults, lvPrincipal, "text",100);
					}
				}else{
					ConstruirAlert alert = new ConstruirAlert();
					alert.construirAlert(contexto, position, "Distrito", searchResults, lvPrincipal, "text",100);
				}
				
			}else if(position== 6){
				
			/*	if(distritoSel != null){
					FragmentManager manager = getFragmentManager();
			    	FragmentTransaction transaction = manager.beginTransaction();
					Fragment fragment = new BuscarCalleFragment();
					MainSocioNegocio.idDistrito = distritoSel.getCodigo();
		            transaction.hide(this);
		            transaction.add(R.id.box, fragment);
		            transaction.addToBackStack(null);
		            transaction.commit();
				}  */

				ConstruirAlert alert = new ConstruirAlert();
				alert.construirAlert(contexto, position, "Calle", searchResults, lvPrincipal, "text",100);
				
			}else if(position == 7){
				
				ConstruirAlert alert = new ConstruirAlert();
				alert.construirAlert(contexto, position, "Referencia", searchResults, lvPrincipal, "text",100);
			}else if(position == 10){

				ConstruirAlert alert = new ConstruirAlert();
				alert.construirAlert(contexto, position, "Ruta", searchResults, lvPrincipal, "text",100);
			}else if(position == 11){

				ConstruirAlert alert = new ConstruirAlert();
				alert.construirAlert(contexto, position, "Zona", searchResults, lvPrincipal, "text",100);
			}else if(position == 12){
				//Canales
				posicion = position;
				Object o = lvPrincipal.getItemAtPosition(position);
				fullObject = new FormatCustomListView();
				fullObject = (FormatCustomListView) o;

				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.select_dialog_singlechoice);

				for (CanalBean p: listaCanales) {
					arrayAdapter.add(p.getCodigo() + " - " + p.getDescripcion());
				}

				final AlertDialog dialog;
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Canal");
				alert.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						canalSel = listaCanales.get(which);
						fullObject.setData(canalSel.getDescripcion());
						searchResults.set(posicion, fullObject);
						lvPrincipal.invalidateViews();
						dialog.dismiss();
					}
				});
				dialog = alert.show();
			}else if(position == 13){
				//Giros
				posicion = position;
				Object o = lvPrincipal.getItemAtPosition(position);
				fullObject = new FormatCustomListView();
				fullObject = (FormatCustomListView) o;

				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.select_dialog_singlechoice);

				for (GiroBean p: listaGiros) {
					arrayAdapter.add(p.getCodigo() + " - " + p.getDescripcion());
				}

				final AlertDialog dialog;
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						contexto);
				alert.setTitle("Giro");
				alert.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						giroSel = listaGiros.get(which);
						fullObject.setData(giroSel.getDescripcion());
						searchResults.set(posicion, fullObject);
						lvPrincipal.invalidateViews();
						dialog.dismiss();
					}
				});
				dialog = alert.show();
			}
		}catch (Exception e){
			showMessage("construirAlert() > " + e.getMessage());
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    	inflater.inflate(R.menu.menu_socio_agregar_direccion, menu);
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
			    	    else
			    	    	bean.setProvincia(searchResults.get(4).getData());
			    	    if(distritoSel != null)
			    	    	bean.setDistrito(distritoSel.getCodigo());
			    	    else
			    	    	bean.setDistrito(searchResults.get(5).getData());
			    	    /*if(calleSel != null){
			    	    	bean.setCalle(calleSel.getCodigo());
			    	    	bean.setNombreCalle(calleSel.getNombre());
			    	    } */
						if(mUbicacion != null){
			    	    	bean.setLatitud(String.valueOf(mUbicacion.latitude));
			    	    	bean.setLongitud(String.valueOf(mUbicacion.longitude));
						}

						bean.setCalle(searchResults.get(6).getData());
			    	    bean.setReferencia(searchResults.get(7).getData());
					bean.setLatitud(searchResults.get(8).getData());
					bean.setLongitud(searchResults.get(9).getData());
					bean.setRuta(searchResults.get(10).getData());
					bean.setZona(searchResults.get(11).getData());

					if(canalSel != null)
						bean.setCanal(canalSel.getCodigo());

					if(giroSel != null)
						bean.setGiro(giroSel.getCodigo());

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
			case R.id.action_addlocation:
				Intent location = new Intent(getActivity().getBaseContext(), MapsActivity.class);
				if(mUbicacion != null){
					location.putExtra(MapsActivity.KEY_PARAM_LATITUD, mUbicacion.latitude);
					location.putExtra(MapsActivity.KEY_PARAM_LONGITUD, mUbicacion.longitude);
				}
				getActivity().startActivityForResult(location, MapsActivity.REQUEST_MAPAS);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	      
			}
			
	}

	public void actualizarUbicacion(double latitud, double longitud){
		try{
			mUbicacion = new LatLng(latitud, longitud);

			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(8);
			fullObject.setData(String.valueOf(latitud));
			searchResults.set(8, fullObject);

			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(9);
			fullObject.setData(String.valueOf(longitud));
			searchResults.set(9, fullObject);
			lvPrincipal.invalidateViews();

			showMessage("Ubicación actualizada...");
		}catch(Exception e){
			showMessage("actualizarUbicacion() > " + e.getMessage());
		}
	}

	private void showMessage(String message){
		if(message != null)
			Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
