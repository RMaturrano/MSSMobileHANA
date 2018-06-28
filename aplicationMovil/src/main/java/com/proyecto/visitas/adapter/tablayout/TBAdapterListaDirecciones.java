package com.proyecto.visitas.adapter.tablayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.visitas.fragment.ListaVisitaHoyFragment;
import com.proyecto.visitas.fragment.ListaVisitaMananaFragment;

public class TBAdapterListaDirecciones extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    public TBAdapterListaDirecciones(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ListaVisitaHoyFragment tab1 = new ListaVisitaHoyFragment();
                return tab1;
            case 1:
                ListaVisitaMananaFragment tab2 = new ListaVisitaMananaFragment();
                return tab2;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
