package com.example.hujiyang.myapplication.Activity.Activity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hujiyang.myapplication.R;


import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    TextView time;
    TextView status;
    Button start;
    TextView history;
    boolean mode = false;
    ArrayList<MyContact2> returnValues = new ArrayList<MyContact2>();
//    Timer timer;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Intent serviceIt;
    public static final String serviceOnCreated = "serviceOnCreated";
    public static final String create = "create";
    public static final String fetch = "fetch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        time = findViewById(R.id.time);
        start = findViewById(R.id.start);
        history = findViewById(R.id.history);
        time.setText("00:00");
        MyContact2 contact2 = new MyContact2();
        contact2.setTime("00:00");
        MongoLabSaveContact2 tsk2 = new MongoLabSaveContact2();
        tsk2.execute(contact2);
        editor = pref.edit();
        editor.putString("time", "00:00");
        editor.commit();

        serviceIt = new Intent(this, MainService.class);
        startService(serviceIt);//开启一个服务器
        IntentFilter filter = new IntentFilter();
        filter.addAction(create);
        filter.addAction(fetch);
        registerReceiver(receiver, filter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.start:
                        if (mode == false) {
                            mode = true;
                            start.setText("Stop monitor");
                            start.setBackgroundColor(Color.parseColor("#DC143C"));
                            MyContact contact = new MyContact();

                            contact.setState("1");

                            MongoLabSaveContact tsk = new MongoLabSaveContact();
                            tsk.execute(contact);

                            MyContact2 contact2 = new MyContact2();
                            contact2.setTime("00:00");
                            MongoLabSaveContact2 tsk2 = new MongoLabSaveContact2();
                            tsk2.execute(contact2);

                            editor = pref.edit();
                            editor.putString("time", "00:00");
                            editor.commit();
                            Toast.makeText(MainActivity.this, "Start sensor success", Toast.LENGTH_SHORT).show();
                            sendBroadcast(new Intent(MainService.state1));
                        } else {
                            mode = false;
                            start.setText("Start monitor");
                            start.setBackgroundColor(Color.parseColor("#2E8B57"));

                            MyContact contact = new MyContact();
                            contact.setState("0");
                            MongoLabSaveContact tsk = new MongoLabSaveContact();
                            tsk.execute(contact);

                            Toast.makeText(MainActivity.this, "Stop sensor success", Toast.LENGTH_SHORT).show();

                            MyContact2 contact2 = new MyContact2();
                            contact2.setTime("00:00");
                            MongoLabSaveContact2 tsk2 = new MongoLabSaveContact2();
                            tsk2.execute(contact2);

                            editor = pref.edit();
                            editor.putString("time", "00:00");
                            editor.commit();

                            status.setText("Safe");
                            status.setTextColor(Color.parseColor("#2E8B57"));
                            time.setText("00:00");
                            sendBroadcast(new Intent(MainService.state0));

                        }
                }
            }
        });
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = 0;
//                mHandler.sendMessage(message);
//            }
//        }, 0, 5000);//run every 0.1s

    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0) {
//                if (mode) {
//                    fetch();
//                }
//            }
//        }
//    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case fetch:
                    fetch();
                    break;

            }

        }
    };
    //connect database
    final class MongoLabSaveContact extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            MyContact contact = (MyContact) params[0];
            Log.d("contact", "" + contact);


            try {


                SupportData sd = new SupportData();
                URL url = new URL(sd.buildContactsSaveURL());

                Log.d("url", "" + url);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Accept", "application/json");

                OutputStreamWriter osw = new OutputStreamWriter(
                        connection.getOutputStream());

                osw.write(sd.createContact(contact));
                osw.flush();
                osw.close();

                Log.d("Response code", "" + (connection.getResponseCode()));
                if (connection.getResponseCode() < 205) {

                    return true;
                } else {
                    return false;

                }

            } catch (Exception e) {
                e.getMessage();
                Log.d("Got error", e.getMessage());
                return false;

            }

        }

    }

    final class MongoLabSaveContact2 extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            MyContact2 contact2 = (MyContact2) params[0];
            Log.d("contact", "" + contact2);


            try {


                SupportData2 sd = new SupportData2();
                URL url = new URL(sd.buildContactsSaveURL());

                Log.d("url", "" + url);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Accept", "application/json");

                OutputStreamWriter osw = new OutputStreamWriter(
                        connection.getOutputStream());

                osw.write(sd.createContact(contact2));
                osw.flush();
                osw.close();

                Log.d("Response code", "" + (connection.getResponseCode()));
                if (connection.getResponseCode() < 205) {

                    return true;
                } else {
                    return false;

                }

            } catch (Exception e) {
                e.getMessage();
                Log.d("Got error", e.getMessage());
                return false;

            }

        }

    }

    //get time
    public void fetch() {
        GetContactsAsyncTask task = new GetContactsAsyncTask();
        try {
            returnValues = task.execute().get();
            MyContact2 FetchedData = (MyContact2) returnValues.toArray()[0];
            time.setText(FetchedData.getTime());
            String cTime = FetchedData.getTime().toString();
            String bTime = pref.getString("time", "00:00");
            if (!cTime.equals(bTime)) {
                status.setText("Dangerous");
                status.setTextColor(Color.parseColor("#DC143C"));
                editor = pref.edit();
                editor.putString("time", cTime);
                editor.commit();
                mes(cTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //notification
    public void mes(String m) {
            NotificationManager notificationManager = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Detect intruder!!!")
                    .setContentText("Time open the door: "+m)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(m)
                    .setDefaults(Notification.DEFAULT_SOUND);
            notificationManager.notify(10, mBuilder.build());
        }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(serviceIt);//关掉应用的时候不留后台
        unregisterReceiver(receiver);
    }

}
