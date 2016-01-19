package com.val.ebtm;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.val.ebtm.myiconnapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

	
	
	private String[] crearDirectorios ()
	{
		String [] rutas = new String[2];
		String ruta_general = null;
		String ruta_ultimo = null;
		
		try 
			{
				Properties propiedades2 = new Properties();
				propiedades2.load(getAssets().open("config.properties"));
				ruta_general = Environment.getExternalStorageDirectory().getPath()+propiedades2.getProperty("ruta_general"); //ruta donde se descargan, eliminan o reproducen 
				ruta_ultimo = Environment.getExternalStorageDirectory().getPath()+propiedades2.getProperty("ruta_ultimo"); //ruta donde se descargan, eliminan o reproducen
				File directorio_general = new File (ruta_general);
				File directorio_ultimo = new File (ruta_ultimo);
				
				if (!directorio_general.exists())
					if (directorio_general.mkdirs())
						Log.d(getClass().getCanonicalName(), "Directorio creado");
			
				if (!directorio_ultimo.exists())
					if (directorio_ultimo.mkdirs())
						Log.d(getClass().getCanonicalName(), "Directorio creado");
					
				rutas [0] = ruta_general;
				rutas [1] = ruta_ultimo;
			
			} catch (IOException ex) 
			{
				Log.e(getClass().getCanonicalName(), ex.getMessage());
			}
		
		return rutas;
		
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		Button boton_show_podcasts =  (Button)findViewById(R.id.botonMostrarProgramasDisponilbes);
		Activity a = this;
		EscuchaEventos escuchaeventos = new EscuchaEventos(a);
		boton_show_podcasts.setOnClickListener(escuchaeventos);
		crearDirectorios ();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.boton_cerrar:
			Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado a salir. Saliendo!...");
			finish();
			break;

		case R.id.boton_ajustes:
			Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado ajustes");
			Intent i = new Intent(this, Ajustes.class);
			startActivity(i);
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
