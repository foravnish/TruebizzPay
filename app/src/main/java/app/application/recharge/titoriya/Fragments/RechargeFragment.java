package app.application.recharge.titoriya.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import app.application.recharge.titoriya.Fragments.RetailerFragment;
import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.asynctask.DtacardRechargeAsync;
import app.application.recharge.titoriya.asynctask.DthRechargeAsync;
import app.application.recharge.titoriya.asynctask.ElectircityValidationAsyncTask;
import app.application.recharge.titoriya.asynctask.GasValidationAsyncTask;
import app.application.recharge.titoriya.asynctask.InsuranceValidAsyncTask;
import app.application.recharge.titoriya.asynctask.OperatorListAsync;
import app.application.recharge.titoriya.asynctask.RechargeVerifiationAsyncTask;
import app.application.recharge.titoriya.connection.InternetStatus;

/**
 * Created by user on 2/7/2017.
 */

public class RechargeFragment extends Fragment implements View.OnClickListener {
    String title_val = "";
    TextView title, operator, datePicker_txv;
    ImageView back_btn;
    LinearLayout bck_ly;
    EditText mob_num_edt, amount_edt;
    //optional edit text
    EditText cycle_edt, pst_account_edt, prcs_unit_edt, bill_unit_edt;
    LinearLayout amt_lin, bil_unit_lin, prcs_unit_lin, cycle_lin, st_pick_lin, city_lin;
    Spinner city_spn;
    //////////////
    TextInputLayout mob_num_til;
    Button rech_btn;
    ArrayList<String> opt_code__list;
    Calendar calendar;
    int year, day, month;
    String recharge_type = "";
    String cityName = "";
    String operator_code = "";
    String selop = "";
    Dialog dialog2;
    static ArrayList<HashMap<String, String>> operatorArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title_val = getArguments().getString("title");
        operatorArrayList = new ArrayList<>();
        checkOperator(title_val);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rechargefragment, container, false);
        title = (TextView) view.findViewById(R.id.title);
        operator = (TextView) view.findViewById(R.id.operator);
        mob_num_edt = (EditText) view.findViewById(R.id.mob_num_edt);
        amount_edt = (EditText) view.findViewById(R.id.amount_edt);

        mob_num_til = (TextInputLayout) view.findViewById(R.id.mob_num_til);
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        back_btn = (ImageView) view.findViewById(R.id.back_btn);
        rech_btn = (Button) view.findViewById(R.id.rech_btn);
        rech_btn.setOnClickListener(this);
        bck_ly.setOnClickListener(this);
        operator.setOnClickListener(this);

        opt_code__list = new ArrayList<>();
        /////////////////////
        amt_lin = (LinearLayout) view.findViewById(R.id.amt_lin);
        bil_unit_lin = (LinearLayout) view.findViewById(R.id.bil_unit_lin);
        prcs_unit_lin = (LinearLayout) view.findViewById(R.id.prcs_unit_lin);
        cycle_lin = (LinearLayout) view.findViewById(R.id.cycle_lin);
        st_pick_lin = (LinearLayout) view.findViewById(R.id.st_pick_lin);
        city_lin = (LinearLayout) view.findViewById(R.id.city_lin);

        pst_account_edt = (EditText) view.findViewById(R.id.pst_account_edt);
        cycle_edt = (EditText) view.findViewById(R.id.cycle_edt);
        prcs_unit_edt = (EditText) view.findViewById(R.id.prcs_unit_edt);
        bill_unit_edt = (EditText) view.findViewById(R.id.bill_unit_edt);
        datePicker_txv = (TextView) view.findViewById(R.id.datePicker_txv);
        city_spn = (Spinner) view.findViewById(R.id.city_spn);

        city_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityName = city_spn.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        amt_lin.setVisibility(View.GONE);
        bil_unit_lin.setVisibility(View.GONE);
        prcs_unit_lin.setVisibility(View.GONE);
        cycle_lin.setVisibility(View.GONE);
        st_pick_lin.setVisibility(View.GONE);
        city_lin.setVisibility(View.GONE);

        //////////////////////////
        datePicker_txv.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("date ", "" + day + "/" + month + "/" + year);
        showDate(year, month + 1, day);

        if (title_val.equalsIgnoreCase("")) {
        } else if (title_val.equalsIgnoreCase("insurance")) {
            title.setText("Pay Insurance Premium");
            mob_num_til.setHint("Policy Number");
            operator.setHint("Select Insurer");
            st_pick_lin.setVisibility(View.VISIBLE);
            recharge_type = "";
        } else if (title_val.equalsIgnoreCase("gas")) {
            title.setText("Pay Gas Bill");
            mob_num_til.setHint("Service Number/Customer ID");
        } else if (title_val.equalsIgnoreCase("electricity")) {
            title.setText("Pay Electricity Bill");
            mob_num_til.setHint("CA/Service/Consumer/K  Number");
            operator.setHint("Select Electricity Board");
        } else if (title_val.equalsIgnoreCase("datacard")) {
            title.setText("Datacard Recharge");
            mob_num_til.setHint("Data Card Number");
        } else if (title_val.equalsIgnoreCase("postpaid")) {
            title.setText("Mobile Recharge");
            mob_num_til.setHint("Postpaid Number");
        } else if (title_val.equalsIgnoreCase("dth")) {
            title.setText("DTH Recharge");
            mob_num_til.setHint("Subscriber Id");
        } else if (title_val.equalsIgnoreCase("mobile")) {
            title.setText("Mobile Recharge");
            mob_num_til.setHint("Mobile Number");
        }

        return view;
    }

    private void showOperatorList(final Context context, final ArrayList<String> operator_list) {
        final Dialog dialog1 = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.setContentView(R.layout.selectoperaotdialog);
        TextView title1 = (TextView) dialog1.findViewById(R.id.title);
        if (title_val.equalsIgnoreCase("insurance")) {
            title1.setText("Select Insurer");
        }
        LinearLayout ly = (LinearLayout) dialog1.findViewById(R.id.ly);
        final ListView g_cmts_lv = (ListView) dialog1.findViewById(R.id.g_cmts_lv);
        if (operator_list.size() > 0) {
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(context, R.layout.operatorlistitem, operator_list);
            g_cmts_lv.setAdapter(itemsAdapter);
        } else {
            Toast.makeText(context, "No operator found", Toast.LENGTH_SHORT).show();
        }
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog1.dismiss();
            }
        });
        g_cmts_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selop=operator_list.get(i).toString();
                operator_code = operatorArrayList.get(i).get("operator_code");
                Log.e("operator_code", ": " + operator_code);
                operator.setText(selop);
                operator.setHint(selop);
                dialog1.dismiss();
                if (selop.contains("Landline") || selop.equalsIgnoreCase("Cellone Postpaid")) {
                    amt_lin.setVisibility(View.VISIBLE);
                    recharge_type = "post_paid_landline";
                } else if (selop.equalsIgnoreCase("MSEDC Limited ")) {
                    cycle_lin.setVisibility(View.GONE);
                    city_lin.setVisibility(View.GONE);

                    bil_unit_lin.setVisibility(View.VISIBLE);
                    prcs_unit_lin.setVisibility(View.VISIBLE);
                    recharge_type = "msedc";
                } else if (selop.equalsIgnoreCase("Reliance Energy")) {
                    bil_unit_lin.setVisibility(View.GONE);
                    prcs_unit_lin.setVisibility(View.GONE);
                    city_lin.setVisibility(View.GONE);
                    cycle_lin.setVisibility(View.VISIBLE);
                    recharge_type = "Reliance Energy";

                } else if (selop.equalsIgnoreCase("Torrent Power ")) {
                    cycle_lin.setVisibility(View.GONE);
                    bil_unit_lin.setVisibility(View.GONE);
                    prcs_unit_lin.setVisibility(View.GONE);
                    city_lin.setVisibility(View.VISIBLE);
                    recharge_type = "torrent";
                    setCityNameList(getActivity(), city_spn);

                } else {
                    recharge_type = "";
                    amt_lin.setVisibility(View.GONE);
                    cycle_lin.setVisibility(View.GONE);
                    bil_unit_lin.setVisibility(View.GONE);
                    prcs_unit_lin.setVisibility(View.GONE);
                    city_lin.setVisibility(View.GONE);

                }
            }
        });
      /*  if (InternetStatus.isConnectingToInternet(context)) {
            new GetCommentDetails(context, "facts", factList.get(FactDetailsActivity.currentpos).get("id").toString(),
                    total_cmmtLists.get(FactDetailsActivity.currentpos),g_cmts_lv).execute();
        } else {
            g_cmts_lv.setAdapter(new GossipCommentAdapter(context, total_cmmtLists.get(FactDetailsActivity.currentpos), true));
        }*/


        dialog1.setCancelable(false);
        dialog1.show();

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
            case R.id.operator:
                showOperatorList(getActivity(), getOperatorList());
                break;
            case R.id.rech_btn:

                if (checkEmptyValidation(recharge_type) == true && InternetStatus.isConnectingToInternet(getActivity())) {
                    //Toast.makeText(getActivity(),"rechrge",Toast.LENGTH_SHORT).show();
                    if (title_val.equalsIgnoreCase("dth") || title_val.equalsIgnoreCase("mobile") ||
                            title_val.equalsIgnoreCase("datacard") || title_val.equalsIgnoreCase("postpaid")) {

                        if (recharge_type.equalsIgnoreCase("post_paid_landline")) {
                            rechargeMobile();
                        }
                        else if (title_val.equalsIgnoreCase("dth")) {

                            dthRecharge();

                        }
                        else if (title_val.equalsIgnoreCase("datacard")) {

                            dataCardRecharge();
                        }
                        else {
                            rechargeMobile2();
                        }
                    } else if (title_val.equalsIgnoreCase("electricity")) {
                        // Toast.makeText(getActivity(),"Gas Bill",Toast.LENGTH_SHORT).show();
                        electicityRecharge();
                    } else if (title_val.equalsIgnoreCase("gas")) {
                        //Toast.makeText(getActivity(),"Gas Bill",Toast.LENGTH_SHORT).show();
                        gesRecharge();

                    } else if (title_val.equalsIgnoreCase("insurance")) {
                        insuranceRecharge();
                    }
                }

                break;
            case R.id.datePicker_txv:
                setDate();
                break;
        }
    }

    private void insuranceRecharge() {

        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);
        mob_num_txv.setText("Policy No: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new InsuranceValidAsyncTask(getActivity(), mob_num_edt.getText().toString(),
                        datePicker_txv.getText().toString(),
                        amount_edt.getText().toString(), operator_code,selop)
                        .execute();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }

    private void gesRecharge() {
        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);
        mob_num_txv.setText("Customer ID: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GasValidationAsyncTask(getActivity(), mob_num_edt.getText().toString(),
                        amount_edt.getText().toString(), operator_code,title_val)
                        .execute();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void electicityRecharge() {

        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);
        mob_num_txv.setText("Customer ID: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ElectircityValidationAsyncTask(getActivity(), mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        , operator_code, cityName, recharge_type, cycle_edt.getText().toString(), bill_unit_edt.getText().toString()
                        , prcs_unit_edt.getText().toString())
                        .execute();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }

    private void dataCardRecharge() {
        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);

        mob_num_txv.setText("Data Card No: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DtacardRechargeAsync(getActivity(), mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        , operator_code, title_val).execute();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void dthRecharge() {

        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);

        mob_num_txv.setText("Subscriber Id: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DthRechargeAsync(getActivity(), mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        , operator_code, title_val).execute();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }

    private void rechargeMobile2() {
        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);

        mob_num_txv.setText("Mobile No: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RechargeVerifiationAsyncTask(getActivity(), mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        ,operator_code, pst_account_edt.getText().toString(),title_val, false).execute();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void rechargeMobile() {

        Button subbmit,cancel_btn;
        TextView mob_num_txv,operator_txv,amt_txv;
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.payment_confirm_dialog);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subbmit=(Button)dialog2.findViewById(R.id.subbmit);
        cancel_btn=(Button)dialog2.findViewById(R.id.cancel_btn);
        mob_num_txv=(TextView) dialog2.findViewById(R.id.mob_num_txv);
        operator_txv=(TextView) dialog2.findViewById(R.id.operator_txv);
        amt_txv=(TextView) dialog2.findViewById(R.id.amt_txv);

        mob_num_txv.setText("Mobile No: "+mob_num_edt.getText().toString());
        operator_txv.setText("Operator: "+operator.getText().toString());
        amt_txv.setText("Amount: "+amount_edt.getText().toString());

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RechargeVerifiationAsyncTask(getActivity(), mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        , operator_code, pst_account_edt.getText().toString(),title_val, true).execute();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }

    private ArrayList<String> getOperatorList() {
        ArrayList<String> opertList = new ArrayList<>();
        Log.e("operatorArrayList", ": " + operatorArrayList.size());

        if (operatorArrayList.size() > 0) {
            for (int i = 0; i < operatorArrayList.size(); i++) {
                if (operatorArrayList.get(i).get("operator_name").contains("[ Surcharge ]")) {

                    opertList.add(operatorArrayList.get(i).get("operator_name").toString().replaceAll("\\[ Surcharge \\]", ""));
                } else {
                    opertList.add(operatorArrayList.get(i).get("operator_name").toString());
                }
                Log.e("oper name", operatorArrayList.get(i).get("operator_name").toString());
            }
        }
        Log.e("opertList", ": " + opertList);
        return opertList;
    }

    private void checkOperator(String opt_Type) {
        ArrayList<String> opertList = new ArrayList<>();
//        opt_code__list.clear();
        if (opt_Type.equalsIgnoreCase("")) {
            opertList.add("No operator found");
        } else if (opt_Type.equalsIgnoreCase("insurance")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"7");
            new OperatorListAsync(getActivity(),"7").execute();
        } else if (opt_Type.equalsIgnoreCase("gas")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"6");
            new OperatorListAsync(getActivity(),"6").execute();
        } else if (opt_Type.equalsIgnoreCase("electricity")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"5");
            new OperatorListAsync(getActivity(),"5").execute();
        } else if (opt_Type.equalsIgnoreCase("datacard")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"4");
            new OperatorListAsync(getActivity(),"8").execute();
        } else if (opt_Type.equalsIgnoreCase("postpaid")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"2");
            new OperatorListAsync(getActivity(),"2").execute();

        } else if (opt_Type.equalsIgnoreCase("dth")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"3");
            new OperatorListAsync(getActivity(),"3").execute();

        } else if (opt_Type.equalsIgnoreCase("mobile")&& InternetStatus.isConnectingToInternet(getActivity())) {
            //getOperatorListFromApi(getActivity(),"1");
            new OperatorListAsync(getActivity(),"1").execute();
        }
    }


///////////////////////////recharge secondstep funcxtion


    private void rechargePayment(final String query_id) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", MyPrefrences.getUserId(getActivity()));
        params.put("pin", MyPrefrences.getUserPin(getActivity()));
        params.put("initiated_query_id", query_id);
        params.put("format", "json");

        JsonObjectRequest req = new JsonObjectRequest(Api.Second_Step_Recharge, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("rechargePayment Result", ": " + response);
                        Toast.makeText(getActivity(), "rechargePayment Result  " + response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // As of f605da3 the following should work
                        Log.w("rechargePayment Error", "!: " + volleyError);
                        Toast.makeText(getActivity(), "rechargePayment Error " + volleyError, Toast.LENGTH_LONG).show();
                    }
                });
          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", MyPrefrences.getUserId(getActivity()));
                params.put("pin", MyPrefrences.getUserPin(getActivity()));
                params.put("initiated_query_id", query_id);
                params.put("format", "json");

                return params;
            }
        };RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);*/
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);


    }


    //////////////get operator list from api
    private void getOperatorListFromApi(final Context context, final String op_type) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.Operator_List_Api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("response is", ": " + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                                JSONArray oprt_JsonArray = jsonObject.optJSONArray("item");
                                parseOperatorList(oprt_JsonArray);
                            } else {
                                Toast.makeText(getActivity(), "No operator available" + s, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.w("Error", "ErrorListener:" + volleyError);
                        Toast.makeText(getActivity(), "Recharge Validation Error " + volleyError, Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operator_type", op_type);
                //params.put("user_token", MyPrefrences.getToken(context));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    //////////////////////////////////////parse operator list
    public static void parseOperatorList(JSONArray jsonArray) {
        if (jsonArray != null) {
            operatorArrayList.clear();
            for (int j = 0; j < jsonArray.length(); j++) {
                HashMap<String, String> map = new HashMap<>();
                try {
                    map.put("operator_code", jsonArray.getJSONObject(j).getString("operator_code"));
                    map.put("operator_name", jsonArray.getJSONObject(j).getString("operator_name"));
                    operatorArrayList.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /////////////////////////////////////////////

    ///////////////////// selct dob for pay inurence  optn
    public void setDate() {
        onCreateDialog().show();
      /*  Toast.makeText(getActivity(), "ca",
                Toast.LENGTH_SHORT)
                .show();*/
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
        datePicker_txv.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    //////////////////check validation
    private boolean checkEmptyValidation(String type) {
        boolean IsEmpty = false;
        if (type.equalsIgnoreCase("")) {
            if (mob_num_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (amount_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Operator")) {
                Util.errorDialog(getActivity(), "Please Select Operator");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Electricity Board")) {
                Util.errorDialog(getActivity(), "Please Select Electricity Board");
            } else if (operator.getHint
                    ().toString().equalsIgnoreCase("Select Insurer")) {
                Util.errorDialog(getActivity(), "Please Select Insurer");
            } else {
                IsEmpty = true;
               /* verifyRechargeValidation(mob_num_edt.getText().toString(), amount_edt.getText().toString()
                        , "2", "123", false);*/
                // Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equalsIgnoreCase("post_paid_landline")) {
            if (mob_num_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (amount_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (pst_account_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Electricity Board")) {
                Util.errorDialog(getActivity(), "Please Select Electricity Board");
            } else if (operator.getHint
                    ().toString().equalsIgnoreCase("Select Insurer")) {
                Util.errorDialog(getActivity(), "Please Select Insurer");
            } else {
                IsEmpty = true;
                Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equalsIgnoreCase("msedc")) {
            if (mob_num_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (amount_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (bill_unit_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (prcs_unit_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Electricity Board")) {
                Util.errorDialog(getActivity(), "Please Select Electricity Board");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Insurer")) {
                Util.errorDialog(getActivity(), "Please Select Insurer");
            } else {
                IsEmpty = true;
                Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equalsIgnoreCase("Reliance Energy")) {
            if (mob_num_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (amount_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (cycle_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Electricity Board")) {
                Util.errorDialog(getActivity(), "Please Select Electricity Board");
            } else if (operator.getHint
                    ().toString().equalsIgnoreCase("Select Insurer")) {
                Util.errorDialog(getActivity(), "Please Select Insurer");
            } else {
                IsEmpty = true;
                Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equalsIgnoreCase("torrent")) {
            if (mob_num_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (amount_edt.getText().toString().isEmpty()) {
                Util.errorDialog(getActivity(), "Please enter value in above fields");
            } else if (cityName.isEmpty() || cityName.equalsIgnoreCase("Select City")) {
                Util.errorDialog(getActivity(), "Please Select City Name");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Electricity Board")) {
                Util.errorDialog(getActivity(), "Please Select Electricity Board");
            } else if (operator.getHint().toString().equalsIgnoreCase("Select Insurer")) {
                Util.errorDialog(getActivity(), "Please Select Insurer");
            } else {
                IsEmpty = true;
                Toast.makeText(getActivity(), "Working", Toast.LENGTH_SHORT).show();
            }
        }
        return IsEmpty;
    }

    ////////////////
    private void setCityNameList(Context context, Spinner spinner) {
        ArrayList<String> cityList = new ArrayList<>();
        cityList.add("Select City");
        cityList.add("Noida");
        cityList.add("Delhi");
        cityList.add("Pune");
        cityList.add("Mumbai");
        ArrayAdapter<String> bnklistAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                cityList);
        bnklistAdapter.setDropDownViewResource(R.layout.listtextview);
        spinner.setAdapter(bnklistAdapter);

    }
}
