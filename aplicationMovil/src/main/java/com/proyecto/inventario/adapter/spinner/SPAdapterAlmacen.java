package com.proyecto.inventario.adapter.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;

import java.util.ArrayList;

public class SPAdapterAlmacen extends ArrayAdapter<AlmacenBean>{

    public SPAdapterAlmacen (Context context){
        super (context,0,new ArrayList<AlmacenBean>());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_listaprecio_listar_item, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.spListaPrecioItem);
        AlmacenBean listaPrecio = getItem(position);
        textView.setText(listaPrecio.getDescripcion().toUpperCase());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sp_listaprecio_listar_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.spListaPrecioItem);
        AlmacenBean listaPrecio = getItem(position);
        textView.setText(listaPrecio.getDescripcion().toUpperCase());
        return view;
    }

}
