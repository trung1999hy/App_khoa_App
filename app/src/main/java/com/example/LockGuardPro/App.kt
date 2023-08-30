package com.example.LockGuardPro

import android.app.Application
import com.example.LockGuardPro.local.Preferences

class App : Application() {

    var preference: Preferences? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        preference = Preferences.getInstance(this)
        if (preference?.firstInstall == false) {
            preference?.firstInstall = true
            preference?.setValueCoin(30)
        }

    }

    companion object {
        var instance: App? = null
        fun newInstance(): App {
            if (instance == null) {
                instance = App()
            }
            return instance!!
        }
    }
}