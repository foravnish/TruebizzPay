package app.application.recharge.titoriya.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.MainActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 6/26/2017.
 */

public class LoginAsync extends AsyncTask<String, Void, String> {
    String username, password;
    Context context;
    ProgressDialog progressDialog;
    Dialog dialog2;
    public LoginAsync(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
//        params.put("username", username);
//        params.put("password", password);

        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.URL_STORE_TOKEN+"?username="+username+"&password="+password, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Util.cancelPgDialog(progressDialog);
        Log.e("response", ": " + s);
        String msg=" ";
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject != null) {
                msg=jsonObject.optString("msg");
                if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                    //JSONObject jobt=jsonObject.getJSONObject("token");


                    Button subbmit,cancel_btn;
                    final EditText pin_edt;
                    dialog2 = new Dialog(context);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog2.setContentView(R.layout.enter_pin_dialog);
                    dialog2.setCancelable(false);

                    subbmit=(Button)dialog2.findViewById(R.id.subbmit);
                    cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
                    pin_edt=(EditText)dialog2.findViewById(R.id.pin_edt);

                    subbmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new LoginPin(context,pin_edt.getText().toString(),username,password).execute();

                        }
                    });
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    dialog2.show();

                } else {
                    Toast.makeText(context,""+msg, Toast.LENGTH_SHORT).show();

                    //Util.errorDialog(context, jsonObject.optString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class LoginPin extends AsyncTask<String, Void, String> {
        String pin,username,password;
        Context context;
        ProgressDialog progressDialog;

        public LoginPin(Context context, String pin, String username, String password) {
            this.context = context;
            this.username = username;
            this.password = password;
            this.pin = pin;

            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            //params.put("user_token", MyPrefrences.getToken(context));
            params.put("username", username);
            params.put("password", password);
            params.put("pin", pin);

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.LOGIN_PIN, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Util.cancelPgDialog(progressDialog);
            Log.e("response", ": " + s);
            String msg=" ";
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    msg=jsonObject.optString("msg");
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                        //JSONObject jobt=jsonObject.getJSONObject("token");

                        dialog2.dismiss();

                        MyPrefrences.setUserLogin(context, true);
                        MyPrefrences.setToken(context, jsonObject.optString("token"));
                        MyPrefrences.setUserId(context, jsonObject.optString("retailer_id"));
                        MyPrefrences.setMobileNoNew(context, jsonObject.optString("mobile"));
                        Toast.makeText(context,""+msg,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context,""+msg,Toast.LENGTH_SHORT).show();

                        //Util.errorDialog(context, jsonObject.optString("message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }



}