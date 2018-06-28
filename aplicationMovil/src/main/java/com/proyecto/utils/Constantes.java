package com.proyecto.utils;

import java.util.concurrent.BrokenBarrierException;

public class Constantes {

    public static final String DEFAULT_IP = "190.116.31.110";
    public static final String DEFAULT_PORT = "8000";
    public static final int DEFAULT_RANGE = 50;

    public static final String TIPO_DIRECCION_FISCAL = "B";
    public static final String TIPO_DIRECCION_ENTREGA = "S";

    public static final String DENTRO_DE_RANGO = "01";
    public static final String FUERA_DE_RANGO = "02";
    public static final String RANGO_NO_DISPONIBLE = "03";

    public static final String FRECUENCIA_SEMANAL = "101";
    public static final String FRECUENCIA_QUINCENAL = "102";
    public static final String FRECUENCIA_MENSUAL = "103";

    public static final String TIPO_REGISTRO_LEAD = "01";
    public static final String TIPO_REGISTRO_FINAL = "02";
    public static final String TIPO_REGISTRO_COMPETENCIA = "03";

    public static String obtenerDescripcionRango(String code){
        if(code != null) {
            switch (code) {
                case DENTRO_DE_RANGO:
                    return "Dentro del rango";
                case FUERA_DE_RANGO:
                    return "Fuera del rango";
                case RANGO_NO_DISPONIBLE:
                    return "No disponible";
                default:
                    return "Info. no disponible";
            }
        }else
            return "Info. no disponible";
    }

    public static String obtenerTipoRegistro(String codigo){
        String res = "";

        if(codigo != null){
            switch (codigo){
                case TIPO_REGISTRO_LEAD:
                    res = "Lead";
                    break;
                case TIPO_REGISTRO_FINAL:
                    res =  "Cliente final";
                    break;
                case TIPO_REGISTRO_COMPETENCIA:
                    res =  "Competencia";
                    break;
            }
        }

        return res;
    }

    public static String obtenerDescripcionFrecuencia(String code){
        if(code != null){
            switch (code){
                case FRECUENCIA_SEMANAL:
                    return "Semanal";
                case FRECUENCIA_QUINCENAL:
                    return "Quincenal";
                case FRECUENCIA_MENSUAL:
                    return "Mensual";
                default:
                    return "";
            }
        }else
            return "";
    }

}
