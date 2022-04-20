package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestAccount
import com.hti.hiinternet.data.response.ResponseAccountData
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel: BaseViewModel() {
    internal val accountResponseLiveData: MutableLiveData<NetworkResult<ResponseAccountData>>? = MutableLiveData()
    internal val status:MutableLiveData<Status>? = MutableLiveData()

    fun loadAccountData(requesst : RequestAccount){
        RestClient.getApiServie().myaccount(requesst,PreFerenceRepo.token)
            .enqueue(object : Callback<NetworkResult<ResponseAccountData>>{
                override fun onFailure(
                    call: Call<NetworkResult<ResponseAccountData>>,
                    t: Throwable
                ) {
                    val data = NetworkResult<ResponseAccountData>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    accountResponseLiveData?.postValue(data)
                    status?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ResponseAccountData>>,
                    response: Response<NetworkResult<ResponseAccountData>>
                ) {

                    if (response.isSuccessful) {

                        if (response.body()?.is_force_update!!) {
//check for update
                        }  else if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {
                            accountResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.SUCCESS)
                        } else {
                            accountResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }

                }

            })
    }

}