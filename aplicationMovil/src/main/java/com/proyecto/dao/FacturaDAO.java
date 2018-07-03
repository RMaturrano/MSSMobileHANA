package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.FacturaDetalleLoteBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.facturas.util.FacturaBuscarBean;

import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public List<FacturaBean> listar(String socioNegocio){

        List<FacturaBean> list = new ArrayList<>();
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
                        "T0.Saldo from TB_FACTURA T0 LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO_CONTACTO T2 ON T0.Contacto = T2.Codigo " +
                        " AND T2.CodigoSocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_CONDICION_PAGO T3 ON T3.CODIGO = T0.CondicionPago LEFT JOIN " +
                        " TB_INDICADOR T4 ON T4.Codigo = T0.Indicador LEFT JOIN " +
                        " TB_LISTA_PRECIO T5 ON T0.ListaPrecio = T5.Codigo " + whereArg0, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToFactura(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }

    public FacturaBean buscar(String clave){

        FacturaBean list = new FacturaBean();

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
                        "T0.Saldo from TB_FACTURA T0 LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_SOCIO_NEGOCIO_CONTACTO T2 ON T0.Contacto = T2.Codigo " +
                        " AND T2.CodigoSocioNegocio = T1.Codigo LEFT JOIN " +
                        " TB_CONDICION_PAGO T3 ON T3.CODIGO = T0.CondicionPago LEFT JOIN " +
                        " TB_INDICADOR T4 ON T4.Codigo = T0.Indicador LEFT JOIN " +
                        " TB_LISTA_PRECIO T5 ON T0.ListaPrecio = T5.Codigo " +
                        " WHERE T0.Clave = " + clave, null);

        if (cursor.moveToFirst()) {
            do {
                list = transformCursorToFactura(cursor);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }

    public List<FacturaDetalleBean> listarDetalle(String claveFactura){
        List<FacturaDetalleBean> list = new ArrayList<>();

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
                        "T0.ClaveFactura from TB_FACTURA_DETALLE T0 LEFT JOIN " +
                        " TB_ARTICULO T1 ON T0.Articulo = T1.Codigo LEFT JOIN " +
                        " TB_ALMACEN T2 ON T0.Almacen = T2.CODIGO " +
                        " where T0.ClaveFactura = " +claveFactura, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToFacturaDetalle(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }

    public List<FacturaDetalleLoteBean> listarDetalleLotes(String claveFactura, int numLinea){
        List<FacturaDetalleLoteBean> list = new ArrayList<>();

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "T0.ClaveFactura, " +
                        "T0.Lote, " +
                        "IFNULL(T0.Cantidad,0) as Cantidad, " +
                        "T0.LineaBase from TB_FACTURA_DETALLE_LOTE  T0 " +
                        " where T0.ClaveFactura = " +claveFactura  +
                        " and T0.LineaBase = " + numLinea, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(transformCursorToFacturaDetalleLote(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return  list;
    }


    public List<FacturaBuscarBean> listarParaBusqueda(String codigoCliente) {

        String whereArg0 = codigoCliente != null ? " where T0.SocioNegocio= '" + codigoCliente + "'"
                :"";

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Clave, " +
                        "T0.SocioNegocio, " +
                        "T1.NombreRazonSocial, " +
                        "T0.Referencia, " +
                        "T0.FechaVencimiento," +
                        "T0.Total from TB_FACTURA T0 JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo" + whereArg0, null);

        List<FacturaBuscarBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToFacturaBuscar(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    public FacturaBuscarBean obtenerPorClave(String clave){
        FacturaBuscarBean bean = null;

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select T0.Clave, " +
                        "T0.SocioNegocio, " +
                        "T1.NombreRazonSocial, " +
                        "T0.Referencia, " +
                        "T0.FechaVencimiento," +
                        "T0.Total from TB_FACTURA T0 JOIN " +
                        " TB_SOCIO_NEGOCIO T1 ON T0.SocioNegocio = T1.Codigo " +
                        "where T0.Clave = '" + clave + "'", null);

        if (cursor.moveToFirst()) {
            do {
                bean = transformCursorToFacturaBuscar(cursor);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return bean;
    }

    private FacturaBuscarBean transformCursorToFacturaBuscar(Cursor cursor){
        FacturaBuscarBean bean = new FacturaBuscarBean();
        bean.setClave(cursor.getString(cursor.getColumnIndex("Clave")));
        bean.setCodigoCliente(cursor.getString(cursor.getColumnIndex("SocioNegocio")));
        bean.setNombreCliente(cursor.getString(cursor.getColumnIndex("NombreRazonSocial")));
        bean.setReferencia(cursor.getString(cursor.getColumnIndex("Referencia")));
        bean.setVencimiento(cursor.getString(cursor.getColumnIndex("FechaVencimiento")));
        bean.setTotal(cursor.getString(cursor.getColumnIndex("Total")));
        return bean;
    }

    public void eliminarFacturas(){
        try{
            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_FACTURA");

            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_FACTURA_DETALLE");
        }catch (Exception e){
        }
    }



    private FacturaBean transformCursorToFactura(Cursor cursor){
        FacturaBean bean = new FacturaBean();
        bean.setClave(cursor.getString(cursor.getColumnIndex("Clave")));
        bean.setNumero(cursor.getString(cursor.getColumnIndex("Numero")));
        bean.setReferencia(cursor.getString(cursor.getColumnIndex("Referencia")));
        bean.setSocioNegocio(cursor.getString(cursor.getColumnIndex("SocioNegocio")));
        bean.setSocioNegocioNombre(cursor.getString(cursor.getColumnIndex("NombreRazonSocial")));
        bean.setListaPrecio(cursor.getString(cursor.getColumnIndex("ListaPrecio")));
        bean.setListaPrecioNombre(cursor.getString(cursor.getColumnIndex("ListaPrecioNombre")));
        bean.setContacto(cursor.getString(cursor.getColumnIndex("Contacto")));
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
        bean.setLineas( (ArrayList<FacturaDetalleBean>) listarDetalle(bean.getClave()));
        return bean;
    }

    private FacturaDetalleBean transformCursorToFacturaDetalle(Cursor cursor){
        FacturaDetalleBean detalle = new FacturaDetalleBean();
        detalle.setLinea(cursor.getInt(cursor.getColumnIndex("Linea")));
        detalle.setArticulo(cursor.getString(cursor.getColumnIndex("Articulo")));
        detalle.setArticuloNombre(cursor.getString(cursor.getColumnIndex("ArticuloNombre")));
        detalle.setUnidadMedida(cursor.getString(cursor.getColumnIndex("UnidadMedida")));
        detalle.setAlmacen(cursor.getString(cursor.getColumnIndex("Almacen")));
        detalle.setAlmacenNombre(cursor.getString(cursor.getColumnIndex("AlmacenNombre")));
        detalle.setCantidad(cursor.getString(cursor.getColumnIndex("Cantidad")));
        detalle.setDiponible(cursor.getString(cursor.getColumnIndex("Disponible")));
        detalle.setListaPrecio(cursor.getString(cursor.getColumnIndex("ListaPrecio")));
        detalle.setPrecioUnitario(cursor.getString(cursor.getColumnIndex("PrecioUnitario")));
        detalle.setPorcentajeDescuento(cursor.getString(cursor.getColumnIndex("PorcentajeDescuento")));
        detalle.setImpuesto(cursor.getString(cursor.getColumnIndex("Impuesto")));
        detalle.setLotes(listarDetalleLotes(cursor.getString(cursor.getColumnIndex("ClaveFactura")),
                detalle.getLinea()));
        return detalle;
    }

    private FacturaDetalleLoteBean transformCursorToFacturaDetalleLote(Cursor cursor){
        FacturaDetalleLoteBean detalle = new FacturaDetalleLoteBean();
        detalle.setClaveBase(cursor.getInt(cursor.getColumnIndex("ClaveFactura")));
        detalle.setCantidad(cursor.getDouble(cursor.getColumnIndex("Cantidad")));
        detalle.setCantidadTemp(detalle.getCantidad());
        detalle.setLote(cursor.getString(cursor.getColumnIndex("Lote")));
        detalle.setLineaBase(cursor.getInt(cursor.getColumnIndex("LineaBase")));
        return detalle;
    }

}
