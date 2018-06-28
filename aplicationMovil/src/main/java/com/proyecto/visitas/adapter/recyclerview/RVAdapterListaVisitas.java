package com.proyecto.visitas.adapter.recyclerview;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.sociosnegocio.util.DireccionBuscarBean;
import com.proyecto.visitas.adapter.recyclerview.listeners.IRVAdapterListaVisitas;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListaVisitas extends RecyclerView.Adapter<RVAdapterListaVisitas.RVAdapterListaVisitasViewHolder>{

    private List<DireccionBuscarBean> mList;
    private IRVAdapterListaVisitas mIRVAdapterListaVisitas;
    private static String COLOR_SELECTED = "#9e9e9e";
    private static String COLOR_UNSELECTED = "#E0ECF8";

    public RVAdapterListaVisitas(IRVAdapterListaVisitas mIRVAdapterListaVisita){
        this.mIRVAdapterListaVisitas = mIRVAdapterListaVisita;
        mList = new ArrayList<>();
    }

    public List<DireccionBuscarBean> getAllItems(){
        return mList;
    }

    public void clearAndAddAll(List<DireccionBuscarBean> direcciones){
        mList.clear();
        mList.addAll(direcciones);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterListaVisitasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListaVisitas.RVAdapterListaVisitasViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_direccion_visita_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterListaVisitasViewHolder holder, int position) {
        DireccionBuscarBean direccion = mList.get(position);
        holder.tvCodCliente.setText(direccion.getCodigoCliente());
        holder.tvNomCliente.setText(direccion.getNombreCliente());
        holder.tvIdDireccion.setText(direccion.getCodigo());
        holder.tvDetDireccion.setText(direccion.getDepartamentoNombre() + "-" +
                            direccion.getProvinciaNombre() + "-" +
                            direccion.getDistritoNombre());

        holder.imgGoMap.setOnClickListener(mapClickListener);
        holder.imgGoMap.setTag(position);

        holder.itemView.setBackgroundColor(direccion.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                Color.parseColor(COLOR_UNSELECTED));
        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setOnLongClickListener(onLongClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class RVAdapterListaVisitasViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodCliente, tvNomCliente, tvIdDireccion, tvDetDireccion;
        private ImageView imgGoMap;

        public RVAdapterListaVisitasViewHolder(View itemView) {
            super(itemView);

            tvCodCliente = (TextView) itemView.findViewById(R.id.tvDirVisCodigoCliente);
            tvNomCliente = (TextView) itemView.findViewById(R.id.tvDirVisNombreCliente);
            tvIdDireccion = (TextView) itemView.findViewById(R.id.tvDirVisCodigoDireccion);
            tvDetDireccion = (TextView) itemView.findViewById(R.id.tvDirVisDetalleDireccion);
            imgGoMap = (ImageView) itemView.findViewById(R.id.imgDirVisGoMap);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DireccionBuscarBean direccion = mList.get((int) v.getTag());

            boolean inSelection = false;

            for (DireccionBuscarBean d: mList) {
                if(d.isSelected()) {
                    inSelection = true;
                    break;
                }
            }

            if(inSelection){
                direccion.setSelected(!direccion.isSelected());
                v.setBackgroundColor(direccion.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                        Color.parseColor(COLOR_UNSELECTED));
            }else {
                mIRVAdapterListaVisitas.onItemClick(direccion);
            }
        }
    };

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            DireccionBuscarBean direccion = mList.get((int) v.getTag());
            direccion.setSelected(!direccion.isSelected());
            v.setBackgroundColor(direccion.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                    Color.parseColor(COLOR_UNSELECTED));
            mIRVAdapterListaVisitas.onItemLongClick(mList.get((int) v.getTag()));
            return true;
        }
    };

    View.OnClickListener mapClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mIRVAdapterListaVisitas.onMapClick(mList.get((int) v.getTag()));
        }
    };

}
