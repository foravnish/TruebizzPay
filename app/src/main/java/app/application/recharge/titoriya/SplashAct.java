package app.application.recharge.titoriya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import app.application.recharge.titoriya.Utils.MyPrefrences;

public class SplashAct extends AppCompatActivity {
    Thread timerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);
        startHandler();
    }
    private void startHandler() {

        timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {


                    if (MyPrefrences.getUserLogin(SplashAct.this) == true) {
                        Intent intent = new Intent(SplashAct.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashAct.this, LoginActivity.class);
                        startActivity(intent);
                    }


                }
            }
        };
        timerThread.start();
    }

}
