package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.demo.R;

/**
 * Created by Blue on 2017/7/28.
 */

public class RegistActivity extends Activity {
    private EditText regist_username;
    private EditText regist_password;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//获取注册页面的用户名和密码输入控件

        setContentView(R.layout.regist);
        intent=getIntent();
        regist_username=(EditText) findViewById(R.id.regist_username);
        regist_password=(EditText) findViewById(R.id.regist_password);
    }

    public void regist_regist(View view){
        //在intent中写入注册信息

        String username=regist_username.getText().toString();
        String password=regist_password.getText().toString();
        intent.putExtra("regist_name",username);
        intent.putExtra("regist_password",password);
        setResult(2,intent);
        finish();


    }
}
