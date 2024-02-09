package com.example.fuelrecords

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.fuelrecords.databinding.ActivityEditBinding
import java.util.Calendar
import java.util.Date

class EditActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bindingEditBinding: ActivityEditBinding
    var prefSpace: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingEditBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bindingEditBinding.root)

        //сжатие активити для клавиатуры
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        bindingEditBinding.apply {
            val adapter = ArrayAdapter(this@EditActivity, android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.typesOfGasolineList))
            spinnerTypeOfGasoline.adapter = adapter

            spinnerTypeOfGasoline.onItemSelectedListener = this@EditActivity

            calendarViewRefuelingDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendarRefeling.set(year, month, dayOfMonth)
            }
        }

        prefSpace = getSharedPreferences(Constance.NAME_SECTOR_SHARED_PREF_FUELRECORD, Context.MODE_PRIVATE)
        textinputTotalMileageDoAfterTextChanged()
    }

    private var numberTypeGas: Int = 0
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Получите выбранный элемент из адаптера Spinner
        //val selectedItem = parent?.getItemAtPosition(position).toString()
        numberTypeGas = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun textinputTotalMileageDoAfterTextChanged() = with(bindingEditBinding) {
        val lastItem = SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").takeData().lastOrNull()
        val lastTotalMileage = lastItem?.totalMileage

        textinputTotalMileage.doAfterTextChanged {
            val difference = Math.round((textinputTotalMileage.text.toString().toDoubleOrNull() ?: 0.0) - (lastTotalMileage ?: 0.0)) / 100.0
            val strMileage = if(difference > 0.0) difference.toString() else ""
            textinputMileage.setText(strMileage)
        }
    }

    val calendarRefeling = Calendar.getInstance()
    fun buttonAddOnClick(view: View) = with(bindingEditBinding) {
        val calendarСurrent = Calendar.getInstance(); calendarСurrent.time = Date()
        //val calendarRefeling = Calendar.getInstance(); calendarRefeling.timeInMillis = calendarViewRefuelingDate.date //Почему то устанавливается дата сегодня

        val record: FuelRecord = FuelRecord(
            recordDate = calendarСurrent,
            refuelingDate = calendarRefeling,
            litersOfGasoline = textinputLitersOfGasoline.text.toString().toDoubleOrNull() ?: 0.0,
            cost = textinputCost.text.toString().toDoubleOrNull() ?: 0.0,
            typeOfGasoline = spinnerTypeOfGasoline.selectedItemPosition,
            totalMileage = textinputTotalMileage.text.toString().toDoubleOrNull() ?: 0.0,
            mileage = textinputMileage.text.toString().toDoubleOrNull() ?: 0.0,
            description = editTextDescription.text.toString()
        )

        val editIntent: Intent = Intent().apply {
            putExtra(Constance.CODE_EDIT_LAUNCHER, record)
        }
        setResult(RESULT_OK, editIntent)
        finish()
    }

    fun buttonCancelOnClick(view: View) {
        setResult(RESULT_CANCELED)
        finish()
    }
}