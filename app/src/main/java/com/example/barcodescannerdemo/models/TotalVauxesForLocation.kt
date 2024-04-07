package com.example.barcodescannerdemo.models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import java.lang.Exception

class TotalVauxesForLocation(private var context: Context, private var plusCode: String) {
    // everything that is contained in the '()', includes the functionality of the 'TotalVauxesForLocation' method, as per described in the UML classdiagram

    private var db: SQLiteDatabase = VauxDataDBHelper(context).readableDatabase

    fun getTotallVauxesForLocation(): Int {

        var totalNumber = 0

        val sqlStatement = "SELECT ${VauxDataContract.TotalVauxesForLocation.COLUMN_NAME_NUM_VAUXES} FROM ${VauxDataContract.TotalVauxesForLocation.VIEW_NAME} WHERE ${VauxDataContract.TotalVauxesForLocation._ID} = '$plusCode'"

        val cursor = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no total number of Vauxes for location found")

        if (cursor.count != 0) {
            cursor.moveToFirst()
            totalNumber = cursor.getInt(0)
            cursor.close()

        } else {

            cursor.close()

        }

        return totalNumber

    }

}