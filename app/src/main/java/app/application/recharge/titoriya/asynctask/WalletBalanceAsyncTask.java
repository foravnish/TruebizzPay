package app.application.recharge.titoriya.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/22/2017.
 */

public class WalletBalanceAsyncTask extends AsyncTask<String, Void, String> {
    String initiated_query_id;
    Context context;
    TextView txtBalance;
    ProgressDialog progressDialog;

    public WalletBalanceAsyncTask(Context context, TextView txtBalance) {
        this.context = context;
        this.txtBalance=txtBalance;
        Util.Check_Balance = 0;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("retailer_id", MyPrefrences.getUserId(context));
        params.put("user_token", MyPrefrences.getToken(context));
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Wallet_Balance_Url, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Util.cancelPgDialog(progressDialog);
        Log.e("response", ": " + s);
        Util.Check_Balance = 1;
        //Util.Wallet_Balance = s;
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                //String initiated_query_id = jsonObject.optString("initiated_query_id");
                Util.Wallet_Balance = jsonObject.optString("msg");
                txtBalance.setText(jsonObject.optString("msg"));
                //Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getActivity(), "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                txtBalance.setText(jsonObject.optString("msg"));
                Util.errorDialog(context, jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
