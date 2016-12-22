package com.example.administrator.carryweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfot;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("cw")
    public CarWash carWash;
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("sport")
    public Sport sport;
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
