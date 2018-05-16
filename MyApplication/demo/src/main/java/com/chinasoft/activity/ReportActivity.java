package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.demo.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.report)
public class ReportActivity extends Activity {

    @ViewInject(R.id.error1)
    private CheckBox error1;
    @ViewInject(R.id.error2)
    private CheckBox error2;
    @ViewInject(R.id.error3)
    private CheckBox error3;
    @ViewInject(R.id.error4)
    private CheckBox error4;
    @ViewInject(R.id.error5)
    private CheckBox error5;
    @ViewInject(R.id.error6)
    private CheckBox error6;
    @ViewInject(R.id.error7)
    private CheckBox error7;
    @ViewInject(R.id.error8)
    private CheckBox error8;
    @ViewInject(R.id.other)
    private EditText other;
    private int errorcode=0;
    private SharedPreferences sp;
    private String bicyid;
    OkHttpClient okHttpClient=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        sp=getSharedPreferences("msg",MODE_PRIVATE);
        Intent intent=getIntent();
        bicyid=intent.getStringExtra("bicyid");

    }

    @Event(value = {R.id.submit_error})
    private void doEvent(View view) {
        String name = sp.getString("username", "");
        String password = sp.getString("password", "");
        switch (view.getId()) {
            case R.id.submit_error:
                if (error1.isChecked())
                    errorcode += 1;
                if (error2.isChecked())
                    errorcode += 10;
                if (error3.isChecked())
                    errorcode += 100;
                if (error4.isChecked())
                    errorcode += 1000;
                if (error5.isChecked())
                    errorcode += 10000;
                if (error6.isChecked())
                    errorcode += 100000;
                if (error7.isChecked())
                    errorcode += 1000000;
                if (error8.isChecked())
                    errorcode += 10000000;
                if (!other.getText().toString().isEmpty()) {
                    errorcode += 100000000;
                }
                if (errorcode != 0) {
                    FormBody.Builder builder_report = new FormBody.Builder();
                    FormBody formBody_report = builder_report.add("id", name)
                            .add("pwd", password).add("bicyid", bicyid).add("errorcode", Integer.toString(errorcode)).build();
                    Request.Builder builder_report1 = new Request.Builder();
                    Request request_report = builder_report1.url("http://192.168.40.7:8080/PublicBicycle/app!report").post(formBody_report).build();
                    exec(request_report);
                    Intent returnintent = new Intent(ReportActivity.this, MenuActivity.class);
                    returnintent.putExtra("flag","report");
                    startActivity(returnintent);

                }
        }
    }
    private void exec(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("异常---->","获取失败！！！！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });

    }
    }

