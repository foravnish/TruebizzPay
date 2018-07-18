package app.application.recharge.titoriya.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/22/2017.
 */

public class InsuranceValidAsyncTask extends AsyncTask<String, Void, String> {
    String policy_number, insurance_dob, amount, operator_val, operator_name;
    Context context;
    ProgressDialog progressDialog;

    public InsuranceValidAsyncTask(Context context, final String policy_number, String insurance_dob,
                                   final String amount, final String operator_val, String selop) {
        this.context = context;
        this.policy_number = policy_number;
        this.insurance_dob = insurance_dob;
        this.amount = amount;
        this.operator_val = operator_val;
        this.operator_name = selop;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... strings) {
        /*Random random = new Random();
        final int tr_id = 02 + random.nextInt() * 3;
        Log.e("tr_id", ": " + tr_id);*/
        HashMap<String, String> params = new HashMap<>();
        params.put("retailer_id", MyPrefrences.getUserId(context));
        params.put("user_token", MyPrefrences.getToken(context));
        params.put("policy_no", policy_number);
        params.put("bday", insurance_dob);
        params.put("amount", amount);
        params.put("insurance_operator", operator_val);
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Insurance_Bill_Recharge, "GET", params);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Util.cancelPgDialog(progressDialog);
        try {
            Util.Recharge_Amount = amount;
            Util.Recharge_Number = policy_number;
            Util.Recharge_Operator = operator_name;
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                String initiated_query_id = jsonObject.optString("initiated_query_id");
                Toast.makeText(context, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                Util.paymentConfirmDialog(context, jsonObject, "insurance", initiated_query_id);
            }
            else if(jsonObject.optString("msg").equalsIgnoreCase("You have insufficient balance")){
                Util.errorDialog(context,jsonObject.optString("msg"));
            }
            else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                MyPrefrences.resetPrefrences(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity)context).finish();
            }

            else {
                Util.paymentConfirmDialog(context, jsonObject, "insurance", "123");
                // Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }
}
