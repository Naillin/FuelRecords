package com.example.fuelrecords

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fuelrecords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMainBinding: ActivityMainBinding
    private lateinit var recordAdapter: RecordAdapter
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    var prefSpace: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMainBinding.root)

        prefSpace = getSharedPreferences(Constance.NAME_SECTOR_SHARED_PREF_FUELRECORD, Context.MODE_PRIVATE)

        initializationRecyclerView()
        initializationBottomMenu()
        launchersPack()
    }

    //СДЕЛАТЬ СОХРАНЕНИЕ В Sharing prefetrresced
    //И УДАЛЕНИЕ

    private fun initializationRecyclerView() = with(bindingMainBinding) {
        recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
        recordAdapter = RecordAdapter(root.context)
        recyclerViewMain.adapter = recordAdapter

        val dataList = SharedPrefTools(prefSpace).takeData()
        if(dataList.isNotEmpty())
        {
            for(item in dataList) {
                recordAdapter.addRecordFuel(item)
            }
        }
    }

    private fun initializationBottomMenu() = with(bindingMainBinding) {
        bottomNavViewMain.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemAdd -> {
                    editLauncher?.launch(Intent(this@MainActivity, EditActivity::class.java))
                }
                R.id.itemDelete -> {

                }
                R.id.itemAllDelete -> {
                    if(SharedPrefTools(prefSpace).takeData().isNotEmpty())
                    {
                        SomeTools(root.context).createYesNoDialog(
                            this@MainActivity,
                            "Подтверждение",
                            "Вы точно хотите удалить все записи?",
                            onYesClicked = {
                                SharedPrefTools(prefSpace).deleteAllData()
                                recordAdapter.deleteAllRecordsFuel()
                            },
                            onNoClicked = {
                                //NOTHING
                            })
                    }
                }
            }
            true
        }
    }

    private fun launchersPack() {
        //возвращение ответа из edit activity
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                val item = ((it.data?.getSerializableExtra(Constance.CODE_EDIT_LAUNCHER) as? FuelRecord)!!)
                recordAdapter.addRecordFuel(item)
                SharedPrefTools(prefSpace).addData(item)
            }
        }
    }
}