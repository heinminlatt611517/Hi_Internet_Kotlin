package com.hti.hiinternet.data.repo

import com.marcinmoskala.kotlinpreferences.PreferenceHolder

object PreFerenceRepo : PreferenceHolder() {
    var token: String? by bindToPreferenceFieldNullable()
    var userId: String? by bindToPreferenceFieldNullable()
    var tokensaveState: String? by bindToPreferenceFieldNullable()
    var user_phone: String? by bindToPreferenceFieldNullable()
    var payment_channel: String? by bindToPreferenceFieldNullable()
    var hotline_phone: String? by bindToPreferenceFieldNullable()
    var lang : String ? by bindToPreferenceFieldNullable()
    var ufid : String ? by bindToPreferenceFieldNullable()
    var uffname : String ? by bindToPreferenceFieldNullable()
    var uflname : String ? by bindToPreferenceFieldNullable()
    var ufprofile : String ? by bindToPreferenceFieldNullable()
    var videoUrl : String ? by bindToPreferenceFieldNullable()





}