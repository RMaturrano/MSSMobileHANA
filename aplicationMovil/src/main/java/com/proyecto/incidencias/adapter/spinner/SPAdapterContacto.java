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
import com.proyecto.sociosnegocio.util.ContactoBuscarBean;

import java.util.ArrayList;

public class SPAdapterContacto extends ArrayAdapter<ContactoBuscarBean>{

    public SPAdapterContacto(@NonNull Context context) {
        super(context, 0, new ArrayList<ContactoBuscarBean>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_contacto_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spContactoItem);
        ContactoBuscarBean direccion = getItem(position);
        textView.setText(direccion.getNombre().toUpperCase());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_contacto_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spContactoItem);
        ContactoBuscarBean direccion = getItem(position);
        textView.setText(direccion.getNombre().toUpperCase());
        return view;
    }

}
