package com.proyecto.incidencias.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.proyect.movil.R;
import com.proyecto.bean.IncidenciaBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.IncidenciaDAO;
import com.proyecto.incidencias.IncidenciaActivity;
import com.proyecto.incidencias.IncidenciaDetalleActivity;
import com.proyecto.incidencias.adapter.recyclerview.RVAdapterListIncidencia;
import com.proyecto.incidencias.adapter.recyclerview.listeners.IRVAdapterListIncidencia;
import com.proyecto.utils.Constantes;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListaEntregaFragment extends Fragment implements IRVAdapterListIncidencia {

    private View mView;
    private RVAdapterListIncidencia mRVAdapterListIncidencia;
    private RecyclerView mRVListaIncidencia;
    private SwipeRefreshLayout mRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_lista_incidencia_tab_entrega, container, false);

        mRVListaIncidencia = (RecyclerView) mView.findViewById(R.id.rvListaIncidenciaEntrega);
        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swpListaIncidenciaEntrega);
        mRefresh.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mRefresh.setOnRefreshListener(onRefreshListener);

        mRVListaIncidencia.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRVListaIncidencia.setHasFixedSize(true);

        mRVAdapterListIncidencia = new RVAdapterListIncidencia(this, IncidenciaActivity.ENTREGA);
        mRVListaIncidencia.setAdapter(mRVAdapterListIncidencia);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        obtenerIncidencias();
    }

    @Override
    public void onItemClick(IncidenciaBean incidencia) {
        Intent intent = new Intent(mView.getContext(), IncidenciaDetalleActivity.class);
        intent.putExtra(IncidenciaDetalleActivity.KEY_PARM_INCIDENCIA, incidencia);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(IncidenciaBean incidencia) {
        if(incidencia.getSincronizado().equals("N")){
            showMessage("Enviando al servidor...");
            enviarIncidenciaAServidor(incidencia);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obtenerIncidencias();
            mRefresh.setRefreshing(false);
        }
    };

    private void obtenerIncidencias(){
        mRVAdapterListIncidencia.clearAndAddAll(new IncidenciaDAO().listar(IncidenciaActivity.ENTREGA));
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void enviarIncidenciaAServidor(final IncidenciaBean incidencia){
        boolean wifi = Connectivity.isConnectedWifi(getActivity().getApplicationContext());
        boolean movil = Connectivity.isConnectedMobile(getActivity().getApplicationContext());
        boolean isConnectionFast = Connectivity.isConnectedFast(getActivity().getApplicationContext());

        if (wifi || movil && isConnectionFast) {

            SharedPreferences mSharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity().getApplicationContext());

            String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
            String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
            String sociedad = mSharedPreferences.getString("sociedades", "-1");
            String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

            JSONObject jsonObject = IncidenciaBean.transformIncidenciaToJSON(incidencia, sociedad);

            if(jsonObject != null){
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(Request.Method.POST, ruta + "activity/addActivity.xsjs", jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try
                                        {
                                            if(response.getString("ResponseStatus").equals("Success")){
                                                new IncidenciaDAO().actualizarSincronizado(incidencia.getClaveMovil());
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
                VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }else
                showMessage("No se pudo construir el objeto de envío...");

        }
    }

}

