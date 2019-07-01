package com.example.vision.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.vision.FragmentApp;
import com.example.vision.utils.Speakerbox;

import java.util.Objects;

public class BaseFragment extends Fragment {

    Speakerbox speakerbox;

    protected String TAG = getClass().getSimpleName();
    protected FragmentApp fragmentAppDemo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speakerbox = new Speakerbox(getActivity().getApplication());

        fragmentAppDemo = ((FragmentApp) Objects.requireNonNull(getActivity()).getApplicationContext());
        Log.d("TAG", TAG);
    }

}
