package com.example.fuelrecords

import android.annotation.SuppressLint
import android.content.Context
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

    public fun exportDataString(context: Context): String? {
        var result: String? = null

        val dataList = takeData()
        if(dataList.isNotEmpty())
        {
            result = ""
            var litersSum: Double = 0.0
            var costSum: Double = 0.0
            var totalMileageSum: Double = 0.0
            for (item in dataList) {
                item.apply {
                    val str = "Дата записи: ${recordDate.time}; Дата заправки: ${refuelingDate.time}; Количество безина: ${litersOfGasoline} л; Стоимость: ${cost} руб; Вид топлива: ${context.resources.getStringArray(R.array.typesOfGasolineList)[typeOfGasoline]}; Всего пройдено ${totalMileage} км; Последняя заправка ${mileage} км назад."
                    result = result + "\n" + str

                    costSum = costSum + cost
                    litersSum = litersSum + litersOfGasoline
                    totalMileageSum = totalMileageSum + totalMileage
                }
            }
            val strMain = "Общая сумма за отчет ${costSum} руб; Общее количество топлива за отчет ${litersSum} л; Общее пройденное расстояние за отчет ${totalMileageSum} км\n\n-------------------------------------------\n\n"
            result = strMain + result
        }

        return result
    }
}