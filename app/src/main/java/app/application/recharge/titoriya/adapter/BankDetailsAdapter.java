package app.application.recharge.titoriya.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.R;

/**
 * Created by user on 2/13/2017.
 */

public class BankDetailsAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> bnk_list;
    private Context mContext;
    private TextView add_val,bnk_val,ifsc_val,brc_val,city_val,state_val;


    public BankDetailsAdapter(Context activity, ArrayList<HashMap<String, String>> histItms) {
        this.mContext = activity;
        this.bnk_list = histItms;
    }

    @Override
    public int getCount() {
        return bnk_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bank_details_adapter, container, false);
        add_val = (TextView) view.findViewById(R.id.add_val);
        bnk_val= (TextView) view.findViewById(R.id.bnk_val);
        ifsc_val= (TextView) view.findViewById(R.id.ifsc_val);
        brc_val= (TextView) view.findViewById(R.id.brc_val);
        city_val= (TextView) view.findViewById(R.id.city_val);
        state_val= (TextView) view.findViewById(R.id.state_val);


        add_val.setText(bnk_list.get(position).get("address"));
        bnk_val.setText(bnk_list.get(position).get("bank_name"));
        ifsc_val.setText(bnk_list.get(position).get("ifsc_code"));
        brc_val.setText(bnk_list.get(position).get("branch_Name"));
        city_val.setText(bnk_list.get(position).get("city_name"));
        state_val.setText(bnk_list.get(position).get("state_name"));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
