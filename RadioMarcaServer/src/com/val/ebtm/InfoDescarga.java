package com.val.ebtm;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vale on 18/03/16.
 */
public class InfoDescarga implements Serializable
{
    private static final long serialVersionUID = 579868457300286134L;
    
    private InfoUsuario info_usuario;
    private String fecha; //esto viene seteado en el cliente, id la fecha del podacast solicitado
    private String momento; //el momento en que se produce la petición
    
    
    public String getMomento() {
		return momento;
	}

    public String getFecha ()
    {
    	return fecha;
    }
    

	public void setMomento(Calendar calendar) 
    {
		DateFormat dateFormat = null;
		
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			momento = dateFormat.format(calendar.getTime());
    }
	

    public InfoDescarga(InfoUsuario info_usuario, String fecha)
    {
        this.info_usuario = info_usuario;
        this.fecha = fecha;
    }

    
    @Override
    public String toString()
    {
        return (this.info_usuario.toString() + " Fecha = " +fecha);
    }
}
