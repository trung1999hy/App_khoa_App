package com.example.LockGuardPro.ui

import android.os.Bundle
import com.example.LockGuardPro.base.PermionActivity
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.service.LockService
import com.example.LockGuardPro.ui.applock.PassFragment
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.ActivityMainBinding


class MainActivity : PermionActivity<ActivityMainBinding>() {
    private var lock: Lock? = null
    var mainFragment = MainFragment.newInstance()
    override fun getLayoutBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        LockService.startService(this)

        val passFragment = PassFragment.newInstance(false)
        openFragment(null, R.id.fragment_container, passFragment.javaClass, null, false)
    }



    fun setAppLock(lock: Lock) {
        this.lock = lock
    }

    fun getAppLock(): Lock? = lock



}

