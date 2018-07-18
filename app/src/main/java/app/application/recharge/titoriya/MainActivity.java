package app.application.recharge.titoriya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.application.recharge.titoriya.Fragments.RetailerFragment;
import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.connection.JSONParser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button home, logout;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        inIt();



//        Log.d("fdfsdfsdgdgd",MyPrefrences.getToken(getApplicationContext()));
//        Log.d("fdfsdfsdgdgd",MyPrefrences.getUserId(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getApplicationContext().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;

//        Toast.makeText(getApplicationContext(), version+"", Toast.LENGTH_SHORT).show();
         new CheckVersion(MainActivity.this,version).execute();


    }

    private void inIt() {
        logout = (Button) findViewById(R.id.logout);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        logout.setOnClickListener(this);
      /*  if (InternetStatus.isConnectingToInternet(MainActivity.this)) {
            new WalletBalanceAsyncTask(MainActivity.this).execute();
        }*/
//        if (MyPrefrences.getUserType(MainActivity.this).equalsIgnoreCase(Util.Distributor)) {
//            Fragment fragment = new DistributorFragment();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("trip_details", "ride rejected");
//            fragment.setArguments(bundle);
//            fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
//            fragmentTransaction.commit();
//        } else {
            Fragment fragment = new RetailerFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("trip_details", "ride rejected");
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
            fragmentTransaction.commit();
//        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("no", null).show();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                MyPrefrences.resetPrefrences(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.home:

//                if (MyPrefrences.getUserType(MainActivity.this).equalsIgnoreCase(Util.Distributor)) {
//                    Fragment fragment = new DistributorFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("trip_details", "ride rejected");
//                    fragment.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
//                    fragmentTransaction.commit();
//                } else {
//                    Fragment fragment = new RetailerFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("trip_details", "ride rejected");
//                    fragment.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
//                    fragmentTransaction.commit();
//                }

                Fragment fragment = new RetailerFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();

                break;
        }
    }



    private class CheckVersion extends AsyncTask<String, Void, String> {
        Context context;
        String version;

        public CheckVersion(Context contex,String version) {
            this.version=version;
            this.context=contex;

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();

            map.put("user_token", MyPrefrences.getToken(context));
            map.put("agent_id", MyPrefrences.getUserId(context));

            JSONParser jsonParser=new JSONParser();
            String result =jsonParser.makeHttpRequest(Api.CHECK_VERSION,"GET",map);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", ": " + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    if (!jsonObject.optString("version").equalsIgnoreCase(version)) {

//                        Util.errorDialog(context, jsonObject.optString("msg"));

                        final Dialog dialog2;
                        Button yes_action,no_action;
                        dialog2 = new Dialog(MainActivity.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.version_verify_dialog);
                        dialog2.setCancelable(false);

                        yes_action=(Button)dialog2.findViewById(R.id.yes_action);
                        no_action=(Button)dialog2.findViewById(R.id.no_action);

                        yes_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://play.google.com/store/apps/details?id=app.application.recharge.titoriya";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);

                                dialog2.dismiss();
                            }
                        });
                        no_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        dialog2.show();

                    }

//                    else if (jsonObject.optString("msg").equalsIgnoreCase("Agent Id not exists!!")){
//                        Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, "You have logout!", Toast.LENGTH_SHORT).show();
//                        MyPrefrences.resetPrefrences(context);
//                        context.startActivity(new Intent(context, LoginActivity.class));
//                        ((Activity)context).finish();
//                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
