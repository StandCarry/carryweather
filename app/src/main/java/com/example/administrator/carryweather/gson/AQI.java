package com.example.administrator.carryweather.gson;

/**
 * Created by Administrator on 2016/12/22.
 */

public class AQI {
    //@SerializedName注解方式来让JSON字段和Java字段之间建立映射关系
    public AQICity city;
    public class AQICity{

        public String aqi;

        public String pm25;
    }
}
