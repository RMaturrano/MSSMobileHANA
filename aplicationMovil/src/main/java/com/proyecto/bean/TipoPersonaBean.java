package com.proyecto.bean;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proyecto.database.MyDataBase;

public class TipoPersonaBean {

	private String codigo, descripcion;
	private TipoPersonaBean b = null;
	private ArrayList<TipoPersonaBean> lista = null;

	@Override
	public String toString() {
		return this.codigo + " - " + descripcion;
	}

	public ArrayList<TipoPersonaBean> lista(Context contexto) {

		lista = new ArrayList<TipoPersonaBean>();

		// Traer de la base de datos interna
		MyDataBase cn = new MyDataBase(contexto, null, null,
				MyDataBase.DATABASE_VERSION);
		SQLiteDatabase db = cn.getWritableDatabase();
		Cursor data = db.rawQuery("select COD_TIP, DES_TIP from TB_TIPO_PERSONA",
				null);
		if (data.getCount() > 0) {
			while (data.moveToNext()) {
				b = new TipoPersonaBean();
				b.setCodigo(data.getString(0));
				b.setDescripcion(data.getString(1));
				lista.add(b);
			}

			data.close();
			db.close();

		}

		return lista;

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
