package app.application.recharge.titoriya;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.Fragments.EditProfile;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

public class Registration extends AppCompatActivity {

    EditText name_edt,email_id,mobile_edt,pwd,pwdConfirm,pin,email_id2;
    Button submit_btn;
    Dialog dialog;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        submit_btn=(Button)findViewById(R.id.submit_btn);

        name_edt=(EditText)findViewById(R.id.name_edt);
        email_id=(EditText)findViewById(R.id.email_id);
        email_id2=(EditText)findViewById(R.id.email_id2);
        mobile_edt=(EditText)findViewById(R.id.mobile_edt);
        pwd=(EditText)findViewById(R.id.pwd);
        pwdConfirm=(EditText)findViewById(R.id.pwdConfirm);
        pin=(EditText)findViewById(R.id.pin);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Name");
                } else if (email_id.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter User ID");
                }else if (email_id2.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Email ID");
                }
                else if (mobile_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Mobile No");
                }
                else if (mobile_edt.getText().toString().length()<10) {
                    Util.errorDialog(Registration.this, "Please Enter Valid Mobile No");
                }
                else if (pwd.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Password");
                }
                else if (pwdConfirm.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Confirm Password");
                }
                else if (!pwdConfirm.getText().toString().equalsIgnoreCase(pwd.getText().toString())) {
                    Util.errorDialog(Registration.this, "Password mismatch");
                }
                else if (pwd.getText().toString().length()<8) {
                    Util.errorDialog(Registration.this, "Please Enter 8 digit Password");
                }
                else if (pwdConfirm.getText().toString().length()<8) {
                    Util.errorDialog(Registration.this, "Please Enter 8 digit Confirm Password");
                }
                else if (pin.getText().toString().isEmpty()) {
                    Util.errorDialog(Registration.this, "Please Enter Pin");
                }
                else if (pin.getText().toString().length()<4) {
                    Util.errorDialog(Registration.this, "Please Enter 4 digit Pin");
                }
                else {

                    new OTP_Verification(Registration.this,mobile_edt).execute();


                }
            }
        });

    }



    private class RegistrationData extends AsyncTask<String, Void, String> {
        Context context;

        public RegistrationData(Context context, EditText name_edt, EditText email_id, EditText email_id2, EditText mobile_edt, EditText pwd, EditText pin) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();



            map.put("name", name_edt.getText().toString());
            map.put("username", email_id.getText().toString());
            map.put("password", pwd.getText().toString());
            map.put("email", email_id2.getText().toString());
            map.put("mobile", mobile_edt.getText().toString());
            map.put("pin", pin.getText().toString());


            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.REGISTRATION_USER,"GET",map);

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

                        //Util.errorDialog(context,jsonObject.optString("msg"));
                        Toast.makeText(context, ""+jsonObject.optString("msg")+"! Please Login", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Registration.this, LoginActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Email/username/mobile already exists!!")){
                        Util.errorDialog(context,jsonObject.optString("msg"));
                        dialog.dismiss();
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
        EditText mobile_edt;
        public OTP_Verification(Context context, EditText mobile_edt) {
            this.context = context;
            this.mobile_edt=mobile_edt;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


//            map.put("user_token", MyPrefrences.getToken(context));
//            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("mobile", mobile_edt.getText().toString());


            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.REGISTRATION_OTP_VERIFY,"GET",map);

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

                        dialog = new Dialog(Registration.this);
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

                                        new RegistrationData(Registration.this,name_edt,email_id,email_id2,mobile_edt,pwd,pin).execute();
                                        dialog.show();
                                    }
                                    else{
                                        Util.errorDialog(Registration.this,"Invalid OTP");
                                    }
//                                    new UpdateProfileDetails(getActivity()).execute();
                                }
                                else{
                                    Util.errorDialog(Registration.this,"Please enter OTP");
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
