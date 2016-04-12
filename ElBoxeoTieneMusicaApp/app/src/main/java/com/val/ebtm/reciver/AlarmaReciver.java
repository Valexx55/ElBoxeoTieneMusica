package com.val.ebtm.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.val.ebtm.service.CheckNuevoPodcastDisponibleService;
import com.val.ebtm.utils.PrefrencesUsuario;

/**
 * Clase que escucha la ejecución/el salto de una alarma programada e invoca al service correspondiente
 *
 * @author  Val
 * @version Disponible desde versión Notario
 */

public class AlarmaReciver extends BroadcastReceiver {


    public AlarmaReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d(getClass().getCanonicalName(), "Alarma ejecutándose");
        Log.d(getClass().getCanonicalName(), "Lanzando el servicio");

        String fecha_ultimo = PrefrencesUsuario.getFechaUltimoPodcast(context);

        Intent intent_serv = new Intent(context, CheckNuevoPodcastDisponibleService.class);
        intent_serv.putExtra("FECHA_ULTIMO_PODCAST", fecha_ultimo);

        context.startService(intent_serv);

    }
}
