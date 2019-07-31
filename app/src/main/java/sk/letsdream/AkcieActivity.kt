package sk.letsdream

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.content_login.*
import org.w3c.dom.Text
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.EmailMethods
import sk.letsdream.helperMethods.HexMethods
import sk.letsdream.helperMethods.TimeMethods
import java.lang.Exception
import java.net.URL
import java.security.MessageDigest


class AkcieActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var privileges: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akcie)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val timeMethod: TimeMethods = TimeMethods()
        val hexMethods: HexMethods = HexMethods()

        timeMethod.UpdateActualTime(date,time)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)




        val vyberAkcieSpinner: ImageButton = findViewById(R.id.vyberakcieSPINNER)
        val nazovAkcieVedlaSpinnera: TextView = findViewById(R.id.akciaFromSpinner)
        val changePrivileges: ImageView = findViewById(R.id.changePriv)
        val vytvoritAkciu: TextView = findViewById(R.id.makeNewAction)
        val nazovAkcieZoSpinnera: TextView = findViewById(R.id.actionName)
        val pocetDobr: TextView = findViewById(R.id.pocetDobrET)
        val pocetNavs: TextView = findViewById(R.id.pocetNavsET)
        val datum: TextView = findViewById(R.id.datumET)
        val casOd: TextView = findViewById(R.id.casOd)
        val casDo: TextView = findViewById(R.id.casDo)
        val upravitDobr: TextView = findViewById(R.id.upravitDobrovol)
        val upravitNavs: TextView = findViewById(R.id.upravitNavstev)
        val upravitDatum: TextView = findViewById(R.id.upravitDatum)
        val upravitCas: TextView = findViewById(R.id.upravitCas)
        val adminUserLabel: TextView = findViewById(R.id.adminUserTW)

        var popUpMenu: PopupMenu = PopupMenu(this, vyberAkcieSpinner)
        popUpMenu.inflate(R.menu.temp_menu)

        try {
            nazovAkcieVedlaSpinnera.text = popUpMenu.menu.getItem(0).toString()
            nazovAkcieZoSpinnera.text = popUpMenu.menu.getItem(0).toString()
        }
        catch (e:Exception)
        {
            nazovAkcieVedlaSpinnera.text = "V databáze neexistuje žiadna akcia."
            nazovAkcieZoSpinnera.text = "V databáze neexistuje žiadna akcia."
        }

        vyberAkcieSpinner.setOnClickListener{
            popUpMenu.setOnMenuItemClickListener {
                nazovAkcieVedlaSpinnera.setText(it.title.toString())
                nazovAkcieZoSpinnera.setText(it.title.toString())
                true
            }
            popUpMenu.show()


        }
        //PHP funguje... uz len vytvorit connection z DB na LABELY
        vytvoritAkciu.visibility = View.INVISIBLE
        upravitDobr.visibility = View.INVISIBLE
        upravitNavs.visibility = View.INVISIBLE
        upravitDatum.visibility = View.INVISIBLE
        upravitCas.visibility = View.INVISIBLE
        adminUserLabel.text = "Používateľ"

        changePrivileges.setOnClickListener{
            if(privileges.toLowerCase()=="1" && adminUserLabel.text == "Používateľ")
            {
                Toast.makeText(this,"K tejto funkcii ma prístup iba administrátor!", Toast.LENGTH_LONG).show()
            }
            else if(privileges.toLowerCase() == "11" && adminUserLabel.text == "Používateľ")
            {
                Toast.makeText(this,"Zapnutý admin mód!", Toast.LENGTH_LONG).show()
                vytvoritAkciu.visibility = View.VISIBLE
                upravitDobr.visibility = View.VISIBLE
                upravitNavs.visibility = View.VISIBLE
                upravitDatum.visibility = View.VISIBLE
                upravitCas.visibility = View.VISIBLE
                adminUserLabel.text = "Administrátor"
            }
            else if(privileges.toLowerCase() == "11" && adminUserLabel.text == "Administrátor")
            {
                Toast.makeText(this,"Zapnutý používateľský mód!", Toast.LENGTH_LONG).show()
                vytvoritAkciu.visibility = View.INVISIBLE
                upravitDobr.visibility = View.INVISIBLE
                upravitNavs.visibility = View.INVISIBLE
                upravitDatum.visibility = View.INVISIBLE
                upravitCas.visibility = View.INVISIBLE
                adminUserLabel.text = "Používateľ"
            }
            else
            {
                Toast.makeText(this,"Nastala chyba! Nerozpoznaná rola používateľa", Toast.LENGTH_LONG).show()
                vytvoritAkciu.visibility = View.INVISIBLE
                upravitDobr.visibility = View.INVISIBLE
                upravitNavs.visibility = View.INVISIBLE
                upravitDatum.visibility = View.INVISIBLE
                upravitCas.visibility = View.INVISIBLE
                adminUserLabel.text = "Používateľ"
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
