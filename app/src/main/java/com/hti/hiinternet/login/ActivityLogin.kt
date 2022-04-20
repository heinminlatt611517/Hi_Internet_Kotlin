package com.hti.hiinternet.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestLogin
import com.hti.hiinternet.data.response.UserInfo
import com.hti.hiinternet.isValid
import com.hti.hiinternet.viewModel.LogInViewModel
import kotlinx.android.synthetic.main.acitvity_login.prLoadingView
import kotlinx.android.synthetic.main.activity_signin.*

class ActivityLogin : BaseActivity() {

    var tag: String = "LogInActivity"
    lateinit var viewModel: LogInViewModel
    lateinit var btnLogIn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        tv_forgetPassword.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://horizoninternet.net/")
            startActivity(openURL)
        }

        viewModel = getViewModel() as LogInViewModel
        btn_signIn.setOnClickListener {
            if (isFormValide()) viewModel.logIn(getLogInRequest())
        }

        observseViewModel(viewModel)
    }

    private fun observseViewModel(viewModel: LogInViewModel) {
        viewModel.status?.observe(this, Observer<Status> {
            when (it) {
                Status.ERROR -> {
                    showNormalView()
                    getErrorDialog("Log In Fail", viewModel.loginInLiveData?.value?.message).show()
                }
                //show Error here
                Status.LOADING -> {
                    showLoadingView()
                }
                Status.SUCCESS -> {
                    finish()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainIntent)

                }
            }
        })
        viewModel.loginInLiveData!!.observe(this, Observer<UserInfo> {
            saveUserInfo(it)
        })
    }

    override fun showLoadingView() {
        edt_phoneNo.visibility = View.GONE
        edt_userName.visibility = View.GONE
        btn_signIn.visibility = View.GONE
        tv_forgetPassword.visibility=View.GONE
        tv2.visibility=View.GONE
        prLoadingView.visibility = View.VISIBLE

    }

    override fun showNormalView() {
        tv_forgetPassword.visibility=View.VISIBLE
        edt_phoneNo.visibility = View.VISIBLE
        edt_userName.visibility = View.VISIBLE
        btn_signIn.visibility = View.VISIBLE
        tv2.visibility=View.VISIBLE
        prLoadingView.visibility = View.GONE

    }

    override fun getViewModel(): ViewModel =
        ViewModelProviders.of(this).get(LogInViewModel::class.java)


    fun saveUserInfo(loginResponse: UserInfo) {

        loginResponse.token.let {
            PreFerenceRepo.token = it


        }
        loginResponse.user_id.let {
            PreFerenceRepo.userId = it

        }
        loginResponse.user_phone.let {
            PreFerenceRepo.user_phone=it
        }

        loginResponse.payment_channel.let {
            PreFerenceRepo.payment_channel=it
        }


    }


    //must call only form is valid
    private fun getLogInRequest(): RequestLogin =
        RequestLogin(edt_userName.text.toString(), PreFerenceRepo.ufid.toString(),PreFerenceRepo.ufprofile.toString())

    private fun isFormValide(): Boolean {
        return if (!edt_userName.isValid()) {
            edt_userName.error = resources.getString(R.string.str_empty_username)
            false
//        } else if (!edt_phoneNo.isValid()) {
//            edt_phoneNo.error = resources.getString(R.string.str_empty_password)
//            false
        } else {
            true
        }

    }


}
