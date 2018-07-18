package app.application.recharge.titoriya.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.R;

/**
 * Created by user on 12/11/2017.
 */

public class SupportCenterDetailAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> disp_dtls;
    private Context mContext;
    private TextView msg,msg_time,type;


    public SupportCenterDetailAdapter(Context activity, ArrayList<HashMap<String, String>> disp_dtls) {
        this.mContext = activity;
        this.disp_dtls = disp_dtls;
    }

    @Override
    public int getCount() {
        return disp_dtls.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dispute_detail_adapter, null);

        msg = (TextView) view.findViewById(R.id.msg);
        msg_time = (TextView) view.findViewById(R.id.msg_time);
        type=(TextView) view.findViewById(R.id.type);

        msg.setText(disp_dtls.get(i).get("comment").toString());
        msg_time.setText(disp_dtls.get(i).get("created_at").toString());
        type.setText(disp_dtls.get(i).get("user").toString());
        return view;
    }
}