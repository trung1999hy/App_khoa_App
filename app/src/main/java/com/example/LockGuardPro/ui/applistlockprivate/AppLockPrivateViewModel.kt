package com.example.LockGuardPro.ui.applistlockprivate

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.LockGuardPro.base.BaseViewModel
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AppLockPrivateViewModel : BaseViewModel() {
    private val repository = Repository()

    fun getAll(): LiveData<List<Lock>> = repository.getAll()
    fun updateAppLock(lock: Lock) {
        scope.launch(Dispatchers.IO) {
            async {
                repository.update(lock)
            }.await()

        }
    }
    fun update(lock: Lock, onSuccessors: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                async {
                    repository.update(lock)
                }.await()
                onSuccessors.invoke()
            } catch (e: Throwable) {

            }
        }
    }

    fun getAppIconByPackageName(context: Context, packageName: String?): Drawable? {
        try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName!!, 0)
            return appInfo.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null

    }


}