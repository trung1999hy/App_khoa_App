package com.example.LockGuardPro.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.ui.inapp.PurchaseInAppActivity
import com.example.LockGuardPro.ui.applistlockprivate.ListLockPrivateFragment
import com.example.LockGuardPro.ui.applock.PassFragment
import com.example.login.base.BaseFragment
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.FragmentSettingBinding

class SettingsFragment : BaseFragment<FragmentSettingBinding>() {

    companion object {
        fun newInstance() = SettingsFragment()
        const val KEY_BIOMETRICS = "KEY_BIOMETRICS"
        const val ENABLE_APP = "ENABLE_APP"
    }

    private lateinit var viewModel: SettingsViewModel
    private var preferences: Preferences? = null
    private var biometrics: Boolean = false
    private var enabledApp: Boolean = false
    override fun getLayoutBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater).apply {
            viewModel = ViewModelProvider(this@SettingsFragment)[SettingsViewModel::class.java]
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        preferences = context?.let { Preferences.getInstance(it) }
        biometrics = preferences?.getBoolean(KEY_BIOMETRICS) == true
        binding.switchBiometrics.isChecked = biometrics
        enabledApp = preferences?.getBoolean(ENABLE_APP) == true
        binding.switchEnbaledApp.isChecked = enabledApp
    }

    override fun initAction(savedInstanceState: Bundle?) {
        binding.switchBiometrics.setOnCheckedChangeListener { buttonView, isChecked ->
            preferences?.setBoolean(KEY_BIOMETRICS, isChecked)
        }
        binding.switchEnbaledApp.setOnCheckedChangeListener { buttonView, isChecked ->
            preferences?.setBoolean(ENABLE_APP, isChecked)
        }
        binding.changePass.setOnClickListener {
            val bundle = Bundle();
            bundle.putBoolean("changePass", true)
            (activity as MainActivity).openFragment(
                requireParentFragment(), R.id.fragment_container,
                PassFragment::class.java, bundle, true
            )
        }
        binding.listAppPrivate.setOnClickListener {
            (activity as MainActivity).openFragment(
                requireParentFragment(),
                R.id.fragment_container,
                ListLockPrivateFragment::class.java,
                null,
                true
            )
        }
        binding.store.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
    }

    override fun listerData(savedInstanceState: Bundle?) {

    }


}