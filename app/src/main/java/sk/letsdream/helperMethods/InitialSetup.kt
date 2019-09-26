package sk.letsdream.helperMethods

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Vibrator
import android.support.v4.content.ContextCompat.startActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_register.view.*
import sk.letsdream.LoginActivity
import sk.letsdream.R
import sk.letsdream.dbMethods.DBConnection
import java.lang.Exception
import java.net.URL
import java.security.MessageDigest

class InitialSetup {

    fun CreateSuperAdmin(context: Activity)
    {
        val dbMethods: DBConnection = DBConnection()
        var networkTask: NetworkTask
        val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val emailMethods: EmailMethods = EmailMethods()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_register, null)
        val mBuilder = AlertDialog.Builder(context).setView(dialogView)
        val mAlertDialog = mBuilder.show()
        val userRegister = dialogView.userEdt
        val dialogHeader = dialogView.dialogHeader
        val passRegister = dialogView.passwordRegEdt
        val passAgainRegister = dialogView.passwordAgainRegEdt
        val emailRegister = dialogView.emailRegEdt
        val nameRegister = dialogView.nameRegEdt
        val surnameRegister = dialogView.surnameRegEdt
        val back = dialogView.backRegBtn
        val gdprCHB = dialogView.gdprCHB
        val hexMethods: HexMethods = HexMethods()
        dialogHeader.text = "Registrácia hlavného administrátora"
        userRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                userRegister.hint = ""
            } else {
                if (userRegister.text.toString() == "")
                    userRegister.hint = "Používateľ"
            }
        }
        passRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                passRegister.hint = ""
            } else {
                if (passRegister.text.toString() == "")
                    passRegister.hint = "Heslo"
            }
        }
        passAgainRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                passAgainRegister.hint = ""
            } else {
                if (passAgainRegister.text.toString() == "")
                    passAgainRegister.hint = "Heslo znova"
            }
        }
        emailRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                emailRegister.hint = ""
            } else {
                if (emailRegister.text.toString() == "")
                    emailRegister.hint = "Email"
            }
        }

        nameRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                emailRegister.hint = ""
            } else {
                if (emailRegister.text.toString() == "")
                    emailRegister.hint = "Zadajte meno"
            }
        }
        surnameRegister.setOnFocusChangeListener { v, hasFocus ->
            vibrate.vibrate(70)
            if (hasFocus) {
                emailRegister.hint = ""
            } else {
                if (emailRegister.text.toString() == "")
                    emailRegister.hint = "Zadajte priezvisko"
            }
        }

        userRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                passRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        passRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                passAgainRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        passAgainRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                nameRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        nameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                surnameRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                emailRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        surnameRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                emailRegister.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        emailRegister.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            vibrate.vibrate(70)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                gdprCHB.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        dialogView.registrujBtn.setOnClickListener {
            vibrate.vibrate(70)
            networkTask = NetworkTask(context)
            networkTask.execute()
            if(isOnline(context)) {

                if (userRegister.text.toString() == "") {
                    Toast.makeText(context, "Prosím zadajte meno účtu!", Toast.LENGTH_LONG).show()
                } else if (passRegister.text.toString() == "") {
                    Toast.makeText(context, "Prosím zadajte heslo!", Toast.LENGTH_LONG).show()
                } else if (passAgainRegister.text.toString() == "") {
                    Toast.makeText(context, "Prosím zadajte znova heslo!", Toast.LENGTH_LONG)
                        .show()
                } else if (emailRegister.text.toString() == "") {
                    Toast.makeText(context, "Prosím zadajte emailovú adresu!", Toast.LENGTH_LONG)
                        .show()
                } else if (!emailMethods.isEmailValid(emailRegister.text.toString())) {
                    Toast.makeText(
                        context,
                        "Prosím zadajte správnu emailovú adresu!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (passRegister.text.toString() != passAgainRegister.text.toString()) {
                    Toast.makeText(context, "Vaše heslá sa nezhodujú!", Toast.LENGTH_LONG).show()
                } else if (nameRegister.text.toString() == "") {
                    Toast.makeText(context, "Zadajte prosím meno!", Toast.LENGTH_LONG).show()
                } else if (surnameRegister.text.toString() == "") {
                    Toast.makeText(context, "Zadajte prosím priezvisko!", Toast.LENGTH_LONG).show()
                } else if (!gdprCHB.isChecked) {
                    Toast.makeText(
                        context,
                        "Pre úspešne zaregistrovanie musíte súhlasiť so spracovaním údajov!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    val sql =
                        "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&mod=register&rest=get"

                    try {
                        var jsonStr: String = URL(sql).readText()
                        var firstApp: Int = 0
                        var lastApp: Int = 0
                        if (jsonStr.toString().contains("<") || jsonStr.toString().contains(">")) {
                            for (i in 0 until jsonStr.toString().length) {
                                if (jsonStr[i] == '<') {
                                    firstApp = i
                                    break
                                }
                            }
                            for (i in 0 until jsonStr.toString().length) {
                                if (jsonStr[i] == '>')
                                    lastApp = i
                            }
                            jsonStr = jsonStr.removeRange(firstApp, lastApp + 1)
                        }
                        if (jsonStr == "1")
                            Toast.makeText(
                                context,
                                "Toto meno už používa niekto iný",
                                Toast.LENGTH_LONG
                            ).show()
                        else {

                            var digest: MessageDigest
                            digest = MessageDigest.getInstance("SHA-256")
                            digest.update(passRegister.text.toString().toByteArray())
                            var hash = hexMethods.bytesToHexString(digest.digest())

                            val sql =
                                "http://letsdream.xf.cz/index.php?username=" + userRegister.text + "&password=" + hash +
                                        "&email=" + emailRegister.text + "&name=" + surnameRegister.text + ",_" +
                                        nameRegister.text + "&mod=registerSuperadmin&rest=post"

                            try {
                                var jsonStr: String = URL(sql).readText()
                                var firstApp: Int = 0
                                var lastApp: Int = 0
                                if (jsonStr.toString().contains("<") || jsonStr.toString().contains(
                                        ">"
                                    )
                                ) {
                                    for (i in 0 until jsonStr.toString().length) {
                                        if (jsonStr[i] == '<') {
                                            firstApp = i
                                            break
                                        }
                                    }
                                    for (i in 0 until jsonStr.toString().length) {
                                        if (jsonStr[i] == '>')
                                            lastApp = i
                                    }
                                    jsonStr = jsonStr.removeRange(firstApp, lastApp + 1)
                                }
                                if (jsonStr.contains("1")) {
                                    Toast.makeText(
                                        context,
                                        "Používateľ úspešne registrovaný!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(context, intent, null)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Hups, niečo je zlé!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            } catch (e: Exception) {
                                throw Exception(e)
                            }
                        }
                    } catch (e: Exception) {
                        throw Exception(e)
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
        back.setOnClickListener {
            vibrate.vibrate(70)
            mAlertDialog.cancel()
        }

    }

    fun initialDBCreation():String
    {
        val dbConnection : DBConnection = DBConnection()
        return dbConnection.initialDBCreation()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}