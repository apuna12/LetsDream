package sk.letsdream

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.support.annotation.NonNull
import android.support.design.widget.NavigationView
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import java.lang.Exception
import java.net.URL
import java.security.MessageDigest
import android.widget.TextView
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_fullpoznamka.view.*
import kotlinx.android.synthetic.main.dialog_register.view.*
import pub.devrel.easypermissions.EasyPermissions
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.system.exitProcess


class LoginActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)
        val progressBar = findViewById<ProgressBar>(R.id.progress_circular)

        val timeMethod: TimeMethods = TimeMethods()
        val hexMethods: HexMethods = HexMethods()

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

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        navView.setNavigationItemSelectedListener(this)
        val dbMethods: DBConnection = DBConnection()
        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val emailMethods: EmailMethods = EmailMethods()
        val initialSetup: InitialSetup = InitialSetup()
        var networkTask: NetworkTask


        var packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        var curVersionCode = packageInfo.versionCode
        var newVersionCode = dbMethods.checkNewVersionCode().toInt()


        if(curVersionCode < newVersionCode)
        {
            lateinit var dialog: AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Aktualizácia")
            builder.setMessage("Je dostupná aktualizácia aplikácie Let's Dream. Stiahnúť?")
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        vibrate.vibrate(70)

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED)
                            {
                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
                            }
                            else
                            {
                                startDownloading()

                            }
                        }
                        else {
                            startDownloading()
                        }


                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        vibrate.vibrate(70)
                        dialog.dismiss()
                        moveTaskToBack(true)
                        exitProcess(-1)
                    }
                }
            }
            builder.setPositiveButton("Áno", dialogClickListener)

            builder.setNegativeButton("Nie", dialogClickListener)

            dialog = builder.create()

            dialog.show()
        }

        if (isOnline(this)) {
            if (initialSetup.initialDBCreation() != "111") {
                if (dbMethods.checkSuperUser() == "0") {
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    initialSetup.CreateSuperAdmin(this)
                }
                else
                    Toast.makeText(
                        this,
                        "Hups! Niečo sa stalo. Skúste neskôr",
                        Toast.LENGTH_SHORT
                    ).show()
            } else if (initialSetup.initialDBCreation() == "111") {

                if (dbMethods.checkSuperUser() == "0") {
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    initialSetup.CreateSuperAdmin(this)
                }

                val showPassChb: CheckBox = findViewById(R.id.showPassChb)
                var passwordEdt: EditText = findViewById(R.id.passwordEdt)
                var usernameEdt: EditText = findViewById(R.id.userEdt)
                val registerBtn: Button = findViewById(R.id.registraciaBtn)
                val loginBtn: Button = findViewById(R.id.prihlasBtn)
                val oNas: TextView = findViewById(R.id.textView21)

                usernameEdt.setOnFocusChangeListener { v, hasFocus ->
                    vibrate.vibrate(70)
                    if (hasFocus) {
                        usernameEdt.hint = ""
                    } else {
                        if (usernameEdt.text.toString() == "")
                            usernameEdt.hint = "Používateľ"
                    }
                }

                oNas.setOnClickListener{
                    vibrate.vibrate(70)

                    val dialogView =
                        LayoutInflater.from(this).inflate(R.layout.dialog_about, null)
                    val mBuilder = AlertDialog.Builder(this).setView(dialogView)
                    dialogView.fullPoznDialog.filters =
                        arrayOf(*dialogView.fullPoznDialog.filters, InputFilter.LengthFilter(100))
                    val mAlertDialog = mBuilder.show()
                    dialogView.buttonSpatDialog.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }

                usernameEdt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    vibrate.vibrate(70)
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        passwordEdt.requestFocus()
                        return@OnKeyListener true
                    }
                    false
                })

                passwordEdt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    vibrate.vibrate(70)
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                        loginBtn.performClick()
                        return@OnKeyListener true
                    }
                    false
                })

                passwordEdt.setOnFocusChangeListener { v, hasFocus ->
                    vibrate.vibrate(70)
                    if (hasFocus) {
                        passwordEdt.hint = ""
                    } else {
                        if (passwordEdt.text.toString() == "")
                            passwordEdt.hint = "Heslo"
                    }
                }

                showPassChb.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                    vibrate.vibrate(70)
                    if (b)
                        passwordEdt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    else
                        passwordEdt.inputType = 129
                }

                registerBtn.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {


                        val dialogView =
                            LayoutInflater.from(this).inflate(R.layout.dialog_register, null)
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
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                userRegister.hint = ""
                            } else {
                                if (userRegister.text.toString() == "")
                                    userRegister.hint = "Používateľ"
                            }
                        }
                        passRegister.setOnFocusChangeListener { v, hasFocus ->
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                passRegister.hint = ""
                            } else {
                                if (passRegister.text.toString() == "")
                                    passRegister.hint = "Heslo"
                            }
                        }
                        passAgainRegister.setOnFocusChangeListener { v, hasFocus ->
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                passAgainRegister.hint = ""
                            } else {
                                if (passAgainRegister.text.toString() == "")
                                    passAgainRegister.hint = "Heslo znova"
                            }
                        }
                        emailRegister.setOnFocusChangeListener { v, hasFocus ->
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                emailRegister.hint = ""
                            } else {
                                if (emailRegister.text.toString() == "")
                                    emailRegister.hint = "Email"
                            }
                        }

                        nameRegister.setOnFocusChangeListener { v, hasFocus ->
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                emailRegister.hint = ""
                            } else {
                                if (emailRegister.text.toString() == "")
                                    emailRegister.hint = "Zadajte meno"
                            }
                        }
                        surnameRegister.setOnFocusChangeListener { v, hasFocus ->
                            vibrate.vibrate(70)
                            if (hasFocus) {
                                emailRegister.hint = ""
                            } else {
                                if (emailRegister.text.toString() == "")
                                    emailRegister.hint = "Zadajte priezvisko"
                            }
                        }

                        userRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                passRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        passRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                passAgainRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        passAgainRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                nameRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        nameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                surnameRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                emailRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                emailRegister.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })
                        emailRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                            vibrate.vibrate(70)
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                                gdprCHB.requestFocus()
                                return@OnKeyListener true
                            }
                            false
                        })

                        dialogView.registrujBtn.setOnClickListener {

                            vibrate.vibrate(70)
                            networkTask = NetworkTask(this)
                            networkTask.execute()
                            if (userRegister.text.toString() == "") {
                                Toast.makeText(this, "Prosím zadajte meno účtu!", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (passRegister.text.toString() == "") {
                                Toast.makeText(this, "Prosím zadajte heslo!", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (passAgainRegister.text.toString() == "") {
                                Toast.makeText(
                                    this,
                                    "Prosím zadajte znova heslo!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else if (emailRegister.text.toString() == "") {
                                Toast.makeText(
                                    this,
                                    "Prosím zadajte emailovú adresu!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else if (!emailMethods.isEmailValid(emailRegister.text.toString().trim())) {
                                Toast.makeText(
                                    this,
                                    "Prosím zadajte správnu emailovú adresu!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (passRegister.text.toString() != passAgainRegister.text.toString()) {
                                Toast.makeText(this, "Vaše heslá sa nezhodujú!", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (nameRegister.text.toString() == "") {
                                Toast.makeText(this, "Zadajte prosím meno!", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (surnameRegister.text.toString() == "") {
                                Toast.makeText(
                                    this,
                                    "Zadajte prosím priezvisko!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else if (!gdprCHB.isChecked) {
                                Toast.makeText(
                                    this,
                                    "Pre úspešne zaregistrovanie musíte súhlasiť so spracovaním údajov!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                val sql =
                                    "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&mod=register&rest=get"

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
                                    if (jsonStr == "1")
                                        Toast.makeText(
                                            this,
                                            "Toto meno už používa niekto iný",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    else {

                                        var digest: MessageDigest
                                        digest = MessageDigest.getInstance("SHA-256")
                                        digest.update(passRegister.text.toString().toByteArray())
                                        var hash = hexMethods.bytesToHexString(digest.digest())

                                        val sql =
                                            "http://letsdream.xf.cz/index.php?username=" +
                                                    userRegister.text + "&password=" + hash +
                                                    "&email=" + emailRegister.text.toString().trim() +
                                                    "&name=" + surnameRegister.text + ",_" +
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
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this, LoginActivity::class.java)
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Hups, niečo je zlé!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                            vibrate.vibrate(70)
                            mAlertDialog.cancel()
                        }
                    } else
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet",
                            Toast.LENGTH_SHORT
                        ).show()


                }

                loginBtn.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

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
                                    Toast.makeText(this, "Nesprávne heslo", Toast.LENGTH_SHORT)
                                        .show()
                                } else if (jsonStr == "1") // User
                                {
                                    Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_SHORT)
                                        .show()
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("privileges", "1")
                                    intent.putExtra("login", usernameEdt.text.toString())
                                    startActivity(intent)

                                } else if (jsonStr == "11") // Admin
                                {
                                    Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_SHORT)
                                        .show()
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("privileges", "11")
                                    intent.putExtra("login", usernameEdt.text.toString())
                                    startActivity(intent)

                                } else if (jsonStr == "111") // Super Admin
                                {
                                    Toast.makeText(this, "Prihlásenie úspešné", Toast.LENGTH_SHORT)
                                        .show()
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("privileges", "111")
                                    intent.putExtra("login", usernameEdt.text.toString())
                                    startActivity(intent)

                                } else if (jsonStr == "2") {
                                    Toast.makeText(
                                        this,
                                        "Daný užívateľ neexistuje",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                } else if (jsonStr == "3") {
                                    Toast.makeText(
                                        this,
                                        "Vaša registrácia nebola potvrdená administrátorom!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Niekde nastala chyba. Máte prístup k internetu?",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                            }
                        } catch (e: Exception) {
                            throw Exception(e)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                this,
                "Hups! Nie ste pripojený na internet. Zapnite si internet a reštartujte aplikáciu.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun startDownloading() {

        var networkTask = NetworkTask(this)
        networkTask.execute()

        val url = "http://letsdream.xf.cz/index.php?mod=downloadNewVersion&rest=get"

        var downloadmanager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var uri = Uri.parse(url)

        var request = DownloadManager.Request(uri)

        var filename = "app-release.apk"

        var fullPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                    "/" + filename


        val myFile = File(fullPath)
        if (myFile.exists())
            myFile.delete()

        request.setTitle("Let's Dream")
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false)
        request.setDestinationUri(
            Uri.parse("file://" + fullPath)
        )

        downloadmanager.enqueue(request)

        var onComplete:BroadcastReceiver = object:BroadcastReceiver() {
            override fun onReceive(context:Context, intent:Intent) {


                myFile.setReadable(true, false)

                if(Build.VERSION.SDK_INT > 23) {
                    val uri = FileProvider.getUriForFile(
                        this@LoginActivity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        myFile
                    )
                    val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    startActivity(intent)
                }
                else
                {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.fromFile(myFile), "application/vnd.android.package-archive")
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            1000 ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startDownloading()
                }
                else
                {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pre ukončenie aplikácie kliknite 'Späť' 2x po sebe. ", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

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
