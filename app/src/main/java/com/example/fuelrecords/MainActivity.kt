package com.example.fuelrecords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fuelrecords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMainBinding: ActivityMainBinding
    private lateinit var recordAdapter: RecordAdapter
    private var editLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMainBinding.root)

        initializationrecyclerView()
        initializationBottomMenu()
        launchersPack()
    }

    //СДЕЛАТЬ СОХРАНЕНИЕ В Sharing prefetrresced
    //И УДАЛЕНИЕ

    private fun initializationrecyclerView() = with(bindingMainBinding) {
        recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
        recordAdapter = RecordAdapter(root.context)
        recyclerViewMain.adapter = recordAdapter
    }

    private fun initializationBottomMenu() = with(bindingMainBinding) {
        bottomNavViewMain.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemAdd -> {
                    editLauncher?.launch(Intent(this@MainActivity, EditActivity::class.java))
                }

                R.id.itemDelete -> {

                }
            }
            true
        }
    }

    private fun launchersPack() {
        //возвращение ответа из edit activity
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                recordAdapter.addRecordFuel(((it.data?.getSerializableExtra(Constance.CODE_EDIT_LAUNCHER) as? FuelRecord)!!))
            }
        }
    }
}