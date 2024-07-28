package com.mobdeve.s11.group07.mco3.wanderfriend

import android.os.Parcel
import android.os.Parcelable

data class Country(val id: Long, val name: Name, val flags: Flags) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readParcelable(Name::class.java.classLoader)!!,
        parcel.readParcelable(Flags::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(name, flags)
        parcel.writeParcelable(this.flags, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}

data class Name(val common: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(common)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Name> {
        override fun createFromParcel(parcel: Parcel): Name {
            return Name(parcel)
        }

        override fun newArray(size: Int): Array<Name?> {
            return arrayOfNulls(size)
        }
    }
}

data class Flags(val png: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(png)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flags> {
        override fun createFromParcel(parcel: Parcel): Flags {
            return Flags(parcel)
        }

        override fun newArray(size: Int): Array<Flags?> {
            return arrayOfNulls(size)
        }
    }
}
