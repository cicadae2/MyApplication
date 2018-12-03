package com.example.hujiyang.myapplication.Activity.Activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    public static final String state1 = "state1";
    public static final String state0 = "state0";


    private MyBroadcastReceiver receiver;

    public Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter());
        IntentFilter filter = new IntentFilter();
        filter.addAction(state1);
        filter.addAction(state0);

        registerReceiver(receiver, filter);
        sendBroadcast(new Intent(MainActivity.serviceOnCreated));


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case state1:
                    state1();
                    break;
                case state0:
                    state0();
                    break;

            }
        }
    }
    private void state1() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }, 0, 5000);//run every 0.1s
    }
    private void state0() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                sendBroadcast(new Intent(MainActivity.fetch));
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
//        if (timertask != null) {
//            timertask.cancel();
//            timertask = null;
//        }
        unregisterReceiver(receiver);
    }
}
