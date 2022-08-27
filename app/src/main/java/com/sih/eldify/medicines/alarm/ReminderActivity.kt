package com.sih.eldify.medicines.alarm

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.sih.eldify.R
import com.sih.eldify.medicines.Injection
import com.sih.eldify.medicines.utils.ActivityUtils
import kotlinx.android.synthetic.main.activity_reminder_actvity.*


class ReminderActivity : AppCompatActivity() {

    var mReminderPresenter: ReminderPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_actvity)
        ButterKnife.bind(this)
        setSupportActionBar(med_ra_toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        if (!intent.hasExtra(ReminderFragment.EXTRA_ID)) {
            finish()
            return
        }
        val id = intent.getLongExtra(ReminderFragment.EXTRA_ID, 0)
        var reminderFragment =
            supportFragmentManager.findFragmentById(R.id.med_ra_contentFrame) as ReminderFragment?
        if (reminderFragment == null) {
            reminderFragment = ReminderFragment.newInstance(id)
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                reminderFragment,
                R.id.med_ra_contentFrame
            )
        }

        //Create MedicinePresenter
        mReminderPresenter = ReminderPresenter(
            Injection.provideMedicineRepository(this@ReminderActivity),
            reminderFragment
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (mReminderPresenter != null) {
                mReminderPresenter!!.finishActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mReminderPresenter != null) {
            mReminderPresenter!!.finishActivity()
        }
    }
}