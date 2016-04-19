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

import com.val.ebtm.InfoDescarga;
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

	
	private InfoDescarga componerInfoDescarga (ResultSet rs)
	{
		InfoDescarga infodescarga = null;
		
			try 
			{
				Blob blob = rs.getBlob("descarga");
				
				ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
				
				infodescarga = (InfoDescarga) ois.readObject();
				
			} catch (Exception e) 
			{
				log.error("Error al desserealizar el BLob en objeto InfoDescarga");
			}
		
		return infodescarga;
		
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
		List<InfoDescarga> lista_descargas = null;
		
		
			log.debug("Entrando en MostrarEstadísticas");
			try 
			{
				conexion = Pool.getConnection();
				st = conexion.createStatement();
				rs = st.executeQuery("SELECT * FROM USUARIOS_APP");
				
				log.debug("Conexión y consulta realizadas. Recuperando usuarios");
				
				lista_usuarios = new LinkedList<InfoUsuario>();
				InfoUsuario is_aux = null;
				
				while (rs.next())
				{
					is_aux = componerInfoUsuario(rs);
					lista_usuarios.add(is_aux);
					
				}
				
				log.debug("Fin desarialización usuarios");
				
			} catch (Exception e) 
			{
				log.error("Error recuperando usuarios de la base de datos ", e);
				
			} finally 
			{
				Pool.liberarRecursos(conexion, st, rs);
				
			}
			
			//AHORA RECUPERAMOS LA INFO DE LAS DESCARGAS
			try 
			{
				conexion = Pool.getConnection();
				st = conexion.createStatement();
				rs = st.executeQuery("SELECT * FROM DESCARGAS_PODCAST");//la teoría de que hibernate no termina de molar tanto: necesito nombre de las tablas y columnas en el código JAVA
				
				log.debug("Conexión y consulta realizadas. Recuperando descargas");
				
				lista_descargas = new LinkedList<InfoDescarga>();
				InfoDescarga is_descarga = null;
				
				while (rs.next())
				{
					is_descarga = componerInfoDescarga(rs);
					lista_descargas.add(is_descarga);
					
				}
				
				log.debug("Fin desarialización descargas");
				
			} catch (Exception e) 
			{
			
				log.error("Error recuperando descargas de la base de datos ", e); //aquí deberíamos redirigir a página de error, abortando el resto de la ejecución
				
			} finally 
			{
				Pool.liberarRecursos(conexion, st, rs);
				
			}
			
			
			MapDescargas mapa_descargas = new MapDescargas(lista_descargas);
			
			request.setAttribute("lista_usuarios", lista_usuarios);
			request.setAttribute("mapa_descargas", mapa_descargas.getMapa_descargas());
					
			request.getRequestDispatcher("mostrarestadisticas.jsp").forward(request, response);
	}

}
