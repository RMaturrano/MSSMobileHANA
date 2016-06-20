package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.ventas.ListaVentasTabPendientesFragment;
import com.proyecto.ventas.ListaVentasTabTodosFragment;

public class PagerAdapterListOrd extends FragmentStatePagerAdapter{
	
	
	private int mNumOfTabs;
	 
    public PagerAdapterListOrd(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                ListaVentasTabTodosFragment tabBasico = new ListaVentasTabTodosFragment();
                return tabBasico;
            case 1:
                ListaVentasTabPendientesFragment tabSec1 = new ListaVentasTabPendientesFragment();
                return tabSec1;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    
}
