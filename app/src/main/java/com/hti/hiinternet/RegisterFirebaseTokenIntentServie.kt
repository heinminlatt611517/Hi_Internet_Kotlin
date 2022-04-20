package com.hti.hiinternet

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestSaveFcm
import com.hti.hiinternet.network.ApiService
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFirebaseTokenIntentServie : IntentService("registertoken") {
    override fun onHandleIntent(p0: Intent?) {
        if (p0?.hasExtra(Constant.FCM_TOKEN)!!) {


            p0.getStringExtra(Constant.FCM_TOKEN).let {


                val request = RequestSaveFcm(it)

                RestClient.getApiServie().RegisterFcmToken(request, PreFerenceRepo.token)
                    .enqueue(
                        object : Callback<NetworkResult<ResponseBody>> {
                            override fun onFailure(
                                call: Call<NetworkResult<ResponseBody>>,
                                t: Throwable
                            ) {

                                Log.d("firebasetokenStatus", t.localizedMessage)

                            }

                            override fun onResponse(
                                call: Call<NetworkResult<ResponseBody>>,
                                response: Response<NetworkResult<ResponseBody>>
                            ) {
                                PreFerenceRepo.tokensaveState = response.body()?.statusMessage
                                Log.d("Firebase Success","SUCCESS")


                            }

                        }
                    )

            }
        }


    }


}