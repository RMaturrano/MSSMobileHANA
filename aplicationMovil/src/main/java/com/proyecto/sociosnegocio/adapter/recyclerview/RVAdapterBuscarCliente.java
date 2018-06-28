package com.proyecto.sociosnegocio.adapter.recyclerview;

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
import com.proyecto.sociosnegocio.adapter.recyclerview.listeners.IRVAdapterBuscarCliente;
import com.proyecto.sociosnegocio.util.ClienteBuscarBean;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterBuscarCliente extends RecyclerView.Adapter<RVAdapterBuscarCliente.RVAdapterBuscarClienteViewHolder>
                                    implements Filterable{

    private IRVAdapterBuscarCliente irvAdapterBuscarCliente;
    private List<ClienteBuscarBean> mListClientes;
    private List<ClienteBuscarBean> mListClientesFiltro;
    private int mSelectedPosition = -1;
    private String mSearchTerm;

    public RVAdapterBuscarCliente(IRVAdapterBuscarCliente irvAdapterBuscarCliente){
        this.irvAdapterBuscarCliente = irvAdapterBuscarCliente;
        mListClientes = new ArrayList<>();
        mListClientesFiltro = new ArrayList<>();
        mSearchTerm ="";
    }

    public void clearAndAddAll(List<ClienteBuscarBean> lstItems) {
        mListClientes.clear();
        mListClientes.addAll(lstItems);
        mListClientesFiltro.clear();
        mListClientesFiltro.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterBuscarClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterBuscarCliente.RVAdapterBuscarClienteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_buscar_clientes_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterBuscarClienteViewHolder holder, int position) {
        ClienteBuscarBean cliente = mListClientes.get(position);

        holder.tvCodigo.setText(cliente.getCodigo());
        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getTelefono());
        holder.tvDocumento.setText(cliente.getNumeroDocumento());

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);

        if(mSelectedPosition == position){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#bbdefb"));
        }else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fafafa"));
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            irvAdapterBuscarCliente.onItemClick(mListClientes.get((int) v.getTag()));
            mSelectedPosition = (int) v.getTag();
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return mListClientes.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListClientes = (ArrayList<ClienteBuscarBean>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if(!constraint.toString().equals("")){
                    if(mSearchTerm.length() > constraint.toString().length())
                        mListClientes = mListClientesFiltro;
                }

                mSearchTerm = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();

                if(mSearchTerm.equals("")){
                    results.count = mListClientesFiltro.size();
                    results.values = mListClientes;
                    return results;
                }else{

                    List<ClienteBuscarBean> FilteredArrayList = new ArrayList<ClienteBuscarBean> ();
                    for (int i = 0; i < mListClientes.size(); i++) {

                        ClienteBuscarBean data = mListClientes.get(i);
                        if (data.getCodigo().toLowerCase().contains(mSearchTerm.toString())
                                || data.getNombre().toLowerCase().contains(mSearchTerm.toString()))  {
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

    static  class RVAdapterBuscarClienteViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigo, tvNombre, tvTelefono, tvDocumento;
        private CardView cardView;

        public RVAdapterBuscarClienteViewHolder(View itemView) {
            super(itemView);

            tvCodigo = (TextView) itemView.findViewById(R.id.tvBuscarClienteCodigo);
            tvNombre = (TextView) itemView.findViewById(R.id.tvBuscarClienteNombre);
            tvTelefono = (TextView) itemView.findViewById(R.id.tvBuscarClienteTelefono);
            tvDocumento = (TextView) itemView.findViewById(R.id.tvBuscarClienteDocumento);
            cardView = (CardView) itemView.findViewById(R.id.cvBuscarCliente);
        }
    }

}
