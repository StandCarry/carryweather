package com.example.administrator.carryweather.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.carryweather.R;
import com.example.administrator.carryweather.db.City;
import com.example.administrator.carryweather.db.County;
import com.example.administrator.carryweather.db.Province;
import com.example.administrator.carryweather.util.HttpUtil;
import com.example.administrator.carryweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE=0;

    public static final int LEVEL_CITY=1;

    public static final int LEVEL_COINTY=2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> datalist=new ArrayList<>();

    /**
    * 省列表
    */
    private  List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selecteProvince;

    /**
     * 选中的城市
     */
    private City selecteCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selecteProvince=provinceList.get(position);
                          queryCities();                          //查询城市列表
                }
                else if (currentLevel==LEVEL_CITY){
                    selecteCity=cityList.get(position);
                             queryCounties();                       //查询县列表
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel==LEVEL_COINTY){
                             queryCities();                       //查询城市列表
                }
                else if(currentLevel==LEVEL_CITY) {
                           queryProvinces();                         //查询省级列表

                }

            }
        });

                   queryProvinces();                                 //查询省级列表
    }

    /**
     *查询全国所有的省，优先从数据库查询，如果没有就去服务器上查询
     */
    private void queryProvinces(){

        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            datalist.clear();
            for (Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }
        else {
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");                                    //从服务器上查询
        }
    }
    /**
     *查询省内所有的城市，优先从数据库查询，如果没有就去服务器上查询
     */
    private void queryCities(){

        titleText.setText(selecteProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",String.valueOf(selecteProvince.getId())).find(City.class);
        if (cityList.size()>0){
            datalist.clear();
            for (City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            int provinceCode=selecteProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }
    /**
     *查询选中市内的所有县，优先从数据库查询，如果没有就去服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selecteCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid= ?",String.valueOf(selecteCity.getId())).find(County.class);
        if (countyList.size()>0){
            datalist.clear();
            for (County county:countyList){
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COINTY;
        }
        else {
            int provinceCode=selecteProvince.getProvinceCode();
            int cityCode=selecteCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /**
     *根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address,final  String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequset(address, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().string();

                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }
                else if ("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selecteProvince.getId());
                }
                else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selecteCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }
                            else if("city".equals(type)){
                                queryCities();
                            }
                            else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

    }

    /**
     *显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     *关闭进度对话框
     */

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}