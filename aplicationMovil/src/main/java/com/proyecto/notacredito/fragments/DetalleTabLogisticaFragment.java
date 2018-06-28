package com.proyecto.notacredito.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyect.movil.R;
import com.proyecto.bean.NotaCreditoBean;
import com.proyecto.notacredito.NotaCreditoDetalleActivity;
import com.proyecto.notacredito.adapter.recyclerview.ItemDetailModelNC;
import com.proyecto.notacredito.adapter.recyclerview.RVAdapterDetalleNotaCredito;
import com.proyecto.notacredito.adapter.recyclerview.listeners.IRVAdapterDetalleNotaCredito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 05/06/2018.
 */

public class DetalleTabLogisticaFragment extends Fragment implements IRVAdapterDetalleNotaCredito {

    private RecyclerView mRV;
    private RVAdapterDetalleNotaCredito mAdapter;
    private NotaCreditoBean mDevolucion;

    private static String TTL_DIRECCION_ENTREGA = "Direccion de entrega";
    private static String TTL_DIRECCION_FISCAL = "Direccion fiscal";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView =  inflater.inflate(R.layout.fragment_detalle_notacredito_tab_general, container, false);

        mRV = (RecyclerView) mView.findViewById(R.id.rvDetalleNotTabGeneral);
        mRV.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRV.setHasFixedSize(true);

        mAdapter = new RVAdapterDetalleNotaCredito(this);
        mRV.setAdapter(mAdapter);

        if(getActivity().getIntent().getExtras() != null &&
                getActivity().getIntent().getExtras().containsKey(NotaCreditoDetalleActivity.KEY_PARAM_FACT)){
            mDevolucion = getActivity().getIntent().getParcelableExtra(NotaCreditoDetalleActivity.KEY_PARAM_FACT);
        }

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mDevolucion != null) {
            List<ItemDetailModelNC> items = new ArrayList<>();
            items.add(new ItemDetailModelNC(TTL_DIRECCION_ENTREGA, mDevolucion.getDireccionEntregaDescripcion(), false));
            items.add(new ItemDetailModelNC(TTL_DIRECCION_FISCAL, mDevolucion.getDireccionFiscalDescripcion(), false));
            mAdapter.clearAndAddAll(items);
        }
    }

    @Override
    public void onItemClick(ItemDetailModelNC item) {

    }
}
