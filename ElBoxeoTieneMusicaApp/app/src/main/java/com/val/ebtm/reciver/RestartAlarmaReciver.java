package com.val.ebtm.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.val.ebtm.controller.Alarma;
import com.val.ebtm.service.CheckNuevoPodcastDisponibleService;
import com.val.ebtm.utils.PrefrencesUsuario;


/**
 * Clase que escucha los reinicios del dispositivo, para proceder a activar el servicio que controla las notificaciones
 * en caso de estar activado en las preferencias del usuario
 *
 * @author Val
 * @version Disponible desde versión Notario
 *
 */
public class RestartAlarmaReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(getClass().getCanonicalName(), "El movil se ha reiniciado");

        if (PrefrencesUsuario.notificacionesActivas(context)) //si las notificaciones estaban activas, reinicio el servicio de check
        {
            Log.d(getClass().getCanonicalName(), "Reprogramando la alarma tras reinicio");

            String fecha_ultimo = PrefrencesUsuario.getFechaUltimoPodcast(context);

            Intent intent_serv = new Intent(context, CheckNuevoPodcastDisponibleService.class);
            intent_serv.putExtra("FECHA_ULTIMO_PODCAST", fecha_ultimo);

            context.startService(intent_serv);
        }

    }
}
