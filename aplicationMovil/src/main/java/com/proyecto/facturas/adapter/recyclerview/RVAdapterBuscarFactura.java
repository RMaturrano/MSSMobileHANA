package com.proyecto.facturas.adapter.recyclerview;

import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.facturas.adapter.recyclerview.listeners.IRVAdapterBuscarFactura;
import com.proyecto.facturas.util.FacturaBuscarBean;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;


public class RVAdapterBuscarFactura extends RecyclerView.Adapter<RVAdapterBuscarFactura.RVAdapterBuscarFacturaViewHolder>{

    private List<FacturaBuscarBean> mListFacturas;
    private IRVAdapterBuscarFactura irvAdapterBuscarFactura;
    private int mSelectedPosition = -1;

    public  RVAdapterBuscarFactura(IRVAdapterBuscarFactura irvAdapterBuscarFactura){
        this.irvAdapterBuscarFactura = irvAdapterBuscarFactura;
        mListFacturas = new ArrayList<>();
    }

    public void clearAndAddAll(List<FacturaBuscarBean> lstItems) {
        mListFacturas.clear();
        mListFacturas.addAll(lstItems);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterBuscarFacturaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterBuscarFacturaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_buscar_facturas_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterBuscarFacturaViewHolder holder, int position) {
        FacturaBuscarBean facturaBean = mListFacturas.get(position);

        holder.tvCliente.setText(facturaBean.getNombreCliente());
        holder.tvVencimiento.setText(StringDateCast.castStringtoDate(facturaBean.getVencimiento()));
        holder.tvSerieCorr.setText(facturaBean.getReferencia());
        holder.tvTotal.setText("S/. " + facturaBean.getTotal());

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);

        if(mSelectedPosition == position){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#bbdefb"));
        }else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fafafa"));
        }
    }

    @Override
    public int getItemCount() {
        return mListFacturas.size();
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            irvAdapterBuscarFactura.onItemClick(mListFacturas.get((int) v.getTag()));
            mSelectedPosition = (int) v.getTag();
            notifyDataSetChanged();
        }
    };

    static  class RVAdapterBuscarFacturaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCliente, tvVencimiento, tvSerieCorr, tvTotal;
        private CardView cardView;

        public RVAdapterBuscarFacturaViewHolder(View itemView) {
            super(itemView);

            tvCliente = (TextView) itemView.findViewById(R.id.tvBuscarFacturaCliente);
            tvVencimiento = (TextView) itemView.findViewById(R.id.tvBuscarFacturaVencimiento);
            tvSerieCorr = (TextView) itemView.findViewById(R.id.tvBuscarFacturaSerie);
            tvTotal = (TextView) itemView.findViewById(R.id.tvBuscarFacturaTotal);
            cardView = (CardView) itemView.findViewById(R.id.cvBuscarFactura);
        }
    }
}
