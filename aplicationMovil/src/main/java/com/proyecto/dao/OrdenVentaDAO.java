package com.proyecto.dao;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.proyecto.database.DataBaseHelper;

public class OrdenVentaDAO {

    public void validarOrdenesLocales(){
        try{

            Cursor cursor = DataBaseHelper
                    .getHelper(null)
                    .getDataBase()
                    .rawQuery("SELECT ClaveMovil, count(Clave) as Qty " +
                            "FROM TB_ORDEN_VENTA GROUP BY ClaveMovil " +
                            "having Qty > 2", null);

            if(cursor.moveToFirst()){
                do{
                    String claveMovil = cursor.getString(cursor.getColumnIndex("ClaveMovil"));
                    eliminarOrdenVentaLocal(claveMovil);
                }while (cursor.moveToNext());
            }

        }catch(Exception e){

        }
    }

    public void eliminarOrdenVentaLocal(String claveMovil){
        try{

            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .delete("TB_ORDEN_VENTA", "ClaveMovil = ? AND EstadoMovil = 'L'", new String[]{claveMovil});

        }catch(Exception e){

        }
    }

    public void eliminarOrdenesNoLocales(){
        try{
            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_ORDEN_VENTA where EstadoMovil <> 'L'");
            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_ORDEN_VENTA_DETALLE where ClaveMovil not in (select Clave from TB_ORDEN_VENTA where EstadoMovil = 'L')");
        }catch (Exception e){

        }
    }

}
