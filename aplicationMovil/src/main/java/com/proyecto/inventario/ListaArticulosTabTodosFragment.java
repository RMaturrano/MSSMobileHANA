package com.proyecto.inventario;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.GrupoArticuloBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.Select;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgART_LIST;
import com.proyecto.utils.Utils;

public class ListaArticulosTabTodosFragment extends Fragment 
						implements OnItemClickListener, OnScrollListener{
	
	private ListView lvArticulo;
	private ListViewCustomAdapterFourRowAndImgART_LIST adapter;
	private ArrayList<ArticuloBean> listaAdapter;
	private ArticuloBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private boolean mIsSearchResultView = false;
    private SwipeRefreshLayout refreshLayout;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.lista_articulos_tab_todos_fragment, container, false);

		contexto = v.getContext();
		adapter = new ListViewCustomAdapterFourRowAndImgART_LIST(contexto);

		lvArticulo = (ListView) v.findViewById(R.id.lvLstArticulosTabTodos);
		lvArticulo.setOnItemClickListener(this);
		lvArticulo.setAdapter(adapter);

		refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
		refreshLayout.setColorSchemeResources(R.color.s1, R.color.s2,
				R.color.s3, R.color.s4);
		refreshLayout.post(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setRefreshing(true);
			}
		});
		new fillDataInBackGround().execute();
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						new fillDataInBackGround().execute();
					}
				});

		lvArticulo.setOnScrollListener(this);
        setHasOptionsMenu(true);
        return v;
        
    }


	private void builDataArticulos(){

		listaAdapter = new ArrayList<ArticuloBean>();
		
		//TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
		Cursor rs= db.rawQuery(
				"select " +
				"A.Codigo, " +
				"A.Nombre," +
				"(select IFNULL(SUM(CAST(STOCK AS NUMERIC)),0) from TB_CANTIDAD where ARTICULO = A.Codigo), " +
				"G.NOMBRE "
						+ "from TB_ARTICULO A join TB_GRUPO_ARTICULO G " +
						"ON A.GrupoArticulo = G.CODIGO join TB_FABRICANTE F " +
						"ON A.Fabricante = F.CODIGO " +
						"order by A.Nombre" , null);

		while (rs.moveToNext()) {		
			
			customListObjet = new ArticuloBean();
			customListObjet.setUtilIcon(icon);
			customListObjet.setCod(rs.getString(0));
			customListObjet.setDesc(rs.getString(1));
			customListObjet.setStock(rs.getString(2));
			customListObjet.setGrupoArticulo(rs.getString(3));
			listaAdapter.add(customListObjet);

		}

	/*	if(listaAdapter.size()>0){
			lvArticulo.setVisibility(View.VISIBLE);
			adapter = new ListViewCustomAdapterFourRowAndImgART_LIST( contexto, listaAdapter);
        	lvArticulo.setAdapter(adapter);

		} */
		
		rs.close();
//		db.close();

		adapter.clearAndAddAll(listaAdapter);
		
	}
    
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.clear();
		inflater.inflate(R.menu.lst_articulo_menu, menu);
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
                    
                	adapter.getFilter().filter(newText);
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

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case R.id.action_filtro:
			
			buildAlertFiltrar(ordenSel,filtroSel);
			
			return true;
		case R.id.action_ordenar:
			
			buildAlertOrdenar(ordenSel,filtroSel);
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }			*/


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent myIntent = new Intent(v.getContext(), DetalleArticuloMain.class);
		ArticuloBean bean = (ArticuloBean) adapter.getItem(position);
    	myIntent.putExtra("id", bean.getCod());
    	startActivity(myIntent);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		boolean enable = false;
		if (lvArticulo != null && lvArticulo.getChildCount() > 0) {
			// check if the first item of the list is visible
			boolean firstItemVisible = lvArticulo.getFirstVisiblePosition() == 0;
			// check if the top of the first item is visible
			boolean topOfFirstItemVisible = lvArticulo.getChildAt(0).getTop() == 0;
			// enabling or disabling the refresh layout
			enable = firstItemVisible && topOfFirstItemVisible;
		}else if (lvArticulo != null && totalItemCount == 0){
			enable = true;
		}
		refreshLayout.setEnabled(enable);
	
	}

	;

	private class fillDataInBackGround extends AsyncTask<String,Void,Object>{

		@Override
		protected Object doInBackground(String... params) {

			listaAdapter = new ArrayList<ArticuloBean>();

			//TRAER TODO DE SQLITE
			DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
			SQLiteDatabase db = helper.getDataBase();

			Cursor rs= db.rawQuery(
					"select " +
							"A.Codigo, " +
							"A.Nombre," +
							"(select IFNULL(SUM(CAST(STOCK AS NUMERIC)),0) from TB_CANTIDAD where ARTICULO = A.Codigo), " +
							"G.NOMBRE "
							+ "from TB_ARTICULO A join TB_GRUPO_ARTICULO G " +
							"ON A.GrupoArticulo = G.CODIGO join TB_FABRICANTE F " +
							"ON A.Fabricante = F.CODIGO " +
							"order by A.Nombre" , null);

			while (rs.moveToNext()) {

				customListObjet = new ArticuloBean();
				customListObjet.setUtilIcon(icon);
				customListObjet.setCod(rs.getString(0));
				customListObjet.setDesc(rs.getString(1));
				customListObjet.setStock(rs.getString(2));
				customListObjet.setGrupoArticulo(rs.getString(3));
				listaAdapter.add(customListObjet);

			}

	/*	if(listaAdapter.size()>0){
			lvArticulo.setVisibility(View.VISIBLE);
			adapter = new ListViewCustomAdapterFourRowAndImgART_LIST( contexto, listaAdapter);
        	lvArticulo.setAdapter(adapter);

		} */

			rs.close();
//		db.close();

			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			adapter.clearAndAddAll(listaAdapter);
			refreshLayout.setRefreshing(false);
		}
	}
	
}
