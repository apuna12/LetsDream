package sk.letsdream

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.res.ColorStateList
import android.database.SQLException
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import kotlinx.android.synthetic.main.content_dochadzka.*
import kotlinx.android.synthetic.main.content_statistics.view.*
import okhttp3.*
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.TimeMethods
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList


class StatistikyActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy);

        val timeMethod: TimeMethods = TimeMethods()

        timeMethod.UpdateActualTime(date,time)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val imageButton: ImageButton = findViewById(R.id.vyberstatistikySPINNER)
        val textViewStatName: TextView = findViewById(R.id.statFromSpinner)
        val textViewStatNameLabel: TextView = findViewById(R.id.názovStatistikyLABEL)
        val dbMethods: DBConnection = DBConnection()


        imageButton.setOnClickListener{
            var popUpMenu: PopupMenu = PopupMenu(this, imageButton)
            popUpMenu.inflate(R.menu.stat_menu)
            popUpMenu.setOnMenuItemClickListener {
                textViewStatName.setText(it.title.toString())
                textViewStatNameLabel.setText(it.title.toString())
                if(it.itemId == 0)
                {

                }
                else if(it.itemId == 1) {
                    val request: String = dbMethods.getStatistics(it.itemId)

                    val barChart: BarChart = findViewById(R.id.barChart)
                    var arrayList: ArrayList<BarEntry> = ArrayList()
                    var typedArrayList = request.split(",").toTypedArray()
                    //mam arraylist v tvare 'akcia-pocet'...rozdelit do dvoch separatnych listov alebo dvojrozmerneho pola
                    var xAxis: XAxis = XAxis()
                    for (i in 0 until typedArrayList.size)
                    {
                        //arrayList.add(Entry(,""))
                    }

                    var bds: BarDataSet = BarDataSet(arrayList, "Prvy Pokus")
                    bds.setColor(Color.RED)
                    var ibds: ArrayList<IBarDataSet> = ArrayList()
                    ibds.add(bds)

                    var barData: BarData = BarData(ibds)
                    barChart.data = barData
                    barChart.xAxis.textColor = Color.WHITE
                    barChart.axisLeft.textColor = Color.WHITE
                    barChart.axisRight.textColor = Color.WHITE
                    barChart.legend.textColor = Color.WHITE
                    //chart.data.setValueTextColor(Color.WHITE)
                    barChart.description.text = ""
                    barChart.isEnabled = true
                }
                else if(it.itemId == 2)
                {

                }
                else if(it.itemId == 3)
                {

                }
                true
            }
            popUpMenu.show()
        }

        val lineChart: LineChart = findViewById(R.id.lineChart)
        val pieChart: PieChart = findViewById(R.id.pieChart)

        /*var arrayList: ArrayList<Entry> = ArrayList()
        arrayList.add(Entry(0F, 20F))
        arrayList.add(Entry(1F, 24F))
        arrayList.add(Entry(2F, 2F))
        arrayList.add(Entry(3F, 10F))

        var lds: LineDataSet = LineDataSet(arrayList,"Prvy Pokus")
        lds.setColor(Color.RED)
        lds.lineWidth = 2F
        var ilds: ArrayList<ILineDataSet> = ArrayList()
        ilds.add(lds)

        var lineData: LineData = LineData(ilds)
        lineChart.data = lineData
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisRight.textColor = Color.WHITE
        lineChart.legend.textColor = Color.WHITE
        //chart.data.setValueTextColor(Color.WHITE)
        lineChart.description.text = ""
        lineChart.isEnabled = false*/



        /*var arrayList: ArrayList<PieEntry> = ArrayList()
        arrayList.add(PieEntry(0F, 20F))
        arrayList.add(PieEntry(1F, 24F))
        arrayList.add(PieEntry(2F, 2F))
        arrayList.add(PieEntry(3F, 10F))

        var pds: PieDataSet = PieDataSet(arrayList,"Prvy Pokus")
        pds.setColor(Color.RED)
        var ipds: ArrayList<IPieDataSet> = ArrayList()
        ipds.add(pds)
        var iPieDataSet: IPieDataSet = pds
        var pieData: PieData = PieData(iPieDataSet)
        pieChart.data = pieData
        pieChart.legend.textColor = Color.WHITE
        pieChart.description.text = ""
        pieChart.isEnabled = true*/

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
