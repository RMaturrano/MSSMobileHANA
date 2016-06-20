package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.cobranza.ListaCobranzasTabPendientesFragment;
import com.proyecto.cobranza.ListaCobranzasTabTodosFragment;

public class PagerAdapterListCobranzas extends FragmentStatePagerAdapter{
	
	
	int mNumOfTabs;
	 
    public PagerAdapterListCobranzas(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                ListaCobranzasTabTodosFragment tabBasico = new ListaCobranzasTabTodosFragment();
                return tabBasico;
            case 1:
                ListaCobranzasTabPendientesFragment tabPend = new ListaCobranzasTabPendientesFragment();
                return tabPend;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
