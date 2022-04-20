package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestAccount
import com.hti.hiinternet.data.request.RequestCheckCreateUser
import com.hti.hiinternet.data.response.ResponseAccountData
import com.hti.hiinternet.data.response.ResponseCheckCreateUser
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckCreateUserViewModel: BaseViewModel() {
    internal val createUserResponseLiveData: MutableLiveData<ResponseCheckCreateUser>? = MutableLiveData()
    internal val status:MutableLiveData<Status>? = MutableLiveData()

    fun loadAccountData(request : RequestCheckCreateUser){
        RestClient.getApiServie().checkCreateUser(request,PreFerenceRepo.token)
            .enqueue(object : Callback<ResponseCheckCreateUser> {
            override fun onFailure(call: Call<ResponseCheckCreateUser>, t: Throwable) {
                status?.postValue(Status.ERROR)

            }

            override fun onResponse(call: Call<ResponseCheckCreateUser>, response: Response<ResponseCheckCreateUser>) {

                if (response.isSuccessful) {
                    if (response.body()!!.errorCode.equals(Constant.API_CODE_SUCCESS)) {

                        status?.postValue(Status.SUCCESS)
                        createUserResponseLiveData!!.postValue(response.body())

                    } else {

                        createUserResponseLiveData!!.postValue(response.body())
                        status?.postValue(Status.ERROR)
                    }


                } else {
                    onFailure(call, Throwable(Constant.ERROR_CODE_500))
                }

            }
        })

    }

}