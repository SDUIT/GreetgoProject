package com.example.zohir.greetgoproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Search_activity extends AppCompatActivity {

    public TextView t;

    public String work;
    private String s;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        t = (TextView) findViewById(R.id.ip);
        s = getIntent().getStringExtra("Ip");
        t.setText( s );
//        work = (Spannable) t.getText();

        new Thread(){
            public void run(){
              while(!isInterrupted()) {
                  System.out.println("Thread Running");
                  try {

                      detectWifi();
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              if( flag == false ) {
                                  t.setText("He/She not in wife Zone");
                              }
                              else {
                                  t.setText(work);
                              }
                          }
                      });
                      Thread.sleep(1000);

                  }
                  catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
            }
        }.start();

    }
    public void detectWifi() {

        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ActivityCompat.requestPermissions(Search_activity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        this.wifiManager.startScan();

        this.wifiList = this.wifiManager.getScanResults();

        Log.d("TAG", wifiList.toString());

         flag = false;
        for( int i=0; i<wifiList.size(); i++ ) {

            String item = wifiList.get(i).toString();
            ///
            System.out.println(item);
            ///
            String[] vector_item = item.split(",");
            String item_ssid = vector_item[0];
            String item_capabilities = vector_item[1];
            String item_level = vector_item[3];
            String item_frequency = vector_item[4];
            String ssid = item_ssid.split(": ")[1];
            String security = item_capabilities.split(": ")[1];
            String level = item_level.split(": ")[1];
            String frequency = item_frequency.split(": ")[1];

            System.out.println( security );

            double ans  = calculateDistance(Double.parseDouble(level), Double.parseDouble(frequency));
            if ( security.equals(s) ) {
                flag = true;
                work = String.valueOf(ans);
//              work = new SpannableString("He is in: " + String.valueOf(ans));
                break;
            }
        }
        if( flag == false ) {
//            work = new SpannableString("He is not in wifi Zone");
        }

    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
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
