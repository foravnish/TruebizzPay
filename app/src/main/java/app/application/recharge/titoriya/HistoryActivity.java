package app.application.recharge.titoriya;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import app.application.recharge.titoriya.adapter.HistoryAdapter;

public class HistoryActivity extends AppCompatActivity {
    ViewPager viewpager;
    TextView title;
    LinearLayout bck_ly;
    String hist_type = "";
    private ArrayList<HashMap<String, String>> histItms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        hist_type = getIntent().getStringExtra("hist_type");
        title = (TextView) findViewById(R.id.title);
        title.setText(hist_type);
        bck_ly = (LinearLayout) findViewById(R.id.bck_ly);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        title.setText(hist_type);
        histItms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, String> map = new HashMap<>();
            int id = 1 + i;
            map.put("id", "" + id);
            histItms.add(map);
        }
        Log.e("hist items",": "+histItms);
        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, histItms,hist_type);
        viewpager.setAdapter(historyAdapter);
        viewpager.setCurrentItem(1);
    }
}
