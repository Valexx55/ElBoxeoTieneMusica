package com.val.ebtm.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.val.ebtm.myiconnapp.R;

/**
 * Clase que representa los eventos que pueden darse en la aplicación
 *
 * Además, proporciona un ḿétodo que traduce, desde el objeto visual presionado
 * por el usuario, la acción lógica o el evento que se debe realizar
 *
 * La legibilidad, y por tanto, la mantenibilidad y en definita la calidad del código
 * aumentan notablemente con la codificación de esta clase
 *
 * @author  Val.
 * Fecha 11-Abril-16
 *
 * Comentario extra: La visión de los eventos, como tipo de actividad, en perspectiva java, podría traducirse como anotación,
 * pudiendo dar lugar a su vez a un framework donde tuviera Acciones (clases que hagan el negocio)
 * bajo determinados Eventos (parametrizados en forma de parámetros en el interior de las antotaciones),
 * dando lugar a una especie de Framework al estilo Struts. Esta idea, se descarta, por estar en contra
 * de desarrollar entornos encorsetados, que sólo cohartan la libertad de programadores con criterio y ayuda
 * a programadores mediocres, que nunca deberían aspirar a dedicarse a esto.
 *
 * Aunque pudiera valer para un desarrollo académico posterior, se indica que en este Framework-Dictadura
 * habría, elementos "Clikeables" (anotaciones sobre objetos visuales) que desembocarían en la ejecución
 * de elementos "Accionables" - o ejecutables, que serían las líneas de código a ejecutar consecuencia
 * de tocar el objeto clickeable (tradicionalmente, "modelo o negocio")
 *
 * NOTA_2: se excluye de esta parte, por legibilidad para las prácticas docentes, los elementos
 * y las acciones de los menús de las actividades, que quedan disparados e interpretados en el propio
 * código de las activities
 *
 *@version Notario
 */
public enum Evento {LISTAR_PODCASTS_DISPONIBLES,
                    ACTIVAR_DESACTIVAR_NOTIFICACIONES,
                    REPRODUCIR_PODCAST,
                    DESCARGAR_PODCAST,
                    ELIMINAR_PODCAST;

    public static Evento traduceEvento (View vista)
    {
        Evento evento_ocurrido = null; // en función de qué obejto visual (v ha sido pulsado, traduzco la operación a realizar
        int id_vista = 0;
        Button boton_pulsado = null;


            //Log.d(Evento.class.getCanonicalName(), R.string.accion_descargar);
            id_vista = vista.getId();
            switch (id_vista) {
                case R.id.botonMostrarProgramasDisponilbes: evento_ocurrido = LISTAR_PODCASTS_DISPONIBLES; break;
                case R.id.checkBox_aviso: evento_ocurrido = ACTIVAR_DESACTIVAR_NOTIFICACIONES; break;
                default:
                        Context context = vista.getContext();
                        boton_pulsado = (Button)vista;
                        String title_aux = boton_pulsado.getText().toString();
                        if (title_aux.equals(context.getString (R.string.accion_descargar)))
                            evento_ocurrido = DESCARGAR_PODCAST;
                        else if (title_aux.equals(context.getString (R.string.accion_reproducir)))
                                evento_ocurrido = REPRODUCIR_PODCAST;
                            else if (title_aux.equals(context.getString (R.string.accion_eliminar)))
                                    evento_ocurrido = ELIMINAR_PODCAST;
                    //else Throw Exception...
            }

        return evento_ocurrido;
    }
}