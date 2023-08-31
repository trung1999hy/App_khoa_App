package com.example.LockGuardPro.model

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
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


) : Parcelable {
    @Ignore
    var drawable: Drawable? = null

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(packetName)
        parcel.writeString(pass)
        parcel.writeByte(if (isLock) 1 else 0)
        parcel.writeString(appName)
        parcel.writeString(typePass)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lock> {
        override fun createFromParcel(parcel: Parcel): Lock {
            return Lock(parcel)
        }

        override fun newArray(size: Int): Array<Lock?> {
            return arrayOfNulls(size)
        }
    }
}


