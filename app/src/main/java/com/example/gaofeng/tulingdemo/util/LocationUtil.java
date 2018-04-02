package com.example.gaofeng.tulingdemo.util;


import android.content.Context;
import android.location.LocationListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.greenrobot.eventbus.EventBus;

public class LocationUtil implements OnGetGeoCoderResultListener {
    /**
     * 反解码地址 类型
     */
    private static GeoCodeOption mGeoCodeOption;
    /**
     * 反解码地址工具
     */
    private static GeoCoder mGeoCoder;

    private static LocationUtil locationUtil;

    private static LatLng latLng;

    private static String address;

    public BDLocationListener myListener = new MyLocationListener();
    private static LocationClient mLocationClient;

    private LocationUtil() {
    }

    public static LocationUtil getInstance(Context context) {
        if (null == locationUtil) {
            locationUtil = new LocationUtil();
            mGeoCodeOption = new GeoCodeOption();
            mGeoCoder = GeoCoder.newInstance();
            mGeoCoder.setOnGetGeoCodeResultListener(locationUtil);
            initParams();
        }
        return locationUtil;
    }

    public static void quearyAddressLatLngInfo(String address) {
        if (mGeoCodeOption != null) {
            mGeoCodeOption.city(address).address(address);
            mGeoCoder.geocode(mGeoCodeOption);
        }
    }

    private static void initParams() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        //option.setPriority(LocationClientOption.NetWorkFirst);
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位
        mLocationClient.setLocOption(option);
    }

    public void startMointer() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
        mLocationClient.registerLocationListener(myListener);
    }

    public void stopMoniter() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        this.latLng = geoCodeResult.getLocation();
        this.address = geoCodeResult.getAddress();
        EventBus.getDefault().post("AddressGetSuccess");
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }


    public static LatLng getLatLng() {
        if (null == latLng) {
            latLng = new LatLng(0, 0);
        }
        return latLng;
    }

    public static String getAddress() {
        return address;
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            address = bdLocation.getAddrStr();
            latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            EventBus.getDefault().post("AddressGetSuccess");
            stopMoniter();
        }
    }


}
