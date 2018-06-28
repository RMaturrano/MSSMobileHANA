package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.database.DataBaseHelper;

public class CorrelativoDAO {

    public static int obtenerUltimoNumero(String tipoRegistro){
        int number = -1;

        try{
            Cursor rs= DataBaseHelper
                    .getHelper(null)
                    .getDataBase().rawQuery("select NUM_COR" +
                            " from TB_COR WHERE COD_COR ='"+tipoRegistro+"'", null);

            while (rs.moveToNext()) {
                number = rs.getInt(0);
            }
            rs.close();
        }catch(Exception e){
            return -1;
        }

        return number;
    }

    public static void updateCorrelativo(String tipoRegistro){

        try{
            DataBaseHelper
                    .getHelper(null)
                    .getDataBase().execSQL(" UPDATE TB_COR " +
                    "SET NUM_COR = NUM_COR + 1 " +
                    "WHERE COD_COR = '"+tipoRegistro+"' ");
        }catch (Exception e){

        }
    }

}
