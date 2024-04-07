package com.example.barcodescannerdemo.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.barcodescannerdemo.Constants
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.miscellaneous.PalletData

class Pallet(
    private var context: Context,
    private var palletSerialNumber: String,
    private var vauxSerialNumber: String = "unspecified",
    private var latitude: String = "",
    private var longitude: String = "") {
    // everything that is contained in the '()', includes the functionality of the 'Pallet' method, as per described in the UML classdiagram

    private var db: SQLiteDatabase = VauxDataDBHelper(context).readableDatabase

    fun getPallets(cursor: Cursor): PalletData {

        val sqlStatement = "SELECT * FROM ${VauxDataContract.Pallet.TABLE_NAME} WHERE ${VauxDataContract.Pallet.COLUMN_NAME_VAUX_SERIAL_NUMBER} = '$vauxSerialNumber'"

        try {
            val cursor2 = db.rawQuery(sqlStatement, null)
                ?: throw Exception("no pallets found")

            val listOfPallets = mutableListOf<String>()

            if (cursor2.count != 0) {
                if (cursor2.moveToFirst()) {
                    do {
                        val pallet = cursor2.getString(0)
                        listOfPallets.add(pallet)
                    } while (cursor2.moveToNext())
                }

            } else {

                cursor2.close()

            }

            println(listOfPallets + "LIST OF PALLET LIST OF PALLETS")

        } catch (e: Exception) {
            throw Exception()
        }

        // functionality according to UML Activity Diagram

        try {
            return PalletData(
                getPalletSerialNumber(cursor),
                getVauxSerialNumber(cursor),
                Coordinates().getLatitude(cursor),
                Coordinates().getLongitude(cursor)
            )
        } catch (e: Exception){

            throw Exception()

        }

    }

    private fun getPalletSerialNumber(cursor: Cursor): String {

        val sqlStatement = "SELECT ${VauxDataContract.Pallet._ID} FROM ${VauxDataContract.Pallet.TABLE_NAME} WHERE ${VauxDataContract.Pallet._ID} = '$palletSerialNumber'"

        var palletSerialNumber = "unspecified"

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no pallet serial number found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            palletSerialNumber = cursor2.getString(0)
            cursor2.close()

        } else {

            cursor2.close()

        }

        return palletSerialNumber

    }

    private fun getVauxSerialNumber(cursor: Cursor): String {

        val sqlStatement = "SELECT ${VauxDataContract.Pallet.COLUMN_NAME_VAUX_SERIAL_NUMBER} FROM ${VauxDataContract.Pallet.TABLE_NAME} WHERE ${VauxDataContract.Pallet._ID} = '$palletSerialNumber'"

        var vauxSerialNumber = "unspecified"

        val cursor2 = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no Vaux serial number found")

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            vauxSerialNumber = cursor2.getString(0)
            cursor2.close()

        } else {

            cursor2.close()

        }

        return vauxSerialNumber

    }

    private inner class Coordinates: GeodeticCoordinates(){

        override fun getLatitude(cursor: Cursor): String {

            val sqlStatement = "SELECT ${VauxDataContract.Pallet.COLUMN_NAME_LATITUDE} FROM ${VauxDataContract.Pallet.TABLE_NAME} WHERE ${VauxDataContract.Pallet._ID} = '$palletSerialNumber'"

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

            val sqlStatement = "SELECT ${VauxDataContract.Pallet.COLUMN_NAME_LONGITUDE} FROM ${VauxDataContract.Pallet.TABLE_NAME} WHERE ${VauxDataContract.Pallet._ID} = '$palletSerialNumber'"

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

    fun addNewPallet() {

        //TODO: SQL EXCEPTION LATEN OPVANGEN BIJ VIEW METHODE DIE DEZE METHODE GEBRUIKT!!

        var result = 0L

        val latitudeRegex = Constants.LATITUDE_REGEX
        val longitudeRegex = Constants.LONGITUDE_REGEX
        val palletNumbersRegex = Constants.SIX_NUMBERS_REGEX

        if (
            latitudeRegex.matches(latitude)
            && longitudeRegex.matches(longitude)
            && palletNumbersRegex.matches(palletSerialNumber.substring(3))
            ) {

            val contentValues = ContentValues().apply {
                put(VauxDataContract.Pallet._ID, palletSerialNumber)
                put(VauxDataContract.Pallet.COLUMN_NAME_VAUX_SERIAL_NUMBER, vauxSerialNumber)
                put(VauxDataContract.Pallet.COLUMN_NAME_LATITUDE, latitude)
                put(VauxDataContract.Pallet.COLUMN_NAME_LONGITUDE, longitude)
            }

            try {

                result = db.insertOrThrow(VauxDataContract.Pallet.TABLE_NAME, null, contentValues)

            } catch (se: SQLException) {

                throw SQLException(se.message)

            } catch (e: Exception) {

                throw Exception(e.message)

            }

            println("result code: $result")

            Log.d("SQL statement submission result (create)", result.toString())

        } else {

            throw SQLException()

        }

    }

    fun setNewCoordinates(newLatitude: String, newLongitude: String): Int {

        val latitudeRegex = Constants.LATITUDE_REGEX
        val longitudeRegex = Constants.LONGITUDE_REGEX

        var result: Int = 0

        if (latitudeRegex.matches(newLatitude) && longitudeRegex.matches(newLongitude)) {

            try {

                val contentValues = ContentValues().apply {
                    put(VauxDataContract.Pallet.COLUMN_NAME_LATITUDE, newLatitude)
                    put(VauxDataContract.Pallet.COLUMN_NAME_LONGITUDE, newLongitude)
                }

                result = db.update(
                    VauxDataContract.Pallet.TABLE_NAME,
                    contentValues,
                    "${VauxDataContract.Pallet._ID} = '${palletSerialNumber}'",
                    null)

                Log.d("SQL statement submission result (update)", result.toString())

            } catch (e: Exception) {

                throw Exception(e.message)

            }

            } else {

                throw Exception("not a latitude and/or longitiude")

            }

        return result

    }

    fun deleteEntry(){

        try {

            val result = db.delete(
                VauxDataContract.Pallet.TABLE_NAME,
                "${VauxDataContract.Pallet._ID} = '${palletSerialNumber}'",
                null)

            Log.d("SQL statement submission result (delete)", result.toString())

        } catch (e: Exception){

            throw Exception()

        }

    }

}
