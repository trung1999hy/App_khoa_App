package com.example.LockGuardPro.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.LockGuardPro.ui.listapp.AppListFragment
import com.example.login.base.BaseFragment
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val appListFragment = AppListFragment.newInstance(this)
    val appLockFragment = AppLockFragment.newInstance(mainFragment = this)
    val settingsFragment = SettingsFragment.newInstance()
    private val listFragment =
        arrayListOf<Fragment>(appListFragment, appLockFragment, settingsFragment)
    private lateinit var mainViewPager: MainViewPager

    private lateinit var viewModel: MainViewModel
    override fun getLayoutBinding(layoutInflater: LayoutInflater): FragmentMainBinding {
        return FragmentMainBinding.inflate(layoutInflater).apply {
            viewModel = ViewModelProvider(this@MainFragment)[MainViewModel::class.java]
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mainViewPager = MainViewPager(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        binding.viewPager.adapter = mainViewPager
        mainViewPager.setFragment(listFragment)
        binding.viewPager.offscreenPageLimit = 2
        binding.BottomNavigationMain.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.unlock -> {
                    binding.viewPager.currentItem = 0
                    true
                }

                R.id.lock -> {
                    binding.viewPager.currentItem = 1
                    true
                }

                else -> {
                    binding.viewPager.currentItem = 2
                    true
                }
            }
        }
    }

    override fun initAction(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun listerData(savedInstanceState: Bundle?) {

    }


}