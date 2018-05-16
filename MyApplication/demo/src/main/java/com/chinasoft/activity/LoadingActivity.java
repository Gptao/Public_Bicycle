package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.demo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Blue on 2017/7/30.
 */

public class LoadingActivity extends Activity{
    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //计时器
        timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(LoadingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        };
        timer.schedule(task,5000);

        //子线程
//        new Thread(){
//            public void run(){
//                super.run();
//                try{
//                    Thread.sleep(5000);
//                    Intent intent=new Intent(LoadingActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                }catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    }
    public void step(View view){
        timer.cancel();
        Intent intent=new Intent(LoadingActivity.this,LoginActivity.class);
        startActivity(intent);

    }
}
