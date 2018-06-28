package com.proyecto.devoluciones.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyect.movil.R;
import com.proyecto.bean.DevolucionBean;
import com.proyecto.devoluciones.DevolucionDetalleActivity;
import com.proyecto.devoluciones.ItemDetailModel;
import com.proyecto.devoluciones.adapter.recyclerview.RVAdapterDetalleDevolucion;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterDetalleDevolucion;
import com.proyecto.sociosnegocio.DetalleSocioNegocioMain;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class DetalleTabGeneralFragment extends Fragment implements IRVAdapterDetalleDevolucion{

    private RecyclerView mRV;
    private RVAdapterDetalleDevolucion mAdapter;
    private DevolucionBean mDevolucion;

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
        View mView =  inflater.inflate(R.layout.fragment_detalle_devolucion_tab_general, container, false);

        mRV = (RecyclerView) mView.findViewById(R.id.rvDetalleDevTabGeneral);
        mRV.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRV.setHasFixedSize(true);

        mAdapter = new RVAdapterDetalleDevolucion(this);
        mRV.setAdapter(mAdapter);

        if(getActivity().getIntent().getExtras() != null &&
                getActivity().getIntent().getExtras().containsKey(DevolucionDetalleActivity.KEY_PARAM_DEVOLUCION)){
            mDevolucion = getActivity().getIntent().getParcelableExtra(DevolucionDetalleActivity.KEY_PARAM_DEVOLUCION);
        }

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mDevolucion != null) {
            List<ItemDetailModel> items = new ArrayList<>();
            items.add(new ItemDetailModel(TTL_NUMERO_DOC, mDevolucion.getClave(), false));
            items.add(new ItemDetailModel(TTL_SOCIO_NEGOCIO, mDevolucion.getSocioNegocioNombre(), true));
            items.add(new ItemDetailModel(TTL_CONTACTO, mDevolucion.getContactoNombre(), false));
            items.add(new ItemDetailModel(TTL_MONEDA, mDevolucion.getMoneda(), false));
            items.add(new ItemDetailModel(TTL_FECHA_CONTABLE, StringDateCast.castStringtoDate(mDevolucion.getFechaContable()), false));
            items.add(new ItemDetailModel(TTL_FECHA_VENCIMIENTO, StringDateCast.castStringtoDate(mDevolucion.getFechaVencimiento()), false));
            items.add(new ItemDetailModel(TTL_COMENTARIOS, mDevolucion.getComentario(), false));
            items.add(new ItemDetailModel(TTL_TOTAL_SIN_DCTO, mDevolucion.getSubTotal(), false));
            items.add(new ItemDetailModel(TTL_IMPUESTO, mDevolucion.getImpuesto(), false));
            items.add(new ItemDetailModel(TTL_TOTAL, mDevolucion.getTotal(), false));
            mAdapter.clearAndAddAll(items);
        }
    }

    @Override
    public void onItemClick(ItemDetailModel item) {
        if(item.getTitle().equals(TTL_SOCIO_NEGOCIO)){
            Intent myIntent = new Intent(getActivity(),
                    DetalleSocioNegocioMain.class);
            myIntent.putExtra("id", mDevolucion.getSocioNegocio());
            startActivity(myIntent);
        }
    }
}
