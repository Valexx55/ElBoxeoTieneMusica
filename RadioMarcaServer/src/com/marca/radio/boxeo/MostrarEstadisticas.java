package com.marca.radio.boxeo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.val.ebtm.InfoUsuario;

/**
 * Servlet implementation class MostrarEstadisticas
 */
@WebServlet("/MostrarEstadisticas")
public class MostrarEstadisticas extends HttpServlet {
	private static final long serialVersionUID = 105L;
	
	
	private final static Logger log = Logger.getLogger("mylog");
	
	
	private InfoUsuario componerInfoUsuario (ResultSet rs)
	{
		InfoUsuario infousuario = null;
		/**
		 * // Se obtiene el campo blob
   Blob blob = rs.getBlob("objeto");

   // Se reconstruye el objeto con un ObjectInputStream
   ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
   DatoGrande dato = (DatoGrande) ois.readObject();
		 */
			try 
			{
				Blob blob = rs.getBlob("usuario");
				
				ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
				
				infousuario = (InfoUsuario) ois.readObject();
				
			} catch (Exception e) 
			{
				log.error("Error al desserealizar el BLob en objeto InfoUsuario");
			}
		
		return infousuario;
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conexion = null;
		Statement st = null;
		ResultSet rs = null;
		List<InfoUsuario> lista_usuarios = null;
		
		
		
			log.debug("Entrando en MostrarEstadísticas");
			try 
			{
				conexion = Pool.getConnection();
				st = conexion.createStatement();
				rs = st.executeQuery("SELECT * FROM USUARIOS_APP");
				
				log.debug("Conexión y consulta realizadas. Recuperando objetos");
				
				lista_usuarios = new LinkedList<InfoUsuario>();
				InfoUsuario is_aux = null;
				
				while (rs.next())
				{
					is_aux = componerInfoUsuario(rs);
					lista_usuarios.add(is_aux);
					
				}
				
				log.debug("Fin desarialización");
				
			} catch (Exception e) 
			{
				log.error("Error recuperando objetos de la base de datos ", e);
				
			} finally 
			{
				Pool.liberarRecursos(conexion, st, rs);
				
			}
			
			
			request.setAttribute("lista_usuarios", lista_usuarios);
					
			request.getRequestDispatcher("mostrarestadisticas.jsp").forward(request, response);
	}

}
