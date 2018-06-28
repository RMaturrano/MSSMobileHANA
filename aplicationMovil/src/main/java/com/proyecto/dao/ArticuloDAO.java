package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.bean.ArticuloBean;
import com.proyecto.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    public List<ArticuloBean> listar(String listaPrecio) {

        String filterPriceList = "";

        if(listaPrecio != null  && !listaPrecio.equals(""))
            filterPriceList = " AND P.CodigoLista = " + listaPrecio;

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "A.Codigo, " +
                        "A.Nombre," +
                        "(select IFNULL(SUM(CAST(STOCK AS NUMERIC)),0) from TB_CANTIDAD where ARTICULO = A.Codigo), " +
                        "G.NOMBRE "
                        + "from TB_ARTICULO A join TB_GRUPO_ARTICULO G " +
                        "ON A.GrupoArticulo = G.CODIGO left join TB_PRECIO P on " +
                        " P.Articulo = A.Codigo AND P.CodigoLista IN(SELECT X0.ListaPrecio from TB_SOCIO_NEGOCIO X0) "+
                        filterPriceList +
                        " GROUP BY A.Codigo, A.Nombre  " +
                        "  having SUM(P.PrecioVenta) > 0 " +
                        " order by G.NOMBRE,A.Nombre", null);

        List<ArticuloBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToArticulo(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    public List<ArticuloBean> listarPorPrecio(String listaPrecio) {

        Cursor cursor = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .rawQuery("select " +
                        "A.Codigo, " +
                        "A.Nombre," +
                        "G.NOMBRE as Grupo, " +
                        "IFNULL(P.PrecioVenta, '0') as Precio "
                        + "from TB_ARTICULO A join TB_GRUPO_ARTICULO G " +
                        "ON A.GrupoArticulo = G.CODIGO left join TB_PRECIO P on " +
                        " P.Articulo = A.Codigo "+
                        " where P.CodigoLista = " + listaPrecio +
                        " AND CAST(IFNULL(P.PrecioVenta, '0') AS DECIMAL) > 0"+
                        " order by G.NOMBRE,A.Nombre " , null);

        List<ArticuloBean> lst = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                lst.add(transformCursorToArticuloxPrecio(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return lst;
    }

    private ArticuloBean transformCursorToArticulo(Cursor cursor){
        ArticuloBean bean = new ArticuloBean();
        bean.setCod(cursor.getString(0));
        bean.setDesc(cursor.getString(1));
        bean.setStock(cursor.getString(2));
        bean.setGrupoArticulo(cursor.getString(3));
        return bean;
    }

    private ArticuloBean transformCursorToArticuloxPrecio(Cursor cursor){
        ArticuloBean bean = new ArticuloBean();
        bean.setCod(cursor.getString(cursor.getColumnIndex("Codigo")));
        bean.setDesc(cursor.getString(cursor.getColumnIndex("Nombre")));
        bean.setPre(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Precio"))));
        bean.setNombreGrupoArt(cursor.getString(cursor.getColumnIndex("Grupo")));
        return bean;
    }

}
