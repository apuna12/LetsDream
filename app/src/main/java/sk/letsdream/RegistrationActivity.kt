package sk.letsdream

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.content_register.*
import sk.letsdream.helperMethods.EmailMethods
import sk.letsdream.helperMethods.HexMethods
import sk.letsdream.helperMethods.TimeMethods
import java.lang.Exception
import java.net.URL
import java.security.MessageDigest

class RegistrationActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val timeMethod: TimeMethods = TimeMethods()

        timeMethod.UpdateActualTime(date, time)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val hexMethods: HexMethods = HexMethods()
        val emailMethods: EmailMethods = EmailMethods()


        val userRegister: EditText = findViewById(R.id.userEdt)
        val passRegister: EditText = findViewById(R.id.passwordRegEdt)
        val passAgainRegister: EditText = findViewById(R.id.passwordAgainRegEdt)
        val emailRegister: EditText = findViewById(R.id.emailRegEdt)
        val submit: Button = findViewById(R.id.registrujBtn)


        userRegister.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                userRegister.hint = ""
            } else {
                if (userRegister.text.toString() == "")
                    userRegister.hint = "Používateľ"
            }
        }
        passRegister.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                passRegister.hint = ""
            } else {
                if (passRegister.text.toString() == "")
                    passRegister.hint = "Heslo"
            }
        }
        passAgainRegister.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                passAgainRegister.hint = ""
            } else {
                if (passAgainRegister.text.toString() == "")
                    passAgainRegister.hint = "Heslo znova"
            }
        }
        emailRegister.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                emailRegister.hint = ""
            } else {
                if (emailRegister.text.toString() == "")
                    emailRegister.hint = "Email"
            }
        }

        submit.setOnClickListener {
            if (userRegister.text.toString() == "") {
                Toast.makeText(this, "Prosím zadajte meno účtu!", Toast.LENGTH_LONG).show()
            } else if (passwordRegEdt.text.toString() == "") {
                Toast.makeText(this, "Prosím zadajte heslo!", Toast.LENGTH_LONG).show()
            } else if (passAgainRegister.text.toString() == "") {
                Toast.makeText(this, "Prosím zadajte znova heslo!", Toast.LENGTH_LONG).show()
            } else if (emailRegister.text.toString() == "") {
                Toast.makeText(this, "Prosím zadajte emailovú adresu!", Toast.LENGTH_LONG).show()
            } else if (!emailMethods.isEmailValid(emailRegister.text.toString())) {
                Toast.makeText(this, "Prosím zadajte správnu emailovú adresu!", Toast.LENGTH_LONG).show()
            } else if (passRegister.text.toString() != passAgainRegister.text.toString()) {
                Toast.makeText(this, "Vaše heslá sa nezhodujú!", Toast.LENGTH_LONG).show()
            } else {

                val sql = "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&mod=register&rest=get"

                try {
                    var jsonStr: String = URL(sql).readText()
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
                        jsonStr = jsonStr.removeRange(firstApp, lastApp + 1)
                    }
                    if (jsonStr == "1")
                        Toast.makeText(this, "Toto meno už používa niekto iný", Toast.LENGTH_LONG).show()
                    else {

                        var digest: MessageDigest
                        digest = MessageDigest.getInstance("SHA-256")
                        digest.update(passwordRegEdt.text.toString().toByteArray())
                        var hash = hexMethods.bytesToHexString(digest.digest())

                        val sql =
                            "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&password=" + hash +
                                    "&email=" + emailRegister.text + "&mod=register&rest=post"

                        try
                        {
                            var jsonStr: String = URL(sql).readText()
                            var firstApp: Int = 0
                            var lastApp: Int = 0
                            if (jsonStr.toString().contains("<") || jsonStr.toString().contains(">"))
                            {
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
                                jsonStr = jsonStr.removeRange(firstApp, lastApp + 1)
                            }
                            if(jsonStr.contains("1"))
                            {
                                Toast.makeText(this,"Používateľ úspešne registrovaný!", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                            else
                            {
                                Toast.makeText(this,"Hups, niečo je zlé!", Toast.LENGTH_LONG).show()
                            }

                        }
                        catch (e: Exception) {
                            throw Exception(e)
                        }
                    }
                }
                catch (e: Exception)
                {
                    throw Exception(e)
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
