package sk.letsdream

import android.app.AlertDialog
import android.content.Intent
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
import android.text.InputFilter
import android.text.InputType
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.content_login.*
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.EmailMethods
import sk.letsdream.helperMethods.HexMethods
import sk.letsdream.helperMethods.TimeMethods
import java.lang.Exception
import java.net.URL
import java.security.Key
import java.security.MessageDigest
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_addnewaction.view.*
import kotlinx.android.synthetic.main.dialog_register.view.*
import sk.letsdream.dbMethods.DBConnection


class LoginActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
        val dbMethods: DBConnection = DBConnection()

        if(dbMethods.checkSuperUser()=="0")
        {
            //pridanie registracie superadmina
        }
        else {


            val showPassChb: CheckBox = findViewById(R.id.showPassChb)
            var passwordEdt: EditText = findViewById(R.id.passwordEdt)
            var usernameEdt: EditText = findViewById(R.id.userEdt)
            val registerBtn: Button = findViewById(R.id.registraciaBtn)
            val loginBtn: Button = findViewById(R.id.prihlasBtn)

            usernameEdt.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    usernameEdt.hint = ""
                } else {
                    if (usernameEdt.text.toString() == "")
                        usernameEdt.hint = "Používateľ"
                }
            }

            usernameEdt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    passwordEdt.requestFocus()
                    return@OnKeyListener true
                }
                false
            })

            passwordEdt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    loginBtn.performClick()
                    return@OnKeyListener true
                }
                false
            })

            passwordEdt.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    passwordEdt.hint = ""
                } else {
                    if (passwordEdt.text.toString() == "")
                        passwordEdt.hint = "Heslo"
                }
            }

            showPassChb.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                if (b)
                    passwordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                else
                    passwordEdt.inputType = 129
            }

            registerBtn.setOnClickListener {
                val emailMethods: EmailMethods = EmailMethods()
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null)
                val mBuilder = AlertDialog.Builder(this).setView(dialogView)
                val mAlertDialog = mBuilder.show()
                val userRegister = dialogView.userEdt
                val passRegister = dialogView.passwordRegEdt
                val passAgainRegister = dialogView.passwordAgainRegEdt
                val emailRegister = dialogView.emailRegEdt
                val nameRegister = dialogView.nameRegEdt
                val surnameRegister = dialogView.surnameRegEdt
                val back = dialogView.backRegBtn
                val gdprCHB = dialogView.gdprCHB
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

                nameRegister.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        emailRegister.hint = ""
                    } else {
                        if (emailRegister.text.toString() == "")
                            emailRegister.hint = "Zadajte meno"
                    }
                }
                surnameRegister.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        emailRegister.hint = ""
                    } else {
                        if (emailRegister.text.toString() == "")
                            emailRegister.hint = "Zadajte priezvisko"
                    }
                }

                userRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        passRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                passRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        passAgainRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                passAgainRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        nameRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                nameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        surnameRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        emailRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        emailRegister.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })
                emailRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        gdprCHB.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })

                dialogView.registrujBtn.setOnClickListener {
                    if (userRegister.text.toString() == "") {
                        Toast.makeText(this, "Prosím zadajte meno účtu!", Toast.LENGTH_LONG).show()
                    } else if (passRegister.text.toString() == "") {
                        Toast.makeText(this, "Prosím zadajte heslo!", Toast.LENGTH_LONG).show()
                    } else if (passAgainRegister.text.toString() == "") {
                        Toast.makeText(this, "Prosím zadajte znova heslo!", Toast.LENGTH_LONG)
                            .show()
                    } else if (emailRegister.text.toString() == "") {
                        Toast.makeText(this, "Prosím zadajte emailovú adresu!", Toast.LENGTH_LONG)
                            .show()
                    } else if (!emailMethods.isEmailValid(emailRegister.text.toString())) {
                        Toast.makeText(
                            this,
                            "Prosím zadajte správnu emailovú adresu!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (passRegister.text.toString() != passAgainRegister.text.toString()) {
                        Toast.makeText(this, "Vaše heslá sa nezhodujú!", Toast.LENGTH_LONG).show()
                    } else if (nameRegister.text.toString() == "") {
                        Toast.makeText(this, "Zadajte prosím meno!", Toast.LENGTH_LONG).show()
                    } else if (surnameRegister.text.toString() == "") {
                        Toast.makeText(this, "Zadajte prosím priezvisko!", Toast.LENGTH_LONG).show()
                    } else if (!gdprCHB.isChecked) {
                        Toast.makeText(
                            this,
                            "Pre úspešne zaregistrovanie musíte súhlasiť so spracovaním údajov!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {

                        val sql =
                            "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&mod=register&rest=get"

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
                                Toast.makeText(
                                    this,
                                    "Toto meno už používa niekto iný",
                                    Toast.LENGTH_LONG
                                ).show()
                            else {

                                var digest: MessageDigest
                                digest = MessageDigest.getInstance("SHA-256")
                                digest.update(passRegister.text.toString().toByteArray())
                                var hash = hexMethods.bytesToHexString(digest.digest())

                                val sql =
                                    "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&password=" + hash +
                                            "&email=" + emailRegister.text + "&name=" + surnameRegister.text + ",_" +
                                            nameRegister.text + "&mod=register&rest=post"

                                try {
                                    var jsonStr: String = URL(sql).readText()
                                    var firstApp: Int = 0
                                    var lastApp: Int = 0
                                    if (jsonStr.toString().contains("<") || jsonStr.toString().contains(
                                            ">"
                                        )
                                    ) {
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
                                    if (jsonStr.contains("1")) {
                                        Toast.makeText(
                                            this,
                                            "Používateľ úspešne registrovaný!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Hups, niečo je zlé!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } catch (e: Exception) {
                                    throw Exception(e)
                                }
                            }
                        } catch (e: Exception) {
                            throw Exception(e)
                        }
                    }
                }
                back.setOnClickListener {
                    mAlertDialog.cancel()
                }

            }

            loginBtn.setOnClickListener {

                var digest: MessageDigest
                digest = MessageDigest.getInstance("SHA-256")
                digest.update(passwordEdt.text.toString().toByteArray())
                var hash = hexMethods.bytesToHexString(digest.digest())

                val sql =
                    "http://letsdream.xf.cz/index.php?username=" + usernameEdt.text + "&password=" + hash + "&mod=login&rest=get"

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
                        if (jsonStr == "0") {
                            Toast.makeText(this, "Nesprávne heslo", Toast.LENGTH_LONG).show()
                        } else if (jsonStr == "1") // User
                        {
                            Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("privileges", "1")
                            startActivity(intent)
                        } else if (jsonStr == "11") // Admin
                        {
                            Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("privileges", "11")
                            startActivity(intent)
                        } else if (jsonStr == "111") // Super Admin
                        {
                            Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("privileges", "111")
                            startActivity(intent)
                        } else if (jsonStr == "2") {
                            Toast.makeText(this, "Daný užívateľ neexistuje", Toast.LENGTH_LONG)
                                .show()
                        } else if (jsonStr == "3") {
                            Toast.makeText(
                                this,
                                "Vaša registrácia nebola potvrdená administrátorom!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Niekde nastala chyba. Máte prístup k internetu?",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
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
