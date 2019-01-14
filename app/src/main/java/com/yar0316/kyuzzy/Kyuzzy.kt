package com.yar0316.kyuzzy

import android.app.Application
import io.realm.Realm

open class Kyuzzy: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}