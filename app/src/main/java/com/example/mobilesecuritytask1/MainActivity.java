package com.example.mobilesecuritytask1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView message;
    private Button button;
    private EditText editText;

    private final int MAX_BRIGHTNESS = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    private void checkPassword() {
        /*
        For correct password:
        1. Need to enter the battery percentage
        2. Wifi setting need to be disabled
        3. Orientation settings need to be locked
        4. Brightness should be at its highest
        5. Airplane mode need to be enabled
         */
        try {
            int userInput = Integer.parseInt(editText.getText().toString());
            if(getBatteryLevel() == userInput && wifiEnabled() == false &&
                    orientationIsLocked() && getBrightnessValue() == MAX_BRIGHTNESS && airplaneEnabled()){
                Log.d("checkPassword","Login Successfully");
                updateUI("Login Successfully",Color.GREEN);
            } else {
                Log.d("checkPassword", "BatteryLevel = " + getBatteryLevel() + " , getBrightnessValue = " + getBrightnessValue()
                        + " , wifiEnabled = " + wifiEnabled() + " , orientationIsLocked = " + orientationIsLocked() + " , airplaneEnabled = " + airplaneEnabled());
                updateUI("Wrong password",Color.RED);
            }
        }
        catch(Exception e) {
            Log.d("checkPassword", "BatteryLevel = " + getBatteryLevel() + " , getBrightnessValue = " + getBrightnessValue()
                    + " , wifiEnabled = " + wifiEnabled() + " , orientationIsLocked = " + orientationIsLocked() + " , airplaneEnabled = " + airplaneEnabled());
            updateUI("Wrong password",Color.RED);
        }
    }


    public int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50;
        }

        return (level / scale) * 100;
    }

    private int getBrightnessValue() {
        return android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS,-1);
    }

    private boolean wifiEnabled(){
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    private boolean orientationIsLocked(){
        return android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 0;
    }

    private boolean airplaneEnabled(){
        return Settings.System.getInt(this.getContentResolver(),Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
    }

    private void updateUI(String str , int color) {
        message.setText(str);
        message.setTextColor(color);
    }

    private void findView() {
        message = findViewById(R.id.message);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editTextTextPassword);
    }

    private void initView() {
        button.setOnClickListener( b -> checkPassword());
    }
}