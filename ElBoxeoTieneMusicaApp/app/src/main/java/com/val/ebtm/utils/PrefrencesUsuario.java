package com.val.ebtm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.val.ebtm.InfoUsuario;

/**
 * Created by vale on 28/03/16.
 *
 * Claseo que encapsula los datos y operaciones relativos a los ficheros de preferencias
 *
 * @author Val
 * @version Disponible desde versión Notario (1.1.4). Mejora en la estructuración del código
 */
public class PrefrencesUsuario {

    public final static String CLAVE_FECHA_ULTIMO_PODCAST = "fecha_ultimo_podcast";
    public final static String CLAVE_ALARMA_PODCASTS = "alarma_check_podcasts";//controlador, para saber si es la primera vez
    public final static String CLAVE_ALARMA_ACTIVADA = "alarma_activada";//para saber el estado de la alarma
    public final static String PREFERENCES_USUARIO = "perfil";//nombre del fichero de preferences perfil.xml será


    public static boolean usuarioRegistrado(Context context)
    {
        boolean usuario_registrado = false;

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            usuario_registrado = sharedPreferences.getBoolean(InfoUsuario.CLAVE_PREFERENCES, false);

        return usuario_registrado;

    }

    public static boolean notificacionesInicializadas(Context context)
    {
        boolean notificacion_init = false;

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            notificacion_init = sharedPreferences.getBoolean(PrefrencesUsuario.CLAVE_ALARMA_PODCASTS, false);

        return notificacion_init;
    }

    public static boolean notificacionesActivas(Context context)
    {
        boolean noti_ON = false;

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            noti_ON = sharedPreferences.getBoolean(PrefrencesUsuario.CLAVE_ALARMA_ACTIVADA, false);

        return noti_ON;
    }

    public static void setEstadoNotificaciones(boolean estado, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PrefrencesUsuario.CLAVE_ALARMA_ACTIVADA, estado);
        editor.commit();

    }

    public static void setUsuarioRegistrado (boolean estado, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(InfoUsuario.CLAVE_PREFERENCES, true);
        editor.commit();

    }

    public static void setFechaUltimoPodcastDisponibe(String fecha, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PrefrencesUsuario.CLAVE_FECHA_ULTIMO_PODCAST, fecha);
        editor.commit();

    }

    public static void setNotificacionesInicializadas(boolean not_activadas, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PrefrencesUsuario.CLAVE_ALARMA_PODCASTS, not_activadas);
        editor.commit();

    }

    public static String getFechaUltimoPodcast(Context context)
    {
        String fecha_ultimo = null;

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            fecha_ultimo = sharedPreferences.getString(PrefrencesUsuario.CLAVE_FECHA_ULTIMO_PODCAST, "");

        return fecha_ultimo;
    }
}
