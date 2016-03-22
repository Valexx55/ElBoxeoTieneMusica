package com.val.ebtm;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vale on 17/03/16.
 */
public class InfoUsuario implements Serializable
{


    private static final long serialVersionUID = 979868457300286134L;
    public final static String CLAVE_PREFERENCES = "Registrado";
    public final static String ID_CUENTA_GOOGLE = "com.google";

    private String[] lista_emails;
    
    private String momento;


    public InfoUsuario ()
    {

    }

    
    private String seraliza_mails (String [] lista_mails)
    {
    	String str_dev = "";
    	
	    	for (String mail: lista_mails)
	        {
	            str_dev = str_dev + " - " + mail;
	        }
    	
    	return str_dev.substring(3);
    	
    }
    
    public String getLista_emails() {
		return seraliza_mails(this.lista_emails);
	}



	public String getMomento() {
		return momento;
	}


	public void setMomento(Calendar calendar) 
    {
		DateFormat dateFormat = null;
		
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			momento = dateFormat.format(calendar.getTime());
    }

	
    public void setLista_emails(String[] lista_emails) 
    {
        this.lista_emails = lista_emails;
    }

    @Override
    public String toString() {
        String str_dev = " ";
        
        	str_dev = seraliza_mails(this.lista_emails);

        return str_dev;

    }
}
