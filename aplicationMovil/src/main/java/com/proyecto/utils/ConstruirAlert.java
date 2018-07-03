package com.proyecto.utils;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

public class ConstruirAlert {
	
	FormatCustomListView fullObject = null;
	FormatCustomListView fullObject2 = null;
	Context context;
	
	public void construirAlert(Context contexto, final int posicion, String titulo,
						final ArrayList<FormatCustomListView> searchResults,
						final ListView lv, String tipoString, int maxLength){
		
		this.context = contexto;

		fullObject = new FormatCustomListView();
    	fullObject = (FormatCustomListView) lv.getItemAtPosition(posicion);
    	
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle(titulo);

		final EditText edt = new EditText(contexto);
		alert.setView(edt);
		
		if(tipoString.equals("numeric")){
			edt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		}else if(tipoString.equalsIgnoreCase("decimal")){
			edt.setRawInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}

		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		edt.setFilters(fArray);
		edt.setMaxLines(1);
		edt.setSingleLine(true);
		edt.setFocusableInTouchMode(true);
		edt.requestFocus();
		
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {

			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
			
			fullObject.setData(edt.getText().toString());
			searchResults.set(posicion, fullObject);
			lv.invalidateViews();
			
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
			// Check if no view has focus:
			  InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
		  }
		});

		edt.requestFocus();
		
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

		alert.show();
		
	}
	
	
	public void construirAlertDatePicker(Context contexto, final int posicion, String titulo,
			final ArrayList<FormatCustomListView> searchResults,
			final ListView lv){


			fullObject = new FormatCustomListView();
			fullObject = (FormatCustomListView) lv.getItemAtPosition(posicion);
			
			
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle(titulo);
			
			final DatePicker dpFecha = new DatePicker(contexto);
			dpFecha.setCalendarViewShown(false);
    		
			alert.setView(dpFecha);
			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			
				 int dia = dpFecha.getDayOfMonth();
				 int mes = dpFecha.getMonth() + 1;
				 int a = dpFecha.getYear();
				 String fecha = a +"/"+String.format("%02d", mes)+"/"+String.format("%02d", dia);
				
			fullObject.setData(fecha);
			searchResults.set(posicion, fullObject);
			lv.invalidateViews();
			
			}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
			}
			});
			
			alert.show();

	}
	
	

}
