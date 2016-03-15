package com.marca.radio.boxeo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet que atiende las peticiones de los clientes para transferirles una copia de un podcast concreto
 * identificado por la fecha del programa
 * 
 */
@WebServlet("/ObtenerPodcast")
public class ObtenerPodcast extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger log = Logger.getLogger("mylog");
	
	private final static int TAMANIO_BLOQUE = 8192;
       
   
	
	/**
	 * Función recursiva auxiliar que localiza la posición del archivo con extensión mp3 en un array de ficheros
	 * 
	 * @param listaficheros el contenido de la carpeta
	 * @param pos la posición del mp3 en el conjunto de listaficheros
	 * @return pos
	 */
    private int localizaMp3 (File[] listaficheros, int pos)
    {
    	if (!listaficheros[pos].getName().endsWith(".mp3"))
    	{
    		pos = pos+1;
    		return (localizaMp3(listaficheros, pos));
    			
    	} 
    	else return pos;
    }
	
	
	/**
	 * Esta función es un apaño. Viene motivada porque al copiar un fichero mp3 en una carpeta, se incluye dos ficheros más, de tipo jpg; por lo que
	 * se hace necesario identificar la posición del mp3 para su ulterior procesamiento
     * 
	 * @param listaficheros La lista de ficheros a procesar
	 * @return la posición del mp3
	 */
	 
    private int buscaFicheroMp3 (File[] listaficheros)
    {
    	return (localizaMp3(listaficheros, 0));
    }
	
	
   

	/**
	 * Método que recibe una fecha como parámetro, la cual identifica la carpeta que contiene al podcast que el cliente quiere descargar, 
	 * localiza dicho fichero en el sistema de ficheros del servidor y devuelve en mensaje HTTP, adjuntando en
	 * el cuerpo el contenido explícito del fichero mp3 demandado. 
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fecha = null;
		String ruta = null;
		File carpeta = null;
		File[] lista_programas = null;
		int pos_mp3 = 0;
		InputStream is = null;
		int leido = 0;
		long total_leidos = 0;
		byte [] buffer_lectura_escritura = null;
		
		

		 	//TODO se debería controlar la entrada, para que nadie pudiera pasar una fecha falsa o incorrecta
		 	//así como controlar la transferencia y sus posibles errores de E/S
		 	//y devolver en sendos casos, los mensajes de error correspondientes
		 	
			fecha = request.getParameter("fecha");
			
			log.info("Petición recibida "+ fecha);
			
			ruta = getServletContext().getInitParameter("ruta_programas")+File.separator +fecha;
			carpeta = new File(ruta);
			lista_programas = carpeta.listFiles();
			pos_mp3 = buscaFicheroMp3(lista_programas);
			is = new FileInputStream(lista_programas[pos_mp3]);
			
			
			buffer_lectura_escritura = new byte[TAMANIO_BLOQUE];
			OutputStream os = response.getOutputStream();
			response.setContentType("audio/mp3");
			
			while ((leido=is.read(buffer_lectura_escritura))!=-1)
					{
						os.write(buffer_lectura_escritura, 0, leido);
						total_leidos = total_leidos+leido;
					}
			
			log.info("Total de bytes leídos = " + total_leidos);
			response.setHeader("Content-Length", Long.toString(total_leidos));
			
			is.close();
			os.flush();
	}

}
