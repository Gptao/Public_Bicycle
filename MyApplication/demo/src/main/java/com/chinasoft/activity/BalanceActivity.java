package com.chinasoft.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.demo.R;

public class BalanceActivity extends Activity {
private Button bt;
    private SharedPreferences sp;
    private TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        //当“余额说明按钮按下时”
        bt=(Button)findViewById(R.id.ban);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        //读取余额并更新
        balance=(TextView)findViewById(R.id.balance);
        sp=getSharedPreferences("msg",MODE_PRIVATE);
        balance.setText(String.valueOf(sp.getFloat("balance",0)));

    }
    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(BalanceActivity.this).inflate(R.layout.ban, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 400, true);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bk, null));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, 100, 500);
        popupWindow.showAsDropDown(view);
    }
}
