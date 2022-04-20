package com.hti.hiinternet.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestLogin
import com.hti.hiinternet.data.response.UserInfo
import com.hti.hiinternet.isValid
import com.hti.hiinternet.viewModel.HomeViewModel
import com.hti.hiinternet.viewModel.LogInViewModel
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.view_logindialog.*
import kotlinx.android.synthetic.main.view_logindialog.edt_userName
import kotlinx.android.synthetic.main.activity_signin.prLoadingView as prLoadingView1

class LogInDialogFragment(private val onSignInButtonClick: () -> Unit) : DialogFragment() {


    private lateinit var viewModel: LogInViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_logindialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        btSignIn.setOnClickListener {
           // Toast.makeText(context,"clicke",Toast.LENGTH_SHORT).show()
            if (isFormValide()) viewModel.userInfo(getLogInRequest())

            Log.d("login request",getLogInRequest().toString())
        }
        observeViewModel(viewModel)

    }

    private fun observeViewModel(viewModel: LogInViewModel) {

        viewModel.userInfoLiveData!!.observe(this, Observer<UserInfo> {
            saveUserInfo(it)
            (activity as BaseActivity).registerFirebasetokne()
        })


        viewModel.status?.observe(this, Observer<Status> {
            when (it) {
                Status.ERROR -> {
                    if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("W")) && (PreFerenceRepo.lang.equals("0")))
                    {
                        tv_errorMessage.text="Wrong User"
                        showNormalView()
                        showErrorMessage()
                    }
                    else if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("W")) && (PreFerenceRepo.lang.equals("1")))
                    {
                        tv_errorMessage.text="အကောင့်မှားယွင်းနေပါသည်"
                        showNormalView()
                        showErrorMessage()
                    }

                    else if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("A")) && (PreFerenceRepo.lang.equals("0")) )
                    {
                        tv_errorMessage.text="Already Bind"
                        showNormalView()
                        showErrorMessage()
                    }

                    else if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("A")) && (PreFerenceRepo.lang.equals("1")) )
                    {
                        tv_errorMessage.text="ချိတ်ဆက်ပြီးသားအကောင့်"
                        showNormalView()
                        showErrorMessage()
                    }

                    else if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("N")) && (PreFerenceRepo.lang.equals("0")) )
                    {
                        tv_errorMessage.text="Need Post Data"
                        showNormalView()
                        showErrorMessage()
                    }

                    else if ((viewModel.userInfoLiveData?.value?.message!!.startsWith("N")) && (PreFerenceRepo.lang.equals("1")) )
                    {
                        tv_errorMessage.text="အကောင့်မှားယွင်းနေပါသည်"
                        showNormalView()
                        showErrorMessage()
                    }

                }
                //show Error here
                Status.LOADING -> {
                    showLoadingView()
                }
                Status.SUCCESS -> {
                    dismiss()

                }
            }
        })


    }

    private fun showLoadingView() {
        btSignIn.visibility=View.INVISIBLE
        tv_errorMessage.visibility=View.GONE
        prLoadingView.visibility=View.VISIBLE
    }

    private fun showNormalView() {

        btSignIn.visibility=View.VISIBLE

        prLoadingView.visibility=View.GONE
    }

    private fun showErrorMessage(){
        tv_errorMessage.visibility=View.VISIBLE
    }


    private fun initView(view: View) {
      viewModel=getViewModel()
        imageView4.setOnClickListener { dismiss() }

        tv_needHelp.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://horizoninternet.net/")
            startActivity(openURL)
        }

//        tv_userName.text=PreFerenceRepo.uffname+""+PreFerenceRepo.uflname
//
//        Glide.with(this)
//            .load(PreFerenceRepo.ufprofile)
//            .into(iv_userProfile)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog

    }

    override fun onStart() {
        super.onStart()

        val layoutParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParam.setMargins(20, 20, 20, 20)

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private  fun getViewModel(): LogInViewModel{
        return ViewModelProviders.of(this).get(LogInViewModel::class.java)
    }

    private fun getLogInRequest(): RequestLogin =
        RequestLogin(edt_userName.text.toString(),PreFerenceRepo.ufid.toString(),PreFerenceRepo.ufprofile.toString())

    private fun isFormValide(): Boolean {
        return if (!edt_userName.isValid()) {
            edt_userName.error = resources.getString(R.string.str_empty_username)
            false
        } else {
            true
        }

    }


    fun saveUserInfo(loginResponse: UserInfo) {

        loginResponse.token.let {
            PreFerenceRepo.token = it

        }
        loginResponse.user_id.let {
            PreFerenceRepo.userId = it

        }
        loginResponse.user_phone.let {
            PreFerenceRepo.user_phone = it
        }

        loginResponse.payment_channel.let {
            PreFerenceRepo.payment_channel = it
        }

    }
}