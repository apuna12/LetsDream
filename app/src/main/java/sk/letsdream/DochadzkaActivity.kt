package sk.letsdream

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.os.Build
import android.os.Bundle
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
import android.view.Menu
import android.widget.DatePicker
import android.widget.TextView
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.TimeMethods
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS



class DochadzkaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dochadzka)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val timeMethod: TimeMethods = TimeMethods()
        val buttonEffects: ButtonEffects = ButtonEffects()

        timeMethod.UpdateActualTime(date,time)
        //buttonEffects.ButtonClickEffect(findViewById(R.id.graph_imageButton))
        //buttonEffects.ButtonClickEffect(findViewById(R.id.star_imageButton))
        //buttonEffects.ButtonClickEffect(findViewById(R.id.person_imageButton))
        //buttonEffects.ButtonClickEffect(findViewById(R.id.door_imageButton))

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)


        val prichodDatePicker: TextView = findViewById(R.id.prichodDatePicker)
        val prichodTimePicker: TextView = findViewById(R.id.prichodTimePicker)
        val odchodDatePicker: TextView = findViewById(R.id.odchodDatePicker)
        val odchodTimePicker: TextView = findViewById(R.id.odchodTimePicker)

        timeMethod.SetDatePicker(this, prichodDatePicker)
        timeMethod.SetDatePicker(this, odchodDatePicker)
        timeMethod.SetTimePicker(this, prichodTimePicker)
        timeMethod.SetTimePicker(this, odchodTimePicker)
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