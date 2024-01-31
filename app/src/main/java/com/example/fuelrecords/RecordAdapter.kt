package com.example.fuelrecords

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelrecords.databinding.RecordItemBinding

class RecordAdapter(private val context: Context): RecyclerView.Adapter<RecordAdapter.RecordHolder>() {
    val recordList = ArrayList<FuelRecord>()
    inner class RecordHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = RecordItemBinding.bind(view)
        fun bind(record: FuelRecord) = with(binding) {
            textViewRecordDate.text = record.recordDate.toString() //возможно преобразовать в тип 12 февраля 2023 года
            val strRefuelingDate = context.resources.getString(R.string.text_date_refil) + record.refuelingDate.toString() //возможно преобразовать в тип 12 февраля 2023 года
            textViewRefuelingDate.text = strRefuelingDate
            val strLitersCost = "${record.litersOfGasoline} литра за ${record.cost} рубля"
            textViewLitersCost.text = strLitersCost
            val strTypeGas = context.resources.getString(R.string.text_type_gas) + context.resources.getStringArray(R.array.typesOfGasolineList)[record.typeOfGasoline].toString()
            textViewTypeOfGasoline.text = strTypeGas
            val strMileage = "Всего пройдено ${record.totalMileage} километров\n${record.mileage} километра последняя заправка"
            textViewMileage.text = strMileage

            textViewDescription.visibility = if(record.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            textViewDescription.text = record.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        val viewParent = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)

//        viewParent.setOnClickListener {
//
//        }

        return RecordHolder(viewParent)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        holder.bind(recordList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRecordFuel(record: FuelRecord) {
        recordList.add(record)
        notifyDataSetChanged()
    }
}