package com.proyecto.servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.CantidadBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.PrecioBean;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncRestMaestros {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Insert mInsert;
    private Select mSelect;
    private SharedPreferences mSharedPreferences;
    private int MY_SOCKET_TIMEOUT_MS = 50000;

    public SyncRestMaestros(Context contexto, ProgressDialog progressDialog){
        mProgressDialog = progressDialog;
        mContext = contexto;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public boolean syncFromServer(){

        boolean result = true;

        try {

            mInsert = new Insert(mContext);
            mSelect = new Select(mContext);

            String ip = mSharedPreferences.getString("ipServidor", "172.16.7.51");
            String port = mSharedPreferences.getString("puertoServidor", "8000");
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";
            String codigoEmpleado = mSharedPreferences.getString(Variables.CODIGO_EMPLEADO, "-1");
            String esSupervisor = mSharedPreferences.getString(Variables.SUPERVISOR, "N");
            String esCobrador = mSharedPreferences.getString(Variables.COBRADOR, "N");

            //region POST SOCIOS DE NEGOCIO

            //region socios_nuevos
            List<SocioNegocioBean> listToSend = mSelect.listaSocioNegocio();

            if(listToSend.size() > 0){
                mProgressDialog.setMessage("Enviando socios de negocio...");

                for (final SocioNegocioBean sn : listToSend){
                    try {

                        JSONObject jsonObject = SocioNegocioBean.transformBPToJSON(sn, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "businesspartner/addBusinessPartnerLead.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                        mInsert.updateEstadoSocioNegocio(sn.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }

                                                }catch (Exception e){}
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {}
                                        }){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String, String> headers = new HashMap<String, String>();
                                        headers.put("Content-Type", "application/json; charset=utf-8");
                                        return headers;
                                    }
                                };
                        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

                    }catch (Exception ex){
                        showToast("Enviando socios de negocio: " + ex.getMessage());
                    }
                }
            }
            //endregion
            //region socios_por_actualizar

            //endregion

            //endregion

            //region REQUEST SOCIOS DE NEGOCIO
            mProgressDialog.setMessage("Registrando socios de negocio...");
            String urlGetBP = esCobrador.equals("N") ? "getBusinessPartner" : "getBusinessPartnerDispatcher";
            JsonObjectRequest mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "businesspartner/"+urlGetBP+".xsjs?empId=" + sociedad +"&cove=" + codigoEmpleado, null,
                    listenerGetSocios, errorListenerGetSocios);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ARTICULOS
            mProgressDialog.setMessage("Registrando articulos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "item/getItem.xsjs?empId=" + sociedad, null,
                    listenerGetItem, errorListenerGetItem);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ALMACENES
            mProgressDialog.setMessage("Registrando almacenes...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "warehouse/getWarehouse.xsjs?empId=" + sociedad + "&uid=" +codigoEmpleado, null,
                    listenerGetAlmacen, errorListenerGetAlmacen);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST CANTIDADES
            mProgressDialog.setMessage("Registrando cantidades...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "stock/getStock.xsjs?empId=" + sociedad + "&usrId=" + codigoEmpleado, null,
                    listenerGetCantidad, errorListenerGetCantidad);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST LISTAS DE PRECIO
            mProgressDialog.setMessage("Registrando listas de precio...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "pricelist/getPriceList.xsjs?empId=" + sociedad, null,
                    listenerGetListaPrecio, errorListenerGetListaPrecio);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST PRECIOS
            mProgressDialog.setMessage("Registrando precios...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "price/getPrices.xsjs?empId=" + sociedad, null,
                    listenerGetPrecios, errorListenerGetPrecios);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

        }catch (Exception e){
            result = false;
        }

        return result;
    }

    //region RESPONSE SOCIOS DE NEGOCIO
    Response.Listener listenerGetSocios = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    ArrayList<SocioNegocioBean> lstResults = new ArrayList<>();
                    SocioNegocioBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new SocioNegocioBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setTipoCliente(jsonObj.getString("TipoSocio"));
                        bean.setTipoPersona(jsonObj.getString("TipoPersona"));
                        bean.setTipoDoc(jsonObj.getString("TipoDocumento"));
                        bean.setNroDoc(jsonObj.getString("NumeroDocumento"));
                        bean.setNombRazSoc(jsonObj.getString("NombreRazonSocial"));
                        bean.setNomCom(jsonObj.getString("NombreComercial"));
                        bean.setApeMat(jsonObj.getString("ApellidoPaterno"));
                        bean.setApePat(jsonObj.getString("ApellidoMaterno"));
                        bean.setPriNom(jsonObj.getString("PrimerNombre"));
                        bean.setSegNom(jsonObj.getString("SegundoNombre"));
                        bean.setTlf1(jsonObj.getString("Telefono1"));
                        bean.setTlf2(jsonObj.getString("Telefono2"));
                        bean.setTlfMov(jsonObj.getString("TelefonoMovil"));
                        bean.setCorreo(jsonObj.getString("CorreoElectronico"));
                        bean.setGrupo(String.valueOf(jsonObj.getInt("GrupoSocio")));
                        bean.setListaPrecio(String.valueOf(jsonObj.getInt("ListaPrecio")));
                        bean.setCondPago(String.valueOf(jsonObj.getInt("CondicionPago")));
                        bean.setIndicador(jsonObj.getString("Indicador"));
                        bean.setZona(jsonObj.getString("Zona"));
                        bean.setCreadoMovil(jsonObj.getString("CreadMovil"));
                        bean.setClaveMovil(jsonObj.getString("ClaveMovil"));
                        bean.setTransaccionMovil(jsonObj.getString("TransaccionMovil"));
                        bean.setValidoenPedido(jsonObj.getString("ValidoenPedido"));
                        bean.setDireccionFiscal(jsonObj.getString("DireccionFiscalCodigo"));
                        bean.setPoseeActivos(jsonObj.getString("PoseeActivos"));
                        bean.setCodProyecto(jsonObj.getString("Proyecto"));
                        bean.setTipoRegistro(jsonObj.getString("TipoRegistro"));
                        bean.setNumUltimaCompra(jsonObj.getString("CodigoUltimaCompra"));
                        bean.setFecUtimaCompra(jsonObj.getString("FechaUltimaCompra"));
                        bean.setMontoUltCompra(jsonObj.getString("MontoUltimaCompra"));
                        bean.setPersonaContacto(jsonObj.getString("PersonaContacto"));
                        bean.setSaldoCuenta(jsonObj.getString("SaldoCuenta"));

                        JSONArray contacts = jsonObj.getJSONArray("Contactos");
                        ContactoBean detalle;
                        ArrayList<ContactoBean> listDet1 = new ArrayList<>();

                        for (int j = 0; j < contacts.length(); j++){
                            JSONObject detail = contacts.getJSONObject(j);
                            detalle = new ContactoBean();
                            detalle.setIdCon(detail.getString("Codigo"));
                            detalle.setNomCon(detail.getString("Nombre"));
                            detalle.setPrimerNombre(detail.getString("PrimerNombre"));
                            detalle.setSegNomCon(detail.getString("SegundoNombre"));
                            detalle.setApeCon(detail.getString("Apellidos"));
                            detalle.setDireccion(detail.getString("Direccion"));
                            detalle.setEmailCon(detail.getString("CorreoElectronico"));
                            detalle.setTel1Con(detail.getString("Telefono1"));
                            detalle.setTel2Con(detail.getString("Telefono2"));
                            detalle.setTelMovCon(detail.getString("TelefonoMovil"));
                            detalle.setPosicion(detail.getString("Posicion"));
                            listDet1.add(detalle);
                        }

                        bean.setContactos(listDet1);

                        JSONArray directions = jsonObj.getJSONArray("Direcciones");
                        DireccionBean direccionBean;
                        ArrayList<DireccionBean> listDet2 = new ArrayList<>();

                        for (int j = 0; j < directions.length(); j++){
                            JSONObject detail = directions.getJSONObject(j);
                            direccionBean = new DireccionBean();
                            direccionBean.setIDDireccion(detail.getString("Codigo"));
                            direccionBean.setPais(detail.getString("Pais"));
                            direccionBean.setDepartamento(detail.getString("Departamento"));
                            direccionBean.setProvincia(detail.getString("Provincia"));
                            direccionBean.setDistrito(detail.getString("Distrito"));
                            direccionBean.setCalle(detail.getString("Calle"));
                            direccionBean.setReferencia(detail.getString("Referencia"));
                            direccionBean.setTipoDireccion(detail.getString("Tipo"));
                            direccionBean.setLatitud(detail.getString("Latitud"));
                            direccionBean.setLongitud(detail.getString("Longitud"));
                            direccionBean.setVisitaLunes(detail.getString("VisitaLunes"));
                            direccionBean.setVisitaMartes(detail.getString("VisitaMartes"));
                            direccionBean.setVisitaMiercoles(detail.getString("VisitaMiercoles"));
                            direccionBean.setVisitaJueves(detail.getString("VisitaJueves"));
                            direccionBean.setVisitaViernes(detail.getString("VisitaViernes"));
                            direccionBean.setVisitaSabado(detail.getString("VisitaSabado"));
                            direccionBean.setVisitaDomingo(detail.getString("VisitaDomingo"));
                            direccionBean.setFrecuenciaVisita(detail.getString("Frecuencia"));
                            direccionBean.setRuta(detail.getString("Ruta"));
                            direccionBean.setZona(detail.getString("Zona"));
                            direccionBean.setCanal(detail.getString("Canal"));
                            direccionBean.setGiro(detail.getString("Giro"));
                            direccionBean.setFechaInicioVisitas(detail.getString("InicioVisitas"));
                            direccionBean.setVendedor(detail.getString("Vendedor"));
                            listDet2.add(direccionBean);
                        }

                        bean.setDirecciones(listDet2);

                        lstResults.add(bean);
                    }

                    mInsert.insertSocioNegocio(lstResults, "ALL");

                }else{
                    showToast("SOCIOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetSocios() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetSocios = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("SOCIOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE ARTICULOS
    Response.Listener listenerGetItem = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ArticuloBean> mList = new ArrayList<>();
                    ArticuloBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ArticuloBean();
                        bean.setCod(jsonObj.getString("Codigo"));
                        bean.setDesc(jsonObj.getString("Nombre"));
                        bean.setFabricante(jsonObj.getString("Fabricante"));
                        bean.setGrupoArticulo(jsonObj.getString("GrupoArticulo"));
                        bean.setCodUM(jsonObj.getString("GrupoUnidadMedida"));
                        bean.setUnidadMedidaVenta(jsonObj.getString("UnidadMedidaVenta"));
                        bean.setAlmacenDefecto(jsonObj.getString("AlmacenDefecto"));
                        mList.add(bean);
                    }

                    mInsert.insertArticulo(mList);

                }else{
                    showToast("ARTICULOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetItem() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetItem = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ARTICULOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE ALMACENES
    Response.Listener listenerGetAlmacen = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<AlmacenBean> mList = new ArrayList<>();
                    AlmacenBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new AlmacenBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertAlmacen(mList);

                }else{
                    showToast("ALMACENES - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetAlmacen() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetAlmacen = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ALMACENES - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE CANTIDADES
    Response.Listener listenerGetCantidad = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<CantidadBean> mList = new ArrayList<>();
                    CantidadBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new CantidadBean();
                        bean.setAlmacen(jsonObj.getString("Almacen"));
                        bean.setArticulo(jsonObj.getString("Articulo"));
                        bean.setStock(jsonObj.getString("Stock"));
                        bean.setComprometido(jsonObj.getString("Comprometido"));
                        bean.setSolicitado(jsonObj.getString("Solicitado"));
                        bean.setDisponible(jsonObj.getString("Disponible"));
                        mList.add(bean);
                    }

                    mInsert.insertCantidad(mList);

                }else{
                    showToast("CANTIDADES - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetCantidad() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetCantidad = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("CANTIDADES - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE LISTAS DE PRECIO
    Response.Listener listenerGetListaPrecio = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ListaPrecioBean> mList = new ArrayList<>();
                    ListaPrecioBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ListaPrecioBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertListaPrecio(mList);

                }else{
                    showToast("LISTA PRECIO - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetListaPrecio() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetListaPrecio = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("LISTA PRECIO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE PRECIOS
    Response.Listener listenerGetPrecios= new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);
                mProgressDialog.dismiss();

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<PrecioBean> mList = new ArrayList<>();
                    PrecioBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new PrecioBean();
                        bean.setListaPrecio(jsonObj.getString("ListaPrecio"));
                        bean.setArticulo(jsonObj.getString("Articulo"));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setPrecioVenta(jsonObj.getString("PrecioVenta"));
                        mList.add(bean);
                    }

                    mInsert.insertPrecios(mList);

                }else{
                    showToast("PRECIOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetPrecios() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetPrecios = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("PRECIOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    private  void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

}
