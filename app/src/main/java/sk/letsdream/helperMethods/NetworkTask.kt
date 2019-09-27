package sk.letsdream.helperMethods

import android.app.Activity
import android.app.Dialog
import android.os.AsyncTask
import sk.letsdream.R

class NetworkTask(var activity: Activity) : AsyncTask<Void, Void, Void>() {

    var dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)

    override fun onPreExecute() {
        val view = activity.layoutInflater.inflate(R.layout.full_screen_progress_bar, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        dialog.dismiss()
    }
}