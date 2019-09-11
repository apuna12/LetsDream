package sk.letsdream

import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.database.SQLException
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
import kotlinx.android.synthetic.main.content_akcie.*
import kotlinx.android.synthetic.main.content_dochadzka.*
import kotlinx.android.synthetic.main.dialog_addnewaction.view.*
import kotlinx.android.synthetic.main.dialog_sendemail.view.*
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
import StateVO
import MyAdapter


class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var privileges = ""
    var loginName = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        loginName = intent.getStringExtra("login")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

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



        val emailButton: Button = findViewById(R.id.buttonOdoslMail)
        val backupButton: Button = findViewById(R.id.buttonZalohaDB)
        val deleteButton: Button = findViewById(R.id.buttonZmazatDB)
        val issueButton: Button = findViewById(R.id.buttonIssue)

        emailButton.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sendemail, null)
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()

            val spinnerDovod = dialogView.reasonEmail
            val emailText = dialogView.emailBody
            val buttonSpat = dialogView.buttonSpatDialog
            val buttonSubmit = dialogView.buttonPotvrditDialog
            val chkBox = dialogView.chkAck
            val recipients = dialogView.recipientSpinner

            var actions = dbMethods.getActions()

            var list_of_items = arrayOf("Všeobecne")
            if(actions.size > 0)
            {
                for(i in 0 until actions.size)
                {
                    list_of_items += actions[i]
                }
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item
                , list_of_items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDovod!!.adapter = adapter
            spinnerDovod.setSelection(0)

            var namesList: Array<String>

            namesList = dbMethods.getAllApprovedNames().split("?").toTypedArray()

            var list_of_names = arrayOf("Vyberte adresáta","Všetci")
            if(namesList.size > 0)
            {
                for(i in 0 until namesList.size-1)
                {
                    list_of_names += namesList[i]
                }
            }

            var listVOs = ArrayList<StateVO>()

            for(i in 0 until list_of_names.size)
            {
                var stateVO = StateVO()
                stateVO.title = list_of_names[i]
                stateVO.isSelected = false
                listVOs.add(stateVO)
            }

            val adapterRecipients = MyAdapter(this, 0, listVOs)
            recipients!!.adapter = adapterRecipients



            buttonSpat.setOnClickListener{
                mAlertDialog.dismiss()
            }

            buttonSubmit.setOnClickListener {
                var text = emailText.text.toString()

                if(text != null || (text.replace(" ", "")) != "")
                {
                    if(chkBox.isChecked) {
                        //dbMethods.sendEmail() //dorobit vyber mien do dialog_sendemail
                    }
                    else
                        Toast.makeText(this, "Nezaškrtli ste, že rozumiete ako táto funkcia funguje", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this, "Nie je možné poslať prázdnu správu", Toast.LENGTH_SHORT).show()
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
