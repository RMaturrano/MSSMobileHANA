package com.proyecto.bean;


public class GrupoSocioNegocioBean {

	private String groupCode, groupName;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.groupName;
	}
	
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	
	
}
