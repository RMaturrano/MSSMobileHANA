package com.proyecto.ventas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.perf.metrics.AddTrace;
import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.inventario.DetalleArticuloMain;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Item;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Row;
import com.proyecto.utils.AlphabetListAdapterImgAndTwoLines.Section;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterImgAndTwoLinesWithFormat;
import com.proyecto.utils.Utils;

public class BuscarArtFragment extends Fragment implements OnItemClickListener {

    private ListView lvBuscarArt;
    private ArrayList<FormatCustomListView> listaAdapter;
    private FormatCustomListView customListObjet = null;
    private Context contexto;
    private Bundle arguments = null;
    private boolean mIsSearchResultView = false;
    private View v;

    private SQLiteDatabase db;
    private DataBaseHelper helper;

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
                // displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    // ///

    private static int icon = R.drawable.inventory;

    @Override
    @AddTrace(name = "onCreateViewTraceBuscarArtFragment", enabled = true)
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedido_cliente_buscar_articulo,
                viewGroup, false);

        v = view;

        helper = DataBaseHelper.getHelper(contexto);
        db = helper.getDataBase();
        lvBuscarArt = (ListView) view.findViewById(R.id.lvPedCliBuscarArticulo);

        getActivity().setTitle("Inventario");

        contexto = view.getContext();
        llenarDatos();

        /************************************/
        /*********** INDEXEEERRRR-> *************/
        /************************************/

        mGestureDetector = new GestureDetector(contexto,
                new SideIndexGestureListener());

        if (listaAdapter.size() > 0) {

            List<Row> rows = new ArrayList<Row>();
            int start = 0;
            int end = 0;
            String previousLetter = null;
            Object[] tmpIndexItem = null;
            Pattern numberPattern = Pattern.compile("[0-9]");

            for (FormatCustomListView item : listaAdapter) {

                String firstLetter = item.getGrupo().substring(0, 1);

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
            lvBuscarArt.setAdapter(alphabetAdapter);

            updateList();

            lvBuscarArt.setOnItemClickListener(this);

        }

        /**************************************/
        /*********** <-INDEXEEERRRR *************/
        /*************************************/

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

        setHasOptionsMenu(true);
        return view;
    }

    @AddTrace(name = "llenarDatosTraceBuscarArtFragment", enabled = true)
    private void llenarDatos() {

        listaAdapter = new ArrayList<FormatCustomListView>();

        Cursor rs = db
                .rawQuery(
                        "select DISTINCT A.Codigo,A.Nombre,A.Fabricante,A.GrupoArticulo, " +
                                "A.GrupoUnidadMedida,A.UnidadMedidaVenta," +
                                "GUM.Nombre,GA.NOMBRE as Grupo, A.AlmacenDefecto " +
                                "from TB_ARTICULO A JOIN TB_GRUPO_UNIDAD_MEDIDA GUM " +
                                "ON A.GrupoUnidadMedida = GUM.Codigo " +
                                " JOIN TB_GRUPO_ARTICULO GA ON A.GrupoArticulo = GA.CODIGO " +
                                " left join TB_PRECIO P ON P.Articulo = A.Codigo " +
                                "AND P.CodigoLista IN(SELECT X0.ListaPrecio from TB_SOCIO_NEGOCIO X0)" +
                                " GROUP BY A.Codigo, A.Nombre " +
                                " having SUM(P.PrecioVenta) > 0 " +
                                "ORDER BY GA.NOMBRE, A.Nombre",
                        null);
//				.rawQuery(
//						"select A.Codigo,A.Nombre,A.Fabricante,A.GrupoArticulo," +
//								"A.GrupoUnidadMedida,A.UnidadMedidaVenta," +
//								"GUM.Nombre," +
//								"IFNULL((SELECT SUM(STOCK) FROM TB_CANTIDAD WHERE ARTICULO = A.Codigo),'0'), " +
//								"IFNULL((SELECT SUM(COMPROMETIDO) FROM TB_CANTIDAD WHERE ARTICULO = A.Codigo),'0'), " +
//								"IFNULL((SELECT SUM(SOLICITADO) FROM TB_CANTIDAD WHERE ARTICULO = A.Codigo),'0'), " +
//								"IFNULL((SELECT SUM(DISPONIBLE) FROM TB_CANTIDAD WHERE ARTICULO = A.Codigo),'0') " +
//								"from TB_ARTICULO A JOIN TB_GRUPO_UNIDAD_MEDIDA GUM " +
//								"ON A.GrupoUnidadMedida = GUM.Codigo " +
//								"ORDER BY A.Nombre",
//						null);


        if (rs.getCount() > 0) {
            Toast.makeText(contexto.getApplicationContext(), "Cargando " + rs.getCount() + " datos. Por favor espere..", Toast.LENGTH_SHORT).show();
            while (rs.moveToNext()) {

                customListObjet = new FormatCustomListView();
                customListObjet.setIcon(icon);
                customListObjet.setGrupo(rs.getString(rs.getColumnIndex("Grupo")));
                customListObjet.setAlmacenDefecto(rs.getString(rs.getColumnIndex("AlmacenDefecto")));
                if (!rs.getString(0).equals(""))
                    customListObjet.setTitulo(rs.getString(0));
                else
                    customListObjet.setTitulo("# NO CODE, SINC AGAIN");
                if (!rs.getString(1).equals(""))
                    customListObjet.setData(rs.getString(1));
                else
                    customListObjet.setData("# NO NAME, SINC AGAIN");

                customListObjet.setExtra(rs.getString(2) + "#" + rs.getString(3) + "#" + rs.getString(4) + "#" + rs.getString(5) + "#" + rs.getString(6));
//				customListObjet.setExtra2(rs.getString(7)+"�"+rs.getString(8)+"�"+rs.getString(9)+"�"+rs.getString(10));

                listaAdapter.add(customListObjet);
            }
        }

        rs.close();
//		db.close();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar_art_fragment, menu);
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

                            if (alphabetAdapter != null) {
                                alphabetAdapter.getFilter().filter(newText);
                            }

                            return false;
                        }
                    });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {

                @Override
                public boolean onClose() {
                    if (alphabetAdapter != null) {
                        alphabetAdapter.getFilter().filter("");
                    }
                    return true;
                }
            });

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (item.getItemId()) {

            case 16908332:

                transaction.remove(this);
                transaction.commit();

                getActivity().setTitle("Articulo");
                getFragmentManager().popBackStack();

                return true;

            case R.id.action_details:

                if (arguments != null) {

                    if (alphabetAdapter.getItemViewType(arguments.getInt("position")) != 1) {
                        Object o = alphabetAdapter.getItem(arguments.getInt("position"));
                        AlphabetListAdapterImgAndTwoLines.Item item2 = (AlphabetListAdapterImgAndTwoLines.Item) o;

                        //openBottomSheet(null, item2);

                        Intent myIntent = new Intent(v.getContext(), DetalleArticuloMain.class);
                        myIntent.putExtra("id", item2.element.getTitulo());
                        startActivity(myIntent);
                    }

                } else {

                    Toast.makeText(contexto, "Seleccione el articulo",
                            Toast.LENGTH_LONG).show();

                }

                return true;

            case R.id.action_aceptar:

                if (arguments != null) {

                    // MANDAR LOS PAR�METROS EN LOCALBORADCAST INTENT
                    Intent localBroadcastIntent = new Intent("buscar_art");
                    localBroadcastIntent.putExtras(arguments);
                    LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager
                            .getInstance(getActivity());
                    myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

                    transaction.remove(this);
                    transaction.commit();

                    // LLAMAR EL POPBACKSTACK(VOLVER)
                    getActivity().setTitle("Articulo");
                    getActivity().getFragmentManager().popBackStack();

                } else {

                    Toast.makeText(contexto, "Seleccione el articulo",
                            Toast.LENGTH_LONG).show();

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (alphabetAdapter.getItemViewType(position) != 1) {
            Object o = alphabetAdapter.getItem(position);
            AlphabetListAdapterImgAndTwoLines.Item item = (AlphabetListAdapterImgAndTwoLines.Item) o;

            arguments = new Bundle();
            arguments.putString("desc", item.element.getData());
            arguments.putString("cod", item.element.getTitulo());
            arguments.putString("almacen", item.element.getAlmacenDefecto());
            arguments.putString("extras", item.element.getExtra());
            arguments.putInt("position", position);

            //	openBottomSheet(view, item);

        }


    }

    //BOTTOM SHEET
    @SuppressLint("InflateParams")
    public void openBottomSheet(View v, AlphabetListAdapterImgAndTwoLines.Item item) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet, null);

        final Dialog mBottomSheetDialog = new Dialog(contexto,
                R.style.MaterialDialogSheet);

        ListView lvPrincipal = (ListView) view.findViewById(R.id.lvBottomSheet);

        ArrayList<FormatCustomListView> searchResults = new ArrayList<FormatCustomListView>();
        FormatCustomListView sr1 = new FormatCustomListView();
        sr1.setTitulo("Codigo");
        sr1.setIcon(R.drawable.ic_vpn_key_blue_24dp);
        sr1.setData(item.element.getTitulo());
        searchResults.add(sr1);

        sr1 = new FormatCustomListView();
        sr1.setTitulo("Descripcion");
        sr1.setIcon(R.drawable.ic_feedback_blue_24dp);
        sr1.setData(item.element.getData());
        searchResults.add(sr1);

        Cursor rs = db.rawQuery(
                "select " +
                        "IFNULL((SELECT SUM(STOCK)), 0), " +
                        "IFNULL((SELECT SUM(COMPROMETIDO)),'0'), " +
                        "IFNULL((SELECT SUM(SOLICITADO)),'0'), " +
                        "IFNULL((SELECT SUM(DISPONIBLE)),'0') " +
                        " FROM TB_CANTIDAD WHERE ARTICULO = '" + item.element.getTitulo() + "' "
                ,
                null);

        String[] extras = new String[4];
        while (rs.moveToNext()) {

            extras[0] = rs.getString(0);
            extras[1] = rs.getString(1);
            extras[2] = rs.getString(2);
            extras[3] = rs.getString(3);
        }

        if (rs != null && !rs.isClosed())
            rs.close();

        sr1 = new FormatCustomListView();
        sr1.setTitulo("Stock");
        sr1.setData(extras[0]);
        sr1.setIcon(R.drawable.ic_trending_up_blue_24dp);
        searchResults.add(sr1);

        sr1 = new FormatCustomListView();
        sr1.setTitulo("Comprometido");
        sr1.setData(extras[1]);
        sr1.setIcon(R.drawable.ic_transfer_within_a_station_blue_24dp);
        searchResults.add(sr1);

        sr1 = new FormatCustomListView();
        sr1.setTitulo("Solicitado");
        sr1.setData(extras[2]);
        sr1.setIcon(R.drawable.ic_transfer_within_a_station_blue_24dp);
        searchResults.add(sr1);

        sr1 = new FormatCustomListView();
        sr1.setTitulo("Disponible");
        sr1.setData(extras[3]);
        sr1.setIcon(R.drawable.ic_transfer_within_a_station_blue_24dp);
        searchResults.add(sr1);

        ListViewCustomAdapterImgAndTwoLinesWithFormat adapter;
        adapter = new ListViewCustomAdapterImgAndTwoLinesWithFormat(contexto, searchResults);
        lvPrincipal.setAdapter(adapter);


        //FLOATING BUTTON
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                // MANDAR LOS PAR�METROS EN LOCALBORADCAST INTENT
                Intent localBroadcastIntent = new Intent("buscar_art");
                localBroadcastIntent.putExtras(arguments);
                LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager
                        .getInstance(getActivity());
                myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

                transaction.remove(BuscarArtFragment.this);
                transaction.commit();

                // LLAMAR EL POPBACKSTACK(VOLVER)
                getActivity().setTitle("Articulo");
                getActivity().getFragmentManager().popBackStack();

            }
        });
        //FLOATING BUTTON


        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }


    // ///////INDEXER
    @AddTrace(name = "updateListraceBuscarArtFragment", enabled = true)
    public void updateList() {
        LinearLayout sideIndex = (LinearLayout) v.findViewById(R.id.sideIndex);
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
        LinearLayout sideIndex = (LinearLayout) v.findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);
            int subitemPosition = sections.get(indexItem[0]);

            lvBuscarArt.setSelection(subitemPosition);
            // getListView().setSelection(subitemPosition);
        }
    }

}
