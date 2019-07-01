package com.example.vision.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.dialer.dialpadview.DialpadView;
import com.android.dialer.dialpadview.DigitsEditText;
import com.android.dialer.dialpadview.R;
import com.example.vision.activities.MainActivity;
import com.example.vision.utils.Preference;
import com.example.vision.utils.Constants;
import com.example.vision.utils.DoubleClick;
import com.example.vision.utils.DoubleClickListener;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class DialpadFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    public final static String EXTRA_REGION_CODE = "EXTRA_REGION_CODE";
    public final static String EXTRA_FORMAT_AS_YOU_TYPE = "EXTRA_FORMAT_AS_YOU_TYPE";
    public final static String EXTRA_ENABLE_STAR = "EXTRA_ENABLE_STAR";
    public final static String EXTRA_ENABLE_POUND = "EXTRA_ENABLE_POUND";
    public final static String EXTRA_ENABLE_PLUS = "EXTRA_ENABLE_PLUS";
    public final static String EXTRA_CURSOR_VISIBLE = "EXTRA_CURSOR_VISIBLE";

    private final static String DEFAULT_REGION_CODE = "US";

    private DigitsEditText digits;
    private AsYouTypeFormatter formatter;
    private String input = "";
    private Callback callback;
    private String regionCode = DEFAULT_REGION_CODE;
    private boolean formatAsYouType = false;
    private boolean enableStar = true;
    private boolean enablePound = true;
    private boolean enablePlus = true;
    private boolean cursorVisible = false;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 103;
    String phone_space;
    private TextView tvTitle;
    private AppCompatImageButton btnBack;

    boolean b = Preference.getInstance().getData(Preference.CALL_IS_FIRST_TIME, true);


    private DoubleClick doubleClick = new DoubleClick(new DoubleClickListener() {
        @Override
        public void onSingleClick(View view) {
            if (view.getId() == R.id.zero) {
                append('0');
            } else if (view.getId() == R.id.one) {
                append('1');
            } else if (view.getId() == R.id.two) {
                append('2');
            } else if (view.getId() == R.id.three) {
                append('3');
            } else if (view.getId() == R.id.four) {
                append('4');
            } else if (view.getId() == R.id.five) {
                append('5');
            } else if (view.getId() == R.id.six) {
                append('6');
            } else if (view.getId() == R.id.seven) {
                append('7');
            } else if (view.getId() == R.id.eight) {
                append('8');
            } else if (view.getId() == R.id.nine) {
                append('9');
            } else if (view.getId() == R.id.star) {
                append('*');
            } else if (view.getId() == R.id.pound) {
                append('#');
            } else if (view.getId() == R.id.fab_ok) {
                checkCallPermission();
            }
        }

        @Override
        public void onDoubleClick(View view) {

           checkCallPermission();
        }

        @Override
        public void onTripleClick(View view) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();

            myDialogFragment.setData(Constants.KEY_CALL, false);
            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());

        }

        @Override
        public void onFourthClick(View view) {
            getActivity().onBackPressed();
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = savedInstanceState;
        if (arguments == null) {
            arguments = getArguments();
        }
        if (arguments != null) {
            regionCode = arguments.getString(EXTRA_REGION_CODE, DEFAULT_REGION_CODE);
            formatAsYouType = arguments.getBoolean(EXTRA_FORMAT_AS_YOU_TYPE, formatAsYouType);
            enableStar = arguments.getBoolean(EXTRA_ENABLE_STAR, enableStar);
            enablePound = arguments.getBoolean(EXTRA_ENABLE_POUND, enablePound);
            enablePlus = arguments.getBoolean(EXTRA_ENABLE_PLUS, enablePlus);
            cursorVisible = arguments.getBoolean(EXTRA_CURSOR_VISIBLE, cursorVisible);
        }


        View view = inflater.inflate(com.example.vision.R.layout.fragment_call, container, false);
        DialpadView dialpadView = (DialpadView) view.findViewById(R.id.dialpad_view);
        dialpadView.setShowVoicemailButton(false);
        digits = (DigitsEditText) dialpadView.getDigits();
        digits.setCursorVisible(cursorVisible);
// if region code is null, no formatting is performed
        formatter = PhoneNumberUtil.getInstance()
                .getAsYouTypeFormatter(formatAsYouType ? regionCode : "");

//        view.findViewById(R.id.fab_ok)
        view.setOnClickListener(doubleClick);
        view.findViewById(R.id.fab_ok).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.zero).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.one).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.two).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.three).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.four).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.five).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.six).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.seven).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.eight).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.nine).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.star).setOnClickListener(doubleClick);
        dialpadView.findViewById(R.id.pound).setOnClickListener(doubleClick);


        tvTitle= view.findViewById(com.example.vision.R.id.tvTitle);
        btnBack = view.findViewById(com.example.vision.R.id.backImgButton);

        tvTitle.setText("Call");
        fragmentAppDemo.speakerbox.play("Call");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WebViewFragment.this, new RegisterFragment());
                ((MainActivity) getActivity()).onBackPressed();
                fragmentAppDemo.speakerbox.play("Home");

            }
        });



        if (b) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();


            myDialogFragment.setData(Constants.KEY_CALL, b);
            Preference.getInstance().setData(Preference.CALL_IS_FIRST_TIME, false);

            myDialogFragment.show(getFragmentManager(), MyDialogFragment.class.getSimpleName());
        }


        if (enablePlus) {
            dialpadView.findViewById(R.id.zero).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    append('+');
                    return true;
                }
            });
        }
// if (enablePound) {
//            dialpadView.findViewById(R.id.pound).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    append('#');
//                }
//            });
//        } else {
//            dialpadView.findViewById(R.id.pound).setVisibility(View.GONE);
//        }
        dialpadView.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poll();
            }
        });


        dialpadView.getDeleteButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clear();
                return true;
            }
        });

        digits.setOnTextContextMenuClickListener(
                new DigitsEditText.OnTextContextMenuClickListener() {
                    @Override
                    public void onTextContextMenuClickListener(int id) {
                        String string = digits.getText().toString();
                        clear();
                        for (int i = 0; i < string.length(); i++) {
                            append(string.charAt(i));
                        }
                    }
                });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Log.d("Tag", "permission was granted, yay! Do the phone call");
//                    callNumber();
//                } else {
//
//                    Log.d("Tag", "permission denied, boo! Disable the functionality that depends on this permission.");
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_CALL_PHONE)
    private void checkCallPermission() {

        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            callNumber();

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Please grant Call Permission",
                    MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        }
    }


    public void callNumber() {
        final String phone = digits.getText().toString();

        for (int i = 0; i < phone.length(); i++) {
            if (i == 0) {
                phone_space = (phone.charAt(i) + " ");
            } else {
                phone_space += (phone.charAt(i) + " ");
            }

        }
        if (TextUtils.isEmpty(phone)) {
            fragmentAppDemo.speakerbox.play("To place a call,enter a valid number");

        } else {

            fragmentAppDemo.speakerbox.play(phone_space);
            Log.d("Tag", phone_space);

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    calling();
//                    Intent i = new Intent(Intent.ACTION_CALL);
//                    i.setData(Uri.parse("tel:" + phone));
//
//
//                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.CALL_PHONE},
//                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
//
//                        // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
//                        // app-defined int constant. The callback method gets the
//                        // result of the request.
//                    } else {
//                        //You already have permission
//                        try {
//                            startActivity(i);
//                        } catch (SecurityException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }, phone.length() * 350);
        }
    }

    public void calling() {
        final String phone = digits.getText().toString();
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + phone));
        //You already have permission
        try {
            startActivity(i);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_REGION_CODE, regionCode);
        outState.putBoolean(EXTRA_FORMAT_AS_YOU_TYPE, formatAsYouType);
        outState.putBoolean(EXTRA_ENABLE_STAR, enableStar);
        outState.putBoolean(EXTRA_ENABLE_POUND, enablePound);
        outState.putBoolean(EXTRA_ENABLE_PLUS, enablePlus);
        outState.putBoolean(EXTRA_CURSOR_VISIBLE, cursorVisible);
    }

    private void poll() {
        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);
            formatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(regionCode);
            if (formatAsYouType) {
                digits.setText("");
                for (char c : input.toCharArray()) {
                    digits.setText(formatter.inputDigit(c));
                }
            } else {
                digits.setText(input);
            }
        }
    }

    private void clear() {
        formatter.clear();
        digits.setText("");
        input = "";
    }

    public void append(char c) {
        input += c;
        if (formatAsYouType) {
            digits.setText(formatter.inputDigit(c));
        } else {
            digits.setText(input);
        }
        fragmentAppDemo.speakerbox.play(String.valueOf(c));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    public interface Callback {
        void ok(String formatted, String raw);
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
}
