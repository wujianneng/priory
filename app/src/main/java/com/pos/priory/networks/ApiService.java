package com.pos.priory.networks;


import com.pos.priory.beans.CashCouponParamsBean;
import com.pos.priory.beans.CashCouponResultBean;
import com.pos.priory.beans.CouponParamBean;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.CreateOrderParamsBean;
import com.pos.priory.beans.CreateOrderResultBean;
import com.pos.priory.beans.DinghuoGoodBean;
import com.pos.priory.beans.ExchangeCashCouponParamBean;
import com.pos.priory.beans.ExchangeCashCouponReslutBean;
import com.pos.priory.beans.ExchangeCouponParamBean;
import com.pos.priory.beans.ExchangeCouponReslutBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.GoldpriceBean;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.beans.InventoryTypeDetialBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.OrderCalculationParamBean;
import com.pos.priory.beans.OrderCalculationResultBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.beans.RepertoryFiltersBean;
import com.pos.priory.beans.RepertoryRecordBean;
import com.pos.priory.beans.RepertoryRecordFiltersBean;
import com.pos.priory.beans.ReturnFilterBean;
import com.pos.priory.beans.RollbackOrderParamBean;
import com.pos.priory.beans.TranferStoresBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.beans.WhitemDetailResultBean;

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
import retrofit2.http.PATCH;
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
    //Users
    @FormUrlEncoded
    @POST("/api/users/token")
    Observable<String> login(@FieldMap Map<String,Object> map);//登錄接口

    @FormUrlEncoded
    @POST("/api/users/token/refresh")
    Observable<String> refreshToken(@FieldMap Map<String,Object> map);//刷新token接口

    @FormUrlEncoded
    @POST("/api/users/password/change")
    Observable<String> resetPassword(@FieldMap Map<String, Object> map);//修改密碼接口

    @GET("/api/users/profile")
    Observable<String> getStaffInfo();//獲取員工信息

    //Systems
    @GET("/api/systems/goldprice")
    Observable<GoldpriceBean> getCurrentGoldPrice();//獲取當前金價

    @GET("/api/systems/version/list")
    Observable<String> getAppVersionCode();//獲取應用版本號

    @GET("/api/systems/stop/list")
    Observable<String> getAppStoreList();//獲取店鋪列表

    //Orders
    @GET("/api/orders/order_list/")
    Observable<String> getTodayOrders(@Query("page") int page);//獲取當天所有訂單

    @GET("/api/orders/order_list")
    Observable<String> getOrdersByOrdernumber(@Query("search") String ordernumber);//通过订单号码查询订单

    @GET("/api/orders/order/{id}")
    Observable<OrderDetailReslutBean> getOrderByOrderId(@Path("id") int id);//通过订单id查询订单

    @GET("/api/orders/order_list")
    Observable<String> getOrdersByDate(@Query("created_date__gte") String created__gte,@Query("created_date__lte") String created__lte);//通过日期查询订单

    @POST("/api/orders/order_calculation")
    Observable<OrderCalculationResultBean> orderCalculation(@Body OrderCalculationParamBean paramsBean);//計算訂單應付

    @POST("/api/orders/order_create")
    Observable<CreateOrderResultBean> createOrder(@Body CreateOrderParamsBean paramsBean);//新建訂單

    @FormUrlEncoded
    @POST("/api/orders/order_rollback")
    Observable<String> rollbackOrder(@FieldMap Map<String, Object> map);//撤回一個訂單

    @POST("/api/orders/order_recycle")
    Observable<String> returnGoods(@Body RequestBody requestBody);//新建回收單

    //Members
    @GET("/api/members/list")
    Observable<MemberBean> getMembers(@Query("search") String mobile);//根據電話號碼查詢會員信息

    @FormUrlEncoded
    @POST("/api/members/create")
    Observable<String> registerMember(@FieldMap Map<String, Object> map);//注冊會員接口

    @FormUrlEncoded
    @PUT("/api/members/detail/{id}")
    Observable<String> editMember(@Path("id") int id,@FieldMap Map<String, Object> map);//修改會員信息

    @GET("/api/members/reward")
    Observable<String> getRewardExList(@Query("member_id") int memberId);//獲取積分兌換列表

    //Product
    @GET("/api/products/list")
    Observable<String> getFittings(@Query("accessory") boolean accessory);//獲取配件列表

    @GET("/api/products/list")
    Observable<String> getProductBySearch(@Query("search") String search);//搜索單個產品


    //Warehouses
    @GET("/api/warehouses/filter")
    Observable<RepertoryFiltersBean> getRepertoryFilters();

    @GET("/api/warehouses/wahrecord_list/")
    Observable<RepertoryRecordBean> getRepertoryRecords(@Query("warehouse_type") String warehouse_type, @Query("whfrom_id") int whfrom_id, @Query("type") int type, @Query("purpose") int purpose,
                                                        @Query("startdate") String startdate, @Query("enddate") String enddate);

    @GET("/api/warehouses/wahrecord_list/")
    Observable<RepertoryRecordBean> getRepertoryRecordsWithSearch(@Query("warehouse_type") String warehouse_type, @Query("whfrom_id") int whfrom_id, @Query("type") int type, @Query("purpose") int purpose,
                                                                  @Query("startdate") String startdate, @Query("enddate") String enddate, @Query("search") String search);

    @GET("/api/warehouses/record/filter")
    Observable<RepertoryRecordFiltersBean> getRepertoryRecordFilters();

    @FormUrlEncoded
    @POST("/api/warehouses/return")
    Observable<String> createReturnItem(@Field("warehouse_id") int warehouse_id, @Field("returntype") String returntype,
                                        @Field("returndate") String returndate, @Field("returncost") String returncost
            , @Field("weight") String weight);

    @PATCH("/api/warehouses/return/detail/{id}")
    Observable<String> editReturnItem(@Path("id") int id, @Body RequestBody requestBody);

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockLists(@Query("warehouse_id") int whname, @Query("category_id") int category, @Query("ordering") String ordering);//获取所有商品

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockListsReturn(@Query("warehouse_id") int whname, @Query("returntype") String returntype,
                                                  @Query("ordering") String ordering);//获取所有商品

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockListByParamReturn(@Query("search") String param, @Query("warehouse_id") int whname
            , @Query("returntype") String returntype, @Query("ordering") String ordering);

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockListByParam(@Query("search") String param, @Query("warehouse_id") int whname
            , @Query("category_id") int category, @Query("ordering") String ordering);

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockLists2(@Query("warehouse_id") int whname, @Query("category_id") int category);//获

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockListByParam2(@Query("warehouse_id") int whname, @Query("category_id") int category, @Query("search") String search);//获

    @GET("/api/warehouses/list")
    Observable<WarehouseBean> getStockListByParam(@Query("search") String param);

    @GET("/api/warehouses/whitem_detail/{id}/")
    Observable<WhitemDetailResultBean> getStockDetail(@Path("id") int id);

    @GET("/api/warehouses/purchase")
    Observable<DinghuoGoodBean> getDinghuoList();

    @GET("/api/warehouses/return/filter")
    Observable<ReturnFilterBean> getReturnFilters();

    @FormUrlEncoded
    @POST("/api/warehouses/purchase/done")
    Observable<String> submitDinghuoList(@Field("id") String purchaseid);//提交訂貨清單

    @FormUrlEncoded
    @PUT("/api/warehouses/purchase/item/{id}")
    Observable<String> editOneDinghuoItem(@Path("id") int id, @Field("quantity") int quantity, @Field("weight") String weight);//调货

    @DELETE("/api/warehouses/purchase/item/{id}")
    Observable<String> deleteOneDinghuoItem(@Path("id") int id);//调货

    @GET("/api/warehouses/detail/{id}")
    Observable<WarehouseBean> getStockItemById(@Path("id") int id);

    @GET("/api/warehouses/code")
    Observable<FittingBean.ResultsBean> getStockByCode();//通过商品二维码/商品条码/商品名 查询商品

    @GET("/api/warehouses/transfer/shoplist")
    Observable<TranferStoresBean> getTranStores();//可调货店铺列表

    @FormUrlEncoded
    @POST("/api/warehouses/inventory_create")
    Observable<String> createNewInventry(@Field("shop") int shopid);//新建盤點

    @FormUrlEncoded
    @POST("/api/warehouses/transfer")
    Observable<String> tranferGoods(@Field("id") int shopid, @Field("item") List<Integer> item);//调货

    @GET("/api/warehouses/inventory")
    Observable<InventoryBean> getInventorys();//獲取所有大盤點清單

    @GET("/api/warehouses/inventory/detail/{id}")
    Observable<InventoryTypeDetialBean> getInventoryTypeDetailById(@Path("id") int id);

    @GET("/api/warehouses/inventory/detail/{id}")
    Observable<InventoryDetialBean> getBigInventoryDetailWithSearch(@Path("id") int id, @Query("category") int categoryid,
                                                                    @Query("counted") boolean counted, @Query("search") String search);

    @GET("/api/warehouses/inventory/detail/{id}")
    Observable<InventoryDetialBean> getBigInventoryDetail(@Path("id") int id, @Query("category") int categoryid, @Query("counted") boolean counted);//獲取所有大盤點清單

    @FormUrlEncoded
    @POST("/api/warehouses/inventory/action/itemcount")
    Observable<String> updateOnBigInventoryItemById(@Field("id") int id, @Field("code") String code);//提交单个大盘点盤點

    @FormUrlEncoded
    @POST("/api/warehouses/inventory/action/finish")
    Observable<String> updateBigInventoryById(@Field("id") int inventoryid);//提交整个大盘点盤點

    @FormUrlEncoded
    @POST("/api/warehouses/purchase/item/create")
    Observable<String> createPurchasingitem(@Field("id") int productid);//生成订货单

    //Payments
    @GET("/api/payments/method/list")
    Observable<String> getPayTypes();//獲取付款方式


    //Coupons
    @POST("/api/coupons/coupon_exchange")
    Observable<ExchangeCouponReslutBean> exchangeCoupon(@Body ExchangeCouponParamBean exchangeCouponParamBean);//兌換優惠券

    @POST("/api/coupons/usable_coupon/list")
    Observable<List<CouponResultBean>> getCoupons(@Body CouponParamBean cashCouponParamsBean);//獲取優惠券列表

    @POST("/api/coupons/cashcoupon_exchange")
    Observable<ExchangeCashCouponReslutBean> exchangeCashCoupons(@Body ExchangeCashCouponParamBean cashCouponParamsBean);//兌換現金券

    @POST("/api/coupons/usable_cashcoupon/list")
    Observable<List<CashCouponResultBean>> getCashCoupons(@Body CashCouponParamsBean cashCouponParamsBean);//獲取現金券列表


    //Datas

    @FormUrlEncoded
    @POST("/api/orders/dayendtable_create")
    Observable<String> getDayReport(@Field("shop") int shop, @Field("date") String date);////獲取日报表

    @GET("/api/orders/dayendtable_detail/{id}/")
    Observable<String> getDayReportDetail(@Path("id") int id);//获取日报表詳情

    @GET("/api/orders/order_turnover")
    Observable<String> getSaleAmountDatas(@Query("startdate") String startdate,
                                          @Query("enddate") String enddate);//获取数据營業額数据

    @GET("/api/products/backend/product_salesreport/")
    Observable<String> getSaleCountDatas(@Query("created_date__gte") String startdate,
                                          @Query("created_date__lte") String enddate
            ,@Query("shop_id") String shop_id,@Query("category_id") String category_id
            ,@Query("sales_quantity_order") String sales_quantity_order,@Query("sales_amount_order") String sales_amount_order
            ,@Query("sales_price_order") String sales_price_order);//获取数据銷售量数据

    @GET("/api/products/categoryselect_list/")
    Observable<String> getProductCategorys();//获取產品分類名稱

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}
