package com.proyecto.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;

public class ListViewCustomAdapterFourRowAndImgFACT_LIST extends BaseAdapter implements Filterable{
	
	
	public ArrayList<FacturaBean> lista;
	public ArrayList<FacturaBean> listaFiltrada;
    public Context context;
    public LayoutInflater inflater;
    private String mSearchTerm;
    
    
    public ListViewCustomAdapterFourRowAndImgFACT_LIST(Context context,ArrayList<FacturaBean> lista) {
        super();
        this.context = context;
        this.lista = lista;
        listaFiltrada = lista;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	
	@Override
	public int getCount() {
		return lista != null ? lista.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewTitleRight;
        TextView txtViewDescriptionRight;
        TextView txtCodigoCliente;
        TextView txtNomCliente;
    }
	

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_four_row_and_img_lst_factura, null);
 
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitleRight = (TextView) convertView.findViewById(R.id.txtViewTitleRight);
            holder.txtViewDescriptionRight = (TextView) convertView.findViewById(R.id.txtViewDescriptionRight);
            holder.txtCodigoCliente = (TextView) convertView.findViewById(R.id.txtCodCliente);
            holder.txtNomCliente = (TextView) convertView.findViewById(R.id.txtNomCliente);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								
        holder.txtViewTitle.setText(lista.get(position).getReferencia());
        holder.txtViewDescription.setText(String.valueOf(lista.get(position).getFechaContable()));
        holder.imgViewLogo.setImageResource(lista.get(position).getUtilIcon());
        holder.txtViewDescriptionRight.setText("Saldo: "+ String.valueOf(lista.get(position).getSaldo()));
        holder.txtViewTitleRight.setText("Total: "+ String.valueOf(lista.get(position).getTotal()));
        holder.txtCodigoCliente.setText(lista.get(position).getSocioNegocio());
        holder.txtNomCliente.setText(lista.get(position).getNombreSocio());

        return convertView;
	}
	
	
	
	@Override
   	public Filter getFilter() {

        try{
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    lista = (ArrayList<FacturaBean>) results.values;
                    notifyDataSetChanged();
                }

                @SuppressLint("DefaultLocale")
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    if(!constraint.toString().equals("")){
                        if(mSearchTerm.length() > constraint.toString().length())
                            lista = listaFiltrada;
                    }

                    mSearchTerm = constraint.toString().toLowerCase();
                    FilterResults results = new FilterResults();

                    if(mSearchTerm.equals("")){

                        results.count = listaFiltrada.size();
                        results.values = listaFiltrada;

                        return results;

                    }else{

                        ArrayList<FacturaBean> FilteredArrayList = new ArrayList<FacturaBean> ();

                        // perform your search here using the searchConstraint String.

                        for (int i = 0; i < lista.size(); i++) {

                            try{
                                FacturaBean data = lista.get(i);

                                if (data.getReferencia().toLowerCase().contains(mSearchTerm.toString()) ||
                                        data.getSocioNegocio().toLowerCase().contains(mSearchTerm.toString()) ||
                                        data.getNombreSocio().toLowerCase().contains(mSearchTerm.toString()))  {

                                    FilteredArrayList.add(data);
                                }
                            }catch (Exception e){}
                        }


                        results.count = FilteredArrayList.size();
                        results.values = FilteredArrayList;

                        return results;
                    }
                }
            };

            return filter;

        }catch (Exception e){
            return null;
        }
   	}
	
	

}
