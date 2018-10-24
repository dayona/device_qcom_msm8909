package com.vst.testService;
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
import android.os.ITestService;
import android.provider.Settings;
import android.widget.Button;
import android.view.KeyEvent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

import java.util.List;

public class TestService extends Activity{
	 // public static final int DISABLE_MASK_CUSTOM = View.STATUS_BAR_DISABLE_EXPAND
  //           | View.STATUS_BAR_DISABLE_NOTIFICATION_ICONS
  //           | View.STATUS_BAR_DISABLE_NOTIFICATION_ALERTS
  //           | View.STATUS_BAR_DISABLE_NOTIFICATION_TICKER
  //           | View.STATUS_BAR_DISABLE_RECENT
  //           | View.STATUS_BAR_DISABLE_HOME
  //           | View.STATUS_BAR_DISABLE_BACK
  //           | View.STATUS_BAR_DISABLE_CLOCK
  //           | View.STATUS_BAR_DISABLE_SEARCH;

    public static final int DISABLE_MASK = StatusBarManager.DISABLE_MASK;
    private Window mRootView;
	private static final String DTAG = "testService";
	private static final String ORIGINAL_LAUNCHER_PACKAGENAME = "com.android.launcher3";
    private static final String ORIGINAL_LAUNCHER_CLASSNAME = "com.android.launcher3.Launcher";

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
		 mRootView = getWindow();
        ((StatusBarManager)getApplication().getSystemService(Context.STATUS_BAR_SERVICE))
                .disable(DISABLE_MASK);

        Button finish_btn = (Button) findViewById(R.id.button1);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSetup();
            }
        });
	}

	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	protected void finishSetup() {
        // TODO Auto-generated method stub
     
        // remove this activity from the package manager.
        PackageManager pm = getPackageManager();
             
        ((StatusBarManager)getApplication().getSystemService(Context.STATUS_BAR_SERVICE))
                .disable(StatusBarManager.DISABLE_NONE);

        setupDefaultLauncher(pm);

        // terminate the activity.
        finish();
    }


      @Override
    protected void onResume() {
        super.onResume();
        if (mRootView != null) {
            int flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            mRootView.getDecorView().setSystemUiVisibility(flag);
            mRootView.setStatusBarColor(Color.TRANSPARENT);
        }
    }
     private void setupDefaultLauncher(PackageManager pm){
        Intent queryIntent = new Intent();
        queryIntent.addCategory(Intent.CATEGORY_HOME);
        queryIntent.setAction(Intent.ACTION_MAIN);

        List<ResolveInfo> homeActivities = pm.queryIntentActivities(queryIntent, 0);
        if(homeActivities == null) {
            return;
        }

        ComponentName defaultLauncher = new ComponentName(ORIGINAL_LAUNCHER_PACKAGENAME,
                ORIGINAL_LAUNCHER_CLASSNAME);
        int activityNum = homeActivities.size();
        ComponentName[] set = new ComponentName[activityNum];
        int defaultMatch = -1;
        for(int i = 0; i < activityNum; i++){
            ResolveInfo info = homeActivities.get(i);
            set[i] = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
            if(ORIGINAL_LAUNCHER_CLASSNAME.equals(info.activityInfo.name)
                    && ORIGINAL_LAUNCHER_PACKAGENAME.equals(info.activityInfo.packageName)){
                defaultMatch = info.match;
            }
        }

        //if Launcher3 is not found, do not set anything
        if(defaultMatch == -1){
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        pm.addPreferredActivity(filter, defaultMatch, set, defaultLauncher);
 	}
} 
