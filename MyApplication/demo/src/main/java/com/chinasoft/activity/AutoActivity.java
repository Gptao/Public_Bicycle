package com.chinasoft.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demo.R;

public class AutoActivity extends AppCompatActivity {

    private AutoCompleteTextView text;
    private String autoctext;
    private Spinner autospin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);

        //自动补全的下拉框
        text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        final String datas[]={"love","like","long","low","light"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AutoActivity.this,android.R.layout.simple_list_item_1,datas);
        text.setAdapter(arrayAdapter);
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoctext=datas[position];
            }
        });
        Toast.makeText(AutoActivity.this,"搜索内容是"+autoctext,Toast.LENGTH_SHORT).show();

        //下拉列表
        autospin=(Spinner)findViewById( R.id.autospin);
        final String spindatas[]={"天津","北京","上海","西安","南京"};
        ArrayAdapter<String> spinadapter=new ArrayAdapter<String>(AutoActivity.this,android.R.layout.simple_list_item_1,spindatas);
        autospin.setAdapter(spinadapter);


    }
}
