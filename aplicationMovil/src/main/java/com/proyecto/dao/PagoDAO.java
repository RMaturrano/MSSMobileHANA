package com.proyecto.dao;


import com.proyecto.database.DataBaseHelper;

public class PagoDAO {

    public void eliminarPagosNoLocales(){
        try{
            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_PAGO where EstadoMovil <> 'L'");
            DataBaseHelper.getHelper(null)
                    .getDataBase()
                    .execSQL("delete from TB_PAGO_DETALLE where ClaveMovil not in (select Clave from TB_ORDEN_VENTA where EstadoMovil = 'L')");
        }catch (Exception e){

        }
    }

}
