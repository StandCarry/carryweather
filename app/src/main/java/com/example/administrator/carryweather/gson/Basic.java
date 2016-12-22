package com.example.administrator.carryweather.gson;

import android.text.style.UpdateLayout;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Basic {

    //@SerializedName注解方式来让JSON字段和Java字段之间建立映射关系

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updataTime;
    }
}
