package com.proyecto.facturas.adapter.recyclerview.listeners;

import com.proyecto.bean.FacturaBean;

public interface IRVAdapterListFactura {
    void onItemClick(FacturaBean entrega);
    void onItemLongClick(FacturaBean entrega);
}
