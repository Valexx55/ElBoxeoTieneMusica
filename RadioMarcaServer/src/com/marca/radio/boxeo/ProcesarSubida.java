package com.marca.radio.boxeo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

/**
 * Servlet ProcesarSubida es la clase encargada de recoger los nuevos podcast por la interfaz web y almacenarlos en el servidor
 * para que queden a disposición de los usuarios de la aplicación móvil
 * 
 */
@MultipartConfig
public class ProcesarSubida extends HttpServlet {
	
	
	
	private static final long serialVersionUID = 2L;
	
	private final static Logger log = Logger.getLogger("mylog");
	
	private final static int TAMANIO_BLOQUE = 8192;;
	
	
	private boolean guardarFichero (Part fichero, String nombrefichero, String nombredirectorio)
	{
		boolean ok = true;
		String ruta = null;
		File dir = null;
		File serverFile = null;
		byte[] buffer_read = null; 
		
			ruta = getServletContext().getInitParameter("ruta_programas");
			ruta = ruta+File.separator+ nombredirectorio;
			dir = new File (ruta);
			serverFile = new File(dir.getAbsolutePath() + File.separator + nombrefichero);
			dir.mkdirs(); //puede devolver false, si ya existe, no se crea de nuevo. Si existe y dentro hay un archivo con el mismo nombre se va a sobreescribir el fichero! (si se llama igual)
			
			buffer_read = new byte[TAMANIO_BLOQUE];
			
			try (BufferedInputStream bis = new BufferedInputStream(fichero.getInputStream());BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(serverFile));) 
			{
			
				int byteleido = 0;
					
			    while ((byteleido = bis.read(buffer_read))!=-1)
			   	{
			    	bos.write(buffer_read, 0, byteleido);
			   	}
				    
			    log.info("Subida exitosa! Fichero guardado en: " + serverFile.getAbsolutePath());
				    
			} 
			catch (Exception e) 
			{
				log.error("Error al subir el podcast", e);
				ok = false;
			}
		
		
		return ok;
	}
	
	/**.
	 * En el mensaje HTTP, el nombre de lfichero, puede venir representado de varias formas (con espacios, barras, etc...)
	 * Lo que hacee esta función, es normalizar el nombre del fichero y extraerlo del mensaje http
	 * 
	 * @param part el/los ficheros adjuntos en la cabecera del mensjae http
	 * @return el nombre lógico que tendrá el fichero en el servidor
	 */
	private static String obtenerNombreFichero(Part part) {
		String strDev = null;
		
		    for (String cd : part.getHeader("content-disposition").split(";")) 
		    {
		        if (cd.trim().startsWith("filename")) 
		        {
		            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
		            strDev = fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); 
		        }
		    }
		    
		return strDev;
	}
	
	/**
	 * Comprueba que el fichero subido tenga extensión .MP3 En realidad, lo suyo seríaa hacer una comprobación de formato interno, 
	 * accediendo al contenido del fichero; pero hay bastante variedad de formatos; por lo que queda pendiente hasta que se defina
	 * un formato de audio específico de MP3
	 * 
	 * @param file_name el nombre del fichero
	 * @return true, si es un fichero MP3, false, en caso contrario.
	 */
	private boolean formatoFicheroValido (String file_name)
	{
		boolean bdev = false;
		
			bdev = file_name.substring((file_name.length()-4), (file_name.length())).equalsIgnoreCase(".mp3");
		
		return bdev;
	}
	
	/**
	 * Ya que se va a modificar el estado interno del servidor, atendemos la petición por un POST
	 * Recogemos el fichero y la fecha relativa al mismo podcast
	 * Parseamos el cuerpo de mensaje y lo grabamos en el sistema de ficheros del servidor
	 * 
	 * La ruta de destino será el nombre de fichero, precedido por una subcarpeta con la fecha en formando AAAA-MM-DD, dentro
	 * de una ruta destinada a tal fin, definida como parámetro del Servlet
	 * 
	 * Si la transferencia finaliza con éxito, se redirigee a una página informativa
	 * En caso contrario, se dirige a una página de error
	 * 
	 *  
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String date = null;
		Part filePart = null;
		String fileName = null;
		
			date = request.getParameter("date"); //la fecha, dará nombre a la carpeta
			
			log.info("Petición subida con fecha " + date);
			
			filePart = request.getPart("file");
			fileName = obtenerNombreFichero(filePart);
			
			log.info("Nombre del fichero recibido = " + fileName);
			
			if (formatoFicheroValido(fileName))
			{
				if (guardarFichero(filePart, fileName, date))
				{
					log.debug("Fichero almacenado !!");
					response.sendRedirect("exito.html");
				}
				else
				{
					log.debug("Error guardando el fichero :(");
					response.sendRedirect("error.html");
				}
					
			}
				
			else
			{
				log.info("Hubo un error en la subida. Redirijo a error");
				response.sendRedirect("error.html");
			}
			
	}

}
