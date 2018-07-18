package app.application.recharge.titoriya.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.CircularViewPagerHandler;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.adapter.BankDetailsAdapter;
import app.application.recharge.titoriya.adapter.HistoryAdapter;
import app.application.recharge.titoriya.adapter.IFSCAdapter;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/8/2017.
 */

public class Historyfragment extends Fragment implements View.OnClickListener {
    ViewPager viewPager;
    String hist_type = "";
    TextView title, list_title;
    LinearLayout bck_ly, left_ly, right_ly;
    private ArrayList<HashMap<String, String>> histItms;
    Context context;
    public JSONArray bankArray;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String,String>> BankData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hist_type = getArguments().getString("hist_type");


        if (hist_type.equalsIgnoreCase("Bank List")) {
            try {
                bankArray = new JSONArray(getArguments().getString("bank_array"));
                Log.d("gfdgdfgfdghdf",bankArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.histroyfragment, container, false);
        title = (TextView) view.findViewById(R.id.title);
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        left_ly = (LinearLayout) view.findViewById(R.id.left_ly);
        right_ly = (LinearLayout) view.findViewById(R.id.right_ly);
        list_title = (TextView) view.findViewById(R.id.list_title);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        title.setText(hist_type);
        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));

        bck_ly.setOnClickListener(this);
        right_ly.setOnClickListener(this);
        left_ly.setOnClickListener(this);
        histItms = new ArrayList<>();
        getHistoryDetails(hist_type);

       /* viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });*/
        //LinearLayout hist_dtl=(LinearLayout) view.findViewById(R.id.hist_dtl);
        // Set in/out flipping animations
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bck_ly:
                Fragment fragment;
//                if (MyPrefrences.getUserType(getActivity()).equalsIgnoreCase(Util.Distributor)) {
//                    fragment = new DistributorFragment();
//                } else if (hist_type.equalsIgnoreCase("Bank List")) {
//                    fragment = new IfscCodeFinder();
//
//                } else {
//                    fragment = new RetailerFragment();
//                }

                fragment = new RetailerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
                break;
            case R.id.left_ly:
                if (histItms.size() > 0) {
                    handleSetPreviousItem(viewPager);
                }
                else  if (BankData.size() > 0){
                    handleSetPreviousItem(viewPager);
                }
                break;
            case R.id.right_ly:
                if (histItms.size() > 0) {
                    handleSetNextItem(viewPager);
                }
                else  if (BankData.size() > 0){
                    handleSetNextItem(viewPager);
                }
                break;
        }
    }

    private void getHistoryDetails(final String hist_type) {
        if (hist_type.equalsIgnoreCase("Payment History")) {

            new PaymentHistory(getActivity()).execute();
//            histItms.clear();
//            for (int i = 0; i < 7; i++) {
//                HashMap<String, String> map = new HashMap<>();
//                int id = 1 + i;
//                map.put("id", "" + id);
//                map.put("tr_id", "001" + i + 234 + i);
//                map.put("amt", "4500");
//                map.put("acct", "01246584");
//                map.put("ref_num", "012468REf");
//                map.put("date", "01-02-2017");
//                map.put("time", "09:21:12 Pm");
//
//                map.put("status", "Failed");
//                map.put("msg", "its message");
//                map.put("action", "its action");
//                histItms.add(map);
//            }
//            HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
//            viewPager.setAdapter(historyAdapter);
//            viewPager.setCurrentItem(0);
//            list_title.setText("Last " + histItms.size() + " Payments Details");

        } else if (hist_type.equalsIgnoreCase("Remittance History")) {
            histItms.clear();
            for (int i = 0; i < 5; i++) {
                HashMap<String, String> map = new HashMap<>();
                int id = 1 + i;
                map.put("id", "" + id);
                map.put("tr_id", "001" + i + 234 + i);
                map.put("amt", "4500" + i);
                map.put("acct", i + "41545" + i);
                map.put("date", "04-01-2017");
                map.put("time", "12:21:12 Pm");
                map.put("status", "Sucess");
                map.put("mob_wallet", "Mobile Wallet " + i);
                map.put("ben_name", "Abcd");
                map.put("ben_code", i + "002" + i);
                histItms.add(map);
            }
            HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
            viewPager.setAdapter(historyAdapter);
            viewPager.setCurrentItem(0);
            list_title.setText("Last " + histItms.size() + " Remittances Details");

        } else if (hist_type.equalsIgnoreCase("Recharge History")) {


            new RechargeHistory(getActivity()).execute();

        } else if (hist_type.equalsIgnoreCase("Bank List")) {
            histItms.clear();
            list_title.setText("Bank List");
            BankData=new ArrayList<>();
            getBenificiaryList2(bankArray);


           /* for (int i = 0; i < 3; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("rch_type", "DTH");
                map.put("mob_num", "7645502543");
                map.put("operator", "Airtel");
                map.put("method", "My Method");
                map.put("ope_ref", "its reffrence ");
                map.put("tr_id", "" + i);
                map.put("date", "05-06-2016");
                map.put("time", " 11:21:12 Pm");
                map.put("status", "Failed");
                map.put("amt", "500" + i);
                histItms.add(map);
            }
            sendToBankList();*/
        } else if (hist_type.equalsIgnoreCase("Billing Summary")) {


            new BillingSummary(getActivity()).execute();

//            histItms.clear();
//            for (int i = 0; i < 6; i++) {
//                HashMap<String, String> map = new HashMap<>();
//                int id = 1 + i;
//                map.put("id", "" + id);
//                map.put("tr_id", "001" + i + 234 + i);
//                map.put("user", "amit kumar");
//                map.put("user_type", "Retailer");
//                map.put("tr_type", "Credit");
//                map.put("amount", "21000");
//                map.put("paid_amount", "11000");
//                map.put("date", "15/08/16");
//                map.put("time", "11:15:21 am");
//                histItms.add(map);
//            }
//            HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
//            viewPager.setAdapter(historyAdapter);
//            viewPager.setCurrentItem(0);



        } else if (hist_type.equalsIgnoreCase("Transaction Details")) {
            histItms.clear();
            for (int i = 0; i < 6; i++) {
                HashMap<String, String> map = new HashMap<>();
                int id = 1 + i;
                map.put("id", "" + id);
                map.put("tr_id", "001" + i + 234 + i);
                map.put("user", "amit kumar");
                map.put("user_type", "Retailer");
                map.put("tr_type", "Credit");
                map.put("act_amount", "1000");
                map.put("date", "25/08/16");
                map.put("time", "1:15:21 am");
                histItms.add(map);
            }
            HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
            viewPager.setAdapter(historyAdapter);
            viewPager.setCurrentItem(0);
        }
    }

    private void getBenificiaryList2(JSONArray bankArray) {

        Log.d("fgdfgdfhgdfhdfh", String.valueOf(bankArray.length()));
        for (int i=0;i<bankArray.length();i++) {
            try {
                JSONObject jsonObject1 = bankArray.getJSONObject(i);
                HashMap<String,String> map=new HashMap<>();
                map.put("bankname",jsonObject1.optString("bankname"));
                map.put("IFSC",jsonObject1.optString("IFSC"));
                map.put("BranchName",jsonObject1.optString("BranchName"));
                map.put("Address",jsonObject1.optString("Address"));
                map.put("City",jsonObject1.optString("City"));
                map.put("State",jsonObject1.optString("State"));

//                Log.d("dfgdfhgdfhfdgd",jsonObject1.optString("bankname"));
//                Log.d("dfgdfhgdfhfdgd",BankData.get(i).get("bankname"));
                BankData.add(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IFSCAdapter historyAdapter = new IFSCAdapter(context, BankData,hist_type);
        viewPager.setAdapter(historyAdapter);
        viewPager.setCurrentItem(1);
        list_title.setText("Last " + BankData.size() + " Records");
    }


    /////////////////// set adapter


    private void sendToBankList() {
        BankDetailsAdapter historyAdapter = new BankDetailsAdapter(context, histItms);
        viewPager.setAdapter(historyAdapter);
        viewPager.setCurrentItem(0);
    }

    ////////////////////////////////get Bank List
    private ArrayList<HashMap<String, String>> getBenificiaryList(JSONArray jsonArray) {
        ArrayList<HashMap<String, String>> benifiList = new ArrayList<>();
        if (jsonArray != null) {
            for (int j = 0; j < jsonArray.length() - 2; j++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    HashMap<String, String> map = new HashMap<>();
                    String info = jsonObject.optString("info");
                    Log.e("info", info);

                    //String s = "Android 4.3 is now available with a variety of performance improvements and new features";
                    Matcher addm = Pattern.compile("Address :\\s(.*)\\sCity :").matcher(info);
                    Matcher bnkname_matcher = Pattern.compile("BankName :\\s(.*)\\sIFSC :").matcher(info);
                    Matcher ifsc_matcher = Pattern.compile("IFSC :\\s(.*)\\sBranchName :").matcher(info);
                    Matcher bnhname_matcher = Pattern.compile("BranchName :\\s(.*)\\sAddress :").matcher(info);
                    Matcher ctyname_matcher = Pattern.compile("City :\\s(.*)\\sState :").matcher(info);
                    Matcher stename_matcher = Pattern.compile("State :\\s(.*)").matcher(info);


                    while (bnkname_matcher.find()) {
                        Log.e("bank_name", bnkname_matcher.group(1).replaceAll("[|]", ""));
                        map.put("bank_name", bnkname_matcher.group(1).replaceAll("[|]", ""));
                    }
                    while (ifsc_matcher.find()) {
                        Log.e("ifsc code", ifsc_matcher.group(1).replaceAll("[|]", ""));
                        map.put("ifsc_code", ifsc_matcher.group(1).replaceAll("[|]", ""));

                    }
                    while (bnhname_matcher.find()) {
                        Log.e("branch Name", bnhname_matcher.group(1).replaceAll("[|]", ""));
                        map.put("branch_Name", bnhname_matcher.group(1).replaceAll("[|]", ""));

                    }
                    while (addm.find()) {
                        Log.e("address", addm.group(1).replaceAll("[|]", ""));
                        map.put("address", addm.group(1).replaceAll("[|]", ""));

                    }
                    while (ctyname_matcher.find()) {
                        Log.e("city name", ctyname_matcher.group(1).replaceAll("[|]", ""));
                        map.put("city_name", ctyname_matcher.group(1).replaceAll("[|]", ""));

                    }
                    while (stename_matcher.find()) {
                        Log.e("state name", stename_matcher.group(1).replaceAll("[|]", ""));
                        map.put("state_name", stename_matcher.group(1).replaceAll("[|]", ""));
                    }
                    benifiList.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (benifiList.size() > 0) {
                histItms = benifiList;
                sendToBankList();

            } else {
                Util.errorDialog(getActivity(), "No Bank List Found");
            }
        } else {
            Util.errorDialog(getActivity(), "No Bank List Found");
        }
        return benifiList;
    }

    //////////////functions for swipe circular means a>b>c then reach at a
    public static void handleSetNextItem(ViewPager mViewPager) {
        if (mViewPager != null) {
            int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if (mViewPager.getCurrentItem() == lastPosition) {
                mViewPager.setCurrentItem(0, true);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        }
    }

    public static void handleSetPreviousItem(ViewPager mViewPager) {
        if (mViewPager != null) {

            if (mViewPager.getCurrentItem() == 0) {
                mViewPager.setCurrentItem(mViewPager.getAdapter().getCount(), true);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
            }
        }
    }

    private class RechargeHistory extends AsyncTask<String, Void, String> {
        Context context;
        public RechargeHistory(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
//            params.put("fname", name.toString());

                    Log.d("fdvgdfgbdfhgdf",MyPrefrences.getToken(context));
                    Log.d("fdvgdfgbdfhgdf",MyPrefrences.getUserId(context));


            params.put("user_token",MyPrefrences.getToken(context) );
            params.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_RechargeHistory, "GET", params);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("fsddgdfgdfdhfd", ": " + s);
            Util.cancelPgDialog(progressDialog);


            try {

                JSONObject obj = new JSONObject(s);
                String status = obj.getString("status");
                String msg = obj.getString("msg");

                if (status.equals("1")) {
                    histItms = new ArrayList<>();
                    JSONArray studentJsonArray = obj.getJSONArray("item");
                    if (studentJsonArray != null) {
                        for (int k = 0; k < studentJsonArray.length(); k++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("transaction_id", studentJsonArray.getJSONObject(k).getString("transaction_id"));
                            map.put("type", studentJsonArray.getJSONObject(k).getString("type"));
                            map.put("mobile", studentJsonArray.getJSONObject(k).getString("mobile"));
                            map.put("operator", studentJsonArray.getJSONObject(k).getString("operator"));
                            map.put("amount", studentJsonArray.getJSONObject(k).getString("amount"));
                            map.put("balance", studentJsonArray.getJSONObject(k).getString("balance"));
                            map.put("rech_type", studentJsonArray.getJSONObject(k).getString("rech_type"));
                            map.put("time", studentJsonArray.getJSONObject(k).getString("time"));
                            map.put("status", studentJsonArray.getJSONObject(k).getString("status"));

                            histItms.add(map);
                            //Toast.makeText(AssignedTask.this,"description is " +studentJsonArray.getJSONObject(k).optString("description")+"duration "+studentJsonArray.getJSONObject(k).getString("duration"),Toast.LENGTH_LONG).show();
                            //Log.w("lat long is :","value is :"+studentJsonArray.getJSONObject(k).optString("remarks")+" ="+studentJsonArray.getJSONObject(k).getString("date_time"));
                        }
                        HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
                        viewPager.setAdapter(historyAdapter);
                        viewPager.setCurrentItem(0);
                        list_title.setText("Last " + histItms.size() + " Recharges Details");

                        //Log.w("lat long is :","value is :"+"array value is : "+employee_list.get(0).get("long"));
                        //Util.openDialog(context, title, student_list);

                    }

                    //Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private class PaymentHistory extends AsyncTask<String, Void, String> {
        Context context;
        public PaymentHistory(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
//            params.put("fname", name.toString());

            Log.d("fdvgdfgbdfhgdf",MyPrefrences.getToken(context));
            Log.d("fdvgdfgbdfhgdf",MyPrefrences.getUserId(context));


            params.put("user_token",MyPrefrences.getToken(context) );
            params.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_paymentHistory, "GET", params);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("fsddgdfgdfdhfd", ": " + s);
            Util.cancelPgDialog(progressDialog);


            try {

                JSONObject obj = new JSONObject(s);
                String status = obj.getString("status");
                String msg = obj.getString("msg");

                if (status.equals("1")) {
                    histItms = new ArrayList<>();
                    JSONArray studentJsonArray = obj.getJSONArray("item");
                    if (studentJsonArray != null) {
                        for (int k = 0; k < studentJsonArray.length(); k++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("ac_name", studentJsonArray.getJSONObject(k).getString("ac_name"));
                            map.put("amount", studentJsonArray.getJSONObject(k).getString("amount"));
                            map.put("bank_refno", studentJsonArray.getJSONObject(k).getString("bank_refno"));
                            map.put("payment_date", studentJsonArray.getJSONObject(k).getString("payment_date"));
                            map.put("status", studentJsonArray.getJSONObject(k).getString("status"));
//                            map.put("balance", studentJsonArray.getJSONObject(k).getString("balance"));
//                            map.put("rech_type", studentJsonArray.getJSONObject(k).getString("rech_type"));
//                            map.put("time", studentJsonArray.getJSONObject(k).getString("time"));
//                            map.put("status", studentJsonArray.getJSONObject(k).getString("status"));

                            histItms.add(map);
                            //Toast.makeText(AssignedTask.this,"description is " +studentJsonArray.getJSONObject(k).optString("description")+"duration "+studentJsonArray.getJSONObject(k).getString("duration"),Toast.LENGTH_LONG).show();
                            //Log.w("lat long is :","value is :"+studentJsonArray.getJSONObject(k).optString("remarks")+" ="+studentJsonArray.getJSONObject(k).getString("date_time"));
                        }
                        HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
                        viewPager.setAdapter(historyAdapter);
                        viewPager.setCurrentItem(0);
                        list_title.setText("Last " + histItms.size() + " Payments Details");


//                        HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
//                        viewPager.setAdapter(historyAdapter);
//                        viewPager.setCurrentItem(0);
//                        list_title.setText("Last " + histItms.size() + " Recharges Details");

                        //Log.w("lat long is :","value is :"+"array value is : "+employee_list.get(0).get("long"));
                        //Util.openDialog(context, title, student_list);

                    }

                    //Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    private class BillingSummary extends AsyncTask<String, Void, String> {
        Context context;
        public BillingSummary(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
//            params.put("fname", name.toString());

            Log.d("fdvgdfgbdfhgdf",MyPrefrences.getToken(context));
            Log.d("fdvgdfgbdfhgdf",MyPrefrences.getUserId(context));


            params.put("user_token",MyPrefrences.getToken(context) );
            params.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_BillingSummary, "GET", params);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("fsddgdfgdfdhfd", ": " + s);
            Util.cancelPgDialog(progressDialog);


            try {

                JSONObject obj = new JSONObject(s);
                String status = obj.getString("status");
                String msg = obj.getString("msg");

                if (status.equals("1")) {
                    histItms = new ArrayList<>();
                    JSONArray studentJsonArray = obj.getJSONArray("item");
                    if (studentJsonArray != null) {
                        for (int k = 0; k < studentJsonArray.length(); k++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("transaction_id", studentJsonArray.getJSONObject(k).getString("transaction_id"));
                            map.put("tr_type", studentJsonArray.getJSONObject(k).getString("tr_type"));
                            map.put("amount", studentJsonArray.getJSONObject(k).getString("amount"));
//                            map.put("operator", studentJsonArray.getJSONObject(k).getString("operator"));
//                            map.put("amount", studentJsonArray.getJSONObject(k).getString("amount"));
//                            map.put("balance", studentJsonArray.getJSONObject(k).getString("balance"));
//                            map.put("rech_type", studentJsonArray.getJSONObject(k).getString("rech_type"));
//                            map.put("time", studentJsonArray.getJSONObject(k).getString("time"));
//                            map.put("status", studentJsonArray.getJSONObject(k).getString("status"));

                            histItms.add(map);
                            //Toast.makeText(AssignedTask.this,"description is " +studentJsonArray.getJSONObject(k).optString("description")+"duration "+studentJsonArray.getJSONObject(k).getString("duration"),Toast.LENGTH_LONG).show();
                            //Log.w("lat long is :","value is :"+studentJsonArray.getJSONObject(k).optString("remarks")+" ="+studentJsonArray.getJSONObject(k).getString("date_time"));
                        }
                        HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
                        viewPager.setAdapter(historyAdapter);
                        viewPager.setCurrentItem(0);
                        //list_title.setText("Last " + histItms.size() + " Payments Details");


//                        HistoryAdapter historyAdapter = new HistoryAdapter(context, histItms, hist_type);
//                        viewPager.setAdapter(historyAdapter);
//                        viewPager.setCurrentItem(0);
//                        list_title.setText("Last " + histItms.size() + " Recharges Details");

                        //Log.w("lat long is :","value is :"+"array value is : "+employee_list.get(0).get("long"));
                        //Util.openDialog(context, title, student_list);

                    }

                    //Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

///////////////////////////////////////////////////  search coach

}
