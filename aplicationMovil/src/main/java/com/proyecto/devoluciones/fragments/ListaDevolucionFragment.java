package com.proyecto.devoluciones.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.DevolucionBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.DevolucionDAO;
import com.proyecto.devoluciones.DevolucionActivity;
import com.proyecto.devoluciones.DevolucionDetalleActivity;
import com.proyecto.devoluciones.adapter.recyclerview.RVAdapterListaDevolucion;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterListaDevolucion;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListaDevolucionFragment extends Fragment implements IRVAdapterListaDevolucion{

    private FloatingActionButton fabAddDevolucion;
    private RecyclerView mRVListaDevolucion;
    private RVAdapterListaDevolucion mAdapter;
    private SwipeRefreshLayout mRefresh;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_devolucion, container, false);

        fabAddDevolucion = (FloatingActionButton) v.findViewById(R.id.fabAddDevolucion);
        fabAddDevolucion.setOnClickListener(fabClickListener);
        fabAddDevolucion.setVisibility(View.INVISIBLE);

        mRVListaDevolucion = (RecyclerView) v.findViewById(R.id.rvListaDevolucion);
        mRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swpListaDevolucion);
        mRefresh.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mRefresh.setOnRefreshListener(onRefreshListener);

        mRVListaDevolucion.setLayoutManager(new LinearLayoutManager(v.getContext()));
        mRVListaDevolucion.setHasFixedSize(true);

        mAdapter = new RVAdapterListaDevolucion(this);
        mRVListaDevolucion.setAdapter(mAdapter);

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        obtenerDevoluciones();
        comprobarPermisosMenu();
    }

    private void comprobarPermisosMenu(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String permisosMenu = pref.getString(Variables.MENU_DEVOLUCIONES, null);
        if (permisosMenu != null) {
            try {
                JSONObject permisos = new JSONObject(permisosMenu);
                String movilCrear = permisos.getString(Variables.MOVIL_CREAR);

                if(movilCrear != null && !movilCrear.trim().equals("")){
                    if (!movilCrear.equals("")) {
                        if (movilCrear.equalsIgnoreCase("Y")) {
                            fabAddDevolucion.setVisibility(FloatingActionButton.VISIBLE);
                        }
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Ocurrió un error intentando obtener los permisos del menú", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onItemClick(DevolucionBean dev) {
        try{
            Intent intent = new Intent(getActivity(), DevolucionDetalleActivity.class);
            intent.putExtra(DevolucionDetalleActivity.KEY_PARAM_DEVOLUCION, dev);
            startActivity(intent);
        }catch (Exception e){
            showMessage("onItemClick() > " + e.getMessage());
        }
    }

    @Override
    public void onItemLongClick(DevolucionBean dev) {
        if(dev.getEstadoMovil() == null || dev.getEstadoMovil().equals(getResources().getString(R.string.LOCAL))){
           showMessage("Enviando al servidor...");
            enviarDevolucionAServidor(dev);
        }
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DevolucionActivity.class);
            startActivity(intent);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obtenerDevoluciones();
            mRefresh.setRefreshing(false);
        }
    };

    private void obtenerDevoluciones() {
        try{
            mAdapter.clearAndAddAll(new DevolucionDAO().listar(null));
        }catch (Exception e){
            showMessage("obtenerDevoluciones() > " + e.getMessage());
        }
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void enviarDevolucionAServidor(final DevolucionBean devolucion){

        try{
            if (existsNetworkConnection()) {

                SharedPreferences mSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
                String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
                String sociedad = mSharedPreferences.getString("sociedades", "-1");
                String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

                JSONObject jsonObject = DevolucionBean.transformDevolucionToJSON(devolucion, sociedad);

                if(jsonObject != null){
                    JsonObjectRequest jsonObjectRequest =
                            new JsonObjectRequest(Request.Method.POST, ruta + "returns/addReturn.xsjs", jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try
                                            {
                                                if(response.getString("ResponseStatus").equals("Success")){
                                                    new DevolucionDAO().actualizarSincronizado(devolucion.getClaveMovil());
                                                }else{
                                                    showMessage(response.getJSONObject("Response")
                                                            .getJSONObject("message")
                                                            .getString("value"));
                                                }

                                            }catch (Exception e){
                                                showMessage("Response - " + e.getMessage());
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {showMessage("VolleyError - " + error.getMessage());}
                                    }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    return headers;
                                }
                            };
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
                }else
                    showMessage("Error preparando el objeto para el envio...");
            }
        }catch (Exception e){
            showMessage("enviarDevolucion() > " + e.getMessage());
        }
    }

    private boolean existsNetworkConnection(){
        boolean wifi = Connectivity.isConnectedWifi(getActivity());
        boolean movil = Connectivity.isConnectedMobile(getActivity());
        boolean isConnectionFast = Connectivity.isConnectedFast(getActivity());

        return (wifi || movil) && isConnectionFast;
    }
}
