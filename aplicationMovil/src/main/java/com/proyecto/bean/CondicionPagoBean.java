package com.proyecto.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class CondicionPagoBean implements Parcelable {

	private String numeroCondicion;
	private String descripcionCondicion;
	private String diasExtra;

	public CondicionPagoBean(){};

	@Override
	public String toString() {
		return this.descripcionCondicion;
	}

	public String getNumeroCondicion() {
		return numeroCondicion;
	}

	public void setNumeroCondicion(String numeroCondicion) {
		this.numeroCondicion = numeroCondicion;
	}

	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	public String getDiasExtra() {
		return diasExtra;
	}

	public void setDiasExtra(String diasExtra) {
		this.diasExtra = diasExtra;
	}

	protected CondicionPagoBean(Parcel in) {
		numeroCondicion = in.readString();
		descripcionCondicion = in.readString();
		diasExtra = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(numeroCondicion);
		dest.writeString(descripcionCondicion);
		dest.writeString(diasExtra);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<CondicionPagoBean> CREATOR = new Parcelable.Creator<CondicionPagoBean>() {
		@Override
		public CondicionPagoBean createFromParcel(Parcel in) {
			return new CondicionPagoBean(in);
		}

		@Override
		public CondicionPagoBean[] newArray(int size) {
			return new CondicionPagoBean[size];
		}
	};
}
