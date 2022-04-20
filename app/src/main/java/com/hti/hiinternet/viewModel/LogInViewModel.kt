package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.request.RequestLogin
import com.hti.hiinternet.data.response.UserInfo
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInViewModel : BaseViewModel() {

    internal val loginInLiveData: MutableLiveData<UserInfo>? =
        MutableLiveData<UserInfo>()

    internal val userInfoLiveData: MutableLiveData<UserInfo>? =
        MutableLiveData<UserInfo>()


    internal val status: MutableLiveData<Status>? = MutableLiveData()


    fun logIn(request: RequestLogin) {

        status?.postValue(Status.LOADING)

        RestClient.getApiServie().login(request).enqueue(object :

            Callback<UserInfo> {
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

                status?.postValue(Status.ERROR)
                userInfoLiveData?.value = UserInfo("fail", "500", t.localizedMessage, "", "","","")


            }

            override fun onResponse(
                call: Call<UserInfo>,
                response: Response<UserInfo>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.error_code.equals(Constant.API_CODE_SUCCESS)) {

                        status?.postValue(Status.SUCCESS)
                        loginInLiveData!!.postValue(response.body())

                    } else {

                        loginInLiveData!!.postValue(response.body())
                        status?.postValue(Status.ERROR)
                    }


                } else {
                    onFailure(call, Throwable(Constant.ERROR_CODE_500))
                }

            }

        })
    }

    fun userInfo(request: RequestLogin) {

        status?.postValue(Status.LOADING)

        RestClient.getApiServie().login(request).enqueue(object :

            Callback<UserInfo> {
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

                status?.postValue(Status.ERROR)
                userInfoLiveData?.value = UserInfo("fail", "500", t.localizedMessage, "", "","","")
            }

            override fun onResponse(
                call: Call<UserInfo>,
                response: Response<UserInfo>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.error_code.equals(Constant.API_CODE_SUCCESS)) {


                        userInfoLiveData!!.postValue(response.body())
                        status?.postValue(Status.SUCCESS)

                    } else {

                        userInfoLiveData!!.postValue(response.body())
                        status?.postValue(Status.ERROR)
                    }


                } else {
                    onFailure(call, Throwable(Constant.ERROR_CODE_500))
                }

            }

        })
    }


}