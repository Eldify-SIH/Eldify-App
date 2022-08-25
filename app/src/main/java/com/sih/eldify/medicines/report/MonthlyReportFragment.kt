package com.sih.eldify.medicines.report

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.sih.eldify.medicines.R
import com.sih.eldify.medicines.data.source.History
import com.sih.eldify.medicines.views.RobotoLightTextView

/**
 * Created by gautam on 13/07/17.
 */
class MonthlyReportFragment : Fragment(),
    MonthlyReportContract.View {
    @BindView(R.id.rv_history_list)
    var rvHistoryList: RecyclerView? = null

    @BindView(R.id.progressLoader)
    var progressLoader: ProgressBar? = null

    @BindView(R.id.noMedIcon)
    var noMedIcon: ImageView? = null

    @BindView(R.id.noMedText)
    var noMedText: RobotoLightTextView? = null

    @BindView(R.id.no_med_view)
    var noMedView: View? = null
    var unbinder: Unbinder? = null

    @BindView(R.id.filteringLabel)
    var filteringLabel: TextView? = null

    @BindView(R.id.tasksLL)
    var tasksLL: LinearLayout? = null
    private var mHistoryAdapter: HistoryAdapter? = null
    private var presenter: MonthlyReportContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHistoryAdapter = HistoryAdapter(ArrayList<History>())
        setHasOptionsMenu(true)
    }

    private fun setAdapter() {
        rvHistoryList!!.adapter = mHistoryAdapter
        rvHistoryList!!.layoutManager = LinearLayoutManager(context)
        rvHistoryList!!.setHasFixedSize(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        unbinder = ButterKnife.bind(this, view)
        setAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    fun setPresenter(presenter: MonthlyReportContract.Presenter?) {
        this.presenter = presenter
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }
        progressLoader!!.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun showHistoryList(historyList: List<History?>?) {
        mHistoryAdapter!!.replaceData(historyList!!)
        tasksLL!!.visibility = View.VISIBLE
        noMedView!!.visibility = View.GONE
    }

    override fun showLoadingError() {}
    override fun showNoHistory() {
        showNoHistoryView(
            getString(R.string.no_history),
            R.drawable.icon_my_health
        )
    }

    override fun showTakenFilterLabel() {
        filteringLabel.setText(R.string.taken_label)
    }

    override fun showIgnoredFilterLabel() {
        filteringLabel.setText(R.string.ignore_label)
    }

    override fun showAllFilterLabel() {
        filteringLabel.setText(R.string.all_label)
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
        val popup = PopupMenu(context!!, activity!!.findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_history, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.all -> presenter!!.setFiltering(FilterType.ALL_MEDICINES)
                R.id.taken -> presenter!!.setFiltering(FilterType.TAKEN_MEDICINES)
                R.id.ignored -> presenter!!.setFiltering(FilterType.IGNORED_MEDICINES)
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
            R.id.menu_filter -> showFilteringPopUpMenu()
        }
        return true
    }

    private fun showNoHistoryView(mainText: String, iconRes: Int) {
        tasksLL!!.visibility = View.GONE
        noMedView!!.visibility = View.VISIBLE
        noMedText.setText(mainText)
        noMedIcon!!.setImageDrawable(resources.getDrawable(iconRes))
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