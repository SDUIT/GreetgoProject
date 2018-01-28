package com.example.zohir.greetgoproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Search_activity extends AppCompatActivity {

    private TextView t;

    private WifiManager wifiManager;
    private List<ScanResult> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        t = (TextView) findViewById(R.id.ip);
        String s = getIntent().getStringExtra("Ip");
        t.setText(s);

    }
    public void detectWifi() {

        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ActivityCompat.requestPermissions(Search_activity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        this.wifiManager.startScan();

        this.wifiList = this.wifiManager.getScanResults();

        Log.d("TAG", wifiList.toString());

        boolean flag = false;
        for( int i=0; i<wifiList.size(); i++ ) {

            String item = wifiList.get(i).toString();
            ///
            System.out.println(item);
            ///
            String[] vector_item = item.split(",");
            String item_ssid = vector_item[0];
            String item_capabilities = vector_item[1];
            String item_level = vector_item[3];
            String ssid = item_ssid.split(": ")[1];
            String security = item_capabilities.split(": ")[1];
            String level = item_level.split(": ")[1];

            if ( item_capabilities.equals(t) ) {
                flag = true;
                t.setText( ssid );
            }
        }
        if( flag == false ) {
            t.setText("He is not in wifi Zone");
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    grantResults[0] = PackageManager.PERMISSION_GRANTED;
                }
                return;
            }
        }
    }

}
