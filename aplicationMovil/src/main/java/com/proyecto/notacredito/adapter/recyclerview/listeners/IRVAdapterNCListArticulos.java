package com.proyecto.notacredito.adapter.recyclerview.listeners;

import com.proyecto.bean.FacturaDetalleBean;

public interface IRVAdapterNCListArticulos {
    void onItemClick(FacturaDetalleBean detalle);
    void onVerLotesClick(FacturaDetalleBean detalle, int pos);
}
