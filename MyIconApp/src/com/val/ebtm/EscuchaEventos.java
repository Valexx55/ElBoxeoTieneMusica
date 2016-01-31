package com.val.ebtm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import com.val.ebtm.myiconnapp.R;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EscuchaEventos implements OnClickListener{
	
	private Context actividad_padre;
	
	public EscuchaEventos(Context a) {
		actividad_padre = a;
	}
	
	
	public void actualizarEstadoFromDescargar (Button boton_descargar)
	{
		boton_descargar.setClickable(false);
		boton_descargar.setAlpha(.5f);
		LinearLayout layaout_padre = (LinearLayout)boton_descargar.getParent();
		Button boton_reproducir = (Button) layaout_padre.getChildAt(0);//en teoría, este es el botón de reproducir
		boton_reproducir.setOnClickListener(this);
		boton_reproducir.setAlpha(1);
		Button boton_eliminar = (Button) layaout_padre.getChildAt(2);//en teoría, este es el botón de eliminar
		boton_eliminar.setOnClickListener(this);
		boton_eliminar.setAlpha(1);
	}
	
	private void actualizarEstadoFromEliminar (Button boton_eliminar)
	{
		boton_eliminar.setClickable(false);
		boton_eliminar.setAlpha(.5f);
		LinearLayout layaout_padre = (LinearLayout)boton_eliminar.getParent();
		Button boton_reproducir = (Button) layaout_padre.getChildAt(0);//en teoría, este es el botón de reproducir
		boton_reproducir.setClickable(false);
		boton_reproducir.setAlpha(.5f);
		Button boton_descargar = (Button) layaout_padre.getChildAt(1);//en teoría, este es el botón de reproducir
		boton_descargar.setOnClickListener(this);
		boton_descargar.setAlpha(1);
	}
	/**
	 * 
	 * @param v el botón seleccionado
	 * @return la fecha asociada al botón seleccionado
	 */
	private String obtenerFechaPadre (View v)
	{
		String fecha_actual_nominal = null;
		String fecha_numerica = null;
		
			LinearLayout layaout_padre = (LinearLayout)v.getParent().getParent();
			
			Log.d(this.getClass().getCanonicalName(), "Layautpadre nombre = " +layaout_padre.getClass().getName());
			
			TextView texto = (TextView)layaout_padre.getChildAt(0);
	
			fecha_actual_nominal = (String) texto.getText();
			
			fecha_numerica = TraduceFecha.fromNominal2Numeric(fecha_actual_nominal);//nueva
			
			Log.d(this.getClass().getCanonicalName(), "fecha actual = " + fecha_actual_nominal);
			Log.d(this.getClass().getCanonicalName(), "fecha numerica = " + fecha_numerica);
		
		return fecha_numerica;
	}

	/**
	 * Esto en realidad debería ser un método de otra clase u otro clase, pero bueno, lo ponemos aquí de momento
	 */
	private void reproducir (String ruta, Activity actividad)
	{
		
		try {
			
			
			Log.d(this.getClass().getCanonicalName(), "Boton PLay --> ID de objeto = "+ this.hashCode());

			
			Intent intent = new Intent();  
			intent.setAction(android.content.Intent.ACTION_VIEW);
			Log.d("Sonando?", ruta);
			File file = new File(ruta);  
			intent.setDataAndType(Uri.fromFile(file), "audio/mp3");  
			actividad_padre.startActivity(intent);
			}
			catch (Exception e)
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				Log.e("Error" , sw.toString());
			}
		
	}
	
	@Override
	public void onClick(View v) {
		
		
		switch (v.getId()) {
		
		case R.id.botonMostrarProgramasDisponilbes:
			
			
				Log.d(this.getClass().getCanonicalName(), "mostrar podcasts");
				

				Intent i = new Intent(actividad_padre, ShowPodcastsActivity.class);
				actividad_padre.startActivity(i);
			
		break;
		
		/*case R.id.boton_aviso_ok:
			
			//Context vc = this.actividad_padre.getApplicationContext();
			Context vc = this.actividad_padre.getApplicationContext().getApplicationContext();
			
			Activity actividad_aviso = (Activity)this.actividad_padre;
			actividad_aviso.finish();
			
			
			Intent i2 = new Intent(vc, ShowPodcastsActivity.class);
			vc.startActivity(i2);
			
			
		break;*/
			
		default: //de momento, gestionamos aquí todos los clicks sobre los podcasts...tal vez sería más apropiado una clase aparte
		
			Log.d(this.getClass().getCanonicalName(), "Ha tocado alguno de los botones Reproducir/Eliminar/Descargar");
			
			//Obtengo información que será necesaria en cualquier caso: qué botón ha tocado y de qué fecha? --> qué botón ha tocado exactamente
			Button boton_pulsado = (Button)v;
			String fecha = null;
			fecha = obtenerFechaPadre(boton_pulsado);
			Properties propiedades2 = new Properties();
			String ruta_general = null;
			try {

				propiedades2.load(((Activity)this.actividad_padre).getBaseContext().getAssets().open("config.properties"));
				ruta_general = Environment.getExternalStorageDirectory().getPath()+propiedades2.getProperty("ruta_general"); //ruta donde se descargan, eliminan o reproducen 

		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
			
			switch (boton_pulsado.getText().toString()) 
				{
				case "Reproducir":
						Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado REPRODUCIR");
						String ruta_fichero_reproducir = null;
					    ruta_fichero_reproducir = ruta_general+"/"+fecha+".mp3";
					    Log.d(this.getClass().getCanonicalName(), "Ruta reproducir = " + ruta_fichero_reproducir);
						reproducir(ruta_fichero_reproducir, (Activity)this.actividad_padre);
					
					break;
				
				case "Descargar":
						Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado DESCARGAR");
						
						if (UtilityNetwork.isNetworkAvailable(this.actividad_padre))
						{
							
							long dr = 0;
							IntentFilter filter = null;
							BroadcastReceiver receiver = null;
							String url = "http://ebtm-ebtm.rhcloud.com/RadioMarcaServer/ObtenerPodcast?fecha="+fecha;
							
							/**
							 * DIBUJAR EL PROCESS DIALOG
							 */
							ProgressDialog mProgressDialog = new ProgressDialog(this.actividad_padre);
							mProgressDialog.setMessage("Descargando podcast . . .");
							mProgressDialog.setIcon(R.drawable.ic_launcher);
							mProgressDialog.setTitle("Accediendo al servidor");
							mProgressDialog.setIndeterminate(true);
							mProgressDialog.setCancelable(false);
							mProgressDialog.show();
							
							//registro la clase que será invocada tras realizarse la descarga
							filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
							receiver = new NotificaReciver(0, this.actividad_padre, this, boton_pulsado, mProgressDialog);
							this.actividad_padre.registerReceiver(receiver, filter);
							
							//seteo el downloadManager para invocarlo
							DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
							request.setDescription("EBTM " + fecha);
							request.setTitle("Podcast EBTM ");
							
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
								{
								    request.allowScanningByMediaScanner();
								    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								}
							String ruta_completa_fichero = ruta_general+"/"+fecha+".mp3";
							Uri uri_dest = Uri.fromFile(new File(ruta_completa_fichero));
							request.setDestinationUri(uri_dest);
							
							DownloadManager manager = (DownloadManager) this.actividad_padre.getSystemService(Context.DOWNLOAD_SERVICE);
							//y lo llamo
							dr = manager.enqueue(request);
							//me quedo con el número id de descarga, que se usará en la clase invocad al concluir la descarga
							((NotificaReciver)receiver).setId_descarga(dr);
							
							Log.d(this.getClass().getCanonicalName(), "Descarga iniciada con con id = " + dr);
						
						}
						else 
							{
								Log.d(this.getClass().getCanonicalName(), "No hay conexión a internet. No se puede recuperar la información remota");
								Toast.makeText(this.actividad_padre, "SIN Conexión a Internet. Imposible iniciar la descarga", Toast.LENGTH_LONG).show();
							
							}
						
					
					break;
				
				case "Eliminar":
		
						Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado ELIMINAR");
						
						String ruta_delete = ruta_general + "/" + fecha + ".mp3"; //la extensión debería ser parametrizada como constante de alguna forma tb ;(
						File f_borrar = new File (ruta_delete);
						f_borrar.delete();
						
						Toast.makeText(actividad_padre,	"Archivo eliminado del dispositivo", Toast.LENGTH_SHORT).show();
						
						actualizarEstadoFromEliminar(boton_pulsado);
					break;
			}
			
		}
		
	}
	
}
