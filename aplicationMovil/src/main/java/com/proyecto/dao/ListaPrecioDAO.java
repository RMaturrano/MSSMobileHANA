package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ListaPrecioDAO {

    public List<ListaPrecioBean> listar() {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Codigo, " +
                        "T0.Nombre " +
                        " FROM TB_LISTA_PRECIO T0 where T0.Codigo IN " +
                        "(SELECT X0.ListaPrecio from TB_SOCIO_NEGOCIO X0)", null);

        List<ListaPrecioBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToListaPrecio(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    private ListaPrecioBean transformCursorToListaPrecio(Cursor cursor){
        ListaPrecioBean bean = new ListaPrecioBean();
        bean.setCodigo(cursor.getString(cursor.getColumnIndex("Codigo")));
        bean.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
        return bean;
    }

}
