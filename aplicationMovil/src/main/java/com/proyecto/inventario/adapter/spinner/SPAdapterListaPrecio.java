package com.proyecto.inventario.adapter.spinner;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.ListaPrecioBean;

import java.util.ArrayList;

public class SPAdapterListaPrecio extends ArrayAdapter<ListaPrecioBean>{


    public SPAdapterListaPrecio (Context context){
        super (context,0,new ArrayList<ListaPrecioBean>());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_listaprecio_listar_item, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.spListaPrecioItem);
        ListaPrecioBean listaPrecio = getItem(position);
        textView.setText(listaPrecio.getNombre().toUpperCase());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_listaprecio_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spListaPrecioItem);
        ListaPrecioBean listaPrecio = getItem(position);
        textView.setText(listaPrecio.getNombre().toUpperCase());
        return view;
    }
}
