package com.proyecto.notacredito.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaDetalleLoteBean;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterNCDetalleLotes extends RecyclerView.Adapter<RVAdapterNCDetalleLotes.RVAdapterNCDetalleLotesViewHolder>{
    private List<FacturaDetalleLoteBean> mListDetalle;

    public RVAdapterNCDetalleLotes(){
        mListDetalle = new ArrayList<>();
    }

    @Override
    public RVAdapterNCDetalleLotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterNCDetalleLotes.RVAdapterNCDetalleLotesViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_detalle_lote_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterNCDetalleLotesViewHolder holder, final int position) {
        FacturaDetalleLoteBean lote = mListDetalle.get(position);
        holder.edtCantidad.setText(String.valueOf(lote.getCantidadTemp()));
        holder.tvLote.setText(lote.getLote());
        holder.edtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null && !s.toString().equals("")) {
                    mListDetalle.get(position).setCantidadTemp(Double.parseDouble(s.toString()));
                }else{
                    mListDetalle.get(position).setCantidadTemp(0d);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListDetalle.size();
    }

    public List<FacturaDetalleLoteBean> getAll(){
        return mListDetalle;
    }

    public void clearAndAddAll(List<FacturaDetalleLoteBean> detalles){
        mListDetalle.clear();
        mListDetalle.addAll(detalles);
        notifyDataSetChanged();
    }

    class RVAdapterNCDetalleLotesViewHolder extends RecyclerView.ViewHolder{

        private TextView tvLote;
        private EditText edtCantidad;

        public RVAdapterNCDetalleLotesViewHolder(View itemView) {
            super(itemView);

            tvLote = (TextView) itemView.findViewById(R.id.tvDetLoteCodigo);
            edtCantidad = (EditText) itemView.findViewById(R.id.tvDetLoteCantidad);
        }
    }
}
