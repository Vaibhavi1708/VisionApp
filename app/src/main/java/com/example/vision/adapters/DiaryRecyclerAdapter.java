package com.example.vision.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vision.R;
import com.example.vision.models.DiaryDBModel;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;

import java.util.List;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.MyViewHolder> {
    Context context;
    List<DiaryDBModel> items;

    private DiaryRecyclerAdapter.OnPositionListener onPositionListener;

    public DiaryRecyclerAdapter(Context context, List<DiaryDBModel> items, OnPositionListener onPositionListener) {
        this.context = context;
        this.items = items;
        this.onPositionListener = onPositionListener;
    }

    @NonNull
    @Override
    public DiaryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_diary_layout, viewGroup, false);
        final DiaryRecyclerAdapter.MyViewHolder myViewHolder = new DiaryRecyclerAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DiaryRecyclerAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);
        final DiaryDBModel diaryDBModel = items.get(position);

        viewHolder.tvBody.setText(diaryDBModel.getBody());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBody;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBody = itemView.findViewById(R.id.tvBody);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    onPositionListener.onPosition(pos);

                }

            });

            itemView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    int pos = (int) view.getTag();
                    onPositionListener.onPosition(pos);
                }

                @Override
                public void onDoubleClick(View view) {
                    onPositionListener.openMic();
                }

                @Override
                public void onTripleClick(View view) {
                    onPositionListener.openInfoPage();
                }

                @Override
                public void onFourthClick(View view) {
                    onPositionListener.openHome();
                }
            }));

        }
    }

    public interface OnPositionListener {
        void onPosition(int pos);

        void openHome();

        void openInfoPage();

        void openMic();

    }

}