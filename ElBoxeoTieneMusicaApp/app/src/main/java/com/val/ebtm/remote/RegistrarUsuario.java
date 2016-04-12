package com.val.ebtm.remote;


import android.os.AsyncTask;
import android.util.Log;

import com.val.ebtm.InfoUsuario;

import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase que se encarga de la información de un usuario en el servidor cuando este
 * usa la aplicación por primera vez
 *
 * @author Val
 * @version 1.1.3
 */
public class RegistrarUsuario extends AsyncTask<InfoUsuario, Integer, String> {

    public static final String REGISTRO_OK = "OK";
    public static final String REGISTRO_KO = "KO";

	@Override
	protected String doInBackground(InfoUsuario... infoUsuarios)
    {
        String str_dev = REGISTRO_KO;
        URL serverUrl = null;
        HttpURLConnection httpCon = null;
        ObjectOutputStream oos = null;


            try

            {
                str_dev = REGISTRO_KO; //Inicialilizo pesimista

                serverUrl = new URL("http://ebtm-ebtm.rhcloud.com/RadioMarcaServer/RegistrarUsuario");
                ///RegistrarUsuario
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setRequestMethod("POST");

                InfoUsuario infoUsuario1 = infoUsuarios[0];
                Log.d(getClass().getCanonicalName(), "Registrando a " + infoUsuario1.toString());

                oos = new ObjectOutputStream(httpCon.getOutputStream());
                oos.writeObject(infoUsuario1);
                oos.close();

                int resp_code = httpCon.getResponseCode();

                if (resp_code == HttpURLConnection.HTTP_CREATED) //Sólo si en alta en el servidor ha sido correcta
                    str_dev = REGISTRO_OK; //actualiza el valor de la respuesta

                httpCon.disconnect();

            }
            catch (Exception e)
            {
                Log.e("Error ",  e.getMessage());
            }

        return str_dev;
		
	}

    @Override
    protected void onPostExecute(String result) {

        Log.d(getClass().getCanonicalName(), "Hemos recibido de RegistrarUsuario= " + result);
    }
}
