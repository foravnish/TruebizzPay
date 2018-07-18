package app.application.recharge.titoriya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.application.recharge.titoriya.Utils.Api;
import app.application.recharge.titoriya.Utils.Util;

public class ForgetPassword extends AppCompatActivity {
    EditText uid_edt, eid_edt;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_forget_password);
        inIt();
    }

    private void inIt() {
        eid_edt = (EditText) findViewById(R.id.eid_edt);
        uid_edt = (EditText) findViewById(R.id.uid_edt);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(ForgetPassword.this, "Please Enter User Id");
                } else if (eid_edt.getText().toString().isEmpty()) {
                    Util.errorDialog(ForgetPassword.this, "Please Enter Email Id");
                } else if (Util.isValidEmail(eid_edt.getText().toString()) == false) {
                    Util.errorDialog(ForgetPassword.this, "Please Enter Valid Email Id");
                } else {
                    startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                }
            }
        });
    }

    public void forgetPasswordAsync(final String uname, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.ForgetPwd_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String msg = obj.getString("msg");


                            if (status.equals("1")) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgetPassword.this, "Please check InterNet Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("username", uname);
                map.put("password", password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
       /* Log.e("lavkush",""+stringRequest);*/
    }
}
