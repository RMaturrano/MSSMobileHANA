package com.proyecto.incidencias.adapter.spinner;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;

import java.util.ArrayList;

public class SPAdapterDireccion extends ArrayAdapter<DireccionBuscarBean> {

    public SPAdapterDireccion(@NonNull Context context) {
        super(context, 0, new ArrayList<DireccionBuscarBean>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_direccion_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spDireccionItem);
        DireccionBuscarBean direccion = getItem(position);
        textView.setText(direccion.getCalle().toUpperCase());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_direccion_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spDireccionItem);
        DireccionBuscarBean direccion = getItem(position);
        textView.setText(direccion.getCodigo().toUpperCase());
        return view;
    }
}
