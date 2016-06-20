package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.facturas.ListaFacturasTabPendientesFragment;
import com.proyecto.ventas.ListaVentasTabPendientesFragment;
import com.proyecto.ventas.ListaVentasTabTodosFragment;

public class PagerAdapterListFactura extends FragmentStatePagerAdapter{
	
	
	private int mNumOfTabs;
	 
    public PagerAdapterListFactura(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                ListaFacturasTabPendientesFragment tabBasico = new ListaFacturasTabPendientesFragment();
                return tabBasico;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    
}
