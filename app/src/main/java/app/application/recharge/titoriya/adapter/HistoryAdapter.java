package app.application.recharge.titoriya.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.R;


/**
 * Created by adm on 9/13/2016.
 */
public class HistoryAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> histItms;
    private Context mContext;
    private TextView size;
    ////////////////title widget
    TextView trn_id, rchg_type, mob_num, amt, operator, method, ope_ref, status, date, time;
    ////////////////////////////// value widget
    TextView trn_id_val, rchg_type_val, mob_num_val, amt_val, operator_val, method_val,
            ope_ref_val, status_val, date_val, time_val,sr_num_val;
    LinearLayout method_ly, operator_ref_ly,operator_ly,mobilelayout,statuslayout,datetimelayout;
    String hist_type;

    public HistoryAdapter(Context activity, ArrayList<HashMap<String, String>> histItms, String hist_type) {
        this.mContext = activity;
        this.histItms = histItms;
        this.hist_type = hist_type;
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
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.histroydetails, container, false);
        size = (TextView) view.findViewById(R.id.size);
        size.setText("Last " + histItms.size() + " Transactions");
        method_ly = (LinearLayout) view.findViewById(R.id.method_ly);
        operator_ref_ly = (LinearLayout) view.findViewById(R.id.operator_ref_ly);
        operator_ly= (LinearLayout) view.findViewById(R.id.operator_ly);
        mobilelayout= (LinearLayout) view.findViewById(R.id.mobilelayout);
        statuslayout= (LinearLayout) view.findViewById(R.id.statuslayout);
        datetimelayout= (LinearLayout) view.findViewById(R.id.datetimelayout);
/////////////////////////////////////////// hist header
        trn_id = (TextView) view.findViewById(R.id.trn_id);
        rchg_type = (TextView) view.findViewById(R.id.rchg_type);
        mob_num = (TextView) view.findViewById(R.id.mob_num);
        amt = (TextView) view.findViewById(R.id.amt);
        operator = (TextView) view.findViewById(R.id.operator);
        method = (TextView) view.findViewById(R.id.method);
        ope_ref = (TextView) view.findViewById(R.id.ope_ref);
        status = (TextView) view.findViewById(R.id.status);
        date = (TextView) view.findViewById(R.id.date);

        ////////////////hist val
        trn_id_val = (TextView) view.findViewById(R.id.trn_id_val);
        rchg_type_val = (TextView) view.findViewById(R.id.rchg_type_val);
        mob_num_val = (TextView) view.findViewById(R.id.mob_num_val);
        amt_val = (TextView) view.findViewById(R.id.amt_val);
        operator_val = (TextView) view.findViewById(R.id.operator_val);
        method_val = (TextView) view.findViewById(R.id.method_val);
        ope_ref_val = (TextView) view.findViewById(R.id.ope_ref_val);
        status_val = (TextView) view.findViewById(R.id.status_val);
        date_val = (TextView) view.findViewById(R.id.date_val);
        time_val = (TextView) view.findViewById(R.id.time_val);
        sr_num_val= (TextView) view.findViewById(R.id.sr_num_val);
        sr_num_val.setText(histItms.get(position).get("id"));

        if (hist_type.equalsIgnoreCase("Payment History")) {
//            rchg_type.setText(mContext.getString(R.string.account));
//            ope_ref.setText(mContext.getString(R.string.reffrence_no));
//            date.setText(mContext.getString(R.string.req_date));
//            operator.setText(mContext.getString(R.string.message));
//            mob_num.setText(mContext.getString(R.string.action));

            trn_id.setText("Account Name");
            rchg_type.setText("Amount");
            amt.setText("bank_refno");
            ope_ref.setText("payment_date");
            date.setText("status");
            ope_ref_val.setTextSize(15);
            method_ly.setVisibility(View.GONE);
            operator_ly.setVisibility(View.GONE);
            statuslayout.setVisibility(View.GONE);
            mobilelayout.setVisibility(View.GONE);

            trn_id_val.setText(histItms.get(position).get("ac_name"));
            rchg_type_val.setText(histItms.get(position).get("amount"));
            amt_val.setText(histItms.get(position).get("bank_refno"));
            ope_ref_val.setText(histItms.get(position).get("payment_date"));
            date_val.setText(histItms.get(position).get("status"));
//            status_val.setText(histItms.get(position).get("status"));
//            operator_val.setText(histItms.get(position).get("msg"));
//            mob_num_val.setText(histItms.get(position).get("action"));

        } else if (hist_type.equalsIgnoreCase("Remittance History")) {
            rchg_type.setText(mContext.getString(R.string.ben_code));
            mob_num.setText(mContext.getString(R.string.ben_name));
            operator.setText(mContext.getString(R.string.mobile_wallet));
            ope_ref.setText(mContext.getString(R.string.account));
            method_ly.setVisibility(View.GONE);

            trn_id_val.setText(histItms.get(position).get("tr_id"));
            rchg_type_val.setText(histItms.get(position).get("ben_code"));
            mob_num_val.setText(histItms.get(position).get("ben_name"));
            operator_val.setText(histItms.get(position).get("mob_wallet"));
            ope_ref_val.setText(histItms.get(position).get("acct"));
            amt_val.setText(histItms.get(position).get("amt"));
            date_val.setText(histItms.get(position).get("date"));
            status_val.setText(histItms.get(position).get("status"));

        } else if (hist_type.equalsIgnoreCase("Recharge History")) {
            trn_id_val.setText(histItms.get(position).get("transaction_id"));
            rchg_type_val.setText(histItms.get(position).get("type"));
            mob_num_val.setText(histItms.get(position).get("mobile"));
            operator_val.setText(histItms.get(position).get("operator"));

            ope_ref_val.setText(histItms.get(position).get("balance"));

            amt_val.setText(histItms.get(position).get("amount"));
            date_val.setText(histItms.get(position).get("time"));
            status_val.setText(histItms.get(position).get("status"));
            method_val.setText(histItms.get(position).get("method"));

        } else if (hist_type.equalsIgnoreCase("Billing Summary")) {
            mob_num.setText(mContext.getString(R.string.user));
            rchg_type.setText(mContext.getString(R.string.tr_type));
            operator.setText(mContext.getString(R.string.paid_amount));
            status.setText(mContext.getString(R.string.user_type));

            method_ly.setVisibility(View.GONE);
            operator_ref_ly.setVisibility(View.GONE);
            mobilelayout.setVisibility(View.GONE);
            operator_ly.setVisibility(View.GONE);
            trn_id_val.setText(histItms.get(position).get("transaction_id"));
//            mob_num_val.setText(histItms.get(position).get("user"));
            rchg_type_val.setText(histItms.get(position).get("tr_type"));
//            status_val.setText(histItms.get(position).get("user_type"));
//            operator_val.setText(histItms.get(position).get("paid_amount"));
            amt_val.setText(histItms.get(position).get("amount"));
//            date_val.setText(histItms.get(position).get("date"));

        }else if (hist_type.equalsIgnoreCase("Transaction Details")) {
            mob_num.setText(mContext.getString(R.string.user));
            rchg_type.setText(mContext.getString(R.string.tr_type));
            status.setText(mContext.getString(R.string.user_type));
            method_ly.setVisibility(View.GONE);
            operator_ref_ly.setVisibility(View.GONE);
            operator_ly.setVisibility(View.GONE);
            statuslayout.setVisibility(View.GONE);
            datetimelayout.setVisibility(View.GONE);
            trn_id_val.setText(histItms.get(position).get("tr_id"));
            mob_num_val.setText(histItms.get(position).get("user"));
            rchg_type_val.setText(histItms.get(position).get("tr_type"));
            status_val.setText(histItms.get(position).get("user_type"));
            amt_val.setText(histItms.get(position).get("act_amount"));
            date_val.setText(histItms.get(position).get("date"));

        }

        time_val.setText(histItms.get(position).get("time"));
        ///////////////////////////////////////////////


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}