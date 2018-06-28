package com.proyecto.facturas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.dao.FacturaDAO;
import com.proyecto.facturas.adapter.recyclerview.RVAdapterBuscarFactura;
import com.proyecto.facturas.adapter.recyclerview.listeners.IRVAdapterBuscarFactura;
import com.proyecto.facturas.util.FacturaBuscarBean;
import com.proyecto.incidencias.IncidenciaActivity;
import com.proyecto.incidencias.adapter.recyclerview.RVAdapterAddIncidencia;
import com.proyecto.utils.StringDateCast;

public class BuscarFacturaActivity extends AppCompatActivity implements IRVAdapterBuscarFactura{

    public static int REQUEST_CODE_BUSCAR_FACTURA = 80;
    public static String KEY_PARAM_FACTURA = "factura";
    public static String KEY_PARAM_CLIENTE = "cliente";

    private RecyclerView rvBuscarFacturas;
    private RVAdapterBuscarFactura mRVAdapterBuscarFactura;
    private FacturaBuscarBean mFacturaSelected;
    private String mCodigoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_factura);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbBuscarFactura);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlBuscarFacturas);

        rvBuscarFacturas = (RecyclerView) findViewById(R.id.rvBuscarFactura);
        rvBuscarFacturas.setLayoutManager(new LinearLayoutManager(BuscarFacturaActivity.this));
        rvBuscarFacturas.setHasFixedSize(true);

        mRVAdapterBuscarFactura = new RVAdapterBuscarFactura(BuscarFacturaActivity.this);
        rvBuscarFacturas.setAdapter(mRVAdapterBuscarFactura);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_CLIENTE)){
                mCodigoCliente = getIntent().getStringExtra(KEY_PARAM_CLIENTE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRVAdapterBuscarFactura.clearAndAddAll(new FacturaDAO().listarParaBusqueda(mCodigoCliente));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_facturas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_aceptar:
                if(mFacturaSelected != null) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_PARAM_FACTURA, mFacturaSelected);
                    setResult(RESULT_OK, intent);
                    finish();
                }else
                    Toast.makeText(BuscarFacturaActivity.this, "Debe seleccionar una factura", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(FacturaBuscarBean facturaBean) {
        mFacturaSelected = new FacturaBuscarBean();
        mFacturaSelected = facturaBean;
    }
}
