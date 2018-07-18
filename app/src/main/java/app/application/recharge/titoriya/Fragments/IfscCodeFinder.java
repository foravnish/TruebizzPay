package app.application.recharge.titoriya.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.InternetStatus;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/13/2017.
 */

public class IfscCodeFinder extends Fragment implements View.OnClickListener {
    TextView title;
    LinearLayout bck_ly;
    EditText bnk_name, br_add;
    Button search;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ifsc_code_finder_fragment, container, false);
        inIt(view);

        return view;
    }

    private void inIt(View view) {
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Ifsc Code Finder");

        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        bnk_name = (EditText) view.findViewById(R.id.bnk_name);
        br_add = (EditText) view.findViewById(R.id.br_add);
        search = (Button) view.findViewById(R.id.search);

        bck_ly.setOnClickListener(this);
        search.setOnClickListener(this);

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
            case R.id.search:
                if (bnk_name.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter bank Name");
                } else if (br_add.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Branch Address");
                } else if (InternetStatus.isConnectingToInternet(getActivity())) {
                    new GetBankListAync(getActivity(), bnk_name.getText().toString(),
                    br_add.getText().toString()).execute();
                }
                break;
        }
    }

    // getBank List AsyncTask
    class GetBankListAync extends AsyncTask<String, Void, String> {
        String bank_name, branch_name;
        Context context;

        public GetBankListAync(Context context, String bank_name, String branch_name) {
            this.context = context;
            this.bank_name = bank_name;
            this.branch_name = branch_name;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_token", MyPrefrences.getToken(context));
            params.put("agent_id", MyPrefrences.getUserId(context));
            params.put("bene_number", MyPrefrences.getUserMobile(context));
            params.put("bene_bankname", bank_name);
            params.put("bene_branchname",branch_name);


            JSONParser jsonParser = new JSONParser();
            String result = jsonParser.makeHttpRequest(Api.Bank_List_Url, "GET", params);

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
                        Log.e("arrayListData", ": " + jsonObject.optJSONArray("item"));

                        Fragment hisFrag = new Historyfragment();
                        FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("hist_type", "Bank List");
                        bundle1.putSerializable("bank_array", jsonObject.optJSONArray("item").toString());

                        hisFrag.setArguments(bundle1);
                        fragmentTransaction1.replace(R.id.container, hisFrag).addToBackStack("one");
                        fragmentTransaction1.commit();
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
                        //Util.errorDialog(context, jsonObject.optString("message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////
}

