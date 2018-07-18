package app.application.recharge.titoriya.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.application.recharge.titoriya.connection.InternetStatus;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/14/2017.
 */

public class MoneyTransferWalletLoginFragment extends Fragment implements View.OnClickListener {
    TextView title;
    LinearLayout bck_ly, reg_user_ly, otp_ly, login_ly;
    Button title_btn, login, sbt_btn, otp_sbt_btn;
    EditText cust_mob_edt, reg_mob_edt, fstname_edt,  otp_code_edt;
    String Otc_Ref = "";
    public static String cust_mob_num = "";
    ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walletlogin, container, false);
        inIt(view);
        return view;
    }

    private void inIt(View view) {
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Money Transfer Wallet");

        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        reg_user_ly = (LinearLayout) view.findViewById(R.id.reg_user_ly);
        otp_ly = (LinearLayout) view.findViewById(R.id.otp_ly);
        login_ly = (LinearLayout) view.findViewById(R.id.login_ly);

        cust_mob_edt = (EditText) view.findViewById(R.id.cust_mob_edt);
        reg_mob_edt = (EditText) view.findViewById(R.id.reg_mob_edt);
        fstname_edt = (EditText) view.findViewById(R.id.fstname_edt);
//        lstname_edt = (EditText) view.findViewById(R.id.lstname_edt);
        otp_code_edt = (EditText) view.findViewById(R.id.otp_code_edt);

        title_btn = (Button) view.findViewById(R.id.title_btn);
        login = (Button) view.findViewById(R.id.login);
        sbt_btn = (Button) view.findViewById(R.id.sbt_btn);
        otp_sbt_btn = (Button) view.findViewById(R.id.otp_sbt_btn);


        bck_ly.setOnClickListener(this);
        login.setOnClickListener(this);
        sbt_btn.setOnClickListener(this);
        otp_sbt_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bck_ly:
                Fragment fragment = new RetailerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
                break;
            case R.id.login:
                if (cust_mob_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Mobile Number");
                } else if (cust_mob_edt.getText().toString().length() < 10) {
                    Util.errorDialog(getActivity(), "Please Enter Valid Mobile Number");
                } else if (InternetStatus.isConnectingToInternet(getActivity())) {
                    cust_mob_num = cust_mob_edt.getText().toString();
                    new CustomerValidationAync(getActivity(), cust_mob_edt.getText().toString()).execute();



                }
                break;
            case R.id.sbt_btn:
                if (reg_mob_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Mobile Number");
                } else if (fstname_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter First Name");
                }
//                else if (lstname_edt.getText().toString().isEmpty()) {
//                    Util.errorDialog(getActivity(), "Please Enter Last Name");
//                }
                else if (reg_mob_edt.getText().toString().length() < 10) {
                    Util.errorDialog(getActivity(), "Please Enter Valid Mobile Number");
                } else {

                    cust_mob_num = reg_mob_edt.getText().toString();
                    String name = fstname_edt.getText().toString().replaceAll(" ","");
                    new CustomerRegisterAync(getActivity(), reg_mob_edt.getText().toString(), name).execute();

                }
                break;
            case R.id.otp_sbt_btn:
                if (otp_code_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter OTP");
                } else if (otp_code_edt.getText().toString().length() < 4) {
                    Util.errorDialog(getActivity(), "Please Enter Valid OTP");
                } else {
                    new CustomerOtpConfirmAync(getActivity(), cust_mob_num, otp_code_edt.getText().toString(), Otc_Ref).execute();
                }
                break;
        }
    }

    public class CustomerValidationAync extends AsyncTask<String, Void, String> {
        String cust_mob_num;
        Context context;




        public CustomerValidationAync(Context context, String cust_mob_num) {
            this.context = context;
            this.cust_mob_num = cust_mob_num;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", cust_mob_num.toString());
            params.put("user_token", MyPrefrences.getToken(context));
            params.put("agent_id", MyPrefrences.getUserId(context));

            Log.d("dfgvdfgdfgdd",cust_mob_num.toString());
            Log.d("dfgvdfgdfgdd",MyPrefrences.getToken(context));
            Log.d("dfgvdfgdfgdd",MyPrefrences.getUserId(context));
            Log.d("dfgvdfgdfgdd",MyPrefrences.getUserPin(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Validation_Url_new, "GET", params);
            return result;
        }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response", ": " + s);
                Log.d("dfgdfhgbdfhdgdfdgh",s.toString());

                Util.cancelPgDialog(progressDialog);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.optString("status").equals("1")){
                        MyPrefrences.setUserMobile(context, cust_mob_num);
                        Fragment customerWalletFragment = new CustomerWalletFragment1();
                        FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("jsondata",s.toString());
                        customerWalletFragment.setArguments(bundle1);
                        fragmentTransaction1.replace(R.id.container, customerWalletFragment).addToBackStack("one");
                        fragmentTransaction1.commit();

                    }

                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else if (jsonObject.optString("status").equals("0")){

                        login_ly.setVisibility(View.GONE);
                        reg_user_ly.setVisibility(View.VISIBLE);
                        reg_mob_edt.setText(cust_mob_num.toString());
                        title_btn.setText("Money Transfer Wallet Login");
                        Toast.makeText(context, "Please Registration...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error: Please connect to the internet", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
    }


    public class CustomerRegisterAync extends AsyncTask<String, Void, String> {
        String cust_mob_num, name;
        Context context;

        public CustomerRegisterAync(Context context, String cust_mob_num, String name) {
            this.context = context;
            this.cust_mob_num = cust_mob_num;
            this.name = name;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
//            params.put("fname", name.toString());
            params.put("fname",name.toString() );
            params.put("mobile", cust_mob_num.toString());
            params.put("user_token", MyPrefrences.getToken(context));

            Log.d("dfgdfgdfdgdhfdhddgfddg",name);
            Log.d("dfgdfgdfdgdhfdhddgfddg",cust_mob_num);
            Log.d("dfgdfgdfdgdhfdhddgfddg",MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Registration_Url_new, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", ": " + s);
            Util.cancelPgDialog(progressDialog);
            Log.d("gdfgdfdgh",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                        Log.d("gdfgdfdgh",jsonObject.optString("msg"));
                        Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_LONG).show();
                        Otc_Ref = jsonObject.optString("item");
                        Util.Register_Mobile_Number = Long.valueOf(cust_mob_num);
//                        MyPrefrences.setUserMobile(context, cust_mob_num);
                        title_btn.setText("Confirm OTP");

                        reg_user_ly.setVisibility(View.GONE);
                        otp_ly.setVisibility(View.VISIBLE);
                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



/////////////////////// Customer Otp Confirm Async Task

    public class CustomerOtpConfirmAync extends AsyncTask<String, Void, String> {
        String cust_mob_num, otp, otp_ref;
        Context context;

        public CustomerOtpConfirmAync(Context context, String cust_mob_num, String otp, String otp_ref) {
            this.context = context;
            this.cust_mob_num = cust_mob_num;
            this.otp = otp;
            this.otp_ref = otp_ref;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("otc", otp);
            params.put("otcref",otp_ref );
            params.put("mobile", cust_mob_num);
            params.put("user_token", MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_OTP_Url_new, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("responsegfhfghfgh", ": " + s);
            Util.cancelPgDialog(progressDialog);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(context, "User Register Successfully", Toast.LENGTH_SHORT).show();
                        title_btn.setText("Money Transfer Wallet Login");
                        otp_ly.setVisibility(View.GONE);
                        login_ly.setVisibility(View.VISIBLE);
                    } else {
                        Util.errorDialog(context, jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //////////////////////////////////////////////
}
