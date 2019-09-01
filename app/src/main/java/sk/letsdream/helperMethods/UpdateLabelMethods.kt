package sk.letsdream.helperMethods

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Vibrator
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.dialog_newregistrations.view.*
import kotlinx.android.synthetic.main.dialog_poznamka.view.*
import org.w3c.dom.Text
import sk.letsdream.MainActivity
import sk.letsdream.R
import sk.letsdream.dbMethods.DBConnection
import java.sql.Time
import java.util.*




class UpdateLabelMethods {

    fun updateTimeLabel(
        context: Context,
        casOd: TextView,
        casDo: TextView,
        nazovAkcieVedlaSpinnera: TextView
    ) {
        val dbMethods: DBConnection = DBConnection()
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(
            context,
            R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                if (m < 10)
                    casOd.setText("" + h + ":0" + m)
                else
                    casOd.setText("" + h + ":" + m)

            }),
            hour,
            minute,
            true
        )
        tpd.setTitle("Čas od: ")


        val d = Calendar.getInstance()
        val hour2 = d.get(Calendar.HOUR)
        val minute2 = d.get(Calendar.MINUTE)

        val tpd2 = TimePickerDialog(
            context,
            R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                if (m < 10)
                    casDo.setText("" + h + ":0" + m)
                else
                    casDo.setText("" + h + ":" + m)

            }),
            hour2,
            minute2,
            true
        )
        tpd2.setTitle("Čas do: ")

        tpd.show()
        tpd.setOnDismissListener {
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            tpd2.show()
            tpd2.setOnDismissListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                if (dbMethods.setLabelStatistics(
                        nazovAkcieVedlaSpinnera.text.toString().replace(
                            " ",
                            "_"
                        ), "Cas_Od", casOd.text.toString()
                    ) == "1"
                ) {
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG)
                        .show()
                }

                if (dbMethods.setLabelStatistics(
                        nazovAkcieVedlaSpinnera.text.toString().replace(
                            " ",
                            "_"
                        ), "Cas_Do", casDo.text.toString()
                    ) == "1"
                ) {
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun updateNumberLabels(
        context: Context,
        upravit: TextView,
        textView: TextView,
        action: TextView,
        setting: String
    ) {
        val dbMethods: DBConnection = DBConnection()


        var popUpMenu: PopupMenu = PopupMenu(context, upravit)
        for (i in 0 until 500) {
            popUpMenu.menu.add(i.toString())
        }
        popUpMenu.show()

        popUpMenu.setOnMenuItemClickListener {
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            textView.setText(it.title.toString())
            if (setting == "pocDobr") {
                if (dbMethods.setLabelStatistics(
                        action.text.toString().replace(" ", "_"),
                        "Pocet_Dobrovolnikov",
                        textView.text.toString()
                    ) == "1"
                )
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(
                        context,
                        "Záznam sa nepodarilo upraviť",
                        Toast.LENGTH_LONG
                    ).show()
            } else if (setting == "pocNavs") {
                if (dbMethods.setLabelStatistics(
                        action.text.toString().replace(" ", "_"),
                        "Pocet_Navstevnikov",
                        textView.text.toString()
                    ) == "1"
                )
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(
                        context,
                        "Záznam sa nepodarilo upraviť",
                        Toast.LENGTH_LONG
                    ).show()
            }
            true

        }
    }

    fun updateDateLabel(context: Context, upravit: TextView, textView: TextView, action: TextView) {
        val dbMethods: DBConnection = DBConnection()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            context, R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                // Display Selected date in textbox
                textView.setText("" + dayOfMonth + "." + monthOfYear + "." + year)
            }, year, month, day
        )
        dpd.show()
        dpd.setOnDismissListener {
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            if (dbMethods.setLabelStatistics(
                    action.text.toString().replace(" ", "_"),
                    "Datum",
                    textView.text.toString()
                ) == "1"
            )
                Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
        }


    }

    fun updatePoznLabels(context: Context, textView: TextView, action: TextView) {
        val dbMethods: DBConnection = DBConnection()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_poznamka, null)
        val mBuilder = AlertDialog.Builder(context).setView(dialogView)
        dialogView.poznDialogET.filters =
            arrayOf(*dialogView.poznDialogET.filters, InputFilter.LengthFilter(100))
        if (dialogView.parent != null)
            (dialogView.parent as ViewGroup).removeView(dialogView)
        else {
            val mAlertDialog = mBuilder.show()
            dialogView.poznDialogBUTTON.setOnClickListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                mAlertDialog.dismiss()
                textView.setText(dialogView.poznDialogET.text)
                if (dbMethods.setLabelStatistics(
                        action.text.toString().replace(" ", "_"),
                        "Poznamka",
                        textView.text.toString()
                    ) == "1"
                )
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(
                        context,
                        "Záznam sa nepodarilo upraviť",
                        Toast.LENGTH_LONG
                    ).show()
            }
            dialogView.backPoznDialogBUTTON.setOnClickListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                mAlertDialog.cancel()
            }
        }

        true


    }

    fun allActions(context: Context, table: TableLayout) {

        var dbMethods: DBConnection = DBConnection()
        var actionTable = table
        var childCount = actionTable.childCount
        if (childCount > 1)
            actionTable.removeViews(1, childCount - 1)
        val dbValue = dbMethods.getAllActions()
        if (dbValue != "0") {
            actionTable.visibility = View.VISIBLE
            var splittedDbValue = dbValue.split("?").toTypedArray()

            var allActions = arrayOf<Array<String>>()
            for (i in 0 until splittedDbValue.size) {
                allActions += splittedDbValue[i].split("-").toTypedArray()
            }
            allActions = allActions.dropLast(1).toTypedArray()

            for (i in 0 until allActions.size) {
                var tableRowAllActions: TableRow = TableRow(context)
                var lp: TableRow.LayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
                    )
                )
                tableRowAllActions.layoutParams = lp
                var textViewNazov: TextView = TextView(context)
                var textViewDatum: TextView = TextView(context)
                var textViewCasOd: TextView = TextView(context)
                var textViewCasDo: TextView = TextView(context)
                var textViewUprav: TextView = TextView(context)

                textViewNazov.setText(allActions[i][0])
                textViewNazov.setTextColor(Color.WHITE)
                textViewNazov.height = 70
                textViewDatum.setText(allActions[i][1])
                textViewDatum.setTextColor(Color.WHITE)
                textViewDatum.height = 70
                textViewCasOd.setText(allActions[i][2])
                textViewCasOd.setTextColor(Color.WHITE)
                textViewCasOd.height = 70
                textViewCasDo.setText(allActions[i][3])
                textViewCasDo.setTextColor(Color.WHITE)
                textViewCasDo.height = 70
                textViewUprav.setText("Upraviť")
                textViewUprav.setTextColor(Color.YELLOW)
                textViewUprav.height = 70


                tableRowAllActions.addView(textViewNazov)
                tableRowAllActions.addView(textViewDatum)
                tableRowAllActions.addView(textViewCasOd)
                tableRowAllActions.addView(textViewCasDo)
                tableRowAllActions.addView(textViewUprav)
                actionTable.addView(tableRowAllActions, i + 1)


            }

            var count = actionTable.childCount
            for (i in 0 until count) {
                val v: View = actionTable.getChildAt(i)
                if (v is TableRow) {
                    var row: TableRow = v as TableRow
                    var rowCount = row.childCount
                    for (j in 0 until rowCount) {
                        val v2: View = row.getChildAt(j)
                        if (v2 is TextView) {
                            var potvrdit = v2 as TextView
                            potvrdit.setOnClickListener {
                                if (it is TextView) {
                                    if (it.text == "Upraviť") {
                                        //zobrazit dialog kde budem môcť jednotlive veci meniť
                                        //nastaviť aby sa upraviť nezobrazoval ak som obycajny pouzivatel
                                    }
                                    else
                                    {
                                        //dorobit zobrazenie celeho riadku po klinuti na hocico okrem "Upraviť"
                                    }
                                }
                            }

                        }
                    }

                }
            }
        } else {
            actionTable.visibility = View.INVISIBLE
        }
    }
}