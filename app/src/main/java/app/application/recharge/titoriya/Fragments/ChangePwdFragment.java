package app.application.recharge.titoriya.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/8/2017.
 */

public class ChangePwdFragment extends Fragment implements View.OnClickListener {
    EditText crt_pwd, new_pwd, cnfm_pwd, crt_pin, new_pin, cnfm_pin;
    Button upd_pwd, upd_pin;
    TextView title;
    LinearLayout bck_ly;
    ProgressDialog progressDialog;
    Dialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword, container, false);
        inIt(view);
        return view;
    }

    private void inIt(View view) {
        title = (TextView) view.findViewById(R.id.title);
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        title.setText("Change Password");

        crt_pwd = (EditText) view.findViewById(R.id.crt_pwd);
        new_pwd = (EditText) view.findViewById(R.id.new_pwd);
        cnfm_pwd = (EditText) view.findViewById(R.id.cnfm_pwd);

        crt_pin = (EditText) view.findViewById(R.id.crt_pin);
        new_pin = (EditText) view.findViewById(R.id.new_pin);
        cnfm_pin = (EditText) view.findViewById(R.id.cnfm_pin);

        upd_pwd = (Button) view.findViewById(R.id.upd_pwd);
        upd_pin = (Button) view.findViewById(R.id.upd_pin);

        upd_pin.setOnClickListener(this);
        upd_pwd.setOnClickListener(this);
        bck_ly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upd_pin:
                if (crt_pin.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Current Pin");
                } else if (new_pin.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter New Pin");
                } else if (cnfm_pin.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Confirm Pin");
                } else if (new_pin.getText().length() < 4) {
                    Util.errorDialog(getActivity(), "Please choose a pin with at least 4 characters");
                } else if (!cnfm_pin.getText().toString().equalsIgnoreCase(new_pin.getText().toString())) {
                    Util.errorDialog(getActivity(), "Please make sure your pin match");
                } else {
                    Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.upd_pwd:
                if (crt_pwd.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Current Password");
                } else if (new_pwd.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter New Password");
                } else if (cnfm_pwd.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Confirm Password");
                } else if (new_pwd.getText().length() < 6) {
                    Util.errorDialog(getActivity(), "Please choose a password with at least 6 characters");
                } else if (!cnfm_pwd.getText().toString().equalsIgnoreCase(new_pwd.getText().toString())) {
                    Util.errorDialog(getActivity(), "Please make sure your passwords match");
                } else {


                    new OTP_Verification(getActivity()).execute();



                    //new ChangePassword(getActivity()).execute();


                }
                break;
            case R.id.bck_ly:
                Fragment fragment = new RetailerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
                break;
        }

    }

    private class ChangePassword extends AsyncTask<String, Void, String> {
        Context context;

        public ChangePassword(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("oldp", crt_pwd.getText().toString());
            map.put("newp", new_pwd.getText().toString());

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.CHANGE_PASSWORD,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);
            Util.cancelPgDialog(progressDialog);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        Util.errorDialog(context,jsonObject.optString("msg"));
                        crt_pwd.setText("");
                        new_pwd.setText("");
                        cnfm_pwd.setText("");



                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }
                    else {
                        Util.errorDialog(context,jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private class OTP_Verification extends AsyncTask<String, Void, String> {
        Context context;

        public OTP_Verification(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("mobile", MyPrefrences.getMobileNoNew(context));


            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.OTP_VERIFY,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);
            Util.cancelPgDialog(progressDialog);
            try {
                final JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        // Toast.makeText(context, jsonObject.optString("otp"), Toast.LENGTH_SHORT).show();

                        dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.enter_otp_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        Button subbmit = (Button) dialog.findViewById(R.id.subbmit);
                        final EditText pin_edt = (EditText) dialog.findViewById(R.id.pin_edt);

                        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });
                        subbmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!pin_edt.getText().toString().isEmpty()){

//
                                    if (jsonObject.optString("otp").contains(pin_edt.getText().toString())){

                                        new ChangePassword(getActivity()).execute();
                                        dialog.show();
                                    }
                                    else{
                                        Util.errorDialog(getActivity(),"Invalid OTP");
                                    }
//                                    new UpdateProfileDetails(getActivity()).execute();
                                }
                                else{
                                    Util.errorDialog(getActivity(),"Please enter OTP");
                                }
                            }
                        });
                        dialog.show();


//                        Util.errorDialog(context,jsonObject.optString("msg"));



                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }
                    else {
                        Util.errorDialog(context,jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
