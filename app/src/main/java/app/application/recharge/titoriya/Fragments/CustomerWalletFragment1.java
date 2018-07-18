package app.application.recharge.titoriya.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.Const;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerWalletFragment1 extends Fragment {

    ListView gridView;
    GridView historygridView;
    GridView gridView2;
    AdapterList adapterList;
    AdapterListHistory adapterListHistory;
    AdapterListHistory.AdapterListReIniate adapterListReIniate;
    List<Const> DataList=new ArrayList<>();
    List<Const> DataList_History=new ArrayList<>();
    List<Const> DataList_Reiniate=new ArrayList<>();
    TextView bal_txv,limit_txv,exit,add_ben,hist,ben_list;
    LinearLayout add_ben_ly,otp_ly,historyLayout;

    EditText ben_name_edt,ben_acc_num_edt,ifsc_edt;
    Button ben_sbt_btn;

    EditText otp_code_edt;
    Button otp_sbt_btn;
    ProgressDialog progressDialog;
    String otc_ref = "";
    Dialog dialog;
    HashMap<String,String> Data=new HashMap<>();
    String[] values = new String[10];
    double amountValue;
    JSONArray histJsonArray=null;
    public CustomerWalletFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viwe= inflater.inflate(R.layout.fragment_customer_wallet_fragment1, container, false);
        gridView=(ListView)viwe.findViewById(R.id.gridView);
        bal_txv=(TextView)viwe.findViewById(R.id.bal_txv);
        limit_txv=(TextView)viwe.findViewById(R.id.limit_txv);
        add_ben=(TextView)viwe.findViewById(R.id.add_ben);
        hist=(TextView)viwe.findViewById(R.id.hist);
        exit=(TextView)viwe.findViewById(R.id.exit);
        ben_list=(TextView)viwe.findViewById(R.id.ben_list);
        add_ben_ly=(LinearLayout)viwe.findViewById(R.id.add_ben_ly);
        otp_ly=(LinearLayout)viwe.findViewById(R.id.otp_ly);

        ben_name_edt=(EditText)viwe.findViewById(R.id.ben_name_edt);
        ben_acc_num_edt=(EditText)viwe.findViewById(R.id.ben_acc_num_edt);
        ifsc_edt=(EditText)viwe.findViewById(R.id.ifsc_edt);
        ben_sbt_btn=(Button)viwe.findViewById(R.id.ben_sbt_btn);

        otp_code_edt=(EditText)viwe.findViewById(R.id.otp_code_edt);
        otp_sbt_btn=(Button)viwe.findViewById(R.id.otp_sbt_btn);

        historygridView=(GridView)viwe.findViewById(R.id.historygridView);
        historyLayout=(LinearLayout)viwe.findViewById(R.id.historyLayout);

//        Log.d("dgvdfghdfddhdghdfsfsdf",getArguments().getString("jsondata"));
        try {

            JSONObject jsonObject=new JSONObject(getArguments().getString("jsondata"));

            JSONObject jsonObject11=jsonObject.getJSONObject("msg");

            Log.d("dfgdfgddf",jsonObject11.optString("account"));

            bal_txv.setText("Balance :  ₹ "+jsonObject11.optString("retailer_avl_bal"));
            limit_txv.setText("Remaining Limit : ₹ "+jsonObject11.optString("waller_balance"));

            if (jsonObject.optString("status").equals("1")){
                JSONArray jsonArray= jsonObject.getJSONArray("item");

                // Log.d("dgvdfghdfddhdghdfsfsdf",jsonArray.toString());

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                    DataList.add(new Const(jsonObject1.optString("info")));
                }
            }

            else if (jsonObject.optString("status").equals("0")){

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapterList=new AdapterList();
        gridView.setAdapter(adapterList);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = new MoneyTransferWalletLoginFragment();
                FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.container, fragment1).addToBackStack("one");
                fragmentTransaction1.commit();
            }
        });
        hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                add_ben_ly.setVisibility(View.GONE);
                new CustomerRemitHistAync(getActivity()).execute();
                Log.d("|gfdhgfhfghfgh", MyPrefrences.getUserId(getActivity()));
            }
        });
        add_ben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gridView.setVisibility(View.GONE);
                add_ben_ly.setVisibility(View.VISIBLE);
                otp_ly.setVisibility(View.GONE);
            }
        });

        ben_sbt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ben_name_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Benificiary Name");
                } else if (ben_acc_num_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Account Number");
                } else if (ifsc_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter IFSC Code");
                } else {
                    // Toast.makeText(getActivity(), "working", Toast.LENGTH_SHORT).show();
                    new AddBeniAync(getActivity(), ben_name_edt.getText().toString()
                            , ben_acc_num_edt.getText().toString(), ifsc_edt.getText().toString())
                            .execute();
                }
            }
        });

        otp_sbt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp_code_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter OTP");
                } else if (otp_code_edt.getText().toString().length() < 4) {
                    Util.errorDialog(getActivity(), "Please Enter Valid OTP");
                } else {
                    new CustomerOtpConfirmAync(getActivity(), otp_code_edt.getText().toString(), otc_ref).execute();
                }
            }
        });


        ben_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.VISIBLE);
                add_ben_ly.setVisibility(View.GONE);
                historyLayout.setVisibility(View.GONE);
            }
        });
        return viwe;
    }

    public class CustomerRemitHistAync extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;

        public CustomerRemitHistAync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_mobile", MyPrefrences.getUserMobile(context));
            params.put("user_token", MyPrefrences.getToken(context));
            params.put("agent_id", MyPrefrences.getUserId(context));

            Log.d("hgfhbfghj", MyPrefrences.getUserMobile(context));
            Log.d("hgfhbfghj", MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Remitance_Hitory_Url_New, "GET", params);


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("responseftgdfrg", ": " + s);
            progressDialog.dismiss();
           // String ss=s.replace("{","1").replace("}","2").replace("[","{").replace("]","}").replace("1","[").replace("2","]");
           // Log.e("gfdgdhfhdghfghfghfg", ": " + ss);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                        DataList_History.clear();
                        Log.d("gdfdfgddfdhdgfhdd",jsonObject.optString("item"));
                        histJsonArray= jsonObject.optJSONArray("item");
                       // getBenificiaryHistList(histJsonArray);
                        if (histJsonArray==null){
                            Toast.makeText(context, "No Record found...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (int i = 0; i < histJsonArray.length(); i++) {
                                JSONObject jsonObject1 = histJsonArray.getJSONObject(i);

                                DataList_History.add(new Const(jsonObject1.optString("info")));

                            }
                        }

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }
                    else {
                        Util.errorDialog(context, jsonObject.optString("msg"));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapterListHistory=new AdapterListHistory();
            historygridView.setAdapter(adapterListHistory);

        }

    }

    public class AddBeniAync extends AsyncTask<String, Void, String> {
        String bname, bAcc, ifsc;
        Context context;

        public AddBeniAync(Context context, String bname, String bAcc, String ifsc) {
            this.context = context;
            this.bname = bname;
            this.bAcc = bAcc;
            this.ifsc = ifsc;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("bene_fname", bname);
            params.put("bene_acno", bAcc);
            params.put("bene_ifsc", ifsc);
            params.put("bene_number", MyPrefrences.getUserMobile(context));
            params.put("user_token",  MyPrefrences.getToken(context));

            Log.d("fgvgdgvdfgdfgh",bname);
            Log.d("fgvgdgvdfgdfgh",bAcc);
            Log.d("fgvgdgvdfgdfgh",ifsc);
            Log.d("fgvgdgvdfgdfgh",MyPrefrences.getUserMobile(context));
            Log.d("fgvgdgvdfgdfgh",MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Add_Benifi_new, "GET", params);
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
                        Toast.makeText(context, "Sent OTP on Your Mobile", Toast.LENGTH_SHORT).show();
                        otc_ref = jsonObject.optString("item");
                        add_ben_ly.setVisibility(View.GONE);
                        otp_ly.setVisibility(View.VISIBLE);
                        ben_acc_num_edt.setText("");
                        ben_name_edt.setText("");
                        ifsc_edt.setText("");
                        //new CustomerValidationAync(context, true).execute();
                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class CustomerValidationAync extends AsyncTask<String, Void, String> {
        Context context;
        ProgressDialog progressDialog;
        boolean isValidation;

        public CustomerValidationAync(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            this.isValidation = isValidation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile",  MyPrefrences.getUserMobile(context));
            params.put("user_token", MyPrefrences.getToken(context));
            params.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Validation_Url_new, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", ": " + s);
            Log.d("dfgdfhgbdfhdgdfdgh123",s.toString());

            Util.cancelPgDialog(progressDialog);



            try {

                JSONObject jsonObject=new JSONObject(s);

                JSONObject jsonObject11=jsonObject.getJSONObject("msg");

                Log.d("dfgdfgddf",jsonObject11.optString("account"));

//                bal_txv.setText("Balance : "+jsonObject11.optString("retailer_avl_bal"));
//                limit_txv.setText("Remaining Limit : "+jsonObject11.optString("waller_balance"));

                if (jsonObject.optString("status").equals("1")){
                    JSONArray jsonArray= jsonObject.getJSONArray("item");
                    DataList.clear();
                    // Log.d("dgvdfghdfddhdghdfsfsdf",jsonArray.toString());

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        DataList.add(new Const(jsonObject1.optString("info")));
                    }
                }
                else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                    Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                    MyPrefrences.resetPrefrences(context);
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity)context).finish();
                }
                else if (jsonObject.optString("status").equals("0")){

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapterList=new AdapterList();
            gridView.setAdapter(adapterList);



        }

    }


    public class CustomerOtpConfirmAyncForDelete extends AsyncTask<String, Void, String> {
        String otp, otp_ref;
        Context context;

        public CustomerOtpConfirmAyncForDelete(Context context, String otp, String otp_ref) {
            this.context = context;
            this.otp = otp;
            this.otp_ref = otp_ref;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("otc", otp);
            params.put("otcref",otp_ref);
            params.put("mobile", MyPrefrences.getUserMobile(context));
            params.put("user_token", MyPrefrences.getToken(context));

            Log.d("gdfddhfddhfghhh",otp);
            Log.d("gdfddhfddhfghhh",otp_ref);
            Log.d("gdfddhfddhfghhh",MyPrefrences.getUserMobile(context));
            Log.d("gdfddhfddhfghhh",MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Add_Benifi_OTP_For_Delete_new, "GET", params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("gdfgdfgdfhdfhfgh", ": " + s);
            Util.cancelPgDialog(progressDialog);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        otp_ly.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                        otp_code_edt.setText("");
                        new CustomerValidationAync(context).execute();

                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class CustomerOtpConfirmAync extends AsyncTask<String, Void, String> {
        String otp, otp_ref;
        Context context;

        public CustomerOtpConfirmAync(Context context, String otp, String otp_ref) {
            this.context = context;
            this.otp = otp;
            this.otp_ref = otp_ref;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("otc", otp);
            params.put("otcref",otp_ref);
            params.put("mobile", MyPrefrences.getUserMobile(context));
            params.put("user_token", MyPrefrences.getToken(context));

            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Customer_Add_Benifi_OTP_new, "GET", params);
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
                        Toast.makeText(context, "Benificer Add Successfully", Toast.LENGTH_SHORT).show();
                        otp_ly.setVisibility(View.GONE);
                        otp_code_edt.setText("");

                       // onResume();
                        //new CustomerValidationAync(context, true).execute();
                        Fragment fragment = new MoneyTransferWalletLoginFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                        fragmentTransaction.commit();


                    }
                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                        MyPrefrences.resetPrefrences(context);
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                    }

                    else {
                        Util.errorDialog(context, jsonObject.optString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    static  class  ViewHolder1{
        TextView name,name1;

    }


    class  AdapterListHistory extends BaseAdapter {
        LayoutInflater inflater;
        String cardStatusString;

        AdapterListHistory(){
            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return DataList_History.size();
        }

        @Override
        public Object getItem(int i) {
            return DataList_History.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.custonlistview_history,viewGroup,false);

            final ViewHolder1 viewHolder1=new ViewHolder1();
            viewHolder1.name=(TextView)view.findViewById(R.id.name);
            viewHolder1.name1=(TextView)view.findViewById(R.id.name1);
//            viewHolder.name.setText(Html.fromHtml(DataList.get(i).getName().toString()));
            viewHolder1.name.setText(Html.fromHtml(DataList_History.get(i).getName().toString()));

            if (DataList_History.get(i).getName().toString().contains("FAILED")){
                viewHolder1. name1.setText("REINIATE");
                viewHolder1.name1.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder1.name1.setVisibility(View.GONE);
            }

            viewHolder1.name1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openDialog(DataList_History.get(i).getName().toString());

                }
            });
            return view;
        }

        private void openDialog(String info) {

            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertdialogcustom_reiniate);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
            gridView2=(GridView)dialog.findViewById(R.id.gridView2);

            text.setText("Re-iniate");
            Button button = (Button) dialog.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            new CustomerREInitate(getActivity(),info).execute();

        }

        private class CustomerREInitate extends AsyncTask<String, Void, String> {
            Context context;
            ProgressDialog progressDialog;
            String infomation;
            public CustomerREInitate(Context context, String infomation) {
                this.context = context;
                this.infomation=infomation;
                progressDialog = new ProgressDialog(context);
                Util.showPgDialog(progressDialog);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> params = new HashMap<>();
                params.put("agent_id", MyPrefrences.getUserId(context));
                params.put("user_token", MyPrefrences.getToken(context));

                Log.d("hgfhbfghj", MyPrefrences.getUserMobile(context));
                Log.d("hgfhbfghj", MyPrefrences.getToken(context));

                JSONParser jsonParser = new JSONParser();
                String result = jsonParser.makeHttpRequest(Api.Customer_Remitance_ReIniate_New, "GET", params);


                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("responseftgdfrg", ": " + s);
                progressDialog.dismiss();
                Util.cancelPgDialog(progressDialog);
                // String ss=s.replace("{","1").replace("}","2").replace("[","{").replace("]","}").replace("1","[").replace("2","]");
                // Log.e("gfdgdhfhdghfghfghfg", ": " + ss);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                            DataList_Reiniate.clear();
                            Log.d("gdfdfgddfdhdgfhdd",jsonObject.optString("item"));
                            JSONArray histJsonArray = jsonObject.optJSONArray("item");
                            // getBenificiaryHistList(histJsonArray);
                            for (int i=0;i<histJsonArray.length();i++){
                                JSONObject jsonObject1=histJsonArray.getJSONObject(i);

                                DataList_Reiniate.add(new Const(jsonObject1.optString("bene_name"),jsonObject1.optString("bene_ac"),jsonObject1.optString("ifsc_code"),infomation));

                            }

                        }
                        else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                            Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                            MyPrefrences.resetPrefrences(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity)context).finish();
                        }

                        else {
                            Util.errorDialog(context, jsonObject.optString("msg"));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterListReIniate=new AdapterListReIniate();
                gridView2.setAdapter(adapterListReIniate);

            }


        }
         class  ViewHolder2{
            TextView name,acount,code,action;
            EditText amount,amounttext;
            Button delete;
            Spinner spiner;
        }
        private class AdapterListReIniate extends BaseAdapter {
            LayoutInflater inflater;
            String cardStatusString;
            AdapterListReIniate(){
                inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }

            @Override
            public int getCount() {
                return DataList_Reiniate.size();
            }

            @Override
            public Object getItem(int i) {
                return DataList_Reiniate.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                view=inflater.inflate(R.layout.custonlistviewreninite,viewGroup,false);
                final ViewHolder2 viewHolder2=new ViewHolder2();

                viewHolder2.name=(TextView)view.findViewById(R.id.name);
                viewHolder2.acount=(TextView)view.findViewById(R.id.acount);
                viewHolder2.code=(TextView)view.findViewById(R.id.code);
                viewHolder2.action=(TextView)view.findViewById(R.id.action);

               viewHolder2.name.setText(DataList_Reiniate.get(i).getName().toString());
               viewHolder2.acount.setText(DataList_Reiniate.get(i).getAc().toString());
               viewHolder2.code.setText(DataList_Reiniate.get(i).getCode().toString());

                viewHolder2.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       // Toast.makeText(getActivity(), "clicked"+i, Toast.LENGTH_SHORT).show();
                        new CustomerREInitateSend(getActivity(),DataList_Reiniate.get(i).getInfo().toString(),DataList_Reiniate.get(i).getAc().toString()).execute();


                    }
                });

                return view;
            }






            private class CustomerREInitateSend extends AsyncTask<String, Void, String> {
                Context context;
                ProgressDialog progressDialog;
                String info,ac_no;

                public CustomerREInitateSend(Context context, String info, String ac_no) {
                    this.context = context;
                    this.info=info;
                    this.ac_no=ac_no;
                    progressDialog = new ProgressDialog(context);
                    Util.showPgDialog(progressDialog);
                }
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }

                @Override
                protected String doInBackground(String... strings) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("info", info);
                    params.put("ac_no", ac_no);
                    params.put("mob_number", MyPrefrences.getUserMobile(context));
                    params.put("user_token", MyPrefrences.getToken(context));
                    params.put("agent_id", MyPrefrences.getUserId(context));

                    Log.d("fgvdfgfdhdfdhfghfg", info);
                    Log.d("fgvdfgfdhdfdhfghfg", ac_no);
                    Log.d("fgvdfgfdhdfdhfghfg", MyPrefrences.getUserMobile(context));
                    Log.d("fgvdfgfdhdfdhfghfg", MyPrefrences.getToken(context));
                    Log.d("fgvdfgfdhdfdhfghfg", MyPrefrences.getUserId(context));

                    JSONParser jsonParser = new JSONParser();
                    String result = jsonParser.makeHttpRequest(Api.Customer_Remitance_ReIniate_Send_New, "GET", params);

                    return result;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Log.e("responseftgdfrg", ": " + s);
                    progressDialog.dismiss();
                    Util.cancelPgDialog(progressDialog);



                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject != null) {
                            if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                                Util.errorDialog(getActivity(),jsonObject.optString("msg"));
                                dialog.dismiss();
                            }
                            else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                                MyPrefrences.resetPrefrences(context);
                                context.startActivity(new Intent(context, LoginActivity.class));
                                ((Activity)context).finish();
                            }

                        }
                        else{
                            Util.errorDialog(getActivity(),jsonObject.optString("msg"));
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }


        }
    }
    static  class  ViewHolder{
        TextView name,baneCode,accountNo,type,ifscCode,status;
        EditText amount,amounttext;
        Button remit,delete;
        Spinner spiner;
    }
    class AdapterList extends BaseAdapter {

        LayoutInflater inflater;
        String cardStatusString;
        private String[] arraySpinner;

        AdapterList(){
            inflater=(LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return DataList.size();
        }

        @Override
        public Object getItem(int i) {
            return DataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.custonlistview,viewGroup,false);
            final ViewHolder viewHolder=new ViewHolder();

            viewHolder.name=(TextView)view.findViewById(R.id.name);



            viewHolder.spiner =(Spinner)view.findViewById(R.id.spiner);

//            amount=(EditText)view.findViewById(R.id.amount);
            viewHolder.amounttext=(EditText)view.findViewById(R.id.amounttext);

            viewHolder.amounttext.setClickable(true);

            viewHolder.remit=(Button)view.findViewById(R.id.remit);
            viewHolder.delete=(Button)view.findViewById(R.id.delete);

            viewHolder.spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    cardStatusString= parent.getItemAtPosition(pos).toString();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            viewHolder.remit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



//                    if (viewHolder.amounttext.getText().toString().equals("")) {
//                        Util.errorDialog(getActivity(), "Please enter amount");
//
//
//                    }
                    try {
                        amountValue = Double.parseDouble(viewHolder.amounttext.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (amountValue<10){

                        Util.errorDialog(getActivity(), "Please enter minimum amount ₹ 10");

                    }
                   else {
                        Log.d("dfgdfghdfhddghfgd1", viewHolder.amounttext.getText().toString());
                        // limit_txv.setText("not blank");
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alertdialogcustom1);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
                        text.setText("Do you want to  ₹ "+amountValue+" remittance now ?");
                        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
                        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                            if (viewHolder.amounttext.getText().toString().equals("")){
//                                Util.errorDialog(getActivity(),"Please enter amount" );
//
//                            }else {
//                                Log.d("dfgdfghdfhddghfgd1", viewHolder.amounttext.getText().toString());
//                                // limit_txv.setText("not blank");
//                                new RemitNow(getActivity(),DataList.get(i).getName().toString(), viewHolder.amounttext.getText().toString(),cardStatusString).execute();
//                            }
                                new RemitNow(getActivity(),DataList.get(i).getName().toString(), viewHolder.amounttext.getText().toString(),cardStatusString).execute();
                                dialog.dismiss();
                            }
                        });

                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }

                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertdialogcustom1);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
                    text.setText("Do you want to delete now ?");
                    Button ok = (Button) dialog.findViewById(R.id.btn_ok);
                    Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DeleteNow(getActivity(),DataList.get(i).getName().toString()).execute();
                            dialog.dismiss();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });

//            String sss=DataList.get(i).getName().toString();
//
//            String s2=sss.replaceAll("\n","|");
//            Log.d("fgdfgdfhbdhdghfgdghd",s2);
//            name.setText(s2);

            viewHolder.name.setText(Html.fromHtml(DataList.get(i).getName().toString()));
//            baneCode.setText(DataList.get(i).getCode().toString());
//            accountNo.setText(DataList.get(i).getAccount().toString());
//            type.setText(DataList.get(i).getType().toString());
//            ifscCode.setText(DataList.get(i).getIfsc().toString());
//            status.setText(DataList.get(i).getStatus().toString());


            this.arraySpinner = new String[] {
                    "IMPS", "NEFT"
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.simple_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(R.layout.textview);
            viewHolder.spiner.setAdapter(adapter);





            return view;
        }
        private class RemitNow extends AsyncTask<String, Void, String> {
            String bene_code, remit_mob,ifsc_code,remit_amount,agent_id,bene_name,sel_type,bene_acc,info;
            Context context;


            public RemitNow(Context context, String info, String remit_amount, String sel_type) {
                this.context = context;
                this.info = info;
                this.remit_amount = remit_amount;
                this.sel_type = sel_type;

                progressDialog = new ProgressDialog(context);
                Util.showPgDialog(progressDialog);
            }

            @Override
            protected String doInBackground(String... strings) {

                HashMap<String, String> params = new HashMap<>();
                //params.put("bene_code", );
                params.put("info",info);
                params.put("remit_mob",MyPrefrences.getUserMobile(context));
                //params.put("ifsc_code",);
                params.put("remit_amount", remit_amount.toString());
                params.put("agent_id", MyPrefrences.getUserId(context));
                //params.put("bene_name",);
                params.put("sel_type", sel_type.toString());
                params.put("user_token",MyPrefrences.getToken(context));
//                params.put("bene_acc", );

                Log.d("dfgdfgdfghbdfh",remit_amount.toString());
                Log.d("dfgdfgdfghbdfh",sel_type.toString());
                Log.d("dfgdfgdfghbdfh",info.toString());

                JSONParser jsonParser = new JSONParser();
                String result = jsonParser.makeHttpRequest(Api.Customer_Remited_new, "GET", params);
                return result;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("responsedfsfsd", ": " + s);
                Util.cancelPgDialog(progressDialog);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                            bal_txv.setText("Balance : "+jsonObject.optString("agent_balance"));
                           // Toast.makeText(context, "Successfully Sent", Toast.LENGTH_SHORT).show();
                            Util.errorDialog(context, jsonObject.optString("msg"));
                            // onResume();
                        }
                        else if(jsonObject.optString("msg").equalsIgnoreCase("You have insufficient balance")){
                            Util.errorDialog(context,jsonObject.optString("msg"));
                        }
                        else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                            Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                            MyPrefrences.resetPrefrences(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity)context).finish();
                        }
                        else {
                            Util.errorDialog(context, jsonObject.optString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        private class DeleteNow  extends AsyncTask<String, Void, String> {
            String bene_code, remit_mob,ifsc_code,remit_amount,agent_id,bene_name,sel_type,bene_acc,info;
            Context context;
            public DeleteNow(Context context, String info) {
                this.context = context;
                this.info = info;
                progressDialog = new ProgressDialog(context);
                Util.showPgDialog(progressDialog);

            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> params = new HashMap<>();
                //params.put("bene_code", );
                params.put("info",info);
                params.put("number",MyPrefrences.getUserMobile(context));
                params.put("user_token",MyPrefrences.getToken(context));
//                params.put("bene_acc", );

                Log.d("fgfhfhgfhfghfgh",info);
                Log.d("fgfhfhgfhfghfgh",MyPrefrences.getUserMobile(context));
                Log.d("fgfhfhgfhfghfgh",MyPrefrences.getToken(context));

                JSONParser jsonParser = new JSONParser();
                String result = jsonParser.makeHttpRequest(Api.Customer_Deleted_new, "GET", params);

                return result;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("responsedfsfsd", ": " + s);
                Util.cancelPgDialog(progressDialog);
                try {
                    final JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        if (jsonObject.optString("status").equalsIgnoreCase("1")) {

                            //Toast.makeText(context, "Successfully Deleted...", Toast.LENGTH_SHORT).show();
                            Util.errorDialog(context, jsonObject.optString("msg"));

                            otp_ly.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);

                            otp_sbt_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (otp_code_edt.getText().toString().isEmpty()) {
                                        Util.errorDialog(getActivity(), "Please Enter OTP");
                                    } else if (otp_code_edt.getText().toString().length() < 4) {
                                        Util.errorDialog(getActivity(), "Please Enter Valid OTP");
                                    } else {
                                        new CustomerOtpConfirmAyncForDelete(getActivity(), otp_code_edt.getText().toString(), jsonObject.optString("item")).execute();
                                    }
                                }
                            });

                            // new CustomerValidationAync(getActivity()).execute();
                            // onResume();
                        }
                        else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
                            Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
                            MyPrefrences.resetPrefrences(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity)context).finish();
                        }

                        else {
                            Util.errorDialog(context, jsonObject.optString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
