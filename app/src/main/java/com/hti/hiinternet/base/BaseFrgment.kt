package com.hti.hiinternet.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.hti.hiinternet.Fragments.MyComplainFragment
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.login.ActivityLogin
import kotlinx.android.synthetic.main.log_out_dialog.view.*
import kotlinx.android.synthetic.main.login_error_dialog.view.*
import kotlinx.android.synthetic.main.login_error_dialog.view.dialog_message
import kotlinx.android.synthetic.main.login_error_dialog.view.dialog_title

abstract class BaseFrgment : Fragment() {
    abstract fun showLoadingView()
    abstract fun showNormalView()


    fun getApiErrorDialog(title: String, message: String): Any {

        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.login_error_dialog, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val mAlertDialog = mBuilder?.show()

        mDialogView.dialog_button.setOnClickListener {
            mAlertDialog?.dismiss()
        }

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        return mAlertDialog as AlertDialog

    }

    fun logOutDialog() : Any {

//        val dialogBuilder = AlertDialog.Builder(activity!! as Context)
//
//        dialogBuilder.setMessage("Do you want to logout this application ?")
//            .setCancelable(false)
//            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
//                activity?.finish()
//                PreFerenceRepo.token = null
//                val mainIntent = Intent(activity, MainActivity::class.java)
//                startActivity(mainIntent)
//            })
//            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
//                dialog.cancel()
//            })
//
//        val alert = dialogBuilder.create()
//        alert.setTitle("Hi_Internet")
//        alert.show()
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.log_out_dialog, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val mAlertDialog = mBuilder?.show()

        mDialogView.btn_no.setOnClickListener {
            mAlertDialog?.dismiss()
        }
        mDialogView.btn_ok.setOnClickListener {
            activity?.finish()
            PreFerenceRepo.token = null
            val mainIntent = Intent(activity, MainActivity::class.java)
            startActivity(mainIntent)
        }
        return mAlertDialog as AlertDialog

    }


    open fun failCreateUserDialog(title: String, message: String?): AlertDialog {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.login_error_dialog, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val mAlertDialog = mBuilder?.show()

        mDialogView.dialog_button.setOnClickListener {
            mAlertDialog?.dismiss()
        }

        mDialogView.dialog_title.setText(title)
        mDialogView.dialog_message.setText(message)
        return mAlertDialog as AlertDialog

    }

}





