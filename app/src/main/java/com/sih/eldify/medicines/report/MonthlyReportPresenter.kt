package com.sih.eldify.medicines.report

import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.data.source.MedicineDataSource
import com.sih.eldify.medicines.data.source.MedicineRepository


/**
 * Created by gautam on 13/07/17.
 */
class MonthlyReportPresenter(
    medicineRepository: MedicineRepository,
    monthlyReportView: MonthlyReportContract.View
) :
    MonthlyReportContract.Presenter {
    private val mMedicineRepository: MedicineRepository
    private val mMonthlyReportView: MonthlyReportContract.View
    override var filterType = FilterType.ALL_MEDICINES
        private set

    override fun start() {
        loadHistory(true)
    }

    override fun loadHistory(showLoading: Boolean) {
        loadHistoryFromDb(showLoading)
    }

    // Empty
    override fun setFiltering(filterType: FilterType?) {
        TODO("Not yet implemented")
    }

    private fun loadHistoryFromDb(showLoading: Boolean) {
        if (showLoading) {
            mMonthlyReportView.setLoadingIndicator(true)
        }
        mMedicineRepository.getMedicineHistory(object : MedicineDataSource.LoadHistoryCallbacks {
            override fun onHistoryLoaded(historyList: List<History?>?) {
                val historyShowList: MutableList<History?> = ArrayList<History?>()

                //We will filter the History based on request type
                for (history in historyList!!) {
                    when (filterType) {
                        FilterType.ALL_MEDICINES -> historyShowList.add(history)
                        FilterType.TAKEN_MEDICINES -> if (history!!.action === 1) {
                            historyShowList.add(history)
                        }
                        FilterType.IGNORED_MEDICINES -> if (history!!.action === 2) {
                            historyShowList.add(history)
                        }
                    }
                }
                processHistory(historyShowList)
                if (!mMonthlyReportView.isActive) {
                    return
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false)
                }
            }

            override fun onDataNotAvailable() {
                if (!mMonthlyReportView.isActive) {
                    return
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false)
                }
                mMonthlyReportView.showLoadingError()
            }
        })
    }

    private fun processHistory(historyList: List<History?>) {
        if (historyList.isEmpty()) {
            // Show a message indicating there are no history for that filter type.
            processEmptyHistory()
        } else {
            //Show the list of history
            mMonthlyReportView.showHistoryList(historyList)
            //Set filter label's text
            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when (filterType) {
            FilterType.ALL_MEDICINES -> mMonthlyReportView.showAllFilterLabel()
            FilterType.TAKEN_MEDICINES -> mMonthlyReportView.showTakenFilterLabel()
            FilterType.IGNORED_MEDICINES -> mMonthlyReportView.showIgnoredFilterLabel()
            else -> mMonthlyReportView.showAllFilterLabel()
        }
    }

    private fun processEmptyHistory() {
        when (filterType) {
            FilterType.ALL_MEDICINES -> mMonthlyReportView.showNoHistory()
            FilterType.TAKEN_MEDICINES -> mMonthlyReportView.showNoTakenHistory()
            FilterType.IGNORED_MEDICINES -> mMonthlyReportView.showNoIgnoredHistory()
            else -> mMonthlyReportView.showNoHistory()
        }
    }

    init {
        mMedicineRepository = medicineRepository
        mMonthlyReportView = monthlyReportView
        mMonthlyReportView.setPresenter(this)
    }
}
