package com.example.LockGuardPro.ui.applock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.ui.MainActivity
import com.example.LockGuardPro.ui.MainFragment
import com.example.LockGuardPro.widget.numberlockview.NumberLockListener
import com.example.LockGuardPro.widget.patternlockview.PatternLockView
import com.example.LockGuardPro.widget.patternlockview.listener.PatternLockViewListenerqqq
import com.example.LockGuardPro.widget.patternlockview.utils.PatternLockUtiladla
import com.example.login.base.BaseFragment
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.FragmentLockBinding


class PassFragment : BaseFragment<FragmentLockBinding>(), NumberLockListener {

    companion object {
        fun newInstance(changePass: Boolean) = PassFragment().apply {
            this.chanegpass = changePass
        }

        const val PASS_LOGIN = "PASS_LOGIN"
        const val KEY_BIOMETRICS = "KEY_BIOMETRICS"
        const val TYPE_PASS = "TYPE_PASS"

        enum class TypePass() {
            TyPePattern, TypePass
        }

    }

    private lateinit var preferences: Preferences
    private var pass: String = ""
    private var chanegpass: Boolean = false
    private var biometrics: Boolean = false
    private var typePass: String = TypePass.TyPePattern.name


    private lateinit var viewModel: PassViewModel
    override fun getLayoutBinding(inflater: LayoutInflater): FragmentLockBinding =
        FragmentLockBinding.inflate(inflater).apply {
            viewModel = ViewModelProvider(this@PassFragment).get(PassViewModel::class.java)
        }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun initView(savedInstanceState: Bundle?) {
        preferences = Preferences.getInstance(requireContext())
        typePass = (preferences.getString(TYPE_PASS) ?: TypePass.TypePass.name)
        pass = preferences.getString(PASS_LOGIN) ?: ""
        chanegpass = arguments?.getBoolean("changePass", false) ?: false
        biometrics = preferences.getBoolean(KEY_BIOMETRICS) == true;
        if (chanegpass || pass.isNullOrEmpty()) {
            pass = ""
            biometrics = false
            binding.fingerprint.visibility = View.GONE
            if (chanegpass) {
                if (typePass == TypePass.TypePass.name) {
                    binding.patternLockView.visibility = View.GONE
                    binding.LllIndicator.visibility = View.VISIBLE
                    binding.numberLockView.visibility = View.VISIBLE
                } else {
                    binding.patternLockView.visibility = View.VISIBLE
                    binding.numberLockView.visibility = View.GONE
                    binding.LllIndicator.visibility = View.GONE
                }
                binding.changeTypePass.visibility = View.GONE

            }
        } else {
            if (typePass == TypePass.TypePass.name) {
                binding.patternLockView.visibility = View.GONE
                binding.numberLockView.visibility = View.VISIBLE
                binding.LllIndicator.visibility = View.VISIBLE
                binding.titlePass.text = "Nhập mật khẩu để đăng nhập"
            } else {
                binding.patternLockView.visibility = View.VISIBLE
                binding.numberLockView.visibility = View.GONE
                binding.LllIndicator.visibility = View.GONE
                binding.titlePass.text = "Vẽ mật khẩu để đăng nhập"
            }
            binding.changeTypePass.visibility = View.GONE

        }
        if (biometrics) {
            binding.fingerprint.visibility = View.VISIBLE
            set()
        }
        binding.numberLockView.setListener(this)
    }

    override fun initAction(savedInstanceState: Bundle?) {
        binding.fingerprint.setOnClickListener {
            set()
        }
        binding.changeTypePass.setOnClickListener {
            var countClick = binding.changeTypePass.tag.toString().toInt()
            countClick++
            binding.changeTypePass.tag = countClick
            if (binding.changeTypePass.tag.toString() == "2") {
                binding.changeTypePass.setImageResource(R.drawable.combination)
                binding.patternLockView.visibility = View.VISIBLE
                binding.numberLockView.visibility = View.GONE
                binding.LllIndicator.visibility = View.GONE
                pass = ""
                if (chanegpass || pass.isNullOrEmpty()) {
                    binding.titlePass.text = "Tạo mật khẩu"
                } else {
                    binding.titlePass.text = "Vẽ mật khẩu để đăng nhập"
                }
                binding.changeTypePass.tag = 0
            } else {
                binding.changeTypePass.setImageResource(R.drawable.password)
                binding.patternLockView.visibility = View.GONE
                pass = ""
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
                if (preferences.getString(PASS_LOGIN) == null || chanegpass) {
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
                        preferences.setString(PASS_LOGIN, pass)
                        preferences.setString(TYPE_PASS, typePass)
                        (activity as MainActivity).openFragment(
                            this@PassFragment,
                            R.id.fragment_container,
                            (activity as MainActivity).mainFragment.javaClass,
                            null,
                            false
                        )
                    } else {
                        binding.patternLockView.clearPattern()
                        binding.titlePass.text = "Mật khẩu sai vui  lòng nhập lại "
                    }
                } else
                    if (pass == PatternLockUtiladla.patternToString(
                            binding.patternLockView,
                            pattern
                        ) && !pass.isNullOrEmpty() && pattern?.size ?: 0 > 3
                    ) {
                        (activity as MainActivity).openFragment(
                            this@PassFragment,
                            R.id.fragment_container,
                            MainFragment::class.java,
                            null,
                            false
                        )
                    } else if (!pass.isNullOrEmpty() && pattern?.size ?: 0 > 3) {
                        binding.patternLockView.clearPattern()
                        binding.titlePass.text = "Mật khẩu sai"
                    } else if (pass.isNullOrEmpty() && pattern?.size ?: 0 > 3) {
                        pass = PatternLockUtiladla.patternToMD5(
                            binding.patternLockView,
                            pattern
                        )
                        binding.patternLockView.clearPattern()
                        binding.titlePass.text = "Vui Lòng vẽ lại mật khẩu ?"
                    } else {
                        binding.patternLockView.clearPattern()
                        binding.titlePass.text = "Vui Lòng vẽ lại mật khẩu ?"
                    }
            }


            override fun onCleared() {

            }

        })

    }

    fun set() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(requireContext())
                val biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence,
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult,
                        ) {
                            super.onAuthenticationSucceeded(result)
                            (activity as MainActivity).openFragment(
                                this@PassFragment,
                                R.id.fragment_container,
                                MainFragment::class.java,
                                null,
                                false
                            )
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for ${getString(R.string.app_name)}")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Cance")
                    .build()
                biometricPrompt.authenticate(promptInfo)
            }

        }
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

    @SuppressLint("ResourceAsColor")
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
        if (preferences.getString(PASS_LOGIN).isNullOrEmpty() || chanegpass) {
            if (pass.isNullOrEmpty()) {
                pass = binding.numberLockView.getPass()
                binding.numberLockView.clearPass()
                binding.titlePass.text = "Nhập lại mật khẩu để xác nhận"
            } else if (pass == binding.numberLockView.getPass() && pass.isNotEmpty()) {
                preferences.setString(PASS_LOGIN, pass)
                preferences.setString(TYPE_PASS, typePass)
                (activity as MainActivity).openFragment(
                    this@PassFragment,
                    R.id.fragment_container,
                    (activity as MainActivity).mainFragment.javaClass,
                    null,
                    false
                )
            }
        } else {
            if (binding.numberLockView.getPass() == preferences.getString(PASS_LOGIN))
                (activity as MainActivity).openFragment(
                    this@PassFragment,
                    R.id.fragment_container,
                    (activity as MainActivity).mainFragment.javaClass,
                    null, false
                )
            else {
                binding.titlePass.text = "Vui Lòng nhập lại mật khẩu ?"
                binding.numberLockView.clearPass()

            }
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