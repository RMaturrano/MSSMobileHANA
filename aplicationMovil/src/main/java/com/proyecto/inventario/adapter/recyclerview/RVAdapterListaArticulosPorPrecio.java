package com.proyecto.inventario.adapter.recyclerview;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.inventario.adapter.recyclerview.listeners.IRVAdapterListaArticulosPorPrecio;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListaArticulosPorPrecio extends RecyclerView.Adapter<RVAdapterListaArticulosPorPrecio.RVAdapterListaArticulosPorPrecioViewHolder>
        implements Filterable {

    private IRVAdapterListaArticulosPorPrecio mInterface;
    private List<ArticuloBean> mList;
    private List<ArticuloBean> mListFilter;
    private String mSearchTerm;

    public RVAdapterListaArticulosPorPrecio(IRVAdapterListaArticulosPorPrecio mInterface){
        this.mInterface = mInterface;
        mList = new ArrayList<>();
        mListFilter = new ArrayList<>();
        mSearchTerm ="";
    }

    public void clearAndAddAll(List<ArticuloBean> lstItems) {
        mList.clear();
        mList.addAll(lstItems);
        mListFilter.clear();
        mListFilter.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterListaArticulosPorPrecioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListaArticulosPorPrecio
                .RVAdapterListaArticulosPorPrecioViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.rv_lista_articulos_por_precio_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterListaArticulosPorPrecioViewHolder holder, int position) {
        ArticuloBean articulo = mList.get(position);

        holder.tvCodigo.setText(articulo.getCod());
        holder.tvNombre.setText(articulo.getDesc());
        holder.tvGrupo.setText(articulo.getNombreGrupoArt());
        holder.tvPrecio.setText(String.valueOf(articulo.getPre()));

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mInterface.onItemClick(mList.get((int) v.getTag()));
        }
    };

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<ArticuloBean>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if(!constraint.toString().equals("")){
                    if(mSearchTerm.length() > constraint.toString().length())
                        mList = mListFilter;
                }

                mSearchTerm = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();

                if(mSearchTerm.equals("")){
                    results.count = mListFilter.size();
                    results.values = mList;
                    return results;
                }else{

                    List<ArticuloBean> FilteredArrayList = new ArrayList<ArticuloBean> ();
                    for (int i = 0; i < mList.size(); i++) {

                        ArticuloBean data = mList.get(i);
                        if (data.getCod().toLowerCase().contains(mSearchTerm.toString())
                                || data.getDesc().toLowerCase().contains(mSearchTerm.toString()))  {
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

    static  class RVAdapterListaArticulosPorPrecioViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigo, tvNombre, tvGrupo, tvPrecio;

        public RVAdapterListaArticulosPorPrecioViewHolder(View itemView) {
            super(itemView);

            tvCodigo = (TextView) itemView.findViewById(R.id.tvListArtXPreCodigo);
            tvNombre = (TextView) itemView.findViewById(R.id.tvListArtXPreNombre);
            tvGrupo = (TextView) itemView.findViewById(R.id.tvListArtXPreGrupo);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvListArtXPrePrecio);
        }
    }

}