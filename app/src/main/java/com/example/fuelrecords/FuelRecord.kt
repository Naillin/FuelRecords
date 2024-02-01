package com.example.fuelrecords

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.Calendar

data class FuelRecord(val recordDate: Calendar,
                      val refuelingDate: Calendar,
                      val litersOfGasoline: Double,
                      val cost: Double,
                      val typeOfGasoline: Int,
                      val totalMileage: Double,
                      val mileage: Double,
                      val description: String?
) : Serializable

//data class FuelRecord(val recordDate: Calendar,
//                      val refuelingDate: Calendar,
//                      val litersOfGasoline: Double,
//                      val cost: Double,
//                      val typeOfGasoline: Int,
//                      val totalMileage: Double,
//                      val mileage: Double,
//                      val description: String?
//) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        (parcel.readSerializable() as? Calendar)!!,
//        (parcel.readSerializable() as? Calendar)!!,
//        parcel.readDouble(),
//        parcel.readDouble(),
//        parcel.readInt(),
//        parcel.readDouble(),
//        parcel.readDouble(),
//        parcel.readString()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeSerializable(recordDate)
//        parcel.writeSerializable(refuelingDate)
//        parcel.writeDouble(litersOfGasoline)
//        parcel.writeDouble(cost)
//        parcel.writeInt(typeOfGasoline)
//        parcel.writeDouble(totalMileage)
//        parcel.writeDouble(mileage)
//        parcel.writeString(description)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<FuelRecord> {
//        override fun createFromParcel(parcel: Parcel): FuelRecord {
//            return FuelRecord(parcel)
//        }
//
//        override fun newArray(size: Int): Array<FuelRecord?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
