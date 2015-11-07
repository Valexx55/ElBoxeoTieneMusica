package com.marca.radio.boxeo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class ObtenerFechaUltimo
 */
@WebServlet("/ObtenerFechaUltimo")
public class ObtenerFechaUltimo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerFechaUltimo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		TreeSet<String> lista_directorios = null;
		String ruta = getServletContext().getInitParameter("ruta_programas");
		String lista_json = null;
		
		File carpeta = new File(ruta);
		
		File[] lista_programas = carpeta.listFiles();
		
		
		
		lista_directorios = new TreeSet<String>();
		if (lista_programas.length >0)
		{
			String nombre_dir_actual = null;
			for (File file : lista_programas) {
				if (file.isDirectory())
				{
					nombre_dir_actual = file.getName();
					lista_directorios.add(nombre_dir_actual);
				}
			}
		}
		
		String ultima_fecha = lista_directorios.last(); //en teoría, esto devuelve el mayor, que en nuestro caso será el último programa
		
		/*Gson gson = new Gson();
		lista_json = gson.toJson(ultima_fecha);
		
		System.out.println(lista_json);*/
		
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.print(ultima_fecha);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
