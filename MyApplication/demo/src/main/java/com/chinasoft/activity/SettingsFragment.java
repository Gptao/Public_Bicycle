package com.chinasoft.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:26
 * Mail: specialcyci@gmail.com
 */
public class SettingsFragment extends Fragment {

    private FragmentTransaction ft;
    private View parentView;
    private ListView listView;
    private double cache=10.9;
    private Button log_out;
    private SharedPreferences sp;

    Map<String,Object> map;
    List<Map<String,Object>> lists;
    SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.settings, container, false);
        listView   = (ListView) parentView.findViewById(R.id.listView);
        log_out   = (Button) parentView.findViewById(R.id.btn_logout);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("flag","log_out");
                startActivity(intent);
            }
        });
        initView();
        return parentView;
    }

    private void initView(){
        lists=new ArrayList<>();
        int[] imgId = {R.drawable.pwd,R.drawable.cache,R.drawable.we};
        String[] titles={"修改密码","清理缓存","关于我们"};
        String[]exps={"Modify pwd" , "Clean cache"+"                      "+cache+"mb", "About us"};
        String[]keys={"img","title","exp"};
        int []ids={R.id.item_img,R.id.item_title_settings,R.id.item_exp_settings};
        simpleAdapter=new SimpleAdapter(getActivity(),lists,R.layout.settings_item,keys,ids);
        for(int i=0;i< imgId.length;i++){
            map=new HashMap<>();
            map.put("img",imgId[i]);
            map.put("title",titles[i]);
            map.put("exp",exps[i]);
            lists.add(map);
        }

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
                if(i==0){
                    System.out.println("----> 修改密码");
                    Intent intent = new Intent(getActivity(), Update_Pwd_Activity.class);
                    startActivity(intent);
                }
                else if(i==1){
                    cache=0;
                    map = lists.get(1);
                    map.put("exp", "Clean cache"+"                      "+cache+"mb");
                    //只用这里改变了
                    simpleAdapter.notifyDataSetChanged();
                    System.out.println("cache"+cache+"mb");
                }
                else if(i==2){
                    System.out.println("----->关于我们");
                }
            }
        });
    }

    }
