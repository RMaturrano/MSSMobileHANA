package com.proyecto.ventas;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgORD;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Utils;

public class ListaVentasTabTodosFragment extends Fragment
        implements OnItemClickListener, OnScrollListener {

    private ListView lvOrd;
    private ListViewCustomAdapterFourRowAndImgORD adapter;
    private ArrayList<OrdenVentaBean> listaAdapter;
    private OrdenVentaBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private boolean mIsSearchResultView = false;
    private SwipeRefreshLayout refreshLayout;

    private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
    private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
    private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;


    // RECIBE LOS PARÀMETROS DESDE EL FRAGMENT CORRESPONDIENTE
    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("event-send-register-ov-ok")) {
                builDataOrd();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.lista_pedido_cliente_tab_todos_fragment, container, false);

        lvOrd = (ListView) v.findViewById(R.id.lvLstPedidoCliTodos);

        contexto = v.getContext();
        adapter = new ListViewCustomAdapterFourRowAndImgORD(contexto);

        // registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
        IntentFilter filter = new IntentFilter("event-send-register-ov-ok");
        LocalBroadcastManager.getInstance(contexto).registerReceiver(
                myLocalBroadcastReceiver, filter);

        lvOrd.setOnItemClickListener(this);

        // Obtener el refreshLayout
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

        // Seteamos los colores que se usarán a lo largo de la animación
        refreshLayout.setColorSchemeResources(R.color.s1, R.color.s2,
                R.color.s3, R.color.s4);

        // Iniciar la tarea al revelar el indicador
        refreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        builDataOrd();
                        refreshLayout.setRefreshing(false);
                    }
                });


        lvOrd.setOnScrollListener(this);
        lvOrd.setAdapter(adapter);

        setHasOptionsMenu(true);
        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        builDataOrd();
    }

    private void builDataOrd() {

        listaAdapter = new ArrayList<OrdenVentaBean>();

        //TRAER TODO DE SQLITE
        DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
        SQLiteDatabase db = helper.getDataBase();

//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();


        Cursor rs = db.rawQuery("select OV.Clave,IFNULL(SN.NombreRazonSocial, ''),Total,Tipo,OV.FechaVencimiento," +
                "OV.EstadoMovil,OV.ClaveMovil " +
                "from TB_ORDEN_VENTA OV left JOIN TB_SOCIO_NEGOCIO SN " +
                "ON OV.SocioNegocio = SN.Codigo where Tipo ='A' " +
                "AND SN.NombreRazonSocial IS NOT NULL " +
                "ORDER BY OV.Clave", null);
        while (rs.moveToNext()) {

            customListObjet = new OrdenVentaBean();
            customListObjet.setUtilIcon(icon);
            customListObjet.setNroDocOrdV(rs.getString(0));
            customListObjet.setNomSN(rs.getString(1));
            customListObjet.setTotGeneral(rs.getDouble(2));

            if (rs.getString(3).equals("P"))
                customListObjet.setEstadoDoc("Pendiente");
            String fechaDocumento = StringDateCast.castStringtoDate(rs.getString(4));
            customListObjet.setFecDoc(fechaDocumento);

            if (rs.getString(5).toString().equals("L")) {
                customListObjet.setUtilIcon2(icon_local);
            } else if (rs.getString(5).toString().equals("S")) {
                customListObjet.setUtilIcon2(icon_cloud_down);
            } else
                customListObjet.setUtilIcon2(icon_cloud_done);
            customListObjet.setClaveMovil(rs.getString(6));

            listaAdapter.add(customListObjet);
        }
		
	/*	if(listaAdapter.size()>0){
			adapter = new ListViewCustomAdapterFourRowAndImgORD(contexto);
			adapter.clearAndAddAll(listaAdapter);
        	lvOrd.setAdapter(adapter);
		}		*/


        rs.close();
//		db.close();

        adapter.clearAndAddAll(listaAdapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.lst_sn_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        if (mIsSearchResultView) {
            searchItem.setVisible(false);
        }

        // In version 3.0 and later, sets up and configures the ActionBar SearchView
        if (Utils.hasHoneycomb()) {
            final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String queryText) {


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (adapter != null) {
                        adapter.getFilter().filter(newText);
                    }
                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {

                @Override
                public boolean onClose() {
                    if (adapter != null) {
                        adapter.getFilter().filter("");
                    }
                    return true;
                }
            });


        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent myIntent = new Intent(v.getContext(), DetalleVentaMain.class);
        myIntent.putExtra("id", ((OrdenVentaBean) adapter.getItem(position)).getNroDocOrdV());
        startActivity(myIntent);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        boolean enable = false;
        if (lvOrd != null && lvOrd.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = lvOrd.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = lvOrd.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        } else if (lvOrd != null && totalItemCount == 0) {
            enable = true;
        }

        refreshLayout.setEnabled(enable);

    }


}
