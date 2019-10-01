package sk.letsdream

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.*
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperMethods.ButtonEffects
import sk.letsdream.helperMethods.NetworkTask
import sk.letsdream.helperMethods.TimeMethods
import android.support.v4.os.HandlerCompat.postDelayed



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var privileges: String = ""
    var loginName: String = ""
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        privileges = intent.getStringExtra("privileges")
        loginName = intent.getStringExtra("login")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val date: TextView = findViewById(R.id.full_dateTW)
        val time: TextView = findViewById(R.id.timeTW)

        val dochadzka: ImageButton = findViewById(R.id.door_imageButton)
        val meno: ImageButton = findViewById(R.id.person_imageButton)
        val akcie: ImageButton = findViewById(R.id.star_imageButton)
        val statistika: ImageButton = findViewById(R.id.graph_imageButton)
        val admin: ImageButton = findViewById(R.id.adminButton)
        val vibrate = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val timeMethod: TimeMethods = TimeMethods()
        val buttonEffects: ButtonEffects = ButtonEffects()
        var networkTask: NetworkTask


        timeMethod.UpdateActualTime(date, time)
        buttonEffects.ButtonClickEffect(findViewById(R.id.graph_imageButton))
        buttonEffects.ButtonClickEffect(findViewById(R.id.star_imageButton))
        buttonEffects.ButtonClickEffect(findViewById(R.id.person_imageButton))
        buttonEffects.ButtonClickEffect(findViewById(R.id.door_imageButton))
        buttonEffects.ButtonClickEffect(findViewById(R.id.adminButton))

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        dochadzka.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val intent = Intent(this@MainActivity, DochadzkaActivity::class.java)
                intent.putExtra("privileges", privileges)
                intent.putExtra("login", loginName)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        meno.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val intent = Intent(this@MainActivity, VyberMenaActivity::class.java)
                intent.putExtra("privileges", privileges)
                intent.putExtra("login", loginName)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        akcie.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val intent = Intent(this@MainActivity, AkcieActivity::class.java)
                intent.putExtra("privileges", privileges)
                intent.putExtra("login", loginName)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        statistika.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(this)
            networkTask.execute()
            if (isOnline(this)) {

                val dbMethods: DBConnection = DBConnection()

                if (dbMethods.getNumberOfActions() != "0") {
                    val intent = Intent(this@MainActivity, StatistikyActivity::class.java)
                    intent.putExtra("privileges", privileges)
                    intent.putExtra("login", loginName)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "V databáze zatiaľ nie sú žiadne akcie.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else

                Toast.makeText(
                    this,
                    "Hups! Nie ste pripojený na internet",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (privileges != "111") {
            admin.visibility = View.INVISIBLE
            adminButtonTW.visibility = View.INVISIBLE
        } else {
            admin.visibility = View.VISIBLE
            adminButtonTW.visibility = View.VISIBLE
            admin.setOnClickListener {
                vibrate.vibrate(70)
                networkTask = NetworkTask(this)
                networkTask.execute()
                if (isOnline(this)) {

                    val intent = Intent(this@MainActivity, AdminActivity::class.java)
                    intent.putExtra("privileges", privileges)
                    intent.putExtra("login", loginName)
                    startActivity(intent)
                }
            }
        }


    }

    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pre odhlásenie kliknite 'Späť' 2x po sebe. ", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
