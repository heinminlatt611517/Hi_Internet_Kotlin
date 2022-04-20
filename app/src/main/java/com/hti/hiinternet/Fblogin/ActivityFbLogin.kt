package com.hti.hiinternet.Fblogin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.GsonBuilder
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestFbId
import com.hti.hiinternet.data.response.FacebookUser
import com.hti.hiinternet.data.response.UserInfo
import com.hti.hiinternet.viewModel.FbViewModel
import kotlinx.android.synthetic.main.activity_fb_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class ActivityFbLogin : BaseActivity() {


    private var callbackManager: CallbackManager? = null
    private var profileTracker: ProfileTracker? = null
    lateinit var btn_fac : Button



    lateinit var user: FacebookUser

    lateinit var viewModel: FbViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_login)


        viewModel=getViewModel() as FbViewModel


        btSignInWithFacebook.setOnClickListener(View.OnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList<String>("public_profile", "email")
            )
        })

        printHashKey(this)
        checkAlreadyLogin()


    }

    override fun showLoadingView() {

    }

    override fun showNormalView() {

    }

    override fun getViewModel(): ViewModel {
        return ViewModelProviders.of(this).get(FbViewModel::class.java)
    }
    private fun checkAlreadyLogin(){
        if (isConnected()) {
            FacebookSdk.sdkInitialize(applicationContext)
            if (isFacebookLoggedIn()) {
//
                goToMainScreen()
              //  requestUserData()
            } else {


                callbackManager = CallbackManager.Factory.create()

                assignListener()

                profileTracker = object : ProfileTracker() {
                    override fun onCurrentProfileChanged(oldProfile: Profile?, newProfile: Profile?) {
                        profileTracker?.stopTracking()
                        Profile.setCurrentProfile(newProfile)
                    }
                }
                (profileTracker as ProfileTracker).startTracking()

            }
        }

    }


    private fun isConnected() : Boolean{
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message)
        }

        return connected
    }

    private fun isFacebookLoggedIn() : Boolean{
        return AccessToken.getCurrentAccessToken() != null
    }

    private fun requestUserData(){
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            val resp = response.jsonObject
            val gson = GsonBuilder().create()
            user = gson.fromJson<FacebookUser>(resp.toString(), FacebookUser::class.java)

/////

            PreFerenceRepo.ufid=user.id
            PreFerenceRepo.uffname=user.first_name
            PreFerenceRepo.uflname=user.last_name
            PreFerenceRepo.ufprofile=user.picture.data.url

            viewModel.loadfbId(getFbIdReqest())
            observeViewModel(viewModel)


            Log.d("User data", user.toString())
            Log.d("User profile", user.picture.data.url)

        }
        val parameters = Bundle()
        parameters.putString("fields", "id,first_name,last_name,email,picture")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun registerUiCompos(){
       // btn_fac =findViewById(R.id.btSignInWithFacebook)
    }

    private fun observeViewModel(viewModel: FbViewModel){

       viewModel.status?.observe(this,Observer<Status>{

           when (it) {
               Status.ERROR -> {
                   Log.d("Fb Id Error","Fail fb id")
               }
               //show Error here
               Status.LOADING -> {
               }
               Status.SUCCESS -> {
                   Log.d("Fb Id success",it.toString())


                   registerFirebasetokne()
                   goToMainScreen()

               }
           }

       })

        viewModel.fbLiveData!!.observe(this, Observer<UserInfo> {

            saveFacebookId(it)
        })


    }

    private fun saveFacebookId(data : UserInfo){

        data.token.let {
            PreFerenceRepo.token = it
        }
        data.user_id.let {
            PreFerenceRepo.userId = it

        }
        data.user_phone.let {
            PreFerenceRepo.user_phone=it
        }

        data.payment_channel.let {
            PreFerenceRepo.payment_channel=it
        }


    }

    private fun assignListener(){

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    requestUserData()
                    Toast.makeText(applicationContext, "Login success", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {

                    Toast.makeText(applicationContext, "Login fail", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun getFbIdReqest() : RequestFbId {
        return RequestFbId()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private  fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.d("", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("", "printHashKey()", e)
        }

    }



}
