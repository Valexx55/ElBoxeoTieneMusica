package com.marca.radio.boxeo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * Servlet implementation class ProcesarSubida
 */
public class ListarPodcastsDisponibles extends HttpServlet {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger("mylog");
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//TODO recuperar todos los directorios disponbles y devolverlos en un XML o en JSON?
		
		String lista_json = null;
		List<String> lista_directorios = null;
		String ruta = getServletContext().getInitParameter("ruta_programas");
		File dir = new File (ruta);
		log.debug("Ruta = " + ruta);
		
		
		//GET PROGRAMMES'S FOLDER
		
		File carpeta = new File(dir.getAbsolutePath());
		File[] lista_programas = carpeta.listFiles();
		
		//PROCCESS EACH ITEM
		
		lista_directorios = new LinkedList<String>();
		if (lista_programas.length >0)
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
		else 
			{
				//devolvemos un listado vacío
			}
		
		//BUILD UP THE GSON RESPONSE MESSAGE
		Gson gson = new Gson();
		lista_json = gson.toJson(lista_directorios);
		
		log.debug(lista_json);
		
		resp.setContentType("application/json");
		PrintWriter pw = resp.getWriter();
		pw.print(lista_json);
		
		
	}
}
