package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.sociosnegocio.ListaSocioNegocioTabCliente;
import com.proyecto.sociosnegocio.ListaSocioNegocioTabLead;

public class PagerAdapter extends FragmentStatePagerAdapter{
	
	private int mNumOfTabs;
	 
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                ListaSocioNegocioTabCliente tabCliente = new ListaSocioNegocioTabCliente();
                return tabCliente;
            case 1:
                ListaSocioNegocioTabLead tabLead = new ListaSocioNegocioTabLead();
                return tabLead;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
