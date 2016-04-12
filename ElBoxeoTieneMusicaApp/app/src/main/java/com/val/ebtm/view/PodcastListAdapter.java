package com.val.ebtm.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.annotation.SuppressLint;
import android.widget.Button;

import com.val.ebtm.controller.EscuchaEventosListener;
import com.val.ebtm.dto.ItemPodcastLogico;
import com.val.ebtm.myiconnapp.R;
import com.val.ebtm.utils.FechaUtil;

import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * @author VAL
 *
 * 17-03-2016 Parche para la 1.1.3 Había un problema al repintar el ListView de podcasts. Cuando un podcast se descargaba o se eliminaba, en ese momento,
 * sí se actualizaba el estado del botón correctamente (EscuchaEventosListener.actualizaEstadoFromDescargar/Eliminar). El problema residía que al desplazar la lista
 * algunos elementos podían repintarse, llamándose automáticamente entonces al método getView. Es ahí, donde la información lógica asociada al estado del botón
 * (seleccionable/no) debía estar actualizada, para que se repintara con los valores coherentes.
 *
 */
@SuppressLint("ViewHolder")
public class PodcastListAdapter extends ArrayAdapter<ItemPodcastLogico> {

	private List<ItemPodcastLogico> l_items_logicos;
	private int layoutIdItemFisico;
	private Context context;
	private EscuchaEventosListener ev;
	

	public PodcastListAdapter(Context context, int layoutIdItemFisico, int layaoutIdTextView, List<ItemPodcastLogico> l_items_logicos) {
		super(context, layoutIdItemFisico,layaoutIdTextView, l_items_logicos);

        this.layoutIdItemFisico = layoutIdItemFisico;
		this.context = context;
		this.l_items_logicos = l_items_logicos;
		this.ev = new EscuchaEventosListener(this.context);
	}



    private void initBotonState (Button boton, boolean seleccionable, OnClickListener listener)
	{
		if (seleccionable)
			{
				boton.setOnClickListener(listener);
			}
			else
				{
					boton.setClickable(false);
					boton.setAlpha(.5f);
				}
	}

    /**
     * El porqué de sobreescribir este método, es porque necesitamos una personalización de la vista de cada fila.
     * Si no lo sobreescribiésemos, la propia clase adapater, se limitaría a "inflar" los objetos visuales descritos
     * en el fichero item_podcast_original (para cada fila, el TextView de la fecha y los tres botones).
     *
     * En esta caso, necesitamos que un determinado botón esté activo o no y/o cambie de estado. Por lo que este método
     * se invoca cuando se representa cada item de la lista original y así mismo cuando se redibuja cualquier item
     * que cambia su condición de visibilidad.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View fila_nueva = null;
        ItemPodcastLogico item_podcast_logico = null;
        String fecha_nominal = null;
        Button boton_descargar = null;
        Button boton_eliminar = null;
        Button boton_reproducir = null;
        TextView texto_fecha = null;


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            fila_nueva = inflater.inflate(layoutIdItemFisico, parent, false);

            item_podcast_logico = l_items_logicos.get(position);

            Log.d(getClass().getCanonicalName(), "Posición = " + position);
            Log.d(getClass().getCanonicalName(), "Fecha = " + item_podcast_logico.getFecha());

            boton_reproducir = (Button) fila_nueva.findViewById(R.id.btn_reproducir);
            initBotonState(boton_reproducir, item_podcast_logico.isReproducir_disponible(), this.ev);

            boton_descargar = (Button) fila_nueva.findViewById(R.id.btn_download);
            initBotonState(boton_descargar, item_podcast_logico.isDescarga_disponible(), this.ev);

            boton_eliminar = (Button) fila_nueva.findViewById(R.id.btn_delete);
            initBotonState(boton_eliminar, item_podcast_logico.isEliminar_disponible(), this.ev);

            texto_fecha = (TextView) fila_nueva.findViewById(R.id.podcast_fecha_id);
            fecha_nominal = FechaUtil.fromNumeric2Nominal(item_podcast_logico.getFecha());
            texto_fecha.setText(fecha_nominal);


            boton_descargar.setTag(parent);//v 1.1.3 el botón es nuevo, y le asocio el padre. El hecho de elegir el boton descargar es arbitrario: en algún sitio debemos guardarlo. Esto es necesario porque cada vez, hay un botón distinto.
            parent.setTag(l_items_logicos);//y al padre, le asocio la lista con la info lógica asociada -esto debería hacerse una vez, pero si me lío a preguntar con ifs,, sale más caro el collar que el galgo
            //con estos dos set, tengo acceso a la lista lógica cuando toquen el botón y para cuando se repinte, esté actualizada
            //ver métodos EscuachaEventos actualizarEstadoFromDescargar/Eliminar
        
        return fila_nueva;
    }

}