package com.proyecto.sociosnegocio;

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
import com.proyecto.dao.ClienteDAO;
import com.proyecto.sociosnegocio.adapter.recyclerview.RVAdapterBuscarCliente;
import com.proyecto.sociosnegocio.adapter.recyclerview.listeners.IRVAdapterBuscarCliente;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;

public class ClienteBuscarActivity extends AppCompatActivity implements IRVAdapterBuscarCliente{

    public static String KEY_PARAM_CLIENTE = "cliente";
    public static int REQUEST_CODE_BUSCAR_CLIENTE = 100;

    private RecyclerView rvBuscarClientes;
    private RVAdapterBuscarCliente mRVAdapterBuscarCliente;
    private ClienteBuscarBean mClienteSelected;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_buscar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbBuscarCliente);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlBuscarCliente);

        rvBuscarClientes = (RecyclerView) findViewById(R.id.rvBuscarCliente);
        rvBuscarClientes.setLayoutManager(new LinearLayoutManager(ClienteBuscarActivity.this));
        rvBuscarClientes.setHasFixedSize(true);

        mRVAdapterBuscarCliente = new RVAdapterBuscarCliente(ClienteBuscarActivity.this);
        rvBuscarClientes.setAdapter(mRVAdapterBuscarCliente);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRVAdapterBuscarCliente.clearAndAddAll(new ClienteDAO().listarParaBusqueda());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busca_cliente, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(mSearchListener);
        mSearchView.setOnCloseListener(mSearchCloseListener);
        mSearchView.setQueryHint("Codigo o nombre...");
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
                mRVAdapterBuscarCliente.clearAndAddAll(new ClienteDAO().listarParaBusqueda());
            else
                mRVAdapterBuscarCliente.getFilter().filter(newText.toLowerCase());
            return false;
        }
    };

    private SearchView.OnCloseListener mSearchCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mRVAdapterBuscarCliente.clearAndAddAll(new ClienteDAO().listarParaBusqueda());
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
            case R.id.action_aceptar:
                if(mClienteSelected != null) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_PARAM_CLIENTE, mClienteSelected);
                    setResult(RESULT_OK, intent);
                    finish();
                }else
                    Toast.makeText(ClienteBuscarActivity.this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(ClienteBuscarBean cliente) {
        mClienteSelected = new ClienteBuscarBean();
        mClienteSelected = cliente;
    }
}
