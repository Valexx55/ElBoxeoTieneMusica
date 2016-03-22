package com.marca.radio.boxeo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.val.ebtm.InfoDescarga;
import com.val.ebtm.InfoUsuario;

/**
 * Servlet implementation class RegistrarDescarga
 */
@WebServlet(description = "Registro de la descarga de un podcast por parte de un usuario", urlPatterns = { "/RegistrarDescarga" })
public class RegistrarDescarga extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger("mylog");   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrarDescarga() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectInputStream ois = null;
		InfoDescarga id = null;
		
			log.debug("Petición registro desrcarga recibida");
		
			ois = new ObjectInputStream(request.getInputStream());
			
			try 
			{
				id = (InfoDescarga)ois.readObject();
				log.info("Info descarga = " + id.toString());
				response.setStatus(HttpURLConnection.HTTP_CREATED);
				
			} catch (ClassNotFoundException e) 
			{
				log.error(e);
				response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			}
	}

}
