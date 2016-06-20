package com.proyecto.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.widget.ListView;

public class BuildListInit {
	

	@SuppressWarnings("rawtypes")
	public void buildItemList(Context contexto, ArrayList<FormatCustomListView> searchResults,
			ListViewCustomAdapterTwoLinesAndImg adapter, ListView lv, HashMap<Integer, String> titulos){
		
		FormatCustomListView sr;
		
		
		Set set = titulos.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();

	         sr = new FormatCustomListView();
			 sr.setTitulo(mentry.getValue().toString());
			 sr.setIcon(Integer.parseInt(mentry.getKey().toString()));
			 searchResults.add(sr);
	         
	      }

		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults);
		lv.setAdapter(adapter);
    	DynamicHeight.setListViewHeightBasedOnChildren(lv);
		
	}
	

}
