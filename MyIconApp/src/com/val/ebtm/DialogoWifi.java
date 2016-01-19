package com.val.ebtm;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogoWifi extends DialogFragment{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {


		Builder builder = new Builder(getActivity());
		
        builder.setMessage("Conéctese a una red WIFI para garantizar el correcto funcionamiento de la aplicación")
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
