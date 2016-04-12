package com.val.ebtm.view;

import com.val.ebtm.controller.EscuchaEventosListener;
import com.val.ebtm.utils.PrefrencesUsuario;
import com.val.ebtm.myiconnapp.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;

/**
 * Actividad que muestra el menú con un elemento check, donde el usuario
 * puede indicar si desea activar o desactivar el servicio de notificaciones
 *
 * @version Entra en funcionamiento desde la versión Notario (1.1.4)
 */
public class AjustesActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_aviso);

        //obtengo el estado de selección
        boolean b_aux = PrefrencesUsuario.notificacionesActivas(this);
        Log.d(getClass().getCanonicalName(), "Notificaciones activadas = " + b_aux);

        //lo pinto
        checkBox.setChecked(b_aux);
        checkBox.setOnClickListener(new EscuchaEventosListener(this));

    }

}
