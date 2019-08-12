package com.pos.priory.networks;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    @GET("/api/app/staff")
    Observable<String> getStaffInfo(@Query("user__username") String username);//獲取員工信息

    @GET("/api/data/goldprice")
    Observable<String> getCurrentGoldPrice();//獲取當前金價

    @GET("/api/data/report")
    Observable<String> getDayReport();//獲取日报表

    @GET("/api/orders")
    Observable<String> getTodayOrders(@Query("today") boolean isToday);//獲取當天所有訂單

    @POST("/api/orders/create")
    Observable<String> createOrder(@Body RequestBody requestBody);//新建訂單

    @DELETE("/api/orders/{id}")
    Observable<String> deleteOrder(@Path("id") int id);//删除一個訂單

    @POST("/api/orders/return/create")
    Observable<String> returnGoods(@Body RequestBody requestBody);//新建回收單

    @GET("/api/app/members")
    Observable<String> getMembers(@Query("search") String mobile);//根據電話號碼查詢會員信息

    @FormUrlEncoded
    @POST("/api/app/members")
    Observable<String> registerMember(@FieldMap Map<String,Object> map);//注冊會員接口

    @GET("/api/orders")
    Observable<String> getOrdersByOrdernumber(@Query("search") String ordernumber);//通过订单号码查询订单

    @GET("/api/orders/{id}")
    Observable<String> getOrderByOrderId(@Path("id") int id);//通过订单id查询订单

    @GET("/api/orders")
    Observable<String> getOrdersByDate(@Query("date") String date);//通过日期查询订单

    @GET("/api/data/dashboard")
    Observable<String> getDashboard();//获取首页看板日统计数据

    @GET("/api/data/saleslist")
    Observable<String> getDatas();//获取数据页数据

    @GET("/api/warehouse/stocks")
    Observable<String> getStockLists();//获取所有商品

    @GET("/api/warehouse/stocks/count")
    Observable<String> getStockSumDatas();//获取倉庫的統計數據

    @GET("/api/warehouse/returnstock")
    Observable<String> getReturnStockLists();//获取回收艙所有商品

    @GET("/api/warehouse/stocks")
    Observable<String> getStockListByParam(@Query("search") String param);//通过商品二维码/商品条码/商品名 查询商品

    @GET("/api/app/store/list")
    Observable<String> getTranStores();//可调货店铺列表

    @POST("/api/warehouse/stocks/transfer")
    Observable<String> tranferGoods(@Body RequestBody requestBody);//调货

    @PUT("/api/warehouse/stocks/{id}/return")
    Observable<String> returnStockById(@Path("id") int id);//退货

    @POST("/api/warehouse/inventory/create")
    Observable<String> createBigInventory();//新建大盤點

    @GET("/api/warehouse/inventory")
    Observable<String> getBigInventorys();//獲取所有大盤點清單

    @GET("/api/warehouse/inventory/detail")
    Observable<String> getBigInventoryById(@Query("id") int id,@Query("page") int page);//獲取所有大盤點清單

    @GET("/api/warehouse/inventory/daily")
    Observable<String> getDailyInventorys();//獲取所有日盤點數據

    @FormUrlEncoded
    @PUT("/api/warehouse/inventory/daily/{id}/update")
    Observable<String> updateDailyInventoryById(@Path("id") int id, @Field("quantity") int quantity);//提交日盤點

    @FormUrlEncoded
    @POST("/api/warehouse/inventory/item/update")
    Observable<String> updateOnBigInventoryItemById(@Field("inventoryid") int id,@Field("qrcode") String code);//提交单个大盘点盤點

    @FormUrlEncoded
    @PUT("/api/warehouse/inventory/{id}/update")
    Observable<String> updateBigInventoryById(@Path("id") int id,@Field("status") String status);//提交整个大盘点盤點

    @GET("/api/warehouse/purchasingitem")
    Observable<String> getPurchasingitems();//获取订货清单

    @FormUrlEncoded
    @POST("/api/warehouse/purchasingitem/create")
    Observable<String> createPurchasingitem(@FieldMap Map<String,Object> map);//生成订货单

    @FormUrlEncoded
    @PUT("/api/warehouse/purchasingitem/{id}/update")
    Observable<String> comfirmPurchasingitem(@Path("id") int id, @Field("status") boolean status);//确认订货

    @FormUrlEncoded
    @PUT("/api/warehouse/purchasingitem/{id}/update")
    Observable<String> editPurchasingitem(@Path("id") int id, @Field("quantity") int quantity);//修改订货数量

    @DELETE("/api/warehouse/purchasingitem/{id}/update")
    Observable<String> deletePurchasingitem(@Path("id") int id);//删除一条订货

    @GET("/api/app/version/list")
    Observable<String> getAppVersionCode();//獲取應用版本號

    @GET("/api/app/store/list")
    Observable<String> getAppStoreList();//獲取店鋪列表
}
