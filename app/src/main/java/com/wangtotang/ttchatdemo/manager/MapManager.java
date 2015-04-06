package com.wangtotang.ttchatdemo.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.ui.GameActivity;
import com.wangtotang.ttchatdemo.util.CollectionUtil;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Wangto Tang on 2015/4/5.
 */
public class MapManager {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context mContext;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    boolean isFirstLoc = true;// 是否首次定位
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    private BmobGeoPoint geoPoint = null;
    private BmobUserManager userManager;
    private ProgressDialog progress ;
    private double QUERY_KILOMETERS = 1;//默认查询1公里范围内的人
    private List<User> nears = null;

    public MapManager(MapView mapView,Context context){
        mMapView = mapView;
        mContext = context;
        userManager = BmobUserManager.getInstance(context);
    }

    /**
     * 初始化百度定位sdk
     * @Title: initBaiduLocClient
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void initBaiduLocClient() {
        mBaiduMap = mMapView.getMap();
        //设置缩放级别
        mBaiduMap.setMaxAndMinZoomLevel(18, 13);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(mContext);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置定位图层的配置（定位模式，是否允许方向信息）
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        initLocation();
        mLocationClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // Receive Location
            double latitude = location.getLatitude();
            double longtitude = location.getLongitude();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(latitude)
                    .longitude(longtitude).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(latitude,longtitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                geoPoint = new BmobGeoPoint(longtitude, latitude);
                updateUserLocation();
                queryNearPeople();
            }
        }

    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setOpenGps(true);// 打开gps
        int span= 5000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        mLocationClient.setLocOption(option);
    }

    public void cancel(){
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    /** 更新用户的经纬度信息
     * @Title: uploadLocation
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void updateUserLocation(){
        User u = (User) userManager.getCurrentUser(User.class);
        final User user = new User();
        user.setLocation(geoPoint);
        user.setObjectId(u.getObjectId());
        user.update(mContext,new UpdateListener() {
            @Override
            public void onSuccess() {
                BmobLog.i("location","更新地址成功");
            }
            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    public void display(LatLng point, BitmapDescriptor bitmap,String nick,String name) {
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true).title(name);
        //构建文字TextOption
        OverlayOptions textOption = new TextOptions()
                .typeface(Typeface.DEFAULT_BOLD)
                .fontSize(30)
                .fontColor(Color.BLACK)
                .text(nick+"")
                .position(point);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);
    }

    public void setMarker() {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
        if(nears!=null){
            for (User near : nears) {
                double latitude = near.getLocation().getLatitude();
                double longitude = near.getLocation().getLongitude();
                String nick = near.getNick();
                String name = near.getUsername();
                //定义Maker坐标点
                LatLng point = new LatLng(latitude,longitude);
                display(point,bitmap,nick,name);
            }
        }
        BaiduMap.OnMarkerClickListener markerListener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //创建InfoWindow展示的view
                Button button = new Button(mContext);
                button.setText("点我");
                button.setBackgroundResource(R.drawable.popup);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name = marker.getTitle();
                        userManager.queryUserByName(name,new FindListener<BmobChatUser>() {
                            @Override
                            public void onSuccess(List<BmobChatUser> bmobChatUsers) {
                                /*Intent intent =new Intent(mContext,SetMyInfoActivity.class);
                                intent.putExtra("from", "add");
                                intent.putExtra("username", name);
                                mContext.startActivity(intent);*/
                                Intent intent = new Intent(mContext,GameActivity.class);
                                intent.putExtra("from", "add");
                                intent.putExtra("username", name);
                                mContext.startActivity(intent);
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });

                    }
                });
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                final InfoWindow mInfoWindow = new InfoWindow(button, marker.getPosition(), -60);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        };
        mBaiduMap.setOnMarkerClickListener(markerListener);
    }

    public void queryNearPeople(){

        progress = new ProgressDialog(mContext);
        progress.setMessage("正在查询附近的人...");
        progress.setCanceledOnTouchOutside(true);
        progress.show();

        if(geoPoint!=null){
            double latitude = geoPoint.getLatitude();
            double longtitude = geoPoint.getLongitude();

            userManager.queryKiloMetersListByPage(false,0,"location",longtitude,latitude,false,QUERY_KILOMETERS,null,null, new FindListener<User>() {
                        @Override
                        public void onSuccess(List<User> users) {
                            if (CollectionUtil.isNotNull(users)) {
                                progress.setMessage("附近的人搜索完成!");
                                nears = users;
                                setMarker();
                            }else{
                                progress.setMessage("暂无附近的人!");
                            }
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    progress.dismiss();
                                }
                            }, 2000);

                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    }

            );
        }
    }

}
