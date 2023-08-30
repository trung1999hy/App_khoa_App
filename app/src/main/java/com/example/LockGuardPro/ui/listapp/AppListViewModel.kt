package com.example.LockGuardPro.ui.listapp

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import com.example.LockGuardPro.base.BaseViewModel
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AppListViewModel() : BaseViewModel() {
    private val repository = Repository()

    private fun getAllInstalledApps(
        context: Context,
        packageManager: PackageManager,
        packageName: String?
    ): ArrayList<ApplicationInfo> {
        val installedApps: ArrayList<ApplicationInfo> = ArrayList()
        try {
            val appList = packageManager.getInstalledApplications(0)
            for (appInfo in appList) {
                if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null && appInfo.packageName != packageName) {
                    installedApps.add(appInfo)
                }
            }
            return installedApps
        } catch (e: Throwable) {
            return arrayListOf()
        }
    }

    fun getAll(): LiveData<List<Lock>> = repository.getAll()

    fun addList(context: Context, packageManager: PackageManager, packageName: String?) {
        scope.launch(Dispatchers.IO) {
            try {
                val getAllApp = async {
                    getAllInstalledApps(context, packageManager, packageName)
                }
                val allAppLocal = async {
                    repository.getAllService()
                }
                var getAllAppLock = allAppLocal.await()
                repository.getAllService()
                var listadd = getAllApp.await()
                val listApp = ArrayList<Lock>()
                listadd.forEach { appInfo ->
                    val packageName = appInfo.packageName
                    val appName = appInfo.loadLabel(packageManager).toString()
                    if (getAllAppLock?.filter { appLock -> appLock.packetName == appInfo.packageName }
                            ?.isEmpty() == true || getAllAppLock.isEmpty())
                        listApp.add(
                            Lock(
                                packetName = packageName,
                                isLock = false,
                                appName = appName,
                                pass = null,
                                typePass = null
                            )
                        )
                }
                repository.addList(listApp)
            } catch (e: Exception) {

            }
        }
    }

    fun addAppLock(lock: Lock) {
        scope.launch(Dispatchers.IO) {
            async {
                repository.update(lock)
            }.await()

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