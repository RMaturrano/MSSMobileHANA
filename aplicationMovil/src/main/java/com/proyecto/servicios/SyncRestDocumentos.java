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
import com.proyecto.bean.DevolucionBean;
import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.FacturaDetalleLoteBean;
import com.proyecto.bean.IncidenciaBean;
import com.proyecto.bean.NotaCreditoBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.ReporteModel;
import com.proyecto.dao.DevolucionDAO;
import com.proyecto.dao.EntregaDAO;
import com.proyecto.dao.FacturaDAO;
import com.proyecto.dao.IncidenciaDAO;
import com.proyecto.dao.NotaCreditoDAO;
import com.proyecto.dao.OrdenVentaDAO;
import com.proyecto.dao.PagoDAO;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.reportes.ReporteEstadoCuenta;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncRestDocumentos {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Insert mInsert;
    private Select mSelect;
    private SharedPreferences mSharedPreferences;
    private int MY_SOCKET_TIMEOUT_MS = 50000;

    public SyncRestDocumentos(Context contexto, ProgressDialog progressDialog){
        mProgressDialog = progressDialog;
        mContext = contexto;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public boolean syncFromServer(){

        boolean result = true;

        try {

            mInsert = new Insert(mContext);
            mSelect = new Select(mContext);

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";
            String codigoEmpleado = mSharedPreferences.getString(Variables.CODIGO_EMPLEADO, "-1");
            String esSupervisor = mSharedPreferences.getString(Variables.SUPERVISOR, "N");
            String esCobrador = mSharedPreferences.getString(Variables.COBRADOR, "N");

            //region POST ORDEN VENTA
            List<OrdenVentaBean> listToSend = mSelect.listaOrdenesVentas();

            if(listToSend.size() > 0){
                mProgressDialog.setMessage("Enviando órdenes de venta...");

                for (final OrdenVentaBean ov : listToSend){
                    try {

                        JSONObject jsonObject = OrdenVentaBean.transformOVToJSON(ov, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "salesorder/addSalesOrder.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                        mInsert.updateEstadoOrdenVenta(ov.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }

                                                }catch (Exception e){showToast("Response - " + e.getMessage());}
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                showToast("VolleyError - " + error.getMessage());
                                            }
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
                        showToast("Enviando ordenes de venta: " + ex.getMessage());
                    }
                }
            }
            //endregion

            //region POST PAGO RECIBIDO
            List<PagoBean> listPagoSend = mSelect.listaPagosRecibidos();

            if(listPagoSend.size() > 0){
                mProgressDialog.setMessage("Enviando pagos recibidos...");

                for (final PagoBean pr : listPagoSend){
                    try {

                        JSONObject jsonObject = PagoBean.transformPRToJSON(pr, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "incomingpayment/addIncomingPayment.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                        mInsert.updateEstadoPago(pr.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }

                                                }catch (Exception e){showToast("Response - " + e.getMessage());}
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                showToast("VolleyError - " + error.getMessage());
                                            }
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
                        showToast("Enviando pagos recibidos: " + ex.getMessage());
                    }
                }
            }
            //endregion

            //region POST INCIDENCIA
            List<IncidenciaBean> lstSendIncidencia = new IncidenciaDAO().listar();

            if(lstSendIncidencia.size() > 0){
                mProgressDialog.setMessage("Enviando incidencias...");

                for (final IncidenciaBean incidencia : lstSendIncidencia){
                    try {

                        JSONObject jsonObject = IncidenciaBean.transformIncidenciaToJSON(incidencia, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "activity/addActivity.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                       new IncidenciaDAO().actualizarSincronizado(incidencia.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }
                                                }catch (Exception e){
                                                    showToast("Response - " + e.getMessage());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {showToast("VolleyError - " + error.getMessage());}
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
                        showToast("Enviando incidencias: " + ex.getMessage());
                    }
                }
            }
            //endregion

            //region POST DEVOLUCION
            List<DevolucionBean> lstSendDevolucion = new DevolucionDAO().listar("L");

            if(lstSendDevolucion.size() > 0){
                mProgressDialog.setMessage("Enviando devoluciones...");

                for (final DevolucionBean devolucion : lstSendDevolucion){
                    try {

                        JSONObject jsonObject = DevolucionBean.transformDevolucionToJSON(devolucion, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "returns/addReturn.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                        new DevolucionDAO().actualizarSincronizado(devolucion.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }
                                                }catch (Exception e){
                                                    showToast("Response - " + e.getMessage());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {showToast("VolleyError - " + error.getMessage());}
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
                        showToast("Enviando devoluciones: " + ex.getMessage());
                    }
                }
            }
            //endregion

            //region POST NOTA CREDITO
            List<NotaCreditoBean> lstSendNotaCredito = new NotaCreditoDAO().listar("L");

            if(lstSendDevolucion.size() > 0){
                mProgressDialog.setMessage("Enviando notas de credito...");

                for (final NotaCreditoBean notacredito : lstSendNotaCredito){
                    try {

                        JSONObject jsonObject = NotaCreditoBean.transformNotaCreditoToJSON(notacredito, sociedad);

                        //request to server
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(Request.Method.POST, ruta + "creditmemo/addCreditMemo.xsjs", jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try
                                                {
                                                    if(response.getString("ResponseStatus").equals("Success")){
                                                        new NotaCreditoDAO().actualizarSincronizado(notacredito.getClaveMovil());
                                                    }else{
                                                        showToast(response.getJSONObject("Response")
                                                                .getJSONObject("message")
                                                                .getString("value"));
                                                    }
                                                }catch (Exception e){
                                                    showToast("Response - " + e.getMessage());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {showToast("VolleyError - " + error.getMessage());}
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
                        showToast("Enviando notas credito: " + ex.getMessage());
                    }
                }
            }
            //endregion

            //region REQUEST ORDEN VENTA
            mProgressDialog.setMessage("Registrando órdenes de venta...");
            String urlGETSO = esCobrador.equals("N") ? "getSalesOrder": "getSalesOrderDispatcher";
            JsonObjectRequest mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "salesorder/"+urlGETSO+".xsjs?empId=" + sociedad + "&usrId=" + codigoEmpleado, null,
                    listenerGetOrdenVenta, errorListenerGetOrdenVenta);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST FACTURAS
            mProgressDialog.setMessage("Registrando facturas...");
            String urlGETIV = esCobrador.equals("N") ? "getInvoice" : "getInvoiceDispatcher";
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "invoice/"+urlGETIV+".xsjs?empId=" + sociedad + "&cove=" +
                            codigoEmpleado, null,
                    listenerGetFactura, errorListenerGetFactura);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST PAGO RECIBIDO
            mProgressDialog.setMessage("Registrando pagos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "incomingpayment/getIncomingPayment.xsjs?empId=" + sociedad + "&usrId=" + codigoEmpleado, null,
                    listenerGetPagoRecibido, errorListenerGetPagoRecibido);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ESTADO DE CUENTA
            mProgressDialog.setMessage("Registrando estado de cuenta de cliente...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "reportes/estadoCuenta.xsjs?empId=" + sociedad, null,
                    listenerGetEstadoCuenta, errorListenerGetEstadoCuenta);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ENTREGA
            mProgressDialog.setMessage("Registrando entregas...");
            String urlGETDN = esCobrador.equals("N") ? "getDeliveryNote" : "getDeliveryNoteDispatcher";
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "deliverynote/"+urlGETDN+".xsjs?empId=" + sociedad + "&cove=" +
                            codigoEmpleado, null,
                    listenerGetEntrega, errorListenerGetEntrega);
            mJSONRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST NOTA DE CREDITO
            mProgressDialog.setMessage("Registrando notas de credito de cliente...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "reportes/saldosXVendedor.xsjs?empId=" + sociedad, null,
                    listenerGetNotaCredito, errorListenerGetNotaCredito);
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

    //region RESPONSE ORDEN VENTA
    Response.Listener listenerGetOrdenVenta = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    ArrayList<OrdenVentaBean> lstResults = new ArrayList<>();
                    OrdenVentaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new OrdenVentaBean();
                        bean.setTipoDoc(jsonObj.getString("Tipo"));
                        bean.setClave(String.valueOf(jsonObj.getInt("Clave")));
                        bean.setNroDoc(String.valueOf(jsonObj.getInt("Numero")));
                        bean.setReferencia(jsonObj.getString("Referencia"));
                        bean.setCodSN(jsonObj.getString("SocioNegocio"));
                        bean.setListaPrecio(String.valueOf(jsonObj.getInt("ListaPrecio")));
                        bean.setContacto(String.valueOf(jsonObj.getInt("Contacto")));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setEmpVentas(String.valueOf(jsonObj.getInt("EmpleadoVenta")));
                        bean.setComentario(jsonObj.getString("Comentario"));
                        bean.setFecContable(jsonObj.getString("FechaContable"));
                        bean.setFecVen(jsonObj.getString("FechaVencimiento"));
                        bean.setDirFiscal(jsonObj.getString("DireccionFiscal"));
                        bean.setDirEntrega(jsonObj.getString("DireccionEntrega"));
                        bean.setCondPago(String.valueOf(jsonObj.getInt("CondicionPago")));
                        bean.setIndicador(jsonObj.getString("Indicador"));
                        bean.setSubTotal(jsonObj.getString("SubTotal"));
                        bean.setImpuesto(jsonObj.getString("Impuesto"));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setCreadoMovil(jsonObj.getString("CreadMovil"));
                        bean.setClaveMovil(jsonObj.getString("ClaveMovil"));
                        bean.setTransaccionMovil(jsonObj.getString("TransaccionMovil"));
                        bean.setModoOffLine(jsonObj.getString("ModoOffLine"));
                        bean.setLatitud(jsonObj.getString("Latitud"));
                        bean.setLongitud(jsonObj.getString("Longitud"));
                        bean.setHoraCreacion(jsonObj.getString("HoraCreacion"));
                        bean.setRangoDireccion(jsonObj.getString("RangoDireccion"));

                        JSONArray contacts = jsonObj.getJSONArray("Lineas");
                        OrdenVentaDetalleBean detalle;
                        ArrayList<OrdenVentaDetalleBean> listDet1 = new ArrayList<>();

                        for (int j = 0; j < contacts.length(); j++){
                            JSONObject detail = contacts.getJSONObject(j);
                            detalle = new OrdenVentaDetalleBean();
                            detalle.setLinea(String.valueOf(detail.getInt("Linea")));
                            detalle.setCodArt(detail.getString("Articulo"));
                            detalle.setCodUM(detail.getString("UnidadMedida"));
                            detalle.setAlmacen(detail.getString("Almacen"));
                            detalle.setCantidad(Double.parseDouble(detail.getString("Cantidad")));
                            detalle.setPrecio(Double.parseDouble(detail.getString("PrecioUnitario")));
                            detalle.setDescuento(Double.parseDouble(detail.getString("PorcentajeDescuento")));
                            detalle.setCodImp(detail.getString("Impuesto"));
                            listDet1.add(detalle);
                        }

                        bean.setDetalles(listDet1);

                        lstResults.add(bean);
                    }

                    mInsert.insertOrdenVenta(lstResults);
                    new OrdenVentaDAO().validarOrdenesLocales();

                }else{
                    //Error producido cuando no se encontraron documentos del usuario
                    if(response.getJSONObject("Response").getInt("code") == -101){
                        new OrdenVentaDAO().eliminarOrdenesNoLocales();
                    }else
                        showToast("ORDEN DE VENTA > " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetOrdenVenta() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetOrdenVenta = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ORDEN DE VENTA > Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE FACTURA
    Response.Listener listenerGetFactura = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    ArrayList<FacturaBean> lstResults = new ArrayList<>();
                    FacturaBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new FacturaBean();
                        bean.setTipo(jsonObj.getString("Tipo"));
                        bean.setClave(String.valueOf(jsonObj.getInt("Clave")));
                        bean.setNumero(String.valueOf(jsonObj.getInt("Numero")));
                        bean.setReferencia(jsonObj.getString("Referencia"));
                        bean.setSocioNegocio(jsonObj.getString("SocioNegocio"));
                        bean.setListaPrecio(String.valueOf(jsonObj.getInt("ListaPrecio")));
                        bean.setContacto(String.valueOf(jsonObj.getInt("Contacto")));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setEmpleadoVenta(jsonObj.getString("EmpleadoVenta"));
                        bean.setComentario(jsonObj.getString("Comentario"));
                        bean.setFechaContable(jsonObj.getString("FechaContable"));
                        bean.setFechaVencimiento(jsonObj.getString("FechaVencimiento"));
                        bean.setDireccionFiscal(jsonObj.getString("DireccionFiscal"));
                        bean.setDireccionEntrega(jsonObj.getString("DireccionEntrega"));
                        bean.setCondicionPago(jsonObj.getString("CondicionPago"));
                        bean.setIndicador(jsonObj.getString("Indicador"));
                        bean.setSubTotal(jsonObj.getString("SubTotal"));
                        bean.setDescuento(jsonObj.getString("Descuento"));
                        bean.setImpuesto(jsonObj.getString("Impuesto"));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setSaldo(jsonObj.getString("Saldo"));

                        JSONArray lines = jsonObj.getJSONArray("Lineas");
                        FacturaDetalleBean detalle;
                        ArrayList<FacturaDetalleBean> listDet1 = new ArrayList<>();

                        for (int j = 0; j < lines.length(); j++){
                            JSONObject detail = lines.getJSONObject(j);
                            detalle = new FacturaDetalleBean();
                            detalle.setLinea(detail.getInt("Linea"));
                            detalle.setArticulo(detail.getString("Articulo"));
                            detalle.setUnidadMedida(detail.getString("UnidadMedida"));
                            detalle.setAlmacen(detail.getString("Almacen"));
                            detalle.setCantidad(detail.getString("Cantidad"));
                            detalle.setDiponible(detail.getString("Disponible"));
                            detalle.setListaPrecio(String.valueOf(detail.getInt("ListaPrecio")));
                            detalle.setPrecioUnitario(detail.getString("PrecioUnitario"));
                            detalle.setPorcentajeDescuento(detail.getString("PorcentajeDescuento"));
                            detalle.setImpuesto(detail.getString("Impuesto"));

                            JSONArray lotes = detail.getJSONArray("Lotes");
                            FacturaDetalleLoteBean lote;
                            ArrayList<FacturaDetalleLoteBean> listDet2 = new ArrayList<>();

                            for (int k = 0; k < lotes.length(); k++) {
                                JSONObject joLote = lotes.getJSONObject(k);
                                lote = new FacturaDetalleLoteBean();
                                lote.setClaveBase(Integer.parseInt(bean.getClave()));
                                lote.setLote(joLote.getString("Lote"));
                                lote.setCantidad(joLote.getDouble("Cantidad"));
                                lote.setLineaBase(joLote.getInt("LineaBase"));
                                listDet2.add(lote);
                            }

                            detalle.setLotes(listDet2);
                            listDet1.add(detalle);
                        }

                        bean.setLineas(listDet1);

                        lstResults.add(bean);
                    }

                    mInsert.insertFacturas(lstResults);

                }else{
                    if(response.getJSONObject("Response").getInt("code") == -101){
                        new FacturaDAO().eliminarFacturas();
                    }else
                        showToast("FACTURA > " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetFactura() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetFactura = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("FACTURA > Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE PAGO RECIBIDO
    Response.Listener listenerGetPagoRecibido = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    ArrayList<PagoBean> lstResults = new ArrayList<>();
                    PagoBean bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new PagoBean();
                        bean.setTipo(jsonObj.getString("Tipo"));
                        bean.setClave(String.valueOf(jsonObj.getInt("Clave")));
                        bean.setNumero(String.valueOf(jsonObj.getInt("Numero")));
                        bean.setSocioNegocio(jsonObj.getString("SocioNegocio"));
                        bean.setEmpleadoVenta(String.valueOf(jsonObj.getInt("EmpleadoVenta")));
                        bean.setComentario(jsonObj.getString("Comentario"));
                        bean.setGlosa(jsonObj.getString("Glosa"));
                        bean.setFechaContable(jsonObj.getString("FechaContable"));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setTipoPago(jsonObj.getString("TipoPago"));
                        bean.setTransferenciaCuenta(jsonObj.getString("TransferenciaCuenta"));
                        bean.setTransferenciaReferencia(jsonObj.getString("TransferenciaReferencia"));
                        bean.setTransferenciaImporte(jsonObj.getString("TransferenciaImporte"));
                        bean.setEfectivoCuenta(jsonObj.getString("EfectivoCuenta"));
                        bean.setEfectivoImporte(jsonObj.getString("EfectivoImporte"));
                        bean.setCreadoMovil(jsonObj.getString("CreadMovil"));
                        bean.setClaveMovil(jsonObj.getString("ClaveMovil"));
                        bean.setChequeCuenta(jsonObj.getString("ChequeCuenta"));
                        bean.setChequeBanco(jsonObj.getString("ChequeBanco"));
                        bean.setChequeVencimiento(jsonObj.getString("ChequeVencimiento"));
                        bean.setChequeImporte(jsonObj.getString("ChequeImporte"));
                        bean.setChequeNumero(jsonObj.getString("ChequeNumero"));
                        bean.setTransaccionMovil(jsonObj.getString("TransaccionMovil"));

                        JSONArray lines = jsonObj.getJSONArray("Lineas");
                        PagoDetalleBean detalle;
                        ArrayList<PagoDetalleBean> listDet1 = new ArrayList<>();

                        for (int j = 0; j < lines.length(); j++){
                            JSONObject detail = lines.getJSONObject(j);
                            detalle = new PagoDetalleBean();
                            detalle.setFacturaCliente(String.valueOf(detail.getString("FacturaCliente")));
                            detalle.setImporte(detail.getString("Importe"));
                            listDet1.add(detalle);
                        }

                        bean.setLineas(listDet1);

                        lstResults.add(bean);
                    }

                    mInsert.insertPagoCliente(lstResults);

                }else{
                    if(response.getJSONObject("Response").getInt("code") == -101){
                        new PagoDAO().eliminarPagosNoLocales();
                    } else
                        showToast("PAGO RECIBIDO > " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetPagoRecibido() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetPagoRecibido = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("PAGO RECIBIDO > Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE ESTADO DE CUENTA
    Response.Listener listenerGetEstadoCuenta = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    ArrayList<ReporteEstadoCuenta> lstResults = new ArrayList<>();
                    ReporteEstadoCuenta bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ReporteEstadoCuenta();
                        bean.setTipoReporte(jsonObj.getString("TipoReporte"));
                        bean.setCliente(jsonObj.getString("Cliente"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        bean.setListaPrecio(jsonObj.getString("ListaPrecio"));
                        bean.setLineaCredito(jsonObj.getString("LineaCredito"));
                        bean.setCondicionPago(jsonObj.getString("CondicionPago"));
                        bean.setClave(String.valueOf(jsonObj.getInt("Clave")));
                        bean.setSunat(jsonObj.getString("Sunat"));
                        bean.setCondicion(jsonObj.getString("Condicion"));
                        bean.setVendedor(jsonObj.getString("Vendedor"));
                        bean.setEmision(jsonObj.getString("Emision"));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setSaldo(jsonObj.getString("Saldo"));
                        bean.setPago_Fecha(jsonObj.getString("Pago_Fecha"));
                        bean.setPago_Dias(String.valueOf(jsonObj.getInt("Pago_Dias")));
                        bean.setPago_Moneda(jsonObj.getString("Pago_Moneda"));
                        bean.setPagado_Importe(jsonObj.getString("Pagado_Importe"));

                        lstResults.add(bean);
                    }

                    mInsert.insertEstadoCuentaCliente(lstResults);

                }else{
                    showToast("ESTADO DE CUENTA > " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetEstadoCuenta() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetEstadoCuenta = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ESTADO DE CUENTA > Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE ENTREGA
    Response.Listener listenerGetEntrega = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();
                    EntregaBean bean;
                    EntregaDAO dao = new EntregaDAO();
                    dao.deleteAll();

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new EntregaBean();
                        bean.setTipo(jsonObj.getString("Tipo"));
                        bean.setClave(jsonObj.getInt("Clave"));
                        bean.setNumero(jsonObj.getInt("Numero"));
                        bean.setReferencia(jsonObj.getString("Referencia"));
                        bean.setSocioNegocio(jsonObj.getString("SocioNegocio"));
                        bean.setListaPrecio(jsonObj.getInt("ListaPrecio"));
                        bean.setContacto(jsonObj.getInt("Contacto"));
                        bean.setMoneda(jsonObj.getString("Moneda"));
                        bean.setEmpleadoVenta(jsonObj.getString("EmpleadoVenta"));
                        bean.setComentario(jsonObj.getString("Comentario"));
                        bean.setFechaContable(jsonObj.getString("FechaContable"));
                        bean.setFechaVencimiento(jsonObj.getString("FechaVencimiento"));
                        bean.setDireccionFiscal(jsonObj.getString("DireccionFiscal"));
                        bean.setDireccionEntrega(jsonObj.getString("DireccionEntrega"));
                        bean.setCondicionPago(jsonObj.getString("CondicionPago"));
                        bean.setIndicador(jsonObj.getString("Indicador"));
                        bean.setSubTotal(jsonObj.getString("SubTotal"));
                        bean.setDescuento(jsonObj.getString("Descuento"));
                        bean.setImpuesto(jsonObj.getString("Impuesto"));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setSaldo(jsonObj.getString("Saldo"));

                        JSONArray lines = jsonObj.getJSONArray("Lineas");
                        EntregaDetalleBean detalle;
                        ArrayList<EntregaDetalleBean> listDet1 = new ArrayList<>();

                        for (int j = 0; j < lines.length(); j++){
                            JSONObject detail = lines.getJSONObject(j);
                            detalle = new EntregaDetalleBean();
                            detalle.setClaveEntrega(bean.getClave());
                            detalle.setLinea(detail.getInt("Linea"));
                            detalle.setArticulo(detail.getString("Articulo"));
                            detalle.setUnidadMedida(detail.getString("UnidadMedida"));
                            detalle.setAlmacen(detail.getString("Almacen"));
                            detalle.setCantidad(detail.getString("Cantidad"));
                            detalle.setDiponible(detail.getString("Disponible"));
                            detalle.setListaPrecio(detail.getInt("ListaPrecio"));
                            detalle.setPrecioUnitario(detail.getString("PrecioUnitario"));
                            detalle.setPorcentajeDescuento(detail.getString("PorcentajeDescuento"));
                            detalle.setImpuesto(detail.getString("Impuesto"));

                            JSONArray lotes = detail.getJSONArray("Lotes");
                            EntregaDetalleLoteBean lote;
                            ArrayList<EntregaDetalleLoteBean> listDet2 = new ArrayList<>();

                            for (int k = 0; k < lotes.length(); k++) {
                                JSONObject joLote = lotes.getJSONObject(k);
                                lote = new EntregaDetalleLoteBean();
                                lote.setClaveBase(bean.getClave());
                                lote.setLote(joLote.getString("Lote"));
                                lote.setCantidad(joLote.getDouble("Cantidad"));
                                lote.setLineaBase(joLote.getInt("LineaBase"));
                                listDet2.add(lote);
                            }

                            detalle.setLotes(listDet2);
                            listDet1.add(detalle);
                        }

                        bean.setLineas(listDet1);
                        dao.insertar(bean);
                    }

                }else{
                    showToast("ENTREGA > " +response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetEntrega() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetEntrega = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("ENTREGA > Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE NOTA DE CREDITO
    Response.Listener listenerGetNotaCredito = new Response.Listener<JSONObject>(){
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
                    ArrayList<ReporteModel> lstResults = new ArrayList<>();
                    ReporteModel bean;

                    for (int i = 0; i < size; i++ ) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        bean = new ReporteModel();
                        bean.setClave(String.valueOf(jsonObj.getInt("Clave")));
                        bean.setSunat(jsonObj.getString("Sunat"));
                        bean.setEmision(jsonObj.getString("Emision"));
                        bean.setDias(String.valueOf(jsonObj.getString("Dias")));
                        bean.setRuc(jsonObj.getString("Ruc"));
                        bean.setNombre(jsonObj.getString("Nombre"));
                        bean.setDireccion(jsonObj.getString("Direccion"));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setPagado(jsonObj.getString("Pagado"));
                        bean.setSaldo(jsonObj.getString("Saldo"));

                        lstResults.add(bean);
                    }

                    mInsert.insertNotaCredito(lstResults);

                }else{
                    showToast("NOTA DE CREDITO > " + response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){

                showToast("listenerGetNotaCredito() > " + e.getMessage());
            }finally {
                mProgressDialog.dismiss();
            }
        }
    };

    Response.ErrorListener errorListenerGetNotaCredito = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mProgressDialog.dismiss();
            showToast("NOTA DE CREDITO > Ocurrio un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    private  void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
