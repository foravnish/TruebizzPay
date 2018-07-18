package app.application.recharge.titoriya.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {
    LinearLayout bck_ly;
    TextView title;
    Button btn_cancel,btn_submit;
    EditText name_val,us_name_val,mob_val,email_val,address_val,city_val,pin_val,comp_val,balance_val;
    ProgressDialog progressDialog;
    Dialog dialog;
    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Edit Profile");

        name_val=(EditText)view.findViewById(R.id.name_val);
        us_name_val=(EditText)view.findViewById(R.id.us_name_val);
        mob_val=(EditText)view.findViewById(R.id.mob_val);
        email_val=(EditText)view.findViewById(R.id.email_val);
        address_val=(EditText)view.findViewById(R.id.address_val);
        city_val=(EditText)view.findViewById(R.id.city_val);
        pin_val=(EditText)view.findViewById(R.id.pin_val);
        comp_val=(EditText)view.findViewById(R.id.comp_val);
        balance_val=(EditText)view.findViewById(R.id.balance_val);

        btn_submit=(Button)view.findViewById(R.id.btn_submit);
        btn_cancel=(Button)view.findViewById(R.id.btn_cancel);

        name_val.setText(getArguments().getString("name"));
        us_name_val.setText(getArguments().getString("usernme"));
        mob_val.setText(getArguments().getString("mobile"));
        email_val.setText(getArguments().getString("email"));
        address_val.setText(getArguments().getString("address"));
        city_val.setText(getArguments().getString("city"));
        pin_val.setText(getArguments().getString("pin"));
        comp_val.setText(getArguments().getString("company"));
        balance_val.setText(getArguments().getString("balance"));

        mob_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cant't change mobile no", Toast.LENGTH_SHORT).show();
            }
        });
        email_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cant't change Email id", Toast.LENGTH_SHORT).show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new OTP_Verification(getActivity()).execute();




                      // new UpdateProfileDetails(getActivity()).execute();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
            }
        });

        bck_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new ProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
            }
        });
        return  view;
    }

    private class UpdateProfileDetails extends AsyncTask<String, Void, String> {
        Context context;

        public UpdateProfileDetails(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));

            map.put("name", name_val.getText().toString());
            map.put("email", email_val.getText().toString());
            map.put("user", us_name_val.getText().toString());
            map.put("cname", comp_val.getText().toString());
            map.put("city", city_val.getText().toString());
            map.put("mobile", mob_val.getText().toString());
            map.put("pin", pin_val.getText().toString());
            map.put("address", address_val.getText().toString());

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.PROFILE_UPDATE,"GET",map);

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
                        dialog.dismiss();

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

            Log.d("fgfdgdfgdfhdg",MyPrefrences.getMobileNoNew(context));

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

                      //  Toast.makeText(context, jsonObject.optString("otp"), Toast.LENGTH_SHORT).show();

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

                                        new UpdateProfileDetails(getActivity()).execute();
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
