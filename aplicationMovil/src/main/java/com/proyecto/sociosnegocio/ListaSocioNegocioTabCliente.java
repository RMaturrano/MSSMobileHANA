package com.proyecto.sociosnegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Item;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Row;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Section;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.Utils;

public class ListaSocioNegocioTabCliente extends Fragment implements OnScrollListener{

	private ListView lvSN;
	private ArrayList<FormatCustomListView> listaAdapter;
	private FormatCustomListView customListObjet = null;
	private Context contexto;
	private boolean mIsSearchResultView = false;
	private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
	private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
	private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;
	private View v;
	private LinearLayout sideIndex;
	private SwipeRefreshLayout refreshLayout;
	
	
	// /// INDEXER
	private AlphabetListAdapterImgAndTwoLines alphabetAdapter = new AlphabetListAdapterImgAndTwoLines();
	private GestureDetector mGestureDetector;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private int sideIndexHeight;
	private static float sideIndexX;
	private static float sideIndexY;
	private int indexListSize;

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

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.lista_socio_negocio_tab_fragment_cliente,
				container, false);

		sideIndex = (LinearLayout) v.findViewById(R.id.sideIndex);
		lvSN = (ListView) v.findViewById(R.id.lvLstSocioNeg);

		contexto = v.getContext();
		builDataBusinessPartner();
		
		/************************************/
		/***********INDEXEEERRRR->*************/
		/************************************/
		
		mGestureDetector = new GestureDetector(contexto,
				new SideIndexGestureListener());
		
		buildListCustomFormat();

		v.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") @Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		// Obtener el refreshLayout
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        // Seteamos los colores que se usarán a lo largo de la animación
        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        // Iniciar la tarea  al revelar el indicador
        refreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                	alphabet.clear();
                	sections.clear();
                	builDataBusinessPartner();
                	buildListCustomFormat();
                	refreshLayout.setRefreshing(false);
                }
            }
        );
        
        
        lvSN.setOnScrollListener(this);
		
		
		setHasOptionsMenu(true);
		return v;

	}
	
	
	private void builDataBusinessPartner() {

		listaAdapter = new ArrayList<FormatCustomListView>();

		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
		// TRAER TODO DE SQLITE
//		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery("select UPPER(NombreRazonSocial),Codigo, EstadoMovil "
				+ "from TB_SOCIO_NEGOCIO " 
				+ "WHERE TipoSocio = 'C' " 
				+ "order by NombreRazonSocial",
				null);
		while (rs.moveToNext()) {

			customListObjet = new FormatCustomListView();

			if (rs.getString(2).toString().equals("L")) {
				customListObjet.setIcon(icon_local);
			} else if (rs.getString(2).toString().equals("U")) {
				customListObjet.setIcon(icon_cloud_done);
			} else
				customListObjet.setIcon(icon_cloud_down);

			customListObjet.setTitulo(rs.getString(1));
			customListObjet.setData(rs.getString(0));
			listaAdapter.add(customListObjet);

		}

//		db.close();

	}

	private void buildListCustomFormat(){
		
		if (listaAdapter.size() > 0) {

			List<Row> rows = new ArrayList<Row>();
			int start = 0;
			int end = 0;
			String previousLetter = null;
			Object[] tmpIndexItem = null;
			Pattern numberPattern = Pattern.compile("[0-9]");
			
			
			for (FormatCustomListView item : listaAdapter) {
				
				String firstLetter = item.getData().substring(0,1);

				// Group numbers together in the scroller
				if (numberPattern.matcher(firstLetter).matches()) {
					firstLetter = "#";
				}

				// If we've changed to a new letter, add the previous letter to the
				// alphabet scroller
				if (previousLetter != null && !firstLetter.equals(previousLetter)) {
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
			/***********<-INDEXEEERRRR*************/
			/*************************************/
			
			
			
		//	lvSN.setAdapter(adapter);
			lvSN.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent myIntent = new Intent(v.getContext(),
							DetalleSocioNegocioMain.class);
					
					if(alphabetAdapter.getItemViewType(position)!= 1 ){
						Object o = alphabetAdapter.getItem(position);
						AlphabetListAdapterImgAndTwoLines.Item item =  (AlphabetListAdapterImgAndTwoLines.Item) o;
						
						myIntent.putExtra("id", item.element.getTitulo());
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

	
	/////////INDEXER
	
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
			@SuppressLint("ClickableViewAccessibility") @Override
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


	//SCROLL EVENT OF LISTVIEW
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		boolean enable = false;
	    if(lvSN != null && lvSN.getChildCount() > 0){
	        // check if the first item of the list is visible
	        boolean firstItemVisible = lvSN.getFirstVisiblePosition() == 0;
	        // check if the top of the first item is visible
	        boolean topOfFirstItemVisible = lvSN.getChildAt(0).getTop() == 0;
	        // enabling or disabling the refresh layout
	        enable = firstItemVisible && topOfFirstItemVisible;
	    }
	    refreshLayout.setEnabled(enable);
		
	}
	
	
	
	
}
