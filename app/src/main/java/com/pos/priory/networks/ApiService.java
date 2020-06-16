package com.pos.priory.networks;


import com.pos.priory.beans.DinghuoGoodBean;
import com.pos.priory.beans.GoldpriceBean;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.beans.InventoryTypeDetialBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.RepertoryFiltersBean;
import com.pos.priory.beans.RepertoryRecordBean;
import com.pos.priory.beans.RepertoryRecordFiltersBean;
import com.pos.priory.beans.ReturnFilterBean;
import com.pos.priory.beans.TranferStoresBean;
import com.pos.priory.beans.WarehouseBean;

import java.util.List;
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
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by kaka on 2018/3/14.
 * email:375120706@qq.com
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("/api/token")
    Observable<String> login(@FieldMap Map<String,Object> map);//登錄接口

    @FormUrlEncoded
    @POST("/api/token/refresh")
    Observable<String> refreshToken(@FieldMap Map<String,Object> map);//刷新token接口

    @FormUrlEncoded
    @POST("/api/rest-auth/password/change")
    Observable<String> resetPassword(@FieldMap Map<String, Object> map);//修改密碼接口

    @GET("/api/user/profile")
    Observable<String> getStaffInfo();//獲取員工信息

    @GET("/api/goldprice")
    Observable<GoldpriceBean> getCurrentGoldPrice();//獲取當前金價

    @GET("/api/data/report")
    Observable<String> getDayReport();//獲取日报表

    @GET("/api/order")
    Observable<String> getTodayOrders(@Query("page") int page);//獲取當天所有訂單

    @POST("/api/order/create")
    Observable<String> createOrder(@Body RequestBody requestBody);//新建訂單

    @DELETE("/api/order/detail/{id}")
    Observable<String> deleteOrder(@Path("id") int id);//删除一個訂單

    @POST("/api/order/return/create")
    Observable<String> returnGoods(@Body RequestBody requestBody);//新建回收單

    @GET("/api/member")
    Observable<MemberBean> getMembers(@Query("search") String mobile);//根據電話號碼查詢會員信息

    @FormUrlEncoded
    @POST("/api/member/create")
    Observable<String> registerMember(@FieldMap Map<String, Object> map);//注冊會員接口

    @FormUrlEncoded
    @PUT("/api/member/detail/{id}")
    Observable<String> editMember(@Path("id") int id,@FieldMap Map<String, Object> map);//修改會員信息

    @GET("/api/order")
    Observable<String> getOrdersByOrdernumber(@Query("search") String ordernumber);//通过订单号码查询订单

    @GET("/api/order/{id}")
    Observable<String> getOrderByOrderId(@Path("id") int id);//通过订单id查询订单

    @GET("/api/order")
    Observable<String> getOrdersByDate(@Query("date") String date);//通过日期查询订单

    @GET("/api/product")
    Observable<String> getFittings(@Query("accessory") boolean accessory);//獲取配件列表

    @GET("/api/product")
    Observable<String> getProductBySearch(@Query("search") String search);//搜索單個產品

    @GET("/api/data/dashboard")
    Observable<String> getDashboard();//获取首页看板日统计数据

    @GET("/api/data/turnover")
    Observable<String> getSaleAmountDatas(@Query("startdate") String startdate,
                                          @Query("enddate") String enddate);//获取数据營業額数据


    @GET("/api/warehouse/filter")
    Observable<RepertoryFiltersBean> getRepertoryFilters();

    @GET("/api/warehouse/record")
    Observable<RepertoryRecordBean> getRepertoryRecords(@Query("wh_id") int whname, @Query("whfrom_id") int whfrom_id, @Query("type") int type, @Query("purpose") int purpose,
                                                        @Query("startdate") String startdate, @Query("enddate") String enddate);

    @GET("/api/warehouse/record")
    Observable<RepertoryRecordBean> getRepertoryRecordsWithSearch(@Query("wh_id") int whname, @Query("whfrom_id") int whfrom_id, @Query("type") int type, @Query("purpose") int purpose,
                                                                  @Query("startdate") String startdate, @Query("enddate") String enddate, @Query("search") String search);

    @GET("/api/warehouse/record/filter")
    Observable<RepertoryRecordFiltersBean> getRepertoryRecordFilters();

    @FormUrlEncoded
    @POST("/api/warehouse/return")
    Observable<String> createReturnItem(@Field("warehouse_id") int warehouse_id, @Field("product_id") int product_id
            , @Field("returntype") String returntype, @Field("returndate") String returndate, @Field("returncost") String returncost
            , @Field("weight") String weight);

    @PUT("/api/warehouse/return/detail/{id}")
    Observable<String> editReturnItem(@Path("id") int id, @Body RequestBody requestBody);

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockLists(@Query("warehouse_id") int whname, @Query("category_id") int category, @Query("ordering") String ordering);//获取所有商品

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockListsReturn(@Query("warehouse_id") int whname, @Query("returntype") String returntype,
                                                  @Query("ordering") String ordering);//获取所有商品

    @GET("/api/warehouse/stocks/count")
    Observable<String> getStockSumDatas();//获取倉庫的統計數據

    @GET("/api/warehouse/returnstock")
    Observable<String> getReturnStockLists();//获取回收艙所有商品

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockListByParamReturn(@Query("search") String param, @Query("warehouse_id") int whname
            , @Query("returntype") String returntype, @Query("ordering") String ordering);

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockListByParam(@Query("search") String param, @Query("warehouse_id") int whname
            , @Query("category_id") int category, @Query("ordering") String ordering);

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockLists2(@Query("warehouse_id") int whname, @Query("category_id") int category);//获

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockListByParam2(@Query("warehouse_id") int whname, @Query("category_id") int category, @Query("search") String search);//获

    @GET("/api/warehouse")
    Observable<WarehouseBean> getStockListByParam(@Query("search") String param);

    @GET("/api/warehouse/purchase")
    Observable<DinghuoGoodBean> getDinghuoList();

    @GET("/api/warehouse/return/filter")
    Observable<ReturnFilterBean> getReturnFilters();

    @FormUrlEncoded
    @POST("/api/warehouse/purchase/done")
    Observable<String> submitDinghuoList(@Field("id") String purchaseid);//调货

    @FormUrlEncoded
    @PUT("/api/warehouse/purchase/item/{id}")
    Observable<String> editOneDinghuoItem(@Path("id") int id, @Field("quantity") int quantity, @Field("weight") String weight);//调货

    @DELETE("/api/warehouse/purchase/item/{id}")
    Observable<String> deleteOneDinghuoItem(@Path("id") int id);//调货

    @GET("/api/warehouse/detail/{id}")
    Observable<WarehouseBean> getStockItemById(@Path("id") int id);

    @GET("/api/warehouse/code")
    Observable<GoodBean> getStockByCode();//通过商品二维码/商品条码/商品名 查询商品

    @GET("/api/warehouse/transfer/shoplist")
    Observable<TranferStoresBean> getTranStores();//可调货店铺列表

    @FormUrlEncoded
    @POST("/api/warehouse/transfer")
    Observable<String> tranferGoods(@Field("id") int shopid, @Field("item") List<Integer> item);//调货

    @GET("/api/inventory")
    Observable<InventoryBean> getInventorys();//獲取所有大盤點清單

    @GET("/api/inventory/detail/{id}")
    Observable<InventoryTypeDetialBean> getInventoryTypeDetailById(@Path("id") int id);

    @GET("/api/inventory/detail/{id}")
    Observable<InventoryDetialBean> getBigInventoryDetailWithSearch(@Path("id") int id, @Query("category") int categoryid,
                                                                    @Query("counted") boolean counted, @Query("search") String search);

    @GET("/api/inventory/detail/{id}")
    Observable<InventoryDetialBean> getBigInventoryDetail(@Path("id") int id, @Query("category") int categoryid, @Query("counted") boolean counted);//獲取所有大盤點清單

    @FormUrlEncoded
    @POST("/api/inventory/action/itemcount")
    Observable<String> updateOnBigInventoryItemById(@Field("id") int id, @Field("code") String code);//提交单个大盘点盤點

    @FormUrlEncoded
    @POST("/api/inventory/action/finish")
    Observable<String> updateBigInventoryById(@Field("id") int inventoryid);//提交整个大盘点盤點


    @FormUrlEncoded
    @POST("/api/warehouse/purchase/item/create")
    Observable<String> createPurchasingitem(@Field("id") int productid);//生成订货单

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

    @FormUrlEncoded
    @POST("/api/coupon/action")
    Observable<String> getCoupons(@FieldMap Map<String,Object> map);//獲取優惠券列表或兌換優惠券

    @FormUrlEncoded
    @POST("/api/cashcoupon/action")
    Observable<String> getCashCoupons(@FieldMap Map<String,Object> map);//獲取現金券列表或兌換現金券


    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}
