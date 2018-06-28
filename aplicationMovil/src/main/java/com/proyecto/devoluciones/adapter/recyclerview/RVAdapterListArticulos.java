package com.proyecto.devoluciones.adapter.recyclerview;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.bean.EntregaDetalleLoteBean;
import com.proyecto.devoluciones.adapter.recyclerview.listeners.IRVAdapterListArticulos;
import com.proyecto.entregas.EntregaDetalleActivity;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterListArticulos extends RecyclerView.Adapter<RVAdapterListArticulos.RVAdapterListArticulosViewHolder>{

    private List<EntregaDetalleBean> mList;
    private IRVAdapterListArticulos mIRVInterface;

    public RVAdapterListArticulos(IRVAdapterListArticulos listener){
        mList = new ArrayList<>();
        mIRVInterface = listener;
    }

    public List<EntregaDetalleBean> getSelected(){
        return  mList;
    }

    public void clearAndAddAll(List<EntregaDetalleBean> detalles){
        mList.clear();
        mList.addAll(detalles);
    }

    @Override
    public RVAdapterListArticulosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVAdapterListArticulos.RVAdapterListArticulosViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.rv_dev_lista_articulos_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RVAdapterListArticulosViewHolder holder, int position) {
        final EntregaDetalleBean bean = mList.get(position);

        holder.cbxSeleccionado.setChecked(bean.isSelected() ? true: false);
        holder.cbxSeleccionado.setOnClickListener(itemViewOnClickListener);
        holder.cbxSeleccionado.setTag(position);

        holder.tvCodigoArticulo.setText(bean.getArticulo());
        holder.tvDescripcionArticulo.setText(bean.getArticuloNombre());
        holder.tvPrecioUnitario.setText(bean.getPrecioUnitario());
        holder.edtCantidad.setText(bean.isSelected() ? bean.getCantidadTemp() : bean.getDiponible());

        holder.btnMas.setOnClickListener(btnMasOnClickListener);
        holder.btnMas.setTag(position);

        holder.btnMenos.setOnClickListener(btnMenosOnClickListener);
        holder.btnMenos.setTag(position);

        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.itemView.setTag(position);

        holder.fabVerLotes.setOnClickListener(onClickVerLotesListener);
        holder.fabVerLotes.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaDetalleBean b = mList.get((int) v.getTag());
            b.setSelected(b.isSelected() ? false : true);
            b.setCantidadTemp(b.getDiponible());

            for (EntregaDetalleLoteBean lote: b.getLotes()) {
                lote.setCantidadTemp(lote.getCantidad());
            }

            mList.set((int) v.getTag(), b);
            notifyItemChanged((int) v.getTag());

            mIRVInterface.onItemClick(b);
        }
    };

    private View.OnClickListener onClickVerLotesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaDetalleBean b = mList.get((int) v.getTag());
            mIRVInterface.onVerLotesClick(b, (int) v.getTag());
        }
    };

    private View.OnClickListener btnMasOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaDetalleBean b = mList.get((int) v.getTag());
            if(b.isSelected()) {
                int cant = Integer.parseInt(b.getCantidadTemp());
                int cantOf = Integer.parseInt(b.getDiponible());
                cant++;

                if(cant <= cantOf){
                    b.setCantidadTemp(String.valueOf(cant));
                    mList.set((int) v.getTag(), b);
                    notifyItemChanged((int) v.getTag());
                }
            }
        }
    };

    private View.OnClickListener btnMenosOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EntregaDetalleBean b = mList.get((int) v.getTag());
            if(b.isSelected()) {
                int cant = Integer.parseInt(b.getCantidadTemp());
                cant--;

                if(cant > 0){
                    b.setCantidadTemp(String.valueOf(cant));
                    mList.set((int) v.getTag(), b);
                    notifyItemChanged((int) v.getTag());
                }
            }
        }
    };


    class RVAdapterListArticulosViewHolder extends RecyclerView.ViewHolder{

        private CheckBox cbxSeleccionado;
        private TextView tvCodigoArticulo, tvDescripcionArticulo, tvPrecioUnitario;
        private EditText edtCantidad;
        private Button btnMas, btnMenos;
        FloatingActionButton fabVerLotes;

        public RVAdapterListArticulosViewHolder(View itemView) {
            super(itemView);

            cbxSeleccionado = (CheckBox) itemView.findViewById(R.id.cbxDevArtSeleccionado);
            tvCodigoArticulo = (TextView) itemView.findViewById(R.id.tvDevArtCodigo);
            tvDescripcionArticulo = (TextView) itemView.findViewById(R.id.tvDevArtDescripcion);
            tvPrecioUnitario = (TextView) itemView.findViewById(R.id.tvDevArtPrecioUnitario);
            edtCantidad = (EditText) itemView.findViewById(R.id.tvDevArtCantidad);
            btnMas = (Button) itemView.findViewById(R.id.btnDevArtMas);
            btnMenos = (Button) itemView.findViewById(R.id.btnDevArtMenos);
            fabVerLotes = (FloatingActionButton) itemView.findViewById(R.id.fabDevVerLote);
        }
    }
}
