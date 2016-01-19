package com.val.ebtm;

public class ItemPodcast {
	
	
	private String fecha;
	private boolean reproducir_disponible;
	private boolean descarga_disponible;
	private boolean eliminar_disponible;
	
	
	
	public ItemPodcast(String fecha, boolean reproducir_disponible,
			boolean descarga_disponible, boolean eliminar_disponible) {
		super();
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
