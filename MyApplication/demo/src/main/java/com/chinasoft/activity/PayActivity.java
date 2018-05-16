package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinasoft.vo.Orderrecord;
import com.example.demo.R;

import org.litepal.tablemanager.Connector;

public class PayActivity extends Activity {
   private TextView tv;
    private Button bt;
    private String str_oid;
    private String str_cost;
    private String starttime;
    private String endtime;
    private String bicyid;
    private Button return_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        tv=(TextView)findViewById(R.id.cost);
        bt=(Button)findViewById(R.id.more);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        return_menu=(Button)findViewById(R.id.return_menu);
        return_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this,MenuActivity.class);
                intent.putExtra("flag","end_trip");
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
        str_cost=intent.getStringExtra("cost");
        str_oid=intent.getStringExtra("oid");
        starttime=intent.getStringExtra("starttime");
        endtime=intent.getStringExtra("endtime");
        bicyid=intent.getStringExtra("bicyid");
        tv.setText(str_cost);
        //存入数据库
        SQLiteDatabase db= Connector.getWritableDatabase();
        Orderrecord orderrecord=new Orderrecord();
        orderrecord.setCost(Integer.parseInt(str_cost));
        orderrecord.setStarttime(starttime);
        orderrecord.setEndtime(endtime);
        orderrecord.setBicyid(bicyid);
        orderrecord.save();
    }
    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(PayActivity.this).inflate(R.layout.more, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 400, true);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bk, null));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        TextView oid=(TextView)contentView.findViewById(R.id.oid);
        TextView tbicynum=(TextView)contentView.findViewById(R.id.bicynum);
        TextView tstarttime=(TextView)contentView.findViewById(R.id.starttime);
        TextView tendtime=(TextView)contentView.findViewById(R.id.endtime);
        oid.setText(str_oid);
        tbicynum.setText(bicyid);
        tstarttime.setText(starttime);
        tendtime.setText(endtime);
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, 100, 500);
        popupWindow.showAsDropDown(view);
    }
}
