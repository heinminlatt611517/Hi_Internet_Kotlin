package com.hti.hiinternet.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestPayment
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel: BaseViewModel() {

    internal val paymentResponseLiveData: MutableLiveData<NetworkResult<ArrayList<ResponsePayment>>>? =
        MutableLiveData()
    internal val status: MutableLiveData<Status>? = MutableLiveData()

    fun loadPaymentData(request : RequestPayment)
    {

        Log.d("Token","payment toke -------------  ${PreFerenceRepo.token}")

        RestClient.getApiServie().payment(request,PreFerenceRepo.token)
            .enqueue(object :
            Callback<NetworkResult<ArrayList<ResponsePayment>>>
            {
                override fun onFailure(
                    call: Call<NetworkResult<ArrayList<ResponsePayment>>>,
                    t: Throwable
                ) {
                    val data = NetworkResult<ArrayList<ResponsePayment>>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    paymentResponseLiveData?.postValue(data)
                    status?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ArrayList<ResponsePayment>>>,
                    response: Response<NetworkResult<ArrayList<ResponsePayment>>>
                ) {

                    if (response.isSuccessful) {

                        if (response.body()?.is_force_update!!) {
//check for update
                        }  else if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {
                            paymentResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.SUCCESS)
                        } else {
                            paymentResponseLiveData?.postValue(response.body())
                            status?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }

                }

            })


    }

}