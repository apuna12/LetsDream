package sk.letsdream.helperMethods

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Vibrator
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_poznamka.view.*
import org.w3c.dom.Text
import sk.letsdream.MainActivity
import sk.letsdream.R
import sk.letsdream.dbMethods.DBConnection
import java.sql.Time
import java.util.*




class UpdateLabelMethods {

    fun updateTimeLabel(context: Context, casOd: TextView, casDo: TextView, nazovAkcieVedlaSpinnera: TextView)
    {
        val dbMethods: DBConnection = DBConnection()
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(context,  R.style.DialogTheme, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            if(m < 10)
                casOd.setText("" + h + ":0" + m)
            else
                casOd.setText("" + h + ":" + m)

        }), hour, minute, true)
        tpd.setTitle("Čas od: ")


        val d = Calendar.getInstance()
        val hour2 = d.get(Calendar.HOUR)
        val minute2 = d.get(Calendar.MINUTE)

        val tpd2 = TimePickerDialog(context,  R.style.DialogTheme, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            if(m < 10)
                casDo.setText("" + h + ":0" + m)
            else
                casDo.setText("" + h + ":" + m)

        }), hour2, minute2, true)
        tpd2.setTitle("Čas do: ")

        tpd.show()
        tpd.setOnDismissListener {
            val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrate.vibrate(70)
            tpd2.show()
            tpd2.setOnDismissListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                if(dbMethods.setLabelStatistics(nazovAkcieVedlaSpinnera.text.toString(), "Cas_Od", casOd.text.toString()) == "1")
                {
                    Toast.makeText(context,"Záznam upravený", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(context,"Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
                }

                if(dbMethods.setLabelStatistics(nazovAkcieVedlaSpinnera.text.toString(), "Cas_Do", casDo.text.toString()) == "1")
                {
                    Toast.makeText(context,"Záznam upravený", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(context,"Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updateNumberLabels(context: Context, upravit: TextView, textView: TextView, action: TextView, setting: String)
    {
        val dbMethods: DBConnection = DBConnection()


            var popUpMenu: PopupMenu = PopupMenu(context, upravit)
            for (i in 0 until 500)
            {
                popUpMenu.menu.add(i.toString())
            }
            popUpMenu.show()

            popUpMenu.setOnMenuItemClickListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                textView.setText(it.title.toString())
                if (setting == "pocDobr") {
                    if (dbMethods.setLabelStatistics(
                            action.text.toString(),
                            "Pocet_Dobrovolnikov",
                            textView.text.toString()
                        ) == "1"
                    )
                        Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
                } else if (setting == "pocNavs") {
                    if (dbMethods.setLabelStatistics(
                            action.text.toString(),
                            "Pocet_Navstevnikov",
                            textView.text.toString()
                        ) == "1"
                    )
                        Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
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
            if (dbMethods.setLabelStatistics(action.text.toString(), "Datum", textView.text.toString()) == "1")
                Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
        }


    }

    fun updatePoznLabels(context: Context, upravit: TextView, textView: TextView, action: TextView) {
        val dbMethods: DBConnection = DBConnection()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_poznamka, null)
        val mBuilder = AlertDialog.Builder(context).setView(dialogView).setTitle("Upravte poznámku")
        dialogView.poznDialogET.filters = arrayOf(*dialogView.poznDialogET.filters, InputFilter.LengthFilter(100))
        if (dialogView.parent != null)
            (dialogView.parent as ViewGroup).removeView(dialogView)
        else {
            val mAlertDialog = mBuilder.show()
            dialogView.poznDialogBUTTON.setOnClickListener {
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                mAlertDialog.dismiss()
                textView.setText(dialogView.poznDialogET.text)
                if (dbMethods.setLabelStatistics(action.text.toString(), "Poznamka", textView.text.toString()) == "1")
                    Toast.makeText(context, "Záznam upravený", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Záznam sa nepodarilo upraviť", Toast.LENGTH_LONG).show()
            }
            dialogView.backPoznDialogBUTTON.setOnClickListener{
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)
                mAlertDialog.cancel()
            }
        }

        true


    }


}