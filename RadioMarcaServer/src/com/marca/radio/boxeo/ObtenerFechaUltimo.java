package com.marca.radio.boxeo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;


/**
 * Servlet implementation class ObtenerFechaUltimo
 */
@WebServlet("/ObtenerFechaUltimo")
public class ObtenerFechaUltimo extends HttpServlet {
	private static final long serialVersionUID = 918L;
	
	private final static Logger log = Logger.getLogger("mylog");

	
	private String obtenerFechaUltimo() 
	{
		String str_fecha_mayor = null;
		String ruta = null;
		String str_aux = null;
		File dir = null;
		File[] lista_programas = null;
		File carpeta = null;
		
			ruta = getServletContext().getInitParameter("ruta_programas");//saco la ruta de la carpeta raíz
			log.debug("Ruta = " + ruta);
			
			dir = new File (ruta);
			carpeta = new File(dir.getAbsolutePath());
			lista_programas = carpeta.listFiles();//saco todas las subcarpetas, donde a su vez, estarán los programas
			
			str_fecha_mayor = "0"; //INIICALIZo _> Este será el mayor
			
			//log.debug("Mayor = " + str_fecha_mayor);
			for (File fichero : lista_programas)//OJO, precondición importante: mantener en la ruta_programs, sólo carpetas de programas! y nada más
			{
				str_aux = fichero.getName();
				if (str_aux.compareTo(str_fecha_mayor)>0) //si str_aux es mayor, pasa a ser la mayor fecha (el podcast más antiguo ;)
				{
					str_fecha_mayor = str_aux;
				}
				//log.debug("Mayor = " + str_fecha_mayor);
			}
			
			log.debug("Mayor al final = " + str_fecha_mayor);
			
		return str_fecha_mayor;
	}

	/**
	 * Devolvemos la fecha del podcast más reciente presente en el servidor en formato plano
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String str_fecha_ultimo = null;
		PrintWriter pw = null;
		
			log.info("Entrando en ObtenerFechaUltimo");
			
			response.setContentType(MediaType.TEXT_PLAIN);
			str_fecha_ultimo = obtenerFechaUltimo ();
			pw = response.getWriter();
			pw.write(str_fecha_ultimo);
			response.setStatus(HttpURLConnection.HTTP_OK);
			
			log.info("Mayor devuelto = " + str_fecha_ultimo);
		
	}


}
