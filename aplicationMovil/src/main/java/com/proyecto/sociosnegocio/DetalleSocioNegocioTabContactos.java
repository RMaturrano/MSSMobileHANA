package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImgToolbarDialog;
import com.proyecto.utils.Variables;

public class DetalleSocioNegocioTabContactos extends Fragment implements
		OnItemClickListener {

	private View v;
	private ListView lvInfoBasica = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private int iconId = Variables.idIconRightBlue36dp;
	private String[] detCont = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_cont,
				container, false);

		contexto = v.getContext();
		// builDataBusinessPartner();

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

		lvInfoBasica = (ListView) v.findViewById(R.id.lvEditarSNTab2);

		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
		// TRAER TODO DE SQLITE
//		MyDataBase cn = new MyDataBase(contexto, null, null,
//				MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery(
				"select Codigo,Nombre, IFNULL(PrimerNombre,'null'), IFNULL(SegundoNombre,'null'), IFNULL(Apellidos,'null'), " +
				"IFNULL(Direccion,'null'), "
						+ " IFNULL(Telefono1,'null'), IFNULL(Telefono2,'null'), IFNULL(TelefonoMovil,'null'), " +
						"IFNULL(CorreoElectronico,'null'), IFNULL(Posicion,'null')"
						+ " from TB_SOCIO_NEGOCIO_CONTACTO WHERE CodigoSocioNegocio ='" + idBP + "'",
				null);

		while (rs.moveToNext()) {

			FormatCustomListView sr1 = new FormatCustomListView();
			sr1.setTitulo(rs.getString(0));
			sr1.setData(rs.getString(1));

			detCont = new String[6];
			detCont[0] = rs.getString(5);
			detCont[1] = rs.getString(6);
			detCont[2] = rs.getString(7);
			detCont[3] = rs.getString(8);
			detCont[4] = rs.getString(9);
			detCont[5] = rs.getString(10);
			
			sr1.setExtra(rs.getString(5) + "¡" + rs.getString(6) + "¡"
					+ rs.getString(7) + "¡" + rs.getString(8) + "¡"
					+ rs.getString(9) + "¡" + rs.getString(10));

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
		FormatCustomListView fullObject = new FormatCustomListView();
		fullObject = (FormatCustomListView) lvInfoBasica
				.getItemAtPosition(position);

		openBottomSheet(view, fullObject);

	}

	// BOTTOM SHEET
	@SuppressLint("InflateParams")
	public void openBottomSheet(View v, FormatCustomListView object) {

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.bottom_sheet_contact_bp, null);

		final Dialog mBottomSheetDialog = new Dialog(contexto,
				R.style.MaterialDialogSheet);

		// TOOLBAR
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case 16908332:
					mBottomSheetDialog.dismiss();
					return true;
				default:
					return true;
				}
			}
		});
		ListView lvInToolbar = (ListView) view.findViewById(R.id.listViewToolbar);
		buildListInToolbar(lvInToolbar, object);

		final ListView lvPrincipal = (ListView) view.findViewById(R.id.lvBottomSheet);
//		String[] extras = object.getExtra().split("¡");

		ArrayList<FormatCustomListView> searchResults = new ArrayList<FormatCustomListView>();
		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("Dirección");
		sr1.setData(detCont[0]);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Teléfono 1");
		sr1.setIcon(iconId);
		sr1.setData(detCont[1]);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Teléfono 2");
		sr1.setData(detCont[2]);
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Teléfono móvil");
		sr1.setData(detCont[3]);
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Correo electrónico");
		sr1.setData(detCont[4]);
		sr1.setIcon(iconId);
		searchResults.add(sr1);

		sr1 = new FormatCustomListView();
		sr1.setTitulo("Posición");
		sr1.setData(detCont[5]);
		searchResults.add(sr1);

		ListViewCustomAdapterTwoLinesAndImg adapter;
		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);
		lvPrincipal.setAdapter(adapter);
		lvPrincipal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(position == 1 || position == 2 || position == 3){
					FormatCustomListView fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(position);

					try {
						Intent callIntent = new Intent(Intent.ACTION_DIAL, null);
						callIntent.setData(Uri.parse("tel:" + fullObject.getData()));
						startActivity(callIntent);
					} catch (ActivityNotFoundException activityException) {
						Log.d("Calling a Phone Number", "Call failed"
								+ activityException);
					}
				}else if(position == 4){
					FormatCustomListView fullObject = new FormatCustomListView();
					fullObject = (FormatCustomListView) lvPrincipal
							.getItemAtPosition(position);

					try {
						
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("message/rfc822");
						intent.putExtra(Intent.EXTRA_EMAIL, new String[]{fullObject.getData()});
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						startActivity(Intent.createChooser(intent, "Enviar Email"));
						
					} catch (ActivityNotFoundException activityException) {
						Log.d("Send a Email", "Send failed"
								+ activityException);
					}
				}
				
			}
		});

		mBottomSheetDialog.setContentView(view);
		mBottomSheetDialog.setCancelable(true);
		mBottomSheetDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
		mBottomSheetDialog.show();

	}

	private void buildListInToolbar(final ListView lv, FormatCustomListView object) {

		FormatCustomListView listFormat = new FormatCustomListView();

		listFormat.setTitulo(object.getData());
		listFormat.setData(object.getTitulo());
		listFormat.setIcon(R.drawable.ic_contacts_white_36dp);

		ListViewCustomAdapterTwoLinesAndImgToolbarDialog adapter = 
					new ListViewCustomAdapterTwoLinesAndImgToolbarDialog(contexto,listFormat);

		lv.setAdapter(adapter);

	}

}
