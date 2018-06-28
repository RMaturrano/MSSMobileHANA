package com.proyecto.visitas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.StringDateCast;

public class DetalleVisitaActivity extends AppCompatActivity {

    public static String KEY_PARAM_DIRECCION = "detDireccion";
    private TextView edtCodCliente, edtNomCliente, edtCodDireccion, edtDetDirecciom, edtCalle,
                    edtCoordenadas, edtFrecuencia, edtFechaInicio, edtNumUltimaCompra, edtFecUltimaCompra,
                    edtMonUltimaCompra, edtNombreContacto, edtTelefonoContacto;
    private CheckBox cbxLunes, cbxMartes, cbxMiercoles, cbxJueves, cbxViernes, cbxSabado, cbxDomingo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_visita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlbDetalleVisita);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detalle dirección");

        edtCodCliente = (TextView) findViewById(R.id.tvDetVisitaCodCliente);
        edtNomCliente = (TextView) findViewById(R.id.tvDetVisitaNomCliente);
        edtCodDireccion = (TextView) findViewById(R.id.tvDetVisitaCodDir);
        edtDetDirecciom = (TextView) findViewById(R.id.tvDetVisitaDetDireccion);
        edtCalle = (TextView) findViewById(R.id.tvDetVisitaCalle);
        edtCoordenadas = (TextView) findViewById(R.id.tvDetVisitaCoordenadas);
        edtFrecuencia = (TextView) findViewById(R.id.tvDetVisitaFrecuencia);
        edtFechaInicio = (TextView) findViewById(R.id.tvDetVisitaFecInicio);
        edtNumUltimaCompra = (TextView) findViewById(R.id.tvDetVisitaNumUltimaCompra);
        edtFecUltimaCompra = (TextView) findViewById(R.id.tvDetVisitaFecUltimaCompra);
        edtMonUltimaCompra = (TextView) findViewById(R.id.tvDetVisitaMonUltimaCompra);
        edtNombreContacto = (TextView) findViewById(R.id.tvDetVisitaNombreContacto);
        edtTelefonoContacto = (TextView) findViewById(R.id.tvDetVisitaTelefonoContacto);

        cbxLunes = (CheckBox) findViewById(R.id.cbxVisitaLun);
        cbxMartes = (CheckBox) findViewById(R.id.cbxVisitaMar);
        cbxMiercoles = (CheckBox) findViewById(R.id.cbxVisitaMie);
        cbxJueves = (CheckBox) findViewById(R.id.cbxVisitaJue);
        cbxViernes = (CheckBox) findViewById(R.id.cbxVisitaVie);
        cbxSabado = (CheckBox) findViewById(R.id.cbxVisitaSab);
        cbxDomingo = (CheckBox) findViewById(R.id.cbxVisitaDom);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent() != null && getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(KEY_PARAM_DIRECCION)){
                DireccionBuscarBean direccion = getIntent().getParcelableExtra(KEY_PARAM_DIRECCION);

                edtCodCliente.setText(direccion.getCodigoCliente());
                edtNomCliente.setText(direccion.getNombreCliente());
                edtCodDireccion.setText(direccion.getCodigo());
                edtDetDirecciom.setText(direccion.getDepartamentoNombre() + " / " +
                        direccion.getProvinciaNombre() + " / " + direccion.getDistritoNombre());
                edtCalle.setText(direccion.getCalle());
                edtCoordenadas.setText(direccion.getLatitud() + " ; " + direccion.getLongitud());
                edtFrecuencia.setText(Constantes.obtenerDescripcionFrecuencia(direccion.getFrecuenciaVisita()));
                edtFechaInicio.setText(StringDateCast.castStringtoDate(direccion.getFechaInicio()));
                edtNumUltimaCompra.setText(direccion.getNumUltimaCompra());
                edtFecUltimaCompra.setText(direccion.getFecUltimaCompra());
                edtMonUltimaCompra.setText(direccion.getMonUltimaCompra());
                edtNombreContacto.setText(direccion.getPersonaContacto());
                edtTelefonoContacto.setText(direccion.getTelefonoContacto());

                cbxLunes.setChecked(direccion.getVisitaLunes().equals("Y") ? true: false);
                cbxMartes.setChecked(direccion.getVisitaMartes().equals("Y") ? true: false);
                cbxMiercoles.setChecked(direccion.getVisitaMiercoles().equals("Y") ? true: false);
                cbxJueves.setChecked(direccion.getVisitaJueves().equals("Y") ? true: false);
                cbxViernes.setChecked(direccion.getVisitaViernes().equals("Y") ? true: false);
                cbxSabado.setChecked(direccion.getVisitaSabado().equals("Y") ? true: false);
                cbxDomingo.setChecked(direccion.getVisitaDomingo().equals("Y") ? true: false);
            }
        }
    }
}
