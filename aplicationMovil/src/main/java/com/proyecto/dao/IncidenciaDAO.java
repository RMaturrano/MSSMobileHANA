package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.proyecto.bean.IncidenciaBean;
import com.proyecto.database.DataBaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    public List<IncidenciaBean> listar(String origen){

      List<IncidenciaBean> mList = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Id, " +
                        "T0.ClaveMovil, " +
                        "T0.Origen, " +
                        "T0.IdCliente, " +
                        "T0.NombreCliente, " +
                        "T0.CodigoContacto, " +
                        "T0.NombreContacto, " +
                        "T0.CodigoDireccion, " +
                        "T0.DetalleDireccion, " +
                        "T0.Motivo, " +
                        "T0.DescripcionMotivo, " +
                        "T0.Comentarios, " +
                        "T0.CodigoVendedor, " +
                        "T0.Latitud, " +
                        "T0.Longitud, " +
                        "T0.FechaCreacion, " +
                        "T0.HoraCreacion, " +
                        "T0.ModoOffline, " +
                        "T0.SerieFactura, " +
                        "T0.CorrelativoFactura, " +
                        "T0.TipoIncidencia, " +
                        "T0.FechaPago, " +
                        "T0.Sincronizado, " +
                        "T0.Rango, " +
                        "T0.Foto " +
                        " FROM TB_INCIDENCIA T0 " +
                        " where T0.Origen = '" + origen + "'", null);

        if (cursor.moveToFirst()) {
            do {
                mList.add(transformCursorToIncidencia(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

      return mList;
    };

    public List<IncidenciaBean> listar(){

        List<IncidenciaBean> mList = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Id, " +
                        "T0.ClaveMovil, " +
                        "T0.Origen, " +
                        "T0.IdCliente, " +
                        "T0.NombreCliente, " +
                        "T0.CodigoContacto, " +
                        "T0.NombreContacto, " +
                        "T0.CodigoDireccion, " +
                        "T0.DetalleDireccion, " +
                        "T0.Motivo, " +
                        "T0.DescripcionMotivo, " +
                        "T0.Comentarios, " +
                        "T0.CodigoVendedor, " +
                        "T0.Latitud, " +
                        "T0.Longitud, " +
                        "T0.FechaCreacion, " +
                        "T0.HoraCreacion, " +
                        "T0.ModoOffline, " +
                        "T0.SerieFactura, " +
                        "T0.CorrelativoFactura, " +
                        "T0.TipoIncidencia, " +
                        "T0.FechaPago, " +
                        "T0.Sincronizado, " +
                        "T0.Foto " +
                        " FROM TB_INCIDENCIA T0 " +
                        " where T0.Sincronizado = 'N'", null);

        if (cursor.moveToFirst()) {
            do {
                mList.add(transformCursorToIncidencia(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return mList;
    };

    public boolean actualizarSincronizado(String clave) {
        ContentValues cv = new ContentValues();
        cv.put("Sincronizado", "Y");

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase().update("TB_INCIDENCIA", cv, "ClaveMovil = ?",
                        new String[]{String.valueOf(clave)});
        return update > 0;
    }

    public boolean insertIncidencia(IncidenciaBean incidencia) {
        ContentValues cv = new ContentValues();
        cv.put("ClaveMovil", incidencia.getClaveMovil());
        cv.put("Origen", incidencia.getOrigen());
        cv.put("IdCliente", incidencia.getCodigoCliente());
        cv.put("NombreCliente", incidencia.getNombreCliente());
        cv.put("CodigoContacto", incidencia.getCodigoContacto());
        cv.put("NombreContacto", incidencia.getNombreContacto());
        cv.put("CodigoDireccion", incidencia.getCodigoDireccion());
        cv.put("DetalleDireccion", incidencia.getDetalleDireccion());
        cv.put("Motivo", incidencia.getMotivo());
        cv.put("DescripcionMotivo", incidencia.getDescripcionMotivo());
        cv.put("Comentarios", incidencia.getComentarios());
        cv.put("CodigoVendedor", incidencia.getCodigoVendedor());
        cv.put("Latitud", incidencia.getLatitud());
        cv.put("Longitud", incidencia.getLongitud());
        cv.put("FechaCreacion", incidencia.getFechaCreacion());
        cv.put("HoraCreacion", incidencia.getHoraCreacion());
        cv.put("ModoOffline", incidencia.getModoOffline());
        cv.put("ClaveFactura", incidencia.getClaveFactura());
        cv.put("SerieFactura", incidencia.getSerieFactura());
        cv.put("CorrelativoFactura", incidencia.getCorrelativoFactura());
        cv.put("TipoIncidencia", incidencia.getTipoIncidencia());
        cv.put("FechaPago", incidencia.getFechaCompromisoPago());
        cv.put("Foto", incidencia.getFoto() != null ? getBitmapAsByteArray(incidencia.getFoto()) : null);
        cv.put("Rango", incidencia.getRango());
        cv.put("Sincronizado", incidencia.getSincronizado());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_INCIDENCIA", null, cv);

        return inserto != -1;
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap getImage(byte[] imageBytes){

        try{
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }catch (Exception e){
            return null ;
        }
    }

    private IncidenciaBean transformCursorToIncidencia(Cursor cursor){

        IncidenciaBean incidenciaBean = new IncidenciaBean();
        incidenciaBean.setClaveMovil(cursor.getString(cursor.getColumnIndex("ClaveMovil")));
        incidenciaBean.setOrigen(cursor.getString(cursor.getColumnIndex("Origen")));
        incidenciaBean.setCodigoCliente(cursor.getString(cursor.getColumnIndex("IdCliente")));
        incidenciaBean.setNombreCliente(cursor.getString(cursor.getColumnIndex("NombreCliente")));
        incidenciaBean.setCodigoContacto(cursor.getInt(cursor.getColumnIndex("CodigoContacto")));
        incidenciaBean.setNombreContacto(cursor.getString(cursor.getColumnIndex("NombreContacto")));
        incidenciaBean.setCodigoDireccion(cursor.getString(cursor.getColumnIndex("CodigoDireccion")));
        incidenciaBean.setDetalleDireccion(cursor.getString(cursor.getColumnIndex("DetalleDireccion")));
        incidenciaBean.setMotivo(cursor.getString(cursor.getColumnIndex("Motivo")));
        incidenciaBean.setDescripcionMotivo(cursor.getString(cursor.getColumnIndex("DescripcionMotivo")));
        incidenciaBean.setComentarios(cursor.getString(cursor.getColumnIndex("Comentarios")));
        incidenciaBean.setCodigoVendedor(cursor.getInt(cursor.getColumnIndex("CodigoVendedor")));
        incidenciaBean.setLatitud(cursor.getString(cursor.getColumnIndex("Latitud")));
        incidenciaBean.setLongitud(cursor.getString(cursor.getColumnIndex("Longitud")));
        incidenciaBean.setFechaCreacion(cursor.getString(cursor.getColumnIndex("FechaCreacion")));
        incidenciaBean.setHoraCreacion(cursor.getString(cursor.getColumnIndex("HoraCreacion")));
        incidenciaBean.setModoOffline(cursor.getString(cursor.getColumnIndex("ModoOffline")));
        incidenciaBean.setSerieFactura(cursor.getString(cursor.getColumnIndex("SerieFactura")));
        incidenciaBean.setCorrelativoFactura(cursor.getInt(cursor.getColumnIndex("CorrelativoFactura")));
        incidenciaBean.setTipoIncidencia(cursor.getString(cursor.getColumnIndex("TipoIncidencia")));
        incidenciaBean.setFechaCompromisoPago(cursor.getString(cursor.getColumnIndex("FechaPago")));
        incidenciaBean.setFoto(getImage(cursor.getBlob(cursor.getColumnIndex("Foto"))));
        incidenciaBean.setRango(cursor.getString(cursor.getColumnIndex("Rango")));
        incidenciaBean.setSincronizado(cursor.getString(cursor.getColumnIndex("Sincronizado")));
        return incidenciaBean;

    }

}
