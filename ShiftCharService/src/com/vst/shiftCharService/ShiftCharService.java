package com.vst.shiftCharService;
import android.app.Activity;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Window;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.IShiftCharService;
import android.provider.Settings;
import android.widget.Button;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

import java.util.List;

public class ShiftCharService extends Activity {
    private static final String DTAG = "shiftCharService";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        IShiftCharService om =IShiftCharService.Stub.asInterface(ServiceManager.getService("Shift"));
        Button button = (Button) findViewById(R.id.button);
        final EditText number = (EditText) findViewById(R.id.num);
        EditText name =(EditText) findViewById(R.id.name);
        final TextView result =(TextView) findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String N=name.getText().toString();
                String num=number.getText().toString();
                try{
                    Log.d(DTAG,"Going to call service ");
                    om.setValue(N,num);
                    result.setText(om.getValue());
                    Log.d(DTAG, "service called succesfully");
                }    		       
		        catch(Exception e){
        			Log.d(DTAG, "FAILED to call service");
        			e.printStackTrace();
                }
	        }
        });
    }
}

