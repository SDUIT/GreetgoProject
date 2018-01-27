package com.example.zohir.greetgoproject;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by zohir on 27.01.2018.
 */

public class GreetgoProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
