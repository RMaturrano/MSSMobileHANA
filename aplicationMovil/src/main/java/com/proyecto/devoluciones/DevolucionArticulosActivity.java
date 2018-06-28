package com.proyecto.devoluciones;

import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.devoluciones.adapter.recyclerview.RVAdapterDetalleLotes;
import com.proyecto.devoluciones.adapter.recyclerview.RVAdapterListArticulos;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterListArticulos;

import java.util.ArrayList;
import java.util.List;

public class DevolucionArticulosActivity extends AppCompatActivity implements IRVAdapterListArticulos{

    public static int REQUEST_CODE_LISTA_PRODUCTOS = 9991;
    public static String KEY_PARAM_LISTA_PRODUCTOS = "kpItems";
    public static String KEY_PARAM_LISTA_PRODUCTOS_RETURN = "kpItemsReturn";

    private RVAdapterListArticulos mRVAdapter;
    private RVAdapterDetalleLotes mAdapterLotes;
    private RecyclerView mRecyclerView;
    private List<EntregaDetalleBean> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_articulos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDevArticulos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Seleccione los productos");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvDevListaArticulos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mRVAdapter = new RVAdapterListArticulos(this);
        mRecyclerView.setAdapter(mRVAdapter);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_LISTA_PRODUCTOS)){
                mList = new ArrayList<>();
                mList.addAll((List) getIntent().getParcelableArrayListExtra(KEY_PARAM_LISTA_PRODUCTOS));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default_aceptar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_aceptar:
                mList.clear();
                mList.addAll(mRVAdapter.getSelected());

                int quantity = 0;
                for (EntregaDetalleBean bean: mList) {
                    if(bean.isSelected())
                        quantity++;
                }

                if(mList.size() > 0 && quantity > 0){
                    boolean shouldReturn = true;

                    for (EntregaDetalleBean bean: mList) {

                        if(bean.isSelected()) {
                            if (bean.getCantidadTemp() == null || bean.getCantidadTemp().trim().equals("") ||
                                    Integer.parseInt(bean.getCantidadTemp()) <= 0) {
                                showToast("El producto " + bean.getArticulo() + " no tiene una cantidad valida. " +
                                        bean.getCantidadTemp() != null ? bean.getCantidadTemp() : "");
                                shouldReturn = false;
                                break;
                            }

                            if (bean.getLotes() != null && bean.getLotes().size() > 0) {
                                double sumaLote = 0d;
                                for (EntregaDetalleLoteBean lote : bean.getLotes()) {
                                    sumaLote += lote.getCantidadTemp();
                                }

                                if (sumaLote != Double.parseDouble(bean.getCantidadTemp())) {
                                    shouldReturn = false;
                                    showToast("Las cantidades de lotes seleccionadas para el producto " + bean.getArticulo() +
                                            " deben ser igual a " + bean.getCantidadTemp());
                                    break;
                                }
                            }
                        }
                    }

                    if(shouldReturn) {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_PARAM_LISTA_PRODUCTOS_RETURN,
                                (ArrayList<? extends Parcelable>) mList);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }else {
                    showToast("Debe seleccionar un producto.");
                    return false;
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarProductos();
    }

    @Override
    public void onItemClick(final EntregaDetalleBean detalle) {

    }

    @Override
    public void onVerLotesClick(final EntregaDetalleBean detalle, final int pos) {
        if(detalle.isSelected() && detalle.getLotes() != null &&
                detalle.getLotes().size() > 0){
            try{

                if(detalle.getLotes() != null &&
                        detalle.getLotes().size() > 0) {

                    View mBSView = getLayoutInflater().inflate(
                            R.layout.bottom_sheet_devolucion_lote, null);
                    final Dialog mBSDialog = new Dialog(this,
                            R.style.MaterialDialogSheet);
                    RecyclerView mRVContent = (RecyclerView) mBSView.findViewById(R.id.rvDevLotes);
                    mRVContent.setLayoutManager(new LinearLayoutManager(mBSView.getContext()));
                    mRVContent.setHasFixedSize(true);

                    Button btnConfirmarLote = (Button) mBSView.findViewById(R.id.btnDevLotesConfirmar);
                    btnConfirmarLote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<EntregaDetalleLoteBean> listaLote = mAdapterLotes.getAll();
                            if(listaLote != null && listaLote.size() > 0){

                                boolean shouldContinue = true;
                                double sumCant = 0;
                                for (EntregaDetalleLoteBean lote: listaLote) {
                                    if(lote.getCantidadTemp() > lote.getCantidad()){
                                        shouldContinue = false;
                                        showToast("La cantidad del lote " + lote.getLote() + " no puede ser mayor a la cantidad seleccionada.");
                                        break;
                                    }else
                                        sumCant += lote.getCantidadTemp();
                                }

                                if(shouldContinue) {
                                    if (sumCant > Double.parseDouble(detalle.getCantidadTemp())) {
                                        showToast("La suma de cantidades en los lotes no puede ser mayor a la cantidad seleccionada del articulo.");
                                    } else if( sumCant < Double.parseDouble(detalle.getCantidadTemp())){
                                        showToast("La suma de cantidades en los lotes no puede ser menor a la cantidad seleccionada del articulo.");
                                    } else {

                                        for (EntregaDetalleBean det : mList) {
                                            if(det.getLinea() == detalle.getLinea()){
                                                det.getLotes().clear();
                                                det.getLotes().addAll(listaLote);
                                                mList.set(pos, det);
                                                mRVAdapter.notifyItemChanged(pos);
                                                break;
                                            }
                                        }

                                        showToast("Se actualizo la informacion de lotes.");
                                        mBSDialog.dismiss();
                                    }
                                }
                            }
                        }
                    });

                    mAdapterLotes = new RVAdapterDetalleLotes();
                    mRVContent.setAdapter(mAdapterLotes);

                    mAdapterLotes.clearAndAddAll(detalle.getLotes());

                    mBSDialog.setContentView(mBSView);
                    mBSDialog.setCancelable(false);
                    mBSDialog.getWindow().setLayout(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mBSDialog.getWindow().setGravity(Gravity.BOTTOM);
                    mBSDialog.show();
                }else
                    Toast.makeText(this, "No se encontró información sobre lotes", Toast.LENGTH_LONG).show();

            }catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void listarProductos(){
        try{
            if(mList != null) {
                for (EntregaDetalleBean b: mList) {
                    //b.setSelected(true);
                    if(b.getCantidadTemp() == null || b.getCantidadTemp().equals(""))
                        b.setCantidadTemp(b.getDiponible());
                }
                mRVAdapter.clearAndAddAll(mList);
            }
        }catch (Exception e){
            showToast(e.getMessage());
        }
    }

    private void showToast(String message){
        if(message != null)
            Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }
}
