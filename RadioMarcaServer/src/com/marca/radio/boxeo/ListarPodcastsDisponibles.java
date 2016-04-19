package com.marca.radio.boxeo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.google.gson.Gson;

/**
 * Servlet ListarPodcastDisponibles sirve para atender peticiones remotas sobre qué podcasts hay disponibles en el lado del servidor
 * La petición por tanto, la atenderá get y deuelve un mensaje en formato json que contiene las fechas de los programas disponilbes
 * 
 */
public class ListarPodcastsDisponibles extends HttpServlet {
	
	
	
	private static final long serialVersionUID = 3L;
	
	private final static Logger log = Logger.getLogger("mylog");
	
	
	/**
	 * Función privada que recibe el listado de carpetas de podcast presentes en el servidor y devuelve una lista con el nombre de cada carpeta (fecha de cada programa)
	 * 
	 * @param lista_programas listado de carpetas presentes en el servidor
	 * @return Una lista de Strings, en la que cada elemento, es el nombre de la carpeta (fecha del progrma)
	 */
	private List<String> listarDirectorios (File[] lista_programas)
	{
		List<String> lista_directorios = null;
		
			lista_directorios = new LinkedList<String>();
			if (lista_programas.length > 0)
			{
				String nombre_dir_actual = null;
				for (File file : lista_programas) 
				{
					if (file.isDirectory())
					{
						nombre_dir_actual = file.getName();
						lista_directorios.add(nombre_dir_actual);
					}
				}	
			}
			
			/*else //no hay programas, devuelvo una lista vacía (!=null) 
				{
				
				}*/
		
		return lista_directorios;
		
	}
	
	
	/**
	 * Petición Get que responde a la petición de ¿qué podcasts hay disponibles en el servidor? usando para ello un formato JSON
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
	
		String lista_json = null;
		List<String> lista_directorios = null;
		String ruta = null;
		File dir = null;
		Gson gson = null;
		PrintWriter pw = null;

		
			ruta = getServletContext().getInitParameter("ruta_programas");
			log.debug("Ruta = " + ruta);
			
			log.info("Petición recibida listar podcasts");
			
			dir = new File (ruta);
		
			File carpeta = new File(dir.getAbsolutePath());
			File[] lista_programas = carpeta.listFiles();
			
			lista_directorios = listarDirectorios(lista_programas);
			
			gson = new Gson();
			lista_json = gson.toJson(lista_directorios);
			
			log.info("Respuesta Lista JSON  = " + lista_json);
			log.debug("Número de progrmas = " + lista_directorios.size());
			
			resp.setContentType("application/json");
			
			
			pw = resp.getWriter();
			pw.print(lista_json);
		
			resp.setStatus(HttpURLConnection.HTTP_OK);
	}
}
