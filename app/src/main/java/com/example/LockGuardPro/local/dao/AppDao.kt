package com.example.LockGuardPro.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.LockGuardPro.model.Lock

@Dao
interface AppDao {
    @Query("SELECT * FROM Lock")
    fun getAll(): LiveData<List<Lock>>
    @Query("SELECT * FROM Lock")
    fun getAllSever(): List<Lock>
    @Insert
    suspend fun insertAll(vararg users: Lock)

    @Insert
    suspend fun insetList(list: ArrayList<Lock>)

    @Update
    suspend fun update(lock: Lock)

    @Delete
    suspend fun delete(user: Lock)
}