package sk.letsdream

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.os.Vibrator
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.dialog_addnewaction.view.*
import kotlinx.android.synthetic.main.dialog_fullpoznamka.view.*
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.*
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat



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
        var networkTask: NetworkTask


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

        val dbMethods: DBConnection = DBConnection()
        val updateLabelMethods: UpdateLabelMethods = UpdateLabelMethods()
        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        val vyberAkcieSpinner: ImageButton = findViewById(R.id.vyberakcieSPINNER)
        val nazovAkcieVedlaSpinnera: TextView = findViewById(R.id.akciaFromSpinner)
        val changePrivileges: ImageView = findViewById(R.id.changePriv)
        val vytvoritAkciu: TextView = findViewById(R.id.makeNewAction)
        val nazovAkcieZoSpinnera: TextView = findViewById(R.id.actionName)
        val pocetDobr: TextView = findViewById(R.id.pocetDobrET)
        val pocetNavs: TextView = findViewById(R.id.pocetNavsET)
        val poznamka: TextView = findViewById(R.id.poznamkaET)
        val datum: TextView = findViewById(R.id.datumET)
        val casOd: TextView = findViewById(R.id.casOd)
        val casDo: TextView = findViewById(R.id.casDo)
        val upravitDobr: TextView = findViewById(R.id.upravitDobrovol)
        val upravitNavs: TextView = findViewById(R.id.upravitNavstev)
        val upravitDatum: TextView = findViewById(R.id.upravitDatum)
        val upravitCas: TextView = findViewById(R.id.upravitCas)
        val upravitPozn: TextView = findViewById(R.id.upravitPozn)
        val adminUserLabel: TextView = findViewById(R.id.adminUserTW)
        val deleteAction: TextView = findViewById(R.id.deleteAction)


        poznamka.filters = arrayOf(*poznamka.filters, InputFilter.LengthFilter(100))

        var akcieList: Array<String>
        akcieList = arrayOf<String>()
        val sql = "http://letsdream.xf.cz/index.php?mod=getAllActions&rest=get"

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
                    Toast.makeText(this, "Hups! Niekde nastala chyba", Toast.LENGTH_SHORT).show()
                } else {
                    akcieList = jsonStr.split(",").toTypedArray()
                    akcieList = akcieList.dropLast(1).toTypedArray()
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }

        val wrapper: Context = ContextThemeWrapper(this, R.style.popupMenuStyle)
        var popUpMenu: PopupMenu = PopupMenu(wrapper, vyberAkcieSpinner)
        if (akcieList.size > 0) {
            for (i in 0 until akcieList.size) {
                if (akcieList[i] != null || akcieList[i] != "")
                    popUpMenu.menu.add(akcieList[i])
            }
        }

        try {
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {
                var actionName: String = popUpMenu.menu.getItem(0).toString()
                nazovAkcieVedlaSpinnera.text = actionName
                nazovAkcieZoSpinnera.text = actionName
                val statsArray: Array<String> =
                    dbMethods.getLabelStatistics(actionName.replace(" ", "_"))
                if (statsArray.get(0) != "NaN") {
                    pocetDobr.text = statsArray.get(0)
                    pocetNavs.text = statsArray.get(1)
                    datum.text = statsArray.get(2)
                    casOd.text = statsArray.get(3)
                    casDo.text = statsArray.get(4)
                    poznamka.text = statsArray.get(5)
                } else
                    Toast.makeText(this, "Hups! Niečo je zlé", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        } catch (e: Exception) {
            nazovAkcieVedlaSpinnera.text = "V databáze neexistuje žiadna akcia."
            nazovAkcieZoSpinnera.text = "V databáze neexistuje žiadna akcia."
        }

        vyberAkcieSpinner.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                popUpMenu.setOnMenuItemClickListener {
                    vibrate.vibrate(70)
                    var actionName: String = it.title.toString()
                    nazovAkcieVedlaSpinnera.setText(actionName)
                    nazovAkcieZoSpinnera.setText(actionName)
                    val statsArray: Array<String> = dbMethods.getLabelStatistics(actionName)
                    if (statsArray.get(0) != "NaN") {
                        pocetDobr.text = statsArray.get(0)
                        pocetNavs.text = statsArray.get(1)
                        datum.text = statsArray.get(2)
                        casOd.text = statsArray.get(3)
                        casDo.text = statsArray.get(4)
                        poznamka.text = statsArray.get(5)
                    } else
                        Toast.makeText(this, "Hups! Niečo je zlé", Toast.LENGTH_SHORT).show()
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
        vytvoritAkciu.visibility = View.INVISIBLE
        upravitDobr.visibility = View.INVISIBLE
        upravitNavs.visibility = View.INVISIBLE
        upravitDatum.visibility = View.INVISIBLE
        upravitCas.visibility = View.INVISIBLE
        upravitPozn.visibility = View.INVISIBLE
        deleteAction.visibility = View.INVISIBLE

        if (privileges.toLowerCase() == "11" || privileges.toLowerCase() == "111") {
            adminUserLabel.text = "Používateľ"
            adminUserLabel.visibility = View.VISIBLE
        } else {
            adminUserLabel.visibility = View.INVISIBLE
            changePrivileges.visibility = View.INVISIBLE

        }


        changePrivileges.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (privileges.toLowerCase() == "1" && adminUserLabel.text == "Používateľ") {
                Toast.makeText(
                    this,
                    "K tejto funkcii ma prístup iba administrátor!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if ((privileges.toLowerCase() == "11" || privileges.toLowerCase() == "111") && adminUserLabel.text == "Používateľ") {
                Toast.makeText(this, "Zapnutý admin mód!", Toast.LENGTH_SHORT).show()
                vytvoritAkciu.visibility = View.VISIBLE
                upravitDobr.visibility = View.VISIBLE
                upravitNavs.visibility = View.VISIBLE
                upravitDatum.visibility = View.VISIBLE
                upravitCas.visibility = View.VISIBLE
                upravitPozn.visibility = View.VISIBLE
                adminUserLabel.text = "Administrátor"
                deleteAction.visibility = View.VISIBLE
                adminUserLabel.visibility = View.VISIBLE
                changePrivileges.visibility = View.VISIBLE

            } else if ((privileges.toLowerCase() == "11" || privileges.toLowerCase() == "111") && adminUserLabel.text == "Administrátor") {
                Toast.makeText(this, "Zapnutý používateľský mód!", Toast.LENGTH_SHORT).show()
                vytvoritAkciu.visibility = View.INVISIBLE
                upravitDobr.visibility = View.INVISIBLE
                upravitNavs.visibility = View.INVISIBLE
                upravitDatum.visibility = View.INVISIBLE
                upravitCas.visibility = View.INVISIBLE
                upravitPozn.visibility = View.INVISIBLE
                adminUserLabel.text = "Používateľ"
                deleteAction.visibility = View.INVISIBLE
            } else {
                Toast.makeText(
                    this,
                    "Nastala chyba! Nerozpoznaná rola používateľa",
                    Toast.LENGTH_SHORT
                ).show()
                vytvoritAkciu.visibility = View.INVISIBLE
                upravitDobr.visibility = View.INVISIBLE
                upravitNavs.visibility = View.INVISIBLE
                upravitDatum.visibility = View.INVISIBLE
                upravitCas.visibility = View.INVISIBLE
                adminUserLabel.text = "Používateľ"
                deleteAction.visibility = View.INVISIBLE
                adminUserLabel.visibility = View.INVISIBLE
                changePrivileges.visibility = View.INVISIBLE
            }
        }

        upravitCas.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                updateLabelMethods.updateTimeLabel(this, casOd, casDo, nazovAkcieVedlaSpinnera)
            }
            Toast.makeText(
                this,
                "Hups! Nie ste pripojený na internet.",
                Toast.LENGTH_SHORT
            ).show()
        }
        upravitDobr.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                updateLabelMethods.updateNumberLabels(
                    this,
                    upravitDobr,
                    pocetDobr,
                    nazovAkcieVedlaSpinnera,
                    "pocDobr"
                )
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        upravitNavs.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                updateLabelMethods.updateNumberLabels(
                    this,
                    upravitNavs,
                    pocetNavs,
                    nazovAkcieVedlaSpinnera,
                    "pocNavs"
                )
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        upravitDatum.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                updateLabelMethods.updateDateLabel(
                    this,
                    upravitDatum,
                    datum,
                    nazovAkcieVedlaSpinnera
                )
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        upravitPozn.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                updateLabelMethods.updatePoznLabels(this, poznamka, nazovAkcieVedlaSpinnera)
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        poznamka.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val dialogView =
                    LayoutInflater.from(this).inflate(R.layout.dialog_fullpoznamka, null)
                val mBuilder = AlertDialog.Builder(this).setView(dialogView)
                dialogView.fullPoznDialog.filters =
                    arrayOf(*dialogView.fullPoznDialog.filters, InputFilter.LengthFilter(100))
                dialogView.fullPoznDialog.text = poznamka.text
                val mAlertDialog = mBuilder.show()
                dialogView.buttonSpatDialog.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            } else
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
        }
        vytvoritAkciu.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val dialogView =
                    LayoutInflater.from(this).inflate(R.layout.dialog_addnewaction, null)
                val mBuilder = AlertDialog.Builder(this).setView(dialogView)
                dialogView.actionNameAddAction.filters =
                    arrayOf(*dialogView.actionNameAddAction.filters, InputFilter.LengthFilter(100))
                dialogView.poznamkaAddAction.filters =
                    arrayOf(*dialogView.poznamkaAddAction.filters, InputFilter.LengthFilter(100))
                val mAlertDialog = mBuilder.show()
                dialogView.submitAddAction.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {
                        var parser = SimpleDateFormat("HH:mm")
                        var formatter = SimpleDateFormat("HH:mm")
                        var casOdTIME =
                            formatter.format(parser.parse(dialogView.casOdAddAction.text.toString()))
                        var casDoTIME =
                            formatter.format(parser.parse(dialogView.casDoAddAction.text.toString()))

                        if (dialogView.actionNameAddAction.text.toString() != "" &&
                            dialogView.dateAddAction.text.toString() != "" &&
                            dialogView.casOdAddAction.text.toString() != "" &&
                            dialogView.casDoAddAction.text.toString() != ""
                        ) {
                            if (casOdTIME < casDoTIME) {
                                val check = dbMethods.addNewAction(
                                    dialogView.actionNameAddAction.text.toString(),
                                    dialogView.pocDobrAddAction.text.toString(),
                                    dialogView.pocNavsAddAction.text.toString(),
                                    dialogView.dateAddAction.text.toString(),
                                    dialogView.casOdAddAction.text.toString(),
                                    dialogView.casDoAddAction.text.toString(),
                                    dialogView.poznamkaAddAction.text.toString()
                                )
                                if (check == "1") {
                                    mAlertDialog.dismiss()
                                    Toast.makeText(this, "Nová akcia pridaná", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                    startActivity(intent)
                                } else
                                    Toast.makeText(
                                        this,
                                        "Hups! Záznam nebol pridaný.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            } else
                                Toast.makeText(
                                    this,
                                    "Hups! Opravte si prosím čas.",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else
                            Toast.makeText(
                                this,
                                "Vyplňte prosím všetky povinné položky!",
                                Toast.LENGTH_SHORT
                            ).show()

                    } else {
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                dialogView.backAddAction.setOnClickListener {
                    vibrate.vibrate(70)
                    mAlertDialog.cancel()
                }

                dialogView.dateAddAction.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

                        timeMethod.SetDatePicker(this, dialogView.dateAddAction)
                    } else
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                dialogView.casOdAddAction.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

                        timeMethod.SetTimePicker(this, dialogView.casOdAddAction)
                    } else
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                dialogView.casDoAddAction.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

                        timeMethod.SetTimePicker(this, dialogView.casDoAddAction)
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
        deleteAction.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val alertDialog = AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Naozaj chcete vymazať akciu '" + nazovAkcieZoSpinnera.text.toString() + "' ?")
                    .setPositiveButton("Áno", DialogInterface.OnClickListener { dialog, i ->
                        vibrate.vibrate(70)
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        if (isOnline(this)) {

                            if (dbMethods.deleteAction(nazovAkcieVedlaSpinnera.text.toString()) == "1") {
                                finish()
                                startActivity(intent)
                                Toast.makeText(this, "Akcia úspešne vymazaná", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Hups! Akcia nebola vymazaná",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else
                            Toast.makeText(
                                this,
                                "Hups! Nie ste pripojený na internet.",
                                Toast.LENGTH_SHORT
                            ).show()
                    })
                    .setNegativeButton("Nie", DialogInterface.OnClickListener { dialog, i ->
                        vibrate.vibrate(70)
                        dialog.cancel()
                    })
                    .show()
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
