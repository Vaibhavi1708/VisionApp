package com.example.vision.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vision.adapters.InboxRecyclerAdapter;
import com.example.vision.models.ContactsDBModel;
import com.example.vision.models.MessageModel;
import com.example.vision.utils.Preference;
import com.example.vision.R;
import com.example.vision.utils.Constants;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;
import com.example.vision.utils.HorizontalSpacingDecoration;
import com.example.vision.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class InboxFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks, InboxRecyclerAdapter.OnPositionListener {

    private RecyclerView recyclerView;
    private TextView tvMessage;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    final ArrayList<MessageModel> messageModels = new ArrayList();
    private final int RC_SMS = 102;

    boolean b = Preference.getInstance().getData(Preference.MESSAGE_IS_FIRST_TIME, true);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_inbox, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_inbox);
        tvMessage = view.findViewById(R.id.tvMessage);

        tvMessage.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                fragmentAppDemo.speakerbox.play(getString(R.string.empty_message));
            }

            @Override
            public void onDoubleClick(View view) {
                Log.d("TAG", "OnDoubleClick");
            }

            @Override
            public void onTripleClick(View view) {
                openInfoPage();

            }

            @Override
            public void onFourthClick(View view) {
                getActivity().onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");
            }
        }));

        //to display dialog fragment

     //   boolean b = Preference.getInstance().getData(Preference.MESSAGE_IS_FIRST_TIME, true);

        if (b) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setData(Constants.KEY_MESSAGE, b);

            Preference.getInstance().setData(Preference.MESSAGE_IS_FIRST_TIME, false);

            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
        }
//

        recyclerView.addItemDecoration(new HorizontalSpacingDecoration(2));

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InboxRecyclerAdapter(this, messageModels, getActivity());
        recyclerView.setAdapter(adapter);

        checkSMSPermission();

        return view;

    }

    @Override
    public void onPosition(int pos) {
        Log.d("TAG", "OnSingeClick " + pos);
        if (!TextUtils.isEmpty(messageModels.get(pos).getName())) {
            Log.d("Tag","condition true");
            fragmentAppDemo.speakerbox.play(messageModels.get(pos).getName() + messageModels.get(pos).getMessage());
        } else {
            Log.d("Tag","condition false");
            fragmentAppDemo.speakerbox.play(messageModels.get(pos).getPhone() + messageModels.get(pos).getMessage());

        }

    }




    public void openHome() {
        getActivity().onBackPressed();
        Log.d("TAG", "OnFourClick");
        fragmentAppDemo.speakerbox.play("Home");

    }

    public void openInfoPage() {
        Log.d("TAG", "InfoOpened");

        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.setData(Constants.KEY_MESSAGE, b);
        myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
    }

    @AfterPermissionGranted(RC_SMS)
    private void checkSMSPermission() {
        String[] perms = {Manifest.permission.READ_SMS};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            getALLSMSData();


        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Please grant SMS permission to view Inbox items.",
                    RC_SMS, perms);
        }
    }

    private void getALLSMSData() {

        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[]{"_id", "address", "body"};

        // Get Content Resolver object, which will deal with Content
        // Provider
        ContentResolver cr = getActivity().getContentResolver();

        // Fetch Sent Inbox Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        if (c != null) {
            Log.d("Tag", "size = " + c.getCount());

            while (c.moveToNext()) {
                String phone = c.getString(c.getColumnIndex("address"));
                String message = c.getString(c.getColumnIndexOrThrow("body"));
                String _id = c.getString(c.getColumnIndex("_id"));
                MessageModel messageModel = new MessageModel(_id, phone, message);
                ContactsDBModel contactsDBModel ;

                contactsDBModel = fragmentAppDemo.getDb().contactsDao().findByName(Utils.removeExtraCharacters(phone));
                if(contactsDBModel != null){
                    messageModel.setName(contactsDBModel.getFname());

                }

                messageModels.add(messageModel);
                Log.d("Tag", "id : " + _id + "Number: " + phone + " .Message: " + message);

            }
            c.close();

            adapter.notifyDataSetChanged();
        }

        if (messageModels.size() == 0) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(R.string.empty_message);
            recyclerView.setVisibility(View.GONE);

        } else {
            tvMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("TAG", "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onPause() {
        fragmentAppDemo.speakerbox.stop();
        super.onPause();
    }
}