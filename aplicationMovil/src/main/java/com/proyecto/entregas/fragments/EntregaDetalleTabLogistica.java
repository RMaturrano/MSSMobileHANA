package com.proyecto.entregas.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.entregas.EntregaDetalleActivity;

public class EntregaDetalleTabLogistica extends Fragment {

    private View mView;
    private TextView tvDireccionFiscal, tvDireccionEntrega, tvCondicionPago, tvIndicador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_detalle_entrega_tab_logistica, container, false);

        tvDireccionFiscal = (TextView) mView.findViewById(R.id.tvEntregaDetalleLogDirFiscal);
        tvDireccionEntrega = (TextView) mView.findViewById(R.id.tvEntregaDetalleLogDirEntrega);
        tvCondicionPago = (TextView) mView.findViewById(R.id.tvEntregaDetalleLogCondPago);
        tvIndicador = (TextView) mView.findViewById(R.id.tvEntregaDetalleLogIndicador);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EntregaBean entrega = getActivity().getIntent().getParcelableExtra(EntregaDetalleActivity.KEY_PARAM_ENTREGA);
        fillRows(entrega);
    }

    public void fillRows(EntregaBean entrega){

        try {
            if(entrega != null){
                tvDireccionFiscal.setText(entrega.getDireccionFiscalDescripcion());
                tvDireccionEntrega.setText(entrega.getDireccionEntregaDescripcion());
                tvCondicionPago.setText(entrega.getCondicionPagoNombre());
                tvIndicador.setText(entrega.getIndicadorNombre());
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
