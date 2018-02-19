package com.proyecto.sociosnegocio;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyect.movil.R;
import com.proyecto.utils.PagerAdapterDetalleBP;

public class DetalleSocioNegocioMainFTabs extends Fragment{
	
	private View v;
	private PagerAdapterDetalleBP adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          v  =  inflater.inflate(R.layout.socio_negocio_detalle_fragment, container, false);
      	    
              TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
              tabLayout.addTab(tabLayout.newTab().setText("Información Básica"));
              tabLayout.addTab(tabLayout.newTab().setText("Datos generales"));
              tabLayout.addTab(tabLayout.newTab().setText("Condiciones de pago"));
              tabLayout.addTab(tabLayout.newTab().setText("Contactos"));
              tabLayout.addTab(tabLayout.newTab().setText("Direcciones"));
              tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       
              final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
              adapter = new PagerAdapterDetalleBP(getFragmentManager(), tabLayout.getTabCount());
              viewPager.setAdapter(adapter);
              viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
              tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                  @Override
                  public void onTabSelected(TabLayout.Tab tab) {
                      viewPager.setCurrentItem(tab.getPosition());
                  }
       
                  @Override
                  public void onTabUnselected(TabLayout.Tab tab) {
       
                  }
       
                  @Override
                  public void onTabReselected(TabLayout.Tab tab) {
       
                  }
              });
              
           return v;
        
    }

    public void notificarCambioUbicacion(String latitud, String longitud){
        DetalleSocioNegocioTabDirecciones tabDirecciones =
                (DetalleSocioNegocioTabDirecciones) adapter.getRegisteredFragment(4);
        tabDirecciones.updateLatLon(latitud, longitud);
    }

}
