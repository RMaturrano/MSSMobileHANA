package com.proyecto.incidencias.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.incidencias.IncidenciaActivity;
import com.proyecto.incidencias.adapter.tablayout.TBAdapterListaIncidencia;
import com.proyecto.utils.PagerAdapter;
import com.proyecto.utils.Variables;

import org.json.JSONObject;

public class ListaIncidenciaFragment extends Fragment{

    public static int REQUEST_CODE_CREAR_INCIDENCIA = 10;

    private FloatingActionButton fabAddActions;
    private FloatingActionButton fabAddOrdenVenta;
    private FloatingActionButton fabAddEntrega;
    private FloatingActionButton fabAddFactura;
    private LinearLayout lytFabAddOrdenVenta;
    private LinearLayout lytFabAddEntrega;
    private LinearLayout lytFabAddFactura;
    private boolean mFabExpanded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_incidencia, container, false);

        fabAddActions = (FloatingActionButton) v.findViewById(R.id.fabAddIncidencia);
        fabAddOrdenVenta = (FloatingActionButton) v.findViewById(R.id.fabAddIncidencia1);
        fabAddEntrega = (FloatingActionButton) v.findViewById(R.id.fabAddIncidencia2);
        fabAddFactura = (FloatingActionButton) v.findViewById(R.id.fabAddIncidencia3);
        lytFabAddOrdenVenta = (LinearLayout) v.findViewById(R.id.lytFabAddIncidencia1);
        lytFabAddEntrega = (LinearLayout) v.findViewById(R.id.lytFabAddIncidencia2);
        lytFabAddFactura = (LinearLayout) v.findViewById(R.id.lytFabAddIncidencia3);

        fabAddActions.setOnClickListener(onClickListenerFabActions);
        fabAddActions.setVisibility(View.INVISIBLE);
        fabAddOrdenVenta.setOnClickListener(onClickListenerFabAddOrden);
        fabAddEntrega.setOnClickListener(onClickListenerFabAddEntrega);
        fabAddFactura.setOnClickListener(onClickListenerFabAddFactura);
        closeSubMenusFab();

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabListaIncidencia);
        tabLayout.addTab(tabLayout.newTab().setText("Orden de venta"));
        tabLayout.addTab(tabLayout.newTab().setText("Entrega"));
        tabLayout.addTab(tabLayout.newTab().setText("Factura"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pgrListaIncidencia);
        final TBAdapterListaIncidencia adapter = new TBAdapterListaIncidencia
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String permisosMenu = pref.getString(Variables.MENU_INCIDENCIAS, null);
        if (permisosMenu != null) {
            try {
                JSONObject permisos = new JSONObject(permisosMenu);
                String movilCrear = permisos.getString(Variables.MOVIL_CREAR);

                if(movilCrear != null && !movilCrear.trim().equals("")){
                    if (!movilCrear.equals("")) {
                        if (movilCrear.equalsIgnoreCase("Y")) {
                            fabAddActions.setVisibility(FloatingActionButton.VISIBLE);
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

    private View.OnClickListener onClickListenerFabActions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFabExpanded == true){
                closeSubMenusFab();
            } else {
                openSubMenusFab();
            }
        }
    };

    private View.OnClickListener onClickListenerFabAddOrden = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeSubMenusFab();
            Intent intentOrden = new Intent(getActivity().getApplicationContext(), IncidenciaActivity.class);
            intentOrden.putExtra(IncidenciaActivity.KEY_PAR_ORIGEN, IncidenciaActivity.ORDEN);
            getActivity().startActivityForResult(intentOrden, REQUEST_CODE_CREAR_INCIDENCIA);
        }
    };

    private View.OnClickListener onClickListenerFabAddEntrega = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeSubMenusFab();
            Intent intentEntrega = new Intent(getActivity().getApplicationContext(), IncidenciaActivity.class);
            intentEntrega.putExtra(IncidenciaActivity.KEY_PAR_ORIGEN, IncidenciaActivity.ENTREGA);
            getActivity().startActivityForResult(intentEntrega, REQUEST_CODE_CREAR_INCIDENCIA);
        }
    };

    private View.OnClickListener onClickListenerFabAddFactura = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeSubMenusFab();
            Intent intentFactura = new Intent(getActivity().getApplicationContext(), IncidenciaActivity.class);
            intentFactura.putExtra(IncidenciaActivity.KEY_PAR_ORIGEN, IncidenciaActivity.FACTURA);
            getActivity().startActivityForResult(intentFactura, REQUEST_CODE_CREAR_INCIDENCIA);
        }
    };

    private void closeSubMenusFab(){
        lytFabAddOrdenVenta.setVisibility(View.INVISIBLE);
        lytFabAddEntrega.setVisibility(View.INVISIBLE);
        lytFabAddFactura.setVisibility(View.INVISIBLE);

        fabAddActions.setImageResource(R.drawable.ic_add_white_36dp);
        mFabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        lytFabAddOrdenVenta.setVisibility(View.VISIBLE);
        lytFabAddEntrega.setVisibility(View.VISIBLE);
        lytFabAddFactura.setVisibility(View.VISIBLE);

        //Change settings icon to 'X' icon
        fabAddActions.setImageResource(R.drawable.ic_close_24dp);
        mFabExpanded = true;
    }

}
