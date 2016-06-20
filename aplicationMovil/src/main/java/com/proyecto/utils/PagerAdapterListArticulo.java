package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.inventario.ListaArticulosTabTodosFragment;

public class PagerAdapterListArticulo extends FragmentStatePagerAdapter{
	
	
	private int mNumOfTabs;
	 
    public PagerAdapterListArticulo(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
            	ListaArticulosTabTodosFragment tabBasico = new ListaArticulosTabTodosFragment();
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
