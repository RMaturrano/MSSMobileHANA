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

public class ListViewCustomAdapterImgAndTwoLines extends BaseAdapter{
	
	public ArrayList<String> title;
    public ArrayList<String> description;
    //public int images[]   -> para poner diferentes imagenes
    public int images;
 
    public Context context;
    public LayoutInflater inflater;
 
    																					//int[] image
    public ListViewCustomAdapterImgAndTwoLines(Context context,ArrayList<String> title, ArrayList<String> description, int image) {
        super();
 
        this.context = context;
        this.title = title;
        this.description = description;
        this.images = image;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.size();
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
        TextView txtViewTitle;
        TextView txtViewDescription;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_row_lstsn, null);
 
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								//images[position]
        holder.imgViewLogo.setImageResource(images);
        holder.txtViewTitle.setText(title.get(position));
        holder.txtViewDescription.setText(description.get(position));
 
        return convertView;
    }
    
    
    

}
