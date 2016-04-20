package com.val.ebtm.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.val.ebtm.reciver.AlarmaReciver;
import com.val.ebtm.utils.FechaUtil;
import com.val.ebtm.utils.PrefrencesUsuario;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *Disponible desde la versión 5 de la aplicación.
 * Clase para programar y desprogramar las alarmas que dan soporte al servicio
 * de notificaciones incluído a partir de esta versión
 *
 * @author Vale
 * @version 1.0 Desde versión Notario
 *
 */
public class Alarma {


    public static final int ID_PROCESO_ALARMA = 8; //Id necesario para tener controlado cuando una alarma se programa y poder acceder a su cancelanción


    private static boolean haSalidoElprograma (Context context)
    {
        boolean ha_salido = false;

            ha_salido = (PrefrencesUsuario.getFechaUltimoPodcast(context).compareTo(FechaUtil.obtenerFechaActual())==0);

        return ha_salido;
    }

    private static void iniciarAlarma (long tiempo, Context context)
    {
        Intent intentAlarm = new Intent(context, AlarmaReciver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, ID_PROCESO_ALARMA, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, tiempo, pi);//TIempo, No es el tiempo que falta. Es el tiempo expresado en milisegundos equivalente a la fecha y hora del omento en que se quiere ejecutar

        //SOlo para test
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        calendar.setTimeInMillis(tiempo);
        calendar.getTime();

        SimpleDateFormat dateformatter = new SimpleDateFormat("E dd/MM/yyyy 'a las' hh:mm:ss");

        Log.d(Alarma.class.getCanonicalName(), "Alarma programada para "+ dateformatter.format(calendar.getTime()));
    }

    public static void programarAlarma (Context context)
    {
        int dias_qfaltan = 0;
        Calendar calendar_futuro = null;
        Calendar calendar_actual = null;
        long tiempo_proxima_alarma_ms = 0;


            calendar_actual = Calendar.getInstance();
            dias_qfaltan = FechaUtil.obtenerDiasHastaProximaEmision(calendar_actual);
            switch (dias_qfaltan)
            {
                case 0: //estamos en domingo. Caso especial
                    if (haSalidoElprograma(context))
                    {
                        //TODO reprogramar para la semana que viene
                        calendar_futuro = (Calendar)calendar_actual.clone();
                        calendar_futuro.add(Calendar.DAY_OF_WEEK, 7);
                        calendar_futuro.set(Calendar.HOUR_OF_DAY, FechaUtil.HORA_FIN_PGM);
                        calendar_futuro.set(Calendar.MINUTE, FechaUtil.MIN_FIN_PGM);
                        calendar_futuro.set(Calendar.SECOND, FechaUtil.SEC_FIN_PGM);
                        tiempo_proxima_alarma_ms = calendar_futuro.getTimeInMillis();

                    } else
                        {
                            //TODO reprogramar para dentro de media hora
                            tiempo_proxima_alarma_ms = calendar_actual.getTimeInMillis()+(FechaUtil.INTERVALO_MS_DIA_PGM);
                        }

                    break;
                default: //estamos en un día de lunes a sábado . caso general
                    calendar_futuro = (Calendar)calendar_actual.clone();
                    calendar_futuro.add(Calendar.DAY_OF_WEEK, dias_qfaltan);
                    calendar_futuro.set(Calendar.HOUR_OF_DAY, FechaUtil.HORA_FIN_PGM);
                    calendar_futuro.set(Calendar.MINUTE, FechaUtil.MIN_FIN_PGM);
                    tiempo_proxima_alarma_ms = calendar_futuro.getTimeInMillis();

            }

            iniciarAlarma(tiempo_proxima_alarma_ms, context);

    }

    public static void desprogramarAlarma (Context context)
    {

        Intent intentAlarm = new Intent(context, AlarmaReciver.class); //quizá se pueda elminar esto --> comprobar más tarde si es ncesario ocon e ID ya vale
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, ID_PROCESO_ALARMA, intentAlarm, PendingIntent.FLAG_NO_CREATE);
        alarmManager.cancel(pi);

        Log.d(Alarma.class.getCanonicalName(), "Alarma ANULADA");

    }



}
