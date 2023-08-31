package com.example.LockGuardPro.ui.applistlockprivate

import android.app.AlertDialog
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
import com.example.LockGuardPro.App
import com.example.LockGuardPro.base.PermionActivity
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.ui.applock.PassTypeActivity
import com.example.LockGuardPro.ui.inapp.PpurchaseInAppActivity
import com.example.login.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.thn.LockGuardPro.databinding.FragmentListAppLockPrivateBinding


class ListLockPrivateFragment : BaseFragment<FragmentListAppLockPrivateBinding>() {

    companion object {
        fun newInstance() = ListLockPrivateFragment()
    }

    private lateinit var viewModel: AppLockPrivateViewModel
    private var adapter: AppLockAdapter? = null
    override fun getLayoutBinding(inflater: LayoutInflater): FragmentListAppLockPrivateBinding =
        FragmentListAppLockPrivateBinding.inflate(inflater).apply {
            viewModel =
                ViewModelProvider(this@ListLockPrivateFragment)[AppLockPrivateViewModel::class.java]
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCoin()
    }
    override fun initView(savedInstanceState: Bundle?) {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        adapter = AppLockAdapter {applock ->
            if (applock.pass.isNullOrEmpty()) {
                (activity as PermionActivity<*>).let {
                    it.checkPermission() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(
                                context
                            ) && isUsageAccessGranted(requireContext())
                        ) {
                            showDialog(applock)
                        } else Toast.makeText(
                            requireContext(),
                            "Lỗi do cung cấp thiếu  quyền vui lòng cấp quyền để app hoạt đông",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else showDialogRemove(applock)
        }
        binding.listApp.adapter = adapter
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


    override fun initAction(savedInstanceState: Bundle?) {

    }

    fun showDialog(lock: Lock) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Bạn có chắc khóa riêng ứng dụng này ko ? ")
            .setMessage("Dùng mất 2 vàng bạn có chắc không ?")
        alertDialog.setPositiveButton(
            "Đồng ý"
        ) { p0, p1 ->

            App.newInstance().preference?.apply {
                if (getValueCoin() ?: 0 > 2) {
                    setValueCoin(getValueCoin()?.minus(2) ?: 0)
                    Toast.makeText(
                        requireContext(),
                        "Đã thêm  thành công và trù 2 vàng",
                        Toast.LENGTH_SHORT

                    ).show()
                    getCoin()
                    startActivity(
                        Intent(
                            requireActivity(),
                            PassTypeActivity::class.java
                        ).apply {
                            val bundle = Bundle()
                            bundle.putParcelable("lock", lock)
                            bundle.putSerializable("lockPrivate", true)
                            putExtras(bundle)
                        })
                } else startActivity(
                    Intent(
                        requireActivity(),
                        PpurchaseInAppActivity::class.java
                    )
                )
            }
        }
        alertDialog.setNegativeButton("Hủy") { p0, p1 ->
            p0.dismiss()
        }
        alertDialog.create().show()
    }
    fun getCoin() {
        binding.coin.text = App.newInstance()?.preference?.getValueCoin().toString()
    }

    fun showDialogRemove(lock: Lock) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(" Xóa")
            .setMessage("Bạn có chắc muốn xóa khóa riêng ứng dụng này ko ?")
        alertDialog.setPositiveButton(
            "Đồng ý"
        ) { p0, p1 ->
            lock.pass = null
            viewModel.update(lock) {
                Snackbar.make(
                    requireActivity(),
                    binding.root, "Bạn đã xóa thành công ", Toast.LENGTH_LONG
                ).show()
            }
            p0.dismiss()
        }
        alertDialog.setNegativeButton("Hủy") { p0, p1 ->
            p0.dismiss()
        }
        alertDialog.create().show()
    }


    override fun listerData(savedInstanceState: Bundle?) {
        viewModel.getAll().observe(viewLifecycleOwner) {
            val intent = Intent("getData")
            val listData = it.filter { !it.isLock }
            listData.forEach {
                it.drawable = viewModel.getAppIconByPackageName(requireContext(), it.packetName)
            }
            requireActivity().sendBroadcast(intent)
            adapter?.submitList(it)
        }
    }
}