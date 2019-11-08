package com.diegobarrena.segundamano.views.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.diegobarrena.segundamano.R
import com.diegobarrena.segundamano.views.base.BaseActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity<MainActivityViewModel>(MainActivityViewModel::class) {
    private val initialStartDate = "2019-01-01"
    private val initialEndDate = "2019-01-31"

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        observe()
    }

    private fun init() {
        graph.isDragEnabled = true
        graph.setScaleEnabled(true)
        graph.description.isEnabled = false

        val xAxis = graph.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            override fun getFormattedValue(value: Float): String {
                return sdf.format(Date(value.toLong()))
            }
        }

        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1.0f

        startDate.text = initialStartDate
        endDate.text = initialEndDate

        selectDateRangeBtn.setOnClickListener {
            showDateRangeDialog()
        }
    }

    private fun observe() {
        viewModel.getHistoricRates(initialStartDate, initialEndDate)

        viewModel.historicRates.observe(
            this,
            Observer {
                it?.let { historic ->
                    progressBar.visibility = View.GONE
                    graph.visibility = View.VISIBLE

                    val sdf = SimpleDateFormat("yyyy-MM-dd")

                    graph.removeAllViews()

                    val points = arrayListOf<Entry>()

                    historic.rates.toSortedMap().forEach { (date, rate) ->
                        sdf.parse(date)?.let {
                            points.add(Entry(it.time.toFloat(), rate.dollar))
                        }
                    }

                    val series = LineDataSet(points, "USD value against EUR")
                    val data = LineData(series)
                    graph.data = data
                    graph.xAxis.labelCount = 4
                }
            })
    }

    private fun showDateRangeDialog() {
        val dialog = Dialog(this)
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.dialog_date_range, null)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        var selectedStartDate: Date? = null
        var selectedEndDate: Date? = null

        val calendar = dialog.findViewById<DateRangeCalendarView>(R.id.calendar)

        val acceptBtn = dialog.findViewById<Button>(R.id.acceptBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)

        calendar.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                selectedStartDate = startDate.time
                selectedEndDate = endDate.time
            }

            override fun onFirstDateSelected(startDate: Calendar) {

            }

        })

        acceptBtn.setOnClickListener {
            if (selectedStartDate != null && selectedEndDate != null) {

                if(selectedEndDate!!.after(Date())) {
                    Toast.makeText(this, "You cannot select a date greater than today.", Toast.LENGTH_SHORT).show()
                    calendar.resetAllSelectedViews()
                } else {
                    applyDateRange(selectedStartDate!!, selectedEndDate!!)
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(this, "Please select a date range.", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun applyDateRange(start: Date, end: Date) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        startDate.text = sdf.format(start)
        endDate.text = sdf.format(end)

        progressBar.visibility = View.VISIBLE
        graph.visibility = View.GONE

        viewModel.getHistoricRates(sdf.format(start), sdf.format(end))
    }
}
