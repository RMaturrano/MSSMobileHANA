package com.proyecto.movil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.proyect.movil.LoginActivity;
import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.Variables;

import java.io.IOException;

public class SplashActivity extends Activity{

	// Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 3000; // 3 segundos
    private Context contexto;
    private AlertDialog.Builder alert = null;
    
    //Lanza la aplicación
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		
    	contexto = this;
    	
    	if(getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    	{
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}else
    	{
    	// Establecer la orientación
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	}
        
    	// Ocultar la barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Tenemos una plantilla llamada splash.xml donde mostraremos la información (logotipo)
        setContentView(R.layout.splash_screen);

		DataBaseHelper dataBaseHelper = new DataBaseHelper(SplashActivity.this);

		//Asignar permisos
		if(Build.VERSION.SDK_INT >= 23){
			if(checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED){
				ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.INTERNET},1);
			}
			if(checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED){
				ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},2);
			}
			if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED){
				ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_CONTACTS},3);
			}
			if(checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED){
				ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.WRITE_CONTACTS},4);
			}
		}
        
        new Handler().postDelayed(new Runnable(){
            public void run(){
            // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
            	
            	SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(
                            contexto);
            	
            	String usuario = pref.getString(Variables.CODIGO_EMPLEADO,"");
        		String password = pref.getString(Variables.PASSWORD_EMPLEADO, "");
        		
        		Log.i("PASSWORD", password);

        		if(!usuario.equals("") && !password.equals("")){
        			
        			//construirAlertPassword(password);
					Intent intent = new Intent(SplashActivity.this, PinVerified.class);
					startActivity(intent);
					finish();
        			
        		}else{
        			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
    	        	startActivity(intent);
    	        	finish();
        		}
        		
	        	
            }

			private void construirAlertPassword(final String password) {
				
				alert = new AlertDialog.Builder(contexto);
    			alert.setTitle("Ingrese password");

    			final EditText edt = new EditText(contexto);
    			alert.setView(edt);
    			alert.setCancelable(false);
    			edt.setMaxLines(1);
    			edt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
    			edt.setImeOptions(EditorInfo.IME_ACTION_GO);
    			edt.setFocusableInTouchMode(true);
    			edt.requestFocus();
    			edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
    			edt.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						
							if (edt.getText().toString().length() > 0) {
	    						
	    						if(edt.getText().toString().equals(password)){
	    							Intent intent = new Intent(SplashActivity.this, MainActivityDrawer.class);
	    		    	        	startActivity(intent);
	    		    	        	finish();
	    						}else{
	    							Toast.makeText(contexto, "Password incorrecto", Toast.LENGTH_SHORT).show();
	    							construirAlertPassword(password);
	    						}

	    						InputMethodManager imm = (InputMethodManager) contexto
	    								.getSystemService(Context.INPUT_METHOD_SERVICE);
	    						imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);


	    					}else{
	    						Toast.makeText(contexto, "Password incorrecto", Toast.LENGTH_SHORT).show();
								construirAlertPassword(password);
	    					}
							
							return true;
						
					}
				});
    			
    			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    				@SuppressLint("DefaultLocale")
    				public void onClick(DialogInterface dialog, int whichButton) {

    					if (edt.getText().toString().length() > 0) {
    						
    						if(edt.getText().toString().equals(password)){
    							Intent intent = new Intent(SplashActivity.this, MainActivityDrawer.class);
    		    	        	startActivity(intent);
    		    	        	finish();
    						}else{
    							Toast.makeText(contexto, "Password incorrecto", Toast.LENGTH_SHORT).show();
    							construirAlertPassword(password);
    						}

    						InputMethodManager imm = (InputMethodManager) contexto
    								.getSystemService(Context.INPUT_METHOD_SERVICE);
    						imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);


    					}else{
    						Toast.makeText(contexto, "Password incorrecto", Toast.LENGTH_SHORT).show();
							construirAlertPassword(password);
    					}

    				}
    			});

    			alert.setNegativeButton("Cancel",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int whichButton) {
    							InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
    				            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    							finish();
    						}
    					});

    			edt.requestFocus();
    			InputMethodManager imm = (InputMethodManager) contexto
    					.getSystemService(Context.INPUT_METHOD_SERVICE);
    			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    			
    			if(!SplashActivity.this.isFinishing()){
    				alert.show();
    			}

			};
        }, DURACION_SPLASH);
    	
    }
    
    
    @Override
    protected void onDestroy() {
    	
    	try {
			if(alert != null){
				alert = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	super.onDestroy();
    }
    
}
