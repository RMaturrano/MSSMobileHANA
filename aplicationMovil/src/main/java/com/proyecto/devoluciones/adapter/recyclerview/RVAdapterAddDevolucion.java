package com.proyecto.devoluciones.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterAddDevolucion;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterAddDevolucion extends RecyclerView.Adapter<RVAdapterAddDevolucion.RVAdapterAddDevolucionViewHolder>{

    private List<ItemAddDevolucion> mList;
    private IRVAdapterAddDevolucion mIRVListener;

    public RVAdapterAddDevolucion(IRVAdapterAddDevolucion listener){
        mList = new ArrayList<>();
        mIRVListener = listener;
    }

    public void addItem(ItemAddDevolucion item){
        if(!updateItem("", item.getTitulo())) {
            mList.add(item);
            notifyItemInserted(mList.size() - 1);
        }
    }

    public String getItem(String title){
        String result = "";
        for (ItemAddDevolucion item: this.mList) {
            if(title.equals(item.getTitulo())) {
                result = item.getContenido();
                break;
            }
        }

        return result;
    }

    public boolean updateItem(String content, String title){
        boolean res = false;
        for (ItemAddDevolucion item: this.mList) {
            if(title.equals(item.getTitulo())) {
                item.setContenido(content);
                notifyDataSetChanged();
                res = true;
                break;
            }
        }

        return res;
    }

    public void clearAndAddAll(List<ItemAddDevolucion> lstItems) {
        mList.clear();
        mList.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterAddDevolucion.RVAdapterAddDevolucionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterAddDevolucion.RVAdapterAddDevolucionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_add_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterAddDevolucion.RVAdapterAddDevolucionViewHolder holder, int position) {
        ItemAddDevolucion item = mList.get(position);

        holder.tvAddDevolucionTitulo.setText(item.getTitulo());
        holder.tvAddDevolucionContenido.setText(item.getContenido());

        if(item.isEditable()){
            holder.ivAddDevolucionPuntero.setImageResource(R.drawable.ic_chevron_right_24dp);
        }else{
            holder.ivAddDevolucionPuntero.setImageResource(R.drawable.ic_chevron_right_24dp_white);
        }

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mIRVListener.onItemClick(mList.get((int) v.getTag()));
        }
    };

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class RVAdapterAddDevolucionViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAddDevolucionTitulo, tvAddDevolucionContenido;
        private ImageView ivAddDevolucionPuntero;

        public RVAdapterAddDevolucionViewHolder(View itemView) {
            super(itemView);

            tvAddDevolucionTitulo = (TextView) itemView.findViewById(R.id.tvAddDevTitulo);
            tvAddDevolucionContenido = (TextView) itemView.findViewById(R.id.tvAddDevContenido);
            ivAddDevolucionPuntero = (ImageView) itemView.findViewById(R.id.ivAddDevPuntero);
        }
    }

}
