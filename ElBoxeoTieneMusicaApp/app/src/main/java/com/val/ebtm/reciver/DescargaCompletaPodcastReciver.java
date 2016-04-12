package com.val.ebtm.reciver;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.val.ebtm.controller.EscuchaEventosListener;

/**
 * Clase que recibe el evento de Descarga de POdcast finalizada y gestiona las actividades
 * de aviso al usuario y actualización de botones
 *
 * @version 1.1.2
 * @author  Val
 *
 */
public class DescargaCompletaPodcastReciver extends BroadcastReceiver {
	
	private long id_descarga;
	private Context context;
	private EscuchaEventosListener e_ev;
	private Button boton_desc;
	private ProgressDialog mProgressDialog;

	

	public DescargaCompletaPodcastReciver(EscuchaEventosListener e_ev, Button boton_desc, ProgressDialog mProgressDialog) {
		this.e_ev = e_ev;
		this.boton_desc = boton_desc;
		this.mProgressDialog = mProgressDialog;
	}



	public void setId_descarga(long id_descarga) {
		this.id_descarga = id_descarga;
	}



	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d (getClass().getCanonicalName(), "Entrando en Onrecieve ");
		
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		Query query = new Query();
		query.setFilterById(this.id_descarga);
		Cursor cursor = downloadManager.query(query);
		cursor.moveToFirst();
		int ref = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
		int status = cursor.getInt(ref);
		
		switch (status) {
		case DownloadManager.STATUS_SUCCESSFUL:
			
			mProgressDialog.dismiss();
			Toast.makeText(context,	"Podcast descargado correctamente!\nYa puede reproducirlo!", Toast.LENGTH_LONG).show();
			Log.d(this.getClass().getCanonicalName(), "Podcast descargado correctamente");
			e_ev.actualizarEstadoFromDescargar(boton_desc);



			break;

		case DownloadManager.STATUS_FAILED: //algo ha fallado
			
			mProgressDialog.dismiss();
			Toast.makeText(context,	"Ha habido un problema en la descarga. Podcast no disponible!", Toast.LENGTH_LONG).show();
			Log.d(this.getClass().getCanonicalName(), "Ha habido un problema durante la descarga del archivo. Archivo no descargado");
			
			break;
		}

        /**
         * IMPORTANTÍSIMO DEREGISTRAR ESTA CLASE, DESASOCIAÁNDOLA DE LA ACTIIVIDA. sI NO, EN LA SIGUIENTE DESCARGA COMPLETA
         * ESTA CLASE SERÁ INOCADA 2 VECES, PUDIENDO PRODUCIR INCOSISTENCIAS
         */
		
        context.unregisterReceiver(this);
		
		
	}

}
