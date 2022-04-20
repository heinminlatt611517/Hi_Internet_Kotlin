package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.request.RequestFbId
import com.hti.hiinternet.data.response.UserInfo
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FbViewModel : BaseViewModel() {

    internal val fbLiveData: MutableLiveData<UserInfo>? = MutableLiveData<UserInfo>()

    internal val status: MutableLiveData<Status>? = MutableLiveData()

    fun loadfbId(request: RequestFbId) {

        RestClient.getApiServie().facebookId(request).enqueue(object : Callback<UserInfo> {
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                status?.postValue(Status.ERROR)
                //fbLiveData?.value = ResponseFbId("","","fail","500","","","","","","")
            }

            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {

                if (response.isSuccessful) {
                    if (response.body()!!.error_code.equals(Constant.API_CODE_SUCCESS)) {

                        status?.postValue(Status.SUCCESS)
                        fbLiveData!!.postValue(response.body())

                    } else {

                        fbLiveData!!.postValue(response.body())
                        status?.postValue(Status.ERROR)
                    }


                } else {
                    onFailure(call, Throwable(Constant.ERROR_CODE_500))
                }

            }
        })
    }
}