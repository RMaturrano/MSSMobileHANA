package com.proyecto.entregas.adapter.recyclerview;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.entregas.adapter.recyclerview.listeners.IRVAdapterListEntrega;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListEntrega extends RecyclerView.Adapter<RVAdapterListEntrega.RVAdapterListEntregaViewHolder>
                                  implements Filterable{

    private List<EntregaBean> mListEntrega;
    private List<EntregaBean> mListEntregaFiltro;
    private IRVAdapterListEntrega mIRVAdapterListEntrega;
    private String mSearchTerm;
    private static String COLOR_SELECTED = "#9e9e9e";
    private static String COLOR_UNSELECTED = "#E0ECF8";

    public RVAdapterListEntrega(IRVAdapterListEntrega mIRVAdapterListEntrega){
        this.mIRVAdapterListEntrega = mIRVAdapterListEntrega;
        mListEntrega = new ArrayList<>();
        mListEntregaFiltro = new ArrayList<>();
        mSearchTerm = "";
    }

    public List<EntregaBean> getAllItems(){
        return this.mListEntrega;
    }

    public void clearAndAddAll(List<EntregaBean> mListEntrega){
        this.mListEntrega.clear();
        this.mListEntrega.addAll(mListEntrega);
        this.mListEntregaFiltro.clear();
        this.mListEntregaFiltro.addAll(mListEntrega);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterListEntregaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListEntrega.RVAdapterListEntregaViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_entrega_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterListEntregaViewHolder holder, int position) {
        EntregaBean entrega = mListEntrega.get(position);

        holder.tvFechaContable.setText(StringDateCast.castStringtoDate(entrega.getFechaContable()));
        holder.tvReferencia.setText(entrega.getReferencia());
        holder.tvCodigoCliente.setText(entrega.getSocioNegocio());
        holder.tvNombreCliente.setText(entrega.getSocioNegocioNombre());

        holder.itemView.setBackgroundColor(entrega.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                Color.parseColor(COLOR_UNSELECTED));
        holder.itemView.setOnLongClickListener(itemViewOnLongClickListener);
        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mListEntrega.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListEntrega = (ArrayList<EntregaBean>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if(!constraint.toString().equals("")){
                    if(mSearchTerm.length() > constraint.toString().length())
                        mListEntrega = mListEntregaFiltro;
                }

                mSearchTerm = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();

                if(mSearchTerm.equals("")){
                    results.count = mListEntregaFiltro.size();
                    results.values = mListEntrega;
                    return results;
                }else{

                    List<EntregaBean> FilteredArrayList = new ArrayList<>();
                    for (int i = 0; i < mListEntrega.size(); i++) {

                        EntregaBean data = mListEntrega.get(i);
                        if (data.getSocioNegocio().toLowerCase().contains(mSearchTerm.toString())
                                || data.getSocioNegocioNombre().toLowerCase().contains(mSearchTerm.toString())
                                || data.getReferencia().toLowerCase().contains(mSearchTerm.toString()))  {
                            FilteredArrayList.add(data);
                        }
                    }

                    results.count = FilteredArrayList.size();
                    results.values = FilteredArrayList;

                    return results;
                }
            }
        };

        return filter;
    }

    class RVAdapterListEntregaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigoCliente, tvNombreCliente, tvFechaContable, tvReferencia;

        public RVAdapterListEntregaViewHolder(View itemView) {
            super(itemView);

            tvCodigoCliente = (TextView) itemView.findViewById(R.id.tvListarEntregaCodigoCli);
            tvNombreCliente = (TextView) itemView.findViewById(R.id.tvListarEntregaNombre);
            tvFechaContable = (TextView) itemView.findViewById(R.id.tvListarEntregaFecha);
            tvReferencia = (TextView) itemView.findViewById(R.id.tvListarEntregaReferencia);
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaBean entrega = mListEntrega.get((int) v.getTag());
            boolean inSelection = false;
            for (EntregaBean d: mListEntrega) {
                if(d.isSelected()) {
                    inSelection = true;
                    break;
                }
            }

            if(inSelection){
                entrega.setSelected(!entrega.isSelected());
                v.setBackgroundColor(entrega.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                        Color.parseColor(COLOR_UNSELECTED));
            }else {
                mIRVAdapterListEntrega.onItemClick(entrega);
            }
        }
    };

    private View.OnLongClickListener itemViewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            EntregaBean entrega = mListEntrega.get((int) v.getTag());
            entrega.setSelected(!entrega.isSelected());
            v.setBackgroundColor(entrega.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                    Color.parseColor(COLOR_UNSELECTED));
            mIRVAdapterListEntrega.onItemLongClick(mListEntrega.get((int) v.getTag()));
            return true;
        }
    };

}
