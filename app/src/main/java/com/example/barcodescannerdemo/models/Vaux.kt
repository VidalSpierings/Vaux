package com.example.barcodescannerdemo.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.example.barcodescannerdemo.Constants
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.miscellaneous.VauxData

class Vaux(
    private var context: Context,
    private var plusCode: String = "",
    private var vauxSerialNumber: String,
    private var latitude: String = "",
    private var longitude: String = "",
    private var numPlateaus: String = "",
    private var isAttached: Boolean = false) {

    private var db: SQLiteDatabase = VauxDataDBHelper(context).readableDatabase

    init {

        VauxesInLocation()

    }

    fun VauxesInLocation(plateauSerialNumber: String = "unspecified"){



    }

    fun getVauxes(cursor: Cursor): VauxData {

        try {

                getVauxSerialNumber(cursor) +
                Coordinates().getLatitude(cursor) +
                Coordinates().getLongitude(cursor) +
                getNumPlateaus(cursor) +
                getIsAttached(cursor)

            return VauxData(
                getPlusCode(cursor),
                getVauxSerialNumber(cursor),
                Coordinates().getLatitude(cursor),
                Coordinates().getLongitude(cursor),
                getNumPlateaus(cursor),
                getIsAttached(cursor)
            )

        } catch (e: Exception) {

            throw Exception()

        }

    }


    private fun getPlusCode(cursor: Cursor): String {

        val sqlStatement = "SELECT ${VauxDataContract.Vaux.COLUMN_NAME_PLUS_CODE} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

        var plusCodeString = ""

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no plus code found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            plusCodeString = cursor2.getString(0)
            cursor2.close()

        } else {

            cursor2.close()

        }

        return plusCodeString

    }

    private fun getVauxSerialNumber(cursor: Cursor): String {

        val sqlStatement = "SELECT ${VauxDataContract.Vaux._ID} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

        var vauxString = ""

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no vaux found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            vauxString = cursor2.getString(0)
            cursor2.close()

        } else {

            cursor2.close()

        }

        return vauxString

    }

    private inner class Coordinates: GeodeticCoordinates(){

        override fun getLatitude(cursor: Cursor): String {

            val sqlStatement = "SELECT ${VauxDataContract.Vaux.COLUMN_NAME_LATITUDE} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

            var latitudeString = ""

            val cursor2 = db.rawQuery(sqlStatement, null)
                ?: throw Exception("no latitude found")

            if (cursor2.count != 0) {
                cursor2.moveToFirst()
                latitudeString = cursor2.getDouble(0).toString()
                cursor2.close()

            } else {

                cursor2.close()

            }

            return latitudeString

        }

        override fun getLongitude(cursor: Cursor): String {

            val sqlStatement = "SELECT ${VauxDataContract.Vaux.COLUMN_NAME_LONGITUDE} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

            var longitudeString = ""

            val cursor2 = db.rawQuery(sqlStatement, null)
                ?: throw Exception("no longitude found")

            if (cursor2.count != 0) {
                cursor2.moveToFirst()
                longitudeString = cursor2.getDouble(0).toString()
                cursor2.close()

            } else {

                cursor2.close()

            }

            return longitudeString

        }


    }

    // contained within private class to adhere to UML classdiagram

    private fun getNumPlateaus(cursor: Cursor): String {

        val sqlStatement = "SELECT ${VauxDataContract.Vaux.COLUMN_NAME_NUM_PLATEAUS} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

        var numPlateausString = ""

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no num plateaus found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            numPlateausString = cursor2.getInt(0).toString()
            cursor2.close()

        } else {

            cursor2.close()

        }

        return numPlateausString

    }

    private fun getIsAttached(cursor: Cursor): Boolean {

        val sqlStatement = "SELECT ${VauxDataContract.Vaux.COLUMN_NAME_IS_ATTACHED} FROM ${VauxDataContract.Vaux.TABLE_NAME} WHERE ${VauxDataContract.Vaux._ID} = '$vauxSerialNumber'"

        var numPlateausString = 0

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no vaux found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            numPlateausString = cursor2.getInt(0)
            cursor2.close()

        } else {

            cursor2.close()

        }

        var realBoolean = false

        if (numPlateausString == 0) {

            realBoolean = false

        } else if (numPlateausString == 1) {

            realBoolean = true

        }

        // within the database, '0' represents 'false' and '1' represents 'true'

        return realBoolean

    }

    fun addNewVaux(){

        var result = 0L

        val latitudeRegex = Constants.LATITUDE_REGEX
        val longitudeRegex = Constants.LONGITUDE_REGEX
        val numbersRegex = Constants.SIX_NUMBERS_REGEX
        val numbersOnlyRegex = Constants.NUMBERS_REGEX

        val plusCodeRegexShort = Constants.PLUS_CODE_SHORT_REGEX
        val plusCodeRegexLong = Constants.PLUS_CODE_FULL_REGEX

        if (latitudeRegex.matches(latitude)
            && longitudeRegex.matches(longitude)
            && numbersRegex.matches(vauxSerialNumber.substring(2))
            && numbersOnlyRegex.matches(numPlateaus)
            && (plusCodeRegexShort.matches(plusCode) || plusCodeRegexLong.matches(plusCode))
            ) {

            // check whether all TextFields (EditTexts) do not contain illegal data

            val isAttachedInteger = if (isAttached) 1 else 0

            /*

            - return '1' when safety rails are attached, return '0' when safety rails are not attached.
              (in the database column 'is_attached' (of datatype INTEGER), 'true' is represented as '1',
              while 'false' is represented as '0')

            */

            try {

                val contentValues = ContentValues().apply {
                    put(VauxDataContract.Vaux.COLUMN_NAME_PLUS_CODE, plusCode)
                    put(VauxDataContract.Vaux._ID, vauxSerialNumber)
                    put(VauxDataContract.Vaux.COLUMN_NAME_LATITUDE, latitude)
                    put(VauxDataContract.Vaux.COLUMN_NAME_LONGITUDE, longitude)
                    put(VauxDataContract.Vaux.COLUMN_NAME_NUM_PLATEAUS, numPlateaus)
                    put(VauxDataContract.Vaux.COLUMN_NAME_IS_ATTACHED, isAttachedInteger)

                }

                result = db.insertOrThrow(VauxDataContract.Vaux.TABLE_NAME, null, contentValues)

                Toast.makeText(context, "added vaux", Toast.LENGTH_LONG).show()

                Log.d("SQL statement submission result code (create)", result.toString())

            } catch (e: Exception){

                throw Exception(e.message)

            }

        } else {

            throw Exception("some or all of the entered data is incorrect")

        }

    }

    fun setAllNewValuesExceptVauxSerialNumber(){

        val latitudeRegex = Constants.LATITUDE_REGEX
        val longitudeRegex = Constants.LONGITUDE_REGEX
        val numbersOnlyRegex = Constants.NUMBERS_REGEX

        val plusCodeRegexShort = Constants.PLUS_CODE_SHORT_REGEX
        val plusCodeRegexLong = Constants.PLUS_CODE_FULL_REGEX

        if (latitudeRegex.matches(latitude)
            && longitudeRegex.matches(longitude)
            && numbersOnlyRegex.matches(numPlateaus)
            && (plusCodeRegexShort.matches(plusCode) || plusCodeRegexLong.matches(plusCode))
        ) {

            try {

                val isAttachedInteger = if (isAttached) 1 else 0

            /*

            - return '1' when safety rails are attached, return '0' when safety rails are not attached.
              (in the database column 'is_attached' (of datatype INTEGER), 'true' is represented as '1',
              while 'false' is represented as '0')

            */

                val contentValues = ContentValues().apply {
                    put(VauxDataContract.Vaux.COLUMN_NAME_PLUS_CODE, plusCode)
                    put(VauxDataContract.Vaux.COLUMN_NAME_LATITUDE, latitude)
                    put(VauxDataContract.Vaux.COLUMN_NAME_LONGITUDE, longitude)
                    put(VauxDataContract.Vaux.COLUMN_NAME_NUM_PLATEAUS, numPlateaus)
                    put(VauxDataContract.Vaux.COLUMN_NAME_IS_ATTACHED, isAttachedInteger)
                }

                val result = db.update(
                    VauxDataContract.Vaux.TABLE_NAME,
                    contentValues,
                    "${VauxDataContract.Vaux._ID} = '${vauxSerialNumber.substring(2)}'",
                    null)

                Log.d("SQL statement submission result (update)", result.toString())

            } catch (e: Exception) {

                throw Exception(e.message)

            }

        } else {

            throw Exception("some or all of the entered data is incorrect")

        }

    }

    fun deleteEntry(){

        try {

            val db = VauxDataDBHelper(context).readableDatabase

            val result = db.delete(
                VauxDataContract.Vaux.TABLE_NAME,
                "${VauxDataContract.Vaux._ID} = '${vauxSerialNumber}'",
                null)

            Log.d("SQL statement submission result code (delete)", result.toString())

        } catch(e: Exception) {

            throw Exception(e.message)

        }

    }

}