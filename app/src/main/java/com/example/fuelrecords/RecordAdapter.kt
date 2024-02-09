package com.example.fuelrecords

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuelrecords.databinding.RecordItemBinding

class RecordAdapter(private val context: Context, private val prefSpace: SharedPreferences?): RecyclerView.Adapter<RecordAdapter.RecordHolder>() {
    private val recordList = ArrayList<FuelRecord>()
    var deleteMode: Boolean = false

    inner class RecordHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = RecordItemBinding.bind(view)
        fun bind(record: FuelRecord) = with(binding) {
            textViewRecordDate.text = record.recordDate.time.toString() //возможно преобразовать в тип 12 февраля 2023 года
            val strRefuelingDate = context.resources.getString(R.string.text_date_refil) + " " + record.refuelingDate.time.toString() //возможно преобразовать в тип 12 февраля 2023 года
            textViewRefuelingDate.text = strRefuelingDate
            val strLitersCost = "${record.litersOfGasoline} л за ${record.cost} руб"
            textViewLitersCost.text = strLitersCost
            val strTypeGas = context.resources.getString(R.string.text_type_gas) + " " + context.resources.getStringArray(R.array.typesOfGasolineList)[record.typeOfGasoline].toString()
            textViewTypeOfGasoline.text = strTypeGas
            val strMileage = "Всего пройдено ${record.totalMileage} км\nПоследняя заправка ${record.mileage} км назад"
            textViewMileage.text = strMileage

            textViewDescription.visibility = if(record.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            textViewDescription.text = record.description

            cardViewDelete.visibility = if(deleteMode) View.VISIBLE else View.GONE
            imageButtonDelete.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").deleteData(adapterPosition)
                    deleteRecordFuel(adapterPosition)
                }
            }
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
    public fun addRecordFuel(record: FuelRecord) {
        recordList.add(record)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun deleteAllRecordsFuel() {
        recordList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun deleteRecordFuel(index: Int) {
        recordList.removeAt(index)
        notifyDataSetChanged()
    }
}