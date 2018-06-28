package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyecto.bean.DireccionBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DireccionDAO {

    public List<DireccionBuscarBean> listar(String codigoCliente) {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Codigo, " +
                                "T0.Calle, " +
                                "T0.Tipo,  " +
                                "T0.Latitud, "+
                                "T0.Longitud "+
                        " FROM TB_SOCIO_NEGOCIO_DIRECCION T0 " +
                        " where T0.CodigoSocioNegocio = '" + codigoCliente + "'", null);

        List<DireccionBuscarBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToDireccion(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    public List<DireccionBuscarBean> listarDireccionesVisita(int day, String currDate) {

        String whereCls = " AND ";

        switch (day){
            case Calendar.MONDAY:
                whereCls += "VisitaLunes='Y'";
                break;
            case Calendar.TUESDAY:
                whereCls += "VisitaMartes='Y'";
                break;
            case Calendar.WEDNESDAY:
                whereCls += "VisitaMiercoles='Y'";
                break;
            case Calendar.THURSDAY:
                whereCls += "VisitaJueves='Y'";
                break;
            case Calendar.FRIDAY:
                whereCls += "VisitaViernes='Y'";
                break;
            case Calendar.SATURDAY:
                whereCls += "VisitaSabado='Y'";
                break;
            case Calendar.SUNDAY:
                whereCls += "VisitaDomingo='Y'";
                break;
        }

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Codigo, " +
                        "T0.Calle, " +
                        "T0.Tipo,  " +
                        "T0.Latitud, "+
                        "T0.Longitud, " +
                        "IFNULL(T1.Codigo,'') as CodigoCliente, "+
                        "IFNULL(T1.NombreRazonSocial,'') as NombreCliente," +
                        "IFNULL(T2.NOMBRE,'') AS DepartamentoNombre, " +
                        "IFNULL(T3.NOMBRE,'') AS ProvinciaNombre," +
                        "IFNULL(T4.NOMBRE,'') AS DistritoNombre, " +
                        " IFNULL(T0.VisitaLunes,'N') AS VisitaLunes, " +
                        " IFNULL(T0.VisitaMartes,'N') AS VisitaMartes, " +
                        " IFNULL(T0.VisitaMiercoles,'N') AS VisitaMiercoles, " +
                        " IFNULL(T0.VisitaJueves,'N') AS VisitaJueves, " +
                        " IFNULL(T0.VisitaViernes,'N') AS VisitaViernes, " +
                        " IFNULL(T0.VisitaSabado,'N') AS VisitaSabado, " +
                        " IFNULL(T0.VisitaDomingo,'N') AS VisitaDomingo, " +
                        " IFNULL(T0.InicioVisitas,'') AS InicioVisitas, " +
                        " IFNULL(T0.Frecuencia,'') AS Frecuencia, " +
                        " IFNULL(T1.NumUltimaCompra,'') AS NumUltimaCompra, " +
                        " IFNULL(T1.FechaUltimaCompra,'') AS FecUltimaCompra, " +
                        " IFNULL(T1.MontoUltimaCompra,'') AS MonUltimaCompra, " +
                        " IFNULL(T5.Nombre,'') AS NombreContacto, " +
                        " IFNULL(T5.TelefonoMovil,'') AS TelefonoContacto " +
                        " FROM TB_SOCIO_NEGOCIO_DIRECCION T0 JOIN TB_SOCIO_NEGOCIO T1 " +
                        " ON T0.CodigoSocioNegocio = T1.Codigo LEFT JOIN TB_DEPARTAMENTO T2 " +
                        " ON T0.Departamento = T2.CODIGO LEFT JOIN TB_PROVINCIA T3 " +
                        " ON T0.Provincia = T3.CODIGO LEFT JOIN TB_DISTRITO T4 " +
                        " ON T0.Distrito = T4.CODIGO LEFT JOIN TB_SOCIO_NEGOCIO_CONTACTO T5 " +
                        " ON T1.PersonaContacto = T5.Nombre AND T1.Codigo = T5.CodigoSocioNegocio " +
                        " WHERE T0.InicioVisitas != '' " +
                        " AND IFNULL(cast(T0.InicioVisitas AS INT),29991230) <= " + currDate + whereCls, null);

        List<DireccionBuscarBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToDireccionVisita(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    public boolean update(DireccionBean direccionBean) {
        ContentValues cv = new ContentValues();
        cv.put("Latitud", direccionBean.getLatitud());
        cv.put("Longitud", direccionBean.getLongitud());

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .update("TB_SOCIO_NEGOCIO_DIRECCION", cv,"Codigo = ? AND CodigoSocioNegocio = ?",
                        new String[]{direccionBean.getIDDireccion(), direccionBean.getCodigoCliente()});

        return update > 0;
    }

    private DireccionBuscarBean transformCursorToDireccion(Cursor cursor){
        DireccionBuscarBean bean = new DireccionBuscarBean();
        bean.setCodigo(cursor.getString(cursor.getColumnIndex("Codigo")));
        bean.setCalle(cursor.getString(cursor.getColumnIndex("Calle")));
        bean.setTipo(cursor.getString(cursor.getColumnIndex("Tipo")));
        bean.setLatitud(cursor.getString(cursor.getColumnIndex("Latitud")));
        bean.setLongitud(cursor.getString(cursor.getColumnIndex("Longitud")));

        return bean;
    }

    private DireccionBuscarBean transformCursorToDireccionVisita(Cursor cursor){
        DireccionBuscarBean bean = new DireccionBuscarBean();
        bean.setCodigo(cursor.getString(cursor.getColumnIndex("Codigo")));
        bean.setCalle(cursor.getString(cursor.getColumnIndex("Calle")));
        bean.setTipo(cursor.getString(cursor.getColumnIndex("Tipo")));
        bean.setLatitud(cursor.getString(cursor.getColumnIndex("Latitud")));
        bean.setLongitud(cursor.getString(cursor.getColumnIndex("Longitud")));
        bean.setCodigoCliente(cursor.getString(cursor.getColumnIndex("CodigoCliente")));
        bean.setNombreCliente(cursor.getString(cursor.getColumnIndex("NombreCliente")));
        bean.setDepartamentoNombre(cursor.getString(cursor.getColumnIndex("DepartamentoNombre")));
        bean.setProvinciaNombre(cursor.getString(cursor.getColumnIndex("ProvinciaNombre")));
        bean.setDistritoNombre(cursor.getString(cursor.getColumnIndex("DistritoNombre")));
        bean.setVisitaLunes(cursor.getString(cursor.getColumnIndex("VisitaLunes")));
        bean.setVisitaMartes(cursor.getString(cursor.getColumnIndex("VisitaMartes")));
        bean.setVisitaMiercoles(cursor.getString(cursor.getColumnIndex("VisitaMiercoles")));
        bean.setVisitaJueves(cursor.getString(cursor.getColumnIndex("VisitaJueves")));
        bean.setVisitaViernes(cursor.getString(cursor.getColumnIndex("VisitaViernes")));
        bean.setVisitaSabado(cursor.getString(cursor.getColumnIndex("VisitaSabado")));
        bean.setVisitaDomingo(cursor.getString(cursor.getColumnIndex("VisitaDomingo")));
        bean.setFechaInicio(cursor.getString(cursor.getColumnIndex("InicioVisitas")));
        bean.setFrecuenciaVisita(cursor.getString(cursor.getColumnIndex("Frecuencia")));
        bean.setNumUltimaCompra(cursor.getString(cursor.getColumnIndex("NumUltimaCompra")));
        bean.setFecUltimaCompra(cursor.getString(cursor.getColumnIndex("FecUltimaCompra")));
        bean.setMonUltimaCompra(cursor.getString(cursor.getColumnIndex("MonUltimaCompra")));
        bean.setPersonaContacto(cursor.getString(cursor.getColumnIndex("NombreContacto")));
        bean.setTelefonoContacto(cursor.getString(cursor.getColumnIndex("TelefonoContacto")));

        return bean;
    }
}
