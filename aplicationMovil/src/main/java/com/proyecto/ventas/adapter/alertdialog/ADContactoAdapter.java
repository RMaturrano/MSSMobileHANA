package com.proyecto.ventas.adapter.alertdialog;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.sociosnegocio.util.ContactoBuscarBean;

import java.util.ArrayList;

public class ADContactoAdapter extends ArrayAdapter<ContactoBuscarBean> {
    public ADContactoAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<ContactoBuscarBean>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ad_contacto_listar_item, parent, false);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.rbtADContacto);
        ContactoBuscarBean direccion = getItem(position);
        radioButton.setText(direccion.getNombre().toUpperCase());
        return view;
    }
}
