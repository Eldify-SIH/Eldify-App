package com.sih.eldify.medicines.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife


/**
 * Created by gautam on 13/07/17.
 */
class HistoryAdapter internal constructor(historyList: List<History>) :
    RecyclerView.Adapter<com.sih.eldify.medicines.report.HistoryAdapter.HistoryViewHolder>() {
    private var mHistoryList: List<History>? = null
    fun replaceData(tasks: List<History>) {
        setList(tasks)
        notifyDataSetChanged()
    }

    private fun setList(historyList: List<History>) {
        mHistoryList = historyList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): com.sih.eldify.medicines.report.HistoryAdapter.HistoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return com.sih.eldify.medicines.report.HistoryAdapter.HistoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: com.sih.eldify.medicines.report.HistoryAdapter.HistoryViewHolder,
        position: Int
    ) {
        val history: History = mHistoryList!![position] ?: return
        holder.tvMedDate.setText(history.getFormattedDate())
        setMedicineAction(holder, history.getAction())
        holder.tvMedicineName.setText(history.getPillName())
        holder.tvDoseDetails.setText(history.getFormattedDose())
    }

    private fun setMedicineAction(
        holder: com.sih.eldify.medicines.report.HistoryAdapter.HistoryViewHolder,
        action: Int
    ) {
        when (action) {
            0 -> holder.ivMedicineAction.setVisibility(View.GONE)
            1 -> {
                holder.ivMedicineAction.setVisibility(View.VISIBLE)
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_taken)
            }
            2 -> {
                holder.ivMedicineAction.setImageResource(R.drawable.image_reminder_not_taken)
                holder.ivMedicineAction.setVisibility(View.VISIBLE)
            }
            else -> holder.ivMedicineAction.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return if (mHistoryList != null && !mHistoryList!!.isEmpty()) mHistoryList!!.size else 0
    }

    internal class HistoryViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        @BindView(R.id.tv_med_date)
        var tvMedDate: RobotoBoldTextView? = null

        @BindView(R.id.tv_medicine_name)
        var tvMedicineName: RobotoBoldTextView? = null

        @BindView(R.id.tv_dose_details)
        var tvDoseDetails: RobotoRegularTextView? = null

        @BindView(R.id.iv_medicine_action)
        var ivMedicineAction: ImageView? = null

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    init {
        setList(historyList)
    }
}