package com.chinasoft.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:26
 * Mail: specialcyci@gmail.com
 */
public class CalendarFragment extends Fragment {

    private View parentView;
    private ListView listView;
    private TextView show_username;
    private SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.calendar, container, false);
        listView   = (ListView) parentView.findViewById(R.id.listView);
        show_username=(TextView)parentView.findViewById(R.id.show_username);
        sp=this.getActivity().getSharedPreferences("msg",MODE_PRIVATE);
        show_username.setText(sp.getString("username",""));
        initView();
        return parentView;
    }

    private void initView(){
         List<Map<String,Object>> lists=new ArrayList<>();
         int[] imgId = {R.drawable.p1,R.drawable.p2,R.drawable.p3,
                R.drawable.p4};
         String[] titles={"我的行程","我的余额","账户充值","我的消息"};
         String[]exps={"History",
                "Balance",
                "Charge",
                "Message"};
            String[]keys={"img","title","exp"};
            int []ids={R.id.item_img,R.id.item_title,R.id.item_exp};
            SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),lists,R.layout.list_item,keys,ids);
            for(int i=0;i< imgId.length;i++){
                Map<String,Object> map=new HashMap<>();
                map.put("img",imgId[i]);
                map.put("title",titles[i]);
                map.put("exp",exps[i]);
                lists.add(map);
            }

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                    Intent in0 = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(in0);
                        break;
                    case 1:
                        Intent in1 = new Intent(getActivity(), BalanceActivity.class);
                        startActivity(in1);break;
                    case 2:
                        Intent in2 = new Intent(getActivity(), DepositActivity.class);
                        startActivity(in2);break;
                    case 3:
                        Intent in3 = new Intent(getActivity(), MessageActivity.class);
                        startActivity(in3);break;
                }
             //   Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
