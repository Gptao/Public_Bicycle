package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActActivity extends Activity {

    private ListView listView;
    Map<String,Object> map;
    List<Map<String,Object>> lists;
    SimpleAdapter simpleAdapter;
    private Button btn_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);
        listView = (ListView) findViewById(R.id.listView);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        initView();
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initView(){
        lists=new ArrayList<>();
        int[] imgId = {R.drawable.activity1,R.drawable.activity2,R.drawable.activity3};
        String[]keys={"img"};
        int []ids={R.id.item_img};
        simpleAdapter=new SimpleAdapter(this,lists,R.layout.activity_act_item,keys,ids);
        for(int i=0;i< imgId.length;i++){
            map=new HashMap<>();
            map.put("img",imgId[i]);
            lists.add(map);
        }

        listView.setAdapter(simpleAdapter);


    }
}
