package com.hti.hiinternet.base

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import com.hti.hiinternet.BuildConfig
import com.hti.hiinternet.Fragments.MyComplainFragment
import com.hti.hiinternet.Fragments.SendComplainFragment
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.RegisterFirebaseTokenIntentServie
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.login.ActivityLogin
import com.hti.hiinternet.util.Constant
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.login_error_dialog.view.*
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {
    private var dialog: AlertDialog? = null
    lateinit var firebaseToken: String
    abstract fun showLoadingView()
    abstract fun showNormalView()
    abstract fun getViewModel(): ViewModel
    open fun getToken(): String {
        return ""
    }


    open fun getErrorDialog(title: String, message: String?): AlertDialog {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.login_error_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.dialog_button.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        return mAlertDialog as AlertDialog

    }

    open fun failCreateUserDialog(title: String, message: String?): AlertDialog {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.login_error_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.dialog_button.setOnClickListener {
            mAlertDialog.dismiss()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MyComplainFragment.newIntance(), null).commit()

        }

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        return mAlertDialog as AlertDialog

    }

    fun getVersionCode(): Int = BuildConfig.VERSION_CODE
    fun getSuccessDialog(title: String, message: String): Any {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.success_dialog, null)
        val mBuilder = this?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }


        val mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.dialog_button.setOnClickListener {
            mAlertDialog?.dismiss()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        return mAlertDialog as AlertDialog
    }

    fun onSessionExpire(message: String, title: String) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.session_expire_dialog, null)
        val mBuilder = this?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
                .setCancelable(false)
        }

        val mAlertDialog = mBuilder?.show()

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        mDialogView.dialog_button.setOnClickListener {
            PreFerenceRepo.token = null
            mAlertDialog?.dismiss()
//            var logInIntent = Intent(this, ActivityLogin::class.java)
//            startActivity(logInIntent)
//            finish()

        }
    }

    fun LhowoginDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("You Need To Login First!!")
            .setCancelable(false)
            .setPositiveButton("Go To Login", DialogInterface.OnClickListener { dialog, id ->
                this?.finish()

                val mainIntent = Intent(this, ActivityLogin::class.java)
                startActivity(mainIntent)
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Hi_Internet")
        alert.show()
    }

    open fun registerFirebasetokne() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            var tokenInten = Intent(this, RegisterFirebaseTokenIntentServie::class.java)
            tokenInten.putExtra(Constant.FCM_TOKEN, it.token)
            Log.d("firebase token is is ", it.token)
            this?.startService(tokenInten)
        }
    }

}