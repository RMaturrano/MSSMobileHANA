package com.proyecto.sociosnegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLinesLead;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLinesLead.Item;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLinesLead.Row;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLinesLead.Section;
import com.proyecto.utils.CustomDialogSocio;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.Utils;
import com.proyecto.utils.Variables;

import org.json.JSONObject;

public class ListaSocioNegocioTabLead extends Fragment implements
		OnScrollListener, OnItemLongClickListener {

	private ListView lvSN;
	private ArrayList<FormatCustomListView> listaAdapter;
	private FormatCustomListView customListObjet = null;
	private Context contexto;
	private boolean mIsSearchResultView = false;
	private View v;
	private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
	private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
	private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;
	private static int icon_cloud_done_red = R.drawable.ic_cloud_done_red_36dp;
	private static int icon_cloud_done_green = R.drawable.ic_cloud_done_green_36dp;
	private SwipeRefreshLayout refreshLayout;
	
	private SharedPreferences pref;
	private String movilAprobar = "";
	private String movilEditar = "";
	private String movilCrear = "";
	private String movilRechazar = "";
	private FloatingActionButton fabCrear;

	// /// INDEXER
	private AlphabetListAdapterImgAndTwoLinesLead alphabetAdapter = new AlphabetListAdapterImgAndTwoLinesLead();
	private GestureDetector mGestureDetector;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private int sideIndexHeight;
	private static float sideIndexX;
	private static float sideIndexY;
	private int indexListSize;
	private LinearLayout sideIndex;

	class SideIndexGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			if (sideIndexX >= 0 && sideIndexY >= 0) {
				displayListItem();
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	// ///
	
	// RECIBE LOS PAR�METROS DESDE EL FRAGMENT CORRESPONDIENTE
	private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals("event-send-register-bp-ok")){
				alphabet.clear();
				sections.clear();
				builDataBusinessPartner();
				buildListCustomFormat();
			}
		}

	};
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.lista_socio_negocio_tab_fragment_lead,
				container, false);

		sideIndex = (LinearLayout) v.findViewById(R.id.sideIndex);
		lvSN = (ListView) v.findViewById(R.id.lvLstSocioLeadNeg);

		contexto = v.getContext();

		// Obtener el refreshLayout
		refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

		// Seteamos los colores que se usar�n a lo largo de la animaci�n
		refreshLayout.setColorSchemeResources(R.color.s1, R.color.s2,
				R.color.s3, R.color.s4);

		// FLOATING BUTTON
		fabCrear = (FloatingActionButton) v
				.findViewById(R.id.fab);
		fabCrear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent registrarSN = new Intent(v.getContext(),
						MainSocioNegocio.class);
				startActivity(registrarSN);
			}
		});
		fabCrear.setVisibility(View.INVISIBLE);
		// FLOATING BUTTON


		lvSN.setOnItemLongClickListener(this);
		lvSN.setOnScrollListener(this);
		setHasOptionsMenu(true);
		return v;

	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		builDataBusinessPartner();

		// registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
		IntentFilter filter = new IntentFilter("event-send-register-bp-ok");
		LocalBroadcastManager.getInstance(getContext()).registerReceiver(
				myLocalBroadcastReceiver, filter);

		/************************************/
		/*********** INDEXEEERRRR-> *************/
		/************************************/

		mGestureDetector = new GestureDetector(contexto,
				new SideIndexGestureListener());

		buildListCustomFormat();

		v.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else {
					return false;
				}
			}
		});

		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);

		// Iniciar la tarea al revelar el indicador
		refreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						alphabet.clear();
						sections.clear();
						builDataBusinessPartner();
						buildListCustomFormat();
						refreshLayout.setRefreshing(false);
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();
		comprobarPermisos();
	}

	private void comprobarPermisos(){

		String permisosMenu = pref.getString(Variables.MENU_SOCIOS_NEGOCIO, null);
		if (permisosMenu != null) {
			try {
				JSONObject permisos = new JSONObject(permisosMenu);
				movilCrear = permisos.getString(Variables.MOVIL_CREAR);
				movilEditar = permisos.getString(Variables.MOVIL_EDITAR);
				movilAprobar = permisos.getString(Variables.MOVIL_APROBAR);
				movilRechazar = permisos.getString(Variables.MOVIL_RECHAZAR);

				if(movilCrear != null && !movilCrear.trim().equals("")){
					if (!movilCrear.equals("")) {
						if (movilCrear.equalsIgnoreCase("Y")) {
							fabCrear.setVisibility(FloatingActionButton.VISIBLE);
						}
					}
				}

			} catch (Exception e) {
				Toast.makeText(contexto, "Ocurrió un error intentando obtener los permisos del menú", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void builDataBusinessPartner() {

		listaAdapter = new ArrayList<FormatCustomListView>();
		
		DataBaseHelper helper = DataBaseHelper.getHelper(getContext());
		SQLiteDatabase db = helper.getDataBase();

		Cursor rs = db
				.rawQuery(
						"select UPPER(LTRIM(RTRIM(IFNULL(NombreRazonSocial,'#')))),Codigo, " +
						"EstadoMovil,ClaveMovil,TransaccionMovil " +
						"from TB_SOCIO_NEGOCIO " +
						"WHERE TipoSocio = 'L' " +
						"order by NombreRazonSocial ASC",	
						null);
		while (rs.moveToNext()) {

			customListObjet = new FormatCustomListView();
			
			if (rs.getString(2).toString().equals("L")) {
				customListObjet.setIcon(icon_local);
			} else if (rs.getString(2).toString().equals("S")) {
				customListObjet.setIcon(icon_cloud_down);
			} else if(rs.getString(2).toString().equals("U") &&
					rs.getString(4).equalsIgnoreCase("1")){
				customListObjet.setIcon(icon_cloud_done);
			}  else if(rs.getString(2).toString().equals("U") &&
					rs.getString(4).equalsIgnoreCase("2")){
				customListObjet.setIcon(icon_cloud_done);
			}else if(rs.getString(2).toString().equals("U") &&
					rs.getString(4).equalsIgnoreCase("3")){
				customListObjet.setIcon(icon_cloud_done_red);
			} else if(rs.getString(2).toString().equals("U") &&
					rs.getString(4).equalsIgnoreCase("4")){
				customListObjet.setIcon(icon_cloud_done_green);
			}else{
				customListObjet.setIcon(icon_local);
			}

			customListObjet.setTitulo(rs.getString(1));
			if(rs.getString(0).equals(""))
				customListObjet.setData("#");
			else
				customListObjet.setData(rs.getString(0));
			customListObjet.setExtra(rs.getString(3));
			customListObjet.setExtra2(rs.getString(4));
			
			listaAdapter.add(customListObjet);

		}

		rs.close();
//		db.close();

	}

	private void buildListCustomFormat() {

		if (listaAdapter.size() > 0) {

			// adapter = new ListViewCustomAdapterImgAndTwoLinesWithFormat(
			// contexto, listaAdapter);

			List<Row> rows = new ArrayList<Row>();
			int start = 0;
			int end = 0;
			String previousLetter = null;
			Object[] tmpIndexItem = null;
			Pattern numberPattern = Pattern.compile("[0-9]");

			for (FormatCustomListView item : listaAdapter) {

				String firstLetter = item.getData().substring(0, 1);

				// Group numbers together in the scroller
				if (numberPattern.matcher(firstLetter).matches()) {
					firstLetter = "#";
				}

				// If we've changed to a new letter, add the previous letter to
				// the
				// alphabet scroller
				if (previousLetter != null
						&& !firstLetter.equals(previousLetter)) {
					end = rows.size() - 1;
					tmpIndexItem = new Object[3];
					tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
					tmpIndexItem[1] = start;
					tmpIndexItem[2] = end;
					alphabet.add(tmpIndexItem);

					start = end + 1;
				}

				// Check if we need to add a header row
				if (!firstLetter.equals(previousLetter)) {
					rows.add(new Section(firstLetter));
					sections.put(firstLetter, start);
				}

				// Add the country to the list
				rows.add(new Item(item));
				previousLetter = firstLetter;
			}

			if (previousLetter != null) {
				// Save the last letter
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
				tmpIndexItem[1] = start;
				tmpIndexItem[2] = rows.size() - 1;
				alphabet.add(tmpIndexItem);
			}

			alphabetAdapter.setRows(rows);
			lvSN.setAdapter(alphabetAdapter);

			updateList();

			/**************************************/
			/*********** <-INDEXEEERRRR *************/
			/*************************************/

			lvSN.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent myIntent = new Intent(v.getContext(),
							DetalleSocioNegocioMain.class);

					if (alphabetAdapter.getItemViewType(position) != 1) {
						Object o = alphabetAdapter.getItem(position);
						AlphabetListAdapterImgAndTwoLinesLead.Item item = (AlphabetListAdapterImgAndTwoLinesLead.Item) o;

						myIntent.putExtra("id", item.element.getTitulo());
						myIntent.putExtra("tipo", "Lead");
						startActivity(myIntent);
					}

				}

			});

		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.lst_sn_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);

		if (mIsSearchResultView) {
			searchItem.setVisible(false);
		}

		// In version 3.0 and later, sets up and configures the ActionBar
		// SearchView
		if (Utils.hasHoneycomb()) {
			final SearchManager searchManager = (SearchManager) getActivity()
					.getSystemService(Context.SEARCH_SERVICE);
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getActivity().getComponentName()));
			searchView
					.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
						@Override
						public boolean onQueryTextSubmit(String queryText) {

							return false;
						}

						@Override
						public boolean onQueryTextChange(String newText) {

							if(alphabetAdapter != null){
								alphabetAdapter.getFilter().filter(newText);
		                	}
							return false;
						}
					});
			searchView.setOnCloseListener(new SearchView.OnCloseListener() {

				@Override
				public boolean onClose() {
					if(alphabetAdapter != null){
						alphabetAdapter.getFilter().filter("");
                	}
					return true;
				}
			});

		}

	}

	// ///////INDEXER

	public void updateList() {

		sideIndex.removeAllViews();
		indexListSize = alphabet.size();
		if (indexListSize < 1) {
			return;
		}

		int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
		int tmpIndexListSize = indexListSize;
		while (tmpIndexListSize > indexMaxSize) {
			tmpIndexListSize = tmpIndexListSize / 2;
		}
		double delta;
		if (tmpIndexListSize > 0) {
			delta = indexListSize / tmpIndexListSize;
		} else {
			delta = 1;
		}

		TextView tmpTV;
		for (double i = 1; i <= indexListSize; i = i + delta) {
			Object[] tmpIndexItem = alphabet.get((int) i - 1);
			String tmpLetter = tmpIndexItem[0].toString();

			tmpTV = new TextView(contexto);
			tmpTV.setText(tmpLetter);
			tmpTV.setGravity(Gravity.CENTER);
			tmpTV.setTextSize(15);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
		}

		sideIndexHeight = sideIndex.getHeight();

		sideIndex.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// now you know coordinates of touch
				sideIndexX = event.getX();
				sideIndexY = event.getY();

				// and can display a proper item it country list
				displayListItem();

				return false;
			}
		});
	}

	public void displayListItem() {

		sideIndexHeight = sideIndex.getHeight();
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

		// compute the item index for given event position belongs to
		int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		// get the item (we can do it since we know item index)
		if (itemPosition < alphabet.size()) {
			Object[] indexItem = alphabet.get(itemPosition);
			int subitemPosition = sections.get(indexItem[0]);

			lvSN.setSelection(subitemPosition);
			// getListView().setSelection(subitemPosition);
		}
	}

	// SCROLL EVENT OF LISTVIEW
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		boolean enable = false;
		if (lvSN != null && lvSN.getChildCount() > 0) {
			// check if the first item of the list is visible
			boolean firstItemVisible = lvSN.getFirstVisiblePosition() == 0;
			// check if the top of the first item is visible
			boolean topOfFirstItemVisible = lvSN.getChildAt(0).getTop() == 0;
			// enabling or disabling the refresh layout
			enable = firstItemVisible && topOfFirstItemVisible;
		}
		refreshLayout.setEnabled(enable);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		if (alphabetAdapter.getItemViewType(position) != 1) {
			Object o = alphabetAdapter.getItem(position);
			AlphabetListAdapterImgAndTwoLinesLead.Item item = (AlphabetListAdapterImgAndTwoLinesLead.Item) o;

			if(item.element.getExtra2().equals("1") || 
					item.element.getExtra2().equals("2")){
			
				boolean crear = false;
				boolean actualizar = false;
				boolean rechazar = false;
				boolean aprobar = false;
				
				if(movilCrear.equalsIgnoreCase("Y"))
					crear = true;
				if(movilEditar.equalsIgnoreCase("Y"))
					actualizar = true;
				if(movilRechazar.equalsIgnoreCase("Y"))
					rechazar = true;
				if(movilAprobar.equalsIgnoreCase("Y"))
					aprobar = true;
				
				if(!crear && !actualizar && !rechazar && !aprobar){
					
					Toast.makeText(contexto, "No tiene permisos para realizar acciones", Toast.LENGTH_SHORT).show();
					return false;
					
				}else{
					
					CustomDialogSocio cd = new CustomDialogSocio();
					Dialog groupDialog = cd.CreateGroupDialog(contexto, "sn",
											  item.element.getTitulo(),
												crear, actualizar, rechazar, aprobar);
					groupDialog.show();
					
				}
				
			}
			
		}
		
		
		return true;
	}

}
