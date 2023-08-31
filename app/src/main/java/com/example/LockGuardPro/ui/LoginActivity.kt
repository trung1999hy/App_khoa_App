package com.example.LockGuardPro.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.service.LockService
import com.example.LockGuardPro.ui.applock.PassActivtiy
import com.example.LockGuardPro.ui.applock.PassActivtiy.Companion.TYPE_PASS
import com.example.LockGuardPro.widget.numberlockview.NumberLockListener
import com.example.LockGuardPro.widget.patternlockview.PatternLockView
import com.example.LockGuardPro.widget.patternlockview.listener.PatternLockViewListenerqqq
import com.example.LockGuardPro.widget.patternlockview.utils.PatternLockUtiladla
import com.example.athu.base.BaseActivity
import com.thn.LockGuardPro.R
import com.thn.LockGuardPro.databinding.ActivityLoginBinding
import com.thn.LockGuardPro.databinding.ViewPatternOverlayBinding


class LoginActivity : BaseActivity<ActivityLoginBinding>(), NumberLockListener {
    private var preferences: com.example.LockGuardPro.local.Preferences? = null
    private var biometrics: Boolean = false
    private var pkg: String? = null
    private var lockService: LockService? = null
    private var typePass: String = PassActivtiy.Companion.TypePass.TyPePattern.name
    private var windowManager: WindowManager? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var viewPatternOverlayBinding: ViewPatternOverlayBinding? = null
    private var password: String? = null

    private var params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // or other appropriate window type
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // optional flags
        PixelFormat.TRANSLUCENT // optional pixel format
    )


    override fun getLayoutBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        viewPatternOverlayBinding = ViewPatternOverlayBinding.inflate(layoutInflater, null, false)
        pkg = intent.getStringExtra("pkg")
        preferences = Preferences.getInstance(this)
        typePass = intent.getStringExtra(TYPE_PASS)?: preferences?.getString(TYPE_PASS) ?: PassActivtiy.Companion.TypePass.TyPePattern.name
        biometrics = preferences?.getBoolean(SettingsFragment.KEY_BIOMETRICS) == true
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(
                this
            )
        ) windowManager?.addView(viewPatternOverlayBinding?.root, params)
        else {
            Toast.makeText(
                this,
                "Lỗi do cung cấp thiếu  quyền vui lòng cấp quyền để app hoạt đông",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
        if (typePass == PassActivtiy.Companion.TypePass.TypePass.name) {
            viewPatternOverlayBinding?.patternLockView?.visibility = View.GONE
            viewPatternOverlayBinding?.LllIndicator?.visibility = View.VISIBLE
            viewPatternOverlayBinding?.numberLockView?.visibility = View.VISIBLE
        } else {
            viewPatternOverlayBinding?.patternLockView?.visibility = View.VISIBLE
            viewPatternOverlayBinding?.numberLockView?.visibility = View.GONE
            viewPatternOverlayBinding?.LllIndicator?.visibility = View.GONE
        }
        viewPatternOverlayBinding!!.numberLockView.setListener(this)
        viewPatternOverlayBinding?.ivIcon?.setImageDrawable(getAppIconByPackageName(this, pkg))
        if (biometrics) {
            runOnUiThread {
                viewPatternOverlayBinding?.fingerprint?.visibility = View.VISIBLE
                set()
            }
        }
        viewPatternOverlayBinding?.fingerprint?.setOnClickListener {
            set()
        }
        viewPatternOverlayBinding?.patternLockView?.addPatternLockListener(object :
            PatternLockViewListenerqqq {
            override fun onStarted() {}
            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}
            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                password = if (intent.getStringExtra("pass") != null) {
                    intent.getStringExtra("pass")
                } else
                    preferences?.getString(PassActivtiy.PASS_LOGIN)

                val pass = PatternLockUtiladla.patternToString(
                    viewPatternOverlayBinding?.patternLockView,
                    pattern
                )
                if (pass == password && password != null) {
                    lockService?.setLastForegroundAppPackage(pkg)
                    biometricPrompt?.cancelAuthentication()
                    windowManager?.removeViewImmediate(viewPatternOverlayBinding?.root)
                    finish()
                } else {
                    runOnUiThread {
                        viewPatternOverlayBinding?.titlePass?.text =
                            "Mật khẩu không đúng vui lòng vẽ lại !"
                    }
                }
                viewPatternOverlayBinding?.patternLockView?.clearPattern()
            }

            override fun onCleared() {}
        })
    }


    private fun getAppIconByPackageName(context: Context, packageName: String?): Drawable? {
        try {
            val packageManager = context.packageManager
            val appInfo = packageName?.let { packageManager.getApplicationInfo(it, 0) }
            return appInfo?.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null

    }


    override fun onBackPressed() {

    }

    override fun onStop() {
        val intent = Intent("set")
        intent.putExtra("isShowActivity", false)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        if (viewPatternOverlayBinding?.root?.isAttachedToWindow == true) {
            windowManager?.removeView(viewPatternOverlayBinding?.root);
            biometricPrompt?.cancelAuthentication()
        } else {
        }
        finish()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun set() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {

                            super.onAuthenticationError(errorCode, errString)
                            runOnUiThread {
                                viewPatternOverlayBinding?.titlePass?.text =
                                    "Mật khẩu không đúng vui lòng nhập lại !"

                            }
                            biometricPrompt?.cancelAuthentication()

                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)
                            finish()
                        }

                        override fun onAuthenticationFailed() {

                            super.onAuthenticationFailed()
                            runOnUiThread {
                                viewPatternOverlayBinding?.titlePass?.text =
                                    "Mật khẩu không đúng vui lòng vẽ lại !"

                            }
                        }
                    })
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for ${getString( R.string.app_name)}")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Cancel")
                    .build()
                promptInfo?.let {
                    biometricPrompt?.authenticate(it)
                }

            }

        }

    }

    override fun onXButtonClick() {
        setPassInput(
            viewPatternOverlayBinding?.image1!!,
            viewPatternOverlayBinding!!.image2,
            viewPatternOverlayBinding!!.image3,
            viewPatternOverlayBinding!!.image4,
            positionSelect = viewPatternOverlayBinding!!.numberLockView.getPass().length
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onNumberButtonClick() {
        setPassInput(
            viewPatternOverlayBinding?.image1!!,
            viewPatternOverlayBinding!!.image2,
            viewPatternOverlayBinding!!.image3,
            viewPatternOverlayBinding!!.image4,
            positionSelect = viewPatternOverlayBinding!!.numberLockView.getPass().length
        )
    }

    override fun onComple() {

        if (viewPatternOverlayBinding!!.numberLockView.getPass() == preferences?.getString(
                PassActivtiy.PASS_LOGIN
            )
        ) finish()
        else {
            viewPatternOverlayBinding!!.titlePass.text = "Vui Lòng nhập lại mật khẩu ?"
            viewPatternOverlayBinding!!.numberLockView.clearPass()

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