package com.chinasoft.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;

public class GridActivity extends Activity {

    private  int[] nameId={R.drawable.app1,R.drawable.app2,R.drawable.app3,R.drawable.app4,
            R.drawable.app5,R.drawable.app6,R.drawable.app7,R.drawable.app8,
            R.drawable.app9,R.drawable.app10,R.drawable.app11,R.drawable.app12};

    private  String[] iconName={"时钟","文件夹","主页","定位","记事本","短信","电话",
    "照片","指南针","搜索","天气","wifi"};
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        gridView=(GridView)findViewById(R.id.gridView);
        MyAdapter myAdapter=new MyAdapter(GridActivity.this);
        gridView.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter
    {
        private Context mContext;

        public MyAdapter(Context context)
        {
            mContext=context;
        }

        @Override
        public int getCount() {
            return nameId.length;
        }

        @Override
        public Object getItem(int position) {
            return nameId[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //动态加载布局文件
            LayoutInflater inflator=LayoutInflater.from(mContext);
            View view=inflator.inflate(R.layout.grid_item,null);
            ImageView img=(ImageView) view.findViewById(R.id.grid_img);
            TextView text=(TextView)view.findViewById(R.id.grid_text);
            img.setImageResource(nameId[position]);
            text.setText(iconName[position]);


                    return view;

//            LinearLayout linear=new LinearLayout(mContext);
//            linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT));
//            linear.setOrientation(LinearLayout.VERTICAL);
//            linear.setGravity(Gravity.CENTER_HORIZONTAL);
//
//
//            ImageView imageView=new ImageView(mContext);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setImageResource(nameId[position]);
//
//            linear.addView(imageView);
//
//            TextView textView=new TextView(mContext);
//            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//            textView.setGravity(Gravity.CENTER_HORIZONTAL);
//            textView.setText(iconName[position]);
//
//
//            linear.addView(textView);
//            return linear;
        }
    }
}
