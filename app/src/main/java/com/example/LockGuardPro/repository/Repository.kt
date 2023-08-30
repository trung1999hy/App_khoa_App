package com.example.LockGuardPro.repository

import androidx.lifecycle.LiveData
import com.example.LockGuardPro.App
import com.example.LockGuardPro.local.AppDatabase
import com.example.LockGuardPro.model.Lock

class Repository(){
    private val appDatabase = AppDatabase.getInstance(App.newInstance().applicationContext)

     fun getAll(): LiveData<List<Lock>> = appDatabase.getAppLockDao().getAll()
    suspend fun add(lock : Lock) = appDatabase.getAppLockDao().insertAll(lock)
    suspend fun addList(list: ArrayList<Lock>) = appDatabase.getAppLockDao().insetList(list)
    suspend fun remove(lock: Lock) = appDatabase.getAppLockDao().delete(lock)
    suspend fun update(lock: Lock) = appDatabase.getAppLockDao().update(lock)
    suspend fun getAllService() = appDatabase.getAppLockDao().getAllSever()

}