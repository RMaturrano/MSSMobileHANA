package com.proyecto.entregas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.entregas.EntregaDetalleActivity;
import com.proyecto.sociosnegocio.DetalleSocioNegocioMain;
import com.proyecto.utils.StringDateCast;

public class EntregaDetalleTabCabecera extends Fragment{

    private View mView;
    private TextView tvNumero, tvReferencia, tvSocio, tvListaPrecio, tvContacto, tvComentario,
                     tvMoneda, tvFechaContable, tvFechaVencimiento, tvTotal;
    private CardView cvSocio;
    private EntregaBean entrega;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_detalle_entrega_tab_cabecera, container, false);

        tvNumero = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabNumero);
        tvReferencia = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabReferencia);
        tvSocio = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabSocioNegocio);
        tvListaPrecio = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabListaPrecio);
        tvContacto = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabContacto);
        tvComentario = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabComentario);
        tvMoneda = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabMoneda);
        tvFechaVencimiento = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabFechaVencimiento);
        tvFechaContable = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabFechaContable);
        tvTotal = (TextView) mView.findViewById(R.id.tvEntregaDetalleCabTotal);
        cvSocio = (CardView) mView.findViewById(R.id.cvDetalleEntregaSocio);
        cvSocio.setOnClickListener(socioClickListener);

        return mView;
    }

    View.OnClickListener socioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(getActivity(),
                    DetalleSocioNegocioMain.class);
            myIntent.putExtra("id", entrega.getSocioNegocio());
            startActivity(myIntent);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        entrega = getActivity().getIntent().getParcelableExtra(EntregaDetalleActivity.KEY_PARAM_ENTREGA);
        fillRows(entrega);
    }

    public void fillRows(EntregaBean entrega){

        try {
            if(entrega != null){
                tvNumero.setText(String.valueOf(entrega.getNumero()));
                tvReferencia.setText(entrega.getReferencia());
                tvSocio.setText(entrega.getSocioNegocioNombre());
                tvListaPrecio.setText(entrega.getListaPrecioNombre());
                tvContacto.setText(entrega.getContactoNombre());
                tvComentario.setText(entrega.getComentario());
                tvMoneda.setText(entrega.getMoneda());
                tvFechaVencimiento.setText(StringDateCast.castStringtoDate(entrega.getFechaVencimiento()));
                tvFechaContable.setText(StringDateCast.castStringtoDate(entrega.getFechaContable()));
                tvTotal.setText(entrega.getTotal());
            }
        }catch (Exception e){
            showMessage(e.getMessage());
        }
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
