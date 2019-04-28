package com.pos.priory.networks;


import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.beans.TransactionBean;
import com.pos.priory.utils.Constants;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kaka on 2018/3/14.
 * email:375120706@qq.com
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("/api/rest-auth/login/")
    Observable<String> login(@FieldMap Map<String,Object> map);//登錄接口

    @FormUrlEncoded
    @POST("/api/rest-auth/password/change")
    Observable<String> resetPassword(@FieldMap Map<String,Object> map);//修改密碼接口

    @GET("/api/staff")
    Observable<String> getStaffInfo(@Query("user") String username);//獲取員工信息

    @GET("/api/goldprice")
    Observable<String> getCurrentGoldPrice();//獲取當前金價

    @GET("/api/orders")
    Observable<String> getTodayOrders();//獲取當天所有訂單

    @GET("/api/orderitems")
    Observable<List<OrderItemBean>> getOrderDetailItems(@Query("ordernumber") String ordernumber, @Query("rmaaction") String rmaaction);


    @GET("/api/members")
    Observable<String> getMembers(@Query("mobile") String mobile);//根據電話號碼查詢會員信息

    @FormUrlEncoded
    @POST("/api/members")
    Observable<String> registerMember(@FieldMap Map<String,Object> map);//注冊會員接口

    @GET("/api/orders")
    Observable<String> getOrdersByOrdernumber(@Query("ordernumber") String ordernumber);

    @GET("/api/orders")
    Observable<String> getOrdersByDate(@Query("date") String date);

    @GET("/api/dashboard")
    Observable<String> getDashboard();

    @GET("/api/stocks")
    Observable<String> getStockListByLocation(@Query("location__name") String location);

    @GET("/api/stocks")
    Observable<String> getStockListByLocationAndProductCode(@Query("location__name") String location,@Query("product__productcode") String productcode);

    @GET("/api/stocks")
    Observable<String> getStockListByNameAndProductCode(@Query("product__name") String name,@Query("product__productcode") String productcode);
}
