package com.val.ebtm.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 *
 * Clase que agrupa las funciones auxiliares de hora y fecha para su uso
 * al resto del clases del proyecto que las necesite
 * 
 * @author Vale
 *
 */
public class FechaUtil {
	
	
	public static final String GTM_USADO = "GMT+1:00";
    public static final int DIA_PROGRAMA = 0; //el programa se emite los domingos
    public static final int HORA_FIN_PGM = 2; //el programa finaliza a las 2 de la noche, hora a partir de la cual puede encontrarse disponible
    public static final int MIN_FIN_PGM = 0; //el programa finaliza a las 2 de la noche, hora a partir de la cual puede encontrarse disponible
    public static final int SEC_FIN_PGM = 0; //el programa finaliza a las 2 de la noche, hora a partir de la cual puede encontrarse disponible
    public static final int INTERVALO_MS_DIA_PGM = 60 * 1000 * 15; //cada quince minutos se reprogrma la alarma el domingo


    public static int obtenerDiasHastaProximaEmision (Calendar calendar)
    {
        int dias_hasta_proximo = 0;

            dias_hasta_proximo = (8-calendar.get(Calendar.DAY_OF_WEEK))%7; //esto funciona porque se emite en DOMINGO. Si se cambia la fecha, debería

        return dias_hasta_proximo;
    }

    public static String obtenerFechaActual()
    {
        String fecha_actual = null;
        SimpleDateFormat sdf = null;
        Date date = null;

            sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date();
            fecha_actual = sdf.format(date);

        return fecha_actual;
    }
	
	public static String fromNominal2Numeric (String fecha_nominal)
	{
		String fecha_numerica = null;
		String mes = null;
		String dia = null;
		String anyo = null;
		String mes_nominal = null;
		
			
		
			Log.d(FechaUtil.class.getCanonicalName(), "Fecha recibida en formato numérico " + fecha_nominal);
		
			//13 de Enero de 2010
			int pos_espacio_fin_mes = fecha_nominal.indexOf(" ", 6); //6 es la posici�n donde empieza el mes
			int pos_espacio_prev_anio = fecha_nominal.indexOf(" ", pos_espacio_fin_mes+1); //6 es la posici�n donde empieza el mes
			
			
			mes_nominal = fecha_nominal.substring(6, pos_espacio_fin_mes);
			anyo = fecha_nominal.substring(pos_espacio_prev_anio+1, fecha_nominal.length());
			dia = fecha_nominal.substring(0, 2);
			
			switch (mes_nominal) {
			case "Enero":
					mes = "01";
				break;

			case "Febrero":
					mes = "02";
				break;
			
			case "Marzo":
					mes = "03";
				break;
			
			case "Abril":
					mes = "04";
				break;
				
			case "Mayo":
					mes = "05";
				break;
			
			case "Junio":
					mes = "06";
				break;
			
			case "Julio":
					mes = "07";
				break;
			
			case "Agosto":
					mes = "08";
				break;
			
			case "Septiembre":
					mes = "09";
				break;
			
			case "Octubre":
					mes = "10";
				break;
			
			case "Noviembre":
					mes = "11";
				break;
			
			case "Diciembre":
					mes = "12";
				break;
			
			}
			
			fecha_numerica = anyo+"-"+mes+"-"+dia;

			Log.d(FechaUtil.class.getCanonicalName(), "Fecha traducida a numero " + fecha_numerica);
		
		return fecha_numerica;
	}

	public static String  fromNumeric2Nominal  (String fecha)
	{
		String fecha_nominal = null;
		String mes = null;
		String dia = null;
		String anyo = null;
		String mes_nominal = null;
		
			
		
			Log.d(FechaUtil.class.getCanonicalName(), "Fecha recibida en formato numérico " + fecha);
		
			anyo = fecha.substring(0, 4);
			mes = fecha.substring(5, 7);
			dia = fecha.substring(8, 10);
		
			switch (mes) {
			case "01":
					mes_nominal = "Enero";
				break;

			case "02":
					mes_nominal = "Febrero";
				break;
			
			case "03":
					mes_nominal = "Marzo";
				break;
			
			case "04":
					mes_nominal = "Abril";
				break;
				
			case "05":
					mes_nominal = "Mayo";
				break;
			
			case "06":
					mes_nominal = "Junio";
				break;
			
			case "07":
					mes_nominal = "Julio";
				break;
			
			case "08":
					mes_nominal = "Agosto";
				break;
			
			case "09":
					mes_nominal = "Septiembre";
				break;
			
			case "10":
					mes_nominal = "Octubre";
				break;
			
			case "11":
					mes_nominal = "Noviembre";
				break;
			
			case "12":
					mes_nominal = "Diciembre";
				break;
			
			}
			
			fecha_nominal = dia + " de " + mes_nominal + " de " + anyo;

			Log.d(FechaUtil.class.getCanonicalName(), "Fecha traducida en formato nominal " + fecha_nominal);
		
		return fecha_nominal;
	}
}
