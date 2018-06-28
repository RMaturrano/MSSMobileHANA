package com.proyecto.devoluciones.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.DevolucionBean;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterListaDevolucion;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListaDevolucion  extends RecyclerView.Adapter<RVAdapterListaDevolucion.RVAdapterListaDevolucionViewHolder>{

    private List<DevolucionBean> mList;
    private IRVAdapterListaDevolucion mIRVAdapter;

    public RVAdapterListaDevolucion(IRVAdapterListaDevolucion intf){
        mList = new ArrayList<>();
        mIRVAdapter = intf;
    }

    public void clearAndAddAll(List<DevolucionBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RVAdapterListaDevolucionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListaDevolucion.RVAdapterListaDevolucionViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_devolucion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterListaDevolucionViewHolder holder, int position) {
        DevolucionBean devolucion = mList.get(position);

        holder.tvFechaCreacion.setText(StringDateCast.castStringtoDate(devolucion.getFechaContable()));
        holder.tvCodigoCliente.setText(devolucion.getSocioNegocio());
        holder.tvNombreCliente.setText(devolucion.getSocioNegocioNombre());
        holder.tvClave.setText(devolucion.getClave());

        if(devolucion.getEstadoMovil() != null){
            if(devolucion.getEstadoMovil().equals("L")){
                holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_off_blue_36dp);
            }else if(devolucion.getEstadoMovil().equals("U")){
                holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_done_blue_36dp);
            }else{
                holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_download_blue_36dp);
            }
        }else{
            holder.imgSincronizado.setImageResource(R.drawable.ic_cloud_off_blue_36dp);
        }

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setOnLongClickListener(itemViewOnLongClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class RVAdapterListaDevolucionViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigoCliente, tvNombreCliente, tvFechaCreacion, tvClave;
        private ImageView imgSincronizado;

        public RVAdapterListaDevolucionViewHolder(View itemView) {
            super(itemView);

            tvCodigoCliente = (TextView) itemView.findViewById(R.id.tvListarDevCodigoCli);
            tvNombreCliente = (TextView) itemView.findViewById(R.id.tvListarDevNombre);
            tvFechaCreacion = (TextView) itemView.findViewById(R.id.tvListarDevFecha);
            tvClave = (TextView) itemView.findViewById(R.id.tvListarDevClave);
            imgSincronizado = (ImageView) itemView.findViewById(R.id.imgListarDevSincronizado);
        }
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mIRVAdapter.onItemClick(mList.get((int)v.getTag()));
        }
    };

    private View.OnLongClickListener itemViewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mIRVAdapter.onItemLongClick(mList.get((int)v.getTag()));
            return true;
        }
    };
}
