package com.sih.eldify.medicines.report

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.sih.eldify.R
import com.sih.eldify.medicines.Injection
import com.sih.eldify.medicines.utils.ActivityUtils

class MonthlyReportActivity : AppCompatActivity() {
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null
    private var presenter: MonthlyReportPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_report)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        //Create Fragment
        var monthlyReportFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as MonthlyReportFragment?
        if (monthlyReportFragment == null) {
            monthlyReportFragment = MonthlyReportFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                monthlyReportFragment,
                R.id.contentFrame
            )
        }

        //Create TaskPresenter
        presenter = MonthlyReportPresenter(
            Injection.provideMedicineRepository(this@MonthlyReportActivity),
            monthlyReportFragment
        )

        //Load previous saved Instance
        if (savedInstanceState != null) {
            val taskFilterType =
                savedInstanceState.getSerializable(CURRENT_FILTERING_TYPE) as FilterType?
            presenter!!.setFiltering(taskFilterType)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(CURRENT_FILTERING_TYPE, presenter?.filterType)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val CURRENT_FILTERING_TYPE = "current_filtering_type"
    }
}