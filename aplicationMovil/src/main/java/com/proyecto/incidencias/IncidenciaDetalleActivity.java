package com.proyecto.incidencias;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.IncidenciaBean;
import com.proyecto.cobranza.DetalleFacturaMain;
import com.proyecto.sociosnegocio.DetalleSocioNegocioMain;
import com.proyecto.utils.Constantes;
import com.proyecto.ventas.DetalleVentaMain;

public class IncidenciaDetalleActivity extends AppCompatActivity {

    public final static String KEY_PARM_INCIDENCIA = "incidencia";
    private IncidenciaBean mIncidencia;
    private TextView mtvCodigoCliente, mtvNombreCliente, mtvDireccion, mtvContacto,
            mtvOrigen, mtvMotivo, mtvComentario, mtvFechaRegistro, mtvFueraLinea,
            mtvNumeroFactura, mtvTipoIncidencia, mtvFechaCompromisoPago, mtvRango, mtvCoordenadas;
    private ImageView mImgFoto;
    private CardView cdvDatosFactura, cdvDatosFoto;
    private FloatingActionButton fbCliente, fbFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDetalleIncidencia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalle de incidencia");

        mtvCodigoCliente = (TextView) findViewById(R.id.tvDetalleIncCodigoCliente);
        mtvNombreCliente = (TextView) findViewById(R.id.tvDetalleIncNombreCliente);
        mtvDireccion = (TextView) findViewById(R.id.tvDetalleIncDireccion);
        mtvContacto = (TextView) findViewById(R.id.tvDetalleIncContacto);
        mtvOrigen = (TextView) findViewById(R.id.tvDetalleIncOrigen);
        mtvMotivo = (TextView) findViewById(R.id.tvDetalleIncMotivo);
        mtvComentario = (TextView) findViewById(R.id.tvDetalleIncComentario);
        mtvFechaRegistro = (TextView) findViewById(R.id.tvDetalleIncFechaRegistro);
        mtvFueraLinea = (TextView) findViewById(R.id.tvDetalleIncEstConexion);
        mtvNumeroFactura = (TextView) findViewById(R.id.tvDetalleIncNumeroFact);
        mtvTipoIncidencia = (TextView) findViewById(R.id.tvDetalleIncTipoIncidencia);
        mtvRango = (TextView) findViewById(R.id.tvDetalleIncRango);
        mtvCoordenadas = (TextView) findViewById(R.id.tvDetalleIncCoordenadas);

        mtvFechaCompromisoPago = (TextView) findViewById(R.id.tvDetalleIncFechaPago);
        mImgFoto = (ImageView) findViewById(R.id.ivDetalleIncFoto);
        cdvDatosFactura = (CardView) findViewById(R.id.cvDatosFactura);
        cdvDatosFoto = (CardView) findViewById(R.id.cvDatosFoto);

        fbCliente = (FloatingActionButton) findViewById(R.id.fabDetalleIncidenciaGoCliente);
        fbCliente.setOnClickListener(fabGoClienteOnClickListener);

        fbFactura = (FloatingActionButton) findViewById(R.id.fabDetalleIncidenciaGoFactura);
        fbFactura.setOnClickListener(fabGoFacturaOnClickListener);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARM_INCIDENCIA)){
                mIncidencia = getIntent().getExtras().getParcelable(KEY_PARM_INCIDENCIA);
            }
        }else
            showMessage("No se ha recibido el parámetro para obtener el detalle de la incidencia.");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mIncidencia != null){

            if(mIncidencia.getOrigen().equals(IncidenciaActivity.ORDEN))
                cdvDatosFactura.setVisibility(View.GONE);
            else
                cdvDatosFactura.setVisibility(View.VISIBLE);

            if(mIncidencia.getOrigen().equals(IncidenciaActivity.FACTURA))
                cdvDatosFoto.setVisibility(View.GONE);
            else
                cdvDatosFoto.setVisibility(View.VISIBLE);

            mtvCodigoCliente.setText(getStringValid(mIncidencia.getCodigoCliente()));
            mtvNombreCliente.setText(getStringValid(mIncidencia.getNombreCliente()));
            mtvDireccion.setText(getStringValid(mIncidencia.getDetalleDireccion()));
            mtvContacto.setText(getStringValid(mIncidencia.getNombreContacto()));
            mtvOrigen.setText(getStringValid(mIncidencia.getOrigen()));
            mtvMotivo.setText(getStringValid(mIncidencia.getMotivo()));
            mtvComentario.setText(getStringValid(mIncidencia.getComentarios()));
            mtvFechaRegistro.setText(mIncidencia.getFechaCreacion());
            mtvFueraLinea.setText(mIncidencia.getModoOffline().equals("Y")? "NO" : "SI");

            if(mIncidencia.getCorrelativoFactura() != null &&
                    mIncidencia.getCorrelativoFactura() > 0)
                mtvNumeroFactura.setText(mIncidencia.getSerieFactura() + " - " + mIncidencia.getCorrelativoFactura());
            else
                mtvNumeroFactura.setText("-");

            if(mIncidencia.getTipoIncidencia() != null &&
                    !mIncidencia.getTipoIncidencia().equals(""))
                mtvTipoIncidencia.setText(mIncidencia.getTipoIncidencia().equals(IncidenciaActivity.CODIGO_PARCIALMENTE_RECHAZADO) ?
                   IncidenciaActivity.PARCIALMENTE_RECHAZADO.toUpperCase() : IncidenciaActivity.TOTALMENTE_RECHAZADO.toUpperCase());
            else
                mtvTipoIncidencia.setText("-");

            mtvFechaCompromisoPago.setText(getStringValid(mIncidencia.getFechaCompromisoPago()));
            mtvRango.setText(Constantes.obtenerDescripcionRango(mIncidencia.getRango()));
            mtvCoordenadas.setText(mIncidencia.getLatitud() != null ? (mIncidencia.getLatitud() + " , " + mIncidencia.getLongitud()) : "");
            mImgFoto.setImageBitmap(mIncidencia.getFoto());
        }
    }


    private View.OnClickListener fabGoClienteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mIncidencia != null) {
                Intent myIntent = new Intent(v.getContext(),
                        DetalleSocioNegocioMain.class);
                myIntent.putExtra("id", mIncidencia.getCodigoCliente());
                startActivity(myIntent);
            }
        }
    };

    private View.OnClickListener fabGoFacturaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mIncidencia != null) {
                Intent myIntent = new Intent(v.getContext(),
                        DetalleFacturaMain.class);
                myIntent.putExtra("id", mIncidencia.getClaveFactura());
                startActivity(myIntent);
            }
        }
    };

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(IncidenciaDetalleActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private String getStringValid(String string){
        if(string != null && !string.equals("")){
            return string.toUpperCase();
        }else
            return "-";
    }
}
