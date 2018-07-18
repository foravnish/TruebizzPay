package app.application.recharge.titoriya.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/13/2017.
 */

public class DisputeAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> histItms;
    private Context mContext;
    private TextView action_val;
    private TextView trn_id_val,rchg_disID_val,sts_val,dis_date_val,dis_mgs_val;
    ProgressDialog progressDialog;
    Dialog dialog;
    ListView listView;
    ArrayList<HashMap<String,String>> dist_list;
    public DisputeAdapter(Context activity, ArrayList<HashMap<String, String>> histItms) {
        this.mContext = activity;
        this.histItms = histItms;
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
        View view = inflater.inflate(R.layout.disputeadapter, container, false);
        action_val = (TextView) view.findViewById(R.id.action_val);


        trn_id_val = (TextView) view.findViewById(R.id.trn_id_val);
        rchg_disID_val = (TextView) view.findViewById(R.id.rchg_disID_val);
        sts_val = (TextView) view.findViewById(R.id.sts_val);
        dis_date_val = (TextView) view.findViewById(R.id.dis_date_val);
        dis_mgs_val = (TextView) view.findViewById(R.id.dis_mgs_val);

        trn_id_val.setText(histItms.get(position).get("transaction_id"));
        rchg_disID_val.setText(histItms.get(position).get("dispute_id"));
        sts_val.setText(histItms.get(position).get("status"));
        dis_date_val.setText(histItms.get(position).get("dispute_created"));
        dis_mgs_val.setText(histItms.get(position).get("message"));


        if (histItms.get(position).get("status").equalsIgnoreCase("On Hold") || histItms.get(position).get("status").equalsIgnoreCase("Closed") ){
            action_val.setText("Cant't reply");
            action_val.setEnabled(false);
        }


        action_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDisputeDetails(mContext, histItms.get(position).get("dispute_id"));
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void getDisputeDetails(final Context context, final String dispute_id) {
        {

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialoglivescoremenu);
            dialog.setCancelable(true);
            Button submit = (Button) dialog.findViewById(R.id.disimiss);
            TextView title = (TextView) dialog.findViewById(R.id.title);
            final EditText add_msg_edt = (EditText) dialog.findViewById(R.id.add_msg_edt);
            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
            listView = (ListView) dialog.findViewById(R.id.choose_sportlv);


            new RaiseDisputeActionDetails(context,dispute_id).execute();


            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();

                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if (add_msg_edt.getText().toString().isEmpty()) {
                                                  Util.errorDialog(mContext,"Please Enter Message");
                                              } else {

                                                  new RaiseDisputeAction(context,dispute_id,add_msg_edt.getText().toString()).execute();
                                                  //Toast.makeText(context, ""+dispute_id, Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }
            );
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                }
            });

            dialog.show();
        }
    }


    private class RaiseDisputeAction extends AsyncTask<String, Void, String> {
        Context context;
        String disputId,add_msg_edt;

        public RaiseDisputeAction(Context context, String dispute_id, String add_msg_edt) {
            this.context = context;
            this.disputId=dispute_id;
            this.add_msg_edt=add_msg_edt;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();

            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("message",add_msg_edt);
            map.put("dispute_id",disputId);

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.ACTION_DISPUTE,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);
            Util.cancelPgDialog(progressDialog);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        dialog.cancel();
                        Util.errorDialog(context, jsonObject.optString("msg"));

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, "Something wrong please try again!");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class RaiseDisputeActionDetails extends AsyncTask<String, Void, String> {
        Context context;
        String dispute_id;

        public RaiseDisputeActionDetails(Context context, String dispute_id) {
            this.context = context;
            this.dispute_id=dispute_id;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();

            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("dispute_id", dispute_id);


            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.ACTION_DISPUTE_DETAILS,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dist_list=new ArrayList<>();
            Log.e("response", ": " + s);
            Util.cancelPgDialog(progressDialog);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("item");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            HashMap<String,String> map=new HashMap<>();
                            map.put("dispute_id",jsonObject1.optString("dispute_id"));
                            map.put("comment",jsonObject1.optString("comment"));
                            map.put("user",jsonObject1.optString("user"));
                            map.put("created_at",jsonObject1.optString("created_at"));
                            dist_list.add(map);
                        }

                        listView.setAdapter(new DisputeDetailAdapter(context, dist_list));

//                        Util.errorDialog(context, jsonObject.optString("msg"));

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, "No Record found!");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
