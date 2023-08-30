package com.example.LockGuardPro.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.LockGuardPro.base.BaseViewModel
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockAppViewModel : BaseViewModel() {
    private val repository = Repository()

    private val _listLock: MutableLiveData<List<Lock>> = MutableLiveData()
    val listLock: LiveData<List<Lock>> = _listLock

    fun getAll(): LiveData<List<Lock>> = repository.getAll()

    fun removeAppLock(lock: Lock, onCallBack: () -> Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                async {
                    repository.update(lock)
                }.await()
                withContext(Dispatchers.Main) {
                    onCallBack.invoke()
                }

            } catch (e: Exception) {
                //
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