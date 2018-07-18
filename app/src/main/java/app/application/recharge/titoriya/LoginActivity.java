package app.application.recharge.titoriya;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.MyPrefrences;
import app.application.recharge.titoriya.Utils.Util;
import app.application.recharge.titoriya.asynctask.LoginAsync;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button s_btnLogin;
    EditText email_id, pwd;
    TextView signup, forgt_pwd,registration;
    RadioGroup user_Type_rg;
    //RadioButton selected_rd, dist_rd, ret_rd;

    Dialog dialog2;
    Button Yes_action,Yes_action2;
    TextView heading;
     EditText otp,mobile;
     LinearLayout linerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_login2);

        //verfiy();
        inIt();
    }

    private void verfiy() {
        ////////////////////// OTP CODE BEGIN
//
        if (MyPrefrences.getLogin_OTP(getApplicationContext()) == true) {

            Toast.makeText(this, "new", Toast.LENGTH_SHORT).show();

            final Dialog dialog2;
            Button Yes_action,Yes_action2;
            TextView heading;
            final EditText otp,mobile;
            final LinearLayout linerlayout;
            dialog2 = new Dialog(LoginActivity.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.otp_dialog);
            dialog2.setCancelable(false);

            Yes_action=(Button)dialog2.findViewById(R.id.Yes_action);
            Yes_action2=(Button)dialog2.findViewById(R.id.Yes_action2);
            otp=(EditText)dialog2.findViewById(R.id.otp);
            mobile=(EditText)dialog2.findViewById(R.id.mobile);
            linerlayout=(LinearLayout) dialog2.findViewById(R.id.linerlayout);

            Yes_action2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mobile.getText().toString().isEmpty() && mobile.getText().toString().length()==10){
                        Util.errorDialog(LoginActivity.this,"Please enter valid mobile no");
                    }
                    else{
                        linerlayout.setVisibility(View.VISIBLE);

                    }
                }
            });
            Yes_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (otp.getText().toString().isEmpty()){
                        Util.errorDialog(LoginActivity.this,"Please enter otp");
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "working", Toast.LENGTH_SHORT).show();
                        MyPrefrences.setLoginOTP(getApplicationContext(), false);
                        dialog2.dismiss();
                    }
                }
            });
            dialog2.show();

        }
        else  if (MyPrefrences.getLogin_OTP(getApplicationContext()) == false) {
            Toast.makeText(this, "old", Toast.LENGTH_SHORT).show();


            dialog2 = new Dialog(LoginActivity.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.otp_dialog);
//            dialog2.setCancelable(false);

            Yes_action=(Button)dialog2.findViewById(R.id.Yes_action);
            Yes_action2=(Button)dialog2.findViewById(R.id.Yes_action2);
            otp=(EditText)dialog2.findViewById(R.id.otp);
            mobile=(EditText)dialog2.findViewById(R.id.mobile);
            linerlayout=(LinearLayout) dialog2.findViewById(R.id.linerlayout);

            Yes_action2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mobile.getText().toString().equalsIgnoreCase("")){
                        Util.errorDialog(LoginActivity.this,"Please enter mobile no");
                    }
                    else if ( mobile.getText().toString().length() < 10){
                        Util.errorDialog(LoginActivity.this,"Please enter valid mobile no");
                    }
                    else{
                        linerlayout.setVisibility(View.VISIBLE);

                    }
                }
            });
            Yes_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (otp.getText().toString().isEmpty()){
                        Util.errorDialog(LoginActivity.this,"Please enter otp");
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "working", Toast.LENGTH_SHORT).show();
                        MyPrefrences.setLoginOTP(getApplicationContext(), false);
                        dialog2.dismiss();
                    }
                }
            });
            dialog2.show();


        }
        /////////////////////////OTP CODE END

    }

    private void inIt() {
        s_btnLogin = (Button) findViewById(R.id.s_btnLogin);
        signup = (TextView) findViewById(R.id.signup);
        forgt_pwd = (TextView) findViewById(R.id.forgt_pwd);
        registration = (TextView) findViewById(R.id.registration);
        pwd = (EditText) findViewById(R.id.pwd);
        email_id = (EditText) findViewById(R.id.email_id);
        user_Type_rg = (RadioGroup) findViewById(R.id.user_Type_rg);
        //dist_rd = (RadioButton) findViewById(R.id.dist_rd);
        //ret_rd = (RadioButton) findViewById(R.id.ret_rd);

//        if (MyPrefrences.getUserType(LoginActivity.this).equalsIgnoreCase(Util.Retailer)) {
//            user_Type_rg.check(dist_rd.getId());
//        } else if (MyPrefrences.getUserType(LoginActivity.this).equalsIgnoreCase(Util.Retailer)) {
//            user_Type_rg.check(ret_rd.getId());
//        } else {
//            user_Type_rg.clearCheck();
//        }
        signup.setOnClickListener(this);
        s_btnLogin.setOnClickListener(this);
        forgt_pwd.setOnClickListener(this);
        registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.signup:
                //   startActivity(new Intent(LoginActivity.this, SignupActivtiy.class));
                break;
            case R.id.forgt_pwd:
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
                break;
            case R.id.s_btnLogin:
                if (email_id.getText().toString().isEmpty()) {
                    Util.errorDialog(LoginActivity.this, "Please Enter User Id");
                } else if (pwd.getText().toString().isEmpty()) {
                    Util.errorDialog(LoginActivity.this, "Please Enter Password");
                }
//                else if (user_Type_rg.getCheckedRadioButtonId() == -1) {
//
//                    Util.errorDialog(LoginActivity.this, "Please Select User");
//                }
                else {


                    new LoginAsync(LoginActivity.this,email_id.getText().toString(),pwd.getText().toString()).execute();


//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);

                   /* MyPrefrences.setUserLogin(LoginActivity.this, true);
                    SplashAct.raiseEnterPinDialog(LoginActivity.this);*/
                }
                break;
            case R.id.registration:
                startActivity(new Intent(LoginActivity.this, Registration.class));
                break;
        }
    }

    private void verifyLogIn(final String name, final String pwd) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.BASE_URL_RECHARGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("response", ": " + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.w("GCMRegIntentService", "sendRegistrationTokenToServer! ErrorListener:" );
                        Toast.makeText(getApplicationContext(), "Login Error " + volleyError, Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("username", name);
                params.put("password", pwd);



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
