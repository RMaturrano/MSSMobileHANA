package com.proyecto.entregas.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.dao.EntregaDAO;
import com.proyecto.entregas.EntregaDetalleActivity;
import com.proyecto.entregas.adapter.recyclerview.RVAdapterListEntrega;
import com.proyecto.entregas.adapter.recyclerview.listeners.IRVAdapterListEntrega;
import com.proyecto.geolocalizacion.ShowMapActivity;

import java.util.ArrayList;
import java.util.List;

public class ListaEntregaFragment extends Fragment implements IRVAdapterListEntrega{

    public static String KEY_PARM_DIRECTION = "entregaDir";

    private View mView;
    private RVAdapterListEntrega mRVAdapterListEntrega;
    private RecyclerView mRVListaEntrega;
    private SwipeRefreshLayout mRefresh;
    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_lista_entrega, container, false);

        mRVListaEntrega = (RecyclerView) mView.findViewById(R.id.rvListaEntrega);
        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swpListaEntrega);
        mRefresh.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mRefresh.setOnRefreshListener(onRefreshListener);

        mRVListaEntrega.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRVListaEntrega.setHasFixedSize(true);

        mRVAdapterListEntrega = new RVAdapterListEntrega(this);
        mRVListaEntrega.setAdapter(mRVAdapterListEntrega);

        setHasOptionsMenu(true);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        obtenerEntregas();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.menu_lista_entrega, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(mSearchListener);
        //mSearchView.setOnCloseListener(mSearchCloseListener);
        mSearchView.setQueryHint("Codigo o nombre de cliente, referencia...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_go_map:
                showDirectionsSelectedOnMap();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private SearchView.OnQueryTextListener mSearchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText == null || newText.equals(""))
                obtenerEntregas();
            else
                mRVAdapterListEntrega.getFilter().filter(newText.toLowerCase());
            return false;
        }
    };

    private SearchView.OnCloseListener mSearchCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            obtenerEntregas();
            if (!mSearchView.isIconified())
                mSearchView.setIconified(true);
            return true;
        }
    };


    @Override
    public void onItemClick(EntregaBean entrega) {
        Intent intent = new Intent(mView.getContext(), EntregaDetalleActivity.class);
        intent.putExtra(EntregaDetalleActivity.KEY_PARAM_ENTREGA, entrega);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(EntregaBean entrega) {

    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obtenerEntregas();
        }
    };

    private void showDirectionsSelectedOnMap(){
        try{
            List<EntregaBean> entregas = new ArrayList<>();
            for (EntregaBean bean: mRVAdapterListEntrega.getAllItems()) {
                if(bean.isSelected()){
                    if(bean.getDireccionEntregaLatitud() != null && bean.getDireccionEntregaLongitud() != null
                            && !TextUtils.isEmpty(bean.getDireccionEntregaLatitud())
                            && !TextUtils.isEmpty(bean.getDireccionEntregaLongitud())) {
                        entregas.add(bean);
                    }
                }
            }

            if(entregas.size() > 0){
                Intent mapIntent = new Intent(getActivity(), ShowMapActivity.class);
                mapIntent.putParcelableArrayListExtra(KEY_PARM_DIRECTION, (ArrayList<? extends Parcelable>) entregas);
                startActivity(mapIntent);
            }else{
                showToast("No ha seleccionado ninguna dirección o no se encontró información de coordenadas.");
            }
        }catch(Exception e){
            showToast(e.getMessage());
        }
    }

    private void obtenerEntregas(){
        try{
            mRefresh.setRefreshing(true);
            mRVAdapterListEntrega.clearAndAddAll(new EntregaDAO().listar(null));
            mRefresh.setRefreshing(false);
        }catch (Exception e){
            showToast(e.getMessage());
        }
    }

    private void showToast(final String message){
        if(message != null)
            Toast.makeText(mView.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
