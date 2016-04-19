package com.marca.radio.boxeo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.val.ebtm.InfoDescarga;
import com.val.ebtm.TraduceFecha;

public class MapDescargas {
	
	
	
	private Map<String, Integer> mapa_descargas;

	
	public Map<String, Integer> getMapa_descargas() {
		return mapa_descargas;
	}

	public void setMapa_descargas(Map<String, Integer> mapa_descargas) {
		this.mapa_descargas = mapa_descargas;
	}
	
	
	
	
	public MapDescargas (List<InfoDescarga> lista_descargas)
	{
		
		String fecha = null;
		Integer value_aux = null;
		String fecha_nominal = null;
		
			this.mapa_descargas = new HashMap<String, Integer>();
			
			for (InfoDescarga id : lista_descargas) 
			{
				fecha = id.getFecha();
				
				fecha_nominal = TraduceFecha.fromNumeric2Nominal(fecha);
				
				if (mapa_descargas.containsKey(fecha_nominal))
				{
					value_aux = mapa_descargas.get(fecha_nominal);
					value_aux++;//Integer admite el operador ++ nolosabía ;)
					mapa_descargas.put(fecha_nominal, value_aux);
				}
					
				else
				{
					mapa_descargas.put(fecha_nominal, 1);
				}
				
			}
		
	}

}
