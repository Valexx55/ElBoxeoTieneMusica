package com.marca.radio.boxeo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ObtenerPodcast
 */
@WebServlet("/ObtenerPodcast")
public class ObtenerPodcast extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger("mylog");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerPodcast() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private int localizaMp3 (File[] listaficheros, int pos)
    {
    	if (!listaficheros[pos].getName().endsWith(".mp3"))
    	{
    		pos = pos+1;
    		return (localizaMp3(listaficheros, pos));
    	} else return pos;
    }
    
    
    //esta función s un apaño. Al copiar un mp3 en una carpeta, windows incluye dos ficheros más, de tipo jpg
    //aquí, devolveremos la posición del mp3, para que sea ése y no otro el que se procedes
    private int buscaFicheroMp3 (File[] listaficheros)
    {
    	return (localizaMp3(listaficheros, 0));
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fecha = request.getParameter("fecha");
		
		OutputStream os = response.getOutputStream();
		response.setContentType("audio/mp3");
		String ruta = getServletContext().getInitParameter("ruta_programas")+File.separator +fecha;
		
		
		File carpeta = new File(ruta);
		
		File[] lista_programas = carpeta.listFiles();
		
		int pos_buena = buscaFicheroMp3(lista_programas);
		
		InputStream is = new FileInputStream(lista_programas[pos_buena]);
		
		int leido;
		leido = is.read();
		long total_leidos = 0;
		
		byte [] buffer_lectura_escritura = new byte[8192];
		
		while ((leido=is.read(buffer_lectura_escritura))!=-1)
				{
					os.write(buffer_lectura_escritura, 0, leido);
					total_leidos = total_leidos+leido;
				}
		log.debug("Total de bytes leídos = " + total_leidos);
		
		//response.setContentLength((int) total_leidos);
		response.setHeader("Content-Length", Long.toString(total_leidos));
		
		is.close();
		os.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
