package sk.letsdream.helperMethods


import android.app.Activity
import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.app.TimePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import android.widget.TextView
import sk.letsdream.MainActivity
import sk.letsdream.R
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class TimeMethods {

    private lateinit var mRunnable: Runnable
    private lateinit var mHandler: Handler


    fun UpdateActualTime(date: TextView, time: TextView) {
        val timeFormatter: SimpleDateFormat = SimpleDateFormat("HH:mm")
        val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        mHandler = Handler()

        mRunnable = Runnable {
            val currentDate = dateFormatter.format(Date())
            val currentTime = timeFormatter.format(Date())

            time.text = currentTime
            date.text = currentDate
            mHandler.postDelayed(mRunnable, 5000)
        }
        mHandler.post(mRunnable)
    }


    fun SetDatePicker(context: Context, textView: TextView){
        textView.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(context, R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    textView.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
                }, year, month, day
            )
            dpd.show()
        }
    }

    fun SetTimePicker(context: Context, textView: TextView){
        textView.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(context,  R.style.DialogTheme, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                textView.setText("" + h + ":" + m)

            }), hour, minute, true)

            tpd.show()

        }
    }
}


