package com.example.vision.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.vision.utils.Preference;
import com.example.vision.R;
import com.example.vision.utils.Constants;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;

import java.util.Objects;

import eo.view.batterymeter.BatteryMeterView;

public class BatteryFragment extends BaseFragment {

    private BatteryMeterView fragment_battery_batteryView;
    private TextView tvBatteryLevel;
    private TextView tvTitle;
    private AppCompatImageButton btnBack;
    private FrameLayout frameLayoutBattery;
    boolean b = Preference.getInstance().getData(Preference.CALL_IS_FIRST_TIME, true);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_battery, container, false);

        if (b) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();

            myDialogFragment.setData(Constants.KEY_BATTERY, b);
            Preference.getInstance().setData(Preference.BATTERY_IS_FIRST_TIME, false);

            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
        }


        fragment_battery_batteryView = view.findViewById(R.id.fragment_battery_batteryView);
        tvBatteryLevel = view.findViewById(R.id.tvBatteryLevel);
        tvTitle = view.findViewById(R.id.tvTitle);
        btnBack = view.findViewById(R.id.backImgButton);
        frameLayoutBattery = view.findViewById(R.id.frame_battery);


        frameLayoutBattery.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {
                batteryLevel(getActivity());

            }

            @Override
            public void onTripleClick(View view) {
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.setData(Constants.KEY_BATTERY, false);
                myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
            }

            @Override
            public void onFourthClick(View view) {
                getActivity().onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");
            }
        }));


        fragment_battery_batteryView.setCharging(false);

        tvTitle.setText("Battery");

        //frameLayoutBattery.setOnClickListener(doubleClick);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");
            }
        });
        batteryLevel(getActivity());
        return view;
    }

    public void batteryLevel(final Context context) {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                context.unregisterReceiver(this);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                Log.d("TAG", "Battery Level : " + level + "%");
                fragment_battery_batteryView.setChargeLevel(level);
                tvBatteryLevel.setText(level + "%");

                if (level > 10) {
                    fragment_battery_batteryView.setCriticalChargeLevel(10);
                    fragment_battery_batteryView.setCriticalColor(55000);
                    tvBatteryLevel.setText(level + "%");
                    //fragmentAppDemo.speakerbox.play("Battery is low");
                }

                fragment_battery_batteryView.setCharging(isCharging);
                if (!isCharging) {
                    fragmentAppDemo.speakerbox.play("Battery is " + level + "% " );
                } else {
                    fragmentAppDemo.speakerbox.play("Battery is " + level + "% " + "   State: Charging");
                }

            }
        };
        final IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    @Override
    public void onDestroy() {
        fragmentAppDemo.speakerbox.stop();
        super.onDestroy();
    }
}
