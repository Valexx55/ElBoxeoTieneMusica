package com.val.ebtm;

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

public class NotificaReciver extends BroadcastReceiver {
	
	private long id_descarga;
	private Context context;
	private EscuchaEventos e_ev;
	private Button boton_desc;
	private ProgressDialog mProgressDialog;
	
	

	public NotificaReciver(long id_descarga, Context context, EscuchaEventos e_ev, Button boton_desc, ProgressDialog mProgressDialog) {
		this.id_descarga = id_descarga;
		this.context = context;
		this.e_ev = e_ev;
		this.boton_desc = boton_desc;
		this.mProgressDialog = mProgressDialog;
	}



	public long getId_descarga() {
		return id_descarga;
	}



	public void setId_descarga(long id_descarga) {
		this.id_descarga = id_descarga;
	}



	public Context getContext() {
		return context;
	}



	public void setContext(Context context) {
		this.context = context;
	}



	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
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

		case DownloadManager.STATUS_FAILED: //something was wrong
			
			mProgressDialog.dismiss();
			Toast.makeText(context,	"Ha habido un problema en la descarga. Podcast no disponible!", Toast.LENGTH_LONG).show();
			Log.d(this.getClass().getCanonicalName(), "Ha habido un problema durante la descarga del archivo. Archivo no descargado");
			
			break;
		}
		
		
		
		
	}

}
