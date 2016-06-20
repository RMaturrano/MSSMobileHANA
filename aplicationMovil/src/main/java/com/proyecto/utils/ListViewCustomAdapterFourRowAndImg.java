package com.proyecto.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.ArticuloBean;

public class ListViewCustomAdapterFourRowAndImg extends BaseAdapter{

	
	public ArrayList<ArticuloBean> lista;
    public Context context;
    public LayoutInflater inflater;
    
    
    public ListViewCustomAdapterFourRowAndImg(Context context,ArrayList<ArticuloBean> lista) {
        super();
        this.context = context;
        this.lista = lista;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	
	@Override
	public int getCount() {
		return lista.size();
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
    }
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_four_row_and_img, null);
 
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitleRight = (TextView) convertView.findViewById(R.id.txtViewTitleRight);
            holder.txtViewDescriptionRight = (TextView) convertView.findViewById(R.id.txtViewDescriptionRight);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								
        holder.txtViewTitle.setText(lista.get(position).getDesc());
        holder.txtViewDescription.setText(String.valueOf(lista.get(position).getCant()));
        holder.imgViewLogo.setImageResource(lista.get(position).getUtilIcon());
        holder.txtViewTitleRight.setText(String.valueOf(lista.get(position).getPre()));
        holder.txtViewDescriptionRight.setText(String.valueOf(lista.get(position).getTotal()));
 
        return convertView;
		
		
	}

}
