package sk.letsdream.helperMethods


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Vibrator
import android.widget.TextView
import sk.letsdream.R
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


    fun SetDatePicker(context: Context, textView: TextView) {

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
                textView.setText("" + dayOfMonth + "." + (monthOfYear+1) + "." + year)
            }, year, month, day
        )
        dpd.show()

    }

    fun SetTimePicker(context: Context, textView: TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd =
            TimePickerDialog(context, R.style.DialogTheme, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrate.vibrate(70)

                if (m < 10) {
                    if(h > 9)
                        textView.setText("" + h + ":0" + m)
                    else
                        textView.setText("0" + h + ":0" + m)
                }
                else
                {
                    if(h > 9)
                        textView.setText("" + h + ":" + m)
                    else
                        textView.setText("0" + h + ":" + m)
                }

            }), hour, minute, true)

        tpd.show()


    }

    fun dateDifference(startDate: String, startTime: String, endDate: String, endTime: String): String
    {
        val startDateTimeString = startDate + " " + startTime
        val endDateTimeString = endDate + " " + endTime
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

        var startDateTime = formatter.parse(startDateTimeString)
        var endDateTime = formatter.parse(endDateTimeString)

        var difference = endDateTime.time - startDateTime.time

        var secondsInMilis = 1000
        var minutesInMilis = secondsInMilis * 60
        var hoursInMilis = minutesInMilis * 60
        var daysInMilis = hoursInMilis * 24

        var elapsedHours = difference / hoursInMilis
        difference = difference % hoursInMilis

        var elapsedMinutes = difference / minutesInMilis
        difference = difference % minutesInMilis

        return elapsedHours.toString() + ":" + elapsedMinutes.toString()
    }
}


