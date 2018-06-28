package com.proyecto.servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Region;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyecto.bean.BancoBean;
import com.proyecto.bean.CanalBean;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.CuentaBean;
import com.proyecto.bean.DepartamentoBean;
import com.proyecto.bean.DistritoBean;
import com.proyecto.bean.FabricanteBean;
import com.proyecto.bean.GiroBean;
import com.proyecto.bean.GrupoArticuloBean;
import com.proyecto.bean.GrupoSocioNegocioBean;
import com.proyecto.bean.GrupoUnidadMedidaBean;
import com.proyecto.bean.ImpuestoBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.MotivoBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.ProyectoBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.bean.ZonaBean;
import com.proyecto.database.Insert;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 10/01/2018.
 */

public class SyncRestInicio {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Insert mInsert;
    private SharedPreferences mSharedPreferences;
    private int MY_SOCKET_TIMEOUT_MS = 50000;

    public SyncRestInicio(Context contexto, ProgressDialog progressDialog){
        mProgressDialog = progressDialog;
        mContext = contexto;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public boolean syncFromServer(){

        boolean result = true;

        try {

            mInsert = new Insert(mContext);

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            //region REQUEST COUNTRY
            mProgressDialog.setMessage("Registrando paises...");
            JsonObjectRequest mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "country/getCountry.xsjs?empId=" + sociedad, null,
                    listenerGetPaises, errorListenerGetPaises);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST STATES
            mProgressDialog.setMessage("Registrando departamentos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "state/getState.xsjs?empId=" + sociedad, null,
                    listenerGetDepartamentos, errorListenerGetDepartamentos);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST PROVINCIAS
            mProgressDialog.setMessage("Registrando provincias...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "province/getProvince.xsjs?empId=" + sociedad, null,
                    listenerGetProvincias, errorListenerGetProvincias);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST DISTRITOS
            mProgressDialog.setMessage("Registrando distritos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "district/getDistrict.xsjs?empId=" + sociedad, null,
                    listenerGetDistritos, errorListenerGetDistritos);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //FALTA LATITUDES Y LONGITUDES

            //region REQUEST BANCOS
            mProgressDialog.setMessage("Registrando bancos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "bank/getBank.xsjs?empId=" + sociedad, null,
                    listenerGetBancos, errorListenerGetBancos);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST CUENTAS CONTABLES
            mProgressDialog.setMessage("Registrando cuentas cont...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "account/getAccount.xsjs?empId=" + sociedad, null,
                    listenerGetCuentas, errorListenerGetCuentas);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST MONEDAS
            mProgressDialog.setMessage("Registrando monedas...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "currency/getCurrency.xsjs?empId=" + sociedad, null,
                    listenerGetMonedas, errorListenerGetMonedas);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST CONDICION PAGO
            mProgressDialog.setMessage("Registrando condiciones de pago...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "paymentterms/getPayTerms.xsjs?empId=" + sociedad, null,
                    listenerGetCondicionPago, errorListenerGetCondicionPago);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST INDICADORES
            mProgressDialog.setMessage("Registrando indicadores...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "indicator/getIndicator.xsjs?empId=" + sociedad, null,
                    listenerGetIndicadores, errorListenerGetIndicadores);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST IMPUESTOS
            mProgressDialog.setMessage("Registrando códigos de impuesto...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "taxcodes/getTaxCodes.xsjs?empId=" + sociedad, null,
                    listenerGetImpuesto, errorListenerGetImpuesto);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST GRUPOS SOCIOS NEGOCIO
            mProgressDialog.setMessage("Registrando grupos de clientes...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "cardgroups/getCardGroup.xsjs?empId=" + sociedad, null,
                    listenerGetGrupoSocio, errorListenerGetGrupoSocio);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ZONAS (PROPIEDADES SN)
            mProgressDialog.setMessage("Registrando zonas...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "cardproperties/getCardProperty.xsjs?empId=" + sociedad, null,
                    listenerGetZona, errorListenerGetZona);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST FABRICANTES
            mProgressDialog.setMessage("Registrando fabricantes...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "manufacturers/getManufacturer.xsjs?empId=" + sociedad, null,
                    listenerGetFabricantes, errorListenerGetFabricantes);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST MOTIVOS
            mProgressDialog.setMessage("Registrando motivos incidencia...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "grounds/getGrounds.xsjs", null,
                    listenerGetMotivos, errorListenerGetMotivos);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST PROYECTOS
            mProgressDialog.setMessage("Registrando proyectos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "project/getProjects.xsjs?empId=" + sociedad, null,
                    listenerGetProyecto, errorListenerGetProyecto);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST CANAL
            mProgressDialog.setMessage("Registrando canales...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "canal/obtenerCanal.xsjs?empId=" + sociedad, null,
                    listenerGetCanal, errorListenerGetCanal);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST GIRO
            mProgressDialog.setMessage("Registrando giros...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "giro/obtenerGiro.xsjs?empId=" + sociedad, null,
                    listenerGetGiro, errorListenerGetGiro);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST GRUPOS ARTICULO
            mProgressDialog.setMessage("Registrando grupos de artículo...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "itemgroups/getItemGroup.xsjs?empId=" + sociedad, null,
                    listenerGetGrupoArticulo, errorListenerGetGrupoArticulo);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST UNIDADES DE MEDIDA
            mProgressDialog.setMessage("Registrando unidades de medida...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "unitmeasure/getUnitMeasure.xsjs?empId=" + sociedad, null,
                    listenerGetUnidadMedida, errorListenerGetUnidadMedida);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST GRUPOS DE UNIDADES DE MEDIDA
            mProgressDialog.setMessage("Registrando grupos de unidades de medida...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "unitmeasuregroup/getUnitMeasureGroup.xsjs?empId=" + sociedad, null,
                    listenerGetGrupoUnidadMedida, errorListenerGetGrupoUnidadMedida);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

        }catch (Exception e){
            result = false;
        }

        return result;
    }

    //region RESPONSE COUNTRY
    Response.Listener listenerGetPaises = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<PaisBean> listPais = new ArrayList<>();
                    PaisBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new PaisBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        listPais.add(bean);
                    }

                    mInsert.insertPais(listPais);
                }else{
                    showToast("PAISES - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetPaises() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetPaises = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("PAISES - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE STATES
    Response.Listener listenerGetDepartamentos = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<DepartamentoBean> mList = new ArrayList<>();
                    DepartamentoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new DepartamentoBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        bean.setPais(jsonObj.getString("Pais"));
                        mList.add(bean);
                    }

                    mInsert.insertDepartamento(mList);

                }else{
                    showToast("DEPARTAMENTOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetDepartamentos() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetDepartamentos = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("DEPARTAMENTOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE PROVINCIAS
    Response.Listener listenerGetProvincias = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ProvinciaBean> mList = new ArrayList<>();
                    ProvinciaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ProvinciaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Descripcion"));
                        bean.setDepartamento(jsonObj.getString("Departamento"));
                        mList.add(bean);
                    }

                    mInsert.insertProvincias(mList);

                }else{
                    showToast("PROVINCIAS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetProvincias() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetProvincias = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("PROVINCIAS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE DISTRITOS
    Response.Listener listenerGetDistritos = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<DistritoBean> mList = new ArrayList<>();
                    DistritoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new DistritoBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Descripcion"));
                        bean.setProvincia(jsonObj.getString("Provincia"));
                        mList.add(bean);
                    }

                    mInsert.insertDistritos(mList);

                }else{
                    showToast("DISTRITOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetDistritos() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetDistritos = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("DISTRITOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE BANCOS
    Response.Listener listenerGetBancos = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<BancoBean> mList = new ArrayList<>();
                    BancoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new BancoBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertBancos(mList);

                }else{
                    showToast("BANCOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetBancos() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetBancos = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("BANCOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE CUENTAS
    Response.Listener listenerGetCuentas = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<CuentaBean> mList = new ArrayList<>();
                    CuentaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new CuentaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertCuentas(mList);

                }else{
                    showToast("CUENTAS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetCuentas() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetCuentas = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("CUENTAS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE MONEDAS
    Response.Listener listenerGetMonedas = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<MonedaBean> mList = new ArrayList<>();
                    MonedaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new MonedaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertMoneda(mList);

                }else{
                    showToast("MONEDAS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetMonedas() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetMonedas = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("MONEDAS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE CONDICION PAGO
    Response.Listener listenerGetCondicionPago = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<CondicionPagoBean> mList = new ArrayList<>();
                    CondicionPagoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new CondicionPagoBean();
                        bean.setNumeroCondicion(jsonObj.getString("Codigo"));
                        bean.setDescripcionCondicion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertCondicionPago(mList);

                }else{
                    showToast("COND PAGO - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetCondicionPago() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetCondicionPago = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("COND PAGO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE INDICADORES
    Response.Listener listenerGetIndicadores = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<IndicadorBean> mList = new ArrayList<>();
                    IndicadorBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new IndicadorBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertIndicador(mList);

                }else{
                    showToast("INDICADORES - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetIndicadores() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetIndicadores = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("INDICADORES - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE IMPUESTO
    Response.Listener listenerGetImpuesto = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ImpuestoBean> mList = new ArrayList<>();
                    ImpuestoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ImpuestoBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        bean.setTasa(Double.parseDouble(jsonObj.getString("Tasa")));
                        mList.add(bean);
                    }

                    mInsert.insertImpuesto(mList);

                }else{
                    showToast("IMPUESTO - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetImpuesto() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetImpuesto = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("IMPUESTO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE GRUPOS SOCIO
    Response.Listener listenerGetGrupoSocio = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<GrupoSocioNegocioBean> mList = new ArrayList<>();
                    GrupoSocioNegocioBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new GrupoSocioNegocioBean();
                        bean.setGroupCode(jsonObj.getString("Codigo"));
                        bean.setGroupName(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertGruposSocioNegocio(mList);

                }else{
                    showToast("GRUPO SOCIO - " +response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetGrupoSocio() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetGrupoSocio = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("GRUPO SOCIO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE ZONAS
    Response.Listener listenerGetZona = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ZonaBean> mList = new ArrayList<>();
                    ZonaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ZonaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertZonas(mList);

                }else{
                    showToast("ZONAS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetZona() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetZona = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ZONAS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE FABRICANTES
    Response.Listener listenerGetFabricantes = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<FabricanteBean> mList = new ArrayList<>();
                    FabricanteBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new FabricanteBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertFabricante(mList);

                }else{
                    showToast("FABRICANTES - " +response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetFabricantes() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetFabricantes = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("FABRICANTES - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE MOTIVOS
    Response.Listener listenerGetMotivos = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<MotivoBean> mList = new ArrayList<>();
                    MotivoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new MotivoBean();
                        bean.setId(jsonObj.getInt("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Descripcion"));
                        bean.setValOrden(jsonObj.getString("ValOrden"));
                        bean.setValEntrega(jsonObj.getString("ValEntrega"));
                        bean.setValFactura(jsonObj.getString("ValFactura"));
                        mList.add(bean);
                    }

                    mInsert.insertMotivo(mList);

                }else{
                    showToast("MOTIVOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetMotivos() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetMotivos = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("MOTIVOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE PROYECTOS
    Response.Listener listenerGetProyecto = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<ProyectoBean> mList = new ArrayList<>();
                    ProyectoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ProyectoBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertProyectos(mList);

                }else{
                    showToast("PROYECTOS - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetProyecto() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetProyecto = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("PROYECTOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE CANALES
    Response.Listener listenerGetCanal = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<CanalBean> mList = new ArrayList<>();
                    CanalBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new CanalBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertCanales(mList);

                }else{
                    showToast("CANAL - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetCanal() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetCanal = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("MOTIVOS - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE GIROS
    Response.Listener listenerGetGiro = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<GiroBean> mList = new ArrayList<>();
                    GiroBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new GiroBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setDescripcion(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertGiros(mList);

                }else{
                    showToast("GIRO - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetGiro() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetGiro = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("GIRO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE GRUPOS ARTICULO
    Response.Listener listenerGetGrupoArticulo = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<GrupoArticuloBean> mList = new ArrayList<>();
                    GrupoArticuloBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new GrupoArticuloBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertGruposArticulo(mList);

                }else{
                    showToast("GRUPO ARTICULO - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetGrupoArticulo() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetGrupoArticulo = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("GRUPO ARTICULO - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE UNIDADES MEDIDA
    Response.Listener listenerGetUnidadMedida = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<UnidadMedidaBean> mList = new ArrayList<>();
                    UnidadMedidaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new UnidadMedidaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        mList.add(bean);
                    }

                    mInsert.insertUnidadMedida(mList);

                }else{
                    showToast("UNIDAD MEDIDA - " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetUnidadMedida() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetUnidadMedida = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("UNIDAD MEDIDA - Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE GRUPOS UNIDAD MEDIDA
    Response.Listener listenerGetGrupoUnidadMedida = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);
                mProgressDialog.dismiss();

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)) {
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    List<GrupoUnidadMedidaBean> mList = new ArrayList<>();
                    GrupoUnidadMedidaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new GrupoUnidadMedidaBean();
                        bean.setCodigo(jsonObj.getString("Codigo"));
                        bean.setNombre(jsonObj.getString("Nombre"));

                        JSONArray details = jsonObj.getJSONArray("Detalles");
                        UnidadMedidaBean detalle;
                        ArrayList<UnidadMedidaBean> listUM = new ArrayList<>();

                        for (int j = 0; j < details.length(); j++){
                            JSONObject umDetail = details.getJSONObject(j);
                            detalle = new UnidadMedidaBean();
                            detalle.setCodigo(umDetail.getString("Codigo"));
                            detalle.setNombre(umDetail.getString("Nombre"));
                            listUM.add(detalle);
                        }

                        bean.setUnidadMedida(listUM);
                        mList.add(bean);
                    }

                    mInsert.insertGruposUnidadMedida(mList);

                }else{
                    showToast("GRUPO UND MEDIDA - " +response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetGrupoUnidadMedida() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetGrupoUnidadMedida = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mProgressDialog.dismiss();
            showToast("GRUPO UND MEDIDA - Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    private  void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
