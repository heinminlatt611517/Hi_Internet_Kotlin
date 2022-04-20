package com.hti.hiinternet.util

import android.app.Activity
import java.util.*

object LanguageUtils {

    fun setLocale(lang: String, context: Activity) {
        val myLocale = Locale(lang)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        context.finish()
        context.overridePendingTransition(0, 0)
        context.startActivity(context.intent)
        context.overridePendingTransition(0, 0)
    }
}