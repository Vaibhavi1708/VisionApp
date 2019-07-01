package com.example.vision.activities;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vision.R;
import com.example.vision.fragments.DashBoardFragment;
import com.example.vision.utils.ActivityUtils;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityUtils.replaceFragment(MainActivity.this, R.id.container, new DashBoardFragment());

        fragmentAppDemo.speakerbox.play("Welcome to Vision App Press on different sides to open and double tap to know the details");

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment != null) {
            if (fragment instanceof DashBoardFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        fragmentAppDemo.speakerbox.play("Bye Bye");
        super.onDestroy();
    }
}

