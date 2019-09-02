package sk.letsdream.dbMethods

import android.content.Context
import android.database.SQLException
import android.widget.Toast
import kotlinx.android.synthetic.main.content_dochadzka.*
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class DBConnection {

    fun getLabelStatistics(actionName: String): Array<String> {
        val sql = "http://letsdream.xf.cz/index.php?action=" + actionName.replace(" ", "_") + "&mod=getAction&rest=get"

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
                if (jsonStr == "0")
                    return "NaN".split(",").toTypedArray()
                else {
                    return jsonStr.split(",").toTypedArray()
                }
            }
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception(e)
        }
        return "NaN".split(",").toTypedArray()
    }

    fun setLabelStatistics(action: String, setting: String, value: String): String {
        val sql = "http://letsdream.xf.cz/index.php?action=" + action.replace(" ", "_") + "&setting=" +
                setting + "&value=" + value.replace(" ", "_") + "&mod=updateAction&rest=post"

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
                if (jsonStr.contains("0")) {
                    return "0"
                } else {
                    return "1"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }


    fun addNewAction(
        action: String,
        pocDobr: String,
        pocNavs: String,
        datum: String,
        casOd: String,
        casDo: String,
        poznamka: String
    ): String {
        val sql = "http://letsdream.xf.cz/index.php?action=" + action.replace(" ", "_") + "&pocDobr=" +
                pocDobr + "&pocNavs=" + pocNavs + "&datum=" + datum + "&casOd=" + casOd +
                "&casDo=" + casDo + "&poznamka=" + poznamka.replace(" ", "_") + "&mod=addNewAction&rest=post"

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
                if (jsonStr.contains("1")) {
                    return "1"
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun deleteAction(action: String): String {
        val sql =
            "http://letsdream.xf.cz/index.php?action=" + action.replace(" ", "_") + "&mod=deleteAction&rest=delete"

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
                if (jsonStr.contains("1")) {
                    return "1"
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getStatistics(stat: Int): String {
        val sql = "http://letsdream.xf.cz/index.php?statistic=" + stat + "&mod=getStatistics&rest=get"

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
                if (jsonStr != "0") {
                    return jsonStr
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun addActivityToVolunteer(
        meno: String,
        prichodDatePicker: String,
        prichodTimePicker: String,
        odchodDatePicker: String,
        odchodTimePicker: String,
        timeDifference: String,
        poznamkaET: String
    ): String {
        val sql = "http://letsdream.xf.cz/index.php?meno=" + meno.replace(" ", "_") +
                "&prichoddatum=" + prichodDatePicker + "&prichodcas=" + prichodTimePicker + "&odchoddatum=" +
                odchodDatePicker + "&odchodcas=" + odchodTimePicker + "&hodiny=" + timeDifference + "&poznamka=" +
                poznamkaET.toString().replace(" ", "_") + "&mod=insertDochadzka&table=dochadzka&rest=post"

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
                if (jsonStr.contains("1")) {
                    return "1"
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getAllApprovedNames(): String {
        val sql = "http://letsdream.xf.cz/index.php?mod=getAllApprovedNames&rest=get"

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
                if (!jsonStr.contains("0")) {
                    return jsonStr
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun promoteToAdmin(name: String): String {
        val sql = "http://letsdream.xf.cz/index.php?name=" + name + "&mod=promote&rest=post"

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
                if (!jsonStr.contains("0")) {
                    return "1"
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun demoteToUser(name: String): String {
        val sql = "http://letsdream.xf.cz/index.php?name=" + name + "&mod=demote&rest=post"

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
                if (!jsonStr.contains("0")) {
                    return "1"
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getPrivileges(name: String): String {
        val sql = "http://letsdream.xf.cz/index.php?name=" + name + "&mod=getPrivileges&rest=get"

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
                if (!jsonStr.contains("0")) {
                    return jsonStr
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getNewRegistrations(): String {
        val sql = "http://letsdream.xf.cz/index.php?mod=getRegistrations&rest=get"

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
                if (!jsonStr.contains("0")) {
                    return jsonStr
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getNewRegistrationsTable(): String {
        val sql = "http://letsdream.xf.cz/index.php?mod=getRegistrationsTable&rest=get"

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
                if (!jsonStr.contains("0")) {
                    return jsonStr
                } else {
                    return "0"
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun acknowledgeUser(login: String): String {
        val sql = "http://letsdream.xf.cz/index.php?login=" + login + "&mod=acknowledgeUser&rest=post"

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
                if (!jsonStr.contains("0")) {
                    return jsonStr
                } else {
                    return "0"
                }
            }

        }
        catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun deleteUser(login: String): String {
        val sql = "http://letsdream.xf.cz/index.php?login=" + login + "&mod=deleteUser&rest=delete"

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
                if (!jsonStr.contains("0")) {
                    return "1"
                } else {
                    return "0"
                }
            }

        }
        catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }
    fun getAllHoursForUser(name: String): String {
        var meno = name
        if(name.contains(" "))
            meno = name.replace(" ","_")
        val sql = "http://letsdream.xf.cz/index.php?name=" + meno + "&mod=getAllHoursForUser&rest=get"

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
                if (jsonStr!="0") {
                    var list = jsonStr.split(",").toTypedArray()
                    list = list.dropLast(1).toTypedArray()
                    var hoursMinutes = arrayOf<Array<String>>()
                    var hours = 0
                    var minutes = 0
                    for(i in 0 until list.size)
                    {
                        hoursMinutes += list[i].split(":").toTypedArray()
                        hours += hoursMinutes[i][0].toInt()
                        minutes += hoursMinutes[i][1].toInt()
                    }

                    if(minutes>59)
                    {
                        hours += minutes/60
                        minutes -= (minutes/60)*60
                    }
                    if(minutes<10)
                        return hours.toString() + ":0" + minutes.toString()
                    else
                        return hours.toString() + ":" + minutes.toString()
                } else {
                    return "0"
                }
            }

        }
        catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun getAllActions(): String
    {
        val sql = "http://letsdream.xf.cz/index.php?mod=getAllActionsForTable&rest=get"

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
                if (jsonStr != "0") {
                    return jsonStr
                } else {
                    return "0"
                }
            }

        }
        catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }

    fun editAction(context: Context, action: String, date: String, from: String, until: String, original: String): String
    {
        val sql = "http://letsdream.xf.cz/index.php?action=" + action + "&date=" + date +
                "&from=" + from + "&until=" + until + "&original=" + original + "&mod=editAction&rest=post"

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
                if (jsonStr != "0") {
                    return "1"
                } else {
                    return "0"
                }
            }

        }
        catch (e: Exception) {
            throw Exception(e)
        }
        return "0"
    }
}