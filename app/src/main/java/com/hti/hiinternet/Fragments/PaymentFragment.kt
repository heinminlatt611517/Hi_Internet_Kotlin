package com.hti.hiinternet.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.hti.hiinternet.MainActivity

import com.hti.hiinternet.R
import com.hti.hiinternet.adapter.PaymentAdapter
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.base.BaseFrgment
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestPayment
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.login.ActivityLogin
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.util.LanguageUtils
import com.hti.hiinternet.util.ui.HtiLinearLayoutManager
import com.hti.hiinternet.viewModel.PaymentViewModel
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.layout_home_header.*


class PaymentFragment : BaseFrgment() {

    lateinit var paymentViewModel: PaymentViewModel
    lateinit var paymentLayoutManager: LinearLayoutManager
    lateinit var recPayment: RecyclerView
    lateinit var paymentAdapter: PaymentAdapter

    lateinit var shrimmerLayout: ShimmerFrameLayout

 companion object {
        fun newIntance() = PaymentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initview(view)

        if (PreFerenceRepo.lang.equals("0")){
            langSpinner.setSelection(0)
        }
        else if (PreFerenceRepo.lang.equals("1")){
            langSpinner.setSelection(1)
        }

        langSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectItem= p0?.getItemAtPosition(p2).toString()

                if (!PreFerenceRepo.lang.equals(p2.toString())){

                    PreFerenceRepo.lang=p2.toString()

                    if (p2==0){
                        activity?.let { LanguageUtils.setLocale("EN", it)
                        }
                    }
                    else if (p2==1) {
                        activity?.let { LanguageUtils.setLocale("UNI", it) }
                    }

                }

            }
        }

        btn_all.setTextColor(resources.getColor(R.color.colorWhite))

        chip_group.setOnCheckedChangeListener { group, checkedId ->
           // val chip : Chip?=view.findViewById(checkedId)

            if (checkedId==R.id.btn_all){
                btn_all.setTextColor(resources.getColor(R.color.colorWhite))
                btn_paid.setTextColor(resources.getColor(R.color.colorBg))
                btn_unpaid.setTextColor(resources.getColor(R.color.colorBg))
                paymentViewModel.paymentResponseLiveData?.value.let {
                    paymentAdapter.setData(it?.data)
                }
            }
            else if (checkedId==R.id.btn_paid){
                btn_paid.setTextColor(resources.getColor(R.color.colorWhite))
                btn_all.setTextColor(resources.getColor(R.color.colorBg))
                btn_unpaid.setTextColor(resources.getColor(R.color.colorBg))
                paymentViewModel.paymentResponseLiveData?.value.let {

                    val paidList = it?.data?.filter {
                        it.paid_status == Constant.INVOICE_STATUS_PAID

                    }

                    paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
                    Log.d("paid list size", paidList?.size.toString())
                }
            }
            else{
                btn_unpaid.setTextColor(resources.getColor(R.color.colorWhite))
                btn_paid.setTextColor(resources.getColor(R.color.colorBg))
                btn_all.setTextColor(resources.getColor(R.color.colorBg))
                paymentViewModel.paymentResponseLiveData?.value.let {

                    val paidList = it?.data?.filter {
                        it.paid_status == Constant.INVOICE_STATUS_UNPAID

                    }
                    paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
                    Log.d("paid list size", paidList?.size.toString())
                }
            }

        }


//        btn_all.setOnClickListener {
//            paymentViewModel.paymentResponseLiveData?.value.let {
//                paymentAdapter.setData(it?.data)
//            }
//        }
//
//        btn_paid.setOnClickListener {
//            paymentViewModel.paymentResponseLiveData?.value.let {
//
//                val paidList = it?.data?.filter {
//                    it.paid_status == Constant.INVOICE_STATUS_PAID
//
//                }
//                paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
//                Log.d("paid list size", paidList?.size.toString())
//            }
//        }
//
//        btn_unpaid.setOnClickListener {
//            paymentViewModel.paymentResponseLiveData?.value.let {
//
//                val paidList = it?.data?.filter {
//                    it.paid_status == Constant.INVOICE_STATUS_UNPAID
//
//                }
//                paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
//                Log.d("paid list size", paidList?.size.toString())
//            }
//        }

        btn_payment_channel.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(PreFerenceRepo.payment_channel)
            startActivity(openURL)
        }

    }

    private fun initview(view: View) {
        paymentLayoutManager =LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        paymentViewModel = getViewModel()
        recPayment = view.findViewById(R.id.recycler_payment)
        recPayment.layoutManager = paymentLayoutManager
        paymentAdapter = PaymentAdapter(ArrayList())
        recPayment.adapter = paymentAdapter
        recPayment.setHasFixedSize(true)
        observeViewModel(paymentViewModel)
        paymentViewModel.loadPaymentData(getPaymentRequest())



        shrimmerLayout = view.findViewById(R.id.loadingLaoyut)

        btn_all.isSelected = true

        paymentAdapter.onItemClick = {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(it.paid_url)
            startActivity(openURL)
        }

    }

    private fun getPaymentRequest(): RequestPayment {
        return RequestPayment()
    }

    private fun observeViewModel(v: PaymentViewModel) {
        Log.d("Payment","success")

        v.status!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    Log.d("PaymentFragment", "success")

                    showNormalView()
                }
                Status.ERROR -> {
                    v.paymentResponseLiveData!!.value.let {
                        if (it!!.errorCode == Constant.SESSION_EXPIRE) {
                            (activity as MainActivity).onSessionExpire(
                                it.message!!,
                                it.statusMessage
                            )
                        }
                        else
                        {
                            getApiErrorDialog("Api Fail",Constant.FAIL_TO_CONNECT_API)
                        }
                    }

                }
                Status.LOADING -> {
                    showLoadingView()
                }

            }

            v.paymentResponseLiveData?.observe(
                this,
                Observer<NetworkResult<ArrayList<ResponsePayment>>> {
                    Log.d("Payment","payment")

                    val chipID= chip_group.checkedChipId

                    paymentAdapter.setData(it.data)

                    if (chipID == R.id.btn_all){
                        paymentViewModel.paymentResponseLiveData?.value.let {
                            paymentAdapter.setData(it?.data)
                        }
                    }
                    else if(chipID == R.id.btn_paid){
                        paymentViewModel.paymentResponseLiveData?.value.let {

                            val paidList = it?.data?.filter {
                                it.paid_status == Constant.INVOICE_STATUS_PAID

                            }
                            paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
                            Log.d("paid list size", paidList?.size.toString())
                        }
                    }
                    else if(chipID == R.id.btn_unpaid){
                        paymentViewModel.paymentResponseLiveData?.value.let {

                            val paidList = it?.data?.filter {
                                it.paid_status == Constant.INVOICE_STATUS_UNPAID

                            }
                            paymentAdapter.setData(paidList as ArrayList<ResponsePayment>?)
                            Log.d("paid list size", paidList?.size.toString())
                        }
                    }




                })

        })
    }

     override fun showLoadingView() {
        nlayout.visibility = View.GONE
        loadingLaoyut.visibility = View.VISIBLE
        shrimmerLayout.startShimmer()
    }

    override fun showNormalView() {
        nlayout.visibility = View.VISIBLE
        loadingLaoyut.visibility = View.GONE
        shrimmerLayout.stopShimmer()
    }

    private fun getViewModel(): PaymentViewModel {
        return ViewModelProviders.of(this).get(PaymentViewModel::class.java)
    }
}
