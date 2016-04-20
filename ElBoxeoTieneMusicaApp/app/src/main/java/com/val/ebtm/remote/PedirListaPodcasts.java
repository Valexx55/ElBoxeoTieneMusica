package com.val.ebtm.remote;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Clase que solicita la lista de podcast disponibles al servidor, devuelta en formato JSON
 *
 * @version 1.1
 */
public class PedirListaPodcasts extends AsyncTask<Void, Integer, String> {

	@Override
	protected String doInBackground(Void... params) {
		
		String msj_json = null;
		
		try 
			{
			
				Log.d(this.getClass().getCanonicalName(), "Llamado a ListarPodcastsDisponibles");
				URL serverUrl = new URL("http://ebtm-ebtm.rhcloud.com/RadioMarcaServer/ListarPodcastsDisponibles");
				HttpURLConnection httpCon = (HttpURLConnection) serverUrl.openConnection();

                if (httpCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(httpCon.getInputStream());

                    int leido;
                    msj_json = "";

                    while ((leido = isr.read()) != -1) {
                        msj_json = msj_json + (char) leido;
                    }

                    isr.close();
                }
				httpCon.disconnect();
			
			}
			catch (Exception e)
				{
					Log.e("Error ",  e.getMessage());
				}
		
		return msj_json;
	}

	@Override
	protected void onPostExecute(String result) {

		Log.d(getClass().getCanonicalName(),"Hemos recibido de ListarPodcastsDisponibles= "+result);
	}

}
