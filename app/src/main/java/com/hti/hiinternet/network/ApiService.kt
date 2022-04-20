package com.hti.hiinternet.network

import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.request.*
import com.hti.hiinternet.data.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/user_login_test")
    fun login(@Body request: RequestLogin): Call<UserInfo>

    @POST("api/promo_slider_list")
    fun home(@Body request: RequestHome, @Header("token") token: String?): Call<NetworkResult<ResponseHomeData>>

    @POST("api/payment_list")
    fun payment(@Body request: RequestPayment, @Header("token") token: String?): Call<NetworkResult<ArrayList<ResponsePayment>>>

    @POST("api/save_firebase_token")
    fun RegisterFcmToken(@Body request: RequestSaveFcm, @Header("token") token: String?): Call<NetworkResult<ResponseBody>>

    @POST("api/serviceticket_list")
    fun mycomplainService(@Body request: RequestMycomplainService, @Header("token") token: String?): Call<NetworkResult<ArrayList<ResponseMycomplainService>>>

    @POST("api/myaccount")
    fun myaccount(@Body request: RequestAccount, @Header("token") token: String?): Call<NetworkResult<ResponseAccountData>>

    @POST("api/complain_category")
    fun complaincategory(@Body request: RequestComplainCategory, @Header("token") token: String?): Call<NetworkResult<ArrayList<Category>>>

    @POST("api/save_serviceticket")
    fun saveTicket(@Body request: RequestSaveServiceTicket, @Header("token") token: String?): Call<NetworkResult<ResponseBody>>

    @POST("api/save_facebook_id")
    fun facebookId(@Body request: RequestFbId): Call<UserInfo>

    @POST("api/check_service_createduser")
    fun checkCreateUser(@Body request : RequestCheckCreateUser,@Header("token") token: String?) : Call<ResponseCheckCreateUser>

}