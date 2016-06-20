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
import com.proyecto.bean.DialogBean;

public class ListViewCustomAdapterImgAndLine extends BaseAdapter{
	
    private ArrayList<DialogBean> description;
 
    public Context context;
    public LayoutInflater inflater;
 
    public ListViewCustomAdapterImgAndLine(Context context, ArrayList<DialogBean> description) {
        super();
 
        this.context = context;
        this.description = description;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return description.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
 
    public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewDescription;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_row_dialog, null);
 
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								//images[position]
        holder.imgViewLogo.setImageResource(description.get(position).getImage());
        holder.txtViewDescription.setText(description.get(position).getDescription());
 
        return convertView;
    }
    
    
    

}
