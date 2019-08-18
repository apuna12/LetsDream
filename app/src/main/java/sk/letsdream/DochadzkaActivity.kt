package sk.letsdream

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.database.SQLException
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.content_akcie.*
import kotlinx.android.synthetic.main.content_dochadzka.*
import kotlinx.android.synthetic.main.dialog_addnewaction.view.*
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



class DochadzkaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dochadzka)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)
        val submit: Button = findViewById(R.id.submitBtn)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val timeMethod: TimeMethods = TimeMethods()
        val dbMethods: DBConnection = DBConnection()

        timeMethod.UpdateActualTime(date, time)

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
        val poznamka: EditText = findViewById(R.id.poznamkaET)
        val meno: TextView = findViewById(R.id.nameFromSpinner)
        val spinnerMeno: ImageButton = findViewById(R.id.vybermenaSPINNER)

        var namesList: Array<String>
        namesList = arrayOf<String>()

        namesList = dbMethods.getAllApprovedNames().split("?").toTypedArray()

        val wrapper: Context = ContextThemeWrapper(this, R.style.popupMenuStyle)
        var popUpMenu: PopupMenu = PopupMenu(wrapper, spinnerMeno)
        if (namesList.size > 0) {
            for (i in 0 until namesList.size - 1) {
                if (namesList[i] != null || namesList[i] != "") {
                    popUpMenu.menu.add(namesList[i])
                }
            }
        }

        spinnerMeno.setOnClickListener {
            popUpMenu.setOnMenuItemClickListener {
                if (popUpMenu.menu.size() == 0) {
                    Toast.makeText(this, "Hups! V Databáze sa nenachádza žiadne meno!", Toast.LENGTH_LONG).show()
                } else {
                    meno.text = it.title.toString()
                }
                true
            }
            popUpMenu.show()
        }

        prichodDatePicker.setOnClickListener {
            timeMethod.SetDatePicker(this, prichodDatePicker)
        }
        odchodDatePicker.setOnClickListener {
            timeMethod.SetDatePicker(this, odchodDatePicker)
        }
        prichodTimePicker.setOnClickListener {
            timeMethod.SetTimePicker(this, prichodTimePicker)
        }
        odchodTimePicker.setOnClickListener {
            timeMethod.SetTimePicker(this, odchodTimePicker)
        }


        submit.setOnClickListener {
            if (prichodDatePicker.text == "Dátum" || prichodTimePicker.text == "Čas" || odchodDatePicker.text == "Dátum" ||
                odchodTimePicker.text == "Čas"
            )
                Toast.makeText(this, "Prosím vyplňte všetky potrebné informácie", Toast.LENGTH_LONG).show()
            else {
                var timeParser = SimpleDateFormat("HH:mm")
                var timeFormatter = SimpleDateFormat("HH:mm")
                var dateParser = SimpleDateFormat("dd.MM.yyyy")
                var dateFormatter = SimpleDateFormat("dd.MM.yyyy")
                var dateOdTIME = dateFormatter.format(dateParser.parse(prichodDatePicker.text.toString()))
                var dateDoTIME = dateFormatter.format(dateParser.parse(odchodDatePicker.text.toString()))
                var casOdTIME = timeFormatter.format(timeParser.parse(prichodTimePicker.text.toString()))
                var casDoTIME = timeFormatter.format(timeParser.parse(odchodTimePicker.text.toString()))
                if (dateOdTIME <= dateDoTIME) {
                    if (casOdTIME <= casDoTIME) {
                        var timeDifference = timeMethod.dateDifference(
                            prichodDatePicker.text.toString(), prichodTimePicker.text.toString(),
                            odchodDatePicker.text.toString(), odchodTimePicker.text.toString()
                        )
                        try {

                            if (dbMethods.addActivityToVolunteer(
                                    meno.text.toString(),
                                    prichodDatePicker.text.toString(),
                                    prichodTimePicker.text.toString(),
                                    odchodDatePicker.text.toString(),
                                    odchodTimePicker.text.toString(),
                                    timeDifference.toString(),
                                    poznamka.text.toString()
                                ) == "1"
                            ) {
                                Toast.makeText(this, "Záznam pridaný", Toast.LENGTH_LONG).show()
                                meno.text = ""
                                prichodDatePicker.text = "Dátum"
                                odchodDatePicker.text = "Dátum"
                                prichodTimePicker.text = "Čas"
                                odchodTimePicker.text = "Čas"
                                poznamka.text.clear()
                            } else
                                Toast.makeText(this, "Hups! Záznam nebol pridaný!", Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Hups! Záznam nebol pridaný! Máte zapnutý internet?",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(this, "Prosím opravte si čas!", Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(this, "Prosím opravte si dátum!", Toast.LENGTH_LONG).show()
                }
            }
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
}
