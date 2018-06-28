package com.proyecto.devoluciones.adapter.recyclerview.listeners;

import com.proyecto.bean.EntregaDetalleBean;

public interface IRVAdapterListArticulos {
    void onItemClick(EntregaDetalleBean detalle);
    void onVerLotesClick(EntregaDetalleBean detalle, int pos);
}
