package com.sih.eldify.medicines.report

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.sih.eldify.R
import com.sih.eldify.medicines.data.source.History
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*

/**
 * Created by gautam on 13/07/17.
 */
class MonthlyReportFragment : Fragment(),
    MonthlyReportContract.View {
    var unbinder: Unbinder? = null


    private var mHistoryAdapter: HistoryAdapter? = null
    private var presenter: MonthlyReportContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHistoryAdapter = HistoryAdapter(ArrayList<History>())
        setHasOptionsMenu(true)
    }

    private fun setAdapter(view: View) {
        view.med_fh_rv_history_list!!.adapter = mHistoryAdapter
        view.med_fh_rv_history_list!!.layoutManager = LinearLayoutManager(context)
        view.med_fh_rv_history_list!!.setHasFixedSize(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        unbinder = ButterKnife.bind(this, view)
        setAdapter(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun setPresenter(presenter: MonthlyReportContract.Presenter?) {
        this.presenter = presenter
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }
        med_fh_progressLoader!!.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showHistoryList(historyList: List<History?>?) {
        mHistoryAdapter!!.replaceData(historyList!! as List<History>)
        med_fh_tasksLL!!.visibility = View.VISIBLE
        med_fh_no_med_view!!.visibility = View.GONE
    }

    override fun showLoadingError() {}
    override fun showNoHistory() {
        showNoHistoryView(
            getString(R.string.no_history),
            R.drawable.icon_my_health
        )
    }

    override fun showTakenFilterLabel() {
        med_fh_filteringLabel?.setText(R.string.taken_label)
    }

    override fun showIgnoredFilterLabel() {
        med_fh_filteringLabel?.setText(R.string.ignore_label)
    }

    override fun showAllFilterLabel() {
        med_fh_filteringLabel?.setText(R.string.all_label)
    }

    override fun showNoTakenHistory() {
        showNoHistoryView(
            getString(R.string.no_taken_med_history),
            R.drawable.icon_my_health
        )
    }

    override fun showNoIgnoredHistory() {
        showNoHistoryView(
            getString(R.string.no_ignored_history),
            R.drawable.icon_my_health
        )
    }

    override val isActive: Boolean
        get() = isAdded

    override fun showFilteringPopUpMenu() {
        val popup = PopupMenu(requireContext(), requireActivity().findViewById(R.id.med_menu_filter))
        popup.menuInflater.inflate(R.menu.filter_history, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.med_all -> presenter!!.setFiltering(FilterType.ALL_MEDICINES)
                R.id.med_taken -> presenter!!.setFiltering(FilterType.TAKEN_MEDICINES)
                R.id.med_ignored -> presenter!!.setFiltering(FilterType.IGNORED_MEDICINES)
            }
            presenter!!.loadHistory(true)
            true
        }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.med_menu_filter -> showFilteringPopUpMenu()
        }
        return true
    }

    private fun showNoHistoryView(mainText: String, iconRes: Int) {
        view?.med_fh_tasksLL!!.visibility = View.GONE
        view?.med_fh_no_med_view!!.visibility = View.VISIBLE
        view?.med_fh_noMedText?.setText(mainText)
        view?.med_fh_noMedIcon!!.setImageDrawable(resources.getDrawable(iconRes))
    }

    companion object {
        fun newInstance(): MonthlyReportFragment {
            val args = Bundle()
            val fragment = MonthlyReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}