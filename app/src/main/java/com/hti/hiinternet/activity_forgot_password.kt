package com.hti.hiinternet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.login.ActivityLogin
import kotlinx.android.synthetic.main.activity_forgot_password.*

class activity_forgot_password : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        iv_back.setOnClickListener {
            val intent=Intent(this,ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }


        btok.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://horizoninternet.net/")
            startActivity(openURL)
        }
    }
}
