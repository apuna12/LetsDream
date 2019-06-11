package sk.letsdream.helperMethods


import android.app.Activity
import android.app.PendingIntent.getActivity
import android.widget.TextView
import sk.letsdream.MainActivity
import sk.letsdream.R
import java.text.SimpleDateFormat
import java.util.*


class timeMethod {
    fun updateTime(dateTextView: TextView, timeTextView: TextView){
        val timeFormatter: SimpleDateFormat = SimpleDateFormat("HH:mm")
        val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")


        val timeThread = object : Thread() {

            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        Thread.sleep(1000)
                        Runnable {

                            val currentDate = dateFormatter.format(Date())
                            val currentTime = timeFormatter.format(Date())

                            timeTextView.text = currentTime
                            dateTextView.text = currentDate
                        }
                    }
                } catch (e: InterruptedException) {
                }
                }

            }
    }
}