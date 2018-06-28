package com.proyecto.devoluciones;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.DevolucionBean;
import com.proyecto.bean.DevolucionDetalleBean;
import com.proyecto.bean.DevolucionDetalleLoteBean;
import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.ClienteDAO;
import com.proyecto.dao.CorrelativoDAO;
import com.proyecto.dao.DevolucionDAO;
import com.proyecto.dao.EntregaDAO;
import com.proyecto.dao.ImpuestoDAO;
import com.proyecto.devoluciones.adapter.recyclerview.ItemAddDevolucion;
import com.proyecto.devoluciones.adapter.recyclerview.RVAdapterAddDevolucion;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterAddDevolucion;
import com.proyecto.entregas.EntregaDetalleActivity;
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

public class DevolucionActivity extends AppCompatActivity implements IRVAdapterAddDevolucion{

    public static int REQUEST_CODE_CREAR_DEVOLUCION = 11;
    public static String KEY_PARAM_ENTREGA = "kpEntrega";
    private DevolucionBean mDevolucion;

    private SharedPreferences mSharedPreferences;
    private String mNroDocumento, mCodigoEmpleado, mNombreEmpleado, mIdDispositivo, mFechaActual;
    private Double mImpuesto = 0d, mTotalAntesDescuento = 0d, mTotal = 0d;

    private RecyclerView mRecyclerView;
    private RVAdapterAddDevolucion mRVAdapter;
    private ClienteBuscarBean mCliente;
    private ContactoBuscarBean mContacto, mContactoTemp;
    private DireccionBuscarBean mDireccionFiscal, mDireccionFiscalTemp,
            mDireccionEntrega, mDireccionEntregaTemp;
    private List<ItemAddDevolucion> mListRows;
    private EntregaBean mEntrega;
    private List<EntregaBean> mListEntregasTemp;

    //region UTIL_CONSTANTS
    private final static String DIALOG_CONTACTOS = "contactosSN";
    private final static String DIALOG_DIRECCION_FISCAL = "direccionFiscalSN";
    private final static String DIALOG_DIRECCION_ENTREGA = "direccionEntregaSN";
    private final static String DIALOG_COMENTARIOS = "comentarios";
    //endregion

    //region SCREEN_ROWS
    private final static String TITULO_CODIGO = "Codigo devolucion";
    private final static String CODIGO_CLIENTE = "Codigo cliente";
    private final static String NOMBRE_CLIENTE = "Nombre cliente";
    private final static String CODIGO_CONTACTO = "Codigo contacto";
    private final static String NOMBRE_CONTACTO = "Nombre contacto";
    private final static String DIRECCION_ENTREGA = "Direccion de entrega";
    private final static String DIRECCION_FISCAL = "Direccion fiscal";
    private final static String VENDEDOR = "Vendedor";
    private final static String COMENTARIOS = "Comentarios";
    private final static String FECHA_CONTABLE = "Fecha contable";
    private final static String ENTREGAS = "Entregas del cliente";
    private final static String BASADO_EN = "Basado en entrega";
    private final static String PRODUCTOS = "Articulos";
    private final static String TOTAL_ANTES_DESCUENTO = "Total antes de impuesto";
    private final static String TOTAL_IMPUESTO = "Total impuesto";
    private final static String TOTAL = "Total";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbAddDevolucion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlAgregarDevolucion);

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        mCodigoEmpleado = mSharedPreferences.getString(Variables.CODIGO_EMPLEADO, "");
        mNombreEmpleado = mSharedPreferences.getString(Variables.NOMBRE_EMPLEADO, "");
        mIdDispositivo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvAddDevolucion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(DevolucionActivity.this));
        mRecyclerView.setHasFixedSize(true);

        mRVAdapter = new RVAdapterAddDevolucion(DevolucionActivity.this);
        mRecyclerView.setAdapter(mRVAdapter);
        mListRows = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView();

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_ENTREGA)){
                mEntrega = getIntent().getParcelableExtra(KEY_PARAM_ENTREGA);
                if(mEntrega != null) {
                    mCliente = new ClienteDAO().buscarPorCodigo(mEntrega.getSocioNegocio());
                    clienteSeleccionado();
                    entregaSeleccionada();
                    iniciarValoresPorDefecto();
                }
            }
        }
    }

    //Inicio de la lista de datos
    private void initRecyclerView(){
        mFechaActual = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date());
        String fullDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());
        int nroDev = CorrelativoDAO.obtenerUltimoNumero("DEV");
        mNroDocumento = mIdDispositivo +"-"+fullDate+"-" + nroDev;

        if(mListRows == null || mListRows.size() == 0){
            mListRows = new ArrayList<>();
            mDevolucion = new DevolucionBean();
            mListRows.add(new ItemAddDevolucion(TITULO_CODIGO, mNroDocumento, false));
            mListRows.add(new ItemAddDevolucion(FECHA_CONTABLE, StringDateCast.castStringtoDate(mFechaActual), false));
            mListRows.add(new ItemAddDevolucion(CODIGO_CLIENTE, true));
            mListRows.add(new ItemAddDevolucion(NOMBRE_CLIENTE, false));
          //  mListRows.add(new ItemAddDevolucion(CODIGO_CONTACTO, true));
          //  mListRows.add(new ItemAddDevolucion(NOMBRE_CONTACTO, false));
            mListRows.add(new ItemAddDevolucion(DIRECCION_FISCAL, true));
            mListRows.add(new ItemAddDevolucion(DIRECCION_ENTREGA, true));
            mListRows.add(new ItemAddDevolucion(VENDEDOR, mNombreEmpleado, false));
            mListRows.add(new ItemAddDevolucion(COMENTARIOS, true));

            mRVAdapter.clearAndAddAll(mListRows);
        }else{
            iniciarValoresPorDefecto();
        }
    }

    private void iniciarValoresPorDefecto(){
        try{
            for (ItemAddDevolucion item: mListRows) {
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
                    /*case CODIGO_CONTACTO:
                        if(mContacto != null)
                            item.setContenido(String.valueOf(mContacto.getCodigo()));
                        break;
                    case NOMBRE_CONTACTO:
                        if(mContacto != null)
                            item.setContenido(mContacto.getNombre());
                        break;  */
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
                    mEntrega = null;
                }
            }else if(resultCode == RESULT_OK && requestCode == DevolucionListaEntregaActivity.REQUEST_CODE_LISTA_ENTREGAS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(DevolucionListaEntregaActivity.KEY_PARAM_ENTREGA_SELECTED)){
                    mEntrega = data.getParcelableExtra(DevolucionListaEntregaActivity.KEY_PARAM_ENTREGA_SELECTED);
                    entregaSeleccionada();
                }else
                    showMessage("No se ha recibido informacion sobre la entrega seleccionada.");
            }else if(resultCode == RESULT_OK && requestCode == DevolucionArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS){
                if(data.getExtras() != null &&
                        data.getExtras().containsKey(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN)){
                    List<EntregaDetalleBean> detalles = data.getParcelableArrayListExtra(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS_RETURN);
                    if(detalles.size() > 0){
                        mEntrega.getLineas().clear();
                        mEntrega.getLineas().addAll(detalles);
                        mRVAdapter.updateItem(String.valueOf(getNumberOfSelecteds(mEntrega)) + " productos", PRODUCTOS);
                        actualizarTotales();
                    }
                }else
                    showMessage("No se ha recibido información sobre los productos seleccionados.");
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
                mListEntregasTemp = new EntregaDAO().listar(mCliente.getCodigo());

                if (mListEntregasTemp.size() > 0) {
                    mRVAdapter.addItem(new ItemAddDevolucion(ENTREGAS, "Disponible: " +
                            String.valueOf(mListEntregasTemp.size()), true));
                    mRVAdapter.addItem(new ItemAddDevolucion(BASADO_EN, true));
                    mRVAdapter.addItem(new ItemAddDevolucion(PRODUCTOS, "0 productos", true));
                    mRVAdapter.addItem(new ItemAddDevolucion(TOTAL_ANTES_DESCUENTO, false));
                    mRVAdapter.addItem(new ItemAddDevolucion(TOTAL_IMPUESTO, false));
                    mRVAdapter.addItem(new ItemAddDevolucion(TOTAL, false));
                } else {
                    showMessage("El cliente no tiene entregas registradas.");
                }
            }
        }catch(Exception e){
            showMessage("clienteSeleccionado() > " + e.getMessage());
        }
    }

    private void entregaSeleccionada(){
        try{
            if(mEntrega != null){
                mRVAdapter.updateItem(mEntrega.getReferencia(), BASADO_EN);
                mRVAdapter.updateItem(String.valueOf(getNumberOfSelecteds(mEntrega)) + " productos", PRODUCTOS);
                actualizarTotales();

                if(mEntrega.getDireccionEntrega() != null)
                {
                    for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                        if(d.getCodigo().equals(mEntrega.getDireccionEntrega())) {
                            mDireccionEntrega = d;
                            break;
                        }
                    }
                }

                if(mEntrega.getDireccionFiscal() != null)
                {
                    for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                        if(d.getCodigo().equals(mEntrega.getDireccionFiscal())) {
                            mDireccionFiscal = d;
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            showMessage("entregaSeleccionada() > " +  e.getMessage());
        }
    }

    @Override
    public void onItemClick(ItemAddDevolucion itemRow) {
        try{
            switch (itemRow.getTitulo()){
                case CODIGO_CLIENTE:
                    startActivityForResult(new Intent(DevolucionActivity.this, ClienteBuscarActivity.class),
                            ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE);
                    break;
                case CODIGO_CONTACTO:
                    if(mCliente != null){
                        if(mCliente.getContactos() != null &&
                                mCliente.getContactos().size() > 0){
                            buildSingleChoiceDialog(DIALOG_CONTACTOS);
                        }else
                            showMessage("El cliente no tiene contactos disponibles.");
                    }else
                        showMessage("Primero, debe seleccionar a un cliente.");
                    break;
                case DIRECCION_FISCAL:
                    if(mCliente != null){
                        if(mCliente.getDirecciones() != null &&
                                mCliente.getDirecciones().size() > 0){
                            buildSingleChoiceDialog(DIALOG_DIRECCION_FISCAL);
                        }else
                            showMessage("El cliente no tiene direcciones disponibles.");
                    }else
                        showMessage("Primero, debe seleccionar a un cliente.");
                    break;
                case DIRECCION_ENTREGA:
                    if(mCliente != null &&
                            mCliente.getDirecciones().size() > 0){
                        buildSingleChoiceDialog(DIALOG_DIRECCION_ENTREGA);
                    }else
                        showMessage("Primero, debe seleccionar a un cliente.");
                    break;
                case COMENTARIOS:
                    buildEditTextDialog(DIALOG_COMENTARIOS, InputType.TYPE_CLASS_TEXT, 100);
                    break;
                case ENTREGAS:
                    if(mListEntregasTemp != null && mListEntregasTemp.size() > 0) {
                        Intent entregas = new Intent(DevolucionActivity.this, DevolucionListaEntregaActivity.class);
                        entregas.putParcelableArrayListExtra(DevolucionListaEntregaActivity.KEY_PARAM_LISTA_ENTREGAS, (ArrayList<? extends Parcelable>) mListEntregasTemp);
                        startActivityForResult(entregas, DevolucionListaEntregaActivity.REQUEST_CODE_LISTA_ENTREGAS);
                    }else
                        showMessage("No hay entregas disponibles del cliente.");
                    break;
                case PRODUCTOS:
                    if(mEntrega != null){
                        if(mEntrega.getLineas() != null && mEntrega.getLineas().size() > 0){
                            Intent articulos = new Intent(this,
                                    DevolucionArticulosActivity.class);
                            articulos.putParcelableArrayListExtra(DevolucionArticulosActivity.KEY_PARAM_LISTA_PRODUCTOS,
                                    (ArrayList<? extends Parcelable>) mEntrega.getLineas());
                            startActivityForResult(articulos, DevolucionArticulosActivity.REQUEST_CODE_LISTA_PRODUCTOS);
                        }else
                            showMessage("La entrega no tiene productos disponibles.");
                    }else
                        showMessage("No se ha seleccionado ninguna entrega.");
                    break;
                case BASADO_EN:
                    if(mEntrega != null){
                        Intent intent = new Intent(this, EntregaDetalleActivity.class);
                        intent.putExtra(EntregaDetalleActivity.KEY_PARAM_ENTREGA, mEntrega);
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
                    registrarDevolucion();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    //region FUNCIONES PROPIAS
    private void showMessage(String message){
        if(message != null)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void buildSingleChoiceDialog(final String who){
        try{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.select_dialog_singlechoice);

            String titleDialog = "";

            if(who.equals(DIALOG_CONTACTOS)){
                for (ContactoBuscarBean d: mCliente.getContactos()) {
                    arrayAdapter.add(d.getCodigo() + " - " + d.getNombre());
                }
                titleDialog = "Contactos del cliente";
            }else if(who.equals(DIALOG_DIRECCION_FISCAL)){
                for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                    if(d.getTipo() != null && d.getTipo().equals(Constantes.TIPO_DIRECCION_FISCAL))
                        arrayAdapter.add(d.getCodigo() +  " - " + d.getCalle());
                }
                titleDialog = "Direccion fiscal";
            }else if(who.equals(DIALOG_DIRECCION_ENTREGA)){
                for (DireccionBuscarBean d: mCliente.getDirecciones()) {
                    if(d.getTipo() != null && d.getTipo().equals(Constantes.TIPO_DIRECCION_ENTREGA))
                        arrayAdapter.add(d.getCodigo() +  " - " + d.getCalle());
                }
                titleDialog = "Direccion entrega";
            }

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(titleDialog);
            alertDialog.setCancelable(false);
            alertDialog.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(who.equals(DIALOG_DIRECCION_FISCAL)){
                        mDireccionFiscalTemp = mCliente.getDirecciones().get(which);
                    }else if(who.equals(DIALOG_DIRECCION_ENTREGA)){
                        mDireccionEntregaTemp = mCliente.getDirecciones().get(which);
                    }else if(who.equals(DIALOG_CONTACTOS)){
                        mContactoTemp = mCliente.getContactos().get(which);
                    }
                }
            });
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(who.equals(DIALOG_CONTACTOS)){
                        if (mContactoTemp != null) {
                            mContacto = mContactoTemp;
                            mRVAdapter.updateItem(String.valueOf(mContacto.getCodigo()), CODIGO_CONTACTO);
                            mRVAdapter.updateItem(mContacto.getNombre(), NOMBRE_CONTACTO);
                        }
                        mContactoTemp = null;
                    }else if(who.equals(DIALOG_DIRECCION_FISCAL)){
                        if (mDireccionFiscalTemp != null) {
                            mDireccionFiscal = mDireccionFiscalTemp;
                            mRVAdapter.updateItem(mDireccionFiscal.getCalle(), DIRECCION_FISCAL);
                        }
                        mDireccionFiscalTemp = null;
                    }else if(who.equals(DIALOG_DIRECCION_ENTREGA)){
                        if (mDireccionEntregaTemp != null) {
                            mDireccionEntrega = mDireccionEntregaTemp;
                            mRVAdapter.updateItem(mDireccionEntrega.getCalle(), DIRECCION_ENTREGA);
                        }
                        mDireccionEntregaTemp = null;
                    }
                }
            });
            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(who.equals(DIALOG_CONTACTOS)){
                        mContactoTemp = null;
                    }else if(who.equals(DIALOG_DIRECCION_FISCAL)){
                        mDireccionFiscalTemp = null;
                    }else if(who.equals(DIALOG_DIRECCION_ENTREGA)){
                        mDireccionEntregaTemp = null;
                    }
                }
            });
            alertDialog.show();

        }catch(Exception e){
            showMessage("buildSingleChoiceDialog() > " + e.getMessage());
        }
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

    private int getNumberOfSelecteds(EntregaBean mEntrega){
        int res = 0;

        for (EntregaDetalleBean b : mEntrega.getLineas()) {
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

            for (EntregaDetalleBean b : mEntrega.getLineas()) {
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
            }else if(mEntrega == null){
                result = false;
                showMessage("Primero, debe seleccionar una entrega.");
            }else if(mEntrega.getLineas() == null || mEntrega.getLineas().size() <= 0){
                result = false;
                showMessage("No se puede registrar una devolución sin productos seleccionados.");
            }
        }catch(Exception e){
            showMessage("validarDatos() > " + e.getMessage());
        }
        return result;
    }

    private void registrarDevolucion(){
        try
        {
            mDevolucion = new DevolucionBean();
            mDevolucion.setClave(mNroDocumento);
            mDevolucion.setClaveMovil(mNroDocumento);
            mDevolucion.setSocioNegocio(mCliente.getCodigo());
            mDevolucion.setListaPrecio(mEntrega.getListaPrecio());
            mDevolucion.setContacto(mContacto != null? mContacto.getCodigo() : null);
            mDevolucion.setMoneda(mEntrega.getMoneda());
            mDevolucion.setEmpleadoVenta(mCodigoEmpleado);
            mDevolucion.setComentario(mRVAdapter.getItem(COMENTARIOS));
            mDevolucion.setFechaContable(mFechaActual);
            mDevolucion.setFechaVencimiento(mFechaActual);
            mDevolucion.setDireccionFiscal(mDireccionFiscal != null ? mDireccionFiscal.getCodigo() : null);
            mDevolucion.setDireccionEntrega(mDireccionEntrega != null ? mDireccionEntrega.getCodigo() : null);
            mDevolucion.setCondicionPago(mEntrega.getCondicionPago());
            mDevolucion.setIndicador(mEntrega.getIndicador());
            mDevolucion.setClaveBase(String.valueOf(mEntrega.getClave()));
            mDevolucion.setEstadoMovil(getResources().getString(R.string.LOCAL));
            mDevolucion.setSubTotal(mTotalAntesDescuento.toString());
            mDevolucion.setImpuesto(mImpuesto.toString());
            mDevolucion.setTotal(mTotal.toString());

            List<DevolucionDetalleBean> lineas = new ArrayList<>();
            int numerador = 0;
            for (EntregaDetalleBean d: mEntrega.getLineas()) {
                if(d.isSelected()) {
                    DevolucionDetalleBean detalle = new DevolucionDetalleBean();
                    detalle.setClaveDevolucion(mNroDocumento);
                    detalle.setLinea(String.valueOf(numerador));
                    detalle.setLineaBase(String.valueOf(d.getLinea()));
                    detalle.setArticulo(d.getArticulo());
                    detalle.setUnidadMedida(d.getUnidadMedida());
                    detalle.setAlmacen(d.getAlmacen());
                    String cantidad = d.getCantidadTemp();
                    detalle.setCantidad(cantidad);
                    detalle.setListaPrecio(d.getListaPrecio());
                    detalle.setPrecioUnitario(d.getPrecioUnitario());
                    detalle.setImpuesto(d.getImpuesto());

                    List<DevolucionDetalleLoteBean> lotesDev = new ArrayList<>();
                    if(d.getLotes() != null) {

                        for (EntregaDetalleLoteBean lote : d.getLotes()) {
                            DevolucionDetalleLoteBean lotedev = new DevolucionDetalleLoteBean();
                            lotedev.setLote(lote.getLote());
                            lotedev.setCantidad(lote.getCantidadTemp());
                            lotedev.setLineaBase(Integer.parseInt(detalle.getLinea()));
                            lotedev.setClaveBase(detalle.getClaveDevolucion());
                            lotesDev.add(lotedev);
                        }
                    }
                    detalle.setLotes(lotesDev);

                    lineas.add(detalle);
                    numerador++;
                }
            }

            mDevolucion.setLineas(lineas);

            if(new DevolucionDAO().insertDevolucion(mDevolucion)){
                CorrelativoDAO.updateCorrelativo("DEV");
                showMessage("Se registro la devolucion con exito.");

                try{
                    showMessage("Enviando al servidor...");
                    enviarDevolucionAServidor(mDevolucion);
                }catch(Exception e){
                    showMessage("Server exception " + e.getMessage());
                }finally {
                    finish();
                }

            }else
                showMessage("Ocurrio un error intentando registrar el documento.");

        }catch(Exception e){
            showMessage("registrarDevolucion() > " + e.getMessage());
        }
    }
    //endregion


    private void enviarDevolucionAServidor(final DevolucionBean devolucion){

        if (existsNetworkConnection()) {

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            JSONObject jsonObject = DevolucionBean.transformDevolucionToJSON(devolucion, sociedad);

            if(jsonObject != null){
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