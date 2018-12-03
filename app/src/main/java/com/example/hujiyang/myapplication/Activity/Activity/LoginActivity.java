package com.example.hujiyang.myapplication.Activity.Activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.hujiyang.myapplication.R;



public class LoginActivity extends Activity {
    public static EditText organizationValue;
    public static EditText deviceIDValue;
    public CheckBox checkbox_remember;
    public Button enter;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        organizationValue = findViewById(R.id.organizationValue);
        deviceIDValue = findViewById(R.id.deviceIDValue);
        checkbox_remember = findViewById(R.id.checkbox_remember);
        enter = findViewById(R.id.enter);
        boolean isRemember = pref.getBoolean("remember", false);
        if (isRemember) {
            String organization = pref.getString("organization", "");
            String deviceID = pref.getString("deviceID", "");
            organizationValue.setText(organization);
            deviceIDValue.setText(deviceID);
            checkbox_remember.setChecked(true);
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String organization = organizationValue.getText().toString();
                String deviceID = deviceIDValue.getText().toString();
                if (checkbox_remember.isChecked()) {
                    editor = pref.edit();
                    editor.putBoolean("remember", true);
                    editor.putString("organization", organization);
                    editor.putString("deviceID", deviceID);
                    editor.commit();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        

    }
}