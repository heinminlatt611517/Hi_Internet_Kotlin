package com.hti.hiinternet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import com.hti.hiinternet.Fragments.*
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.greedbootevent.EventNotiCount
import com.hti.hiinternet.data.greedbootevent.EventNotiRead
import com.hti.hiinternet.data.greedbootevent.EventRecieveNewNoti
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.repo.database.DatabaseManger
import com.hti.hiinternet.data.request.RequestCheckCreateUser
import com.hti.hiinternet.login.ActivityLogin
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.view.LogInDialogFragment
import com.hti.hiinternet.viewModel.CheckCreateUserViewModel
import com.hti.hiinternet.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.viewhome_sub_menu.*
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession


class MainActivity : BaseActivity() {

    lateinit var popupMenu: PopupWindow
    lateinit var mCheckCreateUserViewModel: CheckCreateUserViewModel

    companion object {
        const val NOTI_EXTRA = "Notification"
        const val UPDATE_EXTRA = "Update"
        fun newIntent(context: Context, str: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(NOTI_EXTRA, str)
            intent.putExtra(UPDATE_EXTRA, str)
            return intent
        }
    }

    override fun onStart() {

        super.onStart()

        if (!EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().register(this)
        }
    }

    var isopen: Boolean = false
    lateinit var fragment: Fragment
    lateinit var fab_close: Animation
    lateinit var fab_anticlock: Animation
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            closeMenu()
            when (menuItem.itemId) {
                R.id.home -> {
                    if (isopen) {
                        collpaseFabView(fab_close, fab_anticlock)
                    }

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_frame,
                            this!!.getFragmentByCreatOrReuse(Constant.TAG_HOME)!!,
                            Constant.TAG_HOME
                        )
                        .addToBackStack(Constant.TAG_HOME)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    if (isopen) {
                        collpaseFabView(fab_close, fab_anticlock)
                    }
                    if (isAlreadyLogIn()) {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.main_frame,
                                this!!.getFragmentByCreatOrReuse(Constant.TAG_ACCOUNT)!!,
                                Constant.TAG_ACCOUNT
                            )
                            .addToBackStack(Constant.TAG_ACCOUNT)

                            .commit()

                        return@OnNavigationItemSelectedListener true
                    } else {
                        showLogInDialog()
                    }
                }
                R.id.payment -> {
                    if (isopen) {
                        collpaseFabView(fab_close, fab_anticlock)
                    }


                    if (isAlreadyLogIn()) {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.main_frame,
                                this!!.getFragmentByCreatOrReuse(Constant.TAG_PAYMENT)!!,
                                Constant.TAG_PAYMENT
                            )
                            .addToBackStack(Constant.TAG_PAYMENT)

                            .commit()
                        return@OnNavigationItemSelectedListener true
                    } else {
                        showLogInDialog()
                    }

                }
                R.id.notification -> {
                    if (isopen) {
                        collpaseFabView(fab_close, fab_anticlock)
                    }

                    if (isAlreadyLogIn()) {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.main_frame,
                                this!!.getFragmentByCreatOrReuse(Constant.TAG_NOTI)!!,
                                Constant.TAG_NOTI
                            )
                            .addToBackStack(Constant.TAG_NOTI)
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    } else {
                        showLogInDialog()
                    }

                }
            }
            false
        }

    override fun showLoadingView() {

    }

    override fun showNormalView() {
    }

    override fun getViewModel(): ViewModel {
        return HomeViewModel()
    }

    private fun getCheckCreateUserViewModel(): CheckCreateUserViewModel {
        return ViewModelProviders.of(this).get(CheckCreateUserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        registerFirebasetokne()
        mCheckCreateUserViewModel = getCheckCreateUserViewModel()


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.mutliaction

        checkCallingIntent()

        observerViewModel(mCheckCreateUserViewModel)

        val fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fab_clock = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_clock)
        fab_anticlock = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_anticlock)


        initView(fab_close, fab_anticlock, fab_open, fab_clock)

        callMenu.setOnClickListener {
            closeMenu()
            collpaseFabView(fab_close, fab_anticlock)

            checkPremission()
        }
        //get noti from database and show it in noti nav icon
        showNotiCountView()

    }

    private fun observerViewModel(mCheckCreateUserViewModel: CheckCreateUserViewModel) {
        mCheckCreateUserViewModel.status?.observe(this, Observer {
            when (it) {
                Status.ERROR -> {
                    Log.d("Fb Id Error", "Fail fb id")
                }
                //show Error here
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Log.d("Created user success", it.toString())

                }
            }
        })

        mCheckCreateUserViewModel.createUserResponseLiveData?.observe(this, Observer {
            Log.d("CheckCreateUser", it.checkCreateUser.toString())
            it.checkCreateUser.let {
                if (it == "fail") {
                    failCreateUserDialog(
                        "Fail",
                        getString(R.string.str_txt_dialog_fail_create_user)
                    )
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, SendComplainFragment.newIntance(), null).commit()
//                        if (isAlreadyLogIn()) {
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.main_frame, SendComplainFragment.newIntance(), null).commit()
//                        } else {
//                            showLogInDialog()
//                        }
                }

            }
        })
    }

    private fun checkCallingIntent() {
        if (intent.hasExtra(NOTI_EXTRA)) {
            if (PreFerenceRepo.token == null) {
                showLogInDialog()
                navigation.selectedItemId = R.id.home
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_frame,
                        this!!.getFragmentByCreatOrReuse(Constant.TAG_HOME)!!,
                        Constant.TAG_HOME
                    )
                    .addToBackStack(Constant.TAG_HOME)

                    .commit()
            } else {
                navigation.selectedItemId = R.id.notification
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_frame,
                        this!!.getFragmentByCreatOrReuse(Constant.TAG_NOTI)!!,
                        Constant.TAG_NOTI
                    )
                    .addToBackStack(Constant.TAG_NOTI)

                    .commit()
            }


        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_frame, HomeFragment.newIntance(), null).commit()
        }


    }


    private fun initView(
        fab_close: Animation?,
        fab_anticlock: Animation?,
        fab_open: Animation?,
        fab_clock: Animation?
    ) {
        fab_main.setOnClickListener {

            if (isopen) {
                collpaseFabView(fab_close, fab_anticlock)
                homeMenu.visibility = View.GONE

            } else {
                homeMenu.visibility = View.VISIBLE
                expandFabView(fab_open, fab_clock)
            }
        }

        menuServiceIssue.setOnClickListener {
            closeMenu()
            collpaseFabView(fab_close, fab_anticlock)

            if (isAlreadyLogIn()) {
                mCheckCreateUserViewModel.loadAccountData(getCheckCreateUserRequest())
            } else {
                showLogInDialog()
            }
        }

        menuServiceHistoru.setOnClickListener {
            closeMenu()
            collpaseFabView(fab_close, fab_anticlock)

            if (isAlreadyLogIn()) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, MyComplainFragment.newIntance(), null).commit()
            } else {
                showLogInDialog()
            }
        }
    }

    private fun expandFabView(
        fab_open: Animation?,
        fab_clock: Animation?
    ) {
        openFloatingButton(fab_open, fab_clock)
        //showDimBackground()
        isopen = true
    }

    private fun collpaseFabView(
        fab_close: Animation?,
        fab_anticlock: Animation?
    ) {
        closeFloatingButton(fab_close, fab_anticlock)
        // hideDimBackground()
        isopen = false
    }

    private fun openFloatingButton(
        fab_open: Animation?,
        fab_clock: Animation?
    ) {

        fab_main.startAnimation(fab_clock)

    }

    private fun closeFloatingButton(
        fab_close: Animation?,
        fab_anticlock: Animation?
    ) {


        fab_main.startAnimation(fab_anticlock)

    }


    private fun checkPremission() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    42
                )
            }
        } else {
            // Permission has already been granted
            makeCall(PreFerenceRepo.hotline_phone.toString())
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                makeCall(PreFerenceRepo.hotline_phone.toString())
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    //return old if exit
    fun getFragmentByCreatOrReuse(tag: String): Fragment? {
        when (tag) {
            Constant.TAG_HOME -> {
                return if (getFragment(tag) == null) HomeFragment.newIntance() else getFragment(tag)
            }
            Constant.TAG_PAYMENT -> {
                return if (getFragment(tag) == null) PaymentFragment.newIntance() else getFragment(
                    tag
                )
            }
            Constant.TAG_NOTI -> {
                return if (getFragment(tag) == null) NotificationFragment.newIntance() else getFragment(
                    tag
                )
            }
            Constant.TAG_ACCOUNT -> {
                return if (getFragment(tag) == null) MyAccountFragment.newIntance() else getFragment(
                    tag
                )
            }
        }
        return null
    }


    private fun getFragment(tag: String) = supportFragmentManager.findFragmentByTag(tag)
    private fun showNotiBadger(count: Int) =
        when {
            count > 0 -> navigation.getOrCreateBadge(R.id.notification).number = count
            count <= 0 -> {
                navigation.getOrCreateBadge(R.id.notification).isVisible = false
            }
            else -> navigation.getOrCreateBadge(R.id.notification).clearNumber()
        }


    fun showLogInDialog() {

/*
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.view_logindialog, null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this);
        val dialog = mBuilder.setView(mDialogView).create()
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow()!!.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
       *//*lp.
        aldialog.show()
      *//*  dialog.window?.attributes  =lp
        *//* mDialogView.btn_no.setOnClickListener {
             mAlertDialog.dismiss()
         }
         mDialogView.btn_go_to_login.setOnClickListener {
             this?.finish()
             val mainIntent = Intent(this, ActivityLogin::class.java)
             startActivity(mainIntent)
         }*/
        val logindialog = LogInDialogFragment { onSignButtonClick() }
        logindialog.show(supportFragmentManager, "logindialog")

    }


    fun onSignButtonClick() {
        this?.finish()
        val mainIntent = Intent(this, ActivityLogin::class.java)
        startActivity(mainIntent)
    }

    @Subscribe
    fun onNotiFicationEvent(event: EventNotiCount) {
        showNotiBadger(event.notiCount)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    fun onRecieveNewNoti(event: EventRecieveNewNoti) {
        val currentNotiCount = navigation
            .getOrCreateBadge(R.id.notification).number
        showNotiBadger(currentNotiCount + 1)
    }


    fun showDimBackground() {
        frDimBackground.visibility = View.VISIBLE
    }


    fun hideDimBackground() {
        frDimBackground.visibility = View.GONE
    }

    @Subscribe
    fun onNotiFicationReadEvent(event: EventNotiRead) {

        val currentNotiCount = navigation
            .getOrCreateBadge(R.id.notification).number
        showNotiBadger(currentNotiCount - 1)
    }

    fun showNotiCountView() {
        DatabaseManger.getDataBase(this).NotificationDao()
            .getNotiCount().observe(this, Observer {
                showNotiBadger(it)
            })
    }

    fun closeMenu() {
        homeMenu.visibility = View.GONE
    }

    private fun getCheckCreateUserRequest(): RequestCheckCreateUser {
        return RequestCheckCreateUser()
    }


    fun isAlreadyLogIn(): Boolean {

        // Log.d("FirstToken",PreFerenceRepo.token)
        if (!PreFerenceRepo.token.isNullOrEmpty()) {
            return true
        }
        return false
    }


}
