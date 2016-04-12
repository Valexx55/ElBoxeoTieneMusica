package com.val.ebtm.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.val.ebtm.myiconnapp.R;
import com.val.ebtm.utils.FechaUtil;

/**
 * Clase que genera la notifiación al usuario en caso de tener constancia de un nuevo podcast
 * disponible en el servidor
 *
 * @version 1.4
 * @author Val
 */
public class NotificaPodcastNuevoDisponible
{

    public static void lanzarNotificiacion (String fecha_programa_nuevo, Context context)
    {
        Log.d(NotificaPodcastNuevoDisponible.class.getCanonicalName(), "Lanzando notificación");

        String fecha_nominal = FechaUtil.fromNumeric2Nominal(fecha_programa_nuevo);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("¡Nuevo programa disponible!")
                        .setContentText(fecha_nominal)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL);

        Intent resultIntent = new Intent(context, ShowPodcastsActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity (context, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(537, mBuilder.build());//537 id al azar
    }

}
