package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestMycomplainService
import com.hti.hiinternet.data.response.ResponseMycomplainService
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MycomplainServiceViewModel: BaseViewModel() {

    internal val myComplainResponseLiveData: MutableLiveData<NetworkResult<ArrayList<ResponseMycomplainService>>>? =
        MutableLiveData()
    internal val status: MutableLiveData<Status>? = MutableLiveData()

    fun loadMycomplainService(request : RequestMycomplainService)
    {
        RestClient.getApiServie().mycomplainService(request,PreFerenceRepo.token)
            .enqueue(object: Callback<NetworkResult<ArrayList<ResponseMycomplainService>>>{
                override fun onFailure(
                    call: Call<NetworkResult<ArrayList<ResponseMycomplainService>>>,
                    t: Throwable
                ) {
                    val data = NetworkResult<ArrayList<ResponseMycomplainService>>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    myComplainResponseLiveData?.postValue(data)
                    status?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ArrayList<ResponseMycomplainService>>>,
                    response: Response<NetworkResult<ArrayList<ResponseMycomplainService>>>
                ) {
                    if (response.isSuccessful) {

                        if (response.body()?.is_force_update!!) {
//check for update
                        }  else if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {
                            myComplainResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.SUCCESS)
                        } else {
                            myComplainResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }

                }

            })
    }

}