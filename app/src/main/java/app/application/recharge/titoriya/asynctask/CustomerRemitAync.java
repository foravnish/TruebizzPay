package app.application.recharge.titoriya.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/23/2017.
 */

public class CustomerRemitAync extends AsyncTask<String, Void, String> {
    String bene_code, amount, ifsc, bene_name, sel_type, bene_acc;
    Context context;
    ProgressDialog progressDialog;

    public CustomerRemitAync(Context context, String bene_code, String amount, String ifsc,
                             String bene_name, String sel_type, String bene_acc) {
        this.context = context;
        this.bene_code = bene_code;
        this.amount = amount;
        this.bene_name = bene_name;
        this.ifsc = ifsc;

        this.sel_type = sel_type;
        this.bene_acc = bene_acc;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uid", MyPrefrences.getUserId(context));
        params.put("pin", MyPrefrences.getUserPin(context));
        //params.put("number", MoneyTransferWalletLoginFragment.cust_mob_num);
        params.put("bene_code", bene_code);
        params.put("amount", amount);
        params.put("ifsc", ifsc);
        params.put("bene_name", bene_name);
        params.put("bene_acc", bene_acc);
        params.put("sel_type", sel_type);

        params.put("format", "json");
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Customer_Remitance_Url, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Util.cancelPgDialog(progressDialog);
        Log.e("response", ": " + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject != null) {
                if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                    Log.e("message", ": " + jsonObject.optString("message"));
                    Util.errorDialog(context, jsonObject.optString("message"));
                } else {
                    Util.errorDialog(context, jsonObject.optString("message"));
                    //Util.errorDialog(context, jsonObject.optString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}