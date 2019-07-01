package com.example.vision.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.vision.FragmentApp;

public class BaseActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();
    protected FragmentApp fragmentAppDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentAppDemo = ((FragmentApp) getApplicationContext());

        Log.d("TAG", TAG);
    }
}

