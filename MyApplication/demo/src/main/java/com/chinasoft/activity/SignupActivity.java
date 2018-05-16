package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_signup)
public class SignupActivity extends Activity {

    @ViewInject(R.id.textView3)
    private EditText username;
    @ViewInject(R.id.pwdEt1)
    private EditText password1;
    @ViewInject(R.id.pwdEt2)
    private EditText password2;
    OkHttpClient okHttpClient=new OkHttpClient();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent registintent = new Intent(SignupActivity.this, LoginActivity.class);
                    registintent.putExtra("flag","signup");
                    startActivity(registintent);
                    break;
                case 2:
                    Toast.makeText(SignupActivity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SignupActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    @Event(value = {R.id.subBtn})
    private void doEvent(View view) {
        String pwd1=password1.getText().toString();
        String pwd2=password2.getText().toString();
        String name=username.getText().toString();
        if(pwd1.equals(pwd2)){
        FormBody.Builder builder_sign = new FormBody.Builder();
        FormBody formBody_sign = builder_sign.add("id", name)
                .add("pwd", pwd1).build();
        Request.Builder builder_sign1 = new Request.Builder();
        Request request_report = builder_sign1.url("http://192.168.40.7:8080/PublicBicycle/app!regist").post(formBody_sign).build();
        exec(request_report);
        }
        else
        {
            Toast.makeText(SignupActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
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
                String s=response.body().string();
                JSONObject object=(JSONObject) JSON.parse(s);
                String flag=object.getString("flag");
                Message message=new Message();
                switch (flag){
                    //注册成功
                    case "regist_success":
                        message.what=1;
                        handler.sendMessage(message);
                        break;
                    //用户名已存在
                    case "fail_exist":
                        message.what=2;
                        handler.sendMessage(message);
                        break;
                    //用户名为空
                    case "fail_null":
                        message.what=3;
                        handler.sendMessage(message);
                        break;
                }

            }
        });

    }
}
