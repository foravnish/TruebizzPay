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

public class GasValidationAsyncTask extends AsyncTask<String, Void, String> {
    String service_number, amount, operator_val, rech_type;
    Context context;
    ProgressDialog progressDialog;

    public GasValidationAsyncTask(Context context, final String service_number, final String amount,
                                  final String operator_val, String rech_type) {
        this.context = context;
        this.service_number = service_number;
        this.amount = amount;
        this.operator_val = operator_val;
        this.rech_type = rech_type;
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
        params.put("customerid", service_number);
        params.put("amount", amount);
        params.put("gasoperator", operator_val);
        params.put("rech_type", rech_type);
        params.put("user_token", MyPrefrences.getToken(context));
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Gas_Bill_Recharge, "GET", params);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Util.cancelPgDialog(progressDialog);
        try {
            Util.Recharge_Amount = amount;
            Util.Recharge_Number = service_number;

            Util.Recharge_Operator = rech_type;
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                String initiated_query_id = jsonObject.optString("initiated_query_id");
                Toast.makeText(context, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                Util.paymentConfirmDialog(context, jsonObject, "gas_bill", initiated_query_id);
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
                Util.paymentConfirmDialog(context, jsonObject, "gas_bill", "123");
                //Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }
}
