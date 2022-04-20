package com.hti.hiinternet.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.ProfileTracker
import com.facebook.login.LoginManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.hti.hiinternet.Fblogin.ActivityFbLogin
import com.hti.hiinternet.MainActivity

import com.hti.hiinternet.R
import com.hti.hiinternet.adapter.PaymentAdapter
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.base.BaseFrgment
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestAccount
import com.hti.hiinternet.data.response.ResponseAccountData
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.login.ActivityLogin
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.util.ui.HtiLinearLayoutManager
import com.hti.hiinternet.view.LogOutDialog
import com.hti.hiinternet.viewModel.AccountViewModel
import com.hti.hiinternet.viewModel.PaymentViewModel
import com.marcinmoskala.kotlinpreferences.PreferenceHolder
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.txt_activation_date
import kotlinx.android.synthetic.main.fragment_my_account.txt_address
import kotlinx.android.synthetic.main.fragment_my_account.txt_phone
import kotlinx.android.synthetic.main.fragment_my_account.txt_user_id
import kotlinx.android.synthetic.main.fragment_myaccount.*
import kotlinx.android.synthetic.main.fragment_myaccount.iv_userProfile
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.view_logindialog.*


class MyAccountFragment : BaseFrgment() {

    lateinit var accountViewModel: AccountViewModel

    lateinit var shrimmerLayout: ShimmerFrameLayout

    companion object {
        fun newIntance() = MyAccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myaccount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel = getViewModel()
        observeViewModel(accountViewModel)
        accountViewModel.loadAccountData(getAccountRequest())
        shrimmerLayout = view.findViewById(R.id.loading_Laoyut)
//
        btn_signOut.setOnClickListener {
            //  logOutDialog()


            val logoutdialog = LogOutDialog { onLogout() }
            logoutdialog.show(childFragmentManager, "logoutdialog")
        }

        Glide.with(this)
            .load(PreFerenceRepo.ufprofile)
            .into(iv_userProfile)
    }

    private fun getAccountRequest(): RequestAccount {

        return RequestAccount()
    }

    private fun observeViewModel(v: AccountViewModel) {
        v.status!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    Log.d("PaymentFragment", "success")

                    showNormalView()
                }
                Status.ERROR -> {
                    v.accountResponseLiveData!!.value.let {
                        if (it!!.errorCode == Constant.SESSION_EXPIRE) {
                            (activity as MainActivity as BaseActivity).onSessionExpire(
                                it.message!!,
                                it.statusMessage
                            )
                        } else {
                            getApiErrorDialog("Api Fail", Constant.FAIL_TO_CONNECT_API)
                        }
                    }
                }
                Status.LOADING -> {
                    showLoadingView()
                }

            }

            v.accountResponseLiveData?.observe(
                this,
                Observer<NetworkResult<ResponseAccountData>> {


                    it.data?.let {
                        bindMyAccount(it)
                    }


                })

        })
    }

    private fun bindMyAccount(it: ResponseAccountData) {
//        txt_user_id.setText(it.user_id)
//        txt_user_name.setText(it.user_name)
//        txt_plan.setText(it.plan)
//        txt_activation_date.setText(it.activate_date)
//        txt_status.setText(it.account_status)
//        txt_service.setText(it.service)
//        txt_phone.setText(it.mobile_no)
//        txt_address.setText(it.address)
//
//        txt_title_user_name.setText(it.user_name)


        Log.d("DataPlan",it.service+""+it.plan)

        tv_accountUserName.setText(it.user_name)
        txt_user_id.setText(it.user_id)
        txt_phone.setText(it.mobile_no)
        txt_activation_date.setText(it.activate_date)
        txt_address.setText(it.address)
        btnFtth.setText(it.service+""+it.plan)

    }

    override fun showLoadingView() {
        normal_layout.visibility = View.GONE
        loading_Laoyut.visibility = View.VISIBLE
        shrimmerLayout.startShimmer()
    }

    override fun showNormalView() {
        normal_layout.visibility = View.VISIBLE
        loading_Laoyut.visibility = View.GONE
        shrimmerLayout.stopShimmer()
    }

    private fun getViewModel(): AccountViewModel {

        return ViewModelProviders.of(this).get(AccountViewModel::class.java)
    }

    private fun onLogout() {


        PreFerenceRepo.token = ""
        PreFerenceRepo.userId = ""
        PreFerenceRepo.tokensaveState = ""
        PreFerenceRepo.user_phone = ""
        PreFerenceRepo.payment_channel = ""
        PreFerenceRepo.hotline_phone = ""
        PreFerenceRepo.ufid = ""
        PreFerenceRepo.uffname = ""
        PreFerenceRepo.uflname = ""
        PreFerenceRepo.ufprofile = ""

        activity?.finish()
        LoginManager.getInstance().logOut()
        val mainIntent = Intent(activity, MainActivity::class.java)
        //startActivity(mainIntent)
        activity?.startActivity(mainIntent)

    }

}
