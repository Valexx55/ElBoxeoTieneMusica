package com.val.ebtm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import com.val.ebtm.myiconnapp.R;

import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ShowPodcastsActivity extends AppCompatActivity {

	
	
	
	private ArrayList<ItemPodcast> hashMap2Array (Map<String, ItemPodcast> mapa_programas)
	{
		ArrayList<ItemPodcast> lista_podcasts = null;
		
		Iterator<Entry<String, ItemPodcast>> i = mapa_programas.entrySet().iterator();
		lista_podcasts = new ArrayList<ItemPodcast>(mapa_programas.size());

		while (i.hasNext())
		{
			Map.Entry<String, ItemPodcast> o_actual = (Entry<String, ItemPodcast>) i.next();
			lista_podcasts.add(o_actual.getValue());
		}
		
		return lista_podcasts;
	}
		
	private ArrayList<ItemPodcast> construirListaPodcasts (File[] lista_programas_mvl, String pgms_json_server)
	{
		ArrayList<ItemPodcast> lista_podcasts = null;
		String nombre_pgm = null;
		Map<String, ItemPodcast> lista_mezclada = null;
		
		/** PRIMERO RECORRO EL STRING, EXTRAYENDO LAS FECHAS Y COMPONGO TODOS LOS
		ITEM_PODCAST CON VALOR DES DESCARGA POSIBLE, PERO REPRODUCIR Y ELIMINAR A FALSE
		VOY PONIENDO LOS ITEMS EN EL MAP **/
		
		lista_mezclada = new TreeMap<String, ItemPodcast>();//Pq treemap? --> ordenado, y los ducplicados, se sobreescribirán ;) usando como clave la fecha
		
		if (null != pgms_json_server)
			
		{
			pgms_json_server = pgms_json_server.substring(1, (int)pgms_json_server.length()-1);//quito los corchetes
			String [] msjson_troc = pgms_json_server.split (",");//quito las comas, troceando cada fecha
			String fecha_pgm_aux = null;
			ItemPodcast itpodcasts = null;
			
			for (int i = 0; i < msjson_troc.length; i++) 
				{
				
					fecha_pgm_aux = msjson_troc[i].substring(1, msjson_troc[i].length()-1);//a cada fecha, le quito las comillas ;)
					itpodcasts = new ItemPodcast(fecha_pgm_aux, false, true, false);
					lista_mezclada.put(fecha_pgm_aux, itpodcasts);
				}
		
		}
		
		String str_aux = null;
		
		for (int i = 0; i < lista_programas_mvl.length; i++) 
			{
				str_aux = lista_programas_mvl[i].getName(); //cojo el nombre del fichero en curso
				nombre_pgm = str_aux.substring(0, (int)str_aux.length()-4);
				lista_mezclada.put(nombre_pgm, new ItemPodcast(nombre_pgm, true, false, true));
			}
		
		lista_podcasts = hashMap2Array (lista_mezclada);
		
		Log.d(this.getClass().getCanonicalName(), "Lista = " + lista_podcasts.toString());
		
		return lista_podcasts;
	}
	
	private String getDatesAviablesOnTheServer ()
	{
		String msj_json = null;
		
			try 
			{
				msj_json = new PedirListaPodcasts().execute().get();
				Log.d(this.getClass().getCanonicalName(), "Hemos recibido = " +msj_json);
			} 
			catch (InterruptedException e) 
			{
				Log.e (this.getClass().getCanonicalName(), e.getMessage());
			} 
			catch (ExecutionException e) 
			{
				Log.e (this.getClass().getCanonicalName(), e.getMessage());
			}
		
		return msj_json;
	}
	
	private File [] getFileListDeviceAviable ()
	{
		File [] lista_programas_local = null;
		Properties propiedades = null;
		String ruta_general = null;
		File directorio_raiz = null;
		
			try  
				{
					propiedades = new Properties();
					propiedades.load(this.getBaseContext().getAssets().open("config.properties"));
					ruta_general = Environment.getExternalStorageDirectory().getPath()+propiedades.getProperty("ruta_general"); //ruta donde se descargan, eliminan o reproducen 
				} 
			catch (IOException ex) 
				{
					Log.e (this.getClass().getCanonicalName(), ex.getMessage());
				}
			
			directorio_raiz = new File (ruta_general);//dentro de esa ruta
			lista_programas_local = directorio_raiz.listFiles();//saco todas las subcarpetas
		
		return lista_programas_local;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d (getClass().getCanonicalName()," Ha entrado en onCreate" );
	
		
		String lista_json_server_podcasts = null;
		File [] lista_programas_local = null;
		List<ItemPodcast> lista_item_podcasts = null;
		PodcastListAdapter adapter = null;
		ListView lista_podcasts = null;
		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_show_podcasts);
			
			
			if (UtilityNetwork.isNetworkAvailable(this))
			{
				
				if (!(UtilityNetwork.isWifiAvailable(this)))
				{
					FragmentManager fm = this.getFragmentManager();
			        DialogoWifi dialogo = new DialogoWifi();
			        dialogo.show(fm, "Aviso");
				}
				
				lista_json_server_podcasts = getDatesAviablesOnTheServer();//1 OBTENGO LISTA DE FECHAS DE PODCASTS DISPONIBLES EN EL SERVIDOR
				
			} else 
			{
				Log.d(this.getClass().getCanonicalName(), "No hay conexión a internet. No se puede recuperar la información remota");
				Toast.makeText(this, "SIN Conexión a Internet: Sólo se mostrará la lista de podcasts ya descargados en su móvil", Toast.LENGTH_LONG).show();
			}
			
			
			lista_programas_local = getFileListDeviceAviable();//2 OBTENGO LA LISTA DE PODCASTS DISPONIBLES LOCALMENTE
			lista_item_podcasts = construirListaPodcasts (lista_programas_local, lista_json_server_podcasts);/* 3.- UNA VEZ obtenidos las listas remotas y locales, hago un JOIN de ambas*/
			
			if (lista_item_podcasts.isEmpty())
			{
				Toast.makeText(this, "No hay podcasts disponibles. Active una conexión a Internet y pruebe de nuevo", Toast.LENGTH_LONG).show();
			}
			
			
			/* 4.- Una vez que tengo la lista definitiva, la dibujo */
			adapter = new PodcastListAdapter(ShowPodcastsActivity.this, R.layout.item_podcast_original, lista_item_podcasts);
			lista_podcasts = (ListView)findViewById(R.id.podcast_playlist);
			lista_podcasts.setAdapter(adapter);
			lista_podcasts.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//No dibujo ningún submenú dentro de esta actividad (superior derecha :::
		return true;
	}

}
