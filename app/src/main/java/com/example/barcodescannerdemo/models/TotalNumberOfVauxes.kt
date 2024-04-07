package com.example.barcodescannerdemo.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import java.lang.Exception

class TotalNumberOfVauxes(private var context: Context) {

    var db: SQLiteDatabase = VauxDataDBHelper(context).readableDatabase

    fun getTotalNumberOfVauxes(): Int {

        val sqlStatement = "SELECT * FROM ${VauxDataContract.TotalNumberOfVauxes.VIEW_NAME};"

        val cursor = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no total number of Vauxes found")

        cursor.moveToFirst()

        val totalNumber = cursor.getInt(0)

        cursor.close()

        return totalNumber

    }

}