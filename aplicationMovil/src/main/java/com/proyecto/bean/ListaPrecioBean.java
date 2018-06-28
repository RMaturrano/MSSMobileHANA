package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListaPrecioBean implements Parcelable {

	private String codigo;
	private String nombre;
	private ArrayList<PrecioBean> precio;

	public ListaPrecioBean(){};

	@Override
	public String toString() {
		return this.nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<PrecioBean> getPrecio() {
		return precio;
	}

	public void setPrecio(ArrayList<PrecioBean> precio) {
		this.precio = precio;
	}


	protected ListaPrecioBean(Parcel in) {
		codigo = in.readString();
		nombre = in.readString();
		if (in.readByte() == 0x01) {
			precio = new ArrayList<PrecioBean>();
			in.readList(precio, PrecioBean.class.getClassLoader());
		} else {
			precio = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(codigo);
		dest.writeString(nombre);
		if (precio == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(precio);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ListaPrecioBean> CREATOR = new Parcelable.Creator<ListaPrecioBean>() {
		@Override
		public ListaPrecioBean createFromParcel(Parcel in) {
			return new ListaPrecioBean(in);
		}

		@Override
		public ListaPrecioBean[] newArray(int size) {
			return new ListaPrecioBean[size];
		}
	};
}
