package com.marca.radio.boxeo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter El uso y siginificado de esta clase, queda reservado. Quien sepa leer código,  lleva premio! ;)
 */
@WebFilter("/ProcesarSubida")
public class FiltroClavel implements Filter {

   
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
	HttpServletRequest peticion = null;
	String clavel = null;
	
		peticion = (HttpServletRequest)request;
		clavel = peticion.getParameter("destino");
		if (clavel.equals("nrmnct29"))
			chain.doFilter(request, response);
		else 
		{
			HttpServletResponse respuesta = (HttpServletResponse)response;
			respuesta.sendRedirect("./");
		}
			
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
