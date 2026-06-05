package com.wtorder.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // 에뮬레이터에서 로컬 서버 접속: 10.0.2.2
    // 실제 기기에서는 서버 PC의 IP 주소로 변경
    private const val BASE_URL = "http://172.20.10.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val menuApi: MenuApi = retrofit.create(MenuApi::class.java)
    val orderApi: OrderApi = retrofit.create(OrderApi::class.java)
    val tableApi: TableApi = retrofit.create(TableApi::class.java)
    val storeApi: StoreApi = retrofit.create(StoreApi::class.java)
    val staffCallApi: StaffCallApi = retrofit.create(StaffCallApi::class.java)
}
