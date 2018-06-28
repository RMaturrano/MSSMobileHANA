package com.proyecto.entregas.adapter.tablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.bean.EntregaBean;
import com.proyecto.entregas.fragments.EntregaDetalleTabCabecera;
import com.proyecto.entregas.fragments.EntregaDetalleTabContenido;
import com.proyecto.entregas.fragments.EntregaDetalleTabLogistica;

public class TBAdapterEntregaDetalle extends FragmentStatePagerAdapter{

    private int mNumOfTabs;

    public TBAdapterEntregaDetalle(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EntregaDetalleTabCabecera tab1 = new EntregaDetalleTabCabecera();
                return tab1;
            case 1:
                EntregaDetalleTabContenido tab2 = new EntregaDetalleTabContenido();
                return tab2;
            case 2:
                EntregaDetalleTabLogistica tab3 = new EntregaDetalleTabLogistica();
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
