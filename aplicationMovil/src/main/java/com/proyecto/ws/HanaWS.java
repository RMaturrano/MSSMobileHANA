package com.proyecto.ws;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.proyecto.bean.EmpresaBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HanaWS {

    private Context contexto;
    private String ws_ruta = "http://192.168.1.52:8000/MSS_MOBILE/service/company/getCompany.xsjs";
    private RequestQueue queue;

    public  HanaWS(Context contexto){
        this.contexto = contexto;
        queue = VolleySingleton.getInstance(this.contexto).getRequestQueue();
    }

    public List<EmpresaBean> getCompanyList(){

        final List<EmpresaBean> mList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ws_ruta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int size = jsonArray.length();

                            for (int i = 0; i < size; i++ ){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                EmpresaBean bean = new EmpresaBean();
                                bean.setId(jsonObj.getInt("id"));
                                bean.setDescripcion(jsonObj.getString("descripcion"));
                                bean.setBaseDatos(jsonObj.getString("base_datos"));
                                bean.setUsuario(jsonObj.getString("usuario"));
                                bean.setClave(jsonObj.getString("clave"));
                                bean.setEstado(jsonObj.getString("estado"));
                                bean.setObservacion(jsonObj.getString("observacion"));

                                mList.add(bean);
                            }
                        } catch (Exception e){
                            mList.clear();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mList.clear();
            }
        });

        VolleySingleton.getInstance(contexto).addToRequestQueue(stringRequest);

        return  mList;
    }

}
