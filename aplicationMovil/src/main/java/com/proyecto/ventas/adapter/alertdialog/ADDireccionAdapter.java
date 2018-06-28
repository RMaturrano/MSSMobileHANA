package com.proyecto.ventas.adapter.alertdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.proyect.movil.R;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;

import java.util.ArrayList;

public class ADDireccionAdapter extends ArrayAdapter<DireccionBuscarBean> {
    public ADDireccionAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<DireccionBuscarBean>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ad_contacto_listar_item, parent, false);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.rbtADContacto);
        DireccionBuscarBean direccion = getItem(position);
        radioButton.setText(direccion.getCalle().toUpperCase());
        return view;
    }
}