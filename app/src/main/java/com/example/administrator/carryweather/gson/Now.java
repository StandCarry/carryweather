package com.example.administrator.carryweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Now {

    //@SerializedName注解方式来让JSON字段和Java字段之间建立映射关系
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;
    public class  More{
        @SerializedName("txt")
        public String info;
    }
}
