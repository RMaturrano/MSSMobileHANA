package com.proyecto.visitas.adapter.recyclerview.listeners;


import com.proyecto.sociosnegocio.util.DireccionBuscarBean;

public interface IRVAdapterListaVisitas {
    void onItemClick(DireccionBuscarBean direccion);
    void onItemLongClick(DireccionBuscarBean direccion);
    void onMapClick(DireccionBuscarBean direccion);
}
