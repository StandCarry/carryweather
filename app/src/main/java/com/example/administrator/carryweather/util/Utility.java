package com.example.administrator.carryweather.util;

import android.text.TextUtils;
import android.util.Log;


import com.example.administrator.carryweather.db.City;
import com.example.administrator.carryweather.db.County;
import com.example.administrator.carryweather.db.Province;
import com.example.administrator.carryweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Utility {

    /**
     * 解析处理服务器返回的省级数据
     */

    public static boolean handleProvinceResponse(String response){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allProvince=new JSONArray(response);

                for (int i=0;i<allProvince.length();i++){

                    JSONObject provinceObject=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();

                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }



        }
        return false;
    }


    /**
     * 解析处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray allCities=new JSONArray(response);
                for (int i=0;i<allCities.length();i++){

                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityid){

        if (!TextUtils.isEmpty(response)){
           try {

              JSONArray allcounties=new JSONArray(response);
               for (int i=0;i<allcounties.length();i++){
                   JSONObject countObject=allcounties.getJSONObject(i);
                   County county=new County();
                   county.setCountyName(countObject.getString("name"));
                   county.setWeatherId(countObject.getString("weather_id"));
                   county.setCityId(cityid);
                   county.save();
               }
               return true;

           }catch (JSONException e){
               e.printStackTrace();
           }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){

        try {

            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();

            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
