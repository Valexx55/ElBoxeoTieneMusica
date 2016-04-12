package com.val.ebtm.dto;


/**
 *
 * Clase que agrupa y almacena en tiempo de ejecución, el estado lógico de un item de la lista de podcast, necesaria para representar
 * de forma coherente un botón de cada Elemento/Item: Descarga, Reproducción, Eliminación
 *
 * @see com.val.ebtm.view.ShowPodcastsActivity
 * @see com.val.ebtm.view.PodcastListAdapter
 *
 * @author Val
 * @version 1.3
 */
public class ItemPodcastLogico
{
	
	
	private String fecha;
	private boolean reproducir_disponible;
	private boolean descarga_disponible;
	private boolean eliminar_disponible;
	
	
	
	public ItemPodcastLogico(String fecha, boolean reproducir_disponible, boolean descarga_disponible, boolean eliminar_disponible) {

        this.fecha = fecha;
		this.reproducir_disponible = reproducir_disponible;
		this.descarga_disponible = descarga_disponible;
		this.eliminar_disponible = eliminar_disponible;
	}


    public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public boolean isReproducir_disponible() {
		return reproducir_disponible;
	}


	public void setReproducir_disponible(boolean reproducir_disponible) {
		this.reproducir_disponible = reproducir_disponible;
	}


	public boolean isDescarga_disponible() {
		return descarga_disponible;
	}


	public void setDescarga_disponible(boolean descarga_disponible) {
		this.descarga_disponible = descarga_disponible;
	}


	public boolean isEliminar_disponible() {
		return eliminar_disponible;
	}


	public void setEliminar_disponible(boolean eliminar_disponible) {
		this.eliminar_disponible = eliminar_disponible;
	}
	

	

}
