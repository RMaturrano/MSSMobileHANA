package com.proyecto.facturas.adapter.recyclerview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;
import com.proyecto.facturas.adapter.recyclerview.listeners.IRVAdapterListFactura;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListFactura extends RecyclerView.Adapter<RVAdapterListFactura.RVAdapterListFacturaViewHolder>
        implements Filterable {

    private List<FacturaBean> mListFactura;
    private List<FacturaBean> mListFacturaFiltro;
    private IRVAdapterListFactura mIRVAdapterListFactura;
    private String mSearchTerm;
    private static String COLOR_SELECTED = "#9e9e9e";
    private static String COLOR_UNSELECTED = "#E0ECF8";

    public RVAdapterListFactura(IRVAdapterListFactura mIRVAdapterListEntrega){
        this.mIRVAdapterListFactura = mIRVAdapterListEntrega;
        mListFactura = new ArrayList<>();
        mListFacturaFiltro = new ArrayList<>();
        mSearchTerm = "";
    }

    public List<FacturaBean> getAllItems(){
        return this.mListFactura;
    }

    public void clearAndAddAll(List<FacturaBean> mListEntrega){
        this.mListFactura.clear();
        this.mListFactura.addAll(mListEntrega);
        this.mListFacturaFiltro.clear();
        this.mListFacturaFiltro.addAll(mListEntrega);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterListFacturaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListFactura.RVAdapterListFacturaViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_entrega_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterListFacturaViewHolder holder, int position) {
        FacturaBean entrega = mListFactura.get(position);

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
        return mListFactura.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListFactura = (ArrayList<FacturaBean>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if(!constraint.toString().equals("")){
                    if(mSearchTerm.length() > constraint.toString().length())
                        mListFactura = mListFacturaFiltro;
                }

                mSearchTerm = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();

                if(mSearchTerm.equals("")){
                    results.count = mListFacturaFiltro.size();
                    results.values = mListFactura;
                    return results;
                }else{

                    List<FacturaBean> FilteredArrayList = new ArrayList<>();
                    for (int i = 0; i < mListFactura.size(); i++) {

                        FacturaBean data = mListFactura.get(i);
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

    class RVAdapterListFacturaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigoCliente, tvNombreCliente, tvFechaContable, tvReferencia;

        public RVAdapterListFacturaViewHolder(View itemView) {
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
            FacturaBean entrega = mListFactura.get((int) v.getTag());
            boolean inSelection = false;
            for (FacturaBean d: mListFactura) {
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
                mIRVAdapterListFactura.onItemClick(entrega);
            }
        }
    };

    private View.OnLongClickListener itemViewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            FacturaBean entrega = mListFactura.get((int) v.getTag());
            entrega.setSelected(!entrega.isSelected());
            v.setBackgroundColor(entrega.isSelected() ? Color.parseColor(COLOR_SELECTED) :
                    Color.parseColor(COLOR_UNSELECTED));
            mIRVAdapterListFactura.onItemLongClick(mListFactura.get((int) v.getTag()));
            return true;
        }
    };

}
