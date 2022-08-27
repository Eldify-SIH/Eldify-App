package com.sih.eldify.medicines.medicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sih.eldify.R

import com.sih.eldify.medicines.data.source.MedicineAlarm
import com.sih.eldify.medicines.views.RobotoBoldTextView
import com.sih.eldify.medicines.views.RobotoRegularTextView

/**
 * Created by gautam on 13/07/17.
 */
class MedicineAdapter(medicineAlarmList: List<MedicineAlarm>?) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
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
    ): MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MedicineViewHolder,
        position: Int
    ) {
        val medicineAlarm: MedicineAlarm = medicineAlarmList!![position] ?: return
        holder.tvMedTime!!.setText(medicineAlarm.stringTime)
        holder.tvMedicineName!!.setText(medicineAlarm.pillName)
        holder.tvDoseDetails!!.setText(medicineAlarm.formattedDose)
        holder.ivAlarmDelete!!.setVisibility(View.VISIBLE)
        holder.ivAlarmDelete!!.setOnClickListener(View.OnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onMedicineDeleteClicked(medicineAlarm)
            }
        })
    }

    override fun getItemCount(): Int {
        return if (medicineAlarmList != null && !medicineAlarmList!!.isEmpty()) medicineAlarmList!!.size else 0
    }

    class MedicineViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        var tvMedTime: RobotoBoldTextView? = itemView?.findViewById(R.id.med_fr_tv_med_time)

        var tvMedicineName: RobotoBoldTextView? = itemView?.findViewById(R.id.med_fr_tv_medicine_name)

        var tvDoseDetails: RobotoRegularTextView? = itemView?.findViewById(R.id.med_fr_tv_dose_details)

        var ivAlarmDelete: ImageView? = itemView?.findViewById(R.id.med_rm_iv_alarm_delete)

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