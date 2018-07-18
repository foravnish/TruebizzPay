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
import android.widget.ImageView;
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
import app.application.recharge.titoriya.connection.JSONParser;


/**
 * Created by user on 2/9/2017.
 */

public class ProfileFragment extends Fragment {
    TextView title;
    LinearLayout bck_ly;
    ImageView editData;
    TextView name_val,us_name_val,mob_val,email_val,address_val,city_val,pin_val,comp_val,balance_val;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment, container, false);

        name_val=(TextView)view.findViewById(R.id.name_val);
        us_name_val=(TextView)view.findViewById(R.id.us_name_val);
        mob_val=(TextView)view.findViewById(R.id.mob_val);
        email_val=(TextView)view.findViewById(R.id.email_val);
        address_val=(TextView)view.findViewById(R.id.address_val);
        city_val=(TextView)view.findViewById(R.id.city_val);
        pin_val=(TextView)view.findViewById(R.id.pin_val);
        comp_val=(TextView)view.findViewById(R.id.comp_val);
       // balance_val=(TextView)view.findViewById(R.id.balance_val);

        editData=(ImageView)view.findViewById(R.id.editData);

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new EditProfile();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("name",name_val.getText().toString());
                bundle.putString("usernme",us_name_val.getText().toString());
                bundle.putString("mobile",mob_val.getText().toString());
                bundle.putString("email",email_val.getText().toString());
                bundle.putString("address",address_val.getText().toString());
                bundle.putString("city",city_val.getText().toString());
                bundle.putString("pin",pin_val.getText().toString());
                bundle.putString("company",comp_val.getText().toString());
               // bundle.putString("balance",balance_val.getText().toString());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();

            }
        });

        new ProfileDetails(getActivity()).execute();

        inIt(view);
        return view;
    }

    private void inIt(View view) {
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Profile");
        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        bck_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
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
            }
        });

    }

    private class ProfileDetails extends AsyncTask<String, Void, String> {
        Context context;

        public ProfileDetails(Context context) {
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
            String result =jsonParser.makeHttpRequest(Api.PROFILE_SHOW,"GET",map);

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

                        JSONObject jsonObject1=jsonObject.getJSONObject("item");

                        name_val.setText(jsonObject1.optString("name"));
                        us_name_val.setText(jsonObject1.optString("user"));
                        mob_val.setText(jsonObject1.optString("mobile"));
                        email_val.setText(jsonObject1.optString("email"));
                        address_val.setText(jsonObject1.optString("address"));
                        city_val.setText(jsonObject1.optString("city"));
                        pin_val.setText(jsonObject1.optString("pin"));
                        comp_val.setText(jsonObject1.optString("cname"));

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
}
