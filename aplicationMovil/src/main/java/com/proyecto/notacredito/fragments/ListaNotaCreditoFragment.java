package com.proyecto.notacredito.fragments;

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
import com.proyecto.bean.NotaCreditoBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.dao.NotaCreditoDAO;
import com.proyecto.notacredito.NotaCreditoActivity;
import com.proyecto.notacredito.NotaCreditoDetalleActivity;
import com.proyecto.notacredito.adapter.recyclerview.RVAdapterListaNotaCredito;
import com.proyecto.notacredito.adapter.recyclerview.listeners.IRVAdapterListaNotaCredito;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.Variables;
import com.proyecto.ws.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListaNotaCreditoFragment extends Fragment implements IRVAdapterListaNotaCredito{

    private FloatingActionButton fabAddNotaCredito;
    private RecyclerView mRVListaNotaCredito;
    private RVAdapterListaNotaCredito mAdapter;
    private SwipeRefreshLayout mRefresh;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_notacredito, container, false);

        fabAddNotaCredito = (FloatingActionButton) v.findViewById(R.id.fabAddNotaCredito);
        fabAddNotaCredito.setOnClickListener(fabClickListener);
        fabAddNotaCredito.setVisibility(View.INVISIBLE);

        mRVListaNotaCredito = (RecyclerView) v.findViewById(R.id.rvListaNotaCredito);
        mRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swpListaNotaCredito);
        mRefresh.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mRefresh.setOnRefreshListener(onRefreshListener);

        mRVListaNotaCredito.setLayoutManager(new LinearLayoutManager(v.getContext()));
        mRVListaNotaCredito.setHasFixedSize(true);

        mAdapter = new RVAdapterListaNotaCredito(this);
        mRVListaNotaCredito.setAdapter(mAdapter);

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        obtenerNotasCredito();
        comprobarPermisosMenu();
    }

    private void comprobarPermisosMenu(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String permisosMenu = pref.getString(Variables.MENU_NOTA_CREDITO, null);
        if (permisosMenu != null) {
            try {
                JSONObject permisos = new JSONObject(permisosMenu);
                String movilCrear = permisos.getString(Variables.MOVIL_CREAR);

                if(movilCrear != null && !movilCrear.trim().equals("")){
                    if (!movilCrear.equals("")) {
                        if (movilCrear.equalsIgnoreCase("Y")) {
                            fabAddNotaCredito.setVisibility(FloatingActionButton.VISIBLE);
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
    public void onItemClick(NotaCreditoBean nc) {
        try{
            Intent intent = new Intent(getActivity(), NotaCreditoDetalleActivity.class);
            intent.putExtra(NotaCreditoDetalleActivity.KEY_PARAM_FACT, nc);
            startActivity(intent);
        }catch (Exception e){
            showMessage("onItemClick() > " + e.getMessage());
        }
    }

    @Override
    public void onItemLongClick(NotaCreditoBean nc) {
        if(nc.getEstadoMovil() == null || nc.getEstadoMovil().equals(getResources().getString(R.string.LOCAL))){
            showMessage("Enviando al servidor...");
            enviarNotaCreditoAServidor(nc);
        }
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), NotaCreditoActivity.class);
            startActivity(intent);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            obtenerNotasCredito();
            mRefresh.setRefreshing(false);
        }
    };

    private void obtenerNotasCredito() {
        try{
            mAdapter.clearAndAddAll(new NotaCreditoDAO().listar(null));
        }catch (Exception e){
            showMessage("obtenerNotasCredito() > " + e.getMessage());
        }
    }

    private void showMessage(String message){
        if(message != null)
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void enviarNotaCreditoAServidor(final NotaCreditoBean notacredito){

        try{
            if (existsNetworkConnection()) {

                SharedPreferences mSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String ip = mSharedPreferences.getString("ipServidor", Constantes.DEFAULT_IP);
                String port = mSharedPreferences.getString("puertoServidor", Constantes.DEFAULT_PORT);
                String sociedad = mSharedPreferences.getString("sociedades", "-1");
                String ruta = "http://" + ip + ":" + port + "/MSS_MOBILE/service/";

                JSONObject jsonObject = NotaCreditoBean.transformNotaCreditoToJSON(notacredito, sociedad);

                if(jsonObject != null){
                    JsonObjectRequest jsonObjectRequest =
                            new JsonObjectRequest(Request.Method.POST, ruta + "creditmemo/addCreditMemo.xsjs", jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try
                                            {
                                                if(response.getString("ResponseStatus").equals("Success")){
                                                    new NotaCreditoDAO().actualizarSincronizado(notacredito.getClaveMovil());
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
            showMessage("enviarNotaCreditoAServidor() > " + e.getMessage());
        }
    }

    private boolean existsNetworkConnection(){
        boolean wifi = Connectivity.isConnectedWifi(getActivity());
        boolean movil = Connectivity.isConnectedMobile(getActivity());
        boolean isConnectionFast = Connectivity.isConnectedFast(getActivity());

        return (wifi || movil) && isConnectionFast;
    }

}
