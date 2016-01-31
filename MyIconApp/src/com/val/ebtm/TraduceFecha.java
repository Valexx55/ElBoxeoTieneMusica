package com.val.ebtm;

import android.util.Log;


/**
 * Esta clase deber�a ir un paquete Utils...ya ir�..
 * 
 * Se trata de recibir una fecha AAAA-MM-DD
 * y devolver Dia de Mes de A�o
 * 
 * 
 * @author Vale
 *
 */
public class TraduceFecha {
	
	
	
	
	public static String fromNominal2Numeric (String fecha_nominal)
	{
		String fecha_numerica = null;
		String mes = null;
		String dia = null;
		String anyo = null;
		String mes_nominal = null;
		
			
		
			Log.d(TraduceFecha.class.getCanonicalName(), "Fecha recibida en formato num�rico " + fecha_nominal);
		
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

			Log.d(TraduceFecha.class.getCanonicalName(), "Fecha traducida a numero " + fecha_numerica);
		
		return fecha_numerica;
	}

	public static String  fromNumeric2Nominal  (String fecha)
	{
		String fecha_nominal = null;
		String mes = null;
		String dia = null;
		String anyo = null;
		String mes_nominal = null;
		
			
		
			Log.d(TraduceFecha.class.getCanonicalName(), "Fecha recibida en formato num�rico " + fecha);
		
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

			Log.d(TraduceFecha.class.getCanonicalName(), "Fecha traducida en formato nominal " + fecha_nominal);
		
		return fecha_nominal;
	}
}
