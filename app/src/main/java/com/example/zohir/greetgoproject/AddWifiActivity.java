package com.example.zohir.greetgoproject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AddWifiActivity extends AppCompatActivity {
    private Button mBtn;

    private Element[] nots;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        mBtn = (Button) findViewById(R.id.wifiBtn);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectWifi();
            }
        });
    }

    public void detectWifi() {
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();

        Log.d("TAG", wifiList.toString());

        this.nots = new Element[wifiList.size()];
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

            nots[i] = new Element(ssid, security, level);
        }

        AddWifiActivity.AdapterElements adapterElements = new AddWifiActivity.AdapterElements(this);
        ListView netList = (ListView) findViewById(R.id.listItems);
        netList.setAdapter(adapterElements);
    }

    class AdapterElements extends ArrayAdapter<Object> {
        Activity context;
        public AdapterElements ( Activity context ) {
            super(context,R.layout.items,nots);
            this.context = context;
        }
        public View getView(int Position, View convetrView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.items, null);

            TextView tvSsid = (TextView) item.findViewById(R.id.tvSSID);
            tvSsid.setText(nots[Position].getTitle());

            TextView tvSecurity = (TextView) item.findViewById(R.id.tvSecurity);
            tvSecurity.setText(nots[Position].getSecurity());

            TextView tvLevel = (TextView) item.findViewById(R.id.tvLevel);
            String level = nots[Position].getLevel();

            try {
                int i = Integer.parseInt(level);
                if( i > -50 ) {
                    tvLevel.setText("High");
                }
                else if( i<=50 && i>-80 ) {
                    tvLevel.setText("Medium");
                }
                else if( i<=-80 ) {
                    tvLevel.setText("Low");
                }
            }
            catch (NumberFormatException e) {
                Log.d("TAG", "Неверниӣ Формат Строки");
            }
            return item;
        }
    }
}

