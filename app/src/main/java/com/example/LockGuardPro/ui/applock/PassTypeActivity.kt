package com.example.LockGuardPro.ui.applock

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.LockGuardPro.ui.MainActivity
import com.example.LockGuardPro.ui.applock.PassActivtiy.Companion.TYPE_PASS
import com.example.LockGuardPro.ui.click
import com.example.athu.base.BaseActivity
import com.example.login.base.BaseFragment
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.FragmentTypePassBinding


class PassTypeActivity : BaseActivity<FragmentTypePassBinding>() {
    companion object {
        fun newInstance() = PassTypeActivity()
    }

    override fun getLayoutBinding() =
        FragmentTypePassBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        initAction(savedInstanceState)
    }


    fun initView(savedInstanceState: Bundle?) {
        binding.LlDrawPass.click {
            if (intent.extras?.getBoolean("lockPrivate") == true) {

                val bundle = Bundle(intent.extras)
                bundle. putString(
                    TYPE_PASS,
                    PassActivtiy.Companion.TypePass.TyPePattern.name
                )
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtras(intent.extras!!)
                })
            } else {
                val bundle = Bundle()
                bundle.putString(TYPE_PASS, PassActivtiy.Companion.TypePass.TyPePattern.name)
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtras(bundle)
                })
            }
        }
        binding.LlNumberLock.click {
            if (intent.extras?.getBoolean("lockPrivate") == true) {
                val bundle = Bundle(intent.extras)
                bundle. putString(
                    TYPE_PASS,
                    PassActivtiy.Companion.TypePass.TypePass.name
                )

                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtras(bundle)
                })
            } else {
                val bundle = Bundle()
                bundle.putString(TYPE_PASS, PassActivtiy.Companion.TypePass.TypePass.name)
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtras(bundle)
                })
            }
        }
    }

    fun initAction(savedInstanceState: Bundle?) {


    }

    fun listerData(savedInstanceState: Bundle?) {

    }
}