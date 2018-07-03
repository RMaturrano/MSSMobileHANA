package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.FacturaDetalleLoteBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public List<EntregaBean> listar(String socioNegocio){

        List<EntregaBean> listEntrega = new ArrayList<>();
        String whereArg0 = socioNegocio == null ? "" :
                " WHERE T0.SocioNegocio = '" + socioNegocio + "'";

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Clave, " +
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
                        "IFNULL((SELECT X0.Latitud FROM TB_SOCIO_NEGOCIO_DIRECCION X0" +
                        "   WHERE X0.Codigo = T0.DireccionEntrega AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
                        "   AS DireccionEntregaLatitud ," +
                        "IFNULL((SELECT X0.Longitud FROM TB_SOCIO_NEGOCIO_DIRECCION X0" +
                        "   WHERE X0.Codigo = T0.DireccionEntrega AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
                        "   AS DireccionEntregaLongitud ," +
                        "T0.CondicionPago," +
                        "IFNULL(T3.NOMBRE,'') AS CondicionPagoNombre," +
                        "T0.Indicador," +
                        "IFNULL(T4.Nombre,'') AS IndicadorNombre," +
                        "T0.SubTotal," +
                        "T0.Descuento," +
                        "T0.Impuesto," +
                        "T0.Total," +
                        "T0.Saldo from TB_ENTREGA T0 LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO_CONTACTO T2 ON T0.Contacto = T2.Codigo " +
                            " AND T2.CodigoSocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_CONDICION_PAGO T3 ON T3.CODIGO = T0.CondicionPago LEFT JOIN " +
                        " TB_INDICADOR T4 ON T4.Codigo = T0.Indicador LEFT JOIN " +
                        " TB_LISTA_PRECIO T5 ON T0.ListaPrecio = T5.Codigo " + whereArg0, null);

        if (cursor.moveToFirst()) {
            do {
                listEntrega.add(transformCursorToEntrega(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  listEntrega;
    }

    public List<EntregaDetalleBean> listarDetalle(int claveEntrega){
        List<EntregaDetalleBean> listEntrega = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.Linea, " +
                        "T0.Articulo, " +
                        "T1.Nombre as ArticuloNombre, " +
                        "T0.UnidadMedida, " +
                        "T0.Almacen, " +
                        "IFNULL(T2.NOMBRE,'') AS AlmacenNombre, " +
                        "T0.Cantidad," +
                        "T0.Disponible," +
                        "T0.ListaPrecio," +
                        "T0.PrecioUnitario," +
                        "T0.PorcentajeDescuento," +
                        "T0.Impuesto, " +
                        "T0.ClaveEntrega from TB_ENTREGA_DETALLE T0 LEFT JOIN " +
                        " TB_ARTICULO T1 ON T0.Articulo = T1.Codigo LEFT JOIN " +
                        " TB_ALMACEN T2 ON T0.Almacen = T2.CODIGO " +
                        " where T0.ClaveEntrega = " +claveEntrega, null);

        if (cursor.moveToFirst()) {
            do {
                listEntrega.add(transformCursorToEntregaDetalle(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  listEntrega;
    }

    public List<EntregaDetalleLoteBean> listarDetalleLotes(int claveEntrega, int numLinea){
        List<EntregaDetalleLoteBean> listEntrega = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.ClaveEntrega, " +
                        "T0.Lote, " +
                        "IFNULL(T0.Cantidad,0) as Cantidad, " +
                        "T0.LineaBase from TB_ENTREGA_DETALLE_LOTE  T0 " +
                        " where T0.ClaveEntrega = " +claveEntrega  +
                        " and T0.LineaBase = " + numLinea, null);

        if (cursor.moveToFirst()) {
            do {
                listEntrega.add(transformCursorToEntregaDetalleLote(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  listEntrega;
    }

    public boolean insertar(EntregaBean entrega) {
        ContentValues cv = new ContentValues();
        cv.put("Tipo", entrega.getTipo());
        cv.put("Clave", entrega.getClave());
        cv.put("Numero", entrega.getNumero());
        cv.put("Referencia", entrega.getReferencia());
        cv.put("SocioNegocio", entrega.getSocioNegocio());
        cv.put("ListaPrecio", entrega.getListaPrecio());
        cv.put("Contacto", entrega.getContacto());
        cv.put("Moneda", entrega.getMoneda());
        cv.put("EmpleadoVenta", entrega.getEmpleadoVenta());
        cv.put("Comentario", entrega.getComentario());
        cv.put("FechaContable", entrega.getFechaContable());
        cv.put("FechaVencimiento", entrega.getFechaVencimiento());
        cv.put("DireccionFiscal", entrega.getDireccionFiscal());
        cv.put("DireccionEntrega", entrega.getDireccionEntrega());
        cv.put("CondicionPago", entrega.getCondicionPago());
        cv.put("Indicador", entrega.getIndicador());
        cv.put("SubTotal", entrega.getSubTotal());
        cv.put("Descuento", entrega.getDescuento());
        cv.put("Impuesto", entrega.getImpuesto());
        cv.put("Total", entrega.getTotal());
        cv.put("Saldo", entrega.getSaldo());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_ENTREGA", null, cv);

        if(inserto != -1){
            for (EntregaDetalleBean linea: entrega.getLineas()) {
                insertarLinea(linea);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLinea(EntregaDetalleBean detalle){
        ContentValues cv = new ContentValues();
        cv.put("ClaveEntrega", detalle.getClaveEntrega());
        cv.put("Linea", detalle.getLinea());
        cv.put("Articulo", detalle.getArticulo());
        cv.put("UnidadMedida", detalle.getUnidadMedida());
        cv.put("Almacen", detalle.getAlmacen());
        cv.put("Cantidad", detalle.getCantidad());
        cv.put("Disponible", detalle.getDiponible());
        cv.put("ListaPrecio", detalle.getListaPrecio());
        cv.put("PrecioUnitario", detalle.getPrecioUnitario());
        cv.put("PorcentajeDescuento", detalle.getPorcentajeDescuento());
        cv.put("Impuesto", detalle.getImpuesto());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_ENTREGA_DETALLE", null, cv);

        if(inserto != -1){
            for (EntregaDetalleLoteBean lote: detalle.getLotes()) {
                insertarLote(lote);
            }
        }

        return inserto != -1;
    }

    public boolean insertarLote(EntregaDetalleLoteBean lote) {
        ContentValues cv = new ContentValues();
        cv.put("ClaveEntrega", lote.getClaveBase());
        cv.put("Lote", lote.getLote());
        cv.put("Cantidad", lote.getCantidad());
        cv.put("LineaBase", lote.getLineaBase());

        long inserto = DataBaseHelper
                .getHelper(null)
                .getDataBase().insert("TB_ENTREGA_DETALLE_LOTE", null, cv);

        return inserto != -1;
    }

    public boolean deleteAll(){
        boolean cabe = DataBaseHelper
                .getHelper(null)
                .getDataBase().delete("TB_ENTREGA",null,null) > 0;

        boolean detail = DataBaseHelper
                .getHelper(null)
                .getDataBase().delete("TB_ENTREGA_DETALLE",null,null) > 0;

        boolean detailLote = DataBaseHelper
                .getHelper(null)
                .getDataBase().delete("TB_ENTREGA_DETALLE_LOTE",null,null) > 0;

        return cabe && detail && detailLote;
    }

    private EntregaBean transformCursorToEntrega(Cursor cursor){
        EntregaBean bean = new EntregaBean();
        bean.setClave(cursor.getInt(cursor.getColumnIndex("Clave")));
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
        bean.setDireccionEntregaLatitud(cursor.getString(cursor.getColumnIndex("DireccionEntregaLatitud")));
        bean.setDireccionEntregaLongitud(cursor.getString(cursor.getColumnIndex("DireccionEntregaLongitud")));
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
        return bean;
    }

    private EntregaDetalleBean transformCursorToEntregaDetalle(Cursor cursor){
        EntregaDetalleBean detalle = new EntregaDetalleBean();
        detalle.setLinea(cursor.getInt(cursor.getColumnIndex("Linea")));
        detalle.setArticulo(cursor.getString(cursor.getColumnIndex("Articulo")));
        detalle.setArticuloNombre(cursor.getString(cursor.getColumnIndex("ArticuloNombre")));
        detalle.setUnidadMedida(cursor.getString(cursor.getColumnIndex("UnidadMedida")));
        detalle.setAlmacen(cursor.getString(cursor.getColumnIndex("Almacen")));
        detalle.setAlmacenNombre(cursor.getString(cursor.getColumnIndex("AlmacenNombre")));
        detalle.setCantidad(cursor.getString(cursor.getColumnIndex("Cantidad")));
        detalle.setDiponible(cursor.getString(cursor.getColumnIndex("Disponible")));
        detalle.setListaPrecio(cursor.getInt(cursor.getColumnIndex("ListaPrecio")));
        detalle.setPrecioUnitario(cursor.getString(cursor.getColumnIndex("PrecioUnitario")));
        detalle.setPorcentajeDescuento(cursor.getString(cursor.getColumnIndex("PorcentajeDescuento")));
        detalle.setImpuesto(cursor.getString(cursor.getColumnIndex("Impuesto")));
        detalle.setLotes(listarDetalleLotes(cursor.getInt(cursor.getColumnIndex("ClaveEntrega")),
                                            detalle.getLinea()));
        return detalle;
    }

    private EntregaDetalleLoteBean transformCursorToEntregaDetalleLote(Cursor cursor){
        EntregaDetalleLoteBean detalle = new EntregaDetalleLoteBean();
        detalle.setClaveBase(cursor.getInt(cursor.getColumnIndex("ClaveEntrega")));
        detalle.setCantidad(cursor.getDouble(cursor.getColumnIndex("Cantidad")));
        detalle.setCantidadTemp(detalle.getCantidad());
        detalle.setLote(cursor.getString(cursor.getColumnIndex("Lote")));
        detalle.setLineaBase(cursor.getInt(cursor.getColumnIndex("LineaBase")));
        return detalle;
    }

    public FacturaBean transformEntregaToFactura(EntregaBean entrega){
        FacturaBean factura = new FacturaBean();
        factura.setTipo(entrega.getTipo());
        factura.setClave(String.valueOf(entrega.getClave()));
        factura.setNumero(String.valueOf(entrega.getNumero()));
        factura.setReferencia(entrega.getReferencia());
        factura.setSocioNegocio(entrega.getSocioNegocio());
        factura.setSocioNegocioNombre(entrega.getSocioNegocioNombre());
        factura.setNombreSocio(entrega.getSocioNegocioNombre());
        factura.setListaPrecio(String.valueOf(entrega.getListaPrecio()));
        factura.setContacto(String.valueOf(entrega.getContacto()));
        factura.setMoneda(entrega.getMoneda());
        factura.setEmpleadoVenta(entrega.getEmpleadoVenta());
        factura.setComentario(entrega.getComentario());
        factura.setFechaContable(entrega.getFechaContable());
        factura.setFechaDocumento(entrega.getFechaContable());
        factura.setFechaVencimiento(entrega.getFechaVencimiento());
        factura.setDireccionFiscal(entrega.getDireccionFiscal());
        factura.setDireccionEntrega(entrega.getDireccionEntrega());
        factura.setCondicionPago(entrega.getCondicionPago());
        factura.setIndicador(entrega.getIndicador());
        factura.setSubTotal(entrega.getSubTotal());
        factura.setDescuento(entrega.getDescuento());
        factura.setImpuesto(entrega.getImpuesto());
        factura.setTotal(entrega.getTotal());
        factura.setSaldo(entrega.getSaldo());
        factura.setListaPrecioNombre(entrega.getListaPrecioNombre());
        factura.setContactoNombre(entrega.getContactoNombre());
        factura.setDireccionFiscalDescripcion(entrega.getDireccionFiscalDescripcion());
        factura.setDireccionEntregaDescripcion(entrega.getDireccionEntregaDescripcion());
        factura.setDireccionEntregaLatitud(entrega.getDireccionEntregaLatitud());
        factura.setDireccionEntregaLongitud(entrega.getDireccionEntregaLongitud());
        factura.setCondicionPagoNombre(entrega.getCondicionPagoNombre());
        factura.setIndicadorNombre(entrega.getIndicadorNombre());
        factura.setLineas( (ArrayList<FacturaDetalleBean>) transformEntregaDetalle(entrega.getLineas()));

        return factura;
    }

    public List<FacturaDetalleBean> transformEntregaDetalle(List<EntregaDetalleBean> lineas){
        List<FacturaDetalleBean> detalles = new ArrayList<>();

        for (EntregaDetalleBean e : lineas) {
            FacturaDetalleBean detalle = new FacturaDetalleBean();
            detalle.setLinea(e.getLinea());
            detalle.setArticulo(e.getArticulo());
            detalle.setArticuloNombre(e.getArticuloNombre());
            detalle.setUnidadMedida(e.getUnidadMedida());
            detalle.setAlmacen(e.getAlmacen());
            detalle.setAlmacenNombre(e.getAlmacenNombre());
            detalle.setCantidad(e.getCantidad());
            detalle.setDiponible(e.getDiponible());
            detalle.setListaPrecio(String.valueOf(e.getListaPrecio()));
            detalle.setPrecioUnitario(e.getPrecioUnitario());
            detalle.setPorcentajeDescuento(e.getPorcentajeDescuento());
            detalle.setImpuesto(e.getImpuesto());

            List<FacturaDetalleLoteBean> lotes = new ArrayList<>();
            for (EntregaDetalleLoteBean l : e.getLotes()) {
                FacturaDetalleLoteBean lote = new FacturaDetalleLoteBean();
                lote.setClaveBase(l.getClaveBase());
                lote.setCantidad(l.getCantidad());
                lote.setCantidadTemp(lote.getCantidad());
                lote.setLote(l.getLote());
                lote.setLineaBase(l.getLineaBase());
                lotes.add(lote);
            }
            detalle.setLotes(lotes);

            detalles.add(detalle);
        }

        return detalles;
    }
}