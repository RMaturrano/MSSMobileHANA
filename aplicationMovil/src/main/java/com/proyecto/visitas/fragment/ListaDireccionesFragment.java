package com.proyecto.visitas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.proyect.movil.R;
import com.proyecto.visitas.adapter.tablayout.TBAdapterListaDirecciones;

public class ListaDireccionesFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_direcciones_visita, container, false);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabListaDireccionVisita);
        tabLayout.addTab(tabLayout.newTab().setText("Hoy"));
        //tabLayout.addTab(tabLayout.newTab().setText("Mañana"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pgrListaDireccionVisita);
        final TBAdapterListaDirecciones adapter = new TBAdapterListaDirecciones
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

        return v;
    }
}
