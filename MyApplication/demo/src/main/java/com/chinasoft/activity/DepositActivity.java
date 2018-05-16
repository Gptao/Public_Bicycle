package com.chinasoft.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.demo.R.drawable.button;
import static com.example.demo.R.drawable.button_seleceted;

@ContentView(R.layout.activity_deposit)
public class DepositActivity extends Activity {

    @ViewInject(R.id.other_deposite)
    private EditText other_deposite;
    @ViewInject(R.id.c100)
    private Button bt1;
    @ViewInject(R.id.c50)
    private Button bt2;
    @ViewInject(R.id.c20)
    private Button bt3;
    @ViewInject(R.id.c10)
    private Button bt4;
    private String num;
    private SharedPreferences sp;
    OkHttpClient okHttpClient=new OkHttpClient();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences.Editor editor=sp.edit();
            switch (msg.what){
                case 1:
                    float newbalance=(float)msg.obj;
                    editor.putFloat("balance",newbalance);
                    Toast.makeText(DepositActivity.this, "充值成功~", Toast.LENGTH_SHORT).show();
                    break;
            }
            editor.commit();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        other_deposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setBackground(getDrawable(button));
                bt2.setBackground(getDrawable(button));
                bt3.setBackground(getDrawable(button));
                bt4.setBackground(getDrawable(button));
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=String.valueOf(100);
                bt1.setBackground(getDrawable(button_seleceted));
                bt2.setBackground(getDrawable(button));
                bt3.setBackground(getDrawable(button));
                bt4.setBackground(getDrawable(button));
                other_deposite.setText("");
                Log.i("充值金额————————————",num.toString());
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=String.valueOf(50);
                bt2.setBackground(getDrawable(button_seleceted));
                bt1.setBackground(getDrawable(button));
                bt3.setBackground(getDrawable(button));
                bt4.setBackground(getDrawable(button));
                other_deposite.setText("");
                Log.i("充值金额————————————",num.toString());
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=String.valueOf(20);
                bt3.setBackground(getDrawable(button_seleceted));
                bt1.setBackground(getDrawable(button));
                bt2.setBackground(getDrawable(button));
                bt4.setBackground(getDrawable(button));
                other_deposite.setText("");
                Log.i("充值金额————————————",num.toString());
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=String.valueOf(10);
                bt4.setBackground(getDrawable(button_seleceted));
                bt1.setBackground(getDrawable(button));
                bt3.setBackground(getDrawable(button));
                bt2.setBackground(getDrawable(button));
                other_deposite.setText("");
                Log.i("充值金额————————————",num.toString());
            }
        });


    }
    @Event(value = {R.id.submit_deposit})
    private void doEvent(View view){
        sp=getSharedPreferences("msg",MODE_PRIVATE);
        String name = sp.getString("username","");
        String password = sp.getString("password","");
        if(!other_deposite.getText().toString().isEmpty())
        {
            num=other_deposite.getText().toString();
        }
            FormBody.Builder builder_deposit = new FormBody.Builder();
            FormBody formBody_deposit = builder_deposit.add("id", name)
                    .add("pwd", password).add("num", num).build();
            Request.Builder builder_deposit1 = new Request.Builder();
            Request request_add = builder_deposit1.url("http://192.168.40.7:8080/PublicBicycle/app!deposit").post(formBody_deposit).build();
            exec(request_add);

    }

    private void exec(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("异常---->","获取失败！！！！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("成功---->","充值成功！！！！");
                String s=response.body().string();
                JSONObject object=(JSONObject) JSON.parse(s);
                String flag=(String)object.get("flag");
                Message message=new Message();
                switch(flag){
                    case "fail":
                        break;
                    case "deposit_success":
                        BigDecimal b=(BigDecimal) object.get("balance");
                        float balance=b.floatValue();
                        balance-=0.001f;
                        message.what=1;
                        message.obj=balance;
                        handler.sendMessage(message);
                        break;
                }
            }
        });
    }
}
