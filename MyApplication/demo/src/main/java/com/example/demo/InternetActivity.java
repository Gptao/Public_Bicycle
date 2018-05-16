package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

@ContentView(R.layout.internet_test)
public class InternetActivity extends Activity {

    @ViewInject(R.id.respongse_text)
    private TextView text;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                String result=(String)msg.obj;
                text.setText(result);
            }
        }
    };
    OkHttpClient okHttpClient=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


    }

    @Event(value ={R.id.request_get,R.id.request_post})
    private  void doEvent(View view){
        switch(view.getId()){
            case R.id.request_get:

//                Request request=new Request.Builder()
//                        .url("http://www.baidu.com").get().build();
//
//                exec(request);
                String id = "1";
                String pwd = "1";
                Request request = new Request.Builder()
                        .url("http://192.168.40.7:8080/PublicBicycle/login?id="+id+"&pwd="+pwd)
                        .get()
                        .build();
                exec(request);

                break;
            case R.id.request_post:
                FormBody.Builder builder1=new FormBody.Builder();
                FormBody formBody=builder1.add("id","123")
                        .add("pwd","123").build();

                Request.Builder builder=new Request.Builder();
                Request request1=builder.url("http://192.168.40.7:8080/PublicBicycle/login").post(formBody).build();
                exec(request1);
                //http://192.168.40.7:8080/PublicBicycle/login.jsp
                break;
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
                Log.i("成功---->","获取成功！！！！");
                String s=response.body().string();
                Message message=new Message();
                message.what=1;
                message.obj=s;
                handler.sendMessage(message);

            }
        });
    }
}
