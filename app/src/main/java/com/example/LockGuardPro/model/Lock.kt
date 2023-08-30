package com.example.LockGuardPro.model

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Lock(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "packetName")
    val packetName: String?,
    @ColumnInfo(name = "pass")
    var pass: String?,
    @ColumnInfo(name = "lock")
    var isLock: Boolean = false,
    @ColumnInfo(name = "app_name")
    var appName: String?,
    @ColumnInfo(name = "type_pass")
    var typePass: String?


) : Serializable {
    @Ignore
    var drawable: Drawable? = null
}


