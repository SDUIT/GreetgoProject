package com.example.zohir.greetgoproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddWifiActivity extends AppCompatActivity {
    private Button mBtn;
    private ListView mlistItems;

    private Element[] nots;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;

    private DatabaseReference database;
    DatabaseReference mRef;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        mBtn = (Button) findViewById(R.id.wifiBtn);
        mlistItems = (ListView) findViewById(R.id.listItems);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        database = FirebaseDatabase.getInstance().getReference().child("Users");

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectWifi();
            }
        });

        mlistItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                num = position;
                addme();
            }
        });
    }

    public void addme() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(  DataSnapshot dsp : dataSnapshot.getChildren() ) {
                    if( dsp.child("Myapi").equals( nots[num].getSecurity() )     ) {
                          mRef.child(dsp.getKey()).setValue("name");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void detectWifi() {

        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ActivityCompat.requestPermissions(AddWifiActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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

