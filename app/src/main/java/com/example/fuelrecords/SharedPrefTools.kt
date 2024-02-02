package com.example.fuelrecords

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPrefTools(private val pref: SharedPreferences?) {
    @SuppressLint("CommitPrefEdits")
    public fun addData(record: FuelRecord): Boolean {
        val dataListJson = pref?.getString(Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "")

        var result: Boolean = false
        val gson = Gson()
        if (dataListJson!!.isNotEmpty()) {
            val dataList = gson.fromJson(dataListJson, Array<FuelRecord>::class.java).toMutableList()
            dataList.add(record)

            val updatedDataListJson = gson.toJson(dataList)
            val editor = pref?.edit()
            editor?.putString(Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, updatedDataListJson)
            editor?.apply()

            result = true
        }
        else {
            val dataList = mutableListOf<FuelRecord>()
            dataList.add(record)

            val updatedDataListJson = gson.toJson(dataList)
            val editor = pref?.edit()
            editor?.putString(Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, updatedDataListJson)
            editor?.apply()

            result = true
        }

        return result
    }

    public fun deleteData() {

    }

    public fun deleteAllData() {
        pref?.edit()?.remove(Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD)?.apply()
    }

    public fun takeData(): MutableList<FuelRecord> {
        val dataListJson = pref?.getString(Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "")

        val resultList: MutableList<FuelRecord>
        val gson = Gson()
        resultList = if (dataListJson!!.isNotEmpty()) gson.fromJson(dataListJson, Array<FuelRecord>::class.java).toMutableList()
        else mutableListOf()

        return resultList
    }
}