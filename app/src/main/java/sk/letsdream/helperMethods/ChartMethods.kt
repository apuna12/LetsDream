package sk.letsdream.helperMethods

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import sk.letsdream.R
import sk.letsdream.StatistikyActivity
import sk.letsdream.dbMethods.DBConnection

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

        var bds: BarDataSet = BarDataSet(arrayList, "Prvy Pokus")
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

}