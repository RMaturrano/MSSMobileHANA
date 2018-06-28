package com.proyecto.entregas.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaDetalleLoteBean;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterDetalleLotes extends RecyclerView.Adapter<RVAdapterDetalleLotes.RVAdapterDetalleLotesViewHolder>{

    private List<EntregaDetalleLoteBean> mListDetalle;

    public RVAdapterDetalleLotes(){
        mListDetalle = new ArrayList<>();
    }

    @Override
    public RVAdapterDetalleLotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterDetalleLotes.RVAdapterDetalleLotesViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_detalle_lote_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterDetalleLotesViewHolder holder, int position) {
        EntregaDetalleLoteBean lote = mListDetalle.get(position);
        holder.tvCantidad.setText(String.valueOf(lote.getCantidad()));
        holder.tvLote.setText(lote.getLote());
    }

    @Override
    public int getItemCount() {
        return mListDetalle.size();
    }

    public void clearAndAddAll(List<EntregaDetalleLoteBean> detalles){
        mListDetalle.clear();
        mListDetalle.addAll(detalles);
        notifyDataSetChanged();
    }

    class RVAdapterDetalleLotesViewHolder extends RecyclerView.ViewHolder{

        private TextView tvLote, tvCantidad;

        public RVAdapterDetalleLotesViewHolder(View itemView) {
            super(itemView);

            tvLote = (TextView) itemView.findViewById(R.id.tvDetLoteCodigo);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvDetLoteCantidad);
        }
    }

}
