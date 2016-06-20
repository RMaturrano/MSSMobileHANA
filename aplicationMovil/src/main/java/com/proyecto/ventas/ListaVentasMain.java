package com.proyecto.ventas;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyect.movil.R;
import com.proyecto.utils.PagerAdapterListOrd;

public class ListaVentasMain extends Fragment {
	
	private TabLayout tabLayout;
	
	public static ListaVentasMain newInstance() {
		ListaVentasMain fragment = new ListaVentasMain();
		return fragment;
	}

	public ListaVentasMain() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.lista_pedido_cliente, viewGroup,
				false);

		tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
		tabLayout.addTab(tabLayout.newTab().setText("Aprobados"));
		tabLayout.addTab(tabLayout.newTab().setText("Pendientes"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
		final PagerAdapterListOrd adapter = new PagerAdapterListOrd(
				getFragmentManager(), tabLayout.getTabCount());
		viewPager.setAdapter(adapter);
		viewPager
				.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
						tabLayout));
		tabLayout
				.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
	
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		
		if(tabLayout != null){
			tabLayout.addTab(tabLayout.newTab().setText("Aprobados"));
			tabLayout.addTab(tabLayout.newTab().setText("Pendientes"));
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		}
		
	}
	

}
