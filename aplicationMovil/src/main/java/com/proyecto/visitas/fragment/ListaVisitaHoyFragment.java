package com.proyecto.visitas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.dao.DireccionDAO;
import com.proyecto.geolocalizacion.ShowMapActivity;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.visitas.DetalleVisitaActivity;
import com.proyecto.visitas.adapter.recyclerview.RVAdapterListaVisitas;
import com.proyecto.visitas.adapter.recyclerview.listeners.IRVAdapterListaVisitas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListaVisitaHoyFragment extends Fragment implements IRVAdapterListaVisitas{

    public static String KEY_PARM_DIRECTION = "direction";

    private View mView;
    private RVAdapterListaVisitas mRVAdapterListaVisitas;
    private RecyclerView mRVListaDireccionesVisita;
    private SwipeRefreshLayout mRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_lista_direcciones_visita_tab_hoy, container, false);

        mRVListaDireccionesVisita = (RecyclerView) mView.findViewById(R.id.rvListaVisitasHoy);
        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swpListaVisitasHoy);
        mRefresh.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mRefresh.setOnRefreshListener(onRefreshListener);

        mRVListaDireccionesVisita.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRVListaDireccionesVisita.setHasFixedSize(true);

        mRVAdapterListaVisitas = new RVAdapterListaVisitas(this);
        mRVListaDireccionesVisita.setAdapter(mRVAdapterListaVisitas);

        setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadRecyclerView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_show_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnu_go_map:
                showDirectionsSelectedOnMap();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadRecyclerView(){
        try{

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = dateFormat.format(new Date());

            mRVAdapterListaVisitas.clearAndAddAll(new DireccionDAO()
                    .listarDireccionesVisita(Calendar.getInstance()
                                                     .get(Calendar.DAY_OF_WEEK), date));
        }catch (Exception e){
            showMessage("Loading exception: " + e.getMessage());
        }
    }

    @Override
    public void onItemClick(DireccionBuscarBean direccion) {
        Intent intentDetalle = new Intent(getActivity(), DetalleVisitaActivity.class);
        intentDetalle.putExtra(DetalleVisitaActivity.KEY_PARAM_DIRECCION, direccion);
        startActivity(intentDetalle);
    }

    @Override
    public void onItemLongClick(DireccionBuscarBean direccion) {

    }

    private void showDirectionsSelectedOnMap(){
        try{
            ArrayList<DireccionBuscarBean> directions = new ArrayList<>();
            for (DireccionBuscarBean bean: mRVAdapterListaVisitas.getAllItems()) {
                if(bean.isSelected()){
                    if(bean.getLatitud() != null && bean.getLongitud() != null
                            && !TextUtils.isEmpty(bean.getLatitud()) && !TextUtils.isEmpty(bean.getLongitud())) {
                        directions.add(bean);
                    }
                }
            }

            if(directions.size() > 0){
                Intent mapIntent = new Intent(getActivity(), ShowMapActivity.class);
                mapIntent.putParcelableArrayListExtra(KEY_PARM_DIRECTION, directions);
                startActivity(mapIntent);
            }else{
                //showMessage("No ha seleccionado ninguna dirección o no se encontró información de coordenadas.");
            }
        }catch(Exception e){
            showMessage(e.getMessage());
        }
    }

    @Override
    public void onMapClick(DireccionBuscarBean direccion) {
        if(direccion.getLatitud() != null && direccion.getLongitud() != null
                && !TextUtils.isEmpty(direccion.getLatitud()) && !TextUtils.isEmpty(direccion.getLongitud())){
            Intent mapIntent = new Intent(getActivity(), ShowMapActivity.class);
            ArrayList<DireccionBuscarBean> directions = new ArrayList<>();
            directions.add(direccion);
            mapIntent.putParcelableArrayListExtra(KEY_PARM_DIRECTION, directions);
            startActivity(mapIntent);
        }else
            showMessage("La dirección no cuenta con información de coordenadas.");
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadRecyclerView();
            mRefresh.setRefreshing(false);
        }
    };

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
