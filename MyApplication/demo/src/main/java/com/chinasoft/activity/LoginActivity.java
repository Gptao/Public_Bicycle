package com.chinasoft.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinasoft.vo.Orderrecord;
import com.example.demo.R;
import com.shiran.qqlogindemo.QQlogin;

import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity implements OnClickListener {

    private TextView sign_up;
    private ImageView quit;
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    OkHttpClient okHttpClient=new OkHttpClient();
    private SharedPreferences sp;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mName.setVisibility(View.INVISIBLE);
                    mPsw.setVisibility(View.INVISIBLE);
                    inputAnimator(mInputLayout, mWidth, mHeight);
                    break;
            }

        }
    };

    public void buttonLogin1(View v){
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
       Intent intent = new Intent(LoginActivity.this, QQlogin.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        sp=getSharedPreferences("msg",MODE_PRIVATE);
        Intent intent=getIntent();
        String toast_flag=intent.getStringExtra("flag");
        if(!(toast_flag==null)) {
            switch (toast_flag) {
                case "signup":
                    Toast.makeText(LoginActivity.this, "注册成功~", Toast.LENGTH_SHORT).show();
                    break;
                case "login":
                    Toast.makeText(LoginActivity.this, "欢迎使用共享单车~", Toast.LENGTH_SHORT).show();
                    break;
                case "log_out":
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("logined",false);
                    Toast.makeText(LoginActivity.this, "注销成功~", Toast.LENGTH_SHORT).show();
                    editor.commit();
                    break;
            }
        }

        initView();


        quit = (ImageView) findViewById(R.id.quit_pgm);
        sign_up = (TextView) findViewById(R.id.sign_up);


        //toast显示


        quit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        sign_up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();

        EditText edit_name=(EditText)findViewById(R.id.edit_name);
        EditText edit_password=(EditText)findViewById(R.id.edit_password);
        String name = edit_name.getText().toString();
        String password = edit_password.getText().toString();
        FormBody.Builder builder1=new FormBody.Builder();
        FormBody formBody=builder1.add("id",name)
                .add("pwd",password).build();

        Request.Builder builder=new Request.Builder();
        Request request1=builder.url("http://192.168.40.7:8080/PublicBicycle/app!login").post(formBody).build();
        exec(request1);



    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();

        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                mInputLayout.setVisibility(View.INVISIBLE);
                progressAnimator(progress);
                Toast.makeText(LoginActivity.this, "正在验证中！", Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                timer.schedule(new Task(),3000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
    public class Task extends TimerTask{

        @Override
        public void run() {

            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.putExtra("flag","login");
            startActivity(intent);
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
                JSONObject object=(JSONObject) JSON.parse(s);
                Message message=new Message();
                if(object.get("flag").equals("login_fail"))
                {
                    message.what=1;
                    handler.sendMessage(message);
                }
                else
                {
                    Log.i("成功---->","登陆成功！！！！");
                    //从JSON中读取信息
                    String name=(String)object.get("username");
                    String password=(String)object.get("password");
                    BigDecimal b=(BigDecimal) object.get("balance");
                    float balance=b.floatValue();
                    balance-=0.001f;
                    List<JSONObject> orderlist=(List<JSONObject>) object.get("orders");
                    //用户信息存入preference
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("username", name);
                    editor.putString("password",password);
                    editor.putFloat("balance",balance);
                    editor.putBoolean("logined",true);
                    editor.commit();
                    //行车记录存入数据库
                    SQLiteDatabase db= Connector.getWritableDatabase();
                    for(int i=0;i<orderlist.size();i++)
                    {
                        Orderrecord orderrecord=new Orderrecord();
                        orderrecord.setCost((Integer) orderlist.get(i).get("cost"));
                        orderrecord.setStarttime( orderlist.get(i).getString("starttime"));
                        orderrecord.setEndtime(orderlist.get(i).getString("endtime"));
                        orderrecord.setBicyid(orderlist.get(i).getString("bicynum"));
                        orderrecord.save();
                    }

                    message.what=2;
                    handler.sendMessage(message);

//                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//                    intent.putExtra("response",s);
//                    startActivity(intent);
                }

            }
        });
    }
}
