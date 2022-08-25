package com.sih.eldify.medicines.report

import com.sih.eldify.medicines.BasePresenter
import com.sih.eldify.medicines.BaseView
import com.sih.eldify.medicines.data.source.History

interface MonthlyReportContract {
    interface View : BaseView<Presenter?> {
        fun setLoadingIndicator(active: Boolean)
        fun showHistoryList(historyList: List<History?>?)
        fun showLoadingError()
        fun showNoHistory()
        fun showTakenFilterLabel()
        fun showIgnoredFilterLabel()
        fun showAllFilterLabel()
        fun showNoTakenHistory()
        fun showNoIgnoredHistory()
        val isActive: Boolean

        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {
        fun loadHistory(showLoading: Boolean)
        fun setFiltering(filterType: FilterType?)
        val filterType: FilterType?
    }
}
