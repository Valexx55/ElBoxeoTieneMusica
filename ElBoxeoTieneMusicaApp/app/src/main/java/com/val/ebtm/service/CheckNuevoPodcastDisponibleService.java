package com.val.ebtm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.val.ebtm.controller.Alarma;
import com.val.ebtm.remote.ObtenerFechaUltimoPodcast;
import com.val.ebtm.utils.PrefrencesUsuario;
import com.val.ebtm.view.NotificaPodcastNuevoDisponible;


/**
 * Clase Servicio cuya misión princial es gestionar la respuesta ante una alarma y comprobar si se producen las condiciones
 * para soltar una nueva notificación al usuario. El propio servicio se tras cada comprobación y reprograma la alarma convenientemente
 *
 * @author  Val
 * @version Disponible desde versión Notario
 */
public class CheckNuevoPodcastDisponibleService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String fecha_ulimo_pasada = intent.getStringExtra("FECHA_ULTIMO_PODCAST");
        String fecha_serv_ulti = null;

            try {

                try
                {//si falla la llamada remota, ya tengo inicializado la fecha del últio y se reprogrma la alarma (no falla y sale)
                    fecha_serv_ulti = new ObtenerFechaUltimoPodcast().execute().get();
                }
                catch (Throwable t)
                {//la última fecha, es la que tengo guardada
                    Log.e(getClass().getCanonicalName(), "Error al obtener la fecha del ultimo podcast",t);
                }

                if (null == fecha_serv_ulti)
                    fecha_serv_ulti = PrefrencesUsuario.getFechaUltimoPodcast(this);

                Log.d(getClass().getCanonicalName(), "Fecha último podcast servidor = " + fecha_serv_ulti);
                Log.d(getClass().getCanonicalName(), "Fecha último pasada = " + fecha_ulimo_pasada);

                if (fecha_serv_ulti.compareTo(fecha_ulimo_pasada)>0)
                {
                    Log.d(getClass().getCanonicalName(), "Nuevo disponible. Lanzando notificación ");
                    NotificaPodcastNuevoDisponible.lanzarNotificiacion(fecha_serv_ulti, this);
                    PrefrencesUsuario.setFechaUltimoPodcastDisponibe(fecha_serv_ulti, this);
                    Alarma.programarAlarma(this);

                }
                else
                    {
                        Log.d(getClass().getCanonicalName(), "NO hay un Nuevo podcast disponible. Reprogramo");
                        Alarma.programarAlarma(this);
                    }

            }catch (Throwable t)
            {
                Log.e(getClass().getCanonicalName(), "ERROR aL EJECUTAR EL SERVICIO", t);
            }


        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getCanonicalName(), "Servicio detenido");
    }
}
