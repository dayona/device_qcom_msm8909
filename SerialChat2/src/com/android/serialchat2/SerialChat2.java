/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.serialchat2;

import android.app.Activity;
import android.content.Context;
import android.hardware.SerialManager;
import android.hardware.SerialPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import android.view.KeyEvent;
import android.view.View;
import java.util.Arrays;
import android.widget.Button; 
import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import java.util.List;

import java.nio.ByteBuffer;
import java.io.IOException;

public class SerialChat2 extends Activity implements Runnable, TextView.OnEditorActionListener {

    private static final String TAG = "SerialChat2";

    private TextView mLog;
    private EditText mEditText;
    private ByteBuffer mInputBuffer;
    private ByteBuffer mOutputBuffer;
    private SerialManager mSerialManager;
    private SerialPort mSerialPort;
    private boolean mPermissionRequestPending;

    private static final int MESSAGE_LOG = 1;
    private static final String ORIGINAL_LAUNCHER_PACKAGENAME = "com.android.launcher3";
    private static final String ORIGINAL_LAUNCHER_CLASSNAME = "com.android.launcher3.Launcher";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSerialManager = (SerialManager)getSystemService(Context.SERIAL_SERVICE);
        setContentView(R.layout.serial_chat);
        mLog = (TextView)findViewById(R.id.log);
        mEditText = (EditText)findViewById(R.id.message);
        mEditText.setOnEditorActionListener(this);

        if (false) {
            mInputBuffer = ByteBuffer.allocateDirect(1024);
            mOutputBuffer = ByteBuffer.allocateDirect(1024);
        } else {
            mInputBuffer = ByteBuffer.allocate(1024);
            mOutputBuffer = ByteBuffer.allocate(1024);
        }
        String[] ports = mSerialManager.getSerialPorts();
        if (ports != null && ports.length > 0) {
            try {
                Log.d(TAG, "printing ports " + Arrays.toString(ports));
                mSerialPort = mSerialManager.openSerialPort(ports[0], 9600);
                if (mSerialPort != null) {
                    new Thread(this).start();
                }
            } catch (IOException e) {
            }
        }

        doubleH();
        print();
        linefeed();//LF

        Button finish_btn = (Button) findViewById(R.id.button1);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSetup();
            }
        });
    }

    void linefeed(){
        try {
            // char[] F = new char[]{0x0A};
            Log.d(TAG, "write: " + "LF");
            byte[] bytes = new byte[] {0x0A};
            mOutputBuffer.clear();
            mOutputBuffer.put(bytes);
            mSerialPort.write(mOutputBuffer, bytes.length);
        } catch (IOException e) {
            Log.e(TAG, "write failed", e);
        }
    }

     void doubleH(){
        try {
            // char[] F = new char[]{0x0A};
            Log.d(TAG, "write: " + "Double H");
             String text = "             bro        ";
            byte[] bytes = text.getBytes();
            mOutputBuffer.clear();
            mOutputBuffer.put(bytes);
            mSerialPort.write(mOutputBuffer, bytes.length);       


            Log.d(TAG, "write: " + "Test");
            int feed = 5;
            mOutputBuffer.clear();
            byte[] bytes2 = new byte[] {0x1B,'d',0x00,(byte)feed};
            mOutputBuffer.put(bytes2);
            mOutputBuffer.put(bytes);
            mSerialPort.write(mOutputBuffer, bytes2.length);
            // mOutputBuffer.clear();
            // byte[] bytes3 = new byte[] {0x00|0x20};
            // mOutputBuffer.put(bytes3);
            // mSerialPort.write(mOutputBuffer, bytes3.length);          
              
            // mSerialPort.write(mOutputBuffer, bytes1.length+bytes2.length+bytes3.length+bytes.length);
        } catch (IOException e) {
            Log.e(TAG, "write failed", e);
        }
    }

    void print(){
        try {
            String text = "hello how are you\n"+
            "        lets go \n"+
            "       Enemies Ahead\n"+
            "       Mark My location\n"+
            "       Get to safezone\n"+
            "       Turn on voice chat\n";
            Log.d(TAG, "write: " + text);
            byte[] bytes = text.getBytes();
            mOutputBuffer.clear();
            mOutputBuffer.put(bytes);
            mSerialPort.write(mOutputBuffer, bytes.length);
        } catch (IOException e) {
            Log.e(TAG, "write failed", e);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        String[] ports = mSerialManager.getSerialPorts();
        if (ports != null && ports.length > 0) {
            try {
                Log.d(TAG, "printing ports " + Arrays.toString(ports));
                mSerialPort = mSerialManager.openSerialPort(ports[0], 9600);
                if (mSerialPort != null) {
                    new Thread(this).start();
                }
            } catch (IOException e) {
            }
        }
        Log.d(TAG, "port == " + Arrays.toString(mSerialManager.getSerialPorts()));

    }

    @Override
    public void onPause() {
        super.onPause();
    
    }

    @Override
    public void onDestroy() {
        if (mSerialPort != null) {
            try {
                mSerialPort.close();
            } catch (IOException e) {
            }
            mSerialPort = null;
        }
        super.onDestroy();
    }

    protected void finishSetup() {
        // TODO Auto-generated method stub
     
        // remove this activity from the package manager.
        PackageManager pm = getPackageManager();
        setupDefaultLauncher(pm);
        // terminate the activity.
        finish();
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

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (/* actionId == EditorInfo.IME_ACTION_DONE && */ mSerialPort != null) {
            try {
                String text = v.getText().toString();
                Log.d(TAG, "write: " + text);
                byte[] bytes = text.getBytes();
                mOutputBuffer.clear();
                mOutputBuffer.put(bytes);
                mSerialPort.write(mOutputBuffer, bytes.length);
            } catch (IOException e) {
                Log.e(TAG, "write failed", e);
            }
            v.setText("");
            return true;
        }
        Log.d(TAG, "onEditorAction " + actionId + " event: " + event);
        return false;
    }

    public void run() {
        Log.d(TAG, "run");
        int ret = 0;
        byte[] buffer = new byte[1024];
        while (ret >= 0) {
            try {
                Log.d(TAG, "calling read");
                mInputBuffer.clear();
                ret = mSerialPort.read(mInputBuffer);
                Log.d(TAG, "read returned " + ret);
                mInputBuffer.get(buffer, 0, ret);
            } catch (IOException e) {
                Log.e(TAG, "read failed", e);
                break;
            }

            if (ret > 0) {
                Message m = Message.obtain(mHandler, MESSAGE_LOG);
                String text = new String(buffer, 0, ret);
                Log.d(TAG, "chat: " + text);
                m.obj = text;
                mHandler.sendMessage(m);
            }
        }
        Log.d(TAG, "thread out");
    }

   Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LOG:
                    Log.d(TAG, "handler_message: " + (String) msg.obj);
                    mLog.setText(mLog.getText() + (String) msg.obj);
                    break;
             }
        }
    };
}


