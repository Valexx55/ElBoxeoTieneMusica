package com.val.ebtm.view;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 *
 * Fragment que muestra un aviso al usuario en caso de que este no tenga el wifi activado,
 * requisito necesario para garantizar la descarga de un podcast con éxito.
 *
 * @version 1.2
 */
public class DialogoWifiFragment extends DialogFragment{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {


		Builder builder = new Builder(getActivity());
		
        builder.setMessage("Conéctese a una red WIFI para descargar los podcasts")
               .setTitle("Aviso Importante")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });
		return builder.create();
	}
	
	//ona

}
