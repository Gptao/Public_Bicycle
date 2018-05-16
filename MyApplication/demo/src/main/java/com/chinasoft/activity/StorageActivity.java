package com.chinasoft.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.R;

import org.apache.commons.io.IOUtils;
import org.litepal.tablemanager.Connector;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@ContentView(R.layout.storage)
public class StorageActivity extends Activity {
    @ViewInject(R.id.storename)
    private EditText storename;
    @ViewInject(R.id.storepassword)
    private EditText storepassword;
    @ViewInject(R.id.ifremember)
    private CheckBox ifremember;
    private SharedPreferences sp;
    private boolean remember=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        ifremember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                remember=isChecked;
            }
        });

        sp=getSharedPreferences("msg",MODE_PRIVATE);

        String username=sp.getString("username","");
        storename.setText(username);
    }

    @Event(value = {R.id.storebutton,R.id.databasebutton})
    private void doEvent(View view){
        switch (view.getId()){
            case R.id.storebutton:
        try{
            FileOutputStream fos=openFileOutput("msg.json",MODE_PRIVATE);
            String pwd=storepassword.getText().toString();
            String name=storename.getText().toString();
            HashMap<String,Object> map=new HashMap<>();
            map.put("username",name);
            map.put("password",pwd);

            String data= JSON.toJSONString(map);
            IOUtils.write(data,fos,"utf-8");
            fos.close();
            FileInputStream fis=openFileInput("msg.json");
            List<String> list=IOUtils.readLines(fis,"utf-8");
            String result=list.get(0);

            JSONObject object=(JSONObject)JSON.parse(result);
            String uname=(String)object.get("username");
            Log.i("info","test fileio----->"+uname);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        break;
            case R.id.databasebutton:
                SQLiteDatabase db= Connector.getWritableDatabase();

//                //添加
//                Category c1=new Category("天气");
//                Category c2=new Category("推荐");
//                c1.save();
//                c2.save();
//                News news=new News("头条","天晴了","1000");
//                ArrayList<Category> cs=new ArrayList<>();
//                cs.add(c1);
//                cs.add(c2);
//                Introduction intro=new Introduction("震惊！半年阴雨后天竟然晴了！");
//                Comment com=new Comment("震惊！无良记者为增加点击量竟成了标题党！",new Date());
//                news.setCategories(cs);
//                intro.setNews(news);
//                com.setNews(news);
//                news.save();
//                intro.save();
//                com.save();

                //更新
//                String update="update introduction set content='震惊！半年阴雨后天竟然晴了！' " +
//                        "where content='这种新闻没有什么介绍'";
//                db.execSQL(update);
//                ContentValues cv=new ContentValues();
//                cv.put("content","这种新闻没有什么介绍");
//                db.update("introduction",cv,"content=?",new String[]{"震惊！半年阴雨后天竟然晴了！"});

//                //删除
//                db.delete("comment","content=?",new String[]{"震惊！无良记者为增加点击量竟成了标题党！"});

                //查询
                Cursor cursor=db.rawQuery("select * from news where title=?",new String[]{"头条"});
                while (cursor.moveToNext()){
                    Log.i("search result---->",cursor.getString(cursor.getColumnIndex("content")));
                }
                break;



        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("------>","onstop()");

        SharedPreferences.Editor editor=sp.edit();
        if(remember==true) {
            editor.putString("username", storename.getText().toString());
        }
        else
        {
            editor.putString("username", "");
        }
        editor.commit();
        Log.i("------>","存储成功！");
    }

}
