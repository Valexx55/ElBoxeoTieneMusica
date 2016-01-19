package com.val.ebtm;

import java.util.List;

import com.val.ebtm.myiconnapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("ViewHolder")
public class PodcastListAdapter extends ArrayAdapter<ItemPodcast> {

	private List<ItemPodcast> items;
	private int layoutResourceId;
	private Context context;
	private EscuchaEventos ev;
	

	public PodcastListAdapter(Context context, int layoutResourceId, List<ItemPodcast> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		this.ev = new EscuchaEventos(this.context);
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AtomPaymentHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		
		
		row = inflater.inflate(layoutResourceId, parent, false);
		
		holder = new AtomPaymentHolder();
		holder.itemPodcast = items.get(position);
		holder.reproducirButton = (Button)row.findViewById(R.id.btn_reproducir);
		holder.reproducirButton.setTag(holder.itemPodcast);
		
		initBotonState(holder.reproducirButton, holder.itemPodcast.isReproducir_disponible(), this.ev);
		
		
		
		holder.descargarButton = (Button)row.findViewById(R.id.btn_download);
		holder.descargarButton.setTag(holder.itemPodcast);
		
		initBotonState(holder.descargarButton, holder.itemPodcast.isDescarga_disponible(), this.ev);
		
		
		holder.eliminarButton = (Button)row.findViewById(R.id.btn_delete);
		holder.eliminarButton.setTag(holder.itemPodcast);
		
		initBotonState(holder.eliminarButton, holder.itemPodcast.isEliminar_disponible(), this.ev);
		
		holder.fecha_pgm = (TextView)row.findViewById(R.id.podcast_fecha_id);
		holder.fecha_pgm.setText(holder.itemPodcast.getFecha());

		
		row.setTag(holder);

		return row;
	}

	public static class AtomPaymentHolder 
		{
			ItemPodcast itemPodcast;
			TextView fecha_pgm;
			Button descargarButton;
			Button reproducirButton;
			Button eliminarButton;
		}
}