package com.example.LockGuardPro.ui.listapp

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.LockGuardPro.base.PermionActivity
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.ui.MainFragment
import com.example.LockGuardPro.widget.patternlockview.widget.AppAnimator
import com.example.login.base.BaseFragment
import com.thn.LockGuardPro.databinding.FragmentAppListBinding


class AppListFragment : BaseFragment<FragmentAppListBinding>() {

    companion object {
        fun newInstance(mainFragment: MainFragment) = AppListFragment().apply {
            this.mainFragment = mainFragment
        }

        const val FIRST_INSTALL = "FIRST_INSTALL"
        const val ENABLE_APP = "ENABLE_APP"


    }

    private var position: Int = -1

    private val preferences: Preferences by lazy {
        Preferences.getInstance(requireContext())
    }
    private lateinit var appListAdapter: AppListAdapter

    private lateinit var viewModel: AppListViewModel
    private lateinit var mainFragment: MainFragment
    override fun getLayoutBinding(inflater: LayoutInflater): FragmentAppListBinding =
        FragmentAppListBinding.inflate(inflater).apply {

        }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this@AppListFragment).get(AppListViewModel::class.java)
        appListAdapter = AppListAdapter() { appLock ->
            (activity as PermionActivity<*>?)?.let {
                it.checkPermission() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(
                            context
                        ) && isUsageAccessGranted(requireContext())
                    ) {
                        viewModel.addAppLock(appLock.apply { isLock = true })
                    } else Toast.makeText(
                        requireContext(),
                        "Lỗi do cung cấp thiếu  quyền vui lòng cấp quyền để app hoạt đông ",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
        if (preferences.getBoolean(FIRST_INSTALL) == false) {
            preferences.setBoolean(FIRST_INSTALL, true)
            preferences.setBoolean(ENABLE_APP, true)
        }

        viewModel.addList(
            requireContext(),
            requireActivity().packageManager,
            requireActivity().packageName
        )


        val itemAnimator =
            AppAnimator()
        itemAnimator.setLocked(false)
        binding.rvListApp.itemAnimator = itemAnimator
        binding.rvListApp.setHasFixedSize(true)
        binding.rvListApp.adapter = appListAdapter
    }

    override fun initAction(savedInstanceState: Bundle?) {
    }

    fun isUsageAccessGranted(context: Context): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun listerData(savedInstanceState: Bundle?) {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvListApp.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvListApp.visibility = View.VISIBLE
            }
        }
        viewModel.getAll().observe(viewLifecycleOwner) {
            val intent = Intent("getData")
            activity?.let { it1 -> LocalBroadcastManager.getInstance(it1).sendBroadcast(intent) }
            val listData = it.filter { !it.isLock }
            listData.forEach {
                it.drawable = viewModel.getAppIconByPackageName(requireContext(), it.packetName)
            }
            appListAdapter.setData(listData.sortedBy { it.appName })


        }

    }
}