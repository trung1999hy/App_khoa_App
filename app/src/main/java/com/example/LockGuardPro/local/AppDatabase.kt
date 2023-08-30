package com.example.LockGuardPro.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.LockGuardPro.local.dao.AppDao
import com.example.LockGuardPro.model.Lock
import java.util.concurrent.Executors

@Database(
    entities = [Lock::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppLockDao(): AppDao
    val databaseWriteExecutor = Executors.newFixedThreadPool(2)


    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}