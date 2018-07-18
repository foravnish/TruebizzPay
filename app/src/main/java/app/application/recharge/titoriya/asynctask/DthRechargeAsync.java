package app.application.recharge.titoriya.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
 * Created by user on 6/28/2017.
 */

public class DthRechargeAsync extends AsyncTask<String, Void, String> {
    String subscriberid, amount, operator_val, operator_name,rech_type;
    Context context;
    ProgressDialog progressDialog;

    public DthRechargeAsync(Context context, final String subscriberid, final String amount, final String operator_val,
                            final String rech_type) {
        this.context = context;
        this.subscriberid = subscriberid;
        this.amount = amount;
        this.operator_val = operator_val;
        this.rech_type=rech_type;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", MyPrefrences.getToken(context));
        params.put("subscriberid", subscriberid);
        params.put("amount", amount);
        params.put("operator", operator_val);
        params.put("retailer_id", MyPrefrences.getUserId(context));
        /*if (isPostpaid == true) {
            params.put("numtype", "Postpaid");
        }else{
            params.put("numtype", "prepaid");
        }*/
        params.put("rech_type", rech_type);
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Dth_Recharge, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Util.cancelPgDialog(progressDialog);
        Log.e("response", ": " + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            Util.Recharge_Amount = amount;
            Util.Recharge_Operator = operator_name;
            if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                String initiated_query_id = jsonObject.optString("initiated_query_id");
                Toast.makeText(context, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                Util.paymentConfirmDialog(context, jsonObject, "recharge", initiated_query_id);
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
                Util.paymentConfirmDialog(context, jsonObject, "recharge", "123");
                //Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }
}