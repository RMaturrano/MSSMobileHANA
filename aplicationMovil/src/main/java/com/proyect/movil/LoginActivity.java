package com.proyect.movil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.proyecto.bean.EmpleadoBean;
import com.proyecto.bean.EmpresaBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.movil.MainActivityDrawer;
import com.proyecto.preferences.SettingsMain;
import com.proyecto.servicios.Constants;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;
import com.proyecto.utils.Variables;
import com.proyecto.ws.HanaWS;
import com.proyecto.ws.InvocaWS;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */

public class LoginActivity extends AppCompatActivity {

    private Context contexto;

    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mUser;
    private String mPassword;
    private EmpleadoBean bean;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private String idDispositivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contexto = this;
        idDispositivo = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);

        Log.i("ID DISPOSITIVO LOGIN", idDispositivo);

        // TOOLBAR
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner �cono del drawer toggle
            //ab.setHomeAsUpIndicator(R.drawable.logo_seidor_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the login form.
        mUser = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mUser);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                });
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // super.onCreateOptionsMenu(menu);
    // getMenuInflater().inflate(R.menu.login, menu);
    // return true;
    // }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configuration_section:
                Intent conn = new Intent(contexto,
                        SettingsMain.class);
                startActivity(conn);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mUser = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(mUser)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
            boolean wifi = Connectivity.isConnectedWifi(contexto);
            boolean movil = Connectivity.isConnectedMobile(contexto);
            boolean isConnectionFast = Connectivity
                    .isConnectedFast(contexto);

            if (wifi || movil) {

                if (isConnectionFast) {

                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.

                    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                    showProgress(true);

                    //mAuthTask = new UserLoginTask();
                    //mAuthTask.execute((Void) null);

                    iniciarLogin();

                } else {

                }
            } else {

                Toast.makeText(contexto, "No está conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

            }

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void iniciarLogin() {

        final SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(contexto);

        final String ip = pref.getString("ipServidor", "200.10.84.66");
        final String port = pref.getString("puertoServidor", "80");
        final String sociedad = pref.getString("sociedades", "-1");
        final String ws_ruta = "http://" + ip + ":" + port +
                "/MSS_MOBILE/service/login/auth.xsjs";
        String usuario = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("empresaId", Integer.parseInt(sociedad));
            jsonObject.put("usuario", usuario);
            jsonObject.put("password", Integer.parseInt(password));
            jsonObject.put("idUnico", idDispositivo);
        } catch (Exception e) {
            showToast("Excepción no controlada, JSONObject to send, " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ws_ruta, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showProgress(false);
                        //int a = 0;
                        //int b = 2/a;
                        try {
                            if (response.getString("ResponseStatus").equals("Error")) {
                                String messageError = response.getJSONObject("Response")
                                        .getJSONObject("message")
                                        .getString("value");

                                if (response.getJSONObject("Response").getInt("code") == -102 ||
                                        response.getJSONObject("Response").getInt("code") == -103) {
                                    mEmailView.setError(messageError);
                                    mEmailView.requestFocus();
                                } else if (response.getJSONObject("Response").getInt("code") == -104) {
                                    mPasswordView.setError(messageError);
                                    mPasswordView.requestFocus();
                                } else {
                                    showToast(messageError);
                                }

                            } else {
                                showToast("Datos correctos. Bienvenido...");

                                JSONObject objUser = response.getJSONObject("Response");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Variables.CODIGO_EMPLEADO, objUser.getString("Codigo"));
                                editor.putString(Variables.NOMBRE_EMPLEADO, objUser.getString("Nombre"));
                                editor.putString(Variables.USUARIO_EMPLEADO, objUser.getString("Usuario"));
                                editor.putString(Variables.PASSWORD_EMPLEADO, String.valueOf(objUser.getInt("Clave")));
                                editor.putString(Variables.PERFIL, objUser.getString("Perfil"));
                                editor.putString(Variables.SUPERVISOR, objUser.getString("Supervisor"));
                                editor.putString(Variables.COBRADOR, objUser.getString("Cobrador"));
                                editor.putInt(Variables.MAXIMO_LINEAS, objUser.getInt("MaxLineas"));
                                editor.putString(Variables.DESCRIPCION_COMPANIA, !objUser.isNull("NombreCompania") ? objUser.getString("NombreCompania") : "No disponible");

                                editor.commit();

                                Intent principal = new Intent(contexto, MainActivityDrawer.class);
                                startActivity(principal);

                                finish();
                            }
                        } catch (Exception e) {
                            showToast("Exception > onResponse() > " + e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        showToast("Ocurrio un error intentando conectar con el servidor " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        VolleySingleton.getInstance(contexto).addToRequestQueue(jsonObjectRequest);
    }

    private void showToast(String message) {
        if (message != null)
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("SimpleDateFormat")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            InvocaWS ws = new InvocaWS(contexto);

            bean = ws.ObtenerVendedor(mUser, mPassword,
                    idDispositivo);

            if (bean != null) {
                return true;
            } else
                return false;

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                // REGISTRAR LA SESI�N
                SharedPreferences pref = PreferenceManager
                        .getDefaultSharedPreferences(contexto);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Variables.NOMBRE_EMPLEADO, bean.getNombreVendedor());
                editor.putString(Variables.CODIGO_EMPLEADO, bean.getCodigoVendedor());
                editor.putString(Variables.USUARIO_EMPLEADO, bean.getCodigoUsuario());
                editor.putString(Variables.PASSWORD_EMPLEADO, bean.getClaveUsuario());
                editor.putString(Variables.MOVIL_EDITAR, bean.getMovilEditar());
                editor.putString(Variables.MOVIL_APROBAR, bean.getMovilAprobar());
                editor.putString(Variables.MOVIL_CREAR, bean.getMovilCrear());
                editor.putString(Variables.MOVIL_RECHAZAR, bean.getMovilRechazar());
                editor.putString(Variables.MOVIL_ESCOGER_PRECIO, bean.getMovilEscogerPrecio());
                editor.commit();


                Toast.makeText(contexto, "Bienvenido...", Toast.LENGTH_LONG).show();

                Intent principal = new Intent(contexto, MainActivityDrawer.class);
//				principal.putExtra("S", "T");
                startActivity(principal);


                // EJECUTAR EL SERVICIO QUE CORRER� EN SEGUNDO PLANO
//				Intent intent = new Intent(contexto, ServicioOvPr.class);
//				intent.setAction(Constants.ACTION_RUN_ISERVICE);
//				startService(intent);

                Intent servicio1 = new Intent(contexto, ServicioOvPr.class);
                servicio1.setAction(Constants.ACTION_RUN_ISERVICE);
                startService(servicio1);
//				
			/*	Intent servicio2 = new Intent(contexto, ServicioSocios.class);
				servicio2.setAction(Constants.ACTION_RUN_ISERVICE);
				startService(servicio2);
			*/

                finish();


            } else {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();

                mAuthTask = null;
                showProgress(false);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
