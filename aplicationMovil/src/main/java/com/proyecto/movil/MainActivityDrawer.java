package com.proyecto.movil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.LoginActivity;
import com.proyect.movil.R;
import com.proyecto.cobranza.ListaCobranzasMain;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.facturas.ListaFacturasMain;
import com.proyecto.inventario.ListaArticulosMain;
import com.proyecto.preferences.SettingsMain;
import com.proyecto.reportes.ReporteFragment;
import com.proyecto.servicios.ServicioOvPr;
import com.proyecto.servicios.ServicioSocios;
import com.proyecto.servicios.SincManualTaskDocumentos;
import com.proyecto.servicios.SincManualTaskInicio;
import com.proyecto.servicios.SincManualTaskMaestros;
import com.proyecto.sociosnegocio.ListaSocioNegocio;
import com.proyecto.utils.Variables;
import com.proyecto.ventas.ListaVentasMain;

public class MainActivityDrawer extends AppCompatActivity {
	
	public static boolean shouldThreadTaskContinue = false;
	public static String action = "";
	
	private DrawerLayout drawerLayout;
	private String drawerTitle;
	private Context contexto;
	public static ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawer_layout);
		
		contexto = this;
		
		setToolbar(); // Setear Toolbar como action bar

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}
		
		View headerView = navigationView.inflateHeaderView(R.layout.nav_header); //Header del drawer
		TextView txtUserName =  (TextView) headerView.findViewById(R.id.username);
		TextView txtCodigo =  (TextView) headerView.findViewById(R.id.codigo);
		
		//LLenar head del drawer
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		
		String nombreEmpleado = pref.getString(Variables.NOMBRE_EMPLEADO, "");
		txtUserName.setText(nombreEmpleado);
		
		String codigoEmpleado = pref.getString(Variables.USUARIO_EMPLEADO, "");
		txtCodigo.setText(codigoEmpleado);

		drawerTitle = getResources().getString(R.string.titMenuPrincipal);
		if (savedInstanceState == null) {
			selectItem(drawerTitle);
		}
		
		
	}
	

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar ab = getSupportActionBar();
		if (ab != null) {
			// Poner ícono del drawer toggle
			ab.setHomeAsUpIndicator(R.drawable.ic_menu);
			ab.setDisplayHomeAsUpEnabled(true);
		}

	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						// Marcar item presionado
						menuItem.setChecked(true);
						
						// Crear nuevo fragmento
						String title = menuItem.getTitle().toString();
						selectItem(title);
						return true;
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
			getMenuInflater().inflate(R.menu.menu_principal, menu);
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
				drawerLayout.openDrawer(GravityCompat.START);
			return true;
		case R.id.action_close_session:
				closeSession();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void closeSession(){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Confirmación");
		alert.setMessage("¿Realmente desea salir de la aplicación?");
		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			@SuppressLint("DefaultLocale")
			public void onClick(DialogInterface dialog, int whichButton) {

				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(contexto);
				SharedPreferences.Editor editor = pref.edit();
				editor.remove(Variables.CODIGO_EMPLEADO);
				editor.remove(Variables.NOMBRE_EMPLEADO);
				editor.remove(Variables.USUARIO_EMPLEADO);
				editor.remove(Variables.PASSWORD_EMPLEADO);
				editor.commit();


//				Delete delete = new Delete(contexto);
//				delete.deleteAll();
				
				//DETENER LOS SERVICIOS
				Intent intent = new Intent(contexto, ServicioSocios.class);
				stopService(intent); 
				
				Intent intent2 = new Intent(contexto, ServicioOvPr.class);
				stopService(intent2);
				
				//LANZAR LA PANTALLA DE LOGIN
				Intent login = new Intent(contexto, LoginActivity.class);
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

	private void selectItem(String title) {
		
		// Enviar título como argumento del fragmento
		Bundle args = new Bundle();
		args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);
		
		if(title.equalsIgnoreCase(getResources().getString(R.string.menu_inicio))){
			
			Fragment fragment = PlaceholderFragment.newInstance(title);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, fragment)
					.commit();
			setTitle(title); // Setear título actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_socio_negocio))){
			
			Fragment fragment = new ListaSocioNegocio();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title);
            
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_inventario))){
			
			Fragment fragment = new ListaArticulosMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); 
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_actividades))){
			
			calendarEventM();
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_ventas))){
			
			Fragment fragment = ListaVentasMain.newInstance();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear título actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_facturas))){
			
			Fragment fragment = new ListaFacturasMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear título actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_cobranzas))){
			
			Fragment fragment = new ListaCobranzasMain();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear título actual
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_conexion))){
			
			Intent conn = new Intent(contexto,
					SettingsMain.class);
			startActivity(conn);
			
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_ini))){
			sincronizarInicio();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_doc))){
			sincronizarDocumentos();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_sincronizar_mast))){
			sincronizarMaestros();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_cerrar_sesion))){
			closeSession();
		}else if(title.equalsIgnoreCase(getResources().getString(R.string.menu_reportes))){
			
			Fragment fragment = new ReporteFragment();
			FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.commit();
            setTitle(title); // Setear título actual
			
		}else{
			Fragment fragment = PlaceholderFragment.newInstance(title);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, fragment)
					.commit();
		}

		drawerLayout.closeDrawers(); // Cerrar drawer
	//	setTitle(title); // Setear título actual

	}
	
	
	@SuppressLint("NewApi") 
	private void calendarEventM() {
/*		Calendar calendarEvent = Calendar.getInstance();
		Intent i = new Intent(Intent.ACTION_EDIT);
		i.setType("vnd.android.cursor.item/event");
		i.putExtra("beginTime", calendarEvent.getTimeInMillis());
		i.putExtra("allDay", false);
		i.putExtra("rule", "FREQ=YEARLY");
		i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
		i.putExtra("title", "Editar el título de su actividad aquí");
		startActivity(i);			*/
		
		long startMillis = System.currentTimeMillis();
		Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
		builder.appendPath("time");
		ContentUris.appendId(builder, startMillis);
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
		startActivity(intent);
		
	}
	
	private void sincronizarInicio(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere...");
				pd.setProgress(0);
				pd.setMax(17);
				pd.show();
				SincManualTaskInicio job = new SincManualTaskInicio(pd, contexto, "");
				job.execute();
				
			} else {
				Toast.makeText(contexto, "La conexión es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			
			Toast.makeText(contexto, "No está conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

		}
	}
	
	
	private void sincronizarDocumentos(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere...");
				pd.setProgress(0);
				pd.setMax(5);
				pd.show();
				SincManualTaskDocumentos job = new SincManualTaskDocumentos(pd, contexto, "");
				
				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
				    job.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					job.execute();
				}
				
			} else {
				Toast.makeText(contexto, "La conexión es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			
			Toast.makeText(contexto, "No está conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

		}
	}
	
	
	private void sincronizarMaestros(){
		// VERIFICAR EL ESTADO DE LA CONEXION DEL MOVIL
		boolean wifi = Connectivity.isConnectedWifi(contexto);
		boolean movil = Connectivity.isConnectedMobile(contexto);
		boolean isConnectionFast = Connectivity
				.isConnectedFast(contexto);

		if (wifi || movil) {

			if (isConnectionFast) {
				
//				Intent intent2 = new Intent(contexto, ServicioSociosWithTask.class);
//				intent2.setAction(Constants.ACTION_RUN_ISERVICE);
//				startService(intent2);

				pd = new ProgressDialog(contexto);
				pd.setTitle("Sincronizando");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.setMessage("Por favor, espere");
				pd.setProgress(0);
				pd.setMax(6);
				pd.show();
				SincManualTaskMaestros job = new SincManualTaskMaestros(pd, contexto, "");
				
				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
				    job.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					job.execute();
				}
				
			} else {
				Toast.makeText(contexto, "La conexión es inestable", Toast.LENGTH_LONG).show();
			}
		}else {
			
			Toast.makeText(contexto, "No está conectado a ninguna red de datos...", Toast.LENGTH_LONG).show();

		}
	}

}
