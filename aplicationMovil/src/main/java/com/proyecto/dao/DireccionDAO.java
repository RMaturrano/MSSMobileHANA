package com.proyecto.dao;

import android.content.ContentValues;

import com.proyecto.bean.DireccionBean;
import com.proyecto.database.DataBaseHelper;

/**
 * Created by PC on 05/02/2018.
 */

public class DireccionDAO {

    public boolean update(DireccionBean direccionBean) {
        ContentValues cv = new ContentValues();
        cv.put("CodigoSocioNegocio", direccionBean.getCodigoCliente());
        cv.put("Codigo", direccionBean.getIDDireccion());
        cv.put("Latitud", direccionBean.getLatitud());
        cv.put("Longitud", direccionBean.getLongitud());

        int update = DataBaseHelper
                .getHelper(null)
                .getDataBase()
                .update("TB_SOCIO_NEGOCIO_DIRECCION", cv,"Codigo = ? AND CodigoSocioNegocio = ?",
                        new String[]{direccionBean.getIDDireccion(), direccionBean.getCodigoCliente()});
        return update > 0;
    }
}
