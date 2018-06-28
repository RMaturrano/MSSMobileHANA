package com.proyecto.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.proyect.movil.R;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.TipoClienteRegistroBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;

import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {

    private String generalSelectQuery = "select T0.Codigo, " +
            "T0.NombreRazonSocial, " +
            "T0.TelefonoMovil, " +
            "T0.NumeroDocumento," +
            "IFNULL(T1.Codigo,'') AS ListaPrecioCodigo," +
            "IFNULL(T1.Nombre,'') AS ListaPrecioNombre," +
            "IFNULL(T2.CODIGO,'') AS CondPagoCodigo," +
            "IFNULL(T2.NOMBRE,'') AS CondPagoNombre," +
            "IFNULL(T3.Codigo,'') AS IndicadorCodigo," +
            "IFNULL(T3.Nombre,'') AS IndicadorNombre, " +
            "IFNULL(T4.Codigo,'') AS DireccionFiscalCodigo, " +
            "IFNULL(IFNULL(T4.Calle,T4.Referencia),'') AS DireccionFiscalNombre " +
            " FROM TB_SOCIO_NEGOCIO T0 " +
            " LEFT JOIN TB_LISTA_PRECIO T1 ON T0.ListaPrecio = T1.Codigo" +
            " LEFT JOIN TB_CONDICION_PAGO T2 ON T0.CondicionPago = T2.CODIGO " +
            " LEFT JOIN TB_INDICADOR T3 ON T0.Indicador = T3.Codigo " +
            " LEFT JOIN TB_SOCIO_NEGOCIO_DIRECCION T4 ON T0.Codigo = T4.CodigoSocioNegocio " +
            "       AND T4.Tipo IS NOT NULL AND T4.Tipo = 'B' ";

    public static List<TipoClienteRegistroBean> getTipoRegistro(){
        List<TipoClienteRegistroBean> tipos = new ArrayList<>();
        tipos.add(new TipoClienteRegistroBean("01","Lead"));
        tipos.add(new TipoClienteRegistroBean("02","Final"));
        tipos.add(new TipoClienteRegistroBean("03","Competencia"));
        return tipos;
    }

    public boolean actualizarEstadoMovil(String codigo, String estado){
        ContentValues cv = new ContentValues();
        cv.put("EstadoMovil",estado);

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .update("TB_SOCIO_NEGOCIO", cv,"ClaveMovil = ?",
                        new String[]{codigo});

        return update > 0;
    }

    public List<ClienteBuscarBean> listarParaBusqueda() {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery(generalSelectQuery, null);

        List<ClienteBuscarBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToClienteBuscar(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    public ClienteBuscarBean buscarPorCodigo(String codigo){
        ClienteBuscarBean bean = null;

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery(generalSelectQuery + " where T0.Codigo = '" + codigo + "'",
                        null);

        if (cursor.moveToFirst()) {
            do {
                bean = transformCursorToClienteBuscar(cursor);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return bean;
    }

    private ClienteBuscarBean transformCursorToClienteBuscar(Cursor cursor){
        ClienteBuscarBean bean = new ClienteBuscarBean();
        bean.setCodigo(cursor.getString(cursor.getColumnIndex("Codigo")));
        bean.setNombre(cursor.getString(cursor.getColumnIndex("NombreRazonSocial")));
        bean.setTelefono(cursor.getString(cursor.getColumnIndex("TelefonoMovil")));
        bean.setNumeroDocumento(cursor.getString(cursor.getColumnIndex("NumeroDocumento")));
        bean.setDireccionFiscalCodigo(cursor.getString(cursor.getColumnIndex("DireccionFiscalCodigo")));
        bean.setDireccionFiscalNombre(cursor.getString(cursor.getColumnIndex("DireccionFiscalNombre")));
        bean.setContactos(new ContactoDAO().listar(bean.getCodigo()));
        bean.setDirecciones(new DireccionDAO().listar(bean.getCodigo()));

        String codigoListaPrecio = cursor.getString(cursor.getColumnIndex("ListaPrecioCodigo"));
        if(!codigoListaPrecio.equals("")){
            ListaPrecioBean listaPrecioBean = new ListaPrecioBean();
            listaPrecioBean.setCodigo(codigoListaPrecio);
            listaPrecioBean.setNombre(cursor.getString(cursor.getColumnIndex("ListaPrecioNombre")));
            bean.setListaPrecio(listaPrecioBean);
        }

        String codigoCondPago = cursor.getString(cursor.getColumnIndex("CondPagoCodigo"));
        if(!codigoCondPago.equals("")){
            CondicionPagoBean condicionPagoBean = new CondicionPagoBean();
            condicionPagoBean.setNumeroCondicion(codigoCondPago);
            condicionPagoBean.setDescripcionCondicion(cursor.getString(cursor.getColumnIndex("CondPagoNombre")));
            bean.setCondicionPago(condicionPagoBean);
        }

        String codigoIndicador = cursor.getString(cursor.getColumnIndex("IndicadorCodigo"));
        if(!codigoIndicador.equals("")){
            IndicadorBean indicadorBean = new IndicadorBean();
            indicadorBean.setCodigo(codigoIndicador);
            indicadorBean.setNombre(cursor.getString(cursor.getColumnIndex("IndicadorNombre")));
            bean.setIndicador(indicadorBean);
        }

        return bean;
    }

}
