package sk.letsdream.helperMethods

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Vibrator
import android.support.v4.content.ContextCompat.startActivity
import android.text.InputFilter
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.dialog_fullaction.view.casDoAkcieDialog
import kotlinx.android.synthetic.main.dialog_fullaction.view.casOdAkcieDialog
import kotlinx.android.synthetic.main.dialog_fullaction.view.datumAkcieDialog
import kotlinx.android.synthetic.main.dialog_fullaction.view.nazovAkcieDialog
import kotlinx.android.synthetic.main.dialog_newregistrations.view.buttonSpatDialog
import kotlinx.android.synthetic.main.dialog_poznamka.view.*
import sk.letsdream.R
import sk.letsdream.VyberMenaActivity
import sk.letsdream.dbMethods.DBConnection
import java.util.*
import kotlinx.android.synthetic.main.dialog_changeaction.view.buttonPotvrditDialog
import kotlinx.android.synthetic.main.dialog_changedochadzka.view.*
import kotlinx.android.synthetic.main.dialog_changedochadzka.view.menoDialog
import kotlinx.android.synthetic.main.dialog_changedochadzka.view.poznamkaDialog
import kotlinx.android.synthetic.main.dialog_fulldochadzka.view.*
import kotlinx.android.synthetic.main.dialog_getdochadzka.view.*
import java.text.SimpleDateFormat


class UpdateLabelMethods {



    fun updateTimeLabel(
        context: Activity,
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
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                if (m < 10) {
                    if (h > 9)
                        casOd.setText("" + h + ":0" + m)
                    else
                        casOd.setText("0" + h + ":0" + m)
                }
                else {
                    if(h>10)
                        casOd.setText("" + h + ":" + m)
                    else
                        casOd.setText("0" + h + ":" + m)
                }
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
                if (m < 10) {
                    if (h > 9)
                        casOd.setText("" + h + ":0" + m)
                    else
                        casOd.setText("0" + h + ":0" + m)
                }
                else {
                    if(h>10)
                        casOd.setText("" + h + ":" + m)
                    else
                        casOd.setText("0" + h + ":" + m)
                }

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
                var networkTask = NetworkTask(context)
                networkTask.execute()
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
        context: Activity,
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
            var networkTask = NetworkTask(context)
            networkTask.execute()
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

    fun updateDateLabel(context: Activity, upravit: TextView, textView: TextView, action: TextView) {
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
                textView.setText("" + dayOfMonth + "." + monthOfYear + "." + year)
            }, year, month, day
        )
        dpd.show()
        dpd.setOnDismissListener {
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            var networkTask = NetworkTask(context)
            networkTask.execute()
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

    fun updatePoznLabels(context: Activity, textView: TextView, action: TextView) {
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
                var networkTask = NetworkTask(context)
                networkTask.execute()
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

    fun allActions(context: Activity, table: TableLayout, intent: Intent, privileges: String) {

        if(isOnline(context)) {
            var dbMethods: DBConnection = DBConnection()
            var actionTable = table
            var childCount = actionTable.childCount
            if (childCount > 1)
                actionTable.removeViews(1, childCount - 1)
            var networkTask = NetworkTask(context)
            networkTask.execute()
            val dbValue = dbMethods.getAllActions()
            if (dbValue != "0") {
                actionTable.visibility = View.VISIBLE
                var splittedDbValue = dbValue.split("?").toTypedArray()

                var allActions = arrayOf<Array<String>>()
                for (i in 0 until splittedDbValue.size) {
                    allActions += splittedDbValue[i].split("#").toTypedArray()
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



                    textViewNazov.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(15)))
                    textViewNazov.setText(allActions[i][0])
                    textViewNazov.setTextColor(Color.WHITE)
                    textViewNazov.height = 70
                    textViewNazov.gravity = Gravity.LEFT
                    textViewDatum.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(15)))
                    textViewDatum.setText(allActions[i][1])
                    textViewDatum.setTextColor(Color.WHITE)
                    textViewDatum.height = 70
                    textViewDatum.gravity = Gravity.LEFT
                    textViewCasOd.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(15)))
                    textViewCasOd.setText(allActions[i][2])
                    textViewCasOd.setTextColor(Color.WHITE)
                    textViewCasOd.height = 70
                    textViewCasOd.gravity = Gravity.LEFT
                    textViewCasDo.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(15)))
                    textViewCasDo.setText(allActions[i][3])
                    textViewCasDo.setTextColor(Color.WHITE)
                    textViewCasDo.height = 70
                    textViewCasDo.gravity = Gravity.LEFT
                    textViewCasDo.ellipsize = TextUtils.TruncateAt.END


                    tableRowAllActions.addView(textViewNazov)
                    tableRowAllActions.addView(textViewDatum)
                    tableRowAllActions.addView(textViewCasOd)
                    tableRowAllActions.addView(textViewCasDo)
                    if (privileges == "11" || privileges == "111") {
                        textViewUprav.setText("Upraviť")
                        textViewUprav.setTextColor(Color.YELLOW)
                        textViewUprav.height = 70
                        textViewUprav.gravity = Gravity.CENTER
                        tableRowAllActions.addView(textViewUprav)
                    } else {
                        textViewUprav.setText("")
                        textViewUprav.height = 70
                        tableRowAllActions.addView(textViewUprav)
                    }

                    actionTable.addView(tableRowAllActions, i + 1)


                }

                var count = actionTable.childCount
                for (i in 1 until count) {
                    val v: View = actionTable.getChildAt(i)
                    if (v is TableRow) {
                        var row: TableRow = v as TableRow
                        var rowCount = row.childCount
                        for (j in 0 until rowCount) {
                            val v2: View = row.getChildAt(j)
                            if (v2 is TextView) {
                                var cell = v2 as TextView
                                cell.setOnClickListener {
                                    val vibrate =
                                        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                    vibrate.vibrate(70)
                                    var networkTask = NetworkTask(context)
                                    networkTask.execute()
                                    if (it is TextView) {
                                        if (it.text == "Upraviť") {
                                            val dialogView = LayoutInflater.from(context)
                                                .inflate(R.layout.dialog_changeaction, null)
                                            val mBuilder =
                                                AlertDialog.Builder(context).setView(dialogView)
                                            val nazov = allActions[i - 1][0]
                                            val datum = row.getChildAt(1) as TextView
                                            val casOd = row.getChildAt(2) as TextView
                                            val casDo = row.getChildAt(3) as TextView
                                            dialogView.nazovAkcieDialog.text = nazov
                                            dialogView.datumAkcieDialog.text = datum.text
                                            dialogView.casOdAkcieDialog.text = casOd.text
                                            dialogView.casDoAkcieDialog.text = casDo.text
                                            val mAlertDialog = mBuilder.show()
                                            dialogView.buttonSpatDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                mAlertDialog.dismiss()
                                            }
                                            dialogView.buttonPotvrditDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                var networkTask = NetworkTask(context)
                                                networkTask.execute()
                                                if (dbMethods.editAction(
                                                        context,
                                                        dialogView.nazovAkcieDialog.text.toString(),
                                                        dialogView.datumAkcieDialog.text.toString(),
                                                        dialogView.casOdAkcieDialog.text.toString(),
                                                        dialogView.casDoAkcieDialog.text.toString(),
                                                        nazov.toString()
                                                    ) == "1"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Akcia zmenená",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    val activity: VyberMenaActivity =
                                                        context as VyberMenaActivity
                                                    startActivity(context, intent, null)
                                                    activity.finish()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Hups! Niečo je zlé. Skúste neskôr",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }

                                        } else {
                                            val dialogView = LayoutInflater.from(context)
                                                .inflate(R.layout.dialog_fullaction, null)
                                            val mBuilder =
                                                AlertDialog.Builder(context).setView(dialogView)
                                            dialogView.nazovAkcieDialog.text = allActions[i - 1][0]
                                            dialogView.datumAkcieDialog.text = allActions[i - 1][1]
                                            dialogView.casOdAkcieDialog.text = allActions[i - 1][2]
                                            dialogView.casDoAkcieDialog.text = allActions[i - 1][3]
                                            val mAlertDialog = mBuilder.show()
                                            dialogView.buttonSpatDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                mAlertDialog.dismiss()
                                            }
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
        else
        {
            Toast.makeText(
                context,
                "Hups! Nie ste pripojený na internet.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun processDochadzka(
        context: Context,
        intent: Intent,
        privileges: String,
        name: String? = null
    ) {

        if(isOnline(context)) {
            var dbMethods: DBConnection = DBConnection()

            val dbValue = dbMethods.getDochadzka(privileges, name)
            if (dbValue != "0") {
                val dialogView =
                    LayoutInflater.from(context).inflate(R.layout.dialog_getdochadzka, null)
                val mBuilder = AlertDialog.Builder(context).setView(dialogView)
                val mAlertDialog = mBuilder.show()
                var dochadzkaTable = dialogView.getDochadzkaTable
                var childCount = dochadzkaTable.childCount
                if (childCount > 1)
                    dochadzkaTable.removeViews(1, childCount - 1)
                var splittedDbValue = dbValue.split("?").toTypedArray()

                var allDochadzka = arrayOf<Array<String>>()
                for (i in 0 until splittedDbValue.size) {
                    allDochadzka += splittedDbValue[i].split("-").toTypedArray()
                }
                allDochadzka = allDochadzka.dropLast(1).toTypedArray()

                for (i in 0 until allDochadzka.size) {
                    var tableRowAllActions: TableRow = TableRow(context)
                    var lp: TableRow.LayoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
                        )
                    )
                    tableRowAllActions.layoutParams = lp
                    var textViewMeno: TextView = TextView(context)
                    var textViewPrichod: TextView = TextView(context)
                    var textViewOdchod: TextView = TextView(context)
                    var textViewHodiny: TextView = TextView(context)
                    var textViewPoznamka: TextView = TextView(context)
                    var textViewUprav: TextView = TextView(context)



                    textViewMeno.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(20)))
                    textViewMeno.setText(allDochadzka[i][0])
                    textViewMeno.setTextColor(Color.BLACK)
                    textViewMeno.height = 70
                    textViewMeno.gravity = Gravity.LEFT
                    textViewMeno.setPadding(30, 0, 20, 0)
                    textViewPrichod.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(20)))
                    textViewPrichod.setText(allDochadzka[i][1])
                    textViewPrichod.setTextColor(Color.BLACK)
                    textViewPrichod.height = 70
                    textViewPrichod.gravity = Gravity.LEFT
                    textViewPrichod.setPadding(30, 0, 20, 0)
                    textViewOdchod.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(20)))
                    textViewOdchod.setText(allDochadzka[i][2])
                    textViewOdchod.setTextColor(Color.BLACK)
                    textViewOdchod.height = 70
                    textViewOdchod.gravity = Gravity.LEFT
                    textViewOdchod.setPadding(25, 0, 20, 0)
                    textViewHodiny.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(20)))
                    textViewHodiny.setText(allDochadzka[i][3])
                    textViewHodiny.setTextColor(Color.BLACK)
                    textViewHodiny.height = 70
                    textViewHodiny.gravity = Gravity.LEFT
                    textViewHodiny.setPadding(30, 0, 20, 0)
                    textViewPoznamka.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(20)))
                    textViewPoznamka.setText(allDochadzka[i][4])
                    textViewPoznamka.setTextColor(Color.BLACK)
                    textViewPoznamka.height = 70
                    textViewPoznamka.gravity = Gravity.LEFT
                    textViewPoznamka.setPadding(30, 0, 20, 0)

                    tableRowAllActions.addView(textViewMeno)
                    tableRowAllActions.addView(textViewPrichod)
                    tableRowAllActions.addView(textViewOdchod)
                    tableRowAllActions.addView(textViewHodiny)
                    tableRowAllActions.addView(textViewPoznamka)
                    if (privileges == "11" || privileges == "111") {
                        textViewUprav.setText("Upraviť")
                        textViewUprav.setTextColor(Color.GREEN)
                        textViewUprav.height = 70
                        textViewUprav.gravity = Gravity.CENTER
                        textViewUprav.setPadding(30, 0, 20, 0)
                        tableRowAllActions.addView(textViewUprav)
                    }


                    dochadzkaTable.addView(tableRowAllActions, i + 1)


                }

                var count = dochadzkaTable.childCount
                for (i in 1 until count) {
                    val v: View = dochadzkaTable.getChildAt(i)
                    if (v is TableRow) {
                        var row: TableRow = v as TableRow
                        var rowCount = row.childCount
                        for (j in 0 until rowCount) {
                            val v2: View = row.getChildAt(j)
                            if (v2 is TextView) {
                                var cell = v2 as TextView
                                cell.setOnClickListener {
                                    val vibrate =
                                        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                    vibrate.vibrate(70)
                                    if (it is TextView) {
                                        if (it.text == "Upraviť") {
                                            val dialogView = LayoutInflater.from(context)
                                                .inflate(R.layout.dialog_changedochadzka, null)
                                            val mBuilder =
                                                AlertDialog.Builder(context).setView(dialogView)

                                            val meno = row.getChildAt(0) as TextView
                                            val casPrichodu =
                                                ((row.getChildAt(1) as TextView).text.toString().split(
                                                    " "
                                                ).toTypedArray()[0])
                                            val datumPrichodu =
                                                (row.getChildAt(1) as TextView).text.toString().split(
                                                    " "
                                                ).toTypedArray()[1]
                                            val casOdchodu =
                                                (row.getChildAt(2) as TextView).text.toString().split(
                                                    " "
                                                ).toTypedArray()[0]
                                            val datumOdchodu =
                                                (row.getChildAt(2) as TextView).text.toString().split(
                                                    " "
                                                ).toTypedArray()[1]
                                            val poznamka = row.getChildAt(4) as TextView

                                            dialogView.menoDialog.text = meno.text
                                            dialogView.casPrichoduDialog.setText(casPrichodu)
                                            dialogView.datumPrichoduDialog.setText(datumPrichodu)
                                            dialogView.casOdchoduDialog.setText(casOdchodu)
                                            dialogView.datumOdchoduDialog.setText(datumOdchodu)
                                            dialogView.poznamkaDialog.setText(poznamka.text)





                                            dialogView.casPrichoduDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                val timeMethods: TimeMethods = TimeMethods()
                                                timeMethods.SetTimePicker(
                                                    dialogView.context,
                                                    dialogView.casPrichoduDialog
                                                )
                                            }
                                            dialogView.datumPrichoduDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                val timeMethods: TimeMethods = TimeMethods()
                                                timeMethods.SetDatePicker(
                                                    dialogView.context,
                                                    dialogView.datumPrichoduDialog
                                                )
                                            }
                                            dialogView.casOdchoduDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                val timeMethods: TimeMethods = TimeMethods()
                                                timeMethods.SetTimePicker(
                                                    dialogView.context,
                                                    dialogView.casOdchoduDialog
                                                )
                                            }
                                            dialogView.datumOdchoduDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                val timeMethods: TimeMethods = TimeMethods()
                                                timeMethods.SetDatePicker(
                                                    dialogView.context,
                                                    dialogView.datumOdchoduDialog
                                                )
                                            }

                                            val mAlertDialog = mBuilder.show()
                                            dialogView.buttonSpatDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                mAlertDialog.dismiss()
                                            }
                                            dialogView.buttonPotvrditDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                var timeParser = SimpleDateFormat("HH:mm")
                                                var timeFormatter = SimpleDateFormat("HH:mm")
                                                var dateParser = SimpleDateFormat("dd.MM.yyyy")
                                                var dateFormatter = SimpleDateFormat("dd.MM.yyyy")
                                                var dateOdTIME =
                                                    dateFormatter.format(dateParser.parse(dialogView.datumPrichoduDialog.text.toString()))
                                                var dateDoTIME =
                                                    dateFormatter.format(dateParser.parse(dialogView.datumOdchoduDialog.text.toString()))
                                                var casOdTIME =
                                                    timeFormatter.format(timeParser.parse(dialogView.casPrichoduDialog.text.toString()))
                                                var casDoTIME =
                                                    timeFormatter.format(timeParser.parse(dialogView.casOdchoduDialog.text.toString()))
                                                val timeMethod: TimeMethods = TimeMethods()
                                                if (dateOdTIME <= dateDoTIME) {
                                                    if ((casOdTIME <= casDoTIME && dateOdTIME <= dateDoTIME) || (casOdTIME > casDoTIME && dateOdTIME < dateDoTIME)) {
                                                        var timeDifference =
                                                            timeMethod.dateDifference(
                                                                dateOdTIME,
                                                                casOdTIME,
                                                                dateDoTIME,
                                                                casDoTIME
                                                            )


                                                        var updateResult =
                                                            dbMethods.updateDochadzka(
                                                                meno.text.toString(),
                                                                dateOdTIME,
                                                                dateDoTIME,
                                                                casOdTIME,
                                                                casDoTIME,
                                                                timeDifference,
                                                                dialogView.poznamkaDialog.text.toString(),
                                                                i.toString()
                                                            )
                                                        if (updateResult == "1") {
                                                            Toast.makeText(
                                                                context,
                                                                "Dochádzka zmenená",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                            startActivity(context, intent, null)
                                                            mAlertDialog.dismiss()
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Hups! Niečo je zlé. Skúste neskôr",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    } else
                                                        Toast.makeText(
                                                            context,
                                                            "Hups! Niečo je zlé. Skontrolujte čas!",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                } else
                                                    Toast.makeText(
                                                        context,
                                                        "Hups! Niečo je zlé. Skontrolujte dátumy!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                            }
                                        } else {
                                            val dialogView = LayoutInflater.from(context)
                                                .inflate(R.layout.dialog_fulldochadzka, null)
                                            val mBuilder =
                                                AlertDialog.Builder(context).setView(dialogView)
                                            dialogView.menoDialog.text =
                                                allDochadzka[i - 1][0]
                                            dialogView.prichodDialog.text =
                                                allDochadzka[i - 1][1]
                                            dialogView.odchodDialog.text =
                                                allDochadzka[i - 1][2]
                                            dialogView.hodinyDialog.text =
                                                allDochadzka[i - 1][3]
                                            dialogView.poznDialog.text =
                                                allDochadzka[i - 1][4]
                                            val mAlertDialog = mBuilder.show()
                                            dialogView.buttonSpatDialog.setOnClickListener {
                                                val vibrate =
                                                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                                vibrate.vibrate(70)
                                                mAlertDialog.dismiss()
                                            }
                                        }

                                    }
                                }

                            }
                        }

                    } else {
                    }
                }
            }
        }
        else
        {
            Toast.makeText(
                context,
                "Hups! Nie ste pripojený na internet.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}