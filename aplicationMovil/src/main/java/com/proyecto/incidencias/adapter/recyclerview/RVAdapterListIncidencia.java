package com.proyecto.incidencias.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.IncidenciaBean;
import com.proyecto.incidencias.IncidenciaActivity;
import com.proyecto.incidencias.adapter.recyclerview.listeners.IRVAdapterListIncidencia;
import com.proyecto.sociosnegocio.adapter.recyclerview.RVAdapterBuscarCliente;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListIncidencia extends RecyclerView.Adapter<RVAdapterListIncidencia.IRVAdapterListIncidenciaViewHolder>{

    private List<IncidenciaBean> mList;
    private IRVAdapterListIncidencia mIRVAdapterListIncidencia;
    private String tipoListado = IncidenciaActivity.ORDEN;

    public RVAdapterListIncidencia(IRVAdapterListIncidencia irvAdapterListIncidencia, String tipoListado){
        mList = new ArrayList<>();
        mIRVAdapterListIncidencia = irvAdapterListIncidencia;
        this.tipoListado = tipoListado;
    }

    public void clearAndAddAll(List<IncidenciaBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public IRVAdapterListIncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListIncidencia.IRVAdapterListIncidenciaViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_incidencia_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IRVAdapterListIncidenciaViewHolder holder, int position) {
        IncidenciaBean incidencia = mList.get(position);

        holder.tvFechaCreacion.setText(incidencia.getFechaCreacion() + " " + incidencia.getHoraCreacion());

        if(tipoListado.equals(IncidenciaActivity.FACTURA)){
            holder.tvListarIncidenciaOrdenMotivoDes.setText("Fecha compromiso pago");
            holder.tvListarIncidenciaOrdenCodigoDes.setText("Serie factura");
            holder.tvListarIncidenciaOrdenNombreDes.setText("Correlativo");
            holder.tvMotivo.setText(incidencia.getFechaCompromisoPago());
            holder.tvCodigoCliente.setText(incidencia.getSerieFactura());
            holder.tvNombreCliente.setText(String.valueOf(incidencia.getCorrelativoFactura()));
        }else{
            holder.tvListarIncidenciaOrdenMotivoDes.setText("Motivo");
            holder.tvMotivo.setText(incidencia.getMotivo());
            holder.tvCodigoCliente.setText(incidencia.getCodigoCliente());
            holder.tvNombreCliente.setText(incidencia.getNombreCliente());
        }

        if(incidencia.getSincronizado() != null){
            if(incidencia.getSincronizado().equals("N")){
                holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_off_blue_36dp);
            }else{
                holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_done_blue_36dp);
            }
        }else{
            holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_done_blue_36dp);
        }

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setOnLongClickListener(itemViewOnLongClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class IRVAdapterListIncidenciaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigoCliente, tvNombreCliente, tvFechaCreacion, tvMotivo,
                tvListarIncidenciaOrdenMotivoDes, tvListarIncidenciaOrdenCodigoDes, tvListarIncidenciaOrdenNombreDes;
        private ImageView imgSincronizado;

        public IRVAdapterListIncidenciaViewHolder(View itemView) {
            super(itemView);

            tvCodigoCliente = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenCodigoCli);
            tvNombreCliente = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenNombre);
            tvFechaCreacion = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenFecha);
            tvMotivo = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenMotivo);
            tvListarIncidenciaOrdenMotivoDes = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenMotivoDes);
            tvListarIncidenciaOrdenCodigoDes = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenCodigoDes);
            tvListarIncidenciaOrdenNombreDes = (TextView) itemView.findViewById(R.id.tvListarIncidenciaOrdenNombreDes);
            imgSincronizado = (ImageView) itemView.findViewById(R.id.imgListarIncidenciaSincronizado);
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mIRVAdapterListIncidencia.onItemClick(mList.get((int)v.getTag()));
        }
    };

    private View.OnLongClickListener itemViewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mIRVAdapterListIncidencia.onItemLongClick(mList.get((int)v.getTag()));
            return true;
        }
    };

}
