package com.proyecto.notacredito.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.proyect.movil.R;
import com.proyecto.notacredito.adapter.recyclerview.listeners.IRVAdapterAddNotaCredito;

import java.util.ArrayList;
import java.util.List;


public class RVAdapterAddNotaCredito extends RecyclerView.Adapter<RVAdapterAddNotaCredito.RVAdapterAddNotaCreditoViewHolder>{

    private List<ItemAddCreditNote> mList;
    private IRVAdapterAddNotaCredito mIRVListener;

    public RVAdapterAddNotaCredito(IRVAdapterAddNotaCredito listener){
        mList = new ArrayList<>();
        mIRVListener = listener;
    }

    public void addItem(ItemAddCreditNote item){
        if(!updateItem("", item.getTitulo())) {
            mList.add(item);
            notifyItemInserted(mList.size() - 1);
        }
    }

    public String getItem(String title){
        String result = "";
        for (ItemAddCreditNote item: this.mList) {
            if(title.equals(item.getTitulo())) {
                result = item.getContenido();
                break;
            }
        }

        return result;
    }

    public boolean updateItem(String content, String title){
        boolean res = false;
        for (ItemAddCreditNote item: this.mList) {
            if(title.equals(item.getTitulo())) {
                item.setContenido(content);
                notifyDataSetChanged();
                res = true;
                break;
            }
        }

        return res;
    }

    public void clearAndAddAll(List<ItemAddCreditNote> lstItems) {
        mList.clear();
        mList.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterAddNotaCredito.RVAdapterAddNotaCreditoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterAddNotaCredito.RVAdapterAddNotaCreditoViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_add_credit_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterAddNotaCredito.RVAdapterAddNotaCreditoViewHolder holder, int position) {
        ItemAddCreditNote item = mList.get(position);

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

    static class RVAdapterAddNotaCreditoViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAddDevolucionTitulo, tvAddDevolucionContenido;
        private ImageView ivAddDevolucionPuntero;

        public RVAdapterAddNotaCreditoViewHolder(View itemView) {
            super(itemView);

            tvAddDevolucionTitulo = (TextView) itemView.findViewById(R.id.tvAddNotTitulo);
            tvAddDevolucionContenido = (TextView) itemView.findViewById(R.id.tvAddNotContenido);
            ivAddDevolucionPuntero = (ImageView) itemView.findViewById(R.id.ivAddNotPuntero);
        }
    }

}