package com.proyecto.entregas.adapter.recyclerview;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.entregas.adapter.recyclerview.listeners.IRVAdapterDetalleEntrega;
import com.proyecto.utils.StringDateCast;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterDetalleEntrega extends RecyclerView.Adapter<RVAdapterDetalleEntrega.RVAdapterDetalleEntregaViewHolder>{

    private List<EntregaDetalleBean> mListDetalle;
    private IRVAdapterDetalleEntrega mInterface;

    public RVAdapterDetalleEntrega(IRVAdapterDetalleEntrega mInterface){
        mListDetalle = new ArrayList<>();
        this.mInterface = mInterface;
    }

    @Override
    public RVAdapterDetalleEntregaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterDetalleEntrega.RVAdapterDetalleEntregaViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_listar_entrega_detalle_contenido_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVAdapterDetalleEntregaViewHolder holder, int position) {
        EntregaDetalleBean entrega = mListDetalle.get(position);

        holder.tvCodigoItem.setText(entrega.getArticulo());
        holder.tvDescripcionItem.setText(entrega.getArticuloNombre());
        holder.tvCantidad.setText(entrega.getCantidad());
        holder.tvPrecio.setText(entrega.getPrecioUnitario());
        holder.tvDisponible.setText(entrega.getDiponible());
        holder.tvAlmacen.setText(entrega.getAlmacenNombre());

        holder.fabVerLotes.setOnClickListener(fabClickListener);
        holder.fabVerLotes.setTag(position);
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaDetalleBean entrega = mListDetalle.get((int) v.getTag());
            mInterface.onItemClick(entrega);
        }
    };

    @Override
    public int getItemCount() {
        return mListDetalle.size();
    }

    public void clearAndAddAll(List<EntregaDetalleBean> detalles){
        mListDetalle.clear();
        mListDetalle.addAll(detalles);
        notifyDataSetChanged();
    }

    class RVAdapterDetalleEntregaViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCodigoItem, tvDescripcionItem, tvCantidad, tvPrecio, tvDisponible, tvAlmacen;
        private FloatingActionButton fabVerLotes;

        public RVAdapterDetalleEntregaViewHolder(View itemView) {
            super(itemView);

            tvCodigoItem = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoCodigo);
            tvDescripcionItem = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoDescripcion);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoCantidad);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoPrecio);
            fabVerLotes = (FloatingActionButton) itemView.findViewById(R.id.fabVerLote);
            tvDisponible = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoDispon);
            tvAlmacen = (TextView) itemView.findViewById(R.id.tvListarEntregaContenidoAlmacen);
        }
    }

}
