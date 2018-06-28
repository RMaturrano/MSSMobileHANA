package com.proyecto.devoluciones;

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
import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.entregas.EntregaDetalleActivity;
import com.proyecto.entregas.adapter.recyclerview.RVAdapterListEntrega;
import com.proyecto.entregas.adapter.recyclerview.listeners.IRVAdapterListEntrega;

import java.util.ArrayList;
import java.util.List;

public class DevolucionListaEntregaActivity extends AppCompatActivity implements IRVAdapterListEntrega {

    public static int REQUEST_CODE_LISTA_ENTREGAS = 999;
    public static String KEY_PARAM_LISTA_ENTREGAS = "kpEntregas";
    public static String KEY_PARAM_ENTREGA_SELECTED = "kpEntregaSel";

    private RVAdapterListEntrega mRVAdapterListEntrega;
    private RecyclerView mRVListaEntrega;
    private EntregaBean mEntregaSeleccionada;
    private List<EntregaBean> mListEntrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_lista_entrega);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDevListaEntrega);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Seleccione entrega");

        mRVListaEntrega = (RecyclerView) findViewById(R.id.rvDevListaEntrega);
        mRVListaEntrega.setLayoutManager(new LinearLayoutManager(this));
        mRVListaEntrega.setHasFixedSize(true);

        mRVAdapterListEntrega = new RVAdapterListEntrega(this);
        mRVListaEntrega.setAdapter(mRVAdapterListEntrega);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_LISTA_ENTREGAS)){
                mListEntrega = new ArrayList<>();
                mListEntrega.addAll((List) getIntent().getParcelableArrayListExtra(KEY_PARAM_LISTA_ENTREGAS));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerEntregas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == RESULT_OK && requestCode == DevolucionArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN)){
                    mEntregaSeleccionada.getLineas().clear();

                    List<EntregaDetalleBean> detalles = data.getExtras()
                                .getParcelableArrayList(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN);

                    mEntregaSeleccionada.setLineas(detalles);
                    showToast(getNumberOfSelecteds(mEntregaSeleccionada) + " producto(s) seleccionado(s).");

                    Intent intent = new Intent();
                    intent.putExtra(KEY_PARAM_ENTREGA_SELECTED, mEntregaSeleccionada);
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
    public void onItemClick(EntregaBean entrega) {
        try{
            mEntregaSeleccionada = entrega;
            Intent articulos = new Intent(DevolucionListaEntregaActivity.this,
                    DevolucionArticulosActivity.class);
            articulos.putParcelableArrayListExtra(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS,
                    (ArrayList<? extends Parcelable>) mEntregaSeleccionada.getLineas());
            startActivityForResult(articulos, DevolucionArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS);
        }catch (Exception e){
            showToast("onItemClick() > " + e.getMessage());
        }
    }

    @Override
    public void onItemLongClick(EntregaBean entrega) {

    }

    private void obtenerEntregas(){
        try{
            if(mListEntrega != null)
                mRVAdapterListEntrega.clearAndAddAll(mListEntrega);
        }catch (Exception e){
            showToast(e.getMessage());
        }
    }

    private void showToast(String message){
        if(message != null)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private int getNumberOfSelecteds(EntregaBean mEntrega){
        int res = 0;

        for (EntregaDetalleBean b : mEntrega.getLineas()) {
            if(b.isSelected())
                res++;
        }

        return res;
    }
}
