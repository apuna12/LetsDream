package sk.letsdream


import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.Vibrator
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
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.NetworkTask
import sk.letsdream.helperMethods.TimeMethods
import java.lang.Exception
import java.text.SimpleDateFormat




class DochadzkaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var privileges = ""
    var loginName = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        loginName = intent.getStringExtra("login")
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
        var networkTask: NetworkTask


        timeMethod.UpdateActualTime(date, time)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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

        val prichodDatePicker: TextView = findViewById(R.id.prichodDatePicker)
        val prichodTimePicker: TextView = findViewById(R.id.prichodTimePicker)
        val odchodDatePicker: TextView = findViewById(R.id.odchodDatePicker)
        val odchodTimePicker: TextView = findViewById(R.id.odchodTimePicker)
        val vybermenaLABEL: TextView = findViewById(R.id.vybermenaLABEL)
        val poznamka: EditText = findViewById(R.id.poznamkaET)
        val meno: TextView = findViewById(R.id.nameFromSpinner)
        val spinnerMeno: ImageButton = findViewById(R.id.vybermenaSPINNER)
        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (isOnline(this)) {
            if (privileges == "1") {
                meno.text = dbMethods.getLoggedUserName(loginName)
                spinnerMeno.visibility = View.INVISIBLE
                vybermenaLABEL.text = "Používateľ"
            } else {
                spinnerMeno.visibility = View.VISIBLE
                vybermenaLABEL.text = "Výber mena"
            }

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
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {
                    popUpMenu.setOnMenuItemClickListener {
                        vibrate.vibrate(70)
                        if (popUpMenu.menu.size() == 0) {
                            Toast.makeText(
                                this,
                                "Hups! V Databáze sa nenachádza žiadne meno!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            meno.text = it.title.toString()
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

            prichodDatePicker.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {
                    timeMethod.SetDatePicker(this, prichodDatePicker)
                } else
                    Toast.makeText(
                        this,
                        "Hups! Nie ste pripojený na internet.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            odchodDatePicker.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {
                    timeMethod.SetDatePicker(this, odchodDatePicker)
                } else
                    Toast.makeText(
                        this,
                        "Hups! Nie ste pripojený na internet.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            prichodTimePicker.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {
                    timeMethod.SetTimePicker(this, prichodTimePicker)
                } else
                    Toast.makeText(
                        this,
                        "Hups! Nie ste pripojený na internet.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            odchodTimePicker.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {
                    timeMethod.SetTimePicker(this, odchodTimePicker)
                } else
                    Toast.makeText(
                        this,
                        "Hups! Nie ste pripojený na internet.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            poznamka.setOnClickListener {
                vibrate.vibrate(70)
            }


            submit.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {

                    if (prichodDatePicker.text == "Dátum" || prichodTimePicker.text == "Čas" || odchodDatePicker.text == "Dátum" ||
                        odchodTimePicker.text == "Čas"
                    )
                        Toast.makeText(
                            this,
                            "Prosím vyplňte všetky potrebné informácie",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (meno.text.toString() == "Vyberte meno")
                        Toast.makeText(this, "Nevybrali ste meno", Toast.LENGTH_SHORT).show()
                    else {
                        var timeParser = SimpleDateFormat("HH:mm")
                        var timeFormatter = SimpleDateFormat("HH:mm")
                        var dateParser = SimpleDateFormat("dd.MM.yyyy")
                        var dateFormatter = SimpleDateFormat("dd.MM.yyyy")
                        var dateOdTIME =
                            dateFormatter.format(dateParser.parse(prichodDatePicker.text.toString()))
                        var dateDoTIME =
                            dateFormatter.format(dateParser.parse(odchodDatePicker.text.toString()))
                        var casOdTIME =
                            timeFormatter.format(timeParser.parse(prichodTimePicker.text.toString()))
                        var casDoTIME =
                            timeFormatter.format(timeParser.parse(odchodTimePicker.text.toString()))
                        if (dateOdTIME <= dateDoTIME) {
                            if ((casOdTIME <= casDoTIME && dateOdTIME <= dateDoTIME) || (casOdTIME > casDoTIME && dateOdTIME < dateDoTIME)) {
                                var timeDifference = timeMethod.dateDifference(
                                    prichodDatePicker.text.toString(),
                                    prichodTimePicker.text.toString(),
                                    odchodDatePicker.text.toString(),
                                    odchodTimePicker.text.toString()
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
                                        Toast.makeText(this, "Záznam pridaný", Toast.LENGTH_SHORT)
                                            .show()
                                        meno.text = ""
                                        prichodDatePicker.text = "Dátum"
                                        odchodDatePicker.text = "Dátum"
                                        prichodTimePicker.text = "Čas"
                                        odchodTimePicker.text = "Čas"
                                        poznamka.text.clear()
                                    } else
                                        Toast.makeText(
                                            this,
                                            "Hups! Záznam nebol pridaný!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this,
                                        "Hups! Záznam nebol pridaný! Máte zapnutý internet?",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } else {
                                Toast.makeText(this, "Prosím opravte si čas!", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else {
                            Toast.makeText(this, "Prosím opravte si dátum!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else
                    Toast.makeText(
                        this,
                        "Hups! Nie ste pripojený na internet.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        } else
            Toast.makeText(
                this,
                "Hups! Nie ste pripojený na internet.",
                Toast.LENGTH_SHORT
            ).show()
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
