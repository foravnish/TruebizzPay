package app.application.recharge.titoriya.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



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

public class ElectircBillPayAsyncTask extends AsyncTask<String, Void, String> {
    String initiated_query_id;
    Context context;
    ProgressDialog progressDialog;

    public ElectircBillPayAsyncTask(Context context, String initiated_query_id) {
        this.context = context;
        this.initiated_query_id = initiated_query_id;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uid", MyPrefrences.getUserId(context));
        params.put("pin", MyPrefrences.getUserPin(context));
        params.put("initiated_query_id", initiated_query_id);
        params.put("format", "json");
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Second_Step_Recharge, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Util.cancelPgDialog(progressDialog);
        Log.e("response", ": " + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                String initiated_query_id = jsonObject.optString("initiated_query_id");
                Toast.makeText(context, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            } else {
                Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
