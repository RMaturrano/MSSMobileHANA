package com.proyecto.ventas;

import java.util.ArrayList;

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
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.CustomDialogPedido;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgORD;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Utils;
import com.proyecto.utils.Variables;

import org.json.JSONObject;

public class ListaVentasTabPendientesFragment extends Fragment
        implements OnItemClickListener, OnScrollListener, OnItemLongClickListener {


    private ListView lvOrd;
    private ListViewCustomAdapterFourRowAndImgORD adapter;
    private ArrayList<OrdenVentaBean> listaAdapter;
    private OrdenVentaBean customListObjet = null;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private boolean mIsSearchResultView = false;
    private SwipeRefreshLayout refreshLayout;

    private static int icon_local = R.drawable.ic_cloud_off_blue_36dp;
    private static int icon_cloud_down = R.drawable.ic_cloud_download_blue_36dp;
    private static int icon_cloud_done = R.drawable.ic_cloud_done_blue_36dp;
    private static int icon_cloud_done_red = R.drawable.ic_cloud_done_red_36dp;
    private static int icon_cloud_done_green = R.drawable.ic_cloud_done_green_36dp;

    private SharedPreferences pref;
    private String movilAprobar = "";
    private String movilEditar = "";
    private String movilCrear = "";
    private String movilRechazar = "";
    private FloatingActionButton fabCrear;

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
        View v = inflater.inflate(R.layout.lista_pedido_cliente_tab_pend_fragment, container, false);

        lvOrd = (ListView) v.findViewById(R.id.lvLstPedidoCliPendientes);

        contexto = v.getContext();

        // registrar los mensajes que se van a recibir DESDE OTROS FRAGMENTS
        IntentFilter filter = new IntentFilter("event-send-register-ov-ok");
        LocalBroadcastManager.getInstance(contexto).registerReceiver(
                myLocalBroadcastReceiver, filter);


        lvOrd.setOnItemClickListener(this);
        lvOrd.setOnItemLongClickListener(this);

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
                        adapter.clearAndAddAll(builDataOrd());
                        refreshLayout.setRefreshing(false);
                    }
                });

        pref = PreferenceManager
                .getDefaultSharedPreferences(contexto);

        // FLOATING BUTTON
        fabCrear = (FloatingActionButton) v
                .findViewById(R.id.fab);
        fabCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrarVenta = new Intent(v.getContext(),
                        MainVentas.class);
                startActivity(registrarVenta);
            }
        });
        fabCrear.setVisibility(View.INVISIBLE);
        // FLOATING BUTTON

        adapter = new ListViewCustomAdapterFourRowAndImgORD(contexto);
        lvOrd.setOnScrollListener(this);
        lvOrd.setAdapter(adapter);

        setHasOptionsMenu(true);
        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.clearAndAddAll(builDataOrd());
        comprobarPermisos();
    }

    private void comprobarPermisos() {

        String permisosMenu = pref.getString(Variables.MENU_PEDIDOS, null);
        if (permisosMenu != null) {
            try {
                JSONObject permisos = new JSONObject(permisosMenu);
                movilCrear = permisos.getString(Variables.MOVIL_CREAR);
                movilEditar = permisos.getString(Variables.MOVIL_EDITAR);
                movilAprobar = permisos.getString(Variables.MOVIL_APROBAR);
                movilRechazar = permisos.getString(Variables.MOVIL_RECHAZAR);

                if (movilCrear != null && !movilCrear.trim().equals("")) {
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

    private ArrayList<OrdenVentaBean> builDataOrd() {

        listaAdapter = new ArrayList<OrdenVentaBean>();

        //TRAER TODO DE SQLITE
        DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
        SQLiteDatabase db = helper.getDataBase();

//		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();


        Cursor rs = db.rawQuery("select OV.Clave,IFNULL(SN.NombreRazonSocial, ''),Total,Tipo,OV.FechaVencimiento," +
                "OV.EstadoMovil,OV.ClaveMovil, OV.TransaccionMovil " +
                "from TB_ORDEN_VENTA OV left JOIN TB_SOCIO_NEGOCIO SN " +
                "ON OV.SocioNegocio = SN.Codigo " +
                "where Tipo ='P' " +
                "AND SN.NombreRazonSocial IS NOT NULL " +
                "ORDER BY OV.Clave DESC", null);
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
            } else if (rs.getString(5).toString().equals("U") &&
                    rs.getString(7).equalsIgnoreCase("1")) {
                customListObjet.setUtilIcon2(icon_cloud_done);
            } else if (rs.getString(5).toString().equals("U") &&
                    rs.getString(7).equalsIgnoreCase("2")) {
                customListObjet.setUtilIcon2(icon_cloud_done);
            } else if (rs.getString(5).toString().equals("U") &&
                    rs.getString(7).equalsIgnoreCase("3")) {
                customListObjet.setUtilIcon2(icon_cloud_done_red);
            } else if (rs.getString(5).toString().equals("U") &&
                    rs.getString(7).equalsIgnoreCase("4")) {
                customListObjet.setUtilIcon2(icon_cloud_done_green);
            }

            customListObjet.setClaveMovil(rs.getString(6));
            customListObjet.setTransaccionMovil(rs.getString(7));

            listaAdapter.add(customListObjet);
        }
		
	/*	if(listaAdapter.size()>0){
			adapter = new ListViewCustomAdapterFourRowAndImgORD( contexto, listaAdapter);
	    	lvOrd.setAdapter(adapter);
		}		*/

        rs.close();
//		db.close();

        return listaAdapter;

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
        Intent myIntent = new Intent(contexto, DetalleVentaMain.class);
        myIntent.putExtra("id", ((OrdenVentaBean) adapter.getItem(position)).getNroDocOrdV());
        myIntent.putExtra("estado", ((OrdenVentaBean) adapter.getItem(position)).getEstadoDoc());
        myIntent.putExtra("estadoTransaccion", ((OrdenVentaBean) adapter.getItem(position)).getTransaccionMovil());
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


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {

        if (((OrdenVentaBean) adapter.getItem(position)).getTransaccionMovil().equals("1") ||
                ((OrdenVentaBean) adapter.getItem(position)).getTransaccionMovil().equals("2")) {
            boolean crear = false;
            boolean actualizar = false;
            boolean rechazar = false;
            boolean aprobar = false;

            if (movilCrear.equalsIgnoreCase("Y"))
                crear = true;
            if (movilEditar.equalsIgnoreCase("Y"))
                actualizar = true;
            if (movilRechazar.equalsIgnoreCase("Y"))
                rechazar = true;
            if (movilAprobar.equalsIgnoreCase("Y"))
                aprobar = true;

            if (!crear && !actualizar && !rechazar && !aprobar) {

                Toast.makeText(contexto, "No tiene permisos para realizar acciones", Toast.LENGTH_SHORT).show();
                return false;

            } else {

                CustomDialogPedido cd = new CustomDialogPedido();
                Dialog groupDialog = cd.CreateGroupDialog(contexto, "ov",
                        listaAdapter.get(position).getNroDocOrdV(),
                        crear, actualizar, rechazar, aprobar);
                groupDialog.show();

            }

        }


        return true;
    }


}
