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

        initializationRecyclerView(false)
        initializationBottomMenu()
        launchersPack()
    }

    var delMode: Boolean = false
    private fun initializationRecyclerView(deleteMode: Boolean) = with(bindingMainBinding) {
        recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
        recordAdapter = RecordAdapter(root.context, prefSpace)
        recordAdapter.deleteMode = deleteMode
        recyclerViewMain.adapter = recordAdapter

        val dataList = SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").takeData()
        if(dataList.isNotEmpty())
        {
            for(item in dataList) {
                recordAdapter.addRecordFuel(item)
            }
        }

        //recordAdapter.deleteMode = false
    }


    private fun initializationBottomMenu() = with(bindingMainBinding) {
        bottomNavViewMain.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemAdd -> {
                    initializationRecyclerView(false); delMode = false
                    editLauncher?.launch(Intent(this@MainActivity, EditActivity::class.java))
                }
                R.id.itemDelete -> {
                    if (delMode) {
                        delMode = false
                        initializationRecyclerView(false)
                    }
                    else {
                        delMode = true
                        initializationRecyclerView(true)
                        SomeTools(root.context).createSimpleDialog(this@MainActivity, getString(R.string.dialog_attention), getString(R.string.dialog_on_delete))
                    }
                }
                R.id.itemAllDelete -> {
                    if(SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").takeData().isNotEmpty())
                    {
                        SomeTools(root.context).createYesNoDialog(
                            this@MainActivity,
                            "Подтверждение",
                            "Вы точно хотите удалить все записи?",
                            onYesClicked = {
                                SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").deleteAllData()
                                recordAdapter.deleteAllRecordsFuel()
                            },
                            onNoClicked = {
                                //NOTHING
                            })
                    }
                }
                R.id.itemExport -> {
                    val exportString = SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").exportDataString(root.context)
                    if(!exportString.isNullOrEmpty()) {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, exportString)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
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
                SharedPrefTools(prefSpace, Constance.NAME_OBJECT_SHARED_PREF_FUELRECORD, "").addData(item)
            }
        }
    }
}