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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class ChartMethods{

    fun barChart(context: Context, chart: Int, barChart: BarChart) {
        val dbMethods: DBConnection = DBConnection()
        val request: String = dbMethods.getStatistics(chart)
        var arrayList: ArrayList<BarEntry> = ArrayList()
        var typedArrayList = request.split(",").toTypedArray()
        typedArrayList = typedArrayList.dropLast(1).toTypedArray()
        var items = arrayOf<Array<String>>()
        var xAxisValues = ArrayList<String>()

        for (i in 0 until typedArrayList.size) {
            items += typedArrayList[i].split("-").toTypedArray()
            arrayList.add(BarEntry(i.toFloat(), items[i][1].toFloat()))
            xAxisValues.add(items[i][0])
        }
        val xAxis = barChart.xAxis
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelRotationAngle = 0.toFloat()

        var bds: BarDataSet = BarDataSet(arrayList, "")
        if (chart == 2)
            bds.setColor(Color.CYAN, 100)
        else if (chart == 3)
            bds.setColor(Color.GREEN, 100)
        else if (chart == 4)
        {
            bds.colors = CUSTOM_COLORS.toList()
        }

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
        xAxis.labelRotationAngle = 90.toFloat()

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

    fun volunteerPerformance(context: Context, barChart2: BarChart)
    {
        val dbMethods: DBConnection = DBConnection()
        val request: String = dbMethods.getStatistics(4)
        var arrayList: ArrayList<BarEntry> = ArrayList()
        var typedArrayList = request.split("?").toTypedArray()
        typedArrayList = typedArrayList.dropLast(1).toTypedArray()
        var items = arrayOf<Array<String>>()
        var xAxisValues = ArrayList<String>()


        for (i in 0 until typedArrayList.size)
        {
            var helper : String = String()
            items  += typedArrayList[i].split("-").toTypedArray()
            if(items[i][0].contains(","))
            {
                var temp = items[i][0].split(",")
                items[i][0] = temp[1] + " " + temp[0]
            }

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
        fullCountedList = fullCountedList.dropLast(1).toTypedArray()
        for(i in 0 until fullCountedList.size)
        {
            arrayList.add(BarEntry(i.toFloat(), fullCountedList[i][1].toFloat()))
            xAxisValues.add(fullCountedList[i][0])
        }

        val xAxis = barChart2.xAxis

        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelRotationAngle = 90.toFloat()


        var bds: BarDataSet = BarDataSet(arrayList, "")
        bds.colors = CUSTOM_COLORS.toList()
        bds.valueTextSize = 10f
        var barData: BarData = BarData(bds)
        barData.barWidth = 0.5f
        barChart2.data = barData
        barChart2.xAxis.textColor = Color.WHITE
        barChart2.axisLeft.textColor = Color.WHITE
        barChart2.description.text = ""
        barChart2.axisRight.textColor = Color.WHITE
        barChart2.legend.isEnabled = false
        barChart2.data.setValueTextColor(Color.WHITE)
        barChart2.isEnabled = true
        barChart2.invalidate()
        barChart2.refreshDrawableState()
    }

    val CUSTOM_COLORS = intArrayOf(Color.rgb(65, 236, 186), Color.rgb(120, 100, 0),
        Color.rgb(50, 147, 40), Color.rgb(122, 127, 199), Color.rgb(202, 127, 134), Color.rgb(144, 97, 234), Color.rgb(76, 97, 181),
        Color.rgb(102, 144, 164), Color.rgb(250, 60, 87), Color.rgb(238, 110, 55), Color.rgb(34, 46, 148),
        Color.rgb(50, 24, 200), Color.rgb(210, 162, 148), Color.rgb(149, 50, 42), Color.rgb(140, 201, 201), 
        Color.rgb(56, 15, 67), Color.rgb(37, 7, 70), Color.rgb(246, 73, 5), Color.rgb(186, 112, 32), 
        Color.rgb(6, 157, 143), Color.rgb(124, 89, 58), Color.rgb(150, 126, 81), Color.rgb(72, 254, 173), 
        Color.rgb(89, 92, 167), Color.rgb(70, 166, 49), Color.rgb(175, 33, 35), Color.rgb(186, 222, 130), 
        Color.rgb(254, 202, 151), Color.rgb(118, 177, 155), Color.rgb(222, 254, 230), Color.rgb(197, 86, 219), 
        Color.rgb(16, 189, 254), Color.rgb(41, 222, 117), Color.rgb(19, 71, 117), Color.rgb(212, 164, 50), 
        Color.rgb(8, 111, 142), Color.rgb(45, 83, 112), Color.rgb(108, 113, 71), Color.rgb(72, 55, 115), 
        Color.rgb(53, 246, 163), Color.rgb(178, 4, 160), Color.rgb(51, 132, 224), Color.rgb(147, 95, 218), 
        Color.rgb(67, 174, 10), Color.rgb(191, 146, 99), Color.rgb(53, 15, 0), Color.rgb(146, 177, 159), 
        Color.rgb(117, 182, 56), Color.rgb(201, 221, 116), Color.rgb(100, 2, 141), Color.rgb(128, 18, 55), 
        Color.rgb(47, 12, 154), Color.rgb(211, 171, 107), Color.rgb(7, 49, 145), Color.rgb(207, 151, 209), 
        Color.rgb(217, 76, 215), Color.rgb(127, 130, 87), Color.rgb(196, 158, 19), Color.rgb(84, 79, 177), 
        Color.rgb(35, 131, 50), Color.rgb(175, 51, 93), Color.rgb(87, 156, 68), Color.rgb(106, 52, 227), 
        Color.rgb(24, 9, 59), Color.rgb(205, 188, 14), Color.rgb(252, 29, 160), Color.rgb(240, 34, 108), 
        Color.rgb(173, 204, 89), Color.rgb(224, 117, 236), Color.rgb(19, 146, 193), Color.rgb(154, 225, 184), 
        Color.rgb(91, 161, 14), Color.rgb(223, 99, 126), Color.rgb(9, 11, 187), Color.rgb(194, 252, 44), 
        Color.rgb(249, 112, 56), Color.rgb(44, 143, 174), Color.rgb(235, 143, 34), Color.rgb(176, 138, 156), 
        Color.rgb(228, 216, 2), Color.rgb(233, 83, 84), Color.rgb(221, 186, 220), Color.rgb(169, 215, 43), 
        Color.rgb(191, 79, 100), Color.rgb(194, 194, 202), Color.rgb(47, 120, 130), Color.rgb(98, 184, 237), 
        Color.rgb(29, 51, 175), Color.rgb(241, 24, 242), Color.rgb(240, 247, 134), Color.rgb(125, 9, 162), 
        Color.rgb(150, 254, 247), Color.rgb(8, 11, 113), Color.rgb(209, 45, 64), Color.rgb(104, 181, 181), 
        Color.rgb(215, 153, 223), Color.rgb(131, 129, 52), Color.rgb(89, 104, 22), Color.rgb(121, 100, 42), 
        Color.rgb(95, 59, 59))

}