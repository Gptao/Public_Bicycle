package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

@ContentView(R.layout.activity_update_pwd)
public class Update_Pwd_Activity extends Activity {

    @ViewInject(R.id.oldpwd)
    private EditText oldpwd;
    @ViewInject(R.id.cpwdEt1)
    private EditText password1;
    @ViewInject(R.id.cpwdEt2)
    private EditText password2;
    OkHttpClient okHttpClient=new OkHttpClient();
    private SharedPreferences sp;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences.Editor editor=sp.edit();
            switch (msg.what){
                case 1:
                    editor.putString("password",(String) msg.obj);
                    Toast.makeText(Update_Pwd_Activity.this,"修改密码成功！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Update_Pwd_Activity.this, MenuActivity.class);
                    intent.putExtra("flag","update");
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(Update_Pwd_Activity.this,"旧密码不对！",Toast.LENGTH_SHORT).show();
                    break;

            }
            editor.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        sp=getSharedPreferences("msg",MODE_PRIVATE);
    }

    @Event(value = {R.id.subcBtn})
    private void doEvent(View view) {
        String pwd1=password1.getText().toString();
        String pwd2=password2.getText().toString();
        String name=sp.getString("username","");
        if(pwd1.equals(pwd2)){
            FormBody.Builder builder_sign = new FormBody.Builder();
            FormBody formBody_sign = builder_sign.add("id", name)
                    .add("pwd_old", oldpwd.getText().toString()).add("pwd_new", pwd1).build();
            Request.Builder builder_sign1 = new Request.Builder();
            Request request_report = builder_sign1.url("http://192.168.40.7:8080/PublicBicycle/app!modifypwd").post(formBody_sign).build();
            exec(request_report);
        }
        else
        {
            Toast.makeText(Update_Pwd_Activity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
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
                    case "modify_success":
                        message.what=1;
                        message.obj=password1.getText().toString();
                        handler.sendMessage(message);
                        break;
                    case "modify_fail":
                        message.what=2;
                        handler.sendMessage(message);
                        break;
                }

            }
        });

    }
}
