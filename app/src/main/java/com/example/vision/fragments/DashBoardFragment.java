package com.example.vision.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vision.utils.Preference;
import com.example.vision.R;
import com.example.vision.utils.ActivityUtils;
import com.example.vision.utils.Constants;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;

public class DashBoardFragment extends BaseFragment {
    private TextView tvMessage;
    private TextView tvCall;
    private TextView tvBattery;
    private TextView tvDiary;
    boolean b = Preference.getInstance().getData(Preference.CALL_IS_FIRST_TIME, true);




    private DoubleClick doubleClick = new DoubleClick(new DoubleClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (v.getId() == tvMessage.getId()) {
                ActivityUtils.addFragment(getActivity(), R.id.container, DashBoardFragment.this, new MessageFragment());
                Log.d("TAG", "OnSingleclick");
            } else if (v.getId() == tvBattery.getId()) {
                ActivityUtils.addFragment(getActivity(), R.id.container, DashBoardFragment.this, new BatteryFragment());
                Log.d("TAG", "OnDoubleClick");
            } else if (v.getId() == tvDiary.getId()) {
                ActivityUtils.addFragment(getActivity(), R.id.container, DashBoardFragment.this, new DiaryFragment());
                Log.d("TAG", "OnTripleClick");

            } else if (v.getId() == tvCall.getId()) {
                ActivityUtils.addFragment(getActivity(), R.id.container, DashBoardFragment.this, new DialpadFragment());
                Log.d("TAG", "OnFourClick");
            }
        }


        @Override
        public void onDoubleClick(View v) {
            if (v.getId() == tvMessage.getId()) {
                fragmentAppDemo.speakerbox.play("Message");
                Log.d("TAG", "Message taped");
            } else if (v.getId() == tvBattery.getId()) {
                fragmentAppDemo.speakerbox.play("Battery");
                Log.d("TAG", "Battery Taped");
            } else if (v.getId() == tvDiary.getId()) {
                fragmentAppDemo.speakerbox.play("Diary");
                Log.d("TAG", "Diary Tapped");

            } else if (v.getId() == tvCall.getId()) {
                fragmentAppDemo.speakerbox.play("Call");
                Log.d("TAG", "Call Taped");
            }

        }

        @Override
        public void onTripleClick(View v) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setData(Constants.KEY_DASHBOARD, false);
            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());

        }

        @Override
        public void onFourthClick(View v) {
            getActivity().onBackPressed();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvMessage = view.findViewById(R.id.tvmessage);
        tvCall = view.findViewById(R.id.tvcall);
        tvBattery = view.findViewById(R.id.tvbattery);
        tvDiary = view.findViewById(R.id.tvdairy);

        tvMessage.setOnClickListener(doubleClick);
        tvCall.setOnClickListener(doubleClick);
        tvDiary.setOnClickListener(doubleClick);
        tvBattery.setOnClickListener(doubleClick);

        if (b) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setData(Constants.KEY_DASHBOARD, b);
            Preference.getInstance().setData(Preference.DASHBOARD_IS_FIRST_TIME, false);

            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
        }


        return view;
    }

    @Override
    public void onDestroy() {
        fragmentAppDemo.speakerbox.stop();
        super.onDestroy();

    }

    @Override
    public void onPause() {
        fragmentAppDemo.speakerbox.stop();
        super.onPause();
    }
}
