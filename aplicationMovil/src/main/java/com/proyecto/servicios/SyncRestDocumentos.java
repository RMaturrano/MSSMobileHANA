package com.proyecto.servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.database.Insert;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SyncRestDocumentos {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private Insert mInsert;
    private SharedPreferences mSharedPreferences;

    public SyncRestDocumentos(Context contexto, ProgressDialog progressDialog){
        mProgressDialog = progressDialog;
        mContext = contexto;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
    }

    public boolean syncFromServer(){

        boolean result = true;

        try {

            mInsert = new Insert(mContext);

            String ip = mSharedPreferences.getString("ipServidor", "200.10.84.66");
            String port = mSharedPreferences.getString("puertoServidor", "80");
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            //region REQUEST ORDEN VENTA
            mProgressDialog.setMessage("Registrando órdenes de venta...");
            JsonObjectRequest mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "purchaseorder/getPurchaseOrder.xsjs?empId=" + sociedad, null,
                    listenerGetOrdenVenta, errorListenerGetOrdenVenta);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST FACTURAS
            mProgressDialog.setMessage("Registrando facturas...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "invoice/getInvoice.xsjs?empId=" + sociedad, null,
                    listenerGetFactura, errorListenerGetFactura);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST PAGO RECIBIDO
            mProgressDialog.setMessage("Registrando pagos...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "incomingpayment/getIncomingPayment.xsjs?empId=" + sociedad, null,
                    listenerGetPagoRecibido, errorListenerGetPagoRecibido);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST ESTADO DE CUENTA
            mProgressDialog.setMessage("Registrando estado de cuenta de cliente...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "reportes/estadoCuenta.xsjs?empId=" + sociedad, null,
                    listenerGetEstadoCuenta, errorListenerGetEstadoCuenta);
            VolleySingleton.getInstance(mContext).addToRequestQueue(mJSONRequest);
            //endregion

            //region REQUEST NOTA DE CREDITO
            mProgressDialog.setMessage("Registrando notas de credito de cliente...");
            mJSONRequest = new JsonObjectRequest(Request.Method.GET,
                    ruta + "reportes/saldosXVendedor.xsjs?empId=" + sociedad, null,
                    listenerGetNotaCredito, errorListenerGetNotaCredito);
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
                        bean.setIndicador(String.valueOf(jsonObj.getInt("Indicador")));
                        bean.setSubTotal(String.valueOf(jsonObj.getInt("SubTotal")));
                        bean.setImpuesto(String.valueOf(jsonObj.getInt("Impuesto")));
                        bean.setTotal(jsonObj.getString("Total"));
                        bean.setCreadoMovil(jsonObj.getString("CreadMovil"));
                        bean.setClaveMovil(jsonObj.getString("ClaveMovil"));
                        bean.setTransaccionMovil(jsonObj.getString("TransaccionMovil"));

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

                }else{
                    showToast(response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetOrdenVenta() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetOrdenVenta = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
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
                            detalle.setArticulo(detail.getString("Articulo"));
                            detalle.setUnidadMedida(detail.getString("UnidadMedida"));
                            detalle.setAlmacen(detail.getString("Almacen"));
                            detalle.setCantidad(detail.getString("Cantidad"));
                            detalle.setListaPrecio(String.valueOf(detail.getInt("ListaPrecio")));
                            detalle.setPrecioUnitario(detail.getString("PrecioUnitario"));
                            detalle.setPorcentajeDescuento(detail.getString("PorcentajeDescuento"));
                            detalle.setImpuesto(detail.getString("Impuesto"));
                            listDet1.add(detalle);
                        }

                        bean.setLineas(listDet1);

                        lstResults.add(bean);
                    }

                    mInsert.insertFacturas(lstResults);

                }else{
                    showToast(response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetFactura() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetFactura = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
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
                    showToast(response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetPagoRecibido() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetPagoRecibido = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
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


                }else{
                    showToast(response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetEstadoCuenta() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetEstadoCuenta = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    //region RESPONSE NOTA DE CREDITO
    Response.Listener listenerGetNotaCredito = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            try {
                mProgressDialog.incrementProgressBy(1);

                if(response.getString("ResponseStatus").equals(Variables.RESPONSE_SUCCESS)){
                    JSONArray jsonArray = response.getJSONObject("Response")
                            .getJSONObject("message")
                            .getJSONArray("value");

                    int size = jsonArray.length();


                }else{
                    showToast(response.getJSONObject("Response").getJSONObject("message").getString("value"));
                }
            }catch (Exception e){
                showToast("listenerGetNotaCredito() > " + e.getMessage());
            }
        }
    };

    Response.ErrorListener errorListenerGetNotaCredito = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            showToast("Ocurrió un error intentando conectar con el servidor, " + error.getMessage());
        }
    };
    //endregion

    private  void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
    }
}
