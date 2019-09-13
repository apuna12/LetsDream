package sk.letsdream.helperMethods

import android.content.Context
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import sk.letsdream.dbMethods.DBConnection
import sk.letsdream.helperClasses.LoginTableClass
import java.io.FileOutputStream
import java.util.*

class ProcessBackup {

    fun backupLogintable(context: Context): String
    {
        val dbMethods: DBConnection = DBConnection()
        val COLUMNs = arrayOf<String>("ID", "Username", "Name", "Email", "Privileges", "New_User")
        val dbSplittedData = dbMethods.backupDB().split("Ł").toTypedArray()
        var rowsFromDB = arrayOf<Array<String>>()

        for(i in 0 until dbSplittedData.size)
        {
            rowsFromDB += dbSplittedData[i].split("ł").toTypedArray()
        }

        if(rowsFromDB.size>0)
        {
            var rows = Arrays.asList(LoginTableClass("1", rowsFromDB[0][0], rowsFromDB[0][1],
                rowsFromDB[0][2], rowsFromDB[0][3], rowsFromDB[0][4], rowsFromDB[0][5]))
            for (i in 1 until rowsFromDB.size)
            {
                rows.add(LoginTableClass(i.toString(), rowsFromDB[0][0], rowsFromDB[0][1],
                    rowsFromDB[0][2], rowsFromDB[0][3], rowsFromDB[0][4], rowsFromDB[0][5]))
            }
            val workbook: XSSFWorkbook = XSSFWorkbook()
            val createHelper = workbook.creationHelper

            val sheet = workbook.createSheet("logintable")
            val headerFont = workbook.createFont()
            headerFont.bold = true
            headerFont.setColor(IndexedColors.BLUE.index)

            val headerCellStyle = workbook.createCellStyle()

            val headerRow = sheet.createRow(0)

            for(col in COLUMNs.indices)
            {
                val cell = headerRow.createCell(col)
                cell.setCellValue(COLUMNs[col])
                cell.cellStyle = headerCellStyle
            }

            var rowIdx = 1
            for(data in rows)
            {
                val row = sheet.createRow(rowIdx++)
                row.createCell(0).setCellValue(data.id)
                row.createCell(1).setCellValue(data.username)
                row.createCell(2).setCellValue(data.password)
                row.createCell(3).setCellValue(data.name)
                row.createCell(4).setCellValue(data.email)
                row.createCell(5).setCellValue(data.privileges)
                row.createCell(6).setCellValue(data.new_user)
            }

            val fileOut = FileOutputStream("logintable.xlsx")
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            return "1"
        }

        return "0"

    }

}