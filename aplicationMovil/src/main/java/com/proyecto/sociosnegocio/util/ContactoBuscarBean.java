package com.proyecto.sociosnegocio.util;


import android.os.Parcel;
import android.os.Parcelable;

public class ContactoBuscarBean implements Parcelable {

    private int codigo;
    private String nombre;

    public ContactoBuscarBean(){};

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    protected ContactoBuscarBean(Parcel in) {
        codigo = in.readInt();
        nombre = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(codigo);
        dest.writeString(nombre);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContactoBuscarBean> CREATOR = new Parcelable.Creator<ContactoBuscarBean>() {
        @Override
        public ContactoBuscarBean createFromParcel(Parcel in) {
            return new ContactoBuscarBean(in);
        }

        @Override
        public ContactoBuscarBean[] newArray(int size) {
            return new ContactoBuscarBean[size];
        }
    };
}
