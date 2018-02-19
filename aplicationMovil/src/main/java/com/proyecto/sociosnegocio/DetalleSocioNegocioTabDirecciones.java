package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.DireccionBean;
import com.proyecto.dao.DireccionDAO;
import com.proyecto.database.Select;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImgToolbarDialog;

public class DetalleSocioNegocioTabDirecciones extends Fragment implements OnItemClickListener{

	private DireccionDAO mDireccionDAO;
	
	private View v;
	private ListView lvInfoBasica = null;
	private ListView lvBotomSheet;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ArrayList<FormatCustomListView> searchResultsBottomSheet;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private Dialog mBottomSheetDialog = null;
	private FormatCustomListView fullObject = new FormatCustomListView();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_direc,
				container, false);

		contexto = v.getContext();
		mDireccionDAO = new DireccionDAO();
		
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

		lvInfoBasica = (ListView) v.findViewById(R.id.lvEditarSNTabDir);

		Select select = new Select(contexto);
		searchResults = select.customList("detalleSNdirecciones", idBP);
		select.close();

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvInfoBasica.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvInfoBasica);

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		fullObject = (FormatCustomListView) lvInfoBasica
				.getItemAtPosition(position);

		openBottomSheet(view, fullObject);

	}
	
	// BOTTOM SHEET
	@SuppressLint("InflateParams")
	public void openBottomSheet(View v, FormatCustomListView object) {

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.bottom_sheet_contact_bp, null);

		mBottomSheetDialog = new Dialog(contexto,
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

		lvBotomSheet = (ListView) view.findViewById(R.id.lvBottomSheet);
		String[] extras = object.getExtra().split(";");

		searchResultsBottomSheet = new ArrayList<FormatCustomListView>();
		FormatCustomListView sr1 = new FormatCustomListView();
		sr1.setTitulo("País");
		sr1.setData(extras[0]);
		searchResultsBottomSheet.add(sr1);

		if(extras.length > 1) {

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Departamento");
			sr1.setData(extras[1]);
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Provincia");
			sr1.setData(extras[2]);
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Distrito");
			sr1.setData(extras[3]);
			searchResultsBottomSheet.add(sr1);

			if (!extras[4].equals("null")) {
				sr1 = new FormatCustomListView();
				sr1.setTitulo("Calle");
				sr1.setData(extras[4]);
				searchResultsBottomSheet.add(sr1);
			}


			if (!extras[5].equals("null")) {
				sr1 = new FormatCustomListView();
				sr1.setTitulo("Referencia");
				sr1.setData(extras[5]);
				searchResultsBottomSheet.add(sr1);
			}

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Latitud");
			sr1.setData(object.getLatitud());
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Longitud");
			sr1.setData(object.getLongitud());
			searchResultsBottomSheet.add(sr1);
		}

		ListViewCustomAdapterTwoLinesAndImg adapter;
		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResultsBottomSheet);
		lvBotomSheet.setAdapter(adapter);

		//FLOATING BUTTON
		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
		fab.setOnClickListener(floattingButtonSheetClick);
		//FLOATING BUTTON

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

	//Click en floatting button para editar latitud y longitud
	private View.OnClickListener floattingButtonSheetClick = new View.OnClickListener(){
		@Override
		public void onClick(View v) {

			getActivity().startActivityForResult(new Intent(contexto, SocioDireccionActivity.class)
										.putExtra("titulo", fullObject.getData())
										.putExtra("latitud", fullObject.getLatitud())
										.putExtra("longitud", fullObject.getLongitud()),
					DetalleSocioNegocioMain.REQUEST_CHANGE_MAPS);
		}
	};

	public void updateLatLon(String lat, String lon){

		DireccionBean direccionBean = new DireccionBean();
		direccionBean.setCodigoCliente(DetalleSocioNegocioMain.idBusinessPartner);
		direccionBean.setIDDireccion(fullObject.getTitulo());
		direccionBean.setLongitud(lon);
		direccionBean.setLatitud(lat);

		if(mDireccionDAO.update(direccionBean)){

			Toast.makeText(contexto, "Se actualizó la información de ubicación con éxito.", Toast.LENGTH_LONG).show();

			FormatCustomListView myObj = (FormatCustomListView) lvBotomSheet.getItemAtPosition(5);
			myObj.setData(lat);
			searchResultsBottomSheet.set(5, myObj);

			myObj = (FormatCustomListView) lvBotomSheet.getItemAtPosition(6);
			myObj.setData(lon);
			searchResultsBottomSheet.set(6, myObj);

			fullObject.setLatitud(lat);
			fullObject.setLongitud(lon);

			lvBotomSheet.invalidateViews();
		}else{
			Toast.makeText(contexto, "Ocurrió un problema actualizando la información de la ubicación.", Toast.LENGTH_LONG).show();
		}


	}

}
