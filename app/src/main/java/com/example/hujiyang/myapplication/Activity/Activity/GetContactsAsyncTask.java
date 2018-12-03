package com.example.hujiyang.myapplication.Activity.Activity;

import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetContactsAsyncTask extends AsyncTask<MyContact2, Void, ArrayList<MyContact2>> {
    static String server_output = null;
    static String temp_output = null;

    @Override
    protected ArrayList<MyContact2> doInBackground(MyContact2... arg0) {

        ArrayList<MyContact2> mycontacts = new ArrayList<MyContact2>();
        try
        {
            SupportData2 sd = new SupportData2();
            URL url = new URL(sd.buildContactsFetchURL());
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            // create a basic db list
            String mongoarray = "{ DB_output: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);
            DBObject dbObj = (DBObject) o;
            BasicDBList contacts = (BasicDBList) dbObj.get("DB_output");
            for (Object obj : contacts) {
                DBObject userObj = (DBObject) obj;
                MyContact2 temp = new MyContact2();
                temp.setTime(userObj.get("time").toString());
                mycontacts.add(temp);

            }

        }catch (Exception e) {
            e.getMessage();
        }

        return mycontacts;
    }
}
