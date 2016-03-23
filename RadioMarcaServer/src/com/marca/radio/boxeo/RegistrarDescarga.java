package com.marca.radio.boxeo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.val.ebtm.InfoDescarga;

/**
 * Servlet implementation class RegistrarDescarga
 */
@WebServlet(description = "Registro de la descarga de un podcast por parte de un usuario", urlPatterns = { "/RegistrarDescarga" })
public class RegistrarDescarga extends HttpServlet {
	
	private static final long serialVersionUID = 103L;
	
	private final static Logger log = Logger.getLogger("mylog");   
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		ObjectInputStream ois = null;
		InfoDescarga id = null;
		
			log.debug("Petición registro descarga recibida");
		
			ois = new ObjectInputStream(request.getInputStream());
			
			try 
			{
				id = (InfoDescarga)ois.readObject();
				log.info("Info Descarga recibida = " + id.toString());
				response.setStatus(HttpURLConnection.HTTP_CREATED);
				
				Calendar calendar = Calendar.getInstance();//añado la fecha actual del servidor
				calendar.add(Calendar.HOUR_OF_DAY, 5);//pero con la hora de España

				id.setMomento(calendar);//así podemos ir sabiendo a qué hora se descarga la gente los podcasts
								
				/**transformo a bytes el objeto para su serialización: el parámetro de la bd me pide un aray de bytes, luego uso 
				 * ByteArrayOutputStream
				 * 
				 */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(id);
				
				Connection cx = null;
				PreparedStatement pst = null;
				
				try 
				{

					cx = Pool.getConnection();
					pst = cx.prepareStatement("INSERT INTO DESCARGAS_PODCAST (descarga) VALUES (?)");
					pst.setBytes(1, baos.toByteArray());
					pst.execute();
					
				} 
				catch (Exception e) 
				{
					log.error("Error insertando en la base de datos registrando la descarga ", e);
				}
				finally 
				{
					Pool.liberarRecursos(cx, pst);
				}
				
				
			} catch (ClassNotFoundException e) 
			{
				log.error(e);
				response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			}
			
	}

}
