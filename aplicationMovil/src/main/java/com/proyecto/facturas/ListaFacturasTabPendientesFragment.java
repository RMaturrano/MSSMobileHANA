package com.proyecto.facturas;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.proyecto.bean.FacturaBean;
import com.proyecto.cobranza.DetalleFacturaMain;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgFACT_LIST;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Utils;

public class ListaFacturasTabPendientesFragment extends Fragment 
			implements OnItemClickListener, OnScrollListener{

	private ListView lvFact;
	private ListViewCustomAdapterFourRowAndImgFACT_LIST adapter;
	private ArrayList<FacturaBean> listaAdapter;
	private FacturaBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private boolean mIsSearchResultView = false;
    private SwipeRefreshLayout refreshLayout;
//    
//    private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
//	private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
//	private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.lista_factura_cliente_tab_pend_fragment, container, false);
        
        lvFact = (ListView) v.findViewById(R.id.lvLstFacturaCliTabPend);
        
        contexto = v.getContext();
        builDataOrd();
        
 		lvFact.setOnItemClickListener(this);

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
						builDataOrd();
						refreshLayout.setRefreshing(false);
					}
				});

		lvFact.setOnScrollListener(this);
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataOrd(){
		
		listaAdapter = new ArrayList<FacturaBean>();
		
		//TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery(
				"select T0.Clave, " +
						" T0.Referencia," +
						" T0.FechaContable," +
						" T0.Total, " +
						" T0.Saldo, " +
						" T0.SocioNegocio," +
						" T1.NombreRazonSocial "
						+ "from TB_FACTURA T0 JOIN TB_SOCIO_NEGOCIO T1 " +
						" ON T0.SocioNegocio = T1.Codigo ORDER BY T0.FechaContable desc" , null);
		while (rs.moveToNext()) {		
			
			customListObjet = new FacturaBean();
			customListObjet.setUtilIcon(icon);
			customListObjet.setClave(rs.getString(0));
			customListObjet.setReferencia(rs.getString(1));
			customListObjet.setFechaContable(StringDateCast.castStringtoDate(rs.getString(2)));
			customListObjet.setTotal(rs.getString(3));
			customListObjet.setSaldo(rs.getString(4));
			customListObjet.setSocioNegocio(rs.getString(rs.getColumnIndex("SocioNegocio")));
			customListObjet.setNombreSocio(rs.getString(rs.getColumnIndex("NombreRazonSocial")));
			listaAdapter.add(customListObjet);

		}
		
		if(listaAdapter.size()>0){
			adapter = new ListViewCustomAdapterFourRowAndImgFACT_LIST( contexto, listaAdapter);
        	lvFact.setAdapter(adapter);
		}
		
		rs.close();
//		db.close();
		
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
					adapter.getFilter().filter("");
                    return true;
				}
			});



        }
        
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent myIntent = new Intent(v.getContext(), DetalleFacturaMain.class);
    	//myIntent.putExtra("id", listaAdapter.get(position).getClave());
		myIntent.putExtra("id", ((FacturaBean)adapter.getItem(position)).getClave());
    	startActivity(myIntent);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		boolean enable = false;
		if (lvFact != null && lvFact.getChildCount() > 0) {
			// check if the first item of the list is visible
			boolean firstItemVisible = lvFact.getFirstVisiblePosition() == 0;
			// check if the top of the first item is visible
			boolean topOfFirstItemVisible = lvFact.getChildAt(0).getTop() == 0;
			// enabling or disabling the refresh layout
			enable = firstItemVisible && topOfFirstItemVisible;
		}
		refreshLayout.setEnabled(enable);
	
	}

	
	
}
