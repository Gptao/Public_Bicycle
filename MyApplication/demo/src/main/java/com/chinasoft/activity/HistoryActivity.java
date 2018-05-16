package com.chinasoft.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.demo.R;

import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends Activity {
    ListView lv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lv=(ListView) findViewById(R.id.orderlist);
        tv=(TextView) findViewById(R.id.rule);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });




        List<Map<String,Object>> lists=new ArrayList<>();
        String[]keys={"bicy_id","starttime","endtime","cost"};
        int []ids={R.id.list_bicy_id,R.id.list_starttime,R.id.list_endtime,R.id.list_cost};
        SimpleAdapter simpleAdapter=new SimpleAdapter(HistoryActivity.this,lists,R.layout.historylist_item,keys,ids);

        //读取数据库
        SQLiteDatabase db= Connector.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from orderrecord",null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()){
            cursor.getString(cursor.getColumnIndex("bicyid"));
            Map<String,Object> map=new HashMap<>();
//            Date date=new Date(cursor.getInt(cursor.getColumnIndex("starttime")));
//            String da=format.format(date);
            map.put("bicy_id",cursor.getString(cursor.getColumnIndex("bicyid")));
//            map.put("starttime",format.format(new Date(cursor.getInt(cursor.getColumnIndex("starttime")))));
//            map.put("endtime",format.format(new Date(cursor.getInt(cursor.getColumnIndex("endtime")))));
            map.put("starttime",cursor.getString(cursor.getColumnIndex("starttime")));
            map.put("endtime",cursor.getString(cursor.getColumnIndex("endtime")));
            map.put("cost",cursor.getString(cursor.getColumnIndex("cost")));
            lists.add(map);
        }
        lv.setAdapter(simpleAdapter);
    }
       private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.layout, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
       // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bk, null));
           popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, 100, 400);
        popupWindow.showAsDropDown(view);
    }
}
