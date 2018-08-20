
package com.proyecto.notacredito;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.FacturaDetalleLoteBean;
import com.proyecto.bean.NotaCreditoBean;
import com.proyecto.bean.NotaCreditoDetalleBean;
import com.proyecto.bean.NotaCreditoDetalleLoteBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.ClienteDAO;
import com.proyecto.dao.CorrelativoDAO;
import com.proyecto.dao.FacturaDAO;
import com.proyecto.dao.ImpuestoDAO;
import com.proyecto.dao.NotaCreditoDAO;
import com.proyecto.notacredito.adapter.recyclerview.listeners.IRVAdapterAddNotaCredito;
import com.proyecto.notacredito.adapter.recyclerview.ItemAddCreditNote;
import com.proyecto.notacredito.adapter.recyclerview.RVAdapterAddNotaCredito;
import com.proyecto.sociosnegocio.ClienteBuscarActivity;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;
import com.proyecto.sociosnegocio.util.ContactoBuscarBean;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.location.LocationManager.GPS_PROVIDER;

public class NotaCreditoActivity extends AppCompatActivity implements IRVAdapterAddNotaCredito{

    public static String KEY_PARAM_FACTURA = "kpFactura";
    private final String PATTERN_FECHA = "yyyy/MM/dd";
    private final String PATTERN_HORA = "HH:mm";
    private NotaCreditoBean mNotaCredito;

    private SharedPreferences mSharedPreferences;
    private String mNroDocumento, mCodigoEmpleado, mNombreEmpleado, mIdDispositivo, mFechaActual;
    private Double mImpuesto = 0d, mTotalAntesDescuento = 0d, mTotal = 0d;

    private RecyclerView mRecyclerView;
    private RVAdapterAddNotaCredito mRVAdapter;
    private ClienteBuscarBean mCliente;
    private ContactoBuscarBean mContacto, mContactoTemp;
    private DireccionBuscarBean mDireccionFiscal, mDireccionFiscalTemp,
            mDireccionEntrega, mDireccionEntregaTemp;
    private List<ItemAddCreditNote> mListRows;
    private FacturaBean mFactura;
    private List<FacturaBean> mListFacturasTemp;
    private Location mCurrentLocation = null;

    //region UTIL_CONSTANTS
    private final static String DIALOG_COMENTARIOS = "comentarios";
    //endregion

    //region SCREEN_ROWS
    private final static String TITULO_CODIGO = "Codigo nota de credito";
    private final static String CODIGO_CLIENTE = "Codigo cliente";
    private final static String NOMBRE_CLIENTE = "Nombre cliente";
    private final static String DIRECCION_ENTREGA = "Direccion de entrega";
    private final static String DIRECCION_FISCAL = "Direccion fiscal";
    private final static String VENDEDOR = "Usuario";
    private final static String COMENTARIOS = "Comentarios";
    private final static String FECHA_CONTABLE = "Fecha contable";
    private final static String FACTURAS = "Facturas del cliente";
    private final static String BASADO_EN = "Basado en factura";
    private final static String PRODUCTOS = "Articulos";
    private final static String TOTAL_ANTES_DESCUENTO = "Total antes de impuesto";
    private final static String TOTAL_IMPUESTO = "Total impuesto";
    private final static String TOTAL = "Total";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_credito);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbAddCreditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlAgregarNotaCredito);

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        mCodigoEmpleado = mSharedPreferences.getString(Variables.CODIGO_EMPLEADO, "");
        mNombreEmpleado = mSharedPreferences.getString(Variables.NOMBRE_EMPLEADO, "");
        mIdDispositivo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvAddCreditNote);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NotaCreditoActivity.this));
        mRecyclerView.setHasFixedSize(true);

        mRVAdapter = new RVAdapterAddNotaCredito(NotaCreditoActivity.this);
        mRecyclerView.setAdapter(mRVAdapter);
        mListRows = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView();
        mCurrentLocation = getCurrentLocation();

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_FACTURA)){
                mFactura = getIntent().getParcelableExtra(KEY_PARAM_FACTURA);
                if(mFactura != null) {
                    mCliente = new ClienteDAO().buscarPorCodigo(mFactura.getSocioNegocio());
                    clienteSeleccionado();
                    facturaSeleccionada();
                    iniciarValoresPorDefecto();
                }
            }
        }
    }

    //Inicio de la lista de datos
    private void initRecyclerView(){
        mFechaActual = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date());
        String fullDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());
        int nroNC = CorrelativoDAO.obtenerUltimoNumero("NOT");
        mNroDocumento = mIdDispositivo +"-"+fullDate+"-" + nroNC;

        if(mListRows == null || mListRows.size() == 0){
            mListRows = new ArrayList<>();
            mNotaCredito = new NotaCreditoBean();
            mListRows.add(new ItemAddCreditNote(TITULO_CODIGO, mNroDocumento, false));
            mListRows.add(new ItemAddCreditNote(FECHA_CONTABLE, StringDateCast.castStringtoDate(mFechaActual), false));
            mListRows.add(new ItemAddCreditNote(CODIGO_CLIENTE, true));
            mListRows.add(new ItemAddCreditNote(NOMBRE_CLIENTE, false));
            mListRows.add(new ItemAddCreditNote(DIRECCION_FISCAL, true));
            mListRows.add(new ItemAddCreditNote(DIRECCION_ENTREGA, true));
            mListRows.add(new ItemAddCreditNote(VENDEDOR, mNombreEmpleado, false));
            mListRows.add(new ItemAddCreditNote(COMENTARIOS, true));

            mRVAdapter.clearAndAddAll(mListRows);
        }else{
            iniciarValoresPorDefecto();
        }
    }

    private void iniciarValoresPorDefecto(){
        try{
            for (ItemAddCreditNote item: mListRows) {
                switch (item.getTitulo())
                {
                    case FECHA_CONTABLE:
                        item.setContenido(StringDateCast.castStringtoDate(mFechaActual));
                        break;
                    case VENDEDOR:
                        item.setContenido(mNombreEmpleado);
                        break;
                    case CODIGO_CLIENTE:
                        if(mCliente != null)
                            item.setContenido(mCliente.getCodigo());
                        break;
                    case NOMBRE_CLIENTE:
                        if(mCliente != null)
                            item.setContenido(mCliente.getNombre());
                        break;
                    case DIRECCION_FISCAL:
                        if(mDireccionFiscal != null)
                            item.setContenido(mDireccionFiscal.getCalle());
                        break;
                    case DIRECCION_ENTREGA:
                        if(mDireccionEntrega != null)
                            item.setContenido(mDireccionEntrega.getCalle());
                        break;
                    case TOTAL_ANTES_DESCUENTO:
                        item.setContenido(mTotalAntesDescuento.toString());
                        break;
                    case TOTAL_IMPUESTO:
                        item.setContenido(mImpuesto.toString());
                        break;
                    case TOTAL:
                        item.setContenido(mTotal.toString());
                        break;
                }
            }

            mRVAdapter.notifyDataSetChanged();

        }catch(Exception e){
            showMessage("iniciarValoresPorDefecto() > " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == RESULT_OK && requestCode == ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE) {
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(ClienteBuscarActivity.KEY_PARAM_CLIENTE)) {
                    mCliente = data.getParcelableExtra(ClienteBuscarActivity.KEY_PARAM_CLIENTE);
                    clienteSeleccionado();
                    mFactura = null;
                }
            }else if(resultCode == RESULT_OK && requestCode == NotaCreditoListaFacturaActivity.REQUEST_CODE_LISTA_FACTURAS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(NotaCreditoListaFacturaActivity.KEY_PARAM_FACTURA_SELECTED)){
                    mFactura = data.getParcelableExtra(NotaCreditoListaFacturaActivity.KEY_PARAM_FACTURA_SELECTED);
                    facturaSeleccionada();
                }else
                    showMessage("No se ha recibido informacion sobre la factura seleccionada.");
            }else if(resultCode == RESULT_OK && requestCode == NotaCreditoArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN)){
                    List<FacturaDetalleBean> detalles = data.getParcelableArrayListExtra(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN);
                    if(detalles.size() > 0){
                        mFactura.getLineas().clear();
                        mFactura.getLineas().addAll(detalles);
                        mRVAdapter.updateItem(String.valueOf(getNumberOfSelecteds(mFactura)) + " productos", PRODUCTOS);
                        actualizarTotales();
                    }
                }else
                    showMessage("No se ha recibido informaci?n sobre los productos seleccionados.");
            }
        }catch (Exception e){
            showMessage("onActivityResult > " + e.getMessage());
        }
    }

    private void clienteSeleccionado(){
        try{
            if(mCliente != null) {
                mContacto = null;
                mDireccionFiscal = null;
                mListFacturasTemp = new FacturaDAO().listar(mCliente.getCodigo());

                if (mListFacturasTemp.size() > 0) {
                    mRVAdapter.addItem(new ItemAddCreditNote(FACTURAS, "Disponible: " +
                            String.valueOf(mListFacturasTemp.size()), true));
                    mRVAdapter.addItem(new ItemAddCreditNote(BASADO_EN, true));
                    mRVAdapter.addItem(new ItemAddCreditNote(PRODUCTOS, "0 productos", true));
                    mRVAdapter.addItem(new ItemAddCreditNote(TOTAL_ANTES_DESCUENTO, false));
                    mRVAdapter.addItem(new ItemAddCreditNote(TOTAL_IMPUESTO, false));
                    mRVAdapter.addItem(new ItemAddCreditNote(TOTAL, false));
                } else {
                    showMessage("El cliente no tiene facturas registradas.");
                }
            }
        }catch(Exception e){
            showMessage("clienteSeleccionado() > " + e.getMessage());
        }
    }

    private void facturaSeleccionada(){
        try{
            if(mFactura != null){
                mRVAdapter.updateItem(mFactura.getReferencia(), BASADO_EN);
                mRVAdapter.updateItem(String.valueOf(getNumberOfSelecteds(mFactura)) + " productos", PRODUCTOS);
                actualizarTotales();

                if(mFactura.getDireccionEntrega() != null)
                {
                    for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                        if(d.getCodigo().equals(mFactura.getDireccionEntrega())) {
                            mDireccionEntrega = d;
                            break;
                        }
                    }
                }

                if(mFactura.getDireccionFiscal() != null)
                {
                    for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                        if(d.getCodigo().equals(mFactura.getDireccionFiscal())) {
                            mDireccionFiscal = d;
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            showMessage("facturaSeleccionada() > " +  e.getMessage());
        }
    }

    @Override
    public void onItemClick(ItemAddCreditNote itemRow) {
        try{
            switch (itemRow.getTitulo()){
                case CODIGO_CLIENTE:
                    startActivityForResult(new Intent(this, ClienteBuscarActivity.class),
                            ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE);
                    break;
                case COMENTARIOS:
                    buildEditTextDialog(DIALOG_COMENTARIOS, InputType.TYPE_CLASS_TEXT, 100);
                    break;
                case FACTURAS:
                    if(mListFacturasTemp != null && mListFacturasTemp.size() > 0) {
                        Intent facturas = new Intent(this, NotaCreditoListaFacturaActivity.class);
                        facturas.putParcelableArrayListExtra(NotaCreditoListaFacturaActivity.KEY_PARAM_LISTA_FACTURAS, (ArrayList<? extends Parcelable>) mListFacturasTemp);
                        startActivityForResult(facturas, NotaCreditoListaFacturaActivity.REQUEST_CODE_LISTA_FACTURAS);
                    }else
                        showMessage("No hay facturas disponibles del cliente.");
                    break;
                case PRODUCTOS:
                    if(mFactura != null){
                        if(mFactura.getLineas() != null && mFactura.getLineas().size() > 0){
                            Intent articulos = new Intent(this,
                                    NotaCreditoArticulosActivity.class);
                            articulos.putParcelableArrayListExtra(NotaCreditoArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS,
                                    (ArrayList<? extends Parcelable>) mFactura.getLineas());
                            startActivityForResult(articulos, NotaCreditoArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS);
                        }else
                            showMessage("La factura no tiene productos disponibles.");
                    }else
                        showMessage("No se ha seleccionado ninguna factura.");
                    break;
                case BASADO_EN:
                    if(mFactura != null){
                        Intent intent = new Intent(this, NotaCreditoDetalleActivity.class);
                        intent.putExtra(NotaCreditoDetalleActivity.KEY_PARAM_FACT, mFactura);
                        startActivity(intent);
                    }
                    break;
            }
        }catch (Exception e){
            showMessage("onItemClick > " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.abFirstAdd:
                if(validarDatos())
                    registrarNotaCredito();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    //region UTILS

    private void showMessage(String message){
        Toast.makeText(NotaCreditoActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void buildEditTextDialog(final String who, int inputType, int maxLength){
        try{
            String title = "";
            final EditText editText = new EditText(this);
            editText.setRawInputType(inputType);
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editText.setFilters(fArray);
            editText.setMaxLines(1);
            editText.setFocusableInTouchMode(true);

            if(who.equals(DIALOG_COMENTARIOS)) {
                title = "Comentarios";
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(title);
            alert.setCancelable(false);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(who.equals(DIALOG_COMENTARIOS)){
                        String comments = editText.getText() != null ? editText.getText().toString() : "";
                        mRVAdapter.updateItem(comments, COMENTARIOS);
                        mostrarTeclado(editText, false);
                    }
                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(who.equals(DIALOG_COMENTARIOS)) {
                        mostrarTeclado(editText, false);
                    }
                }
            });

            if(editText.getParent() != null)
                ((ViewGroup) editText.getParent()).removeView(editText);

            alert.setView(editText);
            editText.requestFocus();
            mostrarTeclado(null, true);
            alert.show();
        }catch(Exception e){
            showMessage("buildEditTextDialog() > " + e.getMessage());
        }
    }

    private void mostrarTeclado(EditText editText, boolean show){
        if(show){
            InputMethodManager imm = (InputMethodManager) (this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }else{
            InputMethodManager imm = (InputMethodManager) (this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private int getNumberOfSelecteds(FacturaBean mFactura){
        int res = 0;

        for (FacturaDetalleBean b : mFactura.getLineas()) {
            if(b.isSelected())
                res++;
        }

        return res;
    }

    private void actualizarTotales(){
        try{
            mTotalAntesDescuento = 0d;
            mImpuesto = 0d;
            mTotal = 0d;

            for (FacturaDetalleBean b : mFactura.getLineas()) {
                if (b.isSelected()) {

                    double totalLinea = Double.parseDouble(b.getPrecioUnitario()) *
                            Double.parseDouble(b.getCantidadTemp());
                    double tasaImpuesto = new ImpuestoDAO().obtenerTasa(b.getImpuesto()) / 100.0;

                    mTotalAntesDescuento += Math.round(totalLinea * 100.0)/100.0;
                    mImpuesto += Math.round((tasaImpuesto * totalLinea) * 100.0)/100.0;
                }
            }
            mTotal = Math.round((mTotalAntesDescuento + mImpuesto) * 100.0)/100.0;

            mRVAdapter.updateItem(String.valueOf(mTotalAntesDescuento), TOTAL_ANTES_DESCUENTO);
            mRVAdapter.updateItem(String.valueOf(mImpuesto), TOTAL_IMPUESTO);
            mRVAdapter.updateItem(String.valueOf(mTotal), TOTAL);

        }catch(Exception e){
            showMessage("actualizarTotales() > " + e.getMessage());
        }
    }

    private boolean validarDatos(){
        boolean result = true;
        try
        {
            if(mCliente == null){
                result = false;
                showMessage("Primero, debe seleccionar a un cliente.");
            }else if(mFactura == null){
                result = false;
                showMessage("Primero, debe seleccionar una factura.");
            }else if(mFactura.getLineas() == null || mFactura.getLineas().size() <= 0){
                result = false;
                showMessage("No se puede registrar una nota de credito sin productos seleccionados.");
            }
        }catch(Exception e){
            showMessage("validarDatos() > " + e.getMessage());
        }
        return result;
    }

    private void registrarNotaCredito(){
        try
        {
            mNotaCredito = new NotaCreditoBean();
            mNotaCredito.setClave(mNroDocumento);
            mNotaCredito.setClaveMovil(mNroDocumento);
            mNotaCredito.setSocioNegocio(mCliente.getCodigo());
            mNotaCredito.setListaPrecio(mFactura.getListaPrecio() != null ? Integer.parseInt(mFactura.getListaPrecio()) : null);
            mNotaCredito.setContacto(mContacto != null? mContacto.getCodigo() : null);
            mNotaCredito.setMoneda(mFactura.getMoneda());
            mNotaCredito.setEmpleadoVenta(mCodigoEmpleado);
            mNotaCredito.setComentario(mRVAdapter.getItem(COMENTARIOS));
            mNotaCredito.setFechaContable(mFechaActual);
            mNotaCredito.setFechaVencimiento(mFechaActual);
            mNotaCredito.setDireccionFiscal(mDireccionFiscal != null ? mDireccionFiscal.getCodigo() : null);
            mNotaCredito.setDireccionEntrega(mDireccionEntrega != null ? mDireccionEntrega.getCodigo() : null);
            mNotaCredito.setCondicionPago(mFactura.getCondicionPago());
            mNotaCredito.setIndicador(mFactura.getIndicador());
            mNotaCredito.setClaveBase(String.valueOf(mFactura.getClave()));
            mNotaCredito.setEstadoMovil(getResources().getString(R.string.LOCAL));
            mNotaCredito.setSubTotal(mTotalAntesDescuento.toString());
            mNotaCredito.setImpuesto(mImpuesto.toString());
            mNotaCredito.setTotal(mTotal.toString());

            mNotaCredito.setFechaCreacion(obtenerFechaHora(PATTERN_FECHA));
            mNotaCredito.setHoraCreacion(obtenerFechaHora(PATTERN_HORA));
            mNotaCredito.setModoOffline(existsNetworkConnection() ? "N" : "Y");

            mCurrentLocation = getCurrentLocation();
            if(mCurrentLocation != null){
                mNotaCredito.setLatitud(String.valueOf(mCurrentLocation.getLatitude()));
                mNotaCredito.setLongitud(String.valueOf(mCurrentLocation.getLongitude()));
            }

            List<NotaCreditoDetalleBean> lineas = new ArrayList<>();
            int numerador = 0;
            for (FacturaDetalleBean d: mFactura.getLineas()) {
                if(d.isSelected()) {
                    NotaCreditoDetalleBean detalle = new NotaCreditoDetalleBean();
                    detalle.setClaveNotaCredito(mNroDocumento);
                    detalle.setLinea(String.valueOf(numerador));
                    detalle.setLineaBase(String.valueOf(d.getLinea()));
                    detalle.setArticulo(d.getArticulo());
                    detalle.setUnidadMedida(d.getUnidadMedida());
                    detalle.setAlmacen(d.getAlmacen());
                    String cantidad = d.getCantidadTemp();
                    detalle.setCantidad(cantidad);
                    detalle.setListaPrecio(d.getListaPrecio() != null ? Integer.parseInt(d.getListaPrecio()) : null);
                    detalle.setPrecioUnitario(d.getPrecioUnitario());
                    detalle.setImpuesto(d.getImpuesto());

                    List<NotaCreditoDetalleLoteBean> lotes = new ArrayList<>();
                    if(d.getLotes() != null) {

                        for (FacturaDetalleLoteBean lote : d.getLotes()) {
                            NotaCreditoDetalleLoteBean loteNc = new NotaCreditoDetalleLoteBean();
                            loteNc.setLote(lote.getLote());
                            loteNc.setCantidad(lote.getCantidadTemp());
                            loteNc.setLineaBase(Integer.parseInt(detalle.getLinea()));
                            loteNc.setClaveBase(detalle.getClaveNotaCredito());
                            lotes.add(loteNc);
                        }
                    }
                    detalle.setLotes(lotes);

                    lineas.add(detalle);
                    numerador++;
                }
            }

            mNotaCredito.setLineas(lineas);

            if(new NotaCreditoDAO().insertNotaCredito(mNotaCredito)){
                CorrelativoDAO.updateCorrelativo("NOT");
                showMessage("Se registro la nota de credito con exito.");

                try{
                    showMessage("Enviando al servidor...");
                    enviarNotaCreditoAServidor(mNotaCredito);
                }catch(Exception e){
                    showMessage("Server exception " + e.getMessage());
                }finally {
                    finish();
                }

            }else
                showMessage("Ocurrio un error intentando registrar el documento.");

        }catch(Exception e){
            showMessage("registrarNotaCredito() > " + e.getMessage());
        }
    }

    //endregion

    private String obtenerFechaHora(String pattern){
        String fecha = new SimpleDateFormat(pattern).format(new Date());
        return fecha;
    }


    //region MAP FUNCTIONS

    private Location getCurrentLocation(){
        try{
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mLocationListener);
            if (!this.checkGPSIsEnabled()) {
                showInfoAlert();
            } else {
                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                return location;
            }
		/*if (location == null) {
			location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} */
        }catch (Exception e){
            showMessage("getCurrentLocation() > " + e.getMessage());
            return null;
        }
        return null;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //createOrUpdateMarkerByLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private boolean checkGPSIsEnabled() {
        try {
            int locationMode = 0;
            String locationProviders;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                try {
                    locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    showMessage("checkGPSIsEnabled() SDKV>19 > " + e.getMessage());
                    return false;
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF;

            }else{
                locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }


        } catch (Exception e) {
            showMessage("checkGPSIsEnabled() > " + e.getMessage());
            return false;
        }
    }

    private void showInfoAlert() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Señal GPS")
                .setMessage("No tienes señal GPS. Quieres habilitarla?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //El GPS no está activado
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    //endregion

    private void enviarNotaCreditoAServidor(final NotaCreditoBean notaCredito){

        if (existsNetworkConnection()) {

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            JSONObject jsonObject = NotaCreditoBean.transformNotaCreditoToJSON(notaCredito, sociedad);

            if(jsonObject != null){
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(Request.Method.POST, ruta + "creditmemo/addCreditMemo.xsjs", jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try
                                        {
                                            if(response.getString("ResponseStatus").equals("Success")){
                                                new NotaCreditoDAO().actualizarSincronizado(notaCredito.getClaveMovil());
                                            }else{
                                                showMessage(response.getJSONObject("Response")
                                                        .getJSONObject("message")
                                                        .getString("value"));
                                            }

                                        }catch (Exception e){
                                            showMessage("Response - " + e.getMessage());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {showMessage("VolleyError - " + error.getMessage());}
                                }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                return headers;
                            }
                        };
                VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            }else
                showMessage("Error preparando el objeto para el envio...");
        }
    }

    private boolean existsNetworkConnection(){
        boolean wifi = Connectivity.isConnectedWifi(this);
        boolean movil = Connectivity.isConnectedMobile(this);
        boolean isConnectionFast = Connectivity.isConnectedFast(this);

        return (wifi || movil) && isConnectionFast;
    }
}
