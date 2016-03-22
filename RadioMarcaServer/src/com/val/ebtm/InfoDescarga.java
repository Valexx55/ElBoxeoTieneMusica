package com.val.ebtm;

import com.val.ebtm.InfoUsuario;

import java.io.Serializable;

/**
 * Created by vale on 18/03/16.
 */
public class InfoDescarga implements Serializable
{
    private static final long serialVersionUID = 579868457300286134L;
    private InfoUsuario info_usuario;
    private String fecha;

    public InfoDescarga(InfoUsuario info_usuario, String fecha)
    {
        this.info_usuario = info_usuario;
        this.fecha = fecha;
    }

    @Override
    public String toString()
    {
        return (this.info_usuario.toString() + " Fecha = " +fecha);
    }
}
