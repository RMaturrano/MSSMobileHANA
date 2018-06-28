package com.proyecto.notacredito;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.facturas.adapter.recyclerview.RVAdapterListFactura;
import com.proyecto.facturas.adapter.recyclerview.listeners.IRVAdapterListFactura;

import java.util.ArrayList;
import java.util.List;

public class NotaCreditoListaFacturaActivity extends AppCompatActivity implements IRVAdapterListFactura{

    public static int REQUEST_CODE_LISTA_FACTURAS = 666;
    public static String KEY_PARAM_LISTA_FACTURAS = "kpFacturas";
    public static String KEY_PARAM_FACTURA_SELECTED = "kpFacturaSel";

    private RVAdapterListFactura mRVAdapterListFactura;
    private RecyclerView mRVListaFacturas;
    private FacturaBean mFacturaSeleccionada;
    private List<FacturaBean> mListFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_credito_lista_factura);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbNotListaFactura);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Seleccione entrega");

        mRVListaFacturas = (RecyclerView) findViewById(R.id.rvNotListaFactura);
        mRVListaFacturas.setLayoutManager(new LinearLayoutManager(this));
        mRVListaFacturas.setHasFixedSize(true);

        mRVAdapterListFactura = new RVAdapterListFactura(this);
        mRVListaFacturas.setAdapter(mRVAdapterListFactura);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_LISTA_FACTURAS)){
                mListFactura = new ArrayList<>();
                mListFactura.addAll((List) getIntent().getParcelableArrayListExtra(KEY_PARAM_LISTA_FACTURAS));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerFacturas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == RESULT_OK && requestCode == NotaCreditoArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN)){
                    mFacturaSeleccionada.getLineas().clear();

                    List<FacturaDetalleBean> detalles = data.getExtras()
                            .getParcelableArrayList(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN);

                    mFacturaSeleccionada.setLineas((ArrayList) detalles);
                    showToast(getNumberOfSelecteds(mFacturaSeleccionada) + " producto(s) seleccionado(s).");

                    Intent intent = new Intent();
                    intent.putExtra(KEY_PARAM_FACTURA_SELECTED, mFacturaSeleccionada);
                    setResult(RESULT_OK, intent);
                    finish();

                }else
                    showToast("No se ha recibido ningún producto.");
            }
        }catch (Exception e){
            showToast("onActivityResult() > " + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(FacturaBean entrega) {
        try{
            mFacturaSeleccionada = entrega;
            Intent articulos = new Intent(this,
                    NotaCreditoArticulosActivity.class);
            articulos.putParcelableArrayListExtra(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS,
                    (ArrayList<? extends Parcelable>) mFacturaSeleccionada.getLineas());
            startActivityForResult(articulos, NotaCreditoArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS);
        }catch (Exception e){
            showToast("onItemClick() > " + e.getMessage());
        }
    }

    @Override
    public void onItemLongClick(FacturaBean entrega) {

    }

    private void obtenerFacturas(){
        try{
            if(mListFactura != null)
                mRVAdapterListFactura.clearAndAddAll(mListFactura);
        }catch (Exception e){
            showToast(e.getMessage());
        }
    }

    private void showToast(String message){
        if(message != null)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private int getNumberOfSelecteds(FacturaBean mEntrega){
        int res = 0;

        for (FacturaDetalleBean b : mEntrega.getLineas()) {
            if(b.isSelected())
                res++;
        }

        return res;
    }
}
