package sk.letsdream

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
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
import kotlinx.android.synthetic.main.dialog_fullrecipients.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_fullrecipients.view.*
import kotlinx.android.synthetic.main.dialog_issue.view.*
import kotlinx.android.synthetic.main.dialog_recipients.view.*
import kotlinx.android.synthetic.main.dialog_sendemail.view.*
import kotlinx.android.synthetic.main.dialog_sendemail.view.buttonPotvrditDialog
import kotlinx.android.synthetic.main.dialog_sendemail.view.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_sendemail.view.chkAck
import kotlinx.android.synthetic.main.dialog_sendemail.view.reasonEmail
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.NetworkTask
import sk.letsdream.helperMethods.TimeMethods


class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var privileges = ""
    var loginName = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        loginName = intent.getStringExtra("login")
        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

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

        val emailButton: Button = findViewById(R.id.buttonOdoslMail)
        val backupButton: Button = findViewById(R.id.buttonZalohaDB)
        val deleteButton: Button = findViewById(R.id.buttonZmazatDB)
        val issueButton: Button = findViewById(R.id.buttonIssue)

        emailButton.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sendemail, null)
                val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                val mAlertDialog = mBuilder.show()

                val spinnerDovod = dialogView.reasonEmail
                val emailText = dialogView.emailBody
                val buttonSpat = dialogView.buttonSpatDialog
                val buttonSubmit = dialogView.buttonPotvrditDialog
                val chkBox = dialogView.chkAck
                val spinnerRecipients = dialogView.recipientSpinner
                val recipientsTW = dialogView.textView17

                var actions = dbMethods.getActions()

                var list_of_items = arrayOf("Všeobecne")
                if (actions.size > 0) {
                    for (i in 0 until actions.size) {
                        list_of_items += actions[i]
                    }
                }

                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item
                    , list_of_items
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDovod!!.adapter = adapter
                spinnerDovod.setSelection(0)

                var namesList: Array<String>

                namesList = dbMethods.getAllApprovedNames().split("?").toTypedArray()

                var list_of_names = arrayOf("Všetci")
                if (namesList.size > 0) {
                    for (i in 0 until namesList.size - 1) {
                        list_of_names += namesList[i]
                    }
                }



                spinnerRecipients.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

                        val dialogView =
                            LayoutInflater.from(this).inflate(R.layout.dialog_recipients, null)
                        val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                        val mAlertDialog = mBuilder.show()

                        var regTable = dialogView.recipientsTable
                        var childCount = regTable.childCount
                        if (childCount > 1)
                            regTable.removeViews(1, childCount - 1)

                        var textView: TextView = TextView(this)
                        var textView2: TextView = TextView(this)
                        textView.setText("Meno")
                        textView.setTextColor(Color.BLACK)
                        textView.setPadding(10, 5, 0, 0)
                        textView.setTypeface(null, Typeface.BOLD)
                        textView.textSize = 20.toFloat()

                        textView2.setText("Pridať")
                        textView2.setTextColor(Color.BLACK)
                        textView2.setPadding(0, 5, 30, 0)
                        textView2.setTypeface(null, Typeface.BOLD)
                        textView2.textSize = 20.toFloat()
                        textView2.gravity = Gravity.RIGHT

                        var tableRowAllRegistered: TableRow = TableRow(this)
                        var lp: TableRow.LayoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                        )
                        tableRowAllRegistered.layoutParams = lp

                        tableRowAllRegistered.addView(textView)
                        tableRowAllRegistered.addView(textView2)
                        tableRowAllRegistered.background = resources.getDrawable(R.drawable.border)
                        regTable.addView(tableRowAllRegistered, 0)

                        for (i in 1 until list_of_names.size+1) {
                            var tableRowAllRegistered: TableRow = TableRow(this)
                            var lp: TableRow.LayoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                                )
                            )
                            tableRowAllRegistered.layoutParams = lp
                            var textView: TextView = TextView(this)
                            var checkbox: CheckBox = CheckBox(this)

                            textView.setText(list_of_names[i - 1])
                            textView.setTextColor(Color.BLACK)
                            textView.setPadding(10, 5, 0, 0)
                            checkbox.isChecked = false
                            checkbox.gravity = Gravity.RIGHT
                            checkbox.layoutDirection = View.LAYOUT_DIRECTION_RTL


                            tableRowAllRegistered.addView(textView)
                            tableRowAllRegistered.addView(checkbox)
                            tableRowAllRegistered.background =
                                resources.getDrawable(R.drawable.border)
                            regTable.addView(tableRowAllRegistered, i)

                        }

                        var checkedRecipient = arrayOf<Boolean>()

                        var rec: HashMap<String, Boolean> = HashMap()


                        var count = regTable.childCount
                        for (i in 0 until count) {
                            checkedRecipient += false
                        }
                        for (i in 0 until count) {
                            val v: View = regTable.getChildAt(i)
                            if (v is TableRow) {
                                var row: TableRow = v as TableRow
                                var rowCount = row.childCount

                                for (j in 0 until rowCount) {
                                    val v2: View = row.getChildAt(j)
                                    if (v2 is CheckBox) {
                                        var potvrdit = v2 as CheckBox
                                        potvrdit.setOnClickListener {
                                            vibrate.vibrate(70)
                                            networkTask = NetworkTask(this)
                                            networkTask.execute()
                                            if (it is CheckBox) {
                                                if (it.isChecked) {
                                                    if (i == 1) {
                                                        for (k in 1 until count) {

                                                            val v3: View = regTable.getChildAt(k)
                                                            var tempRow: TableRow = v3 as TableRow

                                                            if (v3 is TableRow) {
                                                                val v4Check: View =
                                                                    tempRow.getChildAt(1)
                                                                val v4TW: View =
                                                                    tempRow.getChildAt(0)
                                                                (v4Check as CheckBox).isChecked =
                                                                    true
                                                                rec.put(
                                                                    (v4TW as TextView).text.toString(),
                                                                    (v4Check as CheckBox).isChecked
                                                                )
                                                            }
                                                        }
                                                    } else {
                                                        val v3: View = regTable.getChildAt(i)
                                                        var tempRow: TableRow = v3 as TableRow

                                                        if (v3 is TableRow) {
                                                            val v4Check: View =
                                                                tempRow.getChildAt(1)
                                                            val v4TW: View = tempRow.getChildAt(0)
                                                            (v4Check as CheckBox).isChecked = true
                                                            rec.put(
                                                                (v4TW as TextView).text.toString(),
                                                                (v4Check as CheckBox).isChecked
                                                            )
                                                        }
                                                    }
                                                } else if (!it.isChecked) {
                                                    if (i == 1) {
                                                        for (k in 1 until count) {
                                                            val v3: View = regTable.getChildAt(k)
                                                            var tempRow: TableRow = v3 as TableRow
                                                            if (v3 is TableRow) {
                                                                val v4Check: View =
                                                                    tempRow.getChildAt(1)
                                                                val v4TW: View =
                                                                    tempRow.getChildAt(0)
                                                                (v4Check as CheckBox).isChecked =
                                                                    false
                                                                rec.put(
                                                                    (v4TW as TextView).text.toString(),
                                                                    (v4Check as CheckBox).isChecked
                                                                )
                                                            }
                                                        }
                                                    } else {
                                                        val v3: View = regTable.getChildAt(i)
                                                        val v3Vsetci: View = regTable.getChildAt(1)
                                                        val tempRowVsetci: TableRow =
                                                            v3Vsetci as TableRow
                                                        var tempRow: TableRow = v3 as TableRow
                                                        if (v3 is TableRow) {
                                                            val v4Check: View =
                                                                tempRow.getChildAt(1)
                                                            val v4TW: View = tempRow.getChildAt(0)
                                                            (v4Check as CheckBox).isChecked = false
                                                            (tempRowVsetci.getChildAt(1) as CheckBox).isChecked =
                                                                false
                                                            rec.put(
                                                                (v4TW as TextView).text.toString(),
                                                                (v4Check as CheckBox).isChecked
                                                            )
                                                            rec.put("Všetci", false)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        dialogView.buttonPotvrditDialogRec.setOnClickListener {
                            vibrate.vibrate(70)
                            networkTask = NetworkTask(this)
                            networkTask.execute()
                            mAlertDialog.dismiss()
                            var allRecipients: String = String()
                            for ((name, checked) in rec) {
                                if (checked && name != "Všetci")
                                    allRecipients = allRecipients.toString() + "<" + name + ">;"
                            }
                            if (allRecipients[allRecipients.lastIndex] == ';')
                                allRecipients = allRecipients.dropLast(1)

                            recipientsTW.text = allRecipients
                        }
                        dialogView.buttonSpatDialogRec.setOnClickListener {
                            vibrate.vibrate(70)
                            mAlertDialog.dismiss()
                        }

                        recipientsTW.setOnClickListener {
                            vibrate.vibrate(70)
                            networkTask = NetworkTask(this)
                            networkTask.execute()
                            val dialogView =
                                LayoutInflater.from(this)
                                    .inflate(R.layout.dialog_fullrecipients, null)
                            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                            val mAlertDialog = mBuilder.show()

                            dialogView.fullRecDialog.text = recipientsTW.text.toString()

                            mAlertDialog.buttonSpatDialog.setOnClickListener {
                                mAlertDialog.dismiss()
                            }
                        }
                    } else
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                }


                buttonSpat.setOnClickListener {
                    vibrate.vibrate(70)
                    mAlertDialog.dismiss()
                }

                buttonSubmit.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {

                        var text = emailText.text.toString()

                        if (text != null || (text.replace(" ", "")) != "") {
                            if (chkBox.isChecked) {
                                var resultFromMail = dbMethods.sendEmail(
                                    recipientsTW.text.toString(),
                                    spinnerDovod.selectedItem.toString(),
                                    text,
                                    "0"
                                )

                                if (resultFromMail == "0")
                                    Toast.makeText(
                                        this,
                                        "Hups! Nieco je zlé. Máte zapnutý internet?",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else if (resultFromMail == "1") {
                                    Toast.makeText(this, "Email zaslaný!", Toast.LENGTH_SHORT)
                                        .show()
                                    emailText.setText("")
                                    recipientsTW.setText("")
                                    spinnerDovod.setSelection(0)
                                } else {
                                    emailText.setText(resultFromMail)
                                }
                            } else
                                Toast.makeText(
                                    this,
                                    "Nezaškrtli ste, že rozumiete ako táto funkcia funguje",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Nie je možné poslať prázdnu správu",
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
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            networkTask = NetworkTask(this)
            networkTask.execute()
        }


        backupButton.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_backupdb, null)
                val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                val mAlertDialog = mBuilder.show()
                val buttonSpat = dialogView.buttonSpatDialog
                val buttonSubmit = dialogView.buttonPotvrditDialog
                val chkBox = dialogView.chkAck

                buttonSubmit.setOnClickListener {
                    vibrate.vibrate(70)
                    networkTask = NetworkTask(this)
                    networkTask.execute()
                    if (isOnline(this)) {
                        if (chkBox.isChecked) {
                            if (dbMethods.backUpTables() == "1") {
                                Toast.makeText(
                                    this, "Databaza zálohovaná a odoslaná manažérovi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mAlertDialog.dismiss()
                            } else
                                Toast.makeText(
                                    this,
                                    "Hups! Niekde nastala chyba",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } else
                            Toast.makeText(
                                this,
                                "Nezaškrtli ste, že rozumiete ako táto funkcia funguje",
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

                buttonSpat.setOnClickListener {
                    vibrate.vibrate(70)
                    mAlertDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        deleteButton.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_deletedb, null)
                val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                val mAlertDialog = mBuilder.show()
                val buttonSpat = dialogView.buttonSpatDialog
                val buttonSubmit = dialogView.buttonPotvrditDialog
                val chkBox = dialogView.chkAck

                buttonSubmit.setOnClickListener {
                    vibrate.vibrate(70)
                    if (isOnline(this)) {
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        if (chkBox.isChecked) {
                            lateinit var dialog: AlertDialog
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Naozaj?")
                            builder.setMessage("Naozaj chcete vymazať celú databázu?")

                            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        vibrate.vibrate(70)
                                        networkTask = NetworkTask(this)
                                        networkTask.execute()
                                        if (dbMethods.deleteDB() == "1") {
                                            Toast.makeText(
                                                this, "Databaza zmazaná",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            mAlertDialog.dismiss()
                                            networkTask = NetworkTask(this)
                                            networkTask.execute()
                                            val intent = Intent(this@AdminActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                            networkTask = NetworkTask(this)
                                            networkTask.execute()
                                            Toast.makeText(
                                                this, "Pre správne fungovanie aplikácie prosím reštartujte aplikáciu",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else
                                            Toast.makeText(
                                                this,
                                                "Hups! Niekde nastala chyba",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        vibrate.vibrate(70)
                                        mAlertDialog.dismiss()
                                    }
                                    DialogInterface.BUTTON_NEUTRAL -> {
                                        vibrate.vibrate(70)
                                        dialog.dismiss()
                                    }


                                }
                            }
                            builder.setPositiveButton("Áno", dialogClickListener)

                            builder.setNegativeButton("Nie", dialogClickListener)

                            builder.setNeutralButton("Zrušiť", dialogClickListener)

                            dialog = builder.create()


                            dialog.show()
                        } else
                            Toast.makeText(
                                this,
                                "Nezaškrtli ste, že rozumiete ako táto funkcia funguje.",
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

                buttonSpat.setOnClickListener {
                    vibrate.vibrate(70)
                    mAlertDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        issueButton.setOnClickListener {
            vibrate.vibrate(70)
            if (isOnline(this)) {
                networkTask = NetworkTask(this)
                networkTask.execute()
                val dialogViewIssue = LayoutInflater.from(this).inflate(R.layout.dialog_issue, null)
                val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogViewIssue)
                val mAlertDialog = mBuilder.show()
                val buttonSpat = dialogViewIssue.buttonSpatDialogIssue
                val buttonSubmit = dialogViewIssue.buttonPotvrditDialogIssue
                val subject = dialogViewIssue.subjectIssue
                val body = dialogViewIssue.bodyIssue
                val chkBox = dialogViewIssue.chkAckIssue

                buttonSubmit.setOnClickListener {
                    vibrate.vibrate(70)
                    if (isOnline(this)) {
                        networkTask = NetworkTask(this)
                        networkTask.execute()
                        if (chkBox.isChecked) {
                            if (subject.text.toString() != "" && body.text.toString() != "") {


                                var resultFromMail = dbMethods.sendEmail(
                                    "<tkocik@gmail.com>", subject.text.toString(),
                                    body.text.toString(), "1"
                                )

                                if (resultFromMail == "0")
                                    Toast.makeText(
                                        this,
                                        "Hups! Nieco je zlé. Máte zapnutý internet?",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else if (resultFromMail == "1") {
                                    Toast.makeText(this, "Email zaslaný!", Toast.LENGTH_SHORT)
                                        .show()
                                    body.setText("")
                                    subject.setText("")
                                } else {
                                    body.setText(resultFromMail)
                                }
                            } else {
                                Toast.makeText(this, "Nevyplnili ste niečo!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Nezaškrtli ste, že rozumiete ako táto funkcia funguje",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Hups! Nie ste pripojený na internet.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                buttonSpat.setOnClickListener {
                    vibrate.vibrate(70)
                    mAlertDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet.",
                    Toast.LENGTH_SHORT
                ).show()
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
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
