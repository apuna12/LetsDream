package sk.letsdream.helperMethods

import android.content.Context
import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import sk.letsdream.R
import sk.letsdream.StatistikyActivity
import sk.letsdream.dbMethods.DBConnection
import java.sql.Time
import android.R.attr.entries
import com.github.mikephil.charting.components.Legend


class ChartMethods{

    fun barChart(context: Context, chart: Int, barChart: BarChart)
    {
        val dbMethods: DBConnection = DBConnection()
        val request: String = dbMethods.getStatistics(chart)
        var arrayList: ArrayList<BarEntry> = ArrayList()
        var typedArrayList = request.split(",").toTypedArray()
        typedArrayList = typedArrayList.dropLast(1).toTypedArray()
        var items = arrayOf<Array<String>>()
        var xAxisValues = ArrayList<String>()

        for (i in 0 until typedArrayList.size)
        {
            items  += typedArrayList[i].split("-").toTypedArray()
            arrayList.add(BarEntry(i.toFloat(), items[i][1].toFloat()))
            xAxisValues.add(items[i][0])
        }
        val xAxis = barChart.xAxis
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        var bds: BarDataSet = BarDataSet(arrayList, "")
        if(chart == 2)
            bds.setColor(Color.CYAN, 100)
        else if(chart == 3)
            bds.setColor(Color.GREEN, 100)
        else
            bds.setColor(Color.RED, 100)
        bds.valueTextSize = 10f
        var barData: BarData = BarData(bds)
        barData.barWidth = 0.5f
        barChart.data = barData
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.textColor = Color.WHITE
        barChart.description.text = ""
        barChart.axisRight.textColor = Color.WHITE
        barChart.legend.isEnabled = false
        barChart.data.setValueTextColor(Color.WHITE)
        barChart.isEnabled = true
        barChart.invalidate()
        barChart.refreshDrawableState()
    }


    fun lineChart(context: Context, chart: Int, lineChart: LineChart)
    {
        val dbMethods: DBConnection = DBConnection()
        val request: String = dbMethods.getStatistics(chart)
        var arrayList: ArrayList<Entry> = ArrayList()
        var typedArrayList = request.split(",").toTypedArray()
        typedArrayList = typedArrayList.dropLast(1).toTypedArray()
        var items = arrayOf<Array<String>>()
        var xAxisValues = ArrayList<String>()

        for (i in 0 until typedArrayList.size)
        {
            items  += typedArrayList[i].split("-").toTypedArray()
            arrayList.add(Entry(i.toFloat(), items[i][1].toFloat()))
            xAxisValues.add(items[i][0])
        }
        val xAxis = lineChart.xAxis
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        var lds: LineDataSet = LineDataSet(arrayList, "")
        lds.lineWidth = 2f
        if(chart == 2)
            lds.setColor(Color.CYAN, 100)
        else if(chart == 3)
            lds.setColor(Color.GREEN, 100)
        else
            lds.setColor(Color.RED, 100)
        lds.valueTextSize = 10f
        var lineData: LineData = LineData(lds)
        lineChart.data = lineData
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.description.text = ""
        lineChart.axisRight.textColor = Color.WHITE
        lineChart.legend.isEnabled = false
        lineChart.data.setValueTextColor(Color.WHITE)
        lineChart.isEnabled = true
        lineChart.invalidate()
        lineChart.refreshDrawableState()
    }

    fun pieChart(context: Context, pieChart: PieChart)
    {
        val dbMethods: DBConnection = DBConnection()
        val request: String = dbMethods.getStatistics(4)
        var arrayList: ArrayList<PieEntry> = ArrayList()
        var typedArrayList = request.split(",").toTypedArray()
        typedArrayList = typedArrayList.dropLast(1).toTypedArray()
        var items = arrayOf<Array<String>>()
        var xAxisValues = ArrayList<String>()


        for (i in 0 until typedArrayList.size)
        {
            var helper : String = String()
            items  += typedArrayList[i].split("-").toTypedArray()
            if(items[i][1].contains(":")) {
                helper = items[i][1].substring(0, items[i][1].indexOf(":", 0, true)) + "." +
                        ((items[i][1].substring(items[i][1].indexOf(":", 0, true)+1)
                            .toInt())/6).toString()
                items[i][1] = helper
            }

            else {
                helper = items[i][1]
                items[i][1] = helper
            }

            xAxisValues.add(items[i][0])
        }
        var clearedNameList: ArrayList<String> = ArrayList()
        for (i in 0 until typedArrayList.size)
        {
            if(!clearedNameList.contains(items[i][0])) {
                clearedNameList.add(items[i][0])
            }
        }

        var fullCountedList = arrayOf<Array<String>>()
        for(i in 0 until clearedNameList.size)
        {
            fullCountedList += (clearedNameList[i] + "-" + "0").split("-").toTypedArray()
            for(j in 0 until typedArrayList.size)
            {
                if(fullCountedList[i][0] == items[j][0])
                {
                    fullCountedList[i][1] = (fullCountedList[i][1].toFloat() + items[j][1].toFloat()).toString()
                }
            }
        }

        var legendLabels: List<LegendEntry> = ArrayList<LegendEntry>()
        for(i in 0 until fullCountedList.size)
        {
            arrayList.add(PieEntry(fullCountedList[i][1].toFloat(), fullCountedList[i][1].toFloat()))
            val entry = LegendEntry()
            entry.formColor = ColorTemplate.COLORFUL_COLORS.get(i)
            entry.label = fullCountedList[i][0]
            legendLabels += entry
        }

        var pds: PieDataSet = PieDataSet(arrayList, "")
        pds.colors = ColorTemplate.COLORFUL_COLORS.toList()
        pds.valueTextSize = 10f
        var pieData: PieData = PieData(pds)
        pieChart.data = pieData
        pieChart.description.text = ""
        pieChart.legend.isEnabled = true
        pieChart.legend.setCustom(legendLabels)
        pieChart.legend.position = Legend.LegendPosition.LEFT_OF_CHART_CENTER
        pieChart.legend.textColor = Color.WHITE
        pieChart.data.setValueTextColor(Color.WHITE)
        pieChart.isEnabled = true
        pieChart.invalidate()
        pieChart.refreshDrawableState()
    }

}