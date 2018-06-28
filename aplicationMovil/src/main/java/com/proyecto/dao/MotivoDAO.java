package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyecto.bean.MotivoBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MotivoDAO {

    public List<MotivoBean> listar(String valOrden,String valEntrega,String valFactura) {

        String whereArg = "";

        if(valOrden.equals("Y"))
            whereArg = "ValOrden = 'Y'";
        else if(valEntrega.equals("Y"))
            whereArg = "ValEntrega = 'Y'";
        else if(valFactura.equals("Y"))
            whereArg = "ValFactura = 'Y'";

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase().query("TB_MOTIVO", null, whereArg,
                        null, null, null, "Descripcion");

        List<MotivoBean> lstMotivos = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lstMotivos.add(transformCursorToMotivo(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lstMotivos;
    }

    private MotivoBean transformCursorToMotivo(Cursor cursor) {
        MotivoBean motivo = new MotivoBean();
        motivo.setId(cursor.getInt(cursor.getColumnIndex("Id")));
        motivo.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
        motivo.setValOrden(cursor.getString(cursor.getColumnIndex("ValOrden")));
        motivo.setValEntrega(cursor.getString(cursor.getColumnIndex("ValEntrega")));
        motivo.setValFactura(cursor.getString(cursor.getColumnIndex("ValFactura")));
        return motivo;
    }
}
