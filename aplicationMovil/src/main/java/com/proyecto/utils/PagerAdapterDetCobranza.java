package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.cobranza.DetalleCobranzaTabCabeceraFragment;
import com.proyecto.cobranza.DetalleCobranzaTabContenidoFragment;
import com.proyecto.cobranza.DetalleCobranzaTabTipoPagoFragment;

public class PagerAdapterDetCobranza extends FragmentStatePagerAdapter{

	
	int mNumOfTabs;
	 
    public PagerAdapterDetCobranza(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DetalleCobranzaTabCabeceraFragment tabCabe = new DetalleCobranzaTabCabeceraFragment();
                return tabCabe;
            case 1:
            	DetalleCobranzaTabContenidoFragment tabCont = new DetalleCobranzaTabContenidoFragment();
                return tabCont;
            case 2:
            	DetalleCobranzaTabTipoPagoFragment tabTipoPago = new DetalleCobranzaTabTipoPagoFragment();
                return tabTipoPago;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
	
}
