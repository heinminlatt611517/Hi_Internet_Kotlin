package com.hti.hiinternet.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestHome
import com.hti.hiinternet.data.response.ResponseHomeData
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import com.marcinmoskala.kotlinpreferences.PreferenceHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : BaseViewModel() {
    internal val homeResponseLiveData: MutableLiveData<NetworkResult<ResponseHomeData>>? =
        MutableLiveData()
    internal val status: MutableLiveData<Status>? = MutableLiveData()
    fun loadHomeData(request: RequestHome) {
        RestClient.getApiServie().home(request, PreFerenceRepo.token)
            .enqueue(object :
                Callback<NetworkResult<ResponseHomeData>> {
                override fun onFailure(call: Call<NetworkResult<ResponseHomeData>>, t: Throwable) {
                    val data = NetworkResult<ResponseHomeData>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    homeResponseLiveData?.postValue(data)
                    status?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ResponseHomeData>>,
                    response: Response<NetworkResult<ResponseHomeData>>
                ) {
                    if (response.isSuccessful) {

                        if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {
                            homeResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.SUCCESS)
                        } else {
                            homeResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }
                }

            })
    }


}