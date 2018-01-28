package com.example.zohir.greetgoproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class regactivity extends AppCompatActivity {


    private Button mRegisterBtn;
    private EditText mEmailField;
    private EditText mPasswordField;

    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mProgress = new ProgressDialog(this);

        mRegisterBtn = (Button) findViewById(R.id.RegBtn);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }
    private void startRegister() {

        final String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        if( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ) {
            mProgress.setMessage("Signing up....");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if( task.isSuccessful() ) {

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user = mDatabase.child("Users").child(user_id);

                        String Myapi = getMacAddr();

                        current_user.child("Myapi").setValue(Myapi);

                        mProgress.cancel();
                        mPasswordField.setText("");
                        mEmailField.setText("");

                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        Intent loginActivity = new Intent( regactivity.this, loginactivity.class );
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginActivity);
                    }
                    else {
                        mProgress.cancel();
                        Toast.makeText(getApplicationContext(), "You cant create more than one account with same Email" , Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(), " You shoud fill all part  " , Toast.LENGTH_SHORT).show();
        }

    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}