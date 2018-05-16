package com.chinasoft.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.chinasoft.templates.ResideMenu.ResideMenu;
import com.chinasoft.zxing.activity.CaptureActivity;
import com.example.demo.R;


/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */

//自行车解锁页面
public class HomeFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private ImageView bicy_search;
    private TextView tv;
    MapView mMapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        setUpViews();
        mMapView = (MapView)  parentView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        AMap amap=mMapView.getMap();
        amap.moveCamera(CameraUpdateFactory.zoomTo(17));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0.1f);//设置定位蓝点精度圈的边框宽度的方法。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        amap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        amap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        return parentView;
    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        bicy_search = (ImageView) parentView.findViewById(R.id.bicy_search);
        if (bicy_search == null)
            Log.i("错误---", "bicy_search按钮是空");
        tv = (TextView) parentView.findViewById(R.id.text);
        bicy_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
                Intent intent=new Intent(getActivity(),CaptureActivity.class);
                startActivity(intent);
            }
        });
        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }
    private void showPopupWindow(View view) {
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.unlock, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bk, null));
        int windowPos[] = calculaePopwindowPos(view, contentView);
        int xoff = -300;
        windowPos[0] /=9;
        windowPos[0]-=100;
        int yoff = -550;
        windowPos[1] /=4;
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, 0,0 );
        popupWindow.showAsDropDown(view);

        Button btn_search=(Button)contentView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bicyid=(EditText)contentView.findViewById(R.id.bicy_id);
                String bicyid_s=bicyid.getText().toString();
                if(bicyid_s.isEmpty())
                {
                    Toast.makeText(getActivity(),"车牌号不能为空！",Toast.LENGTH_SHORT).show();
                }else
                {
                Intent codeintent=new Intent(getActivity(),LockcodeActivity.class);
                codeintent.putExtra("bicyid",bicyid_s);
                startActivity(codeintent);
            }
            }
        });

    }

    private int[]calculaePopwindowPos(final View anchorView,final View contentView){
        final int windowPos[]=new int[2];
        final int anchorLoc[]=new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight=anchorView.getHeight();
       // DisplayMetrics metric=new DisplayMetrics();
        DisplayMetrics metric = getResources().getDisplayMetrics();
       // getWindowManager().getDefaultDisplay().getMetrics(metric);
        final int screenHeight=metric.widthPixels;
        final int screenWidth=metric.heightPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        final int windowHeight= contentView.getMeasuredHeight();
        final int windowwidth=contentView.getMeasuredWidth();
        windowPos[0]=screenWidth-windowwidth;
        windowPos[1]=anchorLoc[1]+anchorHeight;
        return windowPos;
    }
}
