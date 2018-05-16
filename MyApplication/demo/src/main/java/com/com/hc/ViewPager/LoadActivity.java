package com.com.hc.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.example.demo.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Timer timer = new Timer();
        timer.schedule(new Task(),3000);
    }
    public class Task extends TimerTask {

        @Override
        public void run() {
            startActivity(new Intent(LoadActivity.this,GuideActivity.class));
        }
    }
}
