package com.hti.hiinternet.network

import com.hti.hiinternet.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

object RestClient {


    fun getOkhttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(
        getOKhttpClientinterceptor()
    ).hostnameVerifier(HostnameVerifier{ s: String, sslSession: SSLSession ->
        return@HostnameVerifier true
    })

        .build()


    fun getOKhttpClientinterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    fun getApiServie(): ApiService = getRetrofit().create(ApiService::class.java)
    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constant.BASE_URL)
        .client(getOkhttpClient())
        .build()


}