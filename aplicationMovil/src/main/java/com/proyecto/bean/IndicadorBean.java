package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class IndicadorBean implements Parcelable {

	private String codigo;
	private String nombre;

	public IndicadorBean(){};

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

	protected IndicadorBean(Parcel in) {
		codigo = in.readString();
		nombre = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(codigo);
		dest.writeString(nombre);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<IndicadorBean> CREATOR = new Parcelable.Creator<IndicadorBean>() {
		@Override
		public IndicadorBean createFromParcel(Parcel in) {
			return new IndicadorBean(in);
		}

		@Override
		public IndicadorBean[] newArray(int size) {
			return new IndicadorBean[size];
		}
	};
}
