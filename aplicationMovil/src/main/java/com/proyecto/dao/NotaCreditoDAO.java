package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyecto.bean.NotaCreditoBean;
import com.proyecto.bean.NotaCreditoDetalleBean;
import com.proyecto.bean.NotaCreditoDetalleLoteBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NotaCreditoDAO {

    public List<NotaCreditoBean> listar(String sincronizado){
        List<NotaCreditoBean> list = new ArrayList<>();

        String whereArg =  "";

        if(sincronizado != null){
            whereArg = " where T0.EstadoMovil = '"+sincronizado+"' ";
        }


        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Clave," +
                        "T0.ClaveMovil, " +
                        "T0.ClaveBase," +
                        "T0.Numero, " +
                        "T0.Referencia, " +
                        "T0.SocioNegocio, " +
                        "T1.NombreRazonSocial," +
                        "T0.ListaPrecio," +
                        "IFNULL(T5.Nombre,'') AS ListaPrecioNombre," +
                        "T0.Contacto," +
                        "IFNULL(T2.Nombre,'') AS ContactoNombre," +
                        "T0.Moneda," +
                        "T0.EmpleadoVenta," +
                        "T0.Comentario," +
                        "T0.FechaContable," +
                        "T0.FechaVencimiento," +
                        "T0.DireccionFiscal," +
                        "IFNULL((SELECT IFNULL(X0.Calle, X0.Referencia) FROM TB_SOCIO_NEGOCIO_DIRECCION X0" +
                        "   WHERE X0.Codigo = T0.DireccionFiscal AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
                        "   AS DireccionFiscalDescripcion ," +
                        "T0.DireccionEntrega," +
                        "IFNULL((SELECT IFNULL(X0.Calle, X0.Referencia) FROM TB_SOCIO_NEGOCIO_DIRECCION X0" +
                        "   WHERE X0.Codigo = T0.DireccionEntrega AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
                        "   AS DireccionEntregaDescripcion ," +
                        "T0.CondicionPago," +
                        "IFNULL(T3.NOMBRE,'') AS CondicionPagoNombre," +
                        "T0.Indicador," +
                        "IFNULL(T4.Nombre,'') AS IndicadorNombre," +
                        "T0.SubTotal," +
                        "T0.Descuento," +
                        "T0.Impuesto," +
                        "T0.Total," +
                        "T0.EstadoMovil," +
                        "T0.Saldo from TB_NOTA_CREDITO T0 LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO_CONTACTO T2 ON T0.Contacto = T2.Codigo " +
                        " AND T2.CodigoSocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_CONDICION_PAGO T3 ON T3.CODIGO = T0.CondicionPago LEFT JOIN " +
                        " TB_INDICADOR T4 ON T4.Codigo = T0.Indicador LEFT JOIN " +
                        " TB_LISTA_PRECIO T5 ON T0.ListaPrecio = T5.Codigo " +
                        whereArg, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToNotaCredito(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return list;
    }

    public List<NotaCreditoDetalleBean> listarDetalle(String clave){
        List<NotaCreditoDetalleBean> list = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.Linea, " +
                        "T0.LineaBase, " +
                        "T0.Articulo, " +
                        "T1.Nombre as ArticuloNombre, " +
                        "T0.UnidadMedida, " +
                        "T0.Almacen, " +
                        "IFNULL(T2.NOMBRE,'') AS AlmacenNombre, " +
                        "T0.Cantidad," +
                        "T0.ListaPrecio," +
                        "T0.PrecioUnitario," +
                        "T0.PorcentajeDescuento," +
                        "T0.Impuesto, " +
                        "T0.ClaveNotaCredito from TB_NOTA_CREDITO_DETALLE T0 LEFT JOIN " +
                        " TB_ARTICULO T1 ON T0.Articulo = T1.Codigo LEFT JOIN " +
                        " TB_ALMACEN T2 ON T0.Almacen = T2.CODIGO " +
                        " where T0.ClaveNotaCredito = '" +clave + "'", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToNotaCreditoDetalle(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }

    public List<NotaCreditoDetalleLoteBean> listarDetalleLotes(String claveEntrega, int numLinea){
        List<NotaCreditoDetalleLoteBean> listEntrega = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.ClaveNotaCredito, " +
                        "T0.Lote, " +
                        "IFNULL(T0.Cantidad,0) as Cantidad, " +
                        "T0.LineaBase from TB_NOTA_CREDITO_DETALLE_LOTE  T0 " +
                        " where T0.ClaveNotaCredito = '" +claveEntrega + "' " +
                        " and T0.LineaBase = " + numLinea, null);

        if (cursor.moveToFirst()) {
            do {
                listEntrega.add(transformCursorToNotaCreditoDetalleLote(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  listEntrega;
    }

    public boolean insertNotaCredito(NotaCreditoBean notacredito) {
        ContentValues cv = new ContentValues();
        cv.put("Clave", notacredito.getClave());
        cv.put("ClaveMovil", notacredito.getClaveMovil());
        cv.put("Referencia", notacredito.getReferencia());
        cv.put("SocioNegocio", notacredito.getSocioNegocio());
        cv.put("ListaPrecio", notacredito.getListaPrecio());
        cv.put("Contacto", notacredito.getContacto());
        cv.put("Moneda", notacredito.getMoneda());
        cv.put("EmpleadoVenta", notacredito.getEmpleadoVenta());
        cv.put("Comentario", notacredito.getComentario());
        cv.put("FechaContable", notacredito.getFechaContable());
        cv.put("FechaVencimiento", notacredito.getFechaVencimiento());
        cv.put("DireccionFiscal", notacredito.getDireccionFiscal());
        cv.put("DireccionEntrega", notacredito.getDireccionEntrega());
        cv.put("CondicionPago", notacredito.getCondicionPago());
        cv.put("Indicador", notacredito.getIndicador());
        cv.put("SubTotal", notacredito.getSubTotal());
        cv.put("Descuento", notacredito.getDescuento());
        cv.put("Impuesto", notacredito.getImpuesto());
        cv.put("Total", notacredito.getTotal());
        cv.put("Saldo", notacredito.getSaldo());
        cv.put("EstadoMovil", notacredito.getEstadoMovil());
        cv.put("ClaveBase", notacredito.getClaveBase());
        cv.put("Latitud", notacredito.getLatitud());
        cv.put("Longitud", notacredito.getLongitud());
        cv.put("FechaCreacion", notacredito.getFechaCreacion());
        cv.put("HoraCreacion", notacredito.getHoraCreacion());
        cv.put("ModoOffline", notacredito.getModoOffline());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_NOTA_CREDITO", null, cv);

        if(inserto != -1){
            for (NotaCreditoDetalleBean linea: notacredito.getLineas()) {
                insertarLinea(linea);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLinea(NotaCreditoDetalleBean detalle){
        ContentValues cv = new ContentValues();
        cv.put("ClaveNotaCredito", detalle.getClaveNotaCredito());
        cv.put("Linea", detalle.getLinea());
        cv.put("LineaBase", detalle.getLineaBase());
        cv.put("Articulo", detalle.getArticulo());
        cv.put("UnidadMedida", detalle.getUnidadMedida());
        cv.put("Almacen", detalle.getAlmacen());
        cv.put("Cantidad", detalle.getCantidad());
        cv.put("ListaPrecio", detalle.getListaPrecio());
        cv.put("PrecioUnitario", detalle.getPrecioUnitario());
        cv.put("PorcentajeDescuento", detalle.getPorcentajeDescuento());
        cv.put("Impuesto", detalle.getImpuesto());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_NOTA_CREDITO_DETALLE", null, cv);

        if(inserto != -1){
            for (NotaCreditoDetalleLoteBean lote: detalle.getLotes()) {
                insertarLote(lote);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLote(NotaCreditoDetalleLoteBean lote) {
        ContentValues cv = new ContentValues();
        cv.put("ClaveNotaCredito", lote.getClaveBase());
        cv.put("Lote", lote.getLote());
        cv.put("Cantidad", lote.getCantidad());
        cv.put("LineaBase", lote.getLineaBase());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_NOTA_CREDITO_DETALLE_LOTE", null, cv);

        return inserto != -1;
    }

    public boolean actualizarSincronizado(String clave) {
        ContentValues cv = new ContentValues();
        cv.put("EstadoMovil", "U");

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase().update("TB_NOTA_CREDITO", cv, "ClaveMovil = ?",
                        new String[]{String.valueOf(clave)});
        return update > 0;
    }

    private NotaCreditoBean transformCursorToNotaCredito(Cursor cursor){
        NotaCreditoBean bean = new NotaCreditoBean();
        bean.setClave(cursor.getString(cursor.getColumnIndex("Clave")));
        bean.setClaveMovil(cursor.getString(cursor.getColumnIndex("ClaveMovil")));
        bean.setNumero(cursor.getInt(cursor.getColumnIndex("Numero")));
        bean.setReferencia(cursor.getString(cursor.getColumnIndex("Referencia")));
        bean.setSocioNegocio(cursor.getString(cursor.getColumnIndex("SocioNegocio")));
        bean.setSocioNegocioNombre(cursor.getString(cursor.getColumnIndex("NombreRazonSocial")));
        bean.setListaPrecio(cursor.getInt(cursor.getColumnIndex("ListaPrecio")));
        bean.setListaPrecioNombre(cursor.getString(cursor.getColumnIndex("ListaPrecioNombre")));
        bean.setContacto(cursor.getInt(cursor.getColumnIndex("Contacto")));
        bean.setContactoNombre(cursor.getString(cursor.getColumnIndex("ContactoNombre")));
        bean.setMoneda(cursor.getString(cursor.getColumnIndex("Moneda")));
        bean.setEmpleadoVenta(cursor.getString(cursor.getColumnIndex("EmpleadoVenta")));
        bean.setComentario(cursor.getString(cursor.getColumnIndex("Comentario")));
        bean.setFechaContable(cursor.getString(cursor.getColumnIndex("FechaContable")));
        bean.setFechaVencimiento(cursor.getString(cursor.getColumnIndex("FechaVencimiento")));
        bean.setDireccionFiscal(cursor.getString(cursor.getColumnIndex("DireccionFiscal")));
        bean.setDireccionFiscalDescripcion(cursor.getString(cursor.getColumnIndex("DireccionFiscalDescripcion")));
        bean.setDireccionEntrega(cursor.getString(cursor.getColumnIndex("DireccionEntrega")));
        bean.setDireccionEntregaDescripcion(cursor.getString(cursor.getColumnIndex("DireccionEntregaDescripcion")));
        bean.setCondicionPago(cursor.getString(cursor.getColumnIndex("CondicionPago")));
        bean.setCondicionPagoNombre(cursor.getString(cursor.getColumnIndex("CondicionPagoNombre")));
        bean.setIndicador(cursor.getString(cursor.getColumnIndex("Indicador")));
        bean.setIndicadorNombre(cursor.getString(cursor.getColumnIndex("IndicadorNombre")));
        bean.setSubTotal(cursor.getString(cursor.getColumnIndex("SubTotal")));
        bean.setDescuento(cursor.getString(cursor.getColumnIndex("Descuento")));
        bean.setImpuesto(cursor.getString(cursor.getColumnIndex("Impuesto")));
        bean.setTotal(cursor.getString(cursor.getColumnIndex("Total")));
        bean.setSaldo(cursor.getString(cursor.getColumnIndex("Saldo")));
        bean.setLineas(listarDetalle(bean.getClave()));
        bean.setClaveBase(cursor.getString(cursor.getColumnIndex("ClaveBase")));
        bean.setEstadoMovil(cursor.getString(cursor.getColumnIndex("EstadoMovil")));
        return bean;
    }

    private NotaCreditoDetalleBean transformCursorToNotaCreditoDetalle(Cursor cursor){
        NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
        detalle.setLinea(cursor.getString(cursor.getColumnIndex("Linea")));
        detalle.setLineaBase(cursor.getString(cursor.getColumnIndex("LineaBase")));
        detalle.setArticulo(cursor.getString(cursor.getColumnIndex("Articulo")));
        detalle.setArticuloNombre(cursor.getString(cursor.getColumnIndex("ArticuloNombre")));
        detalle.setUnidadMedida(cursor.getString(cursor.getColumnIndex("UnidadMedida")));
        detalle.setAlmacen(cursor.getString(cursor.getColumnIndex("Almacen")));
        detalle.setAlmacenNombre(cursor.getString(cursor.getColumnIndex("AlmacenNombre")));
        detalle.setCantidad(cursor.getString(cursor.getColumnIndex("Cantidad")));
        detalle.setListaPrecio(cursor.getInt(cursor.getColumnIndex("ListaPrecio")));
        detalle.setPrecioUnitario(cursor.getString(cursor.getColumnIndex("PrecioUnitario")));
        detalle.setPorcentajeDescuento(cursor.getString(cursor.getColumnIndex("PorcentajeDescuento")));
        detalle.setImpuesto(cursor.getString(cursor.getColumnIndex("Impuesto")));
        detalle.setLotes(listarDetalleLotes(cursor.getString(cursor.getColumnIndex("ClaveNotaCredito")),
                Integer.parseInt(detalle.getLinea())));
        return detalle;
    }

    private NotaCreditoDetalleLoteBean transformCursorToNotaCreditoDetalleLote(Cursor cursor){
        NotaCreditoDetalleLoteBean detalle = new NotaCreditoDetalleLoteBean();
        detalle.setClaveBase(cursor.getString(cursor.getColumnIndex("ClaveNotaCredito")));
        detalle.setCantidad(cursor.getDouble(cursor.getColumnIndex("Cantidad")));
        detalle.setLote(cursor.getString(cursor.getColumnIndex("Lote")));
        detalle.setLineaBase(cursor.getInt(cursor.getColumnIndex("LineaBase")));
        return detalle;
    }

}
