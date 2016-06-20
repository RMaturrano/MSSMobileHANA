package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.inventario.DetalleArticuloTabCantidadFragment;
import com.proyecto.inventario.DetalleArticuloTabGeneralFragment;
import com.proyecto.inventario.DetalleArticuloTabPreciosFragment;

public class PagerAdapterDetArticulo extends FragmentStatePagerAdapter{

	
	private int mNumOfTabs;
	 
    public PagerAdapterDetArticulo(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DetalleArticuloTabGeneralFragment tabCabe = new DetalleArticuloTabGeneralFragment();
                return tabCabe;
            case 1:
            	DetalleArticuloTabCantidadFragment tabCont = new DetalleArticuloTabCantidadFragment();
                return tabCont;
            case 2:
            	DetalleArticuloTabPreciosFragment tabPre = new DetalleArticuloTabPreciosFragment();
                return tabPre;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
	
}
