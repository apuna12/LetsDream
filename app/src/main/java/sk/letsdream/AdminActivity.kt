package sk.letsdream

import android.graphics.Color
import android.graphics.Typeface
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
import kotlinx.android.synthetic.main.dialog_fullrecipients.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_fullrecipients.view.*
import kotlinx.android.synthetic.main.dialog_recipients.view.*
import kotlinx.android.synthetic.main.dialog_sendemail.view.*
import kotlinx.android.synthetic.main.dialog_sendemail.view.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_sendemail.view.reasonEmail
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.TimeMethods


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
            val spinnerRecipients = dialogView.recipientSpinner
            val recipientsTW = dialogView.textView17

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

            var list_of_names = arrayOf("Všetci")
            if(namesList.size > 0)
            {
                for(i in 0 until namesList.size-1)
                {
                    list_of_names += namesList[i]
                }
            }



            spinnerRecipients.setOnClickListener{

                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_recipients, null)
                val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                val mAlertDialog = mBuilder.show()

                var regTable = dialogView.recipientsTable
                var childCount = regTable.childCount
                if(childCount>1)
                    regTable.removeViews(1,childCount-1)

                var textView: TextView = TextView(this)
                var textView2: TextView = TextView(this)
                textView.setText("Meno")
                textView.setTextColor(Color.BLACK)
                textView.setPadding(10,5,0,0)
                textView.setTypeface(null, Typeface.BOLD)
                textView.textSize = 20.toFloat()

                textView2.setText("Pridať")
                textView2.setTextColor(Color.BLACK)
                textView2.setPadding(0,5,30,0)
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

                for(i in 1 until list_of_names.size) {
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

                    textView.setText(list_of_names[i-1])
                    textView.setTextColor(Color.BLACK)
                    textView.setPadding(10,5,0,0)
                    checkbox.isChecked = false
                    checkbox.gravity = Gravity.RIGHT
                    checkbox.layoutDirection = View.LAYOUT_DIRECTION_RTL


                    tableRowAllRegistered.addView(textView)
                    tableRowAllRegistered.addView(checkbox)
                    tableRowAllRegistered.background = resources.getDrawable(R.drawable.border)
                    regTable.addView(tableRowAllRegistered, i)

                }

                var checkedRecipient = arrayOf<Boolean>()

                var rec: HashMap<String, Boolean> = HashMap()


                var count = regTable.childCount
                for(i in 0 until count) {
                    checkedRecipient += false
                }
                for(i in 0 until count)
                {
                    val v: View = regTable.getChildAt(i)
                    if(v is TableRow)
                    {
                        var row: TableRow = v as TableRow
                        var rowCount = row.childCount

                        for(j in 0 until rowCount)
                        {
                            val v2: View = row.getChildAt(j)
                            if(v2 is CheckBox)
                            {
                                var potvrdit = v2 as CheckBox
                                potvrdit.setOnClickListener{
                                    if(it is CheckBox)
                                    {
                                        if(it.isChecked)
                                        {
                                            if(i==1)
                                            {
                                                for(k in 1 until count)
                                                {

                                                    val v3: View = regTable.getChildAt(k)
                                                    var tempRow: TableRow = v3 as TableRow

                                                    if(v3 is TableRow) {
                                                        val v4Check: View = tempRow.getChildAt(1)
                                                        val v4TW: View = tempRow.getChildAt(0)
                                                        (v4Check as CheckBox).isChecked = true
                                                        rec.put((v4TW as TextView).text.toString(), (v4Check as CheckBox).isChecked )
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                val v3: View = regTable.getChildAt(i)
                                                var tempRow: TableRow = v3 as TableRow

                                                if(v3 is TableRow) {
                                                    val v4Check: View = tempRow.getChildAt(1)
                                                    val v4TW: View = tempRow.getChildAt(0)
                                                    (v4Check as CheckBox).isChecked = true
                                                    rec.put((v4TW as TextView).text.toString(), (v4Check as CheckBox).isChecked)
                                                }
                                            }
                                        }
                                        else if(!it.isChecked)
                                        {
                                            if(i==1)
                                            {
                                                for(k in 1 until count)
                                                {
                                                    val v3: View = regTable.getChildAt(k)
                                                    var tempRow: TableRow = v3 as TableRow
                                                    if(v3 is TableRow) {
                                                        val v4Check: View = tempRow.getChildAt(1)
                                                        val v4TW: View = tempRow.getChildAt(0)
                                                        (v4Check as CheckBox).isChecked = false
                                                        rec.put((v4TW as TextView).text.toString(),(v4Check as CheckBox).isChecked)
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                val v3: View = regTable.getChildAt(i)
                                                val v3Vsetci: View = regTable.getChildAt(1)
                                                val tempRowVsetci: TableRow = v3Vsetci as TableRow
                                                var tempRow: TableRow = v3 as TableRow
                                                if(v3 is TableRow) {
                                                    val v4Check: View = tempRow.getChildAt(1)
                                                    val v4TW: View = tempRow.getChildAt(0)
                                                    (v4Check as CheckBox).isChecked = false
                                                    (tempRowVsetci.getChildAt(1) as CheckBox).isChecked = false
                                                    rec.put((v4TW as TextView).text.toString(),(v4Check as CheckBox).isChecked)
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
                    mAlertDialog.dismiss()
                    var allRecipients: String = String()
                    for((name, checked) in rec)
                    {
                        if(checked && name != "Všetci")
                            allRecipients = allRecipients.toString() + "<" + name + ">;"
                    }
                    if(allRecipients[allRecipients.lastIndex] == ';')
                        allRecipients = allRecipients.dropLast(1)

                    recipientsTW.text = allRecipients
                }
                dialogView.buttonSpatDialogRec.setOnClickListener {
                    mAlertDialog.dismiss()
                }

                recipientsTW.setOnClickListener{
                    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_fullrecipients, null)
                    val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
                    val mAlertDialog = mBuilder.show()

                    dialogView.fullRecDialog.text = recipientsTW.text.toString()

                    mAlertDialog.buttonSpatDialog.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
            }


            buttonSpat.setOnClickListener{
                mAlertDialog.dismiss()
            }

            buttonSubmit.setOnClickListener {
                var text = emailText.text.toString()

                if(text != null || (text.replace(" ", "")) != "")
                {
                    if(chkBox.isChecked) {
                        var resultFromMail = dbMethods.sendEmail(recipientsTW.text.toString(), spinnerDovod.selectedItem.toString(), text, "0")

                        if(resultFromMail == "0")
                            Toast.makeText(this, "Hups! Nieco je zlé. Máte zapnutý internet?", Toast.LENGTH_SHORT).show()
                        else if(resultFromMail == "1")
                        {
                            Toast.makeText(this, "Email zaslaný!", Toast.LENGTH_SHORT).show()
                            emailText.setText("")
                            recipientsTW.setText("")
                            spinnerDovod.setSelection(0)
                        }
                        else
                        {
                            emailText.setText(resultFromMail)
                        }
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


        backupButton.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_backupdb, null)
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()
            val buttonSpat = dialogView.buttonSpatDialog
            val buttonSubmit = dialogView.buttonPotvrditDialog
            val chkBox = dialogView.chkAck

            buttonSubmit.setOnClickListener{
                if(chkBox.isChecked) {
                    if (dbMethods.backUpLoginTable() == "1") {
                        Toast.makeText(
                            this, "Databaza zálohovaná a odoslaná manažérovi",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else
                        Toast.makeText(this, "Niekde nastala chyba", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Nezaškrtli ste, že rozumiete ako táto funkcia funguje", Toast.LENGTH_SHORT).show()
            }

            buttonSpat.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        deleteButton.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_deletedb, null)
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()
            val buttonSpat = dialogView.buttonSpatDialog
            val buttonSubmit = dialogView.buttonPotvrditDialog

            buttonSubmit.setOnClickListener{



            }

            buttonSpat.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        issueButton.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_issue, null)
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()
            val buttonSpat = dialogView.buttonSpatDialog
            val buttonSubmit = dialogView.buttonPotvrditDialog

            buttonSubmit.setOnClickListener{

            }

            buttonSpat.setOnClickListener{
                mAlertDialog.dismiss()
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
