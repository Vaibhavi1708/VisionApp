package com.example.vision.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vision.models.MessageModel;
import com.example.vision.R;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;

import java.util.ArrayList;

public class InboxRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    private ArrayList<MessageModel> items;
    private OnPositionListener onPositionListener;

    public InboxRecyclerAdapter(OnPositionListener onPositionListener, ArrayList<MessageModel> messageModels, Context context) {
        this.onPositionListener = onPositionListener;
        items = messageModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_inbox_layout, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        viewHolder.itemView.setTag(position);
        final MessageModel messageModel = items.get(position);
        if (!TextUtils.isEmpty(messageModel.getName())) {
            ((MyViewHolder) viewHolder).tvPhone.setText(messageModel.getName());

        } else {
            ((MyViewHolder) viewHolder).tvPhone.setText(messageModel.getPhone());

        }
        ((MyViewHolder) viewHolder).tvMessage.setText(messageModel.getMessage());
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMessage;
        public TextView tvPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvPhone = itemView.findViewById(R.id.tvPhone);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = (int) view.getTag();
//                    onPositionListener.onPosition(pos);
//
//                }
//            });

            itemView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    int pos = (int) view.getTag();
                    onPositionListener.onPosition(pos);
                }

                @Override
                public void onDoubleClick(View view) {

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
    }
}
