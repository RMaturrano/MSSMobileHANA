package com.proyecto.devoluciones.adapter.recyclerview.listeners;

import com.proyecto.bean.DevolucionBean;

public interface IRVAdapterListaDevolucion {
    void onItemClick(DevolucionBean dev);
    void onItemLongClick(DevolucionBean dev);
}
