package sk.letsdream

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import java.lang.Exception
import java.util.logging.Handler

class OpeningScreenActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_opening_screen)
        val background = object :Thread() {
            override fun run(){
                try{
                    Thread.sleep(5000)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

        }
        /*var progressBar: ProgressBar = findViewById(R.id.progress)
        progressBar.progressTintList = ColorStateList.valueOf(Color.BLUE)*/

        background.start()

    }
}