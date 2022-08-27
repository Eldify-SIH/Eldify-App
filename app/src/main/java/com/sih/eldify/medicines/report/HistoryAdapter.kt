package com.sih.eldify.medicines.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sih.eldify.R
import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.views.RobotoBoldTextView
import com.sih.eldify.medicines.views.RobotoRegularTextView


/**
 * Created by gautam on 13/07/17.
 */
class HistoryAdapter internal constructor(historyList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
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
    ): HistoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int
    ) {
        val history: History = mHistoryList!![position] ?: return
        holder.tvMedDate?.setText(history.formattedDate)
        setMedicineAction(holder, history.action)
        holder.tvMedicineName?.setText(history.pillName)
        holder.tvDoseDetails?.setText(history.formattedDose)
    }

    private fun setMedicineAction(
        holder: HistoryViewHolder,
        action: Int
    ) {
        when (action) {
            0 -> holder.ivMedicineAction?.setVisibility(View.GONE)
            1 -> {
                holder.ivMedicineAction?.setVisibility(View.VISIBLE)
                holder.ivMedicineAction?.setImageResource(R.drawable.image_reminder_taken)
            }
            2 -> {
                holder.ivMedicineAction?.setImageResource(R.drawable.image_reminder_not_taken)
                holder.ivMedicineAction?.setVisibility(View.VISIBLE)
            }
            else -> holder.ivMedicineAction?.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return if (mHistoryList != null && !mHistoryList!!.isEmpty()) mHistoryList!!.size else 0
    }

    class HistoryViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {

        var tvMedDate: RobotoBoldTextView? = itemView?.findViewById(R.id.med_rh_tv_med_date)

        var tvMedicineName: RobotoBoldTextView? = itemView?.findViewById(R.id.med_rh_tv_medicine_name)

        var tvDoseDetails: RobotoRegularTextView? = itemView?.findViewById(R.id.med_rh_tv_dose_details)

        var ivMedicineAction: ImageView? = itemView?.findViewById(R.id.med_rh_iv_medicine_action)

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

    init {
        setList(historyList)
    }
}