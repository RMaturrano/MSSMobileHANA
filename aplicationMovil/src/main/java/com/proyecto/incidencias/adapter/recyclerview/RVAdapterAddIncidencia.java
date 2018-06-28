package com.proyecto.incidencias.adapter.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.incidencias.adapter.recyclerview.listeners.IRVAdapterAddIncidenciaListener;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterAddIncidencia extends RecyclerView.Adapter<RVAdapterAddIncidencia.RVAdapterAddIncidenciaViewHolder>{

    private List<ItemAddIncidencia> mItems;
    private IRVAdapterAddIncidenciaListener mIrvAdapterAddIncidenciaListener;

    public  RVAdapterAddIncidencia(IRVAdapterAddIncidenciaListener adapter){
        mItems = new ArrayList<>();
        mIrvAdapterAddIncidenciaListener = adapter;
    }

    public ItemAddIncidencia findItem(String title){
        ItemAddIncidencia itemFound = new ItemAddIncidencia();

        for (ItemAddIncidencia item: mItems) {
            if(title.equals(item.getTitulo())){
                itemFound = item;
                break;
            }
        }
        return itemFound;
    }

    public void updateItem(ItemAddIncidencia itemFor){
        for (ItemAddIncidencia item: mItems) {
            if(itemFor.getTitulo().equals(item.getTitulo())) {
                item.setContenido(itemFor.getContenido());
                notifyDataSetChanged();
            }
        }
    }

    public void add(ItemAddIncidencia cliente) {
        mItems.add(cliente);
        notifyItemInserted(mItems.size() - 1);
    }

    public void clearAndAddAll(List<ItemAddIncidencia> lstItems) {
        mItems.clear();
        mItems.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterAddIncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterAddIncidenciaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_add_incidencia_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterAddIncidenciaViewHolder holder, int position) {
        ItemAddIncidencia item = mItems.get(position);

        holder.tvAddIncidenciaTitulo.setText(item.getTitulo());
        holder.tvAddIncidenciaContenido.setText(item.getContenido());

        if(item.isEditable()){
            holder.ivAddIncidenciaPuntero.setImageResource(R.drawable.ic_chevron_right_24dp);
        }else{
            holder.ivAddIncidenciaPuntero.setImageResource(R.drawable.ic_chevron_right_24dp_white);
        }

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mIrvAdapterAddIncidenciaListener.onItemClick(mItems.get((Integer) v.getTag()));
        }
    };

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class RVAdapterAddIncidenciaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAddIncidenciaTitulo, tvAddIncidenciaContenido;
        private ImageView ivAddIncidenciaPuntero;

        public RVAdapterAddIncidenciaViewHolder(View itemView) {
            super(itemView);

            tvAddIncidenciaTitulo = (TextView) itemView.findViewById(R.id.tvAddIncTitulo);
            tvAddIncidenciaContenido = (TextView) itemView.findViewById(R.id.tvAddIncContenido);
            ivAddIncidenciaPuntero = (ImageView) itemView.findViewById(R.id.ivAddIncPuntero);
        }
    }

}
