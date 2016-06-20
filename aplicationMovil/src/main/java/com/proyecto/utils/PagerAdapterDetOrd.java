package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.ventas.DetalleVentaTabCabeceraFragment;
import com.proyecto.ventas.DetalleVentaTabContenidoFragment;
import com.proyecto.ventas.DetalleVentaTabFinanzasFragment;
import com.proyecto.ventas.DetalleVentaTabLogisticaFragment;

public class PagerAdapterDetOrd extends FragmentStatePagerAdapter{

	
	int mNumOfTabs;
	 
    public PagerAdapterDetOrd(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DetalleVentaTabCabeceraFragment tabCabe = new DetalleVentaTabCabeceraFragment();
                return tabCabe;
            case 1:
            	DetalleVentaTabContenidoFragment tabCont = new DetalleVentaTabContenidoFragment();
                return tabCont;
            case 2:
            	DetalleVentaTabLogisticaFragment tabLog = new DetalleVentaTabLogisticaFragment();
                return tabLog;
            case 3:
            	DetalleVentaTabFinanzasFragment tabFin = new DetalleVentaTabFinanzasFragment();
                return tabFin;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
	
}
