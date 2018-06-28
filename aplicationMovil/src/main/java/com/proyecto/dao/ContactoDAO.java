package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.database.DataBaseHelper;
import com.proyecto.sociosnegocio.util.ContactoBuscarBean;

import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {

    public List<ContactoBuscarBean> listar(String codigoCliente) {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Codigo, " +
                        "T0.Nombre " +
                        " FROM TB_SOCIO_NEGOCIO_CONTACTO T0 " +
                        " where T0.CodigoSocioNegocio = '" + codigoCliente + "'", null);

        List<ContactoBuscarBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToContacto(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    private ContactoBuscarBean transformCursorToContacto(Cursor cursor){
        ContactoBuscarBean bean = new ContactoBuscarBean();
        bean.setCodigo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Codigo"))));
        bean.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
        return bean;
    }
}
