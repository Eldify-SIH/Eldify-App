package com.sih.eldify.medicines.addmedicine

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sih.eldify.R
import com.sih.eldify.medicines.Injection
import com.sih.eldify.medicines.utils.ActivityUtils
import com.sih.eldify.mediciness.addmedicine.AddMedicineFragment
import com.sih.eldify.mediciness.addmedicine.AddMedicinePresenter

class AddMedicinesActivity : AppCompatActivity() {
    object addMedsActObj {
        val REQUEST_ADD_TASK = 1
        val EXTRA_TASK_ID = "task_extra_id"
        val EXTRA_TASK_NAME = "task_extra_name"
    }

    val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"

    private var mAddMedicinePresenter: AddMedicinePresenter? = null

    private var mActionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicines)

        //Setup toolbar
        val toolbar = findViewById<View>(R.id.med_aam_toolbar_aam) as Toolbar
        setSupportActionBar(toolbar)
        mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        mActionBar!!.setDisplayShowHomeEnabled(true)
        var addMedicineFragment: AddMedicineFragment? =
            supportFragmentManager.findFragmentById(R.id.med_aam_contentFrame_aam) as AddMedicineFragment?
        val medId = intent.getIntExtra(AddMedicineFragment.ARGUMENT_EDIT_MEDICINE_ID, 0)
        val medName = intent.getStringExtra(AddMedicineFragment.ARGUMENT_EDIT_MEDICINE_NAME)
        setToolbarTitle(medName)
        if (addMedicineFragment == null) {
            addMedicineFragment = AddMedicineFragment.newInstance()
            if (intent.hasExtra(AddMedicineFragment.ARGUMENT_EDIT_MEDICINE_ID)) {
                val bundle = Bundle()
                bundle.putInt(AddMedicineFragment.ARGUMENT_EDIT_MEDICINE_ID, medId)
                addMedicineFragment.setArguments(bundle)
            }
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                addMedicineFragment, R.id.med_aam_contentFrame_aam
            )
        }
        var shouldLoadDataFromRepo = true
        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY)
        }

//        // Create the presenter
        mAddMedicinePresenter = AddMedicinePresenter(
            medId,
            Injection.provideMedicineRepository(applicationContext),
            addMedicineFragment,
            shouldLoadDataFromRepo
        )
    }

    fun setToolbarTitle(medicineName: String?) {
        if (medicineName == null) {
            mActionBar!!.setTitle(getString(R.string.new_medicine))
        } else {
            mActionBar!!.setTitle(medicineName)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}