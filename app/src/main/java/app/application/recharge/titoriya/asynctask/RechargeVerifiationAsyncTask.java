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
 * Created by user on 2/22/2017.
 */

public class RechargeVerifiationAsyncTask extends AsyncTask<String, Void, String> {
    String mob_num, amount, operator_val, account, operator_name,rech_type;
    boolean isPostpaid;
    Context context;
    ProgressDialog progressDialog;

    public RechargeVerifiationAsyncTask(Context context, final String mob_num, final String amount, final String operator_val,
                                        final String account, final String rech_type, final boolean isPostpaid) {
        this.context = context;
        this.mob_num = mob_num;
        this.amount = amount;
        this.operator_val = operator_val;
        this.account = account;
        this.rech_type=rech_type;
        this.isPostpaid = isPostpaid;
        progressDialog = new ProgressDialog(context);
        Util.showPgDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", MyPrefrences.getToken(context));
        params.put("number", mob_num);
        params.put("amount", amount);
        params.put("operator", operator_val);
        params.put("retailer_id", MyPrefrences.getUserId(context));
        if (isPostpaid == true) {
            params.put("numtype", "Postpaid");
        }else{
            params.put("numtype", "prepaid");
        }
        params.put("rech_type", rech_type);

        Log.d("fdsgfdgdfghdfhdfdg",MyPrefrences.getToken(context));
        Log.d("fdsgfdgdfghdfhdfdg",mob_num);
        Log.d("fdsgfdgdfghdfhdfdg",amount);
        Log.d("fdsgfdgdfghdfhdfdg",operator_val);
        Log.d("fdsgfdgdfghdfhdfdg",MyPrefrences.getUserId(context));
        Log.d("fdsgfdgdfghdfhdfdg", String.valueOf(isPostpaid));
        Log.d("fdsgfdgdfghdfhdfdg",rech_type);
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.First_Step_Recharge, "GET", params);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Util.cancelPgDialog(progressDialog);
        //Log.e("response", ": " + s);
        Log.d("gfdgdfgdfhgbdf",s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            Util.Recharge_Amount = amount;
            Util.Recharge_Number = mob_num;
            Util.Recharge_Operator = operator_name;
            Log.d("gfdgdfgdfhgbdf",s);
            if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                Log.d("gfdgdfgdfhgbdf1",s);
                JSONObject jsonItemObject= jsonObject.getJSONObject("item");


                Util.errorDialog2(context,  jsonItemObject.optString("message"),jsonItemObject.optString("transaction_id"),
                        jsonItemObject.optString("operator"),jsonItemObject.optString("number"),jsonItemObject.optString("operator_ref"),
                        jsonItemObject.optString("amount"),jsonItemObject.optString("your_balance"));

//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.alertdialogcustom2);
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                TextView msg_txv = (TextView) dialog.findViewById(R.id.msg_txv);
//                TextView rsID_txv = (TextView) dialog.findViewById(R.id.rsID_txv);
//                TextView operator_txv = (TextView) dialog.findViewById(R.id.operator_txv);
//                TextView number_txv = (TextView) dialog.findViewById(R.id.number_txv);
//                TextView operator_ref_txv = (TextView) dialog.findViewById(R.id.operator_ref_txv);
//                TextView amount_txv = (TextView) dialog.findViewById(R.id.amount_txv);
//                TextView balance_txv = (TextView) dialog.findViewById(R.id.balance_txv);
//
//                msg_txv.setText(jsonItemObject.optString("message"));
//                rsID_txv.setText("transaction id: "+jsonItemObject.optString("transaction_id"));
//                operator_txv.setText("operator: ".toUpperCase()+jsonItemObject.optString("operator"));
//                number_txv.setText("number: ".toUpperCase()+jsonItemObject.optString("number"));
//                operator_ref_txv.setText("operator ref: ".toUpperCase()+jsonItemObject.optString("operator_ref"));
//                amount_txv.setText("amount: ".toUpperCase()+jsonItemObject.optString("amount"));
//                balance_txv.setText("your balance: ".toUpperCase()+jsonItemObject.optString("your_balance"));
//
//
//                Button ok = (Button) dialog.findViewById(R.id.btn_ok);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//
//                        Intent intent = new Intent(context, MainActivity.class);
//                        context.startActivity(intent);
//
//                    }
//                });
//                dialog.show();



               // Toast.makeText(context, "" + Html.fromHtml(jsonItemObject.optString("message")+"\n"+jsonItemObject.optString("your_cost")+"\n" +jsonItemObject.optString("your_balance")), Toast.LENGTH_SHORT).show();
                //Util.paymentConfirmDialog(context, jsonObject, "recharge", initiated_query_id);
            }
            else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                MyPrefrences.resetPrefrences(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity)context).finish();
            }
            else if(jsonObject.optString("msg").equalsIgnoreCase("You have insufficient balance")){
                Util.errorDialog(context,jsonObject.optString("msg"));
            }

            else {
                Toast.makeText(context, "Invalid Number/amount/operator: "+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                //Util.paymentConfirmDialog(context, jsonObject, "recharge", "123");
                //Util.errorDialog(context, jsonObject.optString("message"));
            }
        } catch (JSONException e) {
            Log.d("fgdgdfgdfgdf",e.toString());
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }
}