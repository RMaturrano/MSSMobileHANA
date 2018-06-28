package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyecto.bean.DevolucionBean;
import com.proyecto.bean.DevolucionDetalleBean;
import com.proyecto.bean.DevolucionDetalleLoteBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;


public class DevolucionDAO {

    public List<DevolucionBean> listar(String sincronizado){
        List<DevolucionBean> list = new ArrayList<>();

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
                        "T0.Saldo from TB_DEVOLUCION T0 LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO_CONTACTO T2 ON T0.Contacto = T2.Codigo " +
                        " AND T2.CodigoSocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_CONDICION_PAGO T3 ON T3.CODIGO = T0.CondicionPago LEFT JOIN " +
                        " TB_INDICADOR T4 ON T4.Codigo = T0.Indicador LEFT JOIN " +
                        " TB_LISTA_PRECIO T5 ON T0.ListaPrecio = T5.Codigo " +
                        whereArg, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToDevolucion(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return list;
    }

    public List<DevolucionDetalleBean> listarDetalle(String clave){
        List<DevolucionDetalleBean> list = new ArrayList<>();

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
                        "T0.ClaveDevolucion from TB_DEVOLUCION_DETALLE T0 LEFT JOIN " +
                        " TB_ARTICULO T1 ON T0.Articulo = T1.Codigo LEFT JOIN " +
                        " TB_ALMACEN T2 ON T0.Almacen = T2.CODIGO " +
                        " where T0.ClaveDevolucion = '" +clave + "'", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToDevolucionDetalle(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }

    public List<DevolucionDetalleLoteBean> listarDetalleLotes(String claveEntrega, int numLinea){
        List<DevolucionDetalleLoteBean> listEntrega = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.ClaveEntrega, " +
                        "T0.Lote, " +
                        "IFNULL(T0.Cantidad,0) as Cantidad, " +
                        "T0.LineaBase from TB_DEVOLUCION_DETALLE_LOTE  T0 " +
                        " where T0.ClaveEntrega = '" +claveEntrega + "' " +
                        " and T0.LineaBase = " + numLinea, null);

        if (cursor.moveToFirst()) {
            do {
                listEntrega.add(transformCursorToDevolucionDetalleLote(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  listEntrega;
    }

    public boolean insertDevolucion(DevolucionBean devolucion) {
        ContentValues cv = new ContentValues();
        cv.put("Clave", devolucion.getClave());
        cv.put("ClaveMovil", devolucion.getClaveMovil());
        cv.put("Referencia", devolucion.getReferencia());
        cv.put("SocioNegocio", devolucion.getSocioNegocio());
        cv.put("ListaPrecio", devolucion.getListaPrecio());
        cv.put("Contacto", devolucion.getContacto());
        cv.put("Moneda", devolucion.getMoneda());
        cv.put("EmpleadoVenta", devolucion.getEmpleadoVenta());
        cv.put("Comentario", devolucion.getComentario());
        cv.put("FechaContable", devolucion.getFechaContable());
        cv.put("FechaVencimiento", devolucion.getFechaVencimiento());
        cv.put("DireccionFiscal", devolucion.getDireccionFiscal());
        cv.put("DireccionEntrega", devolucion.getDireccionEntrega());
        cv.put("CondicionPago", devolucion.getCondicionPago());
        cv.put("Indicador", devolucion.getIndicador());
        cv.put("SubTotal", devolucion.getSubTotal());
        cv.put("Descuento", devolucion.getDescuento());
        cv.put("Impuesto", devolucion.getImpuesto());
        cv.put("Total", devolucion.getTotal());
        cv.put("Saldo", devolucion.getSaldo());
        cv.put("EstadoMovil", devolucion.getEstadoMovil());
        cv.put("ClaveBase", devolucion.getClaveBase());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_DEVOLUCION", null, cv);

        if(inserto != -1){
            for (DevolucionDetalleBean linea: devolucion.getLineas()) {
                insertarLinea(linea);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLinea(DevolucionDetalleBean detalle){
        ContentValues cv = new ContentValues();
        cv.put("ClaveDevolucion", detalle.getClaveDevolucion());
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
                .getDataBase().insert("TB_DEVOLUCION_DETALLE", null, cv);

        if(inserto != -1){
            for (DevolucionDetalleLoteBean lote: detalle.getLotes()) {
                insertarLote(lote);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLote(DevolucionDetalleLoteBean lote) {
        ContentValues cv = new ContentValues();
        cv.put("ClaveEntrega", lote.getClaveBase());
        cv.put("Lote", lote.getLote());
        cv.put("Cantidad", lote.getCantidad());
        cv.put("LineaBase", lote.getLineaBase());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_DEVOLUCION_DETALLE_LOTE", null, cv);

        return inserto != -1;
    }

    public boolean actualizarSincronizado(String clave) {
        ContentValues cv = new ContentValues();
        cv.put("EstadoMovil", "U");

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase().update("TB_DEVOLUCION", cv, "ClaveMovil = ?",
                        new String[]{String.valueOf(clave)});
        return update > 0;
    }

    private DevolucionBean transformCursorToDevolucion(Cursor cursor){
        DevolucionBean bean = new DevolucionBean();
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

    private DevolucionDetalleBean transformCursorToDevolucionDetalle(Cursor cursor){
        DevolucionDetalleBean detalle = new DevolucionDetalleBean();
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
        detalle.setLotes(listarDetalleLotes(cursor.getString(cursor.getColumnIndex("ClaveDevolucion")),
                Integer.parseInt(detalle.getLinea())));
        return detalle;
    }

    private DevolucionDetalleLoteBean transformCursorToDevolucionDetalleLote(Cursor cursor){
        DevolucionDetalleLoteBean detalle = new DevolucionDetalleLoteBean();
        detalle.setClaveBase(cursor.getString(cursor.getColumnIndex("ClaveEntrega")));
        detalle.setCantidad(cursor.getDouble(cursor.getColumnIndex("Cantidad")));
        detalle.setLote(cursor.getString(cursor.getColumnIndex("Lote")));
        detalle.setLineaBase(cursor.getInt(cursor.getColumnIndex("LineaBase")));
        return detalle;
    }
}
