package com.hti.hiinternet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hti.hiinternet.Fragments.HomeFragment

class activity_application_guide : AppCompatActivity() {

    companion object{

        fun newIntent(context: Context) : Intent{
            return Intent(context,activity_application_guide::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_guide)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame,HomeFragment.newIntance())
    }
}