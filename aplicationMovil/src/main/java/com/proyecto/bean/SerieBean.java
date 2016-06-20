package com.proyecto.bean;

import java.util.ArrayList;

public class SerieBean {

	private String id, desc;
	SerieBean bean = null;
	
	@Override
	public String toString() {
		return this.desc;
	}
	
	public ArrayList<SerieBean> listaSeriesVenta(){
		
		ArrayList<SerieBean> lst = new ArrayList<SerieBean>();
		bean = new SerieBean();
		bean.setId("0");
		bean.setDesc("PECL001");
		lst.add(bean);
		
		bean = new SerieBean();
		bean.setId("1");
		bean.setDesc("Primario");
		lst.add(bean);
		
		return lst;
		
	}
	
	
	public ArrayList<SerieBean> listaSeriesCobranza(){
		
		ArrayList<SerieBean> lst = new ArrayList<SerieBean>();
		bean = new SerieBean();
		bean.setId("0");
		bean.setDesc("PARE001");
		lst.add(bean);
		
		bean = new SerieBean();
		bean.setId("1");
		bean.setDesc("Primario");
		lst.add(bean);
		
		return lst;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
