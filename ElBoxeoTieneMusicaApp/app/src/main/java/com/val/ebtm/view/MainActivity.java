package com.val.ebtm.view;

import android.accounts.AccountManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import com.val.ebtm.controller.Alarma;
import com.val.ebtm.controller.EscuchaEventosListener;
import com.val.ebtm.dto.FactoryUsuario;
import com.val.ebtm.InfoUsuario;
import com.val.ebtm.remote.ObtenerFechaUltimoPodcast;
import com.val.ebtm.utils.FechaUtil;
import com.val.ebtm.utils.PrefrencesUsuario;
import com.val.ebtm.remote.RegistrarUsuario;
import com.val.ebtm.myiconnapp.R;


/**
 * Clase principal
 *
 * HISTÓRICO DE VERSIONES
 *
 * 1.0.0 Versión inicial - VANILA
 * 1.1.2 TYPEX
 * 1.1.3 STAT
 * 1.1.4 NOTARIO
 *
 *
 * Cambios versión 1.1.4 ---> Se añade el menú con la barra de acciones Twitter, Ajustes y Compartir por Whatsapp
 * Se añade también el ajuste inicial de las notificaciones
 *
 * Cambios versión 1.1.3 ---> Se incluye el registro de los usuarios
 */
public class MainActivity extends AppCompatActivity
{

    private final static String TWITTER_PGM = "https://twitter.com/EbtmPodcast";
    private final static String MENSAJE_COMPARTIR = "¡Descárgate esta app! - https://play.google.com/store/apps/details?id=com.val.ebtm.myiconnapp";


    private void iniciarNotificaciones ()
    {
        String fecha_mayor = null;
        try
        {
                fecha_mayor = new ObtenerFechaUltimoPodcast().execute().get();

        }catch (Throwable t)
        {
             //en el caso de fallar, deberíamos iniciar fecha mayor a la fecha de hoy?
             Log.e(getClass().getCanonicalName(), "Error al inicializar fecha ultimo podcast",t);
        }

        if (null == fecha_mayor)
            fecha_mayor = FechaUtil.obtenerFechaActual();//la primera vez, es necesario saber qué fecha es la útima disponible, para inicializar        //los avisos

        PrefrencesUsuario.setFechaUltimoPodcastDisponibe(fecha_mayor, this);
        Log.d (getClass().getCanonicalName(),"Fecha inicializada ultima = " + PrefrencesUsuario.getFechaUltimoPodcast(this));
        Alarma.programarAlarma(this);

        PrefrencesUsuario.setEstadoNotificaciones(true, this);
        PrefrencesUsuario.setNotificacionesInicializadas(true, this);
    }

    private String[] crearDirectorios () {
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


    private void registrarUsuario ()
    {
        try
        {
            if (!PrefrencesUsuario.usuarioRegistrado(this))
            {
                //defino una factoría aparte para facilatar la serialiación en el servidor
                //y no arrastrar dependencias de otras clases Acount Manager, etc.
                InfoUsuario info_usuario = FactoryUsuario.crearInfoUsuario((AccountManager) getSystemService(ACCOUNT_SERVICE));

                String cod_registro = new RegistrarUsuario().execute(info_usuario).get();
                if (cod_registro.equals(RegistrarUsuario.REGISTRO_OK))
                {
                    PrefrencesUsuario.setUsuarioRegistrado(true, this);
                }
            }

        } catch (Exception ex)
        {
            Log.e(getClass().getCanonicalName(), ex.getMessage());
        }

    }


    private void setAlarmaNotificaciones() {

        if (!PrefrencesUsuario.notificacionesInicializadas(this))
        {
            Log.d(getClass().getCanonicalName(), "Alarma NO inicializada");
            iniciarNotificaciones();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Button boton_show_podcasts =  (Button)findViewById(R.id.botonMostrarProgramasDisponilbes);
        Activity a = this;
        EscuchaEventosListener escuchaeventos = new EscuchaEventosListener(a);
        boton_show_podcasts.setOnClickListener(escuchaeventos);
        crearDirectorios();
        registrarUsuario();
        setAlarmaNotificaciones();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.main, menu);//Para que se reprsente el submenú superior

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = null;

        switch (item.getItemId()) {
            case R.id.boton_cerrar:
                Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado a salir. Saliendo!...");
                finish();
                break;

            case R.id.boton_ajustes:
                Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado ajustes");
                i = new Intent(this, AjustesActivity.class);
                startActivity(i);

                break;

            case R.id.boton_twitter:
                Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado twitter");

                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(TWITTER_PGM));
                startActivity(i);

                break;

            case R.id.boton_compartir:
                Log.d(this.getClass().getCanonicalName(), "El usuario le ha dado compartir");
                i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, MENSAJE_COMPARTIR);
                i.setType("text/plain");
                i.setPackage("com.whatsapp");
                startActivity(i);

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
