package com.example.vision.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vision.R;
import com.example.vision.ViewPagerAdapter;
import com.example.vision.activities.MainActivity;
import com.example.vision.models.ContactsDBModel;
import com.example.vision.utils.Utils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MessageFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    private static final int RC_SMS = 101;
    private AppCompatImageButton btnBack;
    private TextView tvTitle;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        btnBack = view.findViewById(R.id.backImgButton);

        tvTitle.setText("Message");
        fragmentAppDemo.speakerbox.play("Message " + " Inbox");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebViewFragment.this, new RegisterFragment());
                ((MainActivity) getActivity()).onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");
            }
        });
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        checkSMSPermission();

        return view;

    }

    @AfterPermissionGranted(RC_SMS)
    private void checkSMSPermission() {
        String[] perms = {Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            if(fragmentAppDemo.getDb().contactsDao().countContacts() != 0){
                initViewPager();
            }else{
                getContactList();
            }

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Please grant SMS permission to view Message.",
                    RC_SMS, perms);
        }
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    fragmentAppDemo.speakerbox.play("Inbox");
                } else if (i == 1) {
                    fragmentAppDemo.speakerbox.play(" Sent Items");
                } else {
                    fragmentAppDemo.speakerbox.play("Compose");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getContactList() {


        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {

            Log.d("Tag", "contacts size = " + cur.getCount());
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    Log.d(TAG, "Name: " + name);

                    ContactsDBModel contactsDBModel = new ContactsDBModel();
                    contactsDBModel.setFname(name);

                    if (pCur != null && pCur.getCount() > 0) {
                        for (int i = 0; i < pCur.getCount(); i++) {

                            pCur.moveToNext();
                            if (i == 0) {
                                String mobile = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d(TAG, "mobile: " + mobile);
                                contactsDBModel.setMobile(Utils.removeExtraCharacters(mobile));

                            } else if (i == 1) {
                                String work = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d(TAG, "work: " + work);
                                contactsDBModel.setWork(Utils.removeExtraCharacters(work));

                            } else if (i == 2) {
                                String home = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d(TAG, "home: " + home);
                                contactsDBModel.setHome(Utils.removeExtraCharacters(home));

                            } else if (i == 3) {
                                String main = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d(TAG, "main: " + main);
                                contactsDBModel.setMain(Utils.removeExtraCharacters(main));

                            }


                        }

                    }

                    fragmentAppDemo.getDb().contactsDao().insertAll(contactsDBModel);


// while (pCur.moveToNext()) {
// String phoneNo = pCur.getString(pCur.getColumnIndex(
// ContactsContract.CommonDataKinds.Phone.NUMBER));

                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        initViewPager();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
    public void onDestroy() {
        fragmentAppDemo.speakerbox.stop();
        super.onDestroy();
    }

}


