package app.application.recharge.titoriya.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import app.application.recharge.titoriya.adapter.DisputeAdapter;
import app.application.recharge.titoriya.connection.JSONParser;


/**
 * Created by user on 2/13/2017.
 */

public class DisputeFragment extends Fragment implements View.OnClickListener {
    TextView title, list_title;
    LinearLayout bck_ly, right_ly, left_ly;
    Button r_dis_btn;
    ViewPager disp_dtls_vp;
    ProgressDialog progressDialog;
    EditText tr_id__edt,add_msg_edt;
    Dialog dialog;
    private ArrayList<HashMap<String, String>> dispts_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disputefragment, container, false);
        inIt(view);
        return view;
    }

    private void inIt(View view) {
        dispts_list = new ArrayList<>();
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Dispute");
        left_ly = (LinearLayout) view.findViewById(R.id.left_ly);
        right_ly = (LinearLayout) view.findViewById(R.id.right_ly);
        list_title = (TextView) view.findViewById(R.id.list_title);
        list_title.setText("Disputes Details");
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        r_dis_btn = (Button) view.findViewById(R.id.r_dis_btn);
        disp_dtls_vp = (ViewPager) view.findViewById(R.id.disp_dtls_vp);


        new RaiseDisputeDetails(getActivity()).execute();




        r_dis_btn.setOnClickListener(this);
        bck_ly.setOnClickListener(this);
        left_ly.setOnClickListener(this);
        right_ly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bck_ly:
                Fragment fragment = new RetailerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
                break;
            case R.id.r_dis_btn:
                raiseDisputeDialog(getActivity());
                break;
            case R.id.left_ly:
                if (dispts_list.size() > 0) {
                    Historyfragment.handleSetPreviousItem(disp_dtls_vp);
                }
                break;
            case R.id.right_ly:
                if (dispts_list.size() > 0) {
                    Historyfragment.handleSetNextItem(disp_dtls_vp);
                }
        }
    }

    private void raiseDisputeDialog(final Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.raisedisputedialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button subbmit = (Button) dialog.findViewById(R.id.subbmit);
        tr_id__edt= (EditText) dialog.findViewById(R.id.tr_id__edt);
        add_msg_edt = (EditText) dialog.findViewById(R.id.add_msg_edt);

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
                if (tr_id__edt.getText().toString().isEmpty()) {
                    Util.errorDialog(context, "Please Enter Transaction Id");
                } else if (add_msg_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(context, "Please Enter Message");
                } else {


                    new RaiseDispute(getActivity(),tr_id__edt,add_msg_edt,dialog).execute();
                    //dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    private class RaiseDispute extends AsyncTask<String, Void, String> {
        Context context;
        EditText tr_id__edt,add_msg_edt;
        Dialog dialog;
        public RaiseDispute(Context context, EditText tr_id__edt, EditText add_msg_edt, Dialog dialog) {
            this.context = context;
            this.tr_id__edt=tr_id__edt;
            this.add_msg_edt=add_msg_edt;
            this.dialog=dialog;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));
            map.put("message", add_msg_edt.getText().toString());
            map.put("transactionid", tr_id__edt.getText().toString());

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.RAISE_DISPUTE,"GET",map);

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

                        Util.errorDialog(context,jsonObject.optString("msg"));
                        dialog.cancel();

                        new RaiseDisputeDetails(getActivity()).execute();

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }
                    else {
                        Util.errorDialog(context,jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }





    private class RaiseDisputeDetails extends AsyncTask<String, Void, String> {
        Context context;
        EditText tr_id__edt,add_msg_edt;
        Dialog dialog;
        public RaiseDisputeDetails(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.RAISE_DISPUTE_DETAILS,"GET",map);

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
                    dispts_list.clear();
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("item");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<>();
                            map.put("transaction_id", jsonObject1.optString("transaction_id"));
                            map.put("dispute_id", jsonObject1.optString("dispute_id"));
                            map.put("message", jsonObject1.optString("message"));
                            map.put("status", jsonObject1.optString("status"));
                            map.put("dispute_created", jsonObject1.optString("dispute_created"));
                            dispts_list.add(map);
                        }

                        disp_dtls_vp.setAdapter(new DisputeAdapter(getActivity(), dispts_list));
                        disp_dtls_vp.setCurrentItem(0);

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }


                    else {
                        Util.errorDialog(context,jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }



}
