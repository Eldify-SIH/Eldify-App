package com.sih.eldify.medicines.medicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

import com.sih.eldify.medicines.R
import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.views.RobotoBoldTextView
import com.sih.eldify.medicines.views.RobotoRegularTextView

/**
 * Created by gautam on 13/07/17.
 */
class MedicineAdapter(medicineAlarmList: List<MedicineAlarm>?) :
    RecyclerView.Adapter<com.sih.eldify.medicines.medicine.MedicineAdapter.MedicineViewHolder>() {
    private var medicineAlarmList: List<MedicineAlarm>?
    private var onItemClickListener: OnItemClickListener? = null
    fun replaceData(medicineAlarmList: List<MedicineAlarm>?) {
        this.medicineAlarmList = medicineAlarmList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): com.sih.eldify.medicines.medicine.MedicineAdapter.MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_medicine, parent, false)
        return com.sih.eldify.medicines.medicine.MedicineAdapter.MedicineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: com.sih.eldify.medicines.medicine.MedicineAdapter.MedicineViewHolder,
        position: Int
    ) {
        val medicineAlarm: MedicineAlarm = medicineAlarmList!![position] ?: return
        holder.tvMedTime.setText(medicineAlarm.getStringTime())
        holder.tvMedicineName.setText(medicineAlarm.getPillName())
        holder.tvDoseDetails.setText(medicineAlarm.getFormattedDose())
        holder.ivAlarmDelete.setVisibility(View.VISIBLE)
        holder.ivAlarmDelete.setOnClickListener(View.OnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onMedicineDeleteClicked(medicineAlarm)
            }
        })
    }

    override fun getItemCount(): Int {
        return if (medicineAlarmList != null && !medicineAlarmList!!.isEmpty()) medicineAlarmList!!.size else 0
    }

    internal class MedicineViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        @BindView(R.id.tv_med_time)
        var tvMedTime: RobotoBoldTextView? = null

        @BindView(R.id.tv_medicine_name)
        var tvMedicineName: RobotoBoldTextView? = null

        @BindView(R.id.tv_dose_details)
        var tvDoseDetails: RobotoRegularTextView? = null

        @BindView(R.id.iv_medicine_action)
        var ivMedicineAction: ImageView? = null

        @BindView(R.id.iv_alarm_delete)
        var ivAlarmDelete: ImageView? = null

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    interface OnItemClickListener {
        fun onMedicineDeleteClicked(medicineAlarm: MedicineAlarm?)
    }

    init {
        this.medicineAlarmList = medicineAlarmList
    }
}