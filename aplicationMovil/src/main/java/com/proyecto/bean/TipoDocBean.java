package com.proyecto.bean;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proyecto.database.MyDataBase;

public class TipoDocBean {
	
	private String codigo, descripcion;
	private TipoDocBean b = null;
	private ArrayList<TipoDocBean> lista = null;
	
	public ArrayList<TipoDocBean> listaDoc(Context contexto, String tipoPersona){
		
		lista = new ArrayList<TipoDocBean>();
		
		//Traer de la base de datos interna
		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
		SQLiteDatabase db= cn.getWritableDatabase();
		
		Cursor data = null;
		if(tipoPersona.equalsIgnoreCase("TPJ")){
			data= db.rawQuery("select COD_DOC, DES_DOC from TB_TIPO_DOC where COD_DOC <> 1", null); 
		}else if(tipoPersona.equalsIgnoreCase("TPN")){
			data= db.rawQuery("select COD_DOC, DES_DOC from TB_TIPO_DOC", null);
		}
			
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				b = new TipoDocBean();
				b.setCodigo(data.getString(0));
				b.setDescripcion(data.getString(1));
				lista.add(b);
			}
											
			data.close();
			db.close();
											
		}
		
		
		return lista;
		
	}
	
	
	@Override
	public String toString() {
		return codigo+"  -  "+descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
