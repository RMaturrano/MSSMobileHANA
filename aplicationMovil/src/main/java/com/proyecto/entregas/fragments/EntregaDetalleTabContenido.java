package com.proyecto.entregas.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.EntregaBean;
import com.proyecto.bean.EntregaDetalleBean;
import com.proyecto.entregas.EntregaDetalleActivity;
import com.proyecto.entregas.adapter.recyclerview.RVAdapterDetalleEntrega;
import com.proyecto.entregas.adapter.recyclerview.RVAdapterDetalleLotes;
import com.proyecto.entregas.adapter.recyclerview.listeners.IRVAdapterDetalleEntrega;

public class EntregaDetalleTabContenido extends Fragment implements IRVAdapterDetalleEntrega{

    private View mView;
    private RVAdapterDetalleEntrega mRVAdapter;
    private RecyclerView mRVLista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_detalle_entrega_tab_contenido, container, false);

        mRVLista = (RecyclerView) mView.findViewById(R.id.rvListaContenido);
        mRVLista.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRVLista.setHasFixedSize(true);

        mRVAdapter = new RVAdapterDetalleEntrega(this);
        mRVLista.setAdapter(mRVAdapter);

        setHasOptionsMenu(true);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fillRows();
    }

    private void fillRows(){
        try{
            if(getActivity().getIntent().getExtras() != null){
                if(getActivity().getIntent().getExtras().containsKey(EntregaDetalleActivity.KEY_PARAM_ENTREGA)){
                    EntregaBean entrega = getActivity().getIntent().getParcelableExtra(EntregaDetalleActivity.KEY_PARAM_ENTREGA);
                    mRVAdapter.clearAndAddAll(entrega.getLineas());
                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(EntregaDetalleBean detalle) {
        try{

            if(detalle.getLotes() != null &&
                    detalle.getLotes().size() > 0) {

                View mBSView = getActivity().getLayoutInflater().inflate(
                        R.layout.bottom_sheet_entrega_lote, null);
                Dialog mBSDialog = new Dialog(getActivity(),
                        R.style.MaterialDialogSheet);
                RecyclerView mRVContent = (RecyclerView) mBSView.findViewById(R.id.rvLotes);
                mRVContent.setLayoutManager(new LinearLayoutManager(mBSView.getContext()));
                mRVContent.setHasFixedSize(true);

                RVAdapterDetalleLotes mAdapter = new RVAdapterDetalleLotes();
                mRVContent.setAdapter(mAdapter);

                mAdapter.clearAndAddAll(detalle.getLotes());

                mBSDialog.setContentView(mBSView);
                mBSDialog.setCancelable(true);
                mBSDialog.getWindow().setLayout(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mBSDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBSDialog.show();
            }else
                Toast.makeText(getActivity(), "No se encontró información sobre lotes", Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
