package com.hti.hiinternet.viewModel

import androidx.lifecycle.MutableLiveData
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestComplainCategory
import com.hti.hiinternet.data.request.RequestSaveServiceTicket
import com.hti.hiinternet.data.response.Category
import com.hti.hiinternet.network.RestClient
import com.hti.hiinternet.util.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplainCategoryViewModel : BaseViewModel() {

    internal val complainCategoryliveData: MutableLiveData<NetworkResult<ArrayList<Category>>>? =
        MutableLiveData()
    internal val status: MutableLiveData<Status>? = MutableLiveData()

    internal val ticketResponseLiveData: MutableLiveData<NetworkResult<ResponseBody>>? =
        MutableLiveData()
    internal val ticketstatus: MutableLiveData<Status>? = MutableLiveData()


    fun complaincategory(request: RequestComplainCategory) {

        RestClient.getApiServie().complaincategory(request, PreFerenceRepo.token)
            .enqueue(object : Callback<NetworkResult<ArrayList<Category>>> {
                override fun onFailure(
                    call: Call<NetworkResult<ArrayList<Category>>>,
                    t: Throwable
                ) {
                    val data = NetworkResult<ArrayList<Category>>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    complainCategoryliveData?.postValue(data)
                    status?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ArrayList<Category>>>,
                    response: Response<NetworkResult<ArrayList<Category>>>
                ) {
                    if (response.isSuccessful) {

                        if (response.body()?.is_force_update!!) {
//check for update
                        }  else if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {

                            if (PreFerenceRepo.lang.equals("0")){
                                var cateNone =
                                    Category(Constant.ID_CATEGORY_NONE,"--choose category--")
                                (response.body()!!.data as ArrayList<Category>).add(0,cateNone)
                                complainCategoryliveData?.postValue(response.body())
                                status?.postValue(Status.SUCCESS)
                            }
                            else{
                                var cateNone =
                                    Category(Constant.ID_CATEGORY_NONE,"အမျိုးအစားများရွေးချယ်ရန်")
                                (response.body()!!.data as ArrayList<Category>).add(0,cateNone)
                                complainCategoryliveData?.postValue(response.body())
                                status?.postValue(Status.SUCCESS)
                            }


                        } else {

                            complainCategoryliveData?.postValue(response.body())
                            status?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }
                }

            })
    }

    fun saveTicket(request: RequestSaveServiceTicket) {

        ticketstatus?.postValue(Status.LOADING)

        RestClient.getApiServie().saveTicket(request, PreFerenceRepo.token)
            .enqueue(object : Callback<NetworkResult<ResponseBody>> {
                override fun onFailure(call: Call<NetworkResult<ResponseBody>>, t: Throwable) {
                    val data = NetworkResult<ResponseBody>()
                    data.apply {
                        message = t.localizedMessage
                        errorCode = Constant.ERROR_CODE_500
                        statusMessage = t.localizedMessage
                    }
                    ticketResponseLiveData?.postValue(data)
                    ticketstatus?.postValue(Status.ERROR)
                }

                override fun onResponse(
                    call: Call<NetworkResult<ResponseBody>>,
                    response: Response<NetworkResult<ResponseBody>>
                ) {
                    if (response.isSuccessful) {

                        if (response.body()?.is_force_update!!) {
//check for update
                        }  else if (response.body()!!.errorCode.toString() == Constant.API_CODE_SUCCESS) {
                            ticketResponseLiveData?.postValue(response.body())
                            ticketstatus?.postValue(Status.SUCCESS)
                        } else {
                            ticketResponseLiveData?.postValue(response.body())
                            ticketstatus?.postValue(Status.ERROR)
                        }

                    } else {
                        onFailure(call, Throwable("Fail to connect with api"))


                    }

                }

            })
    }
}