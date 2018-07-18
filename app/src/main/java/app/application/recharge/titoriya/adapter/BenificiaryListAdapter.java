package app.application.recharge.titoriya.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.asynctask.CustAccValidAsync;
import app.application.recharge.titoriya.connection.InternetStatus;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/14/2017.
 */

public class BenificiaryListAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> histItms;
    private Context mContext;
    String benficiary_type;
    ///////////widget for beni list
    Spinner tr_type_spn;
    TextView remt_txv, del_txv;
    String otc_ref = "";
    int pos;
    String trans_id = "";

    ////////////////////////////
    public BenificiaryListAdapter(Context activity, ArrayList<HashMap<String, String>> histItms, String benficiary_type) {
        this.mContext = activity;
        this.histItms = histItms;
        this.benficiary_type = benficiary_type;
    }

    @Override
    public int getCount() {
        return histItms.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }


    @Override
    public int getItemPosition(Object object) {
        int position = histItms.indexOf(object);
        return position == -1 ? POSITION_NONE : position;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view;
        if (benficiary_type.equalsIgnoreCase("ben_list")) {
            view = inflater.inflate(R.layout.beneficiarylistadapter, container, false);
            showBenList(view, position);
        } else {
            view = inflater.inflate(R.layout.benificiary_history_adapter, container, false);
            showBenHist(view, position);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    private void showBenList(View view, final int position) {
        tr_type_spn = (Spinner) view.findViewById(R.id.tr_type_spn);
        final EditText amt_edt = (EditText) view.findViewById(R.id.amt_edt);
        final TextView nn_val = (TextView) view.findViewById(R.id.nn_val);
        final TextView ben_co_val = (TextView) view.findViewById(R.id.ben_co_val);
        final TextView acc_num_val = (TextView) view.findViewById(R.id.acc_num_val);
        final TextView type_val = (TextView) view.findViewById(R.id.type_val);
        final TextView ifsc_val = (TextView) view.findViewById(R.id.ifsc_val);
        TextView status_val_txv = (TextView) view.findViewById(R.id.status_val_txv);

        nn_val.setText(histItms.get(position).get("ben_name"));
        ben_co_val.setText(histItms.get(position).get("ben_code"));
        acc_num_val.setText(histItms.get(position).get("acc_num"));
        type_val.setText(histItms.get(position).get("acc_type"));
        ifsc_val.setText(histItms.get(position).get("ifsc_code"));
        status_val_txv.setText(histItms.get(position).get("active"));

        remt_txv = (TextView) view.findViewById(R.id.remt_txv);
        del_txv = (TextView) view.findViewById(R.id.del_txv);
        setBankTrType(mContext, tr_type_spn);
        ///////////////////////

        remt_txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amt_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(mContext, "Please Enter Amount");
                } else if (InternetStatus.isConnectingToInternet(mContext)) {
                    new CustAccValidAsync(mContext,
                            ben_co_val.getText().toString(),
                            amt_edt.getText().toString()
                            , ifsc_val.getText().toString(), nn_val.getText().toString(),
                            tr_type_spn.getSelectedItem().toString()
                            , acc_num_val.getText().toString()).execute();
                    // Toast.makeText(mContext, "Working", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////////////
        del_txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetStatus.isConnectingToInternet(mContext)) {
                    pos = position;
                    new BeniDelAync(mContext, ben_co_val.getText().toString(), ifsc_val.getText().toString()).execute();
                }
            }
        });
    }

    private void showBenHist(View view, final int position) {
        TextView ag_tr_txv = (TextView) view.findViewById(R.id.ag_tr_txv);
        TextView m_tr_id = (TextView) view.findViewById(R.id.m_tr_id);
        TextView top_tr_id = (TextView) view.findViewById(R.id.top_tr_id);
        TextView amt = (TextView) view.findViewById(R.id.amt);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView reinit_trns = (TextView) view.findViewById(R.id.reinit_trns);
        ag_tr_txv.setText(histItms.get(position).get("agent_tr_id"));
        m_tr_id.setText(histItms.get(position).get("mr_tr_id"));
        top_tr_id.setText(histItms.get(position).get("topup_tr_id"));
        amt.setText(histItms.get(position).get("amount"));
        status.setText(histItms.get(position).get("status"));
        reinit_trns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trans_id = histItms.get(position).get("agent_tr_id");
                raiseOtpDialog(mContext, "reinitiate");
            }
        });

    }

    private void setBankTrType(Context context, Spinner spinner) {
        ArrayList<String> trtype_list = new ArrayList<>();
        trtype_list.add("IMPS");
        trtype_list.add("NEFT");
        ArrayAdapter<String> bnklistAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                trtype_list);
        bnklistAdapter.setDropDownViewResource(R.layout.listtextview);
        spinner.setAdapter(bnklistAdapter);

    }

    //////////////////delete benificiary task
    public class BeniDelAync extends AsyncTask<String, Void, String> {
        String bene_code, amount, ifsc, bene_name, sel_type, bene_acc;
        Context context;

        public BeniDelAync(Context context, String bene_code, String ifsc) {
            this.context = context;
            this.bene_code = bene_code;
            this.amount = amount;
            this.bene_name = bene_name;
            this.ifsc = ifsc;
            this.sel_type = sel_type;
            this.bene_acc = bene_acc;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("uid", MyPrefrences.getUserId(context));
            params.put("pin", MyPrefrences.getUserPin(context));
            //params.put("number", MoneyTransferWalletLoginFragment.cust_mob_num);
            params.put("ifsc", ifsc);
            params.put("bene_code", bene_code);
            params.put("format", "json");
            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Delete_Beni_Url, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", ": " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                        otc_ref = jsonObject.optString("message");
                        Log.e("message", ": " + jsonObject.optString("message"));
                        // Util.errorDialog(context, jsonObject.optString("message"));
                        raiseOtpDialog(context, "otp");
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
    /////////////////////// Customer Otp Confirm Async Task

    public class CustomerOtpConfirmAync extends AsyncTask<String, Void, String> {
        String cust_mob_num, otp, otp_ref;
        Context context;

        public CustomerOtpConfirmAync(Context context, String cust_mob_num, String otp, String otp_ref) {
            this.context = context;
            this.cust_mob_num = cust_mob_num;
            this.otp = otp;
            this.otp_ref = otp_ref;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("uid", MyPrefrences.getUserId(context));
            params.put("pin", MyPrefrences.getUserPin(context));
            params.put("number", cust_mob_num);
            params.put("otc", otp);
            params.put("otcRef", otp_ref);
            params.put("format", "json");
            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_OTP_Confirm, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", ": " + s);
            Log.e("position", " " + pos);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                        Toast.makeText(context, "Benificiary Delete Successfully", Toast.LENGTH_SHORT).show();
                        /*CustomerWalletFragment customerWalletFragment = new CustomerWalletFragment();
                        CustomerWalletFragment.CustomerValidationAync validationAync = customerWalletFragment
                                .new CustomerValidationAync(context);
                        validationAync.execute();*/
                        histItms.remove(pos);
                        notifyDataSetChanged();

                    } else {
                        Util.errorDialog(context, jsonObject.optString("message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
///////////////////reinitiate transaction history asyncTask


    //////////////////delete benificiary task
    public class ReinitiateTransHistoryAync extends AsyncTask<String, Void, String> {
        String bene_code, transId;
        Context context;

        public ReinitiateTransHistoryAync(Context context, String bene_code, String transId) {
            this.context = context;
            this.bene_code = bene_code;
            this.transId = transId;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("uid", MyPrefrences.getUserId(context));
            params.put("pin", MyPrefrences.getUserPin(context));
            //params.put("number", MoneyTransferWalletLoginFragment.cust_mob_num);
            params.put("transId", transId);
            params.put("bene_code", bene_code);
            params.put("format", "json");
            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Reinitiate_Transaction_Url, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", ": " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("result_code").equalsIgnoreCase("1")) {
                        otc_ref = jsonObject.optString("message");
                        Log.e("message", ": " + jsonObject.optString("message"));
                        // Util.errorDialog(context, jsonObject.optString("message"));
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

    /////////////////////// Customer Otp Confirm Async Task
    //////////////////////////////////////////////
    private void raiseOtpDialog(final Context context, final String type) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.raisedisputedialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button subbmit = (Button) dialog.findViewById(R.id.subbmit);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        final EditText tr_id__edt = (EditText) dialog.findViewById(R.id.tr_id__edt);
        final EditText add_msg_edt = (EditText) dialog.findViewById(R.id.add_msg_edt);
        add_msg_edt.setVisibility(View.GONE);
        if (type.equalsIgnoreCase("reinitiate")) {
            title.setText("Reinitiate Transaction Details");
            tr_id__edt.setInputType(InputType.TYPE_CLASS_TEXT);
            tr_id__edt.setHint("Enter Benificiary Code");
        } else {
            tr_id__edt.setInputType(InputType.TYPE_CLASS_NUMBER);
            title.setText("Otp Confirm");
            tr_id__edt.setHint("Enter Otp");
        }
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("reinitiate")) {
                    if (tr_id__edt.getText().toString().isEmpty()) {
                        Util.errorDialog(context, "Please Enter Benificiary Code");
                    } else if (InternetStatus.isConnectingToInternet(context)) {
                        String ben_code = tr_id__edt.getText().toString();
                        new ReinitiateTransHistoryAync(context, ben_code, trans_id).execute();
                        dialog.cancel();
                    }
                } else {
                    if (tr_id__edt.getText().toString().isEmpty()) {
                        Util.errorDialog(context, "Please Enter Otp");
                    } else if (InternetStatus.isConnectingToInternet(context)) {
//                        new CustomerOtpConfirmAync(context, MoneyTransferWalletLoginFragment.cust_mob_num,
//                                tr_id__edt.getText().toString(), otc_ref).execute();
//                        dialog.cancel();
                    }
                }

            }
        });
        dialog.show();
    }
}
