package com.val.ebtm;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ListenerCheckBox implements OnCheckedChangeListener{

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().getCanonicalName(), "Ha tocado el checkbox");
		
	}

}
