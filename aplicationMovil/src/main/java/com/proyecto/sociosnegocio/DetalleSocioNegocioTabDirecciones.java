package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.proyect.movil.R;
import com.proyecto.database.Select;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImgToolbarDialog;

public class DetalleSocioNegocioTabDirecciones extends Fragment implements OnItemClickListener{
	
	
	private View v;
	private ListView lvInfoBasica = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_direc,
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
			String[] extras = object.getExtra().split(";");

			ArrayList<FormatCustomListView> searchResults = new ArrayList<FormatCustomListView>();
			FormatCustomListView sr1 = new FormatCustomListView();
			sr1.setTitulo("País");
			sr1.setData(extras[0]);
			searchResults.add(sr1);

			if(extras.length > 1) {

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Departamento");
				sr1.setData(extras[1]);
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Provincia");
				sr1.setData(extras[2]);
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Distrito");
				sr1.setData(extras[3]);
				searchResults.add(sr1);

				if (!extras[4].equals("null")) {
					sr1 = new FormatCustomListView();
					sr1.setTitulo("Calle");
					sr1.setData(extras[4]);
					searchResults.add(sr1);
				}


				if (!extras[5].equals("null")) {
					sr1 = new FormatCustomListView();
					sr1.setTitulo("Referencia");
					sr1.setData(extras[5]);
					searchResults.add(sr1);
				}
			}

			ListViewCustomAdapterTwoLinesAndImg adapter;
			adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
					searchResults);
			lvPrincipal.setAdapter(adapter);

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
