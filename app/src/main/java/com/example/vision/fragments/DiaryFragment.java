package com.example.vision.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vision.models.DiaryDBModel;
import com.example.vision.adapters.DiaryRecyclerAdapter;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;
import com.example.vision.utils.Preference;
import com.example.vision.R;
import com.example.vision.activities.MainActivity;
import com.example.vision.utils.Constants;
import com.example.vision.utils.HorizontalSpacingDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class DiaryFragment extends BaseFragment implements DiaryRecyclerAdapter.OnPositionListener {

    private AppCompatImageButton btnBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ImageView imageMic;
    private RelativeLayout relativeLayout;

    boolean b = Preference.getInstance().getData(Preference.DIARY_IS_FIRST_TIME, true);

    List<DiaryDBModel> diaryDBModels = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_diary, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        btnBack = view.findViewById(R.id.backImgButton);
        recyclerView = view.findViewById(R.id.recycler_view_diary);
        imageMic = view.findViewById(R.id.imageMic);
        relativeLayout = view.findViewById(R.id.diary);

       relativeLayout.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }

            @Override
            public void onDoubleClick(View view) {
                getSpeechInput();
            }

            @Override
            public void onTripleClick(View view) {
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.setData(Constants.KEY_DIARY, b);
                myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
            }

            @Override
            public void onFourthClick(View view) {
                getActivity().onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");
            }
        }));


        tvTitle.setText("Diary");
        fragmentAppDemo.speakerbox.play("Diary");

        if (fragmentAppDemo.getDb().diaryDao().countNotes() != 0) {
            diaryDBModels.addAll(fragmentAppDemo.getDb().diaryDao().getAll());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");

            }
        });

        imageMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
//                Date c = Calendar.getInstance().getTime();
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                String formattedDate = df.format(c);
//                // tvDate.setText(formattedDate);
//                Log.d("Tag", formattedDate);
            }
        });


        if (b) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setData(Constants.KEY_DIARY, b);

            Preference.getInstance().setData(Preference.DIARY_IS_FIRST_TIME, false);

            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
        }

        recyclerView.addItemDecoration(new HorizontalSpacingDecoration(2));

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DiaryRecyclerAdapter(getActivity(), diaryDBModels, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onPosition(int pos) {
        Log.d("TAG", "On Pos = " + pos);
        fragmentAppDemo.speakerbox.play(diaryDBModels.get(pos).getBody());
        Log.d("TAG", "speakResult" + pos);
    }

    public void openHome() {
        getActivity().onBackPressed();
        Log.d("TAG", "OnFourClick");
        fragmentAppDemo.speakerbox.play("Home");

    }

    public void openInfoPage() {
        Log.d("TAG", "InfoOpened");
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.setData(Constants.KEY_DIARY, b);
        myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
    }

    public void openMic(){
        getSpeechInput();
    }


    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getActivity(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && result.size() != 0) {
                        DiaryDBModel diaryDBModel = new DiaryDBModel(result.get(0));

                        diaryDBModels.add(diaryDBModel);
                        Log.d("Tag", "Result" + result);

                        fragmentAppDemo.getDb().diaryDao().insertAll(diaryDBModel);

//                        diaryDBModels=fragmentAppDemo.getDb().diaryDao().getAll();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

}