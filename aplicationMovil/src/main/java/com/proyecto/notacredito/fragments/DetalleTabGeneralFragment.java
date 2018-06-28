package com.proyecto.notacredito.fragments;

import android.content.Intent;
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
import com.proyecto.sociosnegocio.DetalleSocioNegocioMain;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class DetalleTabGeneralFragment extends Fragment implements IRVAdapterDetalleNotaCredito {

    private RecyclerView mRV;
    private RVAdapterDetalleNotaCredito mAdapter;
    private NotaCreditoBean mNotaCredito;

    private static String TTL_NUMERO_DOC = "Numero documento";
    private static String TTL_SOCIO_NEGOCIO = "Socio negocio";
    private static String TTL_CONTACTO = "Persona de contacto";
    private static String TTL_MONEDA = "Moneda";
    private static String TTL_FECHA_CONTABLE = "Fecha contable";
    private static String TTL_FECHA_VENCIMIENTO = "Fecha de vencimiento";
    private static String TTL_COMENTARIOS = "Comentarios";
    private static String TTL_TOTAL_SIN_DCTO = "Total antes del descuento";
    private static String TTL_IMPUESTO = "Impuesto";
    private static String TTL_TOTAL = "Total";

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
            mNotaCredito = getActivity().getIntent().getParcelableExtra(NotaCreditoDetalleActivity.KEY_PARAM_FACT);
        }

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mNotaCredito != null) {
            List<ItemDetailModelNC> items = new ArrayList<>();
            items.add(new ItemDetailModelNC(TTL_NUMERO_DOC, mNotaCredito.getClave(), false));
            items.add(new ItemDetailModelNC(TTL_SOCIO_NEGOCIO, mNotaCredito.getSocioNegocioNombre(), true));
            items.add(new ItemDetailModelNC(TTL_CONTACTO, mNotaCredito.getContactoNombre(), false));
            items.add(new ItemDetailModelNC(TTL_MONEDA, mNotaCredito.getMoneda(), false));
            items.add(new ItemDetailModelNC(TTL_FECHA_CONTABLE, StringDateCast.castStringtoDate(mNotaCredito.getFechaContable()), false));
            items.add(new ItemDetailModelNC(TTL_FECHA_VENCIMIENTO, StringDateCast.castStringtoDate(mNotaCredito.getFechaVencimiento()), false));
            items.add(new ItemDetailModelNC(TTL_COMENTARIOS, mNotaCredito.getComentario(), false));
            items.add(new ItemDetailModelNC(TTL_TOTAL_SIN_DCTO, mNotaCredito.getSubTotal(), false));
            items.add(new ItemDetailModelNC(TTL_IMPUESTO, mNotaCredito.getImpuesto(), false));
            items.add(new ItemDetailModelNC(TTL_TOTAL, mNotaCredito.getTotal(), false));
            mAdapter.clearAndAddAll(items);
        }
    }

    @Override
    public void onItemClick(ItemDetailModelNC item) {
        if(item.getTitle().equals(TTL_SOCIO_NEGOCIO)){
            Intent myIntent = new Intent(getActivity(),
                    DetalleSocioNegocioMain.class);
            myIntent.putExtra("id", mNotaCredito.getSocioNegocio());
            startActivity(myIntent);
        }
    }

}
