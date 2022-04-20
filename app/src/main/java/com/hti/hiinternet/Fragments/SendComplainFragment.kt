package com.hti.hiinternet.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog
import com.hti.hiinternet.BuildConfig

import com.hti.hiinternet.R
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.base.BaseFrgment
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.greedbootevent.EventServiceTicketSuccess
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestComplainCategory
import com.hti.hiinternet.data.request.RequestSaveServiceTicket
import com.hti.hiinternet.data.response.Category
import com.hti.hiinternet.isValid
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.viewModel.ComplainCategoryViewModel
import kotlinx.android.synthetic.main.acitvity_login.*
import kotlinx.android.synthetic.main.fragment_send_complain.*
import kotlinx.android.synthetic.main.login_error_dialog.view.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class SendComplainFragment : BaseFrgment() {

    lateinit var categoryComplainViewModel: ComplainCategoryViewModel

    lateinit var shrimmerLayout: ShimmerFrameLayout

    val category_name: String="Choose Category Name"

    companion object {
        fun newIntance() = SendComplainFragment()
    }

    override fun onStart() {

        super.onStart()

        if (!EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_complain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryComplainViewModel = getViewModel()
        observeViewModel(categoryComplainViewModel)
        categoryComplainViewModel.complaincategory(getRequestComplainCategory())

        complain_phNo.setText(PreFerenceRepo.user_phone)


        btn_send.setOnClickListener {


            if (isValidate()) categoryComplainViewModel.saveTicket(getSaveTicketRequest())

        }


    }

    private fun getSaveTicketRequest(): RequestSaveServiceTicket {

        val category =
            categoryComplainViewModel.complainCategoryliveData?.value?.data?.get(spinner.selectedIndex)
         return RequestSaveServiceTicket(
            PreFerenceRepo.userId,
            BuildConfig.VERSION_CODE,
            complain_phNo.text.toString(),
            complain_text.text.toString(),
            category!!
        )

    }

    private fun getRequestComplainCategory(): RequestComplainCategory {
        return RequestComplainCategory()

    }

    private fun observeViewModel(v: ComplainCategoryViewModel) {

        v.status!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    Log.d("SendComplain Fragment", "success")

                    showNormalView()
                }
                Status.ERROR -> {
                    getApiErrorDialog("Api Fail", Constant.FAIL_TO_CONNECT_API)
                    Log.d("SendComplain Fragment", "error")


                }
                Status.LOADING -> {
                    showLoadingView()
                }

            }

            v.complainCategoryliveData?.observe(
                this,
                Observer<NetworkResult<ArrayList<Category>>> {
                    var categoryName = arrayListOf<String>()
                    it.data?.let {
                        for (i in it) {
                            categoryName.add(i.name)

                        }
                    }

                    spinner.setItems(categoryName)

                })

        })

        v.ticketstatus!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    hideLoading()
                    Log.d("Save ticket", "success")
//                    (activity as BaseActivity).getSuccessDialog(
//                        "Well Received","Your ticket ID is "+v.ticketResponseLiveData!!.value!!.ticketID)
//                        v.ticketResponseLiveData!!.value!!.message!!,
//                        v.ticketResponseLiveData.value!!.statusMessage
                    EventBus.getDefault().post(EventServiceTicketSuccess("Well Received","Your ticket ID is"+v.ticketResponseLiveData!!.value!!.ticketID))

                    complain_text.setText("")
                }
                Status.ERROR -> {
                    getApiErrorDialog("Api Fail", Constant.FAIL_TO_CONNECT_API)
                    Log.d("Save ticket", "error")

                }
                Status.LOADING -> {
                    showLoading()
                }

            }

            v.ticketResponseLiveData?.observe(
                this,
                Observer<NetworkResult<ResponseBody>> {

                })

        })

    }

    private fun isValidate(): Boolean{
        return if (!complain_text.isValid()) {
            Toast.makeText(context, "Complain text must not empty", Toast.LENGTH_SHORT).show()
            false
        } else if (spinner.selectedIndex.equals(0)) {
            Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    override fun showLoadingView() {

        spin_kit.visibility = View.VISIBLE

    }

    override fun showNormalView() {
        spin_kit.visibility = View.GONE

    }

    fun showLoading(){
        pLoading.visibility=View.VISIBLE
        btn_send.visibility=View.GONE
    }

    fun hideLoading(){
        pLoading.visibility=View.GONE
        btn_send.visibility=View.VISIBLE
    }


    private fun getViewModel(): ComplainCategoryViewModel {
        return ViewModelProviders.of(this).get(ComplainCategoryViewModel::class.java)
    }

    @Subscribe
    fun onReceiveSuccessTicket(event : EventServiceTicketSuccess){
        (activity as BaseActivity).getSuccessDialog(event.massage,event.ticketId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().unregister(this)
        }
    }

}
