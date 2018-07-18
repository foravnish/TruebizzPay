package app.application.recharge.titoriya.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

import static app.application.recharge.titoriya.Fragments.RechargeFragment.parseOperatorList;


/**
 * Created by user on 6/27/2017.
 */

public class OperatorListAsync extends AsyncTask<String, Void, String> {
    String op_type;
    Context context;
    ProgressDialog progressDialog;

    public OperatorListAsync(Context context, String op_type) {
        this.context = context;
        this.op_type = op_type;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("operator_type", op_type);
        Log.d("sfgdsgddgdfgd",op_type);
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Operator_List_Api, "GET", params);
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

                if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        JSONArray oprt_JsonArray = jsonObject.optJSONArray("item");
                        parseOperatorList(oprt_JsonArray);
                    } else {
                        //Toast.makeText(context(), "No operator available" + s, Toast.LENGTH_LONG).show();

                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
