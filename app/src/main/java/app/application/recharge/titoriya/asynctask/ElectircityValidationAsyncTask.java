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

public class ElectircityValidationAsyncTask extends AsyncTask<String, Void, String> {
    String ca_num, amount, operator_val, city, cycle_number, billing_unit, processing_unit;
    String rech_type;
    Context context;
    ProgressDialog progressDialog;

    public ElectircityValidationAsyncTask(Context context, final String ca_num, final String amount, final String operator_val,
                                          final String city, final String rech_type, String cycle_number
            , String billing_unit, final String processing_unit) {
        this.ca_num = ca_num;
        this.context = context;
        this.amount = amount;
        this.operator_val = operator_val;
        this.city = city;
        this.rech_type = rech_type;
        this.cycle_number = cycle_number;
        this.city = city;
        this.billing_unit = billing_unit;
        this.processing_unit = processing_unit;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
       /* Random random = new Random();
        final int tr_id = 02 + random.nextInt() * 3;
        Log.e("tr_id", ": " + tr_id);*/

        HashMap<String, String> params = new HashMap<>();
        params.put("retailer_id", MyPrefrences.getUserId(context));
        params.put("user_token", MyPrefrences.getToken(context));
        params.put("customerid", ca_num);
        params.put("amount", amount);
        params.put("elecoperator", operator_val);
        params.put("rech_type", rech_type);
        if (rech_type.equalsIgnoreCase("torrent")) {
            params.put("elec_city", city);
        } else if (rech_type.equalsIgnoreCase("Reliance Energy")) {
            params.put("cycle_number", cycle_number);

        } else if (rech_type.equalsIgnoreCase("msedc")) {
            params.put("billing_unit", billing_unit);
            params.put("processing_unit", processing_unit);

        }
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Electric_Bill_Recharge, "GET", params);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Util.cancelPgDialog(progressDialog);
        try {
            Util.Recharge_Amount = amount;
            Util.Recharge_Number = ca_num;

            Util.Recharge_Operator = rech_type;
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                String initiated_query_id = jsonObject.optString("initiated_query_id");
                Util.paymentConfirmDialog(context, jsonObject, "electricity_bill", initiated_query_id);
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
                Util.paymentConfirmDialog(context, jsonObject, "electricity_bill", "123");
                //  Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }
}
