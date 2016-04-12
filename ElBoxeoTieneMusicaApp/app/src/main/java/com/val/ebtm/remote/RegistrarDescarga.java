package com.val.ebtm.remote;


import android.os.AsyncTask;
import android.util.Log;

import com.val.ebtm.InfoDescarga;

import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase que registra en el servidor cuándo un usuario se descarga un podcast
 *
 * @version 1.1.3 Incluída desde la generación de estadísticas
 */
public class RegistrarDescarga extends AsyncTask<InfoDescarga, Integer, String> {

    public static final String REGISTRO_OK = "OK";
    public static final String REGISTRO_KO = "KO";

	@Override
	protected String doInBackground(InfoDescarga... infoDescargas)
    {
        String str_dev = REGISTRO_KO;
        URL serverUrl = null;
        HttpURLConnection httpCon = null;
        ObjectOutputStream oos = null;


            try
            {

                serverUrl = new URL("http://ebtm-ebtm.rhcloud.com/RadioMarcaServer/RegistrarDescarga");

                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setRequestMethod("POST");

                InfoDescarga infoDescarga = infoDescargas[0];
                Log.d(getClass().getCanonicalName(), "Registrando Descarga " + infoDescarga.toString());

                oos = new ObjectOutputStream(httpCon.getOutputStream());
                oos.writeObject(infoDescarga);
                oos.close();

                int resp_code = httpCon.getResponseCode();

                if (resp_code == HttpURLConnection.HTTP_CREATED) //si en alta en el servidor ha sido correcta
                    str_dev = REGISTRO_OK; //actualiza el valor de la respuesta

                httpCon.disconnect();

            }
            catch (Exception e)
            {
                Log.e(getClass().getCanonicalName(), "Error ",  e);
            }

        return str_dev;
		
	}

    @Override
    protected void onPostExecute(String result) {

        Log.d(getClass().getCanonicalName(), "Hemos recibido de RegistrarUsuario= " + result);
    }
}
