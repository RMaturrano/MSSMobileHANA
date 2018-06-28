package com.proyecto.sociosnegocio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.geolocalizacion.MapsActivity;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImgToolbar;

public class DetalleSocioNegocioMain extends AppCompatActivity {

	private Context contexto;
	private Fragment fragment = new DetalleSocioNegocioMainFTabs();
	private ListViewCustomAdapterTwoLinesAndImgToolbar adapter;
	private FormatCustomListView listFormat;
	private ListView lvInToolbar;

	public static int REQUEST_CHANGE_MAPS = 99;
	public static String idBusinessPartner = null;
	public static String MAIN_FRAGMENT = "F_MAIN";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socio_negocio_detalle_box);
		contexto = this;
		lvInToolbar = (ListView) findViewById(R.id.listViewToolbar);
		
		Intent myIntent = getIntent(); // gets the previously created intent

		if (myIntent.getStringExtra("id") != null) {
			idBusinessPartner = myIntent.getStringExtra("id");
		}

		// TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
		// TOOLBAR

		buildListInToolbar();
		

		// Llenar el fragment en el box
		if (findViewById(R.id.box) != null) {

			if (savedInstanceState != null) {

				fragment = getSupportFragmentManager().getFragment(
						savedInstanceState, "mContent");

			}

			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.box, fragment, MAIN_FRAGMENT);
			transaction.commit();

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == MapsActivity.REQUEST_MAPAS){
			if(data.getExtras().containsKey(MapsActivity.KEY_PARAM_LATITUD) &&
					data.getExtras().containsKey(MapsActivity.KEY_PARAM_LONGITUD)){
				DetalleSocioNegocioMainFTabs detailsFragment = (DetalleSocioNegocioMainFTabs) getSupportFragmentManager()
						.findFragmentByTag(MAIN_FRAGMENT);
				if(detailsFragment != null) {
					detailsFragment.notificarCambioUbicacion(String.valueOf(data.getExtras().getDouble(MapsActivity.KEY_PARAM_LATITUD)),
							String.valueOf(data.getExtras().getDouble(MapsActivity.KEY_PARAM_LONGITUD)));
				}
			}
		}
	}
	
	private void buildListInToolbar(){
		
		// TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//				MyDataBase cn = new MyDataBase(contexto, null, null, 3);
//				SQLiteDatabase db = cn.getWritableDatabase();

				Cursor rs = db.rawQuery("select  TipoSocio,  NombreRazonSocial " 
										+ "from TB_SOCIO_NEGOCIO "
										+ "WHERE Codigo ='"
										+ idBusinessPartner + "'", null);
				while (rs.moveToNext()) {

					listFormat = new FormatCustomListView();

					listFormat.setTitulo(rs.getString(1));
					listFormat.setData(idBusinessPartner);
					listFormat.setIcon(R.drawable.ic_account_circle_white_48dp);
					
					if(rs.getString(0).equals("C"))
						listFormat.setExtra("Cliente");
					else
						listFormat.setExtra("Lead");
					
					adapter = new ListViewCustomAdapterTwoLinesAndImgToolbar(contexto, listFormat);

				}

				rs.close();
//				db.close();
		
		
		lvInToolbar.setAdapter(adapter);
		
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 16908332:			
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
