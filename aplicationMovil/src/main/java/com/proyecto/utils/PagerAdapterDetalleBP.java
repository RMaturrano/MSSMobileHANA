package com.proyecto.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.proyecto.sociosnegocio.DetalleSocioNegocioTabCondPago;
import com.proyecto.sociosnegocio.DetalleSocioNegocioTabDirecciones;
import com.proyecto.sociosnegocio.DetalleSocioNegocioTabGeneral;
import com.proyecto.sociosnegocio.DetalleSocioNegocioTabPrin;
import com.proyecto.sociosnegocio.DetalleSocioNegocioTabContactos;

public class PagerAdapterDetalleBP extends FragmentStatePagerAdapter{
	
	int mNumOfTabs;
	 
    public PagerAdapterDetalleBP(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DetalleSocioNegocioTabPrin tabBasico = new DetalleSocioNegocioTabPrin();
                return tabBasico;
            case 1:
                DetalleSocioNegocioTabGeneral tabGen = new DetalleSocioNegocioTabGeneral();
                return tabGen;
            case 2:
                DetalleSocioNegocioTabCondPago tabCondPago = new DetalleSocioNegocioTabCondPago();
                return tabCondPago;
            case 3:
                DetalleSocioNegocioTabContactos tabSec1 = new DetalleSocioNegocioTabContactos();
                return tabSec1;
            case 4:
                DetalleSocioNegocioTabDirecciones tabDirec = new DetalleSocioNegocioTabDirecciones();
                return tabDirec;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
