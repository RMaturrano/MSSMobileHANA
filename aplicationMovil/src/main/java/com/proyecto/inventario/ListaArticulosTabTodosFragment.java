package com.proyecto.inventario;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.perf.metrics.AddTrace;
import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.GrupoArticuloBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.dao.AlmacenDAO;
import com.proyecto.dao.ArticuloDAO;
import com.proyecto.dao.ListaPrecioDAO;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.Select;
import com.proyecto.inventario.adapter.spinner.SPAdapterAlmacen;
import com.proyecto.inventario.adapter.spinner.SPAdapterListaPrecio;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgART_LIST;
import com.proyecto.utils.Utils;

public class ListaArticulosTabTodosFragment extends Fragment
        implements OnItemClickListener, OnScrollListener {

    private ListView lvArticulo;
    private SPAdapterListaPrecio mAdapterListaPrecio;
    private SPAdapterAlmacen mAdapterAlmacen;
    private Spinner spListaPrecio, spAlmacen;
    private ListaPrecioBean mListaPrecio;
    private AlmacenBean mAlmacen;
    private ListViewCustomAdapterFourRowAndImgART_LIST adapter;
    private List<ArticuloBean> listaAdapter;
    private ArticuloBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private boolean mIsSearchResultView = false;
    private SwipeRefreshLayout refreshLayout;

    private fillDataInBackGround loadInfo = new fillDataInBackGround();

    @Override
    @AddTrace(name = "onCreateViewListaArticulosTabTodosFragmentTrace", enabled = true)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.lista_articulos_tab_todos_fragment, container, false);

        contexto = v.getContext();
        adapter = new ListViewCustomAdapterFourRowAndImgART_LIST(contexto);

        lvArticulo = (ListView) v.findViewById(R.id.lvLstArticulosTabTodos);
        lvArticulo.setOnItemClickListener(this);
        lvArticulo.setAdapter(adapter);

        spListaPrecio = (Spinner) v.findViewById(R.id.spListaPrecio);
        mAdapterListaPrecio = new SPAdapterListaPrecio(v.getContext());
        List<ListaPrecioBean> listasPrecio = new ArrayList<>();
        ListaPrecioBean firstItem = new ListaPrecioBean();
        firstItem.setCodigo("-1");
        firstItem.setNombre("Lista de precio: Todos");
        listasPrecio.add(firstItem);
        listasPrecio.addAll(new ListaPrecioDAO().listar());
        mAdapterListaPrecio.addAll(listasPrecio);
        spListaPrecio.setAdapter(mAdapterListaPrecio);
        spListaPrecio.setVisibility(View.VISIBLE);
        spListaPrecio.setOnItemSelectedListener(spListaPrecioOnItemSelectedListener);

        spAlmacen = (Spinner) v.findViewById(R.id.spAlmacen);
        mAdapterAlmacen = new SPAdapterAlmacen(v.getContext());
        List<AlmacenBean> almacenes = new ArrayList<>();
        AlmacenBean frstItem = new AlmacenBean();
        frstItem.setCodigo("-1");
        frstItem.setDescripcion("Almacen: Todos");
        almacenes.add(frstItem);
        almacenes.addAll(new AlmacenDAO().listar());
        mAdapterAlmacen.addAll(almacenes);
        spAlmacen.setAdapter(mAdapterAlmacen);
        spAlmacen.setVisibility(View.VISIBLE);
        spAlmacen.setOnItemSelectedListener(spAlmacenOnItemSelectedListener);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.s1, R.color.s2,
                R.color.s3, R.color.s4);

        refreshLayout.setRefreshing(true);
        loadInfo = new fillDataInBackGround();
        loadInfo.execute();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadInfo.isCancelled()) {
                    loadInfo.cancel(true);
                }
                loadInfo = new fillDataInBackGround();
                loadInfo.execute();
            }
        });

        lvArticulo.setOnScrollListener(this);
        setHasOptionsMenu(true);
        return v;
    }


    private AdapterView.OnItemSelectedListener spListaPrecioOnItemSelectedListener = new
            AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mListaPrecio = (ListaPrecioBean) parent.getSelectedItem();
                    incializarListaArticulos();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    private AdapterView.OnItemSelectedListener spAlmacenOnItemSelectedListener = new
            AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mAlmacen = (AlmacenBean) parent.getSelectedItem();
                    incializarListaArticulos();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };


    private void incializarListaArticulos() {
        refreshLayout.setRefreshing(true);

        if (loadInfo.isCancelled()) {
            loadInfo.cancel(true);
        }
        if (loadInfo.getStatus() != AsyncTask.Status.RUNNING) {
            loadInfo = new fillDataInBackGround();
            loadInfo.execute();
        }

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
        } else if (lvArticulo != null && totalItemCount == 0) {
            enable = true;
        }
        refreshLayout.setEnabled(enable);

    }

    ;


//    private ProgressDialog progressDialog;

    private class fillDataInBackGround extends AsyncTask<String, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

          /*  progressDialog = new ProgressDialog(contexto);
            progressDialog.setMessage("Cargando...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected Object doInBackground(String... params) {
            listaAdapter = new ArticuloDAO().listar(mListaPrecio != null ? mListaPrecio.getCodigo() : null, mAlmacen != null ? mAlmacen.getCodigo() : null);
            //listaAdapter = new ArrayList<>();
            return null;
        }

        @Override
        @AddTrace(name = "onPostExecutefillDataInBackGroundTrace", enabled = true)
        protected void onPostExecute(Object o) {

            adapter.clearAndAddAll(listaAdapter);
            refreshLayout.setRefreshing(false);

//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
        }
    }

}
