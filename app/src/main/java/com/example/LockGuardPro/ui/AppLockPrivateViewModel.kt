package com.example.LockGuardPro.ui

import androidx.lifecycle.viewModelScope
import com.example.LockGuardPro.base.BaseViewModel
import com.example.LockGuardPro.model.Lock
import com.example.LockGuardPro.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppLockPrivateViewModel : BaseViewModel() {
    private val response: Repository = Repository()
    fun update(lock: Lock, onSuccessors: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                async {
                    response.update(lock)
                }.await()
                withContext(Dispatchers.Main) {
                    onSuccessors.invoke()
                }

            } catch (e: Throwable) {

            }
        }
    }
}