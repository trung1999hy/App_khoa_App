package com.example.LockGuardPro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.ui.applock.PassFragment
import com.example.LockGuardPro.widget.numberlockview.NumberLockListener
import com.example.LockGuardPro.widget.patternlockview.PatternLockView
import com.example.LockGuardPro.widget.patternlockview.listener.PatternLockViewListenerqqq
import com.example.LockGuardPro.widget.patternlockview.utils.PatternLockUtiladla
import com.example.login.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.FragmentAppLockPrivateBinding

class AppLockPrivateFragment : BaseFragment<FragmentAppLockPrivateBinding>(), NumberLockListener {

    companion object {
        fun newInstance(lock: Lock) = AppLockPrivateFragment().apply {
            this.lock = lock
        }

    }

    private var pass: String? = null
    private var lock: Lock? = null
    private var typePass: String = PassFragment.Companion.TypePass.TyPePattern.name
    private lateinit var viewModel: AppLockPrivateViewModel
    override fun getLayoutBinding(inflater: LayoutInflater): FragmentAppLockPrivateBinding {
        return FragmentAppLockPrivateBinding.inflate(inflater).apply {
            viewModel =
                ViewModelProvider(this@AppLockPrivateFragment)[AppLockPrivateViewModel::class.java]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lock = (activity as MainActivity).getAppLock()
    }

    override fun initView(savedInstanceState: Bundle?) {

        if (lock == null) {
            (activity as MainActivity).onBackPressed()
        }
        binding.numberLockView.setListener(this)
    }

    override fun initAction(savedInstanceState: Bundle?) {
        binding.changeTypePass.setOnClickListener {
            var countClick = binding.changeTypePass.tag.toString().toInt()
            countClick++
            binding.changeTypePass.tag = countClick
            if (binding.changeTypePass.tag.toString() == "2") {
                binding.changeTypePass.setImageResource(R.drawable.combination)
                binding.patternLockView.visibility = View.VISIBLE
                binding.numberLockView.visibility = View.GONE
                binding.LllIndicator.visibility = View.GONE
                binding.titlePass.text = "Tạo mật khẩu"
                binding.changeTypePass.tag = 0
            } else {
                binding.changeTypePass.setImageResource(R.drawable.password)
                binding.patternLockView.visibility = View.GONE
                binding.numberLockView.visibility = View.VISIBLE
                binding.LllIndicator.visibility = View.VISIBLE
                binding.titlePass.text = "Nhập mật khẩu tối đa 4 ký tự"
            }
        }
    }



    override fun listerData(savedInstanceState: Bundle?) {
        binding.patternLockView.addPatternLockListener(object :
            PatternLockViewListenerqqq {
            override fun onStarted() {

            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {

            }

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                if (pass.isNullOrEmpty() && pattern?.size ?: 0 > 3) {
                    pass = PatternLockUtiladla.patternToString(
                        binding.patternLockView,
                        pattern
                    )
                    binding.patternLockView.clearPattern()
                    binding.titlePass.text = "Nhập lại mật khẩu để xác nhận"
                } else if (pass == PatternLockUtiladla.patternToString(
                        binding.patternLockView,
                        pattern
                    ) && pattern?.size ?: 0 > 3
                ) {
                    lock?.pass = pass
                    lock?.isLock = true
                    lock?.let {
                        viewModel.update(it) {
                            Snackbar.make(
                                binding.root,
                                "Bạn đã thêm thành công !",
                                Toast.LENGTH_LONG
                            ).show()
                            (activity as MainActivity).onBackPressed()
                        }
                    }
                } else {
                    binding.patternLockView.clearPattern()
                    binding.titlePass.text = "Mật khẩu sai vui  lòng nhập lại "
                }

            }


            override fun onCleared() {

            }

        })

    }

    override fun onXButtonClick() {
        setPassInput(
            binding.image1,
            binding.image2,
            binding.image3,
            binding.image4,
            positionSelect = binding.numberLockView.getPass().length
        )
    }

    override fun onNumberButtonClick() {
        setPassInput(
            binding.image1,
            binding.image2,
            binding.image3,
            binding.image4,
            positionSelect = binding.numberLockView.getPass().length
        )
    }

    override fun onComple() {
        if (pass.isNullOrEmpty()) {
            pass = binding.numberLockView.getPass()
            binding.numberLockView.clearPass()
            binding.titlePass.text = "Nhập lại mật khẩu để xác nhận"
        } else if (pass == binding.numberLockView.getPass() && pass?.isNotEmpty() == true) {
            lock?.pass = pass
            lock?.isLock = true
            lock?.typePass = typePass
            lock?.let {
                viewModel.update(it) {
                    Snackbar.make(
                        binding.root,
                        "Bạn đã thêm thành công !",
                        Toast.LENGTH_LONG
                    ).show()
                    (activity as MainActivity).onBackPressed()
                }
            }
        } else {
            binding.numberLockView.clearPass()
            binding.titlePass.text = "Nhập lại mật khẩu để xác nhận"
        }

    }

    fun setPassInput(vararg image: ImageView, positionSelect: Int) {
        var i = 0
        image.forEach {
            if (i > positionSelect - 1) {
                it.setImageResource(R.drawable.d0_opsz24)
            } else {
                it.setImageResource(R.drawable.circle)
            }
            i++
        }
    }


}