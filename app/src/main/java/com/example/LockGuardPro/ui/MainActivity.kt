package com.example.LockGuardPro.ui

import android.os.Bundle
import com.example.LockGuardPro.base.PermionActivity
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.service.LockService
import com.example.LockGuardPro.ui.applock.PassActivtiy
import com.example.LockGuardPro.ui.applock.PassActivtiy.Companion.PASS_LOGIN
import com.example.LockGuardPro.ui.applock.PassActivtiy.Companion.TYPE_PASS
import com.example.LockGuardPro.ui.applock.PassTypeActivity
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.ActivityMainBinding


class MainActivity : PermionActivity<ActivityMainBinding>() {
    private var lock: Lock? = null
    var mainFragment = MainFragment.newInstance()
    private var preferences: Preferences? = null
    override fun getLayoutBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        LockService.startService(this)
        val passFragment = PassActivtiy.newInstance()
        preferences = Preferences.getInstance(this)
        if (intent.extras?.getBoolean("lockPrivate") == true) {
            openFragment(
                null,
                R.id.fragment_container,
                AppLockPrivateFragment::class.java,
                intent.extras,
                false
            )
        } else {
            if (preferences?.getString(TYPE_PASS).isNullOrEmpty()) {
                openFragment(
                    null,
                    R.id.fragment_container,
                    passFragment.javaClass,
                    intent.extras,
                    false
                )
            } else {
                val bundle = Bundle()
                bundle.putString(TYPE_PASS, preferences?.getString(TYPE_PASS))
                openFragment(null, R.id.fragment_container, passFragment.javaClass, bundle, false)
            }
        }
    }


    fun setAppLock(lock: Lock) {
        this.lock = lock
    }

    fun getAppLock(): Lock? = lock


}

