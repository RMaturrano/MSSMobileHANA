package com.proyecto.utils;

public class StringDateCast {
	
	public static String castStringtoDate(String cadena){
		
		return cadena.substring(0, 4) + "/"+
				cadena.substring(4, 6) + "/"+
				cadena.substring(6);
		
	}
	
	
	public static String castDatetoDateWithoutSlash(String cadena){
		
		return cadena.substring(0, 4) +
				cadena.substring(5, 7) +
				cadena.substring(8);
		
	}

}
