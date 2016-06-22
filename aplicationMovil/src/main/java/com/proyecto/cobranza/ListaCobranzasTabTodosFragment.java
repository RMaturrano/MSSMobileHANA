package com.proyecto.cobranza;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.bean.PagoBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgPAGO;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Utils;

public class ListaCobranzasTabTodosFragment extends Fragment 
				implements OnItemClickListener, OnScrollListener{
	
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
	
 // RECIBE LOS PARÀMETROS DESDE EL FRAGMENT CORRESPONDIENTE
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
        v =  inflater.inflate(R.layout.lista_cobranza_cliente_tab_todos_fragment, container, false);
        
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

		// Seteamos los colores que se usarán a lo largo de la animación
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

 		
		lvCobranzas.setOnScrollListener(this);
		lvCobranzas.setAdapter(adapter);

        setHasOptionsMenu(true);
        return v;
        
    }

	@Override
	public void onStart() {
		super.onStart();
		adapter.clearAndAddAll(builDataCobranzas());
	}

	private ArrayList<PagoBean> builDataCobranzas(){
		
		listaAdapter = new ArrayList<PagoBean>();
		
		//TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		Cursor rs= db.rawQuery("select P.Clave, SN.NombreRazonSocial, P.TipoPago, P.Tipo, P.FechaContable," +
								"P.Numero, P.EstadoMovil, P.ClaveMovil " +
								"from TB_PAGO P left JOIN TB_SOCIO_NEGOCIO SN " +
								"ON P.SocioNegocio = SN.Codigo " +
								"where Tipo = 'A' " +
								"ORDER BY P.Clave", null);
		while (rs.moveToNext()) {		
			
			customListObjet = new PagoBean();
			customListObjet.setUtilIcon(icon);
			customListObjet.setClave(rs.getString(0));
			customListObjet.setNombreSocioNegocio(rs.getString(1));
			if(rs.getString(2).equals("F"))
				customListObjet.setTipoPago("Efectivo");
			else if(rs.getString(2).equals("T"))
				customListObjet.setTipoPago("Transferencia");
			else if(rs.getString(2).equals("C"))
				customListObjet.setTipoPago("Cheque");
			customListObjet.setTipo(rs.getString(3));
			customListObjet.setFechaContable(StringDateCast.castStringtoDate(rs.getString(4)));
			customListObjet.setNumero(rs.getString(5));
			
			if (rs.getString(6).toString().equals("L")) {
				customListObjet.setUtilIcon2(icon_local);
			} else if (rs.getString(6).toString().equals("S")) {
				customListObjet.setUtilIcon2(icon_cloud_down);
			} else
				customListObjet.setUtilIcon2(icon_cloud_done);
			customListObjet.setClaveMovil(rs.getString(7));
			
			listaAdapter.add(customListObjet);
			
		}

		rs.close();
		return listaAdapter;

	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent myIntent = new Intent( contexto, DetalleCobranzaMain.class);
    	myIntent.putExtra("id", ((PagoBean) adapter.getItem(position)).getClave());
    	startActivity(myIntent);
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
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
    

}
