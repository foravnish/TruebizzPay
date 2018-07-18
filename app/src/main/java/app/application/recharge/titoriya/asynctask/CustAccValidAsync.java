package app.application.recharge.titoriya.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/25/2017.
 */

public class CustAccValidAsync extends AsyncTask<String, Void, String> {
    String bene_code, amount, ifsc, bene_name, sel_type, bene_acc;
    Context context;
    ProgressDialog progressDialog;

    public CustAccValidAsync(Context context, String bene_code, String amount, String ifsc,
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
        params.put("ifsc", ifsc);
        params.put("bene_code", bene_code);
        params.put("bene_name", bene_name);
        params.put("bene_acc", bene_acc);
        params.put("format", "json");
        JSONParser jsonParser = new JSONParser();
        String result = jsonParser.makeHttpRequest(Api.Customer_Acc_Validation_Url, "GET", params);
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
                    // Util.errorDialog(context, jsonObject.optString("message"));
                    remitConfirm(context, jsonObject.optString("message"));
                } else {
                    Util.errorDialog(context, jsonObject.optString("message"));
                    //Util.errorDialog(context, jsonObject.optString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void remitConfirm(final Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.raisedisputedialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button subbmit = (Button) dialog.findViewById(R.id.subbmit);
        subbmit.setText("Continue");
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Account Verifiaction Details");
        final EditText tr_id__edt = (EditText) dialog.findViewById(R.id.tr_id__edt);
        final EditText add_msg_edt = (EditText) dialog.findViewById(R.id.add_msg_edt);
        add_msg_edt.setVisibility(View.GONE);
        tr_id__edt.setText(msg);
        tr_id__edt.setInputType(InputType.TYPE_NULL);
        tr_id__edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomerRemitAync(context, bene_code, amount,
                        ifsc, bene_name, sel_type, bene_acc).execute();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
