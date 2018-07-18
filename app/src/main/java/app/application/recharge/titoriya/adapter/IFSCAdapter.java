package app.application.recharge.titoriya.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.R;

/**
 * Created by user on 12/13/2017.
 */

public class IFSCAdapter extends PagerAdapter {
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
    TextView bankname,ifsc_code,BranchName,Address,City,State;

    public IFSCAdapter(Context activity, ArrayList<HashMap<String, String>> histItms, String hist_type) {
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
        View view = inflater.inflate(R.layout.ifscdetails, container, false);
        size = (TextView) view.findViewById(R.id.size);
        size.setText("Last " + histItms.size() + " Transactions");
        method_ly = (LinearLayout) view.findViewById(R.id.method_ly);
        operator_ref_ly = (LinearLayout) view.findViewById(R.id.operator_ref_ly);
        operator_ly= (LinearLayout) view.findViewById(R.id.operator_ly);
        mobilelayout= (LinearLayout) view.findViewById(R.id.mobilelayout);
        statuslayout= (LinearLayout) view.findViewById(R.id.statuslayout);
        datetimelayout= (LinearLayout) view.findViewById(R.id.datetimelayout);
/////////////////////////////////////////// hist header

        bankname = (TextView) view.findViewById(R.id.bankname);
        ifsc_code = (TextView) view.findViewById(R.id.ifsc_code);
        BranchName = (TextView) view.findViewById(R.id.BranchName);
        Address = (TextView) view.findViewById(R.id.Address);
        City = (TextView) view.findViewById(R.id.City);
        State = (TextView) view.findViewById(R.id.State);


        bankname.setText(histItms.get(position).get("bankname").toString());
        ifsc_code.setText(histItms.get(position).get("IFSC").toString());
        BranchName.setText(histItms.get(position).get("bankname").toString());
        Address.setText(histItms.get(position).get("Address").toString());
        City.setText(histItms.get(position).get("City").toString());
        State.setText(histItms.get(position).get("State").toString());

        Log.d("dfgdfhgdfhfdgd",histItms.get(position).get("bankname"));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}