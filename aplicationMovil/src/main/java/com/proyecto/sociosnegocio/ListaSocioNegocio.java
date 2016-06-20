package com.proyecto.sociosnegocio;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyect.movil.R;
import com.proyecto.utils.PagerAdapter;

public class ListaSocioNegocio extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.lista_socio_negocio, viewGroup, false);
	    
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Clientes"));
        tabLayout.addTab(tabLayout.newTab().setText("Lead"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
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


}
