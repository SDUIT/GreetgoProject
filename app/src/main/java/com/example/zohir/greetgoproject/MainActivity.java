package com.example.zohir.greetgoproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button mBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private FirebaseAuth.AuthStateListener mAuthListener;

    protected static MainActivity mainActivity;

    private ListView myUser;
    private List<String> connected = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        mBtn = (Button) findViewById(R.id.addBtn);

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginActivity = new Intent(MainActivity.this, loginactivity.class);
                    loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginActivity);
                }
            }
        };

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddwifiActivity = new Intent(MainActivity.this, AddWifiActivity.class);
                AddwifiActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AddwifiActivity);
            }
        });

        myUser = (ListView) findViewById(R.id.user);
        ArrayAdapter<String> adapter = new ArrayAdapter< String > (this, android.R.layout.simple_list_item_1, connected );
        myUser.setAdapter(adapter);

        myUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, getString(i), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String mi_id = mAuth.getCurrentUser().getUid();
                DataSnapshot friend = dataSnapshot.child(mi_id);
                for (DataSnapshot mData : friend.getChildren()) {
                    if( !mData.getKey().equals("Myapi") ) {
                        System.out.println(mData);
                        String row = "Name: " + dataSnapshot.child(mi_id).child(mData.getKey()).getValue().toString();
                        connected.add(row);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
        if(loginactivity.LoginActivity != null){
            loginactivity.LoginActivity.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
