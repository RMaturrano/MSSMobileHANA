package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.cobranza.DetalleFacturaTabCabeceraFragment;
import com.proyecto.cobranza.DetalleFacturaTabContenidoFragment;
import com.proyecto.cobranza.DetalleFacturaTabLogisticaFragment;

public class PagerAdapterDetFactura extends FragmentStatePagerAdapter{

	
	private int mNumOfTabs;
	 
    public PagerAdapterDetFactura(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DetalleFacturaTabCabeceraFragment tabCabe = new DetalleFacturaTabCabeceraFragment();
                return tabCabe;
            case 1:
            	DetalleFacturaTabContenidoFragment tabCont = new DetalleFacturaTabContenidoFragment();
                return tabCont;
            case 2:
            	DetalleFacturaTabLogisticaFragment tabTipoPago = new DetalleFacturaTabLogisticaFragment();
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
