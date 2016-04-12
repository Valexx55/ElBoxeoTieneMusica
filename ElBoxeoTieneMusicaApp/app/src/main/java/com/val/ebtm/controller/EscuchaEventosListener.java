package com.val.ebtm.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import com.val.ebtm.dto.FactoryUsuario;
import com.val.ebtm.InfoDescarga;
import com.val.ebtm.InfoUsuario;
import com.val.ebtm.dto.ItemPodcastLogico;
import com.val.ebtm.myiconnapp.R;
import com.val.ebtm.reciver.DescargaCompletaPodcastReciver;
import com.val.ebtm.remote.ObtenerFechaUltimoPodcast;
import com.val.ebtm.remote.RegistrarDescarga;
import com.val.ebtm.utils.PrefrencesUsuario;
import com.val.ebtm.utils.FechaUtil;
import com.val.ebtm.utils.UtilityNetwork;
import com.val.ebtm.view.ShowPodcastsActivity;

import android.accounts.AccountManager;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase "listener", que responde a los eventos derivados de la acción del usuario con la interfaz de la aplicación.
 * Actualmente, se maneja el negocio, las acciones en sí, dentro de esta clase. Para la siguiente version, esta clase
 * debería esstrictamente ser de la parte del controlador e invocar a acciones de negocio en otra clase. Se apoya en la
 * clase evento.
 *
 * @see Evento
 *
 * @author Val
 *
 * @version 1.3  Correción Deregistro del Reciver al finalizar el servicio de desgarca
 *
 * @version Notario Mejora e interacción con la clase evento. Actualización de la fecha del último podcast disponible al mostrar la lista
 * de programas (para complementar el servicio de Notificaciones). Inclusión de la gestión del selector activar/desactivar notificaciones.
 * Inversión de la lista de podcast, para mostrar de mayor a menor (más fácil para el usuario acceder al último publicado).
 *
 *
 *
 */

public class EscuchaEventosListener implements OnClickListener{
	
	private Context actividad_padre;
	
	public EscuchaEventosListener(Context a) {
		actividad_padre = a;
	}
	

    private static ItemPodcastLogico obtenerItemPorFecha (List<ItemPodcastLogico> l_items_logicos, String fecha)
    {
        ItemPodcastLogico item_podcast_logico = null;
        ItemPodcastLogico item_podcast_logico_aux = null;
        boolean encontrado = false;
        int i = 0;

            while (!encontrado)
            {
                item_podcast_logico_aux = l_items_logicos.get(i);
                encontrado = item_podcast_logico_aux.getFecha().equals(fecha);
                i = i + 1;
            }

            item_podcast_logico = item_podcast_logico_aux;

        return item_podcast_logico;
    }

	public void actualizarEstadoFromDescargar (Button boton_descargar)
	{

		boton_descargar.setClickable(false);
		boton_descargar.setAlpha(.5f);
		LinearLayout layaout_padre = (LinearLayout)boton_descargar.getParent();


		Button boton_reproducir = (Button) layaout_padre.getChildAt(0);//en teor�a, este es el bot�n de reproducir
		boton_reproducir.setOnClickListener(this);
		boton_reproducir.setAlpha(1);
		Button boton_eliminar = (Button) layaout_padre.getChildAt(2);//en teor�a, este es el bot�n de eliminar
		boton_eliminar.setOnClickListener(this);
		boton_eliminar.setAlpha(1);


        //obtengo la lista padre asociada al botón y de ella, la lista lógica de estados, que toca actuallizar
        //para que la próxima vez que se use en getView al repintarse, esté su estado actualizado
        ViewGroup parent = (ViewGroup)boton_descargar.getTag();
        List<ItemPodcastLogico> l_items_logicos = (List<ItemPodcastLogico>)parent.getTag();
        String fecha = obtenerFechaPadre(boton_descargar);
        ItemPodcastLogico item_podcast_logico = obtenerItemPorFecha(l_items_logicos, fecha);

        item_podcast_logico.setReproducir_disponible(true);
        item_podcast_logico.setEliminar_disponible(true);
        item_podcast_logico.setDescarga_disponible(false);

	}
	
	private void actualizarEstadoFromEliminar (Button boton_eliminar)
	{
		boton_eliminar.setClickable(false);
		boton_eliminar.setAlpha(.5f);
		LinearLayout layaout_padre = (LinearLayout)boton_eliminar.getParent();
		Button boton_reproducir = (Button) layaout_padre.getChildAt(0);//en teor�a, este es el bot�n de reproducir
		boton_reproducir.setClickable(false);
		boton_reproducir.setAlpha(.5f);
		Button boton_descargar = (Button) layaout_padre.getChildAt(1);//en teor�a, este es el bot�n de reproducir
		boton_descargar.setOnClickListener(this);
		boton_descargar.setAlpha(1);

        //obtengo la lista padre asociada al botón y de ella, la lista lógica de estados, que toca actuallizar
        //para que la próxima vez que se use en getView al repintarse, esté su estado actuazliado
        ViewGroup parent = (ViewGroup)boton_descargar.getTag();
        List<ItemPodcastLogico> l_items_logicos = (List<ItemPodcastLogico>)parent.getTag();
        String fecha = obtenerFechaPadre(boton_descargar);
        ItemPodcastLogico item_podcast_logico = obtenerItemPorFecha(l_items_logicos, fecha);

        item_podcast_logico.setReproducir_disponible(false);
        item_podcast_logico.setEliminar_disponible(false);
        item_podcast_logico.setDescarga_disponible(true);

	}
	/**
	 * 
	 * @param v el bot�n seleccionado
	 * @return la fecha asociada al bot�n seleccionado
	 */
	private String obtenerFechaPadre (View v)
	{
		String fecha_actual_nominal = null;
		String fecha_numerica = null;
		
			LinearLayout layaout_padre = (LinearLayout)v.getParent().getParent();
			
			Log.d(this.getClass().getCanonicalName(), "Layautpadre nombre = " +layaout_padre.getClass().getName());
			
			TextView texto = (TextView)layaout_padre.getChildAt(0);
	
			fecha_actual_nominal = (String) texto.getText();
			
			fecha_numerica = FechaUtil.fromNominal2Numeric(fecha_actual_nominal);//nueva
			
			Log.d(this.getClass().getCanonicalName(), "fecha actual = " + fecha_actual_nominal);
			Log.d(this.getClass().getCanonicalName(), "fecha numerica = " + fecha_numerica);
		
		return fecha_numerica;
	}

	/**
	 * Esto en realidad deber�a ser un m�todo de otra clase u otro clase, pero bueno, lo ponemos aqu� de momento
	 */
	private void reproducir (String ruta)
	{
		
		try {
			
			
			Log.d(this.getClass().getCanonicalName(), "Boton PLay --> ID de objeto = "+ this.hashCode());

			
			Intent intent = new Intent();  
			intent.setAction(Intent.ACTION_VIEW);
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

        //TODO Este código, aunque estructurado con buena lógica, queda muy largo. Hay que externalizar las acciones, a métodos nuevos
        //clases de negocio y /o servicio, donde se desarrollen las acciones y no queden aquí enmarañadas.

        //Obtengo informaci�n que ser� necesaria en cualquier caso: qu� bot�n ha tocado y de qu� fecha? --> qu� bot�n ha tocado exactamente

        Button boton_pulsado = null;
        String fecha = null;

        Properties propiedades2 = new Properties();
        String ruta_general = null;

        try {

            propiedades2.load(((Activity)this.actividad_padre).getBaseContext().getAssets().open("config.properties"));
            ruta_general = Environment.getExternalStorageDirectory().getPath()+propiedades2.getProperty("ruta_general"); //ruta donde se descargan, eliminan o reproducen

        } catch (IOException ex) {
            Log.e(getClass().getCanonicalName(), "Error al leer fichero propiedades", ex);
        }

        Evento evento = Evento.traduceEvento(v);

		switch (evento) {
		
            case LISTAR_PODCASTS_DISPONIBLES:

                    Log.d(this.getClass().getCanonicalName(), "mostrar podcasts");

                    Intent i = new Intent(actividad_padre, ShowPodcastsActivity.class);
                    actividad_padre.startActivity(i);

            break;

            case ACTIVAR_DESACTIVAR_NOTIFICACIONES:

                CheckBox checkBox = (CheckBox)v;
                Log.d(getClass().getCanonicalName(), "Ha tocado el check boton aviso");

                boolean activado = checkBox.isChecked();
                if (activado)
                {
                    Log.d(getClass().getCanonicalName(), "El botón está activo");

                    try {
                        //debemos obtener la última fecha y guardarla
                        String fecha_ultimo = null;
                        fecha_ultimo = new ObtenerFechaUltimoPodcast().execute().get();
                        if (null != fecha_ultimo)
                            PrefrencesUsuario.setFechaUltimoPodcastDisponibe(fecha_ultimo, this.actividad_padre);
                        //y hay que programar la alarma
                        Alarma.programarAlarma(this.actividad_padre);
                    }
                    catch (Throwable t)
                    {
                        Log.e(getClass().getCanonicalName(), "Error al actualizar fecha último podcast disponible", t);
                    }
                }

                else //desactiva la alarma
                {
                    Alarma.desprogramarAlarma(this.actividad_padre);
                }
                PrefrencesUsuario.setEstadoNotificaciones(activado, this.actividad_padre);

            break;
		


            case REPRODUCIR_PODCAST:
                    boton_pulsado = (Button)v;
                    fecha = obtenerFechaPadre(boton_pulsado);
                    Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado REPRODUCIR");
                    String ruta_fichero_reproducir = null;
                    ruta_fichero_reproducir = ruta_general+"/"+fecha+".mp3";
                    Log.d(this.getClass().getCanonicalName(), "Ruta reproducir = " + ruta_fichero_reproducir);
                    reproducir(ruta_fichero_reproducir);
                break; //de momento, gestionamos aqu� todos los clicks sobre los podcasts...tal vez ser�a m�s apropiado una clase aparte

            case DESCARGAR_PODCAST:
                Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado DESCARGAR");
                boton_pulsado = (Button)v;
                fecha = obtenerFechaPadre(boton_pulsado);

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
                    /** OJO IMPORTANTÏSIMO LUEGO DE-REGISTRAR EL RECIVER PORQUE SI NO, CUANDO
                     * ACABE LA SEGUNDA DESCARGA, SE INVOCARÁ DOS VECES EL NOTIFICA RECIVER
                     * VER _ NOTIFICA_RECIVER
                     */
                    filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    receiver = new DescargaCompletaPodcastReciver(this, boton_pulsado, mProgressDialog);
                    this.actividad_padre.registerReceiver(receiver, filter);

                    //seteo el downloadManager para invocarlo
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("EBTM " + fecha);
                    request.setTitle("Podcast EBTM " + fecha);

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
                    //me quedo con el n�mero id de descarga, que se usar� en la clase invocad al concluir la descarga
                    ((DescargaCompletaPodcastReciver)receiver).setId_descarga(dr);

                    InfoUsuario infoUsuario = FactoryUsuario.crearInfoUsuario((AccountManager) this.actividad_padre.getSystemService(Context.ACCOUNT_SERVICE));
                    InfoDescarga infoDescarga = new InfoDescarga(infoUsuario, fecha);

                    try
                    {
                        String cod_registro = new RegistrarDescarga().execute(infoDescarga).get();

                    } catch (Exception ex)
                    {
                        Log.e(getClass().getCanonicalName(), ex.getMessage());
                    }

                    Log.d(this.getClass().getCanonicalName(), "Descarga iniciada con con id = " + dr);

                }
                else
                {
                    Log.d(this.getClass().getCanonicalName(), "No hay conexión a internet. No se puede recuperar la información remota");
                    Toast.makeText(this.actividad_padre, "SIN Conexión a Internet. Imposible iniciar la descarga", Toast.LENGTH_LONG).show();

                }


                break;

            case ELIMINAR_PODCAST:
                    Log.d(this.getClass().getCanonicalName(), "++++++Ha tocado ELIMINAR");

                    boton_pulsado = (Button)v;
                    fecha = obtenerFechaPadre(boton_pulsado);

                    String ruta_delete = ruta_general + "/" + fecha + ".mp3"; //la extensi�n deber�a ser parametrizada como constante de alguna forma tb ;(
                    File f_borrar = new File (ruta_delete);
                    f_borrar.delete();

                    Toast.makeText(actividad_padre,	"Archivo eliminado del dispositivo", Toast.LENGTH_SHORT).show();

                    actualizarEstadoFromEliminar(boton_pulsado);
                break;

		}
	}
}
