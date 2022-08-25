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
import com.sih.eldify.medicines.R
import com.sih.eldify.medicines.report.MonthlyReportActivity
import com.sih.eldify.medicines.utils.ActivityUtils
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MedicineActivity : AppCompatActivity() {
    @BindView(R.id.compactcalendar_view)
    var mCompactCalendarView: CompactCalendarView? = null

    @BindView(R.id.date_picker_text_view)
    var datePickerTextView: TextView? = null

    @BindView(R.id.date_picker_button)
    var datePickerButton: RelativeLayout? = null

    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @BindView(R.id.collapsingToolbarLayout)
    var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    @BindView(R.id.app_bar_layout)
    var appBarLayout: AppBarLayout? = null

    @BindView(R.id.contentFrame)
    var contentFrame: FrameLayout? = null

    @BindView(R.id.fab_add_task)
    var fabAddTask: FloatingActionButton? = null

    @BindView(R.id.coordinatorLayout)
    var coordinatorLayout: CoordinatorLayout? = null

    @BindView(R.id.date_picker_arrow)
    var arrow: ImageView? = null
    private var presenter: MedicinePresenter? = null
    private val dateFormat = SimpleDateFormat("MMM dd",  /*Locale.getDefault()*/Locale.ENGLISH)
    private var isExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        mCompactCalendarView!!.setLocale(
            TimeZone.getDefault(),  /*Locale.getDefault()*/
            Locale.ENGLISH
        )
        mCompactCalendarView!!.setShouldDrawDaysHeader(true)
        mCompactCalendarView!!.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                setSubtitle(dateFormat.format(dateClicked))
                val calendar = Calendar.getInstance()
                calendar.time = dateClicked
                val day = calendar[Calendar.DAY_OF_WEEK]
                if (isExpanded) {
                    ViewCompat.animate(arrow!!).rotation(0f).start()
                } else {
                    ViewCompat.animate(arrow!!).rotation(180f).start()
                }
                isExpanded = !isExpanded
                appBarLayout!!.setExpanded(isExpanded, true)
                presenter.reload(day)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth))
            }
        })
        setCurrentDate(Date())
        var medicineFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as MedicineFragment?
        if (medicineFragment == null) {
            medicineFragment = MedicineFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                medicineFragment,
                R.id.contentFrame
            )
        }

        //Create MedicinePresenter
        presenter = MedicinePresenter(
            Injection.provideMedicineRepository(this@MedicineActivity),
            medicineFragment
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.medicine_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_stats) {
            val intent = Intent(this, MonthlyReportActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setCurrentDate(date: Date?) {
        setSubtitle(dateFormat.format(date))
        mCompactCalendarView!!.setCurrentDate(date)
    }

    fun setSubtitle(subtitle: String?) {
        datePickerTextView!!.text = subtitle
    }

    @OnClick(R.id.date_picker_button)
    fun onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(arrow!!).rotation(0f).start()
        } else {
            ViewCompat.animate(arrow!!).rotation(180f).start()
        }
        isExpanded = !isExpanded
        appBarLayout!!.setExpanded(isExpanded, true)
    }
}