package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.demo.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.lockcode)
public class LockcodeActivity extends Activity {

    @ViewInject(R.id.code)
    private TextView code;
    @ViewInject(R.id.timer_show)
    private Chronometer chronometer;
    MapView mMapView = null;
    private SharedPreferences sp;
    private String bicyid;
    OkHttpClient okHttpClient=new OkHttpClient();
    int miss=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String msgcode=(String)msg.obj;
                    Log.i("aaa","----->"+msgcode);
                    code.setText(msgcode);
                    break;
                case 2:
                    String[] s=new String[6];
                    s=(String[]) msg.obj;
                    Intent endintent = new Intent(LockcodeActivity.this, PayActivity.class);
                    endintent.putExtra("diff",s[0]);
                    endintent.putExtra("cost",s[1]);
                    endintent.putExtra("starttime",s[2]);
                    endintent.putExtra("endtime",s[3]);
                    endintent.putExtra("bicyid",s[4]);
                    endintent.putExtra("oid",s[5]);
                    startActivity(endintent);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mMapView = (MapView) findViewById(R.id.map1);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        AMap amap=mMapView.getMap();
        amap.moveCamera(CameraUpdateFactory.zoomTo(17));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0.1f);//设置定位蓝点精度圈的边框宽度的方法。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        amap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        amap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
       // setContentView(R.layout.lockcode);
        //查询车牌号对应的密码并显示
        sp=getSharedPreferences("msg",MODE_PRIVATE);
        Intent intent=getIntent();
        bicyid=intent.getStringExtra("bicyid");
        String name = sp.getString("username","");
        String password = sp.getString("password","");
        FormBody.Builder builder_search = new FormBody.Builder();
        FormBody formBody_search = builder_search.add("id", name)
                    .add("pwd", password).add("bicyid", bicyid).build();
        Request.Builder builder_search1 = new Request.Builder();
        Request request_search = builder_search1.url("http://192.168.40.7:8080/PublicBicycle/app!unlock").post(formBody_search).build();
        exec(request_search);
        chronometer.start();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                miss++;
                chronometer.setText(FormatMiss(miss));
            }
        });
    }

    public static String FormatMiss(int miss){
        String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
        String  mm=(miss % 3600)/60>9?(miss % 3600)/60+"":"0"+(miss % 3600)/60;
        String ss=(miss % 3600) % 60>9?(miss % 3600) % 60+"":"0"+(miss % 3600) % 60;
        if(hh.equals("00"))
        {
            if(mm.equals("00"))
            {
                return ss;
            }else
            {
                return mm+":"+ss;
            }
        }
        else
        {
           return hh+":"+mm+":"+ss;
        }

    }

    //点击报修按钮时
    public void report(View view){
        Intent reportintent=new Intent(LockcodeActivity.this,ReportActivity.class);
        reportintent.putExtra("bicyid",bicyid);
        startActivity(reportintent);
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
                String flag=(String)object.get("flag");
                Message message=new Message();
                SharedPreferences.Editor editor=sp.edit();
                switch(flag){
                    case "unlock_success":
                        message.what=1;
                        message.obj=object.getString("password");
                        handler.sendMessage(message);
                        break;
                    //收到用户名密码错误,弹出提示并退出到登陆界面
                    case "wronguser_fail":
                        editor.putString("username", "");
                        editor.putString("password","");
                        editor.putFloat("balance",0);
                        editor.putBoolean("logined",false);
                        editor.commit();
                        Toast.makeText(LockcodeActivity.this,"登陆已失效！",Toast.LENGTH_SHORT).show();
                        Intent logoutintent = new Intent(LockcodeActivity.this, LoginActivity.class);
                        startActivity(logoutintent);
                        break;
                    //当找不到此车牌对应的车时
                    case "nobicyid_fail":
                        Toast.makeText(LockcodeActivity.this,"车牌号不存在！",Toast.LENGTH_SHORT).show();
                        break;
                    case "endtrip_success":
                        String[] info=new String[6];
                        info[0]=object.getString("diff");
                        info[1]=object.getString("cost");
                        info[2]=object.getString("start");
                        info[3]=object.getString("end");
                        info[4]=object.getString("bicyid");
                        info[5]=object.getString("oid");
                        message.what=2;
                        message.obj=info;
                        handler.sendMessage(message);
                        break;
                }


            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        chronometer.stop();
    }

    public void end_trip(View view) {
        String name = sp.getString("username","");
        String password = sp.getString("password","");
        FormBody.Builder builder_search = new FormBody.Builder();
        FormBody formBody_search = builder_search.add("id", name)
                .add("pwd", password).add("bicyid", bicyid).build();
        Request.Builder builder_search1 = new Request.Builder();
        Request request_search = builder_search1.url("http://192.168.40.7:8080/PublicBicycle/app!endtrip").post(formBody_search).build();
        exec(request_search);

    }
}