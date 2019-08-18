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
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.nav_header_main.*
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.TimeMethods
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

        /*if(privileges == "1")
        {
            promoteDemote.visibility = View.INVISIBLE
        }
        else if(privileges == "11" || privileges =="111")
        {
            promoteDemote.visibility = View.VISIBLE
        }
        else
        {
            promoteDemote.visibility = View.INVISIBLE
        }*/
        promoteDemote.visibility = View.INVISIBLE

        var namesList: Array<String>
        namesList = arrayOf<String>()

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


        var tableRow: TableRow = TableRow(this)
        var lp : TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT))
        tableRow.layoutParams = lp
        var textView : TextView = TextView(this)
        textView.setTextColor(Color.WHITE)

        /*textView.setText("daco")
        tableRow.addView(textView)
        table.addView(tableRow, 1)*/

    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}