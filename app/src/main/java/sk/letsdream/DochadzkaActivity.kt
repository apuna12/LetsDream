package sk.letsdream

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.database.SQLException
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
import kotlinx.android.synthetic.main.content_dochadzka.*
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

        var resp: String? = String()

        timeMethod.UpdateActualTime(date,time)

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

        var list_of_items = arrayOf("Patvaros Nigel","Fero Pokuta", "Vikina Migova")
       /*val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeno!!.setAdapter(aa)


        spinnerMeno?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                meno.text = spinnerMeno.selectedItem.toString()

            }

        }*/



        timeMethod.SetDatePicker(this, prichodDatePicker)
        timeMethod.SetDatePicker(this, odchodDatePicker)
        timeMethod.SetTimePicker(this, prichodTimePicker)
        timeMethod.SetTimePicker(this, odchodTimePicker)


        submit.setOnClickListener{
            var timeDifference = timeMethod.dateDifference(prichodDatePicker.text.toString(), prichodTimePicker.text.toString(),
                odchodDatePicker.text.toString(), odchodTimePicker.text.toString())
            val urlPost = "http://letsdream.xf.cz/index.php?meno=" + meno.text + "&prichodDatum=" + prichodDatePicker.text +
                    "&prichodCas=" + prichodTimePicker.text + "&odchodDatum=" + odchodDatePicker.text + "&odchodCas=" +
                    odchodTimePicker.text + "&hodiny=" + timeDifference + "&poznamka=" + poznamkaET.text + "&table=dochadzka&rest=post"

            try {
                URL(urlPost).readText()

                //In case you need to use jsonStr
                /*val urlGet = "http://letsdream.xf.cz/index.php?meno=" + meno.text + "&prichodDatum=" + prichodDatePicker.text +
                    "&prichodCas=" + prichodTimePicker.text + "&odchodDatum=" + odchodDatePicker.text + "&odchodCas=" +
                    odchodTimePicker.text + "&hodiny=" + timeDifference + "&poznamka=" + poznamkaET.text + "&table=dochadzka&mod=get"
                var jsonStr = URL(urlGet).readText()
                var firstApp: Int = 0
                var lastApp: Int = 0
                if (jsonStr.toString().contains("<") || jsonStr.toString().contains(">")) {
                    for (i in 0 until jsonStr.toString().length) {
                        if (jsonStr[i] == '<') {
                            firstApp = i
                            break
                        }
                    }
                    for (i in 0 until jsonStr.toString().length) {
                        if (jsonStr[i] == '>')
                            lastApp = i
                    }
                    jsonStr = jsonStr.removeRange(firstApp, lastApp)


                }*/
                Toast.makeText(this, "Záznam pridaný", Toast.LENGTH_LONG).show()
            }
            catch (e: Exception)
            {
                Toast.makeText(this, "Pridanie neprebehlo úspešne", Toast.LENGTH_LONG).show()
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
