package com.chinasoft.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Time: 下午1:31
 * Mail: specialcyci@gmail.com
 */

//个人中心页面
public class ProfileFragment extends Fragment {
    private ListView listView;
    Map<String,Object> map;
    List<Map<String,Object>> lists;
    SimpleAdapter simpleAdapter;
    private Button btn_previous;
    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.profile, container, false);
        listView   = (ListView) parentView.findViewById(R.id.listView);
        initView();
        return parentView;
    }

    private void initView(){
        lists=new ArrayList<>();
        int[] imgId = {R.drawable.activity1,R.drawable.activity2,R.drawable.activity3};
        String[]keys={"img"};
        int []ids={R.id.item_img};
        simpleAdapter=new SimpleAdapter(getActivity(),lists,R.layout.activity_act_item,keys,ids);
        for(int i=0;i< imgId.length;i++){
            map=new HashMap<>();
            map.put("img",imgId[i]);
            lists.add(map);
        }

        listView.setAdapter(simpleAdapter);


    }
}
