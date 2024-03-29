package sk.letsdream

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import kotlinx.android.synthetic.main.content_statistics.*
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.ChartMethods
import sk.letsdream.helperMethods.NetworkTask
import sk.letsdream.helperMethods.TimeMethods


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

        timeMethod.UpdateActualTime(date, time)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val imageButton: ImageButton = findViewById(R.id.vyberstatistikySPINNER)
        val textViewStatName: TextView = findViewById(R.id.statFromSpinner)
        val textViewStatNameLabel: TextView = findViewById(R.id.názovStatistikyLABEL)
        val dbMethods: DBConnection = DBConnection()
        var networkTask: NetworkTask

        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val wrapper: Context = ContextThemeWrapper(this, R.style.popupMenuStyle)
        var popUpMenu: PopupMenu = PopupMenu(wrapper, imageButton)

        popUpMenu.menuInflater.inflate(R.menu.array, popUpMenu.menu)
        textViewStatName.setText(popUpMenu.menu.getItem(0).toString())
        textViewStatNameLabel.setText(textViewStatName.text)
        val lineChart: LineChart = findViewById(R.id.lineChart)
        barChart.visibility = View.INVISIBLE
        lineChart.visibility = View.VISIBLE
        val chartMethods: ChartMethods = ChartMethods()
        chartMethods.lineChart(this, 2, lineChart)

        imageButton.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                popUpMenu.setOnMenuItemClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    textViewStatName.setText(it.title.toString())
                    textViewStatNameLabel.setText(it.title.toString())
                    val itemId = it.itemId
                    if (itemId == R.id.menu_item_2) {
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        val lineChart: LineChart = findViewById(R.id.lineChart)
                        barChart.visibility = View.INVISIBLE
                        lineChart.visibility = View.VISIBLE
                        chartMethods.lineChart(this, 2, lineChart)
                    } else if (itemId == R.id.menu_item_3) {
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        val barChart: BarChart = findViewById(R.id.barChart)
                        lineChart.visibility = View.INVISIBLE
                        barChart.visibility = View.VISIBLE
                        chartMethods.barChart(this, 3, barChart)
                    } else if (itemId == R.id.menu_item_4) {
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        val pieChart: PieChart = findViewById(R.id.pieChart)
                        lineChart.visibility = View.INVISIBLE
                        barChart.visibility = View.VISIBLE
                        chartMethods.volunteerPerformance(this, barChart)
                    }
                    true
                }
                popUpMenu.show()
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
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
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
