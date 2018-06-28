package com.proyecto.notacredito.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.notacredito.adapter.recyclerview.listeners.IRVAdapterDetalleNotaCredito;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterDetalleNotaCredito extends RecyclerView.Adapter<RVAdapterDetalleNotaCredito.RVAdapterDetalleNotaCreditoViewHolder> {

    private List<ItemDetailModelNC> mList;
    private IRVAdapterDetalleNotaCredito mInterface;

    public RVAdapterDetalleNotaCredito(IRVAdapterDetalleNotaCredito intf){
        mInterface = intf;
        mList = new ArrayList<>();
    }

    public void clearAndAddAll(List<ItemDetailModelNC> lst){
        mList.clear();
        mList.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterDetalleNotaCreditoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterDetalleNotaCredito.RVAdapterDetalleNotaCreditoViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_detalle_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterDetalleNotaCreditoViewHolder holder, int position) {
        ItemDetailModelNC item = mList.get(position);

        holder.tvTitulo.setText(item.getTitle());
        holder.tvContenido.setText(item.getContent());

        if(item.isEditable()){
            holder.imgEditable.setImageResource(R.drawable.ic_keyboard_arrow_right_blue_36dp);
        }else{
            holder.imgEditable.setImageResource(R.drawable.ic_chevron_right_24dp_white);
        }

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class RVAdapterDetalleNotaCreditoViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitulo, tvContenido;
        private ImageView imgEditable;

        public RVAdapterDetalleNotaCreditoViewHolder(View itemView) {
            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.tvDetalleDevTitulo);
            tvContenido = (TextView) itemView.findViewById(R.id.tvDetalleDevContenido);
            imgEditable = (ImageView) itemView.findViewById(R.id.ivDetalleDevEditable);
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ItemDetailModelNC item = mList.get((int)v.getTag());
            if(item.isEditable())
                mInterface.onItemClick(item);
        }
    };


}
