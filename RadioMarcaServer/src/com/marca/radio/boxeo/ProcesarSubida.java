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
 * Servlet implementation class ProcesarSubida
 */
@MultipartConfig
public class ProcesarSubida extends HttpServlet {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger("mylog");
	
	
	//TODO SACAR A UN PROPERTIES LA RUTA
	//TODO VALIDAR MP3, aunque sea sólo por la extensión
	private boolean guardarFichero (Part fichero, String nombrefichero, String nombredirectorio)
	{
		boolean ok=true;
		String ruta = null;
		
		//ruta = System.getProperty("catalina.home")+ File.separator + "programas" +File.separator +nombredirectorio;
		//ruta = System.getenv("OPENSHIFT_ENV_VAR")+File.separator +nombredirectorio;
		ruta = getServletContext().getInitParameter("ruta_programas");
		ruta = ruta+File.separator+ nombredirectorio;
		File dir = new File(ruta);
		File serverFile = new File(dir.getAbsolutePath() + File.separator + nombrefichero);
		dir.mkdirs(); //puede devolver false, si ya existe, no se crea de nuevo. Si existe y dentro hay un archivo con el mismo nombre se va a sobreescribir el fichero! (si se llama igual)
		
		byte[] buffer_read = new byte[4096];
		
		try (BufferedInputStream bis = new BufferedInputStream(fichero.getInputStream());BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(serverFile));) {
		
				int byteleido;
			        
				
			    while ((byteleido = bis.read(buffer_read))!=-1)
			        {
			        	bos.write(buffer_read, 0, byteleido);
			        }
			    
			    //log.info("Fichero fuardado en: " + serverFile.getAbsolutePath());
			    log.debug("Fichero guardado en: " + serverFile.getAbsolutePath());
			    
				} catch (Exception e) {
					
					/*StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					log.error(sw.toString());*/
					e.printStackTrace();
					ok = false;
				}
		
		
		return ok;
	}
	
	private static String obtenerNombreFichero(Part part) {
		String strDev = null;
		
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            strDev = fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return strDev;
	}
	
	/**
	 * Comprueba que el fichero subido tenga extensión .MP3 En realidad, lo suyo sería hacer una comprobación de formato interno, 
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//log.debug("Petición recibida.");
		log.debug("Petición recibida.");
		String date = request.getParameter("date"); //la fecha, dará nombre a la carpeta
		//String notificar = request.getParameter("notify"); //el usuario puede elegir si notificar o no
		//boolean hayQueNotificar = (notificar == null)? false:true; //traducimos ése valor a booleano. Si vale null, es que no ha clicado la opción. Si la clickó, valdrá true
		//log.debug("Ha entrado en el Procesar subida!");
		
		//log.debug("Fecha recibida = " + date);
		log.debug("Fecha recibida = " + date);
		
		Part filePart = request.getPart("file");
		String fileName = obtenerNombreFichero(filePart);
		
		if (formatoFicheroValido(fileName))
		{
			
			guardarFichero(filePart, fileName, date);
			//log.debug("Fichero almacenado !!");
			log.debug("Fichero almacenado !!");
			response.sendRedirect("exito.html");
			
			/*if (hayQueNotificar)
			{//ESTA PARTE DE MOMENTO, NI SE HACE NADA DE NADA
				log.trace("Debemos generar las notificaciones");
				//TODO Ir a la base de datos, leer los id's de los destinatarios, componer el mensaje y enviarlo GCM PART
			}*/
			
		}
		else response.sendRedirect("error.html");
		
		
		
		
		
	}

}
