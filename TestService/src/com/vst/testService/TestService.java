package com.vst.testService;
import android.app.Activity;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.ITestService;
import android.util.Log;
public class TestService extends Activity{
	private static final String DTAG = "testService";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ITestService om =ITestService.Stub.asInterface(ServiceManager.getService("Test"));
		try{
			Log.d(DTAG,"Going to call service");
			om.setValue(20);
			Log.d(DTAG, "service called succesfully");
		}
		catch(Exception e){
			Log.d(DTAG, "FAILED to call service");
			e.printStackTrace();
		}
	}
} 
