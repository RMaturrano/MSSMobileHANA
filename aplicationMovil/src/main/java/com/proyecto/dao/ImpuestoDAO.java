package com.proyecto.dao;

import android.database.Cursor;

import com.proyecto.database.DataBaseHelper;

public class ImpuestoDAO {

    public static double obtenerTasa(String taxCode){
        double number = 100;

        try{
            Cursor rs= DataBaseHelper
                    .getHelper(null)
                    .getDataBase().rawQuery("select IFNULL(Tasa,100)" +
                            " from TB_IMPUESTO WHERE Codigo ='"+ taxCode +"'", null);

            while (rs.moveToNext()) {
                number = rs.getInt(0);
            }
            rs.close();
        }catch(Exception e){
            return 100;
        }

        return number;
    }
}
