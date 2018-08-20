package com.proyecto.incidencias;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.IncidenciaBean;
import com.proyecto.bean.MotivoBean;
import com.proyecto.bean.TipoPersonaBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.ClienteDAO;
import com.proyecto.dao.CorrelativoDAO;
import com.proyecto.dao.FacturaDAO;
import com.proyecto.dao.IncidenciaDAO;
import com.proyecto.dao.MotivoDAO;
import com.proyecto.facturas.BuscarFacturaActivity;
import com.proyecto.facturas.util.FacturaBuscarBean;
import com.proyecto.incidencias.adapter.recyclerview.ItemAddIncidencia;
import com.proyecto.incidencias.adapter.recyclerview.RVAdapterAddIncidencia;
import com.proyecto.incidencias.adapter.recyclerview.listeners.IRVAdapterAddIncidenciaListener;
import com.proyecto.incidencias.adapter.spinner.SPAdapterContacto;
import com.proyecto.incidencias.adapter.spinner.SPAdapterDireccion;
import com.proyecto.movil.PlaceholderFragment;
import com.proyecto.reportes.BuscarSocioReporteFragment;
import com.proyecto.reportes.ReporteFragment;
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

public class IncidenciaActivity extends AppCompatActivity implements IRVAdapterAddIncidenciaListener{

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public final static String KEY_PAR_ORIGEN = "origen";
    public final static String KEY_PAR_CLIENTE = "cliente";
    public final static String KEY_PAR_FACTURA = "factura";
    public final static String KEY_PAR_REFERENCIA = "referencia";

    public final static String ORDEN = "Orden venta";
    public final static String ENTREGA = "Entrega mercancia";
    public final static String FACTURA = "Factura de cliente";

    private EditText mEdtMotivo,
            mEdtComentarios  ,
            mEdtLatitud  ,
            mEdtLongitud ,
            mEdtFechaCreacion ,
            mEdtHoraCreacion ,
            mEdtEstadoConexion,
            mEdtTipoIncidencia,
            mEdtFechaPago;

    private Button mBtnTomarFoto;
    private Bitmap mBitMapImage;
    private SharedPreferences mSharedPreferences;
    private Location mCurrentLocation = null;

    private RVAdapterAddIncidencia mRVAdapterAddIncidencia;
    private RecyclerView mRecyclerView;
    private String mOrigenIncidencia;
    private List<ItemAddIncidencia> mLstItems;
    private List<MotivoBean> mLstMotivos;
    private FacturaBuscarBean mFacturaSeleccionada;
    private ClienteBuscarBean mClienteSeleccionado;
    private DireccionBuscarBean mDireccionSeleccionada;
    private ContactoBuscarBean mContactoSeleccionado;
    private MotivoBean mMotivoSeleccionado;
    private String mClaveMovil, mCodigoEmpleado, mNombreEmpleado, mIdDispositivo, mReferencia, mClaveEntrega;

    private final String ESTADO_CONECTADO = "Conectado";
    private final String ESTADO_DESCONECTADO = "Sin conexion";
    public final static String PARCIALMENTE_RECHAZADO = "Parcialmente Rechazado";
    public final static String TOTALMENTE_RECHAZADO = "Totalmente Rechazado";
    public final static String CODIGO_PARCIALMENTE_RECHAZADO = "01";
    public final static String CODIGO_TOTALMENTE_RECHAZADO = "02";
    private final String PATTERN_FECHA = "yyyy/MM/dd";
    private final String PATTERN_HORA = "HH:mm";

    //region CAMPOS_FORMULARIO
    private final String TITULO_CLAVE_MOVIL = "Codigo incidencia";
    private final String TITULO_ORIGEN = "Origen";
    private final String TITULO_CODIGO_CLIENTE = "Codigo cliente";
    private final String TITULO_NOMBRE_CLIENTE = "Nombre cliente";
    private final String TITULO_CODIGO_CONTACTO = "Codigo de contacto";
    private final String TITULO_PERSONA_CONTACTO = "Nombre de contacto";
    private final String TITULO_COD_DIRECCION = "Codigo direccion";
    private final String TITULO_DET_DIRECCION = "Detalle direccion";
    private final String TITULO_MOTIVO = "Motivo";
    private final String TITULO_COMENTARIOS = "Comentarios";
    private final String TITULO_COD_VENDEDOR = "Codigo vendedor";
    private final String TITULO_LATITUD = "Latitud";
    private final String TITULO_LONGITUD = "Longitud";
    private final String TITULO_FECHA_CREACION = "Fecha creacion";
    private final String TITULO_HORA_CREACION = "Hora creacion";
    private final String TITULO_ESTADO_CONEXION = "Estado de conexion";
    private final String TITULO_TIPO_INCIDENCIA = "Tipo de incidencia";
    private final String TITULO_SERIE_FACTURA = "Serie factura";
    private final String TITULO_CORR_FACTURA = "Correlativo factura";
    private final String TITULO_FECHA_PAGO = "Fecha de compromiso de pago";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbAddIncidencia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.ttlAgregarIncidencia);

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        mCodigoEmpleado = mSharedPreferences.getString(Variables.CODIGO_EMPLEADO, "");
        mNombreEmpleado = mSharedPreferences.getString(Variables.NOMBRE_EMPLEADO, "");
        mIdDispositivo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mBtnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
        mBtnTomarFoto.setOnClickListener(onClickListenerTomarFoto);

        //Inflo el recycler y configuramos el Adapter
        mRecyclerView = (RecyclerView) findViewById(R.id.rvAddIncidencia);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(IncidenciaActivity.this));
        mRecyclerView.setHasFixedSize(true);

        mRVAdapterAddIncidencia = new RVAdapterAddIncidencia(IncidenciaActivity.this);
        mRecyclerView.setAdapter(mRVAdapterAddIncidencia);

        mEdtMotivo = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtMotivo, InputType.TYPE_CLASS_TEXT, 100);
        mEdtComentarios = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtComentarios, InputType.TYPE_CLASS_TEXT, 100);
        mEdtLatitud = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtLatitud, InputType.TYPE_CLASS_TEXT, 100);
        mEdtLongitud = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtLongitud, InputType.TYPE_CLASS_TEXT, 100);
        mEdtFechaCreacion = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtFechaCreacion, InputType.TYPE_CLASS_TEXT, 100);
        mEdtHoraCreacion = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtHoraCreacion, InputType.TYPE_CLASS_TEXT, 100);
        mEdtEstadoConexion = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtEstadoConexion, InputType.TYPE_CLASS_TEXT, 100);
        mEdtTipoIncidencia = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtTipoIncidencia, InputType.TYPE_CLASS_TEXT, 100);
        mEdtFechaPago = new EditText(IncidenciaActivity.this);
        inicializarObjeto(mEdtFechaPago, InputType.TYPE_DATETIME_VARIATION_DATE, 100);

        mLstMotivos = new ArrayList<>();

        try{
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(KEY_PAR_ORIGEN)) {
                mOrigenIncidencia = getIntent().getExtras().getString(KEY_PAR_ORIGEN);

                switch (mOrigenIncidencia){
                    case ENTREGA:
                        if(getIntent().getExtras().containsKey(KEY_PAR_CLIENTE))
                            mClienteSeleccionado = new ClienteDAO().buscarPorCodigo(getIntent().getExtras().getString(KEY_PAR_CLIENTE));
                        if(getIntent().getExtras().containsKey(KEY_PAR_REFERENCIA))
                            mReferencia = getIntent().getExtras().getString(KEY_PAR_REFERENCIA);
                        if(getIntent().getExtras().containsKey(KEY_PAR_FACTURA))
                            mClaveEntrega = getIntent().getExtras().getString(KEY_PAR_FACTURA);
                        break;
                    case FACTURA:
                        if(getIntent().getExtras().containsKey(KEY_PAR_FACTURA)) {
                            mFacturaSeleccionada = new FacturaDAO().obtenerPorClave(getIntent().getExtras().getString(KEY_PAR_FACTURA));
                            mClienteSeleccionado = new ClienteDAO().buscarPorCodigo(mFacturaSeleccionada.getCodigoCliente());
                        }
                        break;
                    case ORDEN:
                        if(getIntent().getExtras().containsKey(KEY_PAR_CLIENTE))
                            mClienteSeleccionado = new ClienteDAO().buscarPorCodigo(getIntent().getExtras().getString(KEY_PAR_CLIENTE));
                        break;
                    default:
                        break;
                }
            }
            else
                mOrigenIncidencia = ORDEN;
        }catch(Exception e){
            showMessage("Error obteniendo datos desde la llamada - " + e.getMessage());
        }
    }

    private void inicializarObjeto(EditText editText, int inputType, int maxLength){
        editText.setRawInputType(inputType);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(fArray);
        editText.setMaxLines(1);
        editText.setFocusableInTouchMode(true);
    }

    private void inicializarForm(){

        try{
            String fullDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());
            mClaveMovil = mIdDispositivo + "-" + fullDate + "-" + (CorrelativoDAO.obtenerUltimoNumero("INC"));

            mBtnTomarFoto.setVisibility(View.VISIBLE);
            mLstItems = new ArrayList<>();
            mLstItems.add(new ItemAddIncidencia(TITULO_CLAVE_MOVIL, mClaveMovil, true));
            mLstItems.add(new ItemAddIncidencia(TITULO_ORIGEN, obtenerOrigenIncidencia(), true));

            if(mOrigenIncidencia.equals(ORDEN) || mOrigenIncidencia.equals(ENTREGA)){
                mLstItems.add(new ItemAddIncidencia(TITULO_CODIGO_CLIENTE, mClienteSeleccionado != null? mClienteSeleccionado.getCodigo() : "", true));
                mLstItems.add(new ItemAddIncidencia(TITULO_NOMBRE_CLIENTE, mClienteSeleccionado != null? mClienteSeleccionado.getNombre() : "", false));
                mLstItems.add(new ItemAddIncidencia(TITULO_CODIGO_CONTACTO, mContactoSeleccionado != null? String.valueOf(mContactoSeleccionado.getCodigo()) : "", true));
                mLstItems.add(new ItemAddIncidencia(TITULO_PERSONA_CONTACTO, mContactoSeleccionado != null? mContactoSeleccionado.getNombre() : "", false));
                mLstItems.add(new ItemAddIncidencia(TITULO_COD_DIRECCION, mDireccionSeleccionada != null? mDireccionSeleccionada.getCodigo() : "", true));
                mLstItems.add(new ItemAddIncidencia(TITULO_DET_DIRECCION, mDireccionSeleccionada != null? mDireccionSeleccionada.getCalle() : "", false));
                mLstItems.add(new ItemAddIncidencia(TITULO_MOTIVO, mEdtMotivo.getText() != null? mEdtMotivo.getText().toString() : "", true));
                mLstItems.add(new ItemAddIncidencia(TITULO_LATITUD, mCurrentLocation != null? String.valueOf(mCurrentLocation.getLatitude()) : "", false));
                mLstItems.add(new ItemAddIncidencia(TITULO_LONGITUD, mCurrentLocation != null? String.valueOf(mCurrentLocation.getLongitude()) : "", false));
            }

            if(mOrigenIncidencia.equals(FACTURA) || mOrigenIncidencia.equals(ENTREGA)){
                String serie ="", correlativo = "";
                if(mReferencia != null && !mReferencia.equals("")){
                    String[] ref = mReferencia.split("-");
                    if(ref != null && ref.length > 1){
                        serie = ref[0];
                        correlativo = ref[1];
                    }
                }else if(mFacturaSeleccionada != null){
                    serie = mFacturaSeleccionada.getSerie();
                    correlativo = String.valueOf(mFacturaSeleccionada.getCorrelativo());
                }

                mLstItems.add(new ItemAddIncidencia(TITULO_SERIE_FACTURA, serie, true));
                mLstItems.add(new ItemAddIncidencia(TITULO_CORR_FACTURA, correlativo, true));
            }

            if(mOrigenIncidencia.equals(ENTREGA)){
                mLstItems.add(new ItemAddIncidencia(TITULO_TIPO_INCIDENCIA, mEdtTipoIncidencia.getText() != null? mEdtTipoIncidencia.getText().toString() : "", true));
            }else if(mOrigenIncidencia.equals(FACTURA)){
                mLstItems.add(new ItemAddIncidencia(TITULO_FECHA_PAGO, mEdtFechaPago.getText() != null? mEdtFechaPago.getText().toString() : "", true));
            }

            mLstItems.add(new ItemAddIncidencia(TITULO_COD_VENDEDOR, mNombreEmpleado, false));
            mLstItems.add(new ItemAddIncidencia(TITULO_COMENTARIOS, mEdtComentarios.getText() != null? mEdtComentarios.getText().toString() : "", true));
            mLstItems.add(new ItemAddIncidencia(TITULO_FECHA_CREACION, obtenerFechaHora(PATTERN_FECHA), false));
            mLstItems.add(new ItemAddIncidencia(TITULO_HORA_CREACION, obtenerFechaHora(PATTERN_HORA), false));
            mLstItems.add(new ItemAddIncidencia(TITULO_ESTADO_CONEXION, comprobarConexion(), false));

            mRVAdapterAddIncidencia.clearAndAddAll(mLstItems);
            mLstMotivos.clear();

            String valOrden = "N", valEntrega= "N", valFactura = "N";
            switch (mOrigenIncidencia){
                case ORDEN:
                    valOrden = "Y";
                    valEntrega = "N";
                    valFactura = "N";
                    break;
                case ENTREGA:
                    valOrden = "N";
                    valEntrega = "Y";
                    valFactura = "N";
                    break;
                case FACTURA:
                    valOrden = "N";
                    valEntrega = "N";
                    valFactura = "Y";
                    mBtnTomarFoto.setVisibility(View.GONE);
                    break;
            }

            mLstMotivos.addAll(new MotivoDAO().listar(valOrden, valEntrega, valFactura));
        }catch (Exception e){
            showMessage("inicializarForm() > " + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocation = getCurrentLocation();
        inicializarForm();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if(resultCode == RESULT_OK && requestCode == BuscarFacturaActivity.REQUEST_CODE_BUSCAR_FACTURA) {

                mFacturaSeleccionada = data.getParcelableExtra(BuscarFacturaActivity.KEY_PARAM_FACTURA);
                String[] referencia = mFacturaSeleccionada.getReferencia().split("-");
                String serie = referencia[0];
                String correlativo = referencia[1];

                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_SERIE_FACTURA, serie, true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_CORR_FACTURA, correlativo, true));

                if(mOrigenIncidencia.equals(FACTURA)){
                    mClienteSeleccionado = new ClienteDAO().buscarPorCodigo(mFacturaSeleccionada.getCodigoCliente());
                }

            }else if(resultCode == RESULT_OK && requestCode == ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE) {

                mClienteSeleccionado = new ClienteBuscarBean();
                mClienteSeleccionado = data.getParcelableExtra(ClienteBuscarActivity.KEY_PARAM_CLIENTE);

                mDireccionSeleccionada = null;
                mContactoSeleccionado = null;
                mFacturaSeleccionada = null;

                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_CODIGO_CLIENTE, mClienteSeleccionado.getCodigo(), true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_NOMBRE_CLIENTE, mClienteSeleccionado.getNombre(), true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_SERIE_FACTURA, "", true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_CORR_FACTURA, "", true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_PERSONA_CONTACTO, "", true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_COD_DIRECCION, "", true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_DET_DIRECCION, "", true));

                autoSeleccionarDireccionMasCercana();

            }else if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){

                try{
                    Bundle extras = data.getExtras();
                    mBitMapImage = (Bitmap) extras.get("data");
                    mBtnTomarFoto.setText("VER FOTO");
                    mBtnTomarFoto.setBackgroundColor(Color.parseColor("#329932"));
                    //mImageView.setImageBitmap(imageBitmap);
                }catch (Exception e){
                    showMessage("REQUEST_IMAGE_CAPTURE - " + e.getMessage());
                }

            }
        }catch (Exception e){
            Toast.makeText(IncidenciaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                return true;
            case R.id.abFirstAdd:
                if(validarDatos())
                    registrarIncidencia();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region CAPTURAR_FOTO
    private View.OnClickListener onClickListenerTomarFoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mBitMapImage == null){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }else{
                Dialog builder = new Dialog(IncidenciaActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });

                ImageView imageView = new ImageView(IncidenciaActivity.this);
                imageView.setImageBitmap(mBitMapImage);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();
            }
        }
    };
    //endregion

    @Override
    public void onItemClick(ItemAddIncidencia item) {
        try{
            switch (item.getTitulo()){
                case TITULO_ORIGEN:
                    mostrarInputDialog("Origen Incidencia", true,
                            new String[]{ORDEN,ENTREGA,FACTURA},
                            spnOrigenIncidenciaListener,
                            mOrigenIncidencia.equals(ORDEN) ? 0 :
                                    (mOrigenIncidencia.equals(ENTREGA)? 1 : 2),
                            null,
                            dlgOrigenListener,
                            null,
                            dlgOrigenCancelListener,
                            false,
                            null,
                            false, null
                            );
                    break;
                case TITULO_MOTIVO:

                    String[] charSequence = new String[mLstMotivos.size()];
                    int i = 0;
                    for (MotivoBean motivo: mLstMotivos) {
                        charSequence[i] = motivo.getDescripcion();
                        i++;
                    }

                    mostrarInputDialog("Motivo incidencia", true,
                            charSequence,
                            spnMotivoIncidenciaListener,
                            -1,
                            null, dlgMotivoListener,
                            null, dlgMotivoCancelListener,
                            false, null,
                            false, null
                            );

                    break;
                case TITULO_COMENTARIOS:
                    mostrarInputDialog("Comentarios", false,
                            null,null,-1,
                            null,
                            dlgComentariosListener,
                            null,
                            dlgComentariosCancelListener,
                            true,
                            mEdtComentarios,
                            false, null);
                    break;
                case TITULO_FECHA_PAGO:
                    int year = Integer.parseInt(obtenerFechaHora("yyyy"));
                    int month = Integer.parseInt(obtenerFechaHora("MM")) - 1;
                    int day = Integer.parseInt(obtenerFechaHora("dd"));
                    if(mEdtFechaPago.getText() != null && !mEdtFechaPago.getText().toString().equals("")){
                        year = Integer.parseInt(mEdtFechaPago.getText().toString().substring(0, 4));
                        month = Integer.parseInt(mEdtFechaPago.getText().toString().substring(5, 7)) - 1;
                        day = Integer.parseInt(mEdtFechaPago.getText().toString().substring(8, 10));
                    }

                    DatePickerDialog datePickerDialog = new DatePickerDialog(IncidenciaActivity.this,
                            dateSetListenerPago,
                            year, month, day);
                    datePickerDialog.show();
                    break;
                case TITULO_SERIE_FACTURA:
                case TITULO_CORR_FACTURA:
                    if(mClienteSeleccionado != null || mOrigenIncidencia.equals(FACTURA)) {
                        Intent intent = new Intent(IncidenciaActivity.this, BuscarFacturaActivity.class);
                        if(mClienteSeleccionado != null)
                            intent.putExtra(BuscarFacturaActivity.KEY_PARAM_CLIENTE, mClienteSeleccionado.getCodigo());
                        startActivityForResult(intent, BuscarFacturaActivity.REQUEST_CODE_BUSCAR_FACTURA);
                    }else{
                        showMessage("Primero, debe seleccionar a un cliente.");
                    }
                    break;
                case TITULO_CODIGO_CLIENTE:
                    startActivityForResult(new Intent(IncidenciaActivity.this, ClienteBuscarActivity.class),
                           ClienteBuscarActivity.REQUEST_CODE_BUSCAR_CLIENTE);
                    break;
                case TITULO_COD_DIRECCION:
                    if(mClienteSeleccionado != null && mClienteSeleccionado.getDirecciones() != null){
                        Spinner spinner = new Spinner(IncidenciaActivity.this);
                        SPAdapterDireccion spFirstAdapter = new SPAdapterDireccion(IncidenciaActivity.this);
                        spFirstAdapter.addAll(mClienteSeleccionado.getDirecciones());
                        spinner.setAdapter(spFirstAdapter);
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setOnItemSelectedListener(spDireccionOnItemSelectedListener);

                        mostrarInputDialog("Codigo direccion", false,
                                null, null, -1,
                                null, dlgDireccionListener,
                                null, dlgDireccionCancelListener,
                                false, null,
                                true, spinner);
                    }
                    break;
                case TITULO_CODIGO_CONTACTO:
                    if(mClienteSeleccionado != null){
                        Spinner spinner = new Spinner(IncidenciaActivity.this);
                        SPAdapterContacto spFirstAdapter = new SPAdapterContacto(IncidenciaActivity.this);
                        spFirstAdapter.addAll(mClienteSeleccionado.getContactos());
                        spinner.setAdapter(spFirstAdapter);
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setOnItemSelectedListener(spContactoOnItemSelectedListener);

                        mostrarInputDialog("Codigo contacto", false,
                                null, null, -1,
                                null, dlgContactoListener,
                                null, dlgContactoCancelListener,
                                false, null,
                                true, spinner);
                    }
                    break;
                case TITULO_TIPO_INCIDENCIA:

                    String tipoIncidencia = mEdtTipoIncidencia.getText() != null ?
                            mEdtTipoIncidencia.getText().toString() : "";
                    int selectedIndex = 0;
                    if(!tipoIncidencia.equals("") && tipoIncidencia.equals(TOTALMENTE_RECHAZADO))
                        selectedIndex = 1;

                    mostrarInputDialog("Tipo de Incidencia", true,
                            new String[]{PARCIALMENTE_RECHAZADO,TOTALMENTE_RECHAZADO},
                            spnTipoIncidenciaListener,
                            selectedIndex,
                            null,
                            dlgTipoListener,
                            null,
                            dlgTipoCancelListener,
                            false,
                            null,
                            false, null
                    );
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            Toast.makeText(IncidenciaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //region LISTENER_ORIGEN_INCIDENCIA
    DialogInterface.OnClickListener spnOrigenIncidenciaListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:
                    mOrigenIncidencia = ORDEN;
                    break;
                case 1:
                    mOrigenIncidencia = ENTREGA;
                    break;
                case 2:
                    mOrigenIncidencia = FACTURA;
                    break;
                default:
                    mOrigenIncidencia = ORDEN;
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dlgOrigenListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            inicializarForm();
            mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_ORIGEN, obtenerOrigenIncidencia(), true));
        }
    };

    DialogInterface.OnClickListener dlgOrigenCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mOrigenIncidencia = mRVAdapterAddIncidencia.findItem(TITULO_ORIGEN).getContenido();
        }
    };

    //endregion

    //region LISTENER_MOTIVO_INCIDENCIA
    DialogInterface.OnClickListener spnMotivoIncidenciaListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtMotivo.setText(mLstMotivos.get(which).getDescripcion());
            mMotivoSeleccionado = mLstMotivos.get(which);
        }
    };

    DialogInterface.OnClickListener dlgMotivoListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            inicializarForm();
            mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_MOTIVO, mEdtMotivo.getText().toString(), true));
        }
    };

    DialogInterface.OnClickListener dlgMotivoCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtMotivo.setText(mRVAdapterAddIncidencia.findItem(TITULO_MOTIVO).getContenido());
            for (MotivoBean motivo: mLstMotivos) {
                if(motivo.getDescripcion().equals(mEdtMotivo.getText().toString()))
                {
                    mMotivoSeleccionado = motivo;
                    break;
                }
            }
        }
    };

    //endregion

    //region LISTENER_COMENTARIOS
    DialogInterface.OnClickListener dlgComentariosListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String comments = mEdtComentarios.getText() != null ? mEdtComentarios.getText().toString() : "";
            mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_COMENTARIOS, comments, true));
            mostrarTeclado(mEdtComentarios, false);
        }
    };

    DialogInterface.OnClickListener dlgComentariosCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtComentarios.setText(mRVAdapterAddIncidencia.findItem(TITULO_COMENTARIOS).getContenido());
            mostrarTeclado(mEdtComentarios, false);
        }
    };

    //endregion

    //region LISTENER_FECHA_PAGO
    DatePickerDialog.OnDateSetListener dateSetListenerPago = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            try{
                String fechaPago = String.valueOf(year) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", dayOfMonth);
                mEdtFechaPago.setText(fechaPago);
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_FECHA_PAGO, fechaPago, true));
            }catch(Exception e){
                showMessage("dateSetListenerPago() > " + e.getMessage());
            }
        }
    };
    //endregion

    //region LISTENER_DIRECCION
    AdapterView.OnItemSelectedListener spDireccionOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mDireccionSeleccionada = (DireccionBuscarBean) parent.getSelectedItem();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    DialogInterface.OnClickListener dlgDireccionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(mDireccionSeleccionada != null) {
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_COD_DIRECCION, mDireccionSeleccionada.getCodigo(), true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_DET_DIRECCION, mDireccionSeleccionada.getCalle(), true));
            }
        }
    };

    DialogInterface.OnClickListener dlgDireccionCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                String codDireccion = mRVAdapterAddIncidencia.findItem(TITULO_COD_DIRECCION).getContenido();
                String calle = mRVAdapterAddIncidencia.findItem(TITULO_DET_DIRECCION).getContenido();

                if(!codDireccion.equals("") && !calle.equals("")){
                    mDireccionSeleccionada = new DireccionBuscarBean();
                    mDireccionSeleccionada.setCodigo(codDireccion);
                    mDireccionSeleccionada.setCalle(calle);
                }else{
                    mDireccionSeleccionada = null;
                }
            }catch (Exception e){
                mDireccionSeleccionada = null;
                Toast.makeText(IncidenciaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    //endregion

    //region LISTENER_CONTACTO
    AdapterView.OnItemSelectedListener spContactoOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mContactoSeleccionado = (ContactoBuscarBean) parent.getSelectedItem();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    DialogInterface.OnClickListener dlgContactoListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(mContactoSeleccionado != null) {
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_CODIGO_CONTACTO, String.valueOf(mContactoSeleccionado.getCodigo()), true));
                mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_PERSONA_CONTACTO, mContactoSeleccionado.getNombre(), true));
            }
        }
    };

    DialogInterface.OnClickListener dlgContactoCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                String nombre = mRVAdapterAddIncidencia.findItem(TITULO_PERSONA_CONTACTO).getContenido();
                String codigo = mRVAdapterAddIncidencia.findItem(TITULO_CODIGO_CONTACTO).getContenido();

                if(!nombre.equals("")){
                    mContactoSeleccionado = new ContactoBuscarBean();
                    mContactoSeleccionado.setCodigo(Integer.parseInt(codigo));
                    mContactoSeleccionado.setNombre(nombre);
                }else{
                    mContactoSeleccionado = null;
                }
            }catch (Exception e){
                mContactoSeleccionado = null;
                Toast.makeText(IncidenciaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    //endregion

    //region LISTENER_TIPO_INCIDENCIA
    DialogInterface.OnClickListener spnTipoIncidenciaListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:
                    mEdtTipoIncidencia.setText(PARCIALMENTE_RECHAZADO);
                    break;
                case 1:
                    mEdtTipoIncidencia.setText(TOTALMENTE_RECHAZADO);
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dlgTipoListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_TIPO_INCIDENCIA, mEdtTipoIncidencia.getText().toString(), true));
        }
    };

    DialogInterface.OnClickListener dlgTipoCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtTipoIncidencia.setText(mRVAdapterAddIncidencia.findItem(TITULO_TIPO_INCIDENCIA).getContenido());
        }
    };

    //endregion

    //region FUNCIONES
    private String obtenerOrigenIncidencia(){
        if(mOrigenIncidencia == null || mOrigenIncidencia.equals(""))
            return  ORDEN;
        else
            return mOrigenIncidencia;
    }

    private String obtenerFechaHora(String pattern){
        String fecha = new SimpleDateFormat(pattern).format(new Date());

        if(pattern.equals(PATTERN_FECHA))
            mEdtFechaCreacion.setText(fecha);
        else if(pattern.equals(PATTERN_HORA))
            mEdtHoraCreacion.setText(fecha);

        return fecha;
    }

    private String comprobarConexion(){
        String respuesta;
        boolean wifi = Connectivity.isConnectedWifi(IncidenciaActivity.this);
        boolean movil = Connectivity.isConnectedMobile(IncidenciaActivity.this);

        if(wifi || movil)
            respuesta = ESTADO_CONECTADO;
        else
            respuesta = ESTADO_DESCONECTADO;

        mEdtEstadoConexion.setText(respuesta);
        return  respuesta;
    }

    private void mostrarInputDialog(String titulo, boolean singleChoice,
                                    CharSequence[] singleChoiceItems,
                                    DialogInterface.OnClickListener singleChoiceLitener,
                                    int singleChoiceItemSelected,
                                    String positiveButtonText,
                                    DialogInterface.OnClickListener positiveButtonListener,
                                    String negativeButtonText,
                                    DialogInterface.OnClickListener negativeButtonListener,
                                    boolean inputEditText,
                                    EditText editText,
                                    boolean spinnerInput,
                                    Spinner spinner
                                    ){
        try {

            AlertDialog.Builder alert = new AlertDialog.Builder(IncidenciaActivity.this);
            alert.setTitle(titulo);
            alert.setCancelable(false);
            alert.setPositiveButton(positiveButtonText != null? positiveButtonText : "Ok", positiveButtonListener);
            alert.setNegativeButton(negativeButtonText != null? negativeButtonText : "Cancelar", negativeButtonListener);

            if(singleChoice)
                alert.setSingleChoiceItems(singleChoiceItems, singleChoiceItemSelected,singleChoiceLitener);

            if(inputEditText){

                if(editText.getParent() != null)
                    ((ViewGroup) editText.getParent()).removeView(editText);

                alert.setView(editText);
                editText.requestFocus();
                mostrarTeclado(null, true);
            }

            if(spinnerInput){
                alert.setView(spinner);
            }

            alert.show();
        }catch(Exception e){
            Toast.makeText(IncidenciaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarTeclado(EditText editText, boolean show){
        if(show){
            InputMethodManager imm = (InputMethodManager) ((Context) IncidenciaActivity.this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }else{
            InputMethodManager imm = (InputMethodManager) ((Context) IncidenciaActivity.this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    //endregion

    //region REGISTRO_INCIDENCIA
    private boolean validarDatos(){
        boolean result = true;

        switch (mOrigenIncidencia){
            case ORDEN:
                if(validarDatosBasicos()){
                    if(!mEdtMotivo.getText().toString().toUpperCase().contains("FALTA DE TIEMPO") &&
                        mBitMapImage == null){
                            showMessage("Debe registrar una foto.");
                            result = false;
                        }
                }else
                    result = false;
                break;
            case ENTREGA:
                if(validarDatosBasicos()){
                    if(mFacturaSeleccionada == null && mReferencia == null){
                        showMessage("Debe seleccionar una factura.");
                        result = false;
                    }else if(mEdtTipoIncidencia.getText() == null || mEdtTipoIncidencia.getText().toString().equals("")){
                        showMessage("Seleccione el tipo de incidencia.");
                        result = false;
                    }else if(!mEdtMotivo.getText().toString().toUpperCase().contains("FALTA DE TIEMPO") &&
                            mBitMapImage == null){
                        showMessage("Debe registrar una foto.");
                        result = false;
                    }
                }else
                    result = false;
                break;
            case FACTURA:
                if(mFacturaSeleccionada == null){
                    showMessage("Debe seleccionar una factura.");
                    result = false;
                }else if(mEdtComentarios.getText() == null || mEdtComentarios.getText().toString().equals("")){
                    showMessage("Debe ingresar un comentario.");
                    result = false;
                }else if(mEdtFechaPago.getText() == null || mEdtFechaPago.getText().toString().equals("")){
                    showMessage("Seleccione una fecha de compromiso de pago.");
                    result = false;
                }else if(mCodigoEmpleado == null ||
                        mCodigoEmpleado.equals("")){
                    showMessage("No se ha obtenido informacion del usuario logueado.");
                    result = false;
                }
                break;
        }

        return result;
    }

    private boolean validarDatosBasicos(){
        boolean result = true;

        if(mClienteSeleccionado == null){
            showMessage("Debe seleccionar un cliente.");
            result = false;
        }else if(mDireccionSeleccionada == null){
            showMessage("Debe seleccionar la direccion del cliente.");
            result = false;
        }else if(mMotivoSeleccionado == null){
            showMessage("Debe seleccionar un motivo.");
            result = false;
        }else if(mCodigoEmpleado == null ||
                mCodigoEmpleado.equals("")){
            showMessage("No se ha obtenido informacion del usuario logueado.");
            result = false;
        }

        return  result;
    }

    private void registrarIncidencia(){
        try {
            IncidenciaBean incidencia = new IncidenciaBean();
            incidencia.setClaveMovil(mClaveMovil);
            incidencia.setOrigen(mOrigenIncidencia);

            if(mClienteSeleccionado != null){
                incidencia.setCodigoCliente(mClienteSeleccionado.getCodigo());
                incidencia.setNombreCliente(mClienteSeleccionado.getNombre());
            }

            if(mContactoSeleccionado != null){
                incidencia.setCodigoContacto(mContactoSeleccionado.getCodigo());
                incidencia.setNombreContacto(mContactoSeleccionado.getNombre());
            }

            if(mDireccionSeleccionada != null) {
                incidencia.setCodigoDireccion(mDireccionSeleccionada.getCodigo());
                incidencia.setDetalleDireccion(mDireccionSeleccionada.getCalle());
            }

            if(mMotivoSeleccionado != null){
                incidencia.setMotivo(String.valueOf(mMotivoSeleccionado.getId()));
                incidencia.setDescripcionMotivo(String.valueOf(mMotivoSeleccionado.getDescripcion()));
            }

            incidencia.setCodigoVendedor(Integer.parseInt(mCodigoEmpleado));
            incidencia.setComentarios(mRVAdapterAddIncidencia.findItem(TITULO_COMENTARIOS).getContenido());
            incidencia.setCodigoVendedor(Integer.parseInt(mCodigoEmpleado));
            incidencia.setLatitud(mRVAdapterAddIncidencia.findItem(TITULO_LATITUD).getContenido());
            incidencia.setLongitud(mRVAdapterAddIncidencia.findItem(TITULO_LONGITUD).getContenido());
            incidencia.setFechaCreacion(mRVAdapterAddIncidencia.findItem(TITULO_FECHA_CREACION).getContenido());
            incidencia.setHoraCreacion(mRVAdapterAddIncidencia.findItem(TITULO_HORA_CREACION).getContenido());
            incidencia.setModoOffline(mRVAdapterAddIncidencia.findItem(TITULO_ESTADO_CONEXION).getContenido().equals(ESTADO_CONECTADO)?
                    "N": "Y");

            if(mClaveEntrega == null || mClaveEntrega.equals(""))
                incidencia.setClaveFactura(mFacturaSeleccionada != null ? mFacturaSeleccionada.getClave() : null);
            else
                incidencia.setClaveFactura(mClaveEntrega);

            incidencia.setSerieFactura(mRVAdapterAddIncidencia.findItem(TITULO_SERIE_FACTURA).getContenido());
            incidencia.setCorrelativoFactura(mRVAdapterAddIncidencia.findItem(TITULO_CORR_FACTURA).getContenido().equals("") ?
                    null : Integer.parseInt(mRVAdapterAddIncidencia.findItem(TITULO_CORR_FACTURA).getContenido()));
            incidencia.setTipoIncidencia(mRVAdapterAddIncidencia.findItem(TITULO_TIPO_INCIDENCIA).getContenido().equals("") ?
                    null : (mRVAdapterAddIncidencia.findItem(TITULO_TIPO_INCIDENCIA).getContenido().equals(PARCIALMENTE_RECHAZADO) ?
            CODIGO_PARCIALMENTE_RECHAZADO: CODIGO_TOTALMENTE_RECHAZADO));
            incidencia.setFechaCompromisoPago(mRVAdapterAddIncidencia.findItem(TITULO_FECHA_PAGO).getContenido());
            incidencia.setFoto(mBitMapImage);
            incidencia.setSincronizado("N");

            mCurrentLocation = getCurrentLocation();
            if(mCurrentLocation != null){
                incidencia.setRango(rangoDireccion(mCurrentLocation));
                incidencia.setLatitud(String.valueOf(mCurrentLocation.getLatitude()));
                incidencia.setLongitud(String.valueOf(mCurrentLocation.getLongitude()));
            }

            if(new IncidenciaDAO().insertIncidencia(incidencia)){
                CorrelativoDAO.updateCorrelativo("INC");
                showMessage("Se registro la incidencia con exito.");

                try{
                    showMessage("Enviando al servidor...");
                    enviarIncidenciaAServidor(incidencia);
                }catch(Exception e){
                    showMessage("Server exception " + e.getMessage());
                }finally {
                    finish();
                }

            }else
                showMessage("Ocurrio un error intentando registrar el documento.");
        }catch (Exception e){
            showMessage("Ocurrio un error intentando registrar la incidencia: " + e.getMessage());
        }
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(IncidenciaActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    //endregion

    private void enviarIncidenciaAServidor(final IncidenciaBean incidencia){

        if (existsNetworkConnection()) {

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            JSONObject jsonObject = IncidenciaBean.transformIncidenciaToJSON(incidencia, sociedad);

            String jsonString = jsonObject.toString();

            if(jsonObject != null){
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

    private String rangoDireccion(Location currentLocation){
        String rangoResult = Constantes.RANGO_NO_DISPONIBLE;

        try{

            if(mDireccionSeleccionada != null){
                if(mDireccionSeleccionada.getLatitud() != null && mDireccionSeleccionada.getLongitud() != null &&
                        !TextUtils.isEmpty(mDireccionSeleccionada.getLatitud()) &&
                        !TextUtils.isEmpty(mDireccionSeleccionada.getLongitud())){

                    Location locationTo = new Location(GPS_PROVIDER);
                    locationTo.setLatitude(Double.parseDouble(mDireccionSeleccionada.getLatitud()));
                    locationTo.setLongitude(Double.parseDouble(mDireccionSeleccionada.getLongitud()));

                    float distance = currentLocation.distanceTo(locationTo);

                    if(distance > Constantes.DEFAULT_RANGE)
                        rangoResult = Constantes.FUERA_DE_RANGO;
                    else
                        rangoResult = Constantes.DENTRO_DE_RANGO;
                }
            }

        }catch(Exception e){
            showMessage("rangoDireccion() > " + e.getMessage());
            return rangoResult;
        }

        return rangoResult;
    }

    private void autoSeleccionarDireccionMasCercana(){

        try{
            mCurrentLocation = getCurrentLocation();

            float bestDistance = -1;

            if(mClienteSeleccionado.getDirecciones() != null && mCurrentLocation != null){
                for (DireccionBuscarBean direccion: mClienteSeleccionado.getDirecciones()) {
                    if(direccion.getTipo().equals(Constantes.TIPO_DIRECCION_ENTREGA) &&
                            direccion.getLatitud() != null && direccion.getLongitud() != null &&
                            !TextUtils.isEmpty(direccion.getLatitud()) && !TextUtils.isEmpty(direccion.getLongitud())){

                        Location locationTo = new Location(GPS_PROVIDER);
                        locationTo.setLatitude(Double.parseDouble(direccion.getLatitud()));
                        locationTo.setLongitude(Double.parseDouble(direccion.getLongitud()));

                        float distance = mCurrentLocation.distanceTo(locationTo);
                        if(bestDistance == -1) {
                            bestDistance = distance;
                            mDireccionSeleccionada = direccion;
                        }else{
                            if(distance < bestDistance){
                                bestDistance = distance;
                                mDireccionSeleccionada = direccion;
                            }
                        }
                    }
                }

                if(mDireccionSeleccionada != null){
                    mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_COD_DIRECCION, mDireccionSeleccionada.getCodigo(), true));
                    mRVAdapterAddIncidencia.updateItem(new ItemAddIncidencia(TITULO_DET_DIRECCION, mDireccionSeleccionada.getCalle(), true));
                    showMessage("Autoseleccion de direccion mas cercana...");
                }
            }
        }catch (Exception e){
            showMessage("autoSeleccionarDireccionMasCercana > " + e.getMessage());
        }
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

}
