package sk.letsdream

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputFilter
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.dialog_fullpoznamka.view.*
import kotlinx.android.synthetic.main.dialog_fullpoznamka.view.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_newregistrations.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.w3c.dom.Text
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.TimeMethods
import sk.letsdream.helperMethods.UpdateLabelMethods
import java.lang.Exception

class VyberMenaActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    var privileges: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vybermena)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val timeMethod: TimeMethods = TimeMethods()
        val buttonEffects: ButtonEffects = ButtonEffects()

        timeMethod.UpdateActualTime(date,time)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)





        var dbMethods:DBConnection = DBConnection()
        var table: TableLayout = findViewById(R.id.table)

        val spinnerMeno: ImageButton = findViewById(R.id.vybermenaSPINNER_VM)
        val meno: TextView = findViewById(R.id.nameFromSpinner_VM)
        val promoteDemote: TextView = findViewById(R.id.textView20)
        val leftBracket : TextView = findViewById(R.id.leftBracket)
        val rightBracket : TextView = findViewById(R.id.rightBracket)
        val newRegistrations : TextView = findViewById(R.id.newRegistrations)
        val spravovatRegistracie: TextView = findViewById(R.id.textView16)
        val pocetHodin: TextView = findViewById(R.id.pocetHodin)
        val changePrivileges: ImageView = findViewById(R.id.changePriv_VM)
        val userTW: TextView = findViewById(R.id.adminUserTW_VM)
        val showDochadzka: TextView = findViewById(R.id.showDochadzka)

        if(privileges == "1")
        {
            leftBracket.visibility = View.INVISIBLE
            rightBracket.visibility = View.INVISIBLE
            newRegistrations.visibility = View.INVISIBLE
            spravovatRegistracie.visibility = View.INVISIBLE
            userTW.visibility = View.INVISIBLE
            changePrivileges.visibility = View.INVISIBLE
        }
        else if(privileges == "11" || privileges =="111")
        {
            leftBracket.visibility = View.VISIBLE
            rightBracket.visibility = View.VISIBLE
            newRegistrations.visibility = View.VISIBLE
            spravovatRegistracie.visibility = View.VISIBLE
            userTW.visibility = View.VISIBLE
            changePrivileges.visibility = View.VISIBLE
            var newRegs: String = dbMethods.getNewRegistrations()
            if(newRegs != "0")
                newRegistrations.text = "Nové: " + newRegs
            else
            {
                leftBracket.visibility = View.INVISIBLE
                rightBracket.visibility = View.INVISIBLE
                newRegistrations.visibility = View.INVISIBLE
                spravovatRegistracie.visibility = View.INVISIBLE
            }
        }
        else
        {
            leftBracket.visibility = View.INVISIBLE
            rightBracket.visibility = View.INVISIBLE
            newRegistrations.visibility = View.INVISIBLE
            spravovatRegistracie.visibility = View.INVISIBLE
        }
        promoteDemote.visibility = View.INVISIBLE

        var namesList: Array<String>

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
        else
            Toast.makeText(this,"Hups! Neexistujú žiadne mená v databáze", Toast.LENGTH_LONG).show()

        spinnerMeno.setOnClickListener {
            popUpMenu.setOnMenuItemClickListener {
                if (popUpMenu.menu.size() == 0) {
                    Toast.makeText(this, "Hups! V Databáze sa nenachádza žiadne meno!", Toast.LENGTH_LONG).show()
                } else {
                    meno.text = it.title.toString()
                    if(dbMethods.getPrivileges(meno.text.toString()).toLowerCase() == "admin" && privileges.toLowerCase() == "11")
                    {
                        promoteDemote.visibility = View.INVISIBLE
                    }
                    else if(dbMethods.getPrivileges(meno.text.toString()).toLowerCase() == "admin" && privileges.toLowerCase() == "111")
                    {
                        promoteDemote.visibility = View.VISIBLE
                        promoteDemote.text = "Degradovať na užívateľa"
                    }
                    else if(dbMethods.getPrivileges(meno.text.toString()).toLowerCase() == "user" &&
                        (privileges.toLowerCase() == "111" || privileges.toLowerCase() == "11"))
                    {
                        promoteDemote.visibility = View.VISIBLE
                        promoteDemote.text = "Povýšiť na administrátora"
                    }
                    else if(privileges.toLowerCase() == "1")
                    {
                        promoteDemote.visibility = View.INVISIBLE
                    }
                    else{
                        promoteDemote.visibility = View.INVISIBLE
                    }

                    var pocetHod = dbMethods.getAllHoursForUser(meno.text.toString())

                    pocetHodin.text = pocetHod + " h"
                }
                true
            }
            popUpMenu.show()
        }

        promoteDemote.setOnClickListener{
            if(privileges.toLowerCase() == "111" || privileges.toLowerCase() == "11")
            {
                if(meno.text != "Vyberte meno") {
                    if(promoteDemote.text == "Povýšiť na administrátora")
                    {
                        val alertDialog = android.app.AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Povýšenie")
                            .setMessage("Naozaj chcete povýšiť užívateľa '" + meno.text.toString() + "' na administrátora?")
                            .setPositiveButton("Áno", DialogInterface.OnClickListener{ dialog, i ->
                                dbMethods.promoteToAdmin(meno.text.toString())
                                Toast.makeText(this, "Používateľ povýšený", Toast.LENGTH_LONG).show()
                                if(privileges == "111")
                                    promoteDemote.text = "Degradovať na užívateľa"
                                else
                                    promoteDemote.visibility = View.INVISIBLE
                            })
                            .setNegativeButton("Nie", DialogInterface.OnClickListener{ dialog, i ->
                                dialog.cancel()
                            })
                            .show()
                    }
                    else if(promoteDemote.text == "Degradovať na užívateľa")
                    {
                        val alertDialog = android.app.AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Degradovanie")
                            .setMessage("Naozaj chcete degradovať administrátora '" + meno.text.toString() + "' na bežného užívateľa?")
                            .setPositiveButton("Áno", DialogInterface.OnClickListener{ dialog, i ->
                                dbMethods.demoteToUser(meno.text.toString())
                                Toast.makeText(this, "Používateľ degradovaný", Toast.LENGTH_LONG).show()
                                promoteDemote.text = "Povýšiť na administrátora"
                            })
                            .setNegativeButton("Nie", DialogInterface.OnClickListener{ dialog, i ->
                                dialog.cancel()
                            })
                            .show()

                    }
                }
                else
                    Toast.makeText(this, "Hups! Nevybrali ste žiadne meno", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this, "Hups! Niečo je zlé. Skúste neskôr", Toast.LENGTH_LONG).show()
            }
        }

        newRegistrations.setOnClickListener{

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_newregistrations, null)
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()

            var regTable = dialogView.newRegistrationsTable
            var childCount = regTable.childCount
            if(childCount>1)
                regTable.removeViews(1,childCount-1)
            val dbValue = dbMethods.getNewRegistrationsTable()
            var splittedDbValue = dbValue.split("?").toTypedArray()

            var allNewRegistrations = arrayOf<Array<String>>()
            for (i in 0 until splittedDbValue.size)
            {
                allNewRegistrations += splittedDbValue[i].split("-").toTypedArray()
            }
            allNewRegistrations = allNewRegistrations.dropLast(1).toTypedArray()

            for(i in 0 until allNewRegistrations.size)
            {
                var tableRowAllRegistered: TableRow = TableRow(this)
                var lp : TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT))
                tableRowAllRegistered.layoutParams = lp
                var textViewMeno : TextView = TextView(this)
                var textViewLogin : TextView = TextView(this)
                var textViewPotvrd : TextView = TextView(this)
                var textViewOdstran : TextView = TextView(this)

                textViewMeno.setText(allNewRegistrations[i][0])
                textViewMeno.setTextColor(Color.BLACK)
                textViewLogin.setText(allNewRegistrations[i][1])
                textViewLogin.setTextColor(Color.BLACK)
                textViewPotvrd.setText("Potvrdiť")
                textViewPotvrd.setTextColor(Color.GREEN)
                textViewOdstran.setText("Odstrániť")
                textViewOdstran.setTextColor(Color.RED)


                tableRowAllRegistered.addView(textViewMeno)
                tableRowAllRegistered.addView(textViewLogin)
                tableRowAllRegistered.addView(textViewPotvrd)
                tableRowAllRegistered.addView(textViewOdstran)
                regTable.addView(tableRowAllRegistered, i+1)


            }

            var count = regTable.childCount
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
                        if(v2 is TextView)
                        {
                            var potvrdit = v2 as TextView
                            potvrdit.setOnClickListener{
                                if(it is TextView)
                                {
                                    if(it.text == "Potvrdiť")
                                    {
                                        val login = row.getChildAt(j-1) as TextView
                                        val name = row.getChildAt(j-2) as TextView

                                        val alertDialog = android.app.AlertDialog.Builder(this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Potvrdiť?")
                                            .setMessage("Naozaj si prajete potvrdiť používateľa '" + name.text + "' ?")
                                            .setPositiveButton("Áno", DialogInterface.OnClickListener{ dialog, i ->
                                                if(dbMethods.acknowledgeUser(login.text.toString()) == "1") {
                                                    Toast.makeText(this, "Používateľ potvrdený", Toast.LENGTH_LONG)
                                                        .show()
                                                    finish()
                                                    startActivity(intent)
                                                }
                                                else
                                                    Toast.makeText(this, "Hups! Niečo je zlé. Skúste neskôr", Toast.LENGTH_LONG).show()

                                            })
                                            .setNegativeButton("Nie", DialogInterface.OnClickListener{ dialog, i ->
                                                dialog.cancel()
                                            }).show()

                                    }
                                    else if(it.text == "Odstrániť")
                                    {
                                        val login = row.getChildAt(j-2) as TextView
                                        val name = row.getChildAt(j-3) as TextView

                                        val alertDialog = android.app.AlertDialog.Builder(this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Odstrániť?")
                                            .setMessage("Naozaj si prajete odstrániť používateľa '" + name.text + "' ?")
                                            .setPositiveButton("Áno", DialogInterface.OnClickListener{ dialog, i ->
                                                if(dbMethods.deleteUser(login.text.toString()) == "1") {
                                                    Toast.makeText(this, "Používateľ odstránený", Toast.LENGTH_LONG)
                                                        .show()
                                                    finish()
                                                    startActivity(intent)
                                                }
                                                else
                                                    Toast.makeText(this, "Hups! Niečo je zlé. Skúste neskôr", Toast.LENGTH_LONG).show()
                                            })
                                            .setNegativeButton("Nie", DialogInterface.OnClickListener{ dialog, i ->
                                                dialog.cancel()
                                            }).show()
                                    }
                                }
                            }

                        }
                    }
                }
            }

            dialogView.buttonSpatDialog.setOnClickListener {
                mAlertDialog.dismiss()
            }


            // dorobit tabulku, kde budu stlpce: Meno, login, Potvrdenie, Odstranenie
            // kliknutim na potvrdenie sa vymaze riadok a zmeni New_User z 1 na 0
            // vratenim sa mimo dialogu sa refresne cely intent
        }

        var updateLabelMethods: UpdateLabelMethods = UpdateLabelMethods()

        updateLabelMethods.allActions(this, table, intent, "1")

        changePrivileges.setOnClickListener{
            if(privileges.toLowerCase()=="1" && userTW.text == "Používateľ")
            {
                Toast.makeText(this,"K tejto funkcii ma prístup iba administrátor!", Toast.LENGTH_SHORT).show()
                updateLabelMethods.allActions(this, table, intent, "1")
            }
            else if((privileges.toLowerCase() == "11" || privileges.toLowerCase()=="111") && userTW.text == "Používateľ")
            {
                Toast.makeText(this,"Zapnutý admin mód!", Toast.LENGTH_SHORT).show()
                userTW.text = "Admin"
                changePrivileges.visibility = View.VISIBLE
                updateLabelMethods.allActions(this, table, intent, "11")
            }
            else if((privileges.toLowerCase() == "11" || privileges.toLowerCase()=="111") && userTW.text == "Admin")
            {
                Toast.makeText(this,"Zapnutý používateľský mód!", Toast.LENGTH_SHORT).show()
                userTW.text = "Používateľ"
                updateLabelMethods.allActions(this, table, intent, "1")
            }
            else
            {
                Toast.makeText(this,"Nastala chyba! Nerozpoznaná rola používateľa", Toast.LENGTH_SHORT).show()
                userTW.visibility = View.INVISIBLE
                changePrivileges.visibility = View.INVISIBLE
                updateLabelMethods.allActions(this, table, intent, "1")
            }
        }


        showDochadzka.setOnClickListener{
            if(privileges == "11" || privileges == "111")
            {
                if(dbMethods.getDochadzka(privileges) != "0")
                {

                }
                else
                    Toast.makeText(this, "Hups! Niečo je zlé. Skúste neskôr", Toast.LENGTH_LONG).show()
            }
            else
            {
                if(dbMethods.getDochadzka(privileges, meno.text.toString()) != "0")
                {

                }
                else
                    Toast.makeText(this, "Hups! Niečo je zlé. Skúste neskôr", Toast.LENGTH_LONG).show()
            }
        }

    }



    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}