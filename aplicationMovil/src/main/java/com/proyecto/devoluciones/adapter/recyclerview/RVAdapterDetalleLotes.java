package com.proyecto.devoluciones.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaDetalleLoteBean;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterDetalleLotes extends RecyclerView.Adapter<RVAdapterDetalleLotes.RVAdapterDetalleLotesViewHolder>{
    private List<EntregaDetalleLoteBean> mListDetalle;

    public RVAdapterDetalleLotes(){
        mListDetalle = new ArrayList<>();
    }

    @Override
    public RVAdapterDetalleLotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterDetalleLotes.RVAdapterDetalleLotesViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_detalle_lote_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterDetalleLotesViewHolder holder, final int position) {
        EntregaDetalleLoteBean lote = mListDetalle.get(position);
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

    public List<EntregaDetalleLoteBean> getAll(){
        return mListDetalle;
    }

    public void clearAndAddAll(List<EntregaDetalleLoteBean> detalles){
        mListDetalle.clear();
        mListDetalle.addAll(detalles);
        notifyDataSetChanged();
    }

    class RVAdapterDetalleLotesViewHolder extends RecyclerView.ViewHolder{

        private TextView tvLote;
        private EditText edtCantidad;

        public RVAdapterDetalleLotesViewHolder(View itemView) {
            super(itemView);

            tvLote = (TextView) itemView.findViewById(R.id.tvDetLoteCodigo);
            edtCantidad = (EditText) itemView.findViewById(R.id.tvDetLoteCantidad);
        }
    }
}
