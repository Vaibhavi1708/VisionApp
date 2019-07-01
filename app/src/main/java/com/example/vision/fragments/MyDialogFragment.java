package com.example.vision.fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.vision.FragmentApp;
import com.example.vision.R;
import com.example.vision.utils.Constants;
import com.example.vision.utils.Speakerbox;

public class MyDialogFragment extends DialogFragment {
    private boolean isFirstTime;
    private String screenName;
    public Speakerbox speakerbox;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Dialog dialog = getDialog();


        if (dialog == null) {
            return;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        if(!isFirstTime){
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        } else {
            window.getAttributes().windowAnimations = R.style.FullScreenDialogStyle;
        }
        window.setBackgroundDrawable(new ColorDrawable(0));
        final WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.CENTER;
        windowParams.dimAmount = 0.60f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dialog.setCanceledOnTouchOutside(true);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        windowParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        windowParams.height = FrameLayout.LayoutParams.MATCH_PARENT;



    }
//
//       if (isFirstTime) {
//            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
//       }
//       else{
//           setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogAnimation);
//        }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_common, container, false);

        speakerbox = new Speakerbox(FragmentApp.getInstance());


        TextView tv = view.findViewById(R.id.dialog_fragment_common_tv);
        Log.d("TAG", "MyDialogFragment");

        if (screenName.equals(Constants.KEY_DASHBOARD)) {

            tv.setText(getString(R.string.s_db));
            speakerbox.play(getString(R.string.s1_db));

        } else if (screenName.equals(Constants.KEY_CALL)) {

            tv.setText(R.string.s_c);
            speakerbox.play(getString(R.string.s1_c));

        } else if (screenName.equals(Constants.KEY_MESSAGE)) {

            tv.setText(R.string.s_m);
            speakerbox.play(getString(R.string.s1_m));

        } else if (screenName.equals(Constants.KEY_DIARY)) {

            tv.setText(R.string.s_d);
            speakerbox.play(getString(R.string.s1_d));
        }
        else if (screenName.equals(Constants.KEY_BATTERY)) {

            tv.setText(R.string.s_b);
            speakerbox.play(getString(R.string.s1_b));
        }


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    public void setData(String name,boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
        this.screenName = name;
    }

    @Override
    public void onPause() {
        speakerbox.stop();
        super.onPause();
    }
}
