package com.sih.eldify.medicines.medicine

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.sih.eldify.medicines.Injection
import com.sih.eldify.R
import com.sih.eldify.medicines.report.MonthlyReportActivity
import com.sih.eldify.medicines.utils.ActivityUtils
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_medicine.*
import java.text.SimpleDateFormat
import java.util.*

class MedicineActivity : AppCompatActivity() {



    private var presenter: MedicinePresenter? = null
    private val dateFormat = SimpleDateFormat("MMM dd",  /*Locale.getDefault()*/Locale.ENGLISH)
    private var isExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)
        ButterKnife.bind(this)
        setSupportActionBar(med_am_toolbar)
        med_am_compactcalendar_view!!.setLocale(
            TimeZone.getDefault(),  /*Locale.getDefault()*/
            Locale.ENGLISH
        )
        med_am_compactcalendar_view!!.setShouldDrawDaysHeader(true)
        med_am_compactcalendar_view!!.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                setSubtitle(dateFormat.format(dateClicked))
                val calendar = Calendar.getInstance()
                calendar.time = dateClicked
                val day = calendar[Calendar.DAY_OF_WEEK]
                if (isExpanded) {
                    ViewCompat.animate(med_am_date_picker_arrow!!).rotation(0f).start()
                } else {
                    ViewCompat.animate(med_am_date_picker_arrow!!).rotation(180f).start()
                }
                isExpanded = !isExpanded
                med_am_app_bar_layout!!.setExpanded(isExpanded, true)
                presenter!!.reload(day)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth))
            }
        })
        setCurrentDate(Date())
        var medicineFragment =
            supportFragmentManager.findFragmentById(R.id.med_am_contentFrame) as MedicineFragment?
        if (medicineFragment == null) {
            medicineFragment = MedicineFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                medicineFragment,
                R.id.med_am_contentFrame
            )
        }

        //Create MedicinePresenter
        presenter = MedicinePresenter(
            Injection.provideMedicineRepository(this@MedicineActivity),
            medicineFragment
        )

        med_am_date_picker_button.setOnClickListener{
            onDatePickerButtonClicked()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.medicine_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.med_menu_stats) {
            val intent = Intent(this, MonthlyReportActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setCurrentDate(date: Date?) {
        setSubtitle(dateFormat.format(date))
        med_am_compactcalendar_view!!.setCurrentDate(date)
    }

    fun setSubtitle(subtitle: String?) {
        med_am_date_picker_text_view!!.text = subtitle
    }

    fun onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(med_am_date_picker_arrow!!).rotation(0f).start()
        } else {
            ViewCompat.animate(med_am_date_picker_arrow!!).rotation(180f).start()
        }
        isExpanded = !isExpanded
        med_am_app_bar_layout!!.setExpanded(isExpanded, true)
    }
}