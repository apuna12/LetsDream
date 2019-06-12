package sk.letsdream.helperMethods


import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.widget.TextView
import sk.letsdream.MainActivity
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

}


