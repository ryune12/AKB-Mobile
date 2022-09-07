package com.calvindoaldisutanto.akbresto.API;

import com.calvindoaldisutanto.akbresto.API.Response.DetailResponse;
import com.calvindoaldisutanto.akbresto.API.Response.MenuResponse;
import com.calvindoaldisutanto.akbresto.API.Response.OrderResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("Menu")
    Call<MenuResponse> getMenu();

    @GET("Menu/{kategori}")
    Call<MenuResponse> getMenuByKategori(@Path("kategori") String kategori);

    @GET("Menushow/{id}")
    Call<MenuResponse> getMenuById(@Path("id") String id);

    @GET("Order/byReservasi/{id}")
    Call<OrderResponse> getOrder(@Path("id") String id);

    @POST("Order")
    @FormUrlEncoded
    Call<OrderResponse> createOrder(@Field("id_reservasi") String id);

    @POST("DetailOrderAPI/create")
    @FormUrlEncoded
    Call<OrderResponse> createDetail(@Field("id_order") String id_order,
                                     @Field("id_menu") String id_menu,
                                     @Field("kuantitas") String jumlah,
                                     @Field("subtotal") String subtotal);
    @GET("DetailOrder/get/{id}")
    Call<DetailResponse> getDetail(@Path("id") String id);

    @GET("EndPesanan/{id}")
    Call<OrderResponse> end(@Path("id") String id_reservasi);
}
