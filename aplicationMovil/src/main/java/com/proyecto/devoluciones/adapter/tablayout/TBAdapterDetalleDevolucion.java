package com.proyecto.devoluciones.adapter.tablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.devoluciones.fragments.DetalleTabContenidoFragment;
import com.proyecto.devoluciones.fragments.DetalleTabFinanzasFragment;
import com.proyecto.devoluciones.fragments.DetalleTabGeneralFragment;
import com.proyecto.devoluciones.fragments.DetalleTabLogisticaFragment;

public class TBAdapterDetalleDevolucion  extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public TBAdapterDetalleDevolucion(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DetalleTabGeneralFragment tab1 = new DetalleTabGeneralFragment();
                return tab1;
            case 1:
                DetalleTabContenidoFragment tab2 = new DetalleTabContenidoFragment();
                return tab2;
            case 2:
                DetalleTabLogisticaFragment tab3 = new DetalleTabLogisticaFragment();
                return tab3;
            case 3:
                DetalleTabFinanzasFragment tab4 = new DetalleTabFinanzasFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
