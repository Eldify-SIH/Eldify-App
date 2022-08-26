package com.sih.eldify.medicines.alarm

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.sih.eldify.R
import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.medicine.MedicineActivity
import com.sih.eldify.medicines.views.RobotoBoldTextView
import com.sih.eldify.medicines.views.RobotoRegularTextView
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by gautam on 13/07/17.
 */
class ReminderFragment : Fragment(), ReminderContract.View {
    @BindView(R.id.tv_med_time)
    var tvMedTime: RobotoBoldTextView? = null

    @BindView(R.id.tv_medicine_name)
    var tvMedicineName: RobotoBoldTextView? = null

    @BindView(R.id.tv_dose_details)
    var tvDoseDetails: RobotoRegularTextView? = null

    @BindView(R.id.iv_ignore_med)
    var ivIgnoreMed: ImageView? = null

    @BindView(R.id.iv_take_med)
    var ivTakeMed: ImageView? = null

    @BindView(R.id.linearLayout)
    var linearLayout: LinearLayout? = null
    var unbinder: Unbinder? = null
    private var medicineAlarm: MedicineAlarm? = null
    private var id: Long = 0
    private var mMediaPlayer: MediaPlayer? = null
    private var mVibrator: Vibrator? = null
    private var presenter: ReminderContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = requireArguments().getLong(EXTRA_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_reminder, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun setPresenter(presenter: ReminderContract.Presenter?) {
        this.presenter = presenter
    }

    override fun showMedicine(medicineAlarm: MedicineAlarm?) {
        this.medicineAlarm = medicineAlarm
        mVibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 1000, 10000)
        mVibrator!!.vibrate(pattern, 0)
        mMediaPlayer = MediaPlayer.create(context, R.raw.cuco_sound)
        mMediaPlayer!!.isLooping = true
        mMediaPlayer!!.start()
        tvMedTime?.text = medicineAlarm!!.stringTime
        tvMedicineName?.text = medicineAlarm.pillName
        tvDoseDetails?.text = medicineAlarm.formattedDose
    }

    override fun showNoData() {
        //
    }

    override fun onResume() {
        super.onResume()
        presenter!!.onStart(id)
    }

    @OnClick(R.id.iv_take_med)
    fun onMedTakeClick() {
        onMedicineTaken()
        stopMedialPlayer()
        stopVibrator()
    }

    @OnClick(R.id.iv_ignore_med)
    fun onMedIgnoreClick() {
        onMedicineIgnored()
        stopMedialPlayer()
        stopVibrator()
    }

    private fun stopMedialPlayer() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
        }
    }

    private fun stopVibrator() {
        if (mVibrator != null) {
            mVibrator!!.cancel()
        }
    }

    private fun onMedicineTaken() {
        val history = History()
        val takeTime = Calendar.getInstance()
        val date = takeTime.time
        val dateString = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        val hour = takeTime[Calendar.HOUR_OF_DAY]
        val minute = takeTime[Calendar.MINUTE]
        val am_pm = if (hour < 12) "am" else "pm"
        history.hourTaken = hour
        history.minuteTaken = minute
        history.dateString = dateString
        history.pillName = medicineAlarm?.pillName
        history.action = 1
        history.doseQuantity = medicineAlarm?.doseQuantity
        history.doseUnit = medicineAlarm?.doseUnit
        presenter!!.addPillsToHistory(history)
        val stringMinute: String
        stringMinute = if (minute < 10) "0$minute" else "" + minute
        var nonMilitaryHour = hour % 12
        if (nonMilitaryHour == 0) nonMilitaryHour = 12
        Toast.makeText(
            context,
            medicineAlarm?.pillName
                .toString() + " was taken at " + nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".",
            Toast.LENGTH_SHORT
        ).show()
        val returnHistory = Intent(context, MedicineActivity::class.java)
        startActivity(returnHistory)
        requireActivity().finish()
    }

    private fun onMedicineIgnored() {
        val history = History()
        val takeTime = Calendar.getInstance()
        val date = takeTime.time
        val dateString = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        val hour = takeTime[Calendar.HOUR_OF_DAY]
        val minute = takeTime[Calendar.MINUTE]
        val am_pm = if (hour < 12) "am" else "pm"
        history.hourTaken = hour
        history.minuteTaken = minute
        history.dateString = dateString
        history.pillName = medicineAlarm?.pillName
        history.action = 2
        history.doseQuantity = medicineAlarm?.doseQuantity
        history.doseUnit = medicineAlarm?.doseUnit
        presenter!!.addPillsToHistory(history)
        val stringMinute: String
        stringMinute = if (minute < 10) "0$minute" else "" + minute
        var nonMilitaryHour = hour % 12
        if (nonMilitaryHour == 0) nonMilitaryHour = 12
        Toast.makeText(
            context,
            medicineAlarm?.pillName
                .toString() + " was ignored at " + nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".",
            Toast.LENGTH_SHORT
        ).show()
        val returnHistory = Intent(context, MedicineActivity::class.java)
        startActivity(returnHistory)
        requireActivity().finish()
    }

    override val isActive: Boolean
        get() = isAdded

    override fun onFinish() {
        stopMedialPlayer()
        stopVibrator()
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        fun newInstance(id: Long): ReminderFragment {
            val args = Bundle()
            args.putLong(EXTRA_ID, id)
            val fragment = ReminderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
