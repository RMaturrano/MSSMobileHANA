package com.proyecto.movil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.LoginActivity;
import com.proyect.movil.R;
import com.proyecto.database.Delete;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;
import com.proyecto.utils.Variables;

import java.util.Arrays;
import java.util.Random;

public class PinVerified extends AppCompatActivity {

    private String mPassword;
    private View mProgressLayout, mPinLayout;
    private TextView tvUsuario;
    private SharedPreferences pref;
    private ImageView img1, img2, img3, img4, img5, img6;
    private Button btnL1P1, btnL1P2, btnL1P3,
                   btnL2P1, btnL2P2, btnL2P3,
                   btnL3P1, btnL3P2, btnL3P3,
                   btnL4P1, btnCerrarSesion;
    private ImageButton btnBackSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verified);

        mProgressLayout  = (View) findViewById(R.id.progressLayout);
        mPinLayout = (View) findViewById(R.id.pinLayout);

        tvUsuario = (TextView) findViewById(R.id.tvUserLoged);

        img1 = (ImageView) findViewById(R.id.imgVrf1);
        img2 = (ImageView) findViewById(R.id.imgVrf2);
        img3 = (ImageView) findViewById(R.id.imgVrf3);
        img4 = (ImageView) findViewById(R.id.imgVrf4);
        img5 = (ImageView) findViewById(R.id.imgVrf5);
        img6 = (ImageView) findViewById(R.id.imgVrf6);

        btnL1P1 = (Button) findViewById(R.id.btnL1P1);
        btnL1P2 = (Button) findViewById(R.id.btnL1P2);
        btnL1P3 = (Button) findViewById(R.id.btnL1P3);

        btnL2P1 = (Button) findViewById(R.id.btnL2P1);
        btnL2P2 = (Button) findViewById(R.id.btnL2P2);
        btnL2P3 = (Button) findViewById(R.id.btnL2P3);

        btnL3P1 = (Button) findViewById(R.id.btnL3P1);
        btnL3P2 = (Button) findViewById(R.id.btnL3P2);
        btnL3P3 = (Button) findViewById(R.id.btnL3P3);

        btnL4P1 = (Button) findViewById(R.id.btnL4P1);
        btnBackSpace = (ImageButton) findViewById(R.id.btnL4Del);

        btnL1P1.setOnClickListener(onClickListenerButtonPin);
        btnL1P2.setOnClickListener(onClickListenerButtonPin);
        btnL1P3.setOnClickListener(onClickListenerButtonPin);
        btnL2P1.setOnClickListener(onClickListenerButtonPin);
        btnL2P2.setOnClickListener(onClickListenerButtonPin);
        btnL2P3.setOnClickListener(onClickListenerButtonPin);
        btnL3P1.setOnClickListener(onClickListenerButtonPin);
        btnL3P2.setOnClickListener(onClickListenerButtonPin);
        btnL3P3.setOnClickListener(onClickListenerButtonPin);
        btnL4P1.setOnClickListener(onClickListenerButtonPin);
        btnBackSpace.setOnClickListener(onClickListenerDel);
        btnCerrarSesion = (Button) findViewById(R.id.btnLogOff);
        btnCerrarSesion.setOnClickListener(closeSessionClickListener);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        String usuario = pref.getString(Variables.NOMBRE_EMPLEADO,"");
        tvUsuario.setText(usuario);

        setRandomNumeration();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRandomNumeration();
        mPassword = "";
    }

    private void setRandomNumeration(){

        try{
            int[] positions = new int[10];
            Random rdr = new Random();

            for (int i = 0; i <= 9; i++){
                int toPos = rdr.nextInt((10 - 1) + 1) + 1;
                boolean shouldContinue = contains(positions, toPos);

                while(shouldContinue){
                    toPos = rdr.nextInt((10 - 1) + 1) + 1;
                    shouldContinue = contains(positions, toPos);
                }

                switch (toPos){
                    case 1:
                        btnL1P1.setText(String.valueOf(i));
                        break;
                    case 2:
                        btnL1P2.setText(String.valueOf(i));
                        break;
                    case 3:
                        btnL1P3.setText(String.valueOf(i));
                        break;
                    case 4:
                        btnL2P1.setText(String.valueOf(i));
                        break;
                    case 5:
                        btnL2P2.setText(String.valueOf(i));
                        break;
                    case 6:
                        btnL2P3.setText(String.valueOf(i));
                        break;
                    case 7:
                        btnL3P1.setText(String.valueOf(i));
                        break;
                    case 8:
                        btnL3P2.setText(String.valueOf(i));
                        break;
                    case 9:
                        btnL3P3.setText(String.valueOf(i));
                        break;
                    case 10:
                        btnL4P1.setText(String.valueOf(i));
                        break;
                }

                positions[i] = toPos;
            }
        }catch (Exception e){
            showToast("setRandomNumeration() > " + e.getMessage());
        }
    }

    private void checkPassword(){

        showProgress(true);

        try{

            String password = pref.getString(Variables.PASSWORD_EMPLEADO, "");
            if(mPassword.equals(password)){
                showProgress(false);
                showToast("Clave ingresada correctamente...");
                Intent intent = new Intent(PinVerified.this, MainActivityDrawer.class);
                startActivity(intent);
                finish();
            }else{
                showProgress(false);
                showToast("La clave ingresada no es correcta.");
                img1.setImageResource(R.drawable.circle_white);
                img2.setImageResource(R.drawable.circle_white);
                img3.setImageResource(R.drawable.circle_white);
                img4.setImageResource(R.drawable.circle_white);
                img5.setImageResource(R.drawable.circle_white);
                img6.setImageResource(R.drawable.circle_white);
                mPassword = "";
            }

        }catch(Exception e){
            showProgress(false);
            showToast("checkPassword() > " + e.getMessage());
        }
    }

    private static boolean contains(final int[] array, final int v) {

        boolean result = false;

        for(int i : array){
            if(i == v){
                result = true;
                break;
            }
        }

        return result;
    }

    private void showToast(String message){
        if(message != null)
            Toast.makeText(PinVerified.this, message, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClickListenerButtonPin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try{
                if(mPassword.length() < 6){
                    Button button = (Button)v;
                    mPassword += button.getText().toString();
                    checkImageResource(true);

                    if(mPassword.length() == 6)
                        checkPassword();
                }
            }catch (Exception e){
                showToast("onClickListenerButtonPin() > " + e.getMessage());
            }
        }
    };

    private View.OnClickListener onClickListenerDel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                if(mPassword.length() > 0){
                    checkImageResource(false);
                    mPassword = mPassword.substring(0, mPassword.length() -1);
                }
            }catch(Exception ex){
                showToast("onClickListenerDel() > " + ex.getMessage());
            }
        }
    };

    private View.OnClickListener closeSessionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(PinVerified.this);
            alert.setTitle("Confirmacion");
            alert.setMessage("Cerrar la sesion?");
            alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @SuppressLint("DefaultLocale")
                public void onClick(DialogInterface dialog, int whichButton) {

                    SharedPreferences pref = PreferenceManager
                            .getDefaultSharedPreferences(PinVerified.this);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove(Variables.CODIGO_EMPLEADO);
                    editor.remove(Variables.NOMBRE_EMPLEADO);
                    editor.remove(Variables.USUARIO_EMPLEADO);
                    editor.remove(Variables.PASSWORD_EMPLEADO);
                    editor.commit();


                    Delete delete = new Delete(PinVerified.this);
                    delete.deleteAll();

                    //DETENER LOS SERVICIOS
                    Intent intent = new Intent(PinVerified.this, ServicioSocios.class);
                    stopService(intent);

                    Intent intent2 = new Intent(PinVerified.this, ServicioOvPr.class);
                    stopService(intent2);

                    //LANZAR LA PANTALLA DE LOGIN
                    Intent login = new Intent(PinVerified.this, LoginActivity.class);
                    startActivity(login);

                    finish();

                }
            });

            alert.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    };

    private void checkImageResource(boolean checked){
        try {
            switch (mPassword.length()){
                case 1:
                    img1.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
                case 2:
                    img2.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
                case 3:
                    img3.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
                case 4:
                    img4.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
                case 5:
                    img5.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
                case 6:
                    img6.setImageResource(checked ? R.drawable.lock_circle_green
                            : R.drawable.circle_white);
                    break;
            }
        }catch (Exception e){
            showToast("checkImageResource() > " + e.getMessage());
        }
    }

    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mProgressLayout.setVisibility(View.VISIBLE);
            mProgressLayout.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressLayout.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mPinLayout.setVisibility(View.VISIBLE);
            mPinLayout.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mPinLayout.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            mPinLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
