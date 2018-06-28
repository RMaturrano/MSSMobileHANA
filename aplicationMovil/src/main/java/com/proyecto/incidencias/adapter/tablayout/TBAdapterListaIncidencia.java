package com.proyecto.incidencias.adapter.tablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.incidencias.fragments.ListaEntregaFragment;
import com.proyecto.incidencias.fragments.ListaFacturaFragment;
import com.proyecto.incidencias.fragments.ListaOrdenFragment;

public class TBAdapterListaIncidencia  extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public TBAdapterListaIncidencia(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListaOrdenFragment tab1 = new ListaOrdenFragment();
                return tab1;
            case 1:
                ListaEntregaFragment tab2 = new ListaEntregaFragment();
                return tab2;
            case 2:
                ListaFacturaFragment tab3 = new ListaFacturaFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
