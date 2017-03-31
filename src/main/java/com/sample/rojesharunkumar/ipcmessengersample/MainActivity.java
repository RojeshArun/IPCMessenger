package com.sample.rojesharunkumar.ipcmessengersample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static final int JOB_1 = 1;
    private static final int JOB_2 = 2;
    private static final int JOB_1_RESPONSE = 3;
    private static final int JOB_2_RESPONSE = 4;

    Messenger messenger = null;
    boolean isBind = false;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void GetMessage(View v) {

        String button_text = (String) ((Button) v).getText();
        if (button_text.equals("Get First Message")) {

            Message msg1 =  Message.obtain(null,JOB_1);
            msg1.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else {

            Message msg2 =  Message.obtain(null,JOB_2);
            msg2.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinderService) {
            messenger = new Messenger(iBinderService);
            isBind = true;



        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
            isBind = false;

        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        isBind = false;
        messenger = null;
    }

    class ResponseHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message;

            switch (msg.what){
                case JOB_1_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                    break;
                case JOB_2_RESPONSE:
                    message = msg.getData().getString("response_message");
                    textView.setText(message);
                    break;
            }
        }
    }
}
