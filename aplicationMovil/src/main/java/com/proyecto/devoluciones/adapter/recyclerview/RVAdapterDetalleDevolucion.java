package com.proyecto.devoluciones.adapter.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.devoluciones.ItemDetailModel;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterDetalleDevolucion;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterDetalleDevolucion  extends RecyclerView.Adapter<RVAdapterDetalleDevolucion.RVAdapterDetalleDevolucionViewHolder> {

    private List<ItemDetailModel> mList;
    private IRVAdapterDetalleDevolucion mInterface;

    public RVAdapterDetalleDevolucion(IRVAdapterDetalleDevolucion intf){
        mInterface = intf;
        mList = new ArrayList<>();
    }

    public void clearAndAddAll(List<ItemDetailModel> lst){
        mList.clear();
        mList.addAll(lst);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterDetalleDevolucionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterDetalleDevolucion.RVAdapterDetalleDevolucionViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_detalle_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterDetalleDevolucionViewHolder holder, int position) {
        ItemDetailModel item = mList.get(position);

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


    static class RVAdapterDetalleDevolucionViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitulo, tvContenido;
        private ImageView imgEditable;

        public RVAdapterDetalleDevolucionViewHolder(View itemView) {
            super(itemView);

            tvTitulo = (TextView) itemView.findViewById(R.id.tvDetalleDevTitulo);
            tvContenido = (TextView) itemView.findViewById(R.id.tvDetalleDevContenido);
            imgEditable = (ImageView) itemView.findViewById(R.id.ivDetalleDevEditable);
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ItemDetailModel item = mList.get((int)v.getTag());
            if(item.isEditable())
                mInterface.onItemClick(item);
        }
    };


}
