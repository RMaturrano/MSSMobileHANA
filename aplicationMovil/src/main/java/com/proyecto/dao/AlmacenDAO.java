package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.bean.AlmacenBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 03/07/2018.
 */

public class AlmacenDAO {

    public List<AlmacenBean> listar() {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.CODIGO, " +
                        "T0.NOMBRE " +
                        " FROM TB_ALMACEN T0 ", null);

        List<AlmacenBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToListaPrecio(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    private AlmacenBean transformCursorToListaPrecio(Cursor cursor){
        AlmacenBean bean = new AlmacenBean();
        bean.setCodigo(cursor.getString(cursor.getColumnIndex("CODIGO")));
        bean.setDescripcion(cursor.getString(cursor.getColumnIndex("NOMBRE")));
        return bean;
    }

}
