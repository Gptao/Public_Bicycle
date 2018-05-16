package com.chinasoft.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chinasoft.templates.ResideMenu.ResideMenu;
import com.chinasoft.templates.ResideMenu.ResideMenuItem;
import com.example.demo.R;




public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        setUpMenu();
        Intent intent=getIntent();
        String toast_flag=intent.getStringExtra("flag");
        if(!(toast_flag==null)) {
            switch (toast_flag) {
                case "report":
                    Toast.makeText(MenuActivity.this, "报修成功，感谢您的配合~", Toast.LENGTH_SHORT).show();
                    break;
                case "update":
                    Toast.makeText(MenuActivity.this, "密码修改成功~", Toast.LENGTH_SHORT).show();
                    break;
                case "end_trip":
                    Toast.makeText(MenuActivity.this, "支付成功，欢迎再次骑行~", Toast.LENGTH_SHORT).show();
                    break;
                case "login":
                    Toast.makeText(MenuActivity.this, "登陆成功~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "车辆解锁");
        itemCalendar  = new ResideMenuItem(this,R.drawable.icon_calendar,  "个人信息");
        itemProfile = new ResideMenuItem(this,  R.drawable.icon_profile, "活动中心");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "设置");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
            changeFragment(new ProfileFragment());
//            Intent intent = new Intent(this, ActActivity.class);
//            startActivity(intent);

        }else if (view == itemCalendar){
            changeFragment(new CalendarFragment());
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
          //  Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
          //  Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
}
