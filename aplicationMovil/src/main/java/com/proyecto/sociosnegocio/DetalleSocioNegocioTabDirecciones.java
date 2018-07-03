package com.proyecto.sociosnegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.proyect.movil.R;
import com.proyecto.bean.DireccionBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.ClienteDAO;
import com.proyecto.dao.DireccionDAO;
import com.proyecto.database.Select;
import com.proyecto.geolocalizacion.MapsActivity;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImgToolbarDialog;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

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
	boolean wifi;
	boolean movil;
	boolean isConnectionFast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_direc,
				container, false);

		contexto = v.getContext();
		mDireccionDAO = new DireccionDAO();

		wifi = Connectivity.isConnectedWifi(contexto);
		movil = Connectivity.isConnectedMobile(contexto);
		isConnectionFast = Connectivity.isConnectedFast(contexto);

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
				R.layout.bottom_sheet_direction_bp, null);

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
		sr1.setTitulo("Pais");
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

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Zona");
			sr1.setData(object.getZona());
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Ruta");
			sr1.setData(object.getRuta());
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Canal");
			sr1.setData(object.getCanal());
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Giro");
			sr1.setData(object.getGiro());
			searchResultsBottomSheet.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Vendedor");
			sr1.setData(object.getVendedor());
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

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		String supervisor = pref.getString(Variables.SUPERVISOR, "N");

		if (supervisor.equals("Y")) {
			fab.setVisibility(View.VISIBLE);
		}

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

			try{
				if(wifi || movil && isConnectionFast){
					LatLng ubicacion = null;

					if(fullObject.getLatitud() != null && !TextUtils.isEmpty(fullObject.getLatitud()) &&
							fullObject.getLongitud() != null && !TextUtils.isEmpty(fullObject.getLongitud())) {
						ubicacion = new LatLng(Double.parseDouble(fullObject.getLatitud()),
								Double.parseDouble(fullObject.getLongitud()));
					}

					Intent location = new Intent(getActivity().getBaseContext(), MapsActivity.class);
					if(ubicacion != null){
						location.putExtra(MapsActivity.KEY_PARAM_LATITUD, ubicacion.latitude);
						location.putExtra(MapsActivity.KEY_PARAM_LONGITUD, ubicacion.longitude);
					}
					getActivity().startActivityForResult(location, MapsActivity.REQUEST_MAPAS);
				}else{
					showMessage("Para utilizar esta funcionalidad, primero, debe conectarse a una red de datos...");
				}

			}catch (Exception e){
				showMessage(e.getMessage());
			}
		}
	};

	public void updateLatLon(String lat, String lon){

		DireccionBean direccionBean = new DireccionBean();
		direccionBean.setCodigoCliente(DetalleSocioNegocioMain.idBusinessPartner);
		direccionBean.setIDDireccion(fullObject.getTitulo());
		direccionBean.setLongitud(lon);
		direccionBean.setLatitud(lat);
		direccionBean.setTipoDireccion(fullObject.getTipo());

		if(wifi || movil && isConnectionFast){
			mBottomSheetDialog.dismiss();
			showMessage("Enviando al servidor...");
			sendGeoToServer(direccionBean);
		}else{
			showMessage("No se pudo actualizar la ubicacion, se ha perdido la conexion de red.");
		}
	}

	private void showMessage(String message){
		if(message != null)
			Toast.makeText(contexto, message, Toast.LENGTH_LONG).show();
	}

	private void sendGeoToServer(final DireccionBean direccion){
		try{

			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(contexto);
			String codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "-1");
			String sociedad = pref.getString("sociedades", "-1");
			String ip = pref.getString("ipServidor", Constantes.DEFAULT_IP);
			String port = pref.getString("puertoServidor", Constantes.DEFAULT_PORT);
			String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

			JSONObject jsonObject = DireccionBean.transformToJSON(direccion);

			if(jsonObject != null) {

				jsonObject.put("Empresa", Integer.parseInt(sociedad));

				JsonObjectRequest jsonObjectRequest =
						new JsonObjectRequest(Request.Method.POST, ruta + "businesspartner/updateCoordinates.xsjs", jsonObject,
								new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										try
										{
											if(response.getString("ResponseStatus").equals("Success")){

												if(mDireccionDAO.update(direccion)){
													fullObject.setLatitud(direccion.getLatitud());
													fullObject.setLongitud(direccion.getLongitud());
													showMessage("Ubicacion enviada al servidor con exito.");
												}else{
													showMessage("Ocurrio un problema actualizando la informacion de la ubicacion.");
												}

											}else{
												showMessage(response.getJSONObject("Response")
														.getJSONObject("message")
														.getString("value"));
											}

										}catch (Exception e){showMessage("Response - " + e.getMessage());}
									}
								},
								new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										showMessage("VolleyError - " + error.getMessage());
									}
								}){
							@Override
							public Map<String, String> getHeaders() throws AuthFailureError {
								HashMap<String, String> headers = new HashMap<String, String>();
								headers.put("Content-Type", "application/json; charset=utf-8");
								return headers;
							}
						};
				VolleySingleton.getInstance(contexto).addToRequestQueue(jsonObjectRequest);

			}else
				showMessage("Error convirtiendo el archivo para el envio...");

		}catch (Exception e){
			showMessage("sendGeoToServer() > " + e.getMessage());
		}
	}

}
