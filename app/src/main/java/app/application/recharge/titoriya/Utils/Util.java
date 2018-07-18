package app.application.recharge.titoriya.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONObject;

import app.application.recharge.titoriya.MainActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.asynctask.ElectircBillPayAsyncTask;
import app.application.recharge.titoriya.asynctask.GasBillPayAsyncTask;
import app.application.recharge.titoriya.asynctask.InsuranceBillPayAsyncTask;
import app.application.recharge.titoriya.asynctask.RechargePayAsyncTask;
import app.application.recharge.titoriya.connection.InternetStatus;

/**
 * Created by user on 2/6/2017.
 */

public class Util {
    public static String USER_TYPE = "";
    public static String Distributor = "Distributor";
    public static String Retailer = "Retailer";
    public static long Register_Mobile_Number = 0;
    public static String Wallet_Balance = "Not Available";
    public static int Check_Balance = 0;
    /// recharge related val
    public static String Recharge_Number = "";
    public static String Recharge_Amount = "";
    public static String Recharge_Operator = "";
    //////////////////

    //////////////////////paymentConfirm dialog

    public static void errorDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(message));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void errorDialog2(final Context context, String message, String tras_id, String operator, String number, String oper_ref, String amount, String balance) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView msg_txv = (TextView) dialog.findViewById(R.id.msg_txv);
        TextView rsID_txv = (TextView) dialog.findViewById(R.id.rsID_txv);
        TextView operator_txv = (TextView) dialog.findViewById(R.id.operator_txv);
        TextView number_txv = (TextView) dialog.findViewById(R.id.number_txv);
        TextView operator_ref_txv = (TextView) dialog.findViewById(R.id.operator_ref_txv);
        TextView amount_txv = (TextView) dialog.findViewById(R.id.amount_txv);
        TextView balance_txv = (TextView) dialog.findViewById(R.id.balance_txv);

        msg_txv.setText(message);
        rsID_txv.setText("transaction id: ".toUpperCase()+tras_id);
        operator_txv.setText("operator: ".toUpperCase()+operator);
        number_txv.setText("number: ".toUpperCase()+number);
        operator_ref_txv.setText("operator ref: ".toUpperCase()+oper_ref);
        amount_txv.setText("amount: ".toUpperCase()+amount);
        balance_txv.setText("your balance: ".toUpperCase()+balance);


        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
            }
        });

        dialog.show();
    }

    public static void paymentConfirmDialog(final Context context, JSONObject jsonObject, final String type, final String initiated_query_id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_confirm_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button subbmit = (Button) dialog.findViewById(R.id.subbmit);
        subbmit.setText("Confirm");
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView mob_num_txv = (TextView) dialog.findViewById(R.id.mob_num_txv);
        final TextView operator_txv = (TextView) dialog.findViewById(R.id.operator_txv);
        final TextView amt_txv = (TextView) dialog.findViewById(R.id.amt_txv);
        final TextView srchrge_txv = (TextView) dialog.findViewById(R.id.srchrge_txv);
        final TextView ur_cst_txv = (TextView) dialog.findViewById(R.id.ur_cst_txv);
        ur_cst_txv.setVisibility(View.GONE);
        srchrge_txv.setVisibility(View.GONE);

        mob_num_txv.setText(Util.Recharge_Number);
        operator_txv.setText(Util.Recharge_Operator);
        amt_txv.setText(Util.Recharge_Amount);
       /* double tax = Double.valueOf(Util.Recharge_Amount) / 10;
        double ur_cst = Double.valueOf(Util.Recharge_Amount) - tax;
        double cst_w_srch= Double.valueOf(Util.Recharge_Amount) + tax;*/
        if (type.equalsIgnoreCase("recharge") && jsonObject != null) {
            title.setText("Recharge Confirm Information");
            // ur_cst_txv.setText("Your Cost : "+ur_cst);
        } else if (type.equalsIgnoreCase("electricity_bill")) {
            title.setText("Electricity Bill Details");
            // ur_cst_txv.setText("Your Cost : "+cst_w_srch);
            //  srchrge_txv.setText("Surcharge : "+tax);

        } else if (type.equalsIgnoreCase("gas_bill")) {
            title.setText("Gas Bill Details");
            // ur_cst_txv.setText("Your Cost : "+cst_w_srch);
            //  srchrge_txv.setText("Surcharge : "+tax);

        } else if (type.equalsIgnoreCase("insurance")) {
            title.setText("Insurance  Details");
            // ur_cst_txv.setText("Your Cost : "+cst_w_srch);
            //  srchrge_txv.setText("Surcharge : "+tax);
        }

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

                if (type.equalsIgnoreCase("electricity_bill") && InternetStatus.isConnectingToInternet(context))
                    new ElectircBillPayAsyncTask(context, initiated_query_id).execute();
                else if (type.equalsIgnoreCase("gas_bill") && InternetStatus.isConnectingToInternet(context)) {
                    new GasBillPayAsyncTask(context, initiated_query_id).execute();
                } else if (type.equalsIgnoreCase("insurance") && InternetStatus.isConnectingToInternet(context)) {
                    new InsuranceBillPayAsyncTask(context, initiated_query_id).execute();
                } else {
                    new RechargePayAsyncTask(context, initiated_query_id).execute();
                    //rechargePayment(initiated_query_id);
                }
                dialog.dismiss();

            }
        });
        dialog.show();

    }



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    ///////////////show progress dialog for Async Task
    public static void showPgDialog(ProgressDialog progressDialog) {
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
    }

    public static void cancelPgDialog(ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
