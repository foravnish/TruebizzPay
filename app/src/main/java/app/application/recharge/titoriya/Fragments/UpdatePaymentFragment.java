package app.application.recharge.titoriya.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import app.application.recharge.titoriya.LoginActivity;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;

/**
 * Created by user on 2/14/2017.
 */

public class UpdatePaymentFragment extends Fragment implements View.OnClickListener {
    TextView title, datePicker_txv;
    LinearLayout bck_ly;
    Calendar calendar;
    int year, day, month;
    Spinner bnkname_spn;

    List<String> bnkList = new ArrayList<String>();
    EditText amt_edt, acc_name_edt, acc_num_edt, ref_num_edt, msg_edt;
    Button updt_amy_btn;
    String bankName = "";
    ProgressDialog progressDialog;
    List<HashMap<String,String>> BankData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_payment_fragment, container, false);

        inIt(view);

        new GetBankList(getActivity()).execute();

        return view;
    }

    private void inIt(View view) {
        bnkname_spn = (Spinner) view.findViewById(R.id.bnkname_spn);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Update Payment");
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        datePicker_txv = (TextView) view.findViewById(R.id.datePicker_txv);
        amt_edt = (EditText) view.findViewById(R.id.amt_edt);
        acc_name_edt = (EditText) view.findViewById(R.id.acc_name_edt);
        acc_num_edt = (EditText) view.findViewById(R.id.acc_num_edt);
        ref_num_edt = (EditText) view.findViewById(R.id.ref_num_edt);
        msg_edt = (EditText) view.findViewById(R.id.msg_edt);
        updt_amy_btn = (Button) view.findViewById(R.id.updt_amy_btn);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("date ", "" + day + "/" + month + "/" + year);
        //  datePicker_txv.setText(""+ day + "/" + month + "/" + year);
        showDate(year, month + 1, day);


        bnkname_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                bankName = bnkname_spn.getSelectedItem().toString();
                  bankName = BankData.get(i).get("bank_id");
                //Toast.makeText(getActivity(), ""+BankData.get(i).get("bank_id"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bck_ly.setOnClickListener(this);
        datePicker_txv.setOnClickListener(this);
        updt_amy_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bck_ly:
                Fragment fragment = null;
//                if (MyPrefrences.getUserType(getActivity()).equalsIgnoreCase(Util.Distributor)) {
//                    fragment = new DistributorFragment();
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
            case R.id.datePicker_txv:
               // setDate();
                break;
            case R.id.updt_amy_btn:
                if (bankName.equalsIgnoreCase("Select Bank") || bankName.equalsIgnoreCase("")) {
                    Util.errorDialog(getActivity(), "Please Select Bank");
                } else if (amt_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Amount");
                } else if (acc_name_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Account Name");
                } else if (acc_num_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Account Number");
                } else if (ref_num_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Refrence Number");
                } else if (msg_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(getActivity(), "Please Enter Message");
                } else {

                    new UpdatePayment(getActivity()).execute();

//                    Toast.makeText(getActivity(), "workiing", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    ////////////////////////
    private void setBankNameList(Context context, Spinner spinner) {
        ArrayAdapter<String> bnklistAdapter = new ArrayAdapter<String>(context,R.layout.simple_spinner_item, bnkList);
        bnklistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(bnklistAdapter);

    }

    /////////////////////
    public void setDate() {
        onCreateDialog().show();
        Toast.makeText(getActivity(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }


    protected Dialog onCreateDialog() {
        // TODO Auto-generated method stub
        return new DatePickerDialog(getActivity(),
                myDateListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        datePicker_txv.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private class UpdatePayment extends AsyncTask<String, Void, String> {
        Context context;

        public UpdatePayment(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            Util.showPgDialog(progressDialog);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();


            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));

            map.put("banklist", bankName);
            map.put("date", datePicker_txv.getText().toString());
            map.put("amount", amt_edt.getText().toString());
            map.put("acname", acc_name_edt.getText().toString());
            map.put("acno", acc_num_edt.getText().toString());
            map.put("brefno", ref_num_edt.getText().toString());
            map.put("message", msg_edt.getText().toString());

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.UPDATE_PAYMENT,"GET",map);

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

                        amt_edt.setText("");
                        acc_name_edt.setText("");
                        acc_num_edt.setText("");
                        ref_num_edt.setText("");
                        msg_edt.setText("");



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



    private class GetBankList extends AsyncTask<String, Void, String> {
        Context context;

        public GetBankList(Context context) {
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
            String result =jsonParser.makeHttpRequest(Api.BANK_LIST,"GET",map);

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

                        BankData =new ArrayList<>();
                       // Util.errorDialog(context,jsonObject.optString("msg"));

                        JSONArray jsonArray=jsonObject.getJSONArray("item");

                        for (int i=0;i<jsonArray.length();i++){
                            HashMap<String ,String > map=new HashMap<>();

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            map.put("bank_id",jsonObject1.optString("bank_id"));
                            map.put("bank_name",jsonObject1.optString("bank_name"));
                            BankData.add(map);

                            bnkList.add(jsonObject1.optString("bank_name"));

                        }

                        ArrayAdapter<String> bnklistAdapter = new ArrayAdapter<String>(context,R.layout.simple_spinner_item, bnkList);
                        bnklistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bnkname_spn.setAdapter(bnklistAdapter);

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
