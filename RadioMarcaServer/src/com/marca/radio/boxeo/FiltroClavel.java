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
 * Servlet Filter implementation class FiltroClavel
 */
@WebFilter("/ProcesarSubida")
public class FiltroClavel implements Filter {

    /**
     * Default constructor. 
     */
    public FiltroClavel() {
        // TODO Auto-generated constructor stub
    }

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
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		HttpServletRequest peticion = (HttpServletRequest)request;
		String clavel = peticion.getParameter("destino");
		if (clavel.equals("nrmnct29")) //si no ha introducido bien la clave, 
			chain.doFilter(request, response);
		else 
		{
			HttpServletResponse respuesta = (HttpServletResponse)response;
			respuesta.sendRedirect("./");//le redirijo a la página de inicio
		}
			
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
