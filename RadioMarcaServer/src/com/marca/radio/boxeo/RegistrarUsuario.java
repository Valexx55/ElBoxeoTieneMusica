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
import com.val.ebtm.InfoUsuario;


/**
 * Servlet implementation class RegistrarUsuario
 */
@WebServlet(description = "Registra un Usuario de la App El Boxeo Tiene Música", urlPatterns = { "/RegistrarUsuario" })
public class RegistrarUsuario extends HttpServlet {
	
	
	private final static Logger log = Logger.getLogger("mylog");
	
	private static final long serialVersionUID = 1569L;
       

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		ObjectInputStream ois = null;
		InfoUsuario is = null;
		
			log.debug("Petición registro usuario recibida");
		
			ois = new ObjectInputStream(request.getInputStream());
			
			try 
			{
				is = (InfoUsuario)ois.readObject();
				log.info("Usuario recibido = " + is.toString());
				response.setStatus(HttpURLConnection.HTTP_CREATED);
				
				Calendar calendar = Calendar.getInstance();//añado la fecha actual del servidor
				calendar.add(Calendar.HOUR_OF_DAY, 5);//pero con la hora de España

				is.setMomento(calendar);
				
				/**transformo a bytes el objeto para su serialización: el parámetro de la bd me pide un aray de bytes, luego uso 
				 * ByteArrayOutputStream
				 * 
				 */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(is);
				
				Connection cx = null;
				PreparedStatement pst = null;
				
				try 
				{

					cx = Pool.getConnection();
					pst = cx.prepareStatement("INSERT INTO USUARIOS_APP (usuario) VALUES (?)");
					pst.setBytes(1, baos.toByteArray());
					pst.execute();
					
				} 
				catch (Exception e) 
				{
					log.error("Error insertando en la base de datos registrando el usuario ", e);
					response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
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
