package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class DetalleSocioNegocioTabGeneral extends Fragment implements
		OnItemClickListener {

	private View v;
	private ListView lvInfoBasica = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private int iconId = Variables.idIconRightBlue36dp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_gen,
				container, false);

		contexto = v.getContext();
		if (DetalleSocioNegocioMain.idBusinessPartner != null) {

			idBP = DetalleSocioNegocioMain.idBusinessPartner;
			getItemsOfBusinessPartner();
			lvInfoBasica.setOnItemClickListener(this);

		}

		setHasOptionsMenu(true);
		return v;

	}

	private void getItemsOfBusinessPartner() {

		searchResults = new ArrayList<FormatCustomListView>();

		lvInfoBasica = (ListView) v.findViewById(R.id.lvEditarSNTabGen);

		// TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//		MyDataBase cn = new MyDataBase(contexto, null, null,
//				MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery(
				"select  Telefono1, Telefono2, TelefonoMovil, CorreoElectronico "
						+ "from TB_SOCIO_NEGOCIO " 
						+ "WHERE Codigo ='"
						+ idBP + "'", null);

		while (rs.moveToNext()) {

			FormatCustomListView sr1 = new FormatCustomListView();
			sr1.setTitulo("Teléfono 1");
			sr1.setData(rs.getString(0));
			sr1.setIcon(iconId);
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Teléfono 2");
			sr1.setData(rs.getString(1));
			sr1.setIcon(iconId);
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Teléfono móvil");
			sr1.setData(rs.getString(2));
			sr1.setIcon(iconId);
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Correo electrónico");
			sr1.setData(rs.getString(3));
			sr1.setIcon(iconId);
			searchResults.add(sr1);


		}
		rs.close();
//		db.close();

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvInfoBasica.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvInfoBasica);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == 0 || position == 1 || position == 2) {
			// Capturar el objeto (que refleja la selección estado doc)
			FormatCustomListView fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvInfoBasica
					.getItemAtPosition(position);

			try {
				Intent callIntent = new Intent(Intent.ACTION_DIAL, null);
				callIntent.setData(Uri.parse("tel:" + fullObject.getData()));
				startActivity(callIntent);
			} catch (ActivityNotFoundException activityException) {
				Log.d("Calling a Phone Number", "Call failed"
						+ activityException);
			}

		} else if (position == 3) {
			FormatCustomListView fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lvInfoBasica
					.getItemAtPosition(position);

			try {
				
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{fullObject.getData()});

				startActivity(Intent.createChooser(intent, "Enviar Email"));
				
			} catch (ActivityNotFoundException activityException) {
				Log.d("Send a Email", "Send failed"
						+ activityException);
			}

		}

	}

}
