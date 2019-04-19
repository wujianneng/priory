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
    Observable<String> login(@FieldMap Map<String,Object> map);

    @GET("/api/staff/")
    Observable<String> getStaffInfo(@Query("user") String username);

    @GET("/api/goldprice/")
    Observable<String> getCurrentGoldPrice();

    @GET("/api/orders")
    Observable<String> getTodayOrders(@Query("location") String location,@Query("daycontrol") String dayontrol);

    @GET("/api/orderitems")
    Observable<List<OrderItemBean>> getOrderDetailItems(@Query("ordernumber") String ordernumber, @Query("rmaaction") String rmaaction);

    @GET("/api/invoices")
    Observable<String> getOrderDetailInvoice(@Query("ordernumber") String ordernumber);

    @GET("/api/transactions")
    Observable<List<TransactionBean>> getOrderDetailTransaction(@Query("invoicenumber") String invoicenumber);


    @GET("/api/members")
    Observable<String> getMembers(@Query("mobile") String mobile);

    @GET("/api/orders")
    Observable<String> getOrdersByOrdernumber(@Query("ordernumber") String ordernumber);

    @GET("/api/stocks")
    Observable<String> getStockListByLocation(@Query("location") String location);

    @GET("/api/stocks")
    Observable<String> getStockListByLocationAndProductCode(@Query("location") String location,@Query("productcode") String productcode);

    @GET("/api/stocks")
    Observable<String> getStockListByNameAndProductCode(@Query("name") String name,@Query("productcode") String productcode);
}
