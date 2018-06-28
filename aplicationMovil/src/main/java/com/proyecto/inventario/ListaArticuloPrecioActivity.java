package com.proyecto.inventario;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.dao.ArticuloDAO;
import com.proyecto.inventario.adapter.recyclerview.RVAdapterListaArticulosPorPrecio;
import com.proyecto.inventario.adapter.recyclerview.listeners.IRVAdapterListaArticulosPorPrecio;

public class ListaArticuloPrecioActivity extends AppCompatActivity implements IRVAdapterListaArticulosPorPrecio{

    public static String KEY_PARAM_LISTA_PRECIO = "kpListaPrecio";

    private RecyclerView mRecycerView;
    private RVAdapterListaArticulosPorPrecio mAdapter;
    private SearchView mSearchView;

    private String mListaPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulo_precio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbListaArticulo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlBuscarCliente);

        mRecycerView = (RecyclerView) findViewById(R.id.rvListaArticulo);
        mRecycerView.setLayoutManager(new LinearLayoutManager(this));
        mRecycerView.setHasFixedSize(true);

        mAdapter = new RVAdapterListaArticulosPorPrecio(this);
        mRecycerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().getExtras() != null) {
            mListaPrecio = getIntent().getStringExtra(KEY_PARAM_LISTA_PRECIO);
            mAdapter.clearAndAddAll(new ArticuloDAO().listarPorPrecio(mListaPrecio));
            setTitle("Articulos por lista de precio");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lst_sn_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(mSearchListener);
        mSearchView.setOnCloseListener(mSearchCloseListener);
        mSearchView.setQueryHint("Codigo o descripcion...");
        return super.onCreateOptionsMenu(menu);
    }

    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText == null || newText.equals(""))
                mAdapter.clearAndAddAll(new ArticuloDAO().listarPorPrecio(mListaPrecio));
            else
                mAdapter.getFilter().filter(newText.toLowerCase());
            return false;
        }
    };

    private SearchView.OnCloseListener mSearchCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mAdapter.clearAndAddAll(new ArticuloDAO().listarPorPrecio(mListaPrecio));
            if (!mSearchView.isIconified())
                mSearchView.setIconified(true);
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(ArticuloBean articulo) {
        Intent myIntent = new Intent(ListaArticuloPrecioActivity.this, DetalleArticuloMain.class);
        myIntent.putExtra("id", articulo.getCod());
        startActivity(myIntent);
    }
}
