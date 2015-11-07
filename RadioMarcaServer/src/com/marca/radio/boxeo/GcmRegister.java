package com.marca.radio.boxeo;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GcmRegister
 */
@WebServlet("/GcmRegister")
public class GcmRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GcmRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

    /***
     * hay un pequeño problema: cuando un suuario desinstala la aplicación, no tenemos manera de ser notificados en el propio
     * dispositivo, por lo que es imposible "desregistrarnos" en nuestro servidor y evitar así intentar enviar notificaciones
     * a un usuario que no puede recibirlas porque hay eliminado la app
     * 
     * Parece ser , que GCM, te devuelve un mensaje donde te informa si ese id ya no está activo, respondiendo con un
     * NotRegistered. Queda pendiente el análisis y la solución para tratar a los usuarios dergistrados, mediante
     * el análisis de la respuesta del servidor GCM a los envíos.
     */
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Aquí recibimos en el cuerpo de la petición, el id del cliente que debemos procesar
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		BufferedReader br = request.getReader();
		String id_recibido = br.readLine();
		System.out.println("HEmos recibido " + id_recibido);
		
		Pool pool = Pool.getInstance();
		Connection conexion = pool.getConnection();
		try {
			Statement st = conexion.createStatement();
			st.execute("INSERT INTO USERS_BOX_GMC (ID_GCM, ACTIVADO) VALUES ('"+id_recibido+"', 1)");
			Pool.liberarRecursos(conexion, st);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
