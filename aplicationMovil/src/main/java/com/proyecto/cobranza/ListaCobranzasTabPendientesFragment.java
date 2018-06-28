package com.proyecto.cobranza;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.PagoBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.CustomDialogPago;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgPAGO;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Utils;
import com.proyecto.utils.Variables;

import org.json.JSONException;
import org.json.JSONObject;

public class ListaCobranzasTabPendientesFragment extends Fragment 
				implements OnItemClickListener,OnItemLongClickListener, OnScrollListener{
	
	private ListView lvCobranzas;
	private ListViewCustomAdapterFourRowAndImgPAGO adapter;
	private ArrayList<PagoBean> listaAdapter;
	private PagoBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private SwipeRefreshLayout refreshLayout;
    private boolean mIsSearchResultView = false;
    
    private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
	private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
	private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;
	private static int icon_cloud_done_red = R.drawable.ic_cloud_done_red_36dp;
	private static int icon_cloud_done_green = R.drawable.ic_cloud_done_green_36dp;
	
	private SharedPreferences pref;
	private String movilAprobar = "";
	private String movilEditar = "";
	private String movilCrear = "";
	private String movilRechazar = "";
	private FloatingActionButton fabCrear;
	
	
 // RECIBE LOS PAR�METROS DESDE EL FRAGMENT CORRESPONDIENTE
  	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
  		@Override
  		public void onReceive(Context context, Intent intent) {

  			if(intent.getAction().equals("event-send-pago-bp-ok")){
				adapter.clearAndAddAll(builDataCobranzas());
  			}
  		}

  	};
  	
  	
  	
  	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.lista_cobranza_cliente_tab_pendientes_fragment, container, false);
        
        lvCobranzas = (ListView) v.findViewById(R.id.lvLstCobranzaTodos);
		adapter = new ListViewCustomAdapterFourRowAndImgPAGO(v.getContext());
        
        contexto = v.getContext();

     // registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
 		IntentFilter filter = new IntentFilter("event-send-pago-bp-ok");
 		LocalBroadcastManager.getInstance(contexto).registerReceiver(
 				myLocalBroadcastReceiver, filter);
        
 		lvCobranzas.setOnItemClickListener(this);

 		
		// Obtener el refreshLayout
		refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

		// Seteamos los colores que se usar�n a lo largo de la animaci�n
		refreshLayout.setColorSchemeResources(R.color.s1, R.color.s2,
				R.color.s3, R.color.s4);

		// Iniciar la tarea al revelar el indicador
		refreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						adapter.clearAndAddAll(builDataCobranzas());
						refreshLayout.setRefreshing(false);
					}
				});

		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		
		// FLOATING BUTTON
		fabCrear = (FloatingActionButton) v
				.findViewById(R.id.fab);
		fabCrear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent registrarCobranza = new Intent(v.getContext(),
						MainCobranzas.class);
				startActivity(registrarCobranza);
			}
		});
		fabCrear.setVisibility(View.INVISIBLE);
		// FLOATING BUTTON
 		
		lvCobranzas.setOnScrollListener(this);
		lvCobranzas.setOnItemLongClickListener(this);
		lvCobranzas.setAdapter(adapter);

        setHasOptionsMenu(true);
        return v;
        
    }

	@Override
	public void onStart() {
		super.onStart();
		adapter.clearAndAddAll(builDataCobranzas());
		comprobarPermisos();
	}

	private void comprobarPermisos(){

		String permisosMenu = pref.getString(Variables.MENU_COBRANZAS, null);
		if (permisosMenu != null) {
			try {
				JSONObject permisos = new JSONObject(permisosMenu);
				movilCrear = permisos.getString(Variables.MOVIL_CREAR);
				movilEditar = permisos.getString(Variables.MOVIL_EDITAR);
				movilAprobar = permisos.getString(Variables.MOVIL_APROBAR);
				movilRechazar = permisos.getString(Variables.MOVIL_RECHAZAR);

				if(movilCrear != null && !movilCrear.trim().equals("")){
					if (!movilCrear.equals("")) {
						if (movilCrear.equalsIgnoreCase("Y")) {
							fabCrear.setVisibility(FloatingActionButton.VISIBLE);
						}
					}
				}

			} catch (Exception e) {
				Toast.makeText(contexto, "Ocurrió un error intentando obtener los permisos del menú", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private ArrayList<PagoBean> builDataCobranzas(){
		
		listaAdapter = new ArrayList<PagoBean>();

		try{
			//TRAER TODO DE SQLITE
			DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
			SQLiteDatabase db = helper.getDataBase();
//
			Cursor rs= db.rawQuery("select P.Clave, SN.NombreRazonSocial, P.TipoPago, P.Tipo, P.FechaContable," +
					"P.Numero, P.EstadoMovil, P.ClaveMovil, P.TransaccionMovil  " +
					"from TB_PAGO P left JOIN TB_SOCIO_NEGOCIO SN " +
					"ON P.SocioNegocio = SN.Codigo " +
					"where Tipo = 'P' " +
					"ORDER BY P.Clave", null);
			while (rs.moveToNext()) {

				customListObjet = new PagoBean();
				customListObjet.setUtilIcon(icon);
				customListObjet.setClave(rs.getString(0));
				customListObjet.setNombreSocioNegocio(rs.getString(1));
				if(rs.getString(2).equals("F"))
					customListObjet.setTipoPago("Efectivo");
				else if(rs.getString(2).equals("T"))
					customListObjet.setTipoPago("Transferencia/Deposito");
				else if(rs.getString(2).equals("C"))
					customListObjet.setTipoPago("Cheque");
				customListObjet.setTipo(rs.getString(3));
				customListObjet.setFechaContable(StringDateCast.castStringtoDate(rs.getString(4)));
				customListObjet.setNumero(rs.getString(5));

				if (rs.getString(6).toString().equals("L")) {
					customListObjet.setUtilIcon2(icon_local);
				} else if (rs.getString(6).toString().equals("S")) {
					customListObjet.setUtilIcon2(icon_cloud_down);
				} else if(rs.getString(6).toString().equals("U") &&
						rs.getString(8).equalsIgnoreCase("1")){
					customListObjet.setUtilIcon2(icon_cloud_done);
				} else if(rs.getString(6).toString().equals("U") &&
						rs.getString(8).equalsIgnoreCase("2")){
					customListObjet.setUtilIcon2(icon_cloud_done);
				} else if(rs.getString(6).toString().equals("U") &&
						rs.getString(8).equalsIgnoreCase("3")){
					customListObjet.setUtilIcon2(icon_cloud_done_red);
				} else if(rs.getString(6).toString().equals("U") &&
						rs.getString(8).equalsIgnoreCase("4")){
					customListObjet.setUtilIcon2(icon_cloud_done_green);
				}

				customListObjet.setEstadoRegistroMovil(rs.getString(6));
				customListObjet.setClaveMovil(rs.getString(7));
				customListObjet.setTransaccionMovil(rs.getString(8));

				listaAdapter.add(customListObjet);

			}

			rs.close();
		}catch(Exception e){
			Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		return listaAdapter;

	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent myIntent = new Intent( contexto, DetalleCobranzaMain.class);
    	myIntent.putExtra("id", ((PagoBean) adapter.getItem(position)).getClave());
    	myIntent.putExtra("estado", ((PagoBean) adapter.getItem(position)).getEstadoRegistroMovil());
    	myIntent.putExtra("estadoTransaccion", ((PagoBean) adapter.getItem(position)).getTransaccionMovil());
    	startActivity(myIntent);
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.clear();
		inflater.inflate(R.menu.lst_sn_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        if (mIsSearchResultView) {
            searchItem.setVisible(false);
        }

        // In version 3.0 and later, sets up and configures the ActionBar SearchView
        if (Utils.hasHoneycomb()) {
            final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String queryText) {
                    
                    
                	return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    
                	if(adapter != null){
                		adapter.getFilter().filter(newText);
                	}
                	
                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
				
				@Override
				public boolean onClose() {
					if(adapter != null){
                		adapter.getFilter().filter("");
                	}
                    return true;
				}
			});



        }
        
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		boolean enable = false;
		if (lvCobranzas != null && lvCobranzas.getChildCount() > 0) {
			// check if the first item of the list is visible
			boolean firstItemVisible = lvCobranzas.getFirstVisiblePosition() == 0;
			// check if the top of the first item is visible
			boolean topOfFirstItemVisible = lvCobranzas.getChildAt(0).getTop() == 0;
			// enabling or disabling the refresh layout
			enable = firstItemVisible && topOfFirstItemVisible;
		}else if (lvCobranzas != null && totalItemCount == 0){
			enable = true;
		}

		refreshLayout.setEnabled(enable);
	
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		if(((PagoBean) adapter.getItem(position)).getTransaccionMovil().equals("1") ||
				((PagoBean) adapter.getItem(position)).getTransaccionMovil().equals("2")){
			boolean crear = false;
			boolean actualizar = false;
			boolean rechazar = false;
			boolean aprobar = false;
			
			if(movilCrear.equalsIgnoreCase("Y"))
				crear = true;
			if(movilEditar.equalsIgnoreCase("Y"))
				actualizar = true;
			if(movilRechazar.equalsIgnoreCase("Y"))
				rechazar = true;
			if(movilAprobar.equalsIgnoreCase("Y"))
				aprobar = true;
			
			if(!crear && !actualizar && !rechazar && !aprobar){
				
				Toast.makeText(contexto, "No tiene permisos para realizar acciones", Toast.LENGTH_SHORT).show();
				return false;
				
			}else{
				
				CustomDialogPago cd = new CustomDialogPago();
				Dialog groupDialog = cd.CreateGroupDialog(contexto,
											listaAdapter.get(position).getClave(),
											crear, actualizar, rechazar, aprobar);
				groupDialog.show();
				
			}
			
		}
		
		return true;
	}
    

}
