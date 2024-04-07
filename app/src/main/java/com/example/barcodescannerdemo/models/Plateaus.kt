package com.example.barcodescannerdemo.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.barcodescannerdemo.Constants
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.miscellaneous.PlateauData

class Plateaus(
    private var context: Context,
    private var plateauSerialNumber: String,
    private var vauxSerialNumber: String = "",
    private var height: String = "") {

    private var db: SQLiteDatabase = VauxDataDBHelper(context).readableDatabase

    init {

        YAxisHeightForVauxPlateaus(plateauSerialNumber)

    }

    /*

    - the 'init' declaration, together with everything that is in between the '()' after the line 'class Plateaus',
      contains all the functionality of the constructor of this class

    */

    fun YAxisHeightForVauxPlateaus(plateauSerialNumber: String){



    }

    /*

    fun getPlateaus() =
        PlateauData(
            getPlateauSerialNumber(),
            getVauxSerialNumber(),
            getHeight()
        )

    */

    fun getPlateaus(cursor: Cursor? = null): PlateauData {

        try {
            return PlateauData(
                getPlateauSerialNumber(),
                getVauxSerialNumber(),
                getHeight()
            )
        } catch (e: Exception){

            throw Exception()

        }

    }

    private fun getPlateauSerialNumber(cursor: Cursor? = null): String {

        val sqlStatement = "SELECT ${VauxDataContract.Plateaus._ID} FROM ${VauxDataContract.Plateaus.TABLE_NAME} WHERE ${VauxDataContract.Plateaus._ID} = '$plateauSerialNumber'"

        var plateauSerialNumber = "unspecified"

        val cursor = db.rawQuery(sqlStatement, null)
            ?: throw Exception("no plateau serial number found")

        if (cursor.count != 0) {
            cursor.moveToFirst()
            plateauSerialNumber = cursor.getString(0)
            cursor.close()

        } else {

            cursor.close()

        }

        return plateauSerialNumber

    }

    private fun getVauxSerialNumber(cursor: Cursor? = null): String? {

        val sqlStatement = "SELECT ${VauxDataContract.Plateaus.COLUMN_NAME_VAUX_SERIAL_NUMBER} FROM ${VauxDataContract.Plateaus.TABLE_NAME} WHERE ${VauxDataContract.Plateaus._ID} = '$plateauSerialNumber'"

        var vauxSerialNumber: String? = "unspecified"

        val cursor: Cursor? = db.rawQuery(sqlStatement, null)

        if (cursor?.count != 0 && cursor != null) {
            println("CODE COMES HERE VAUX")
            cursor.moveToFirst()
            vauxSerialNumber = cursor.getString(0)
            cursor.close()

        } else if (cursor == null) {

            cursor?.close()

        }

        return vauxSerialNumber

    }

    private fun getHeight(cursor: Cursor? = null): String? {

        val sqlStatement = "SELECT ${VauxDataContract.Plateaus.COLUMN_NAME_HEIGHT} FROM ${VauxDataContract.Plateaus.TABLE_NAME} WHERE ${VauxDataContract.Plateaus._ID} = '$plateauSerialNumber'"

        var height: String? = "unspecified"

        val cursor: Cursor? = db.rawQuery(sqlStatement, null)

        if (cursor?.count != 0 && cursor != null) {
            cursor.moveToFirst()
            height = cursor.getString(0)
            cursor.close()

        } else if (cursor == null) {

            cursor?.close()

        }

        return height

    }

    fun addNewPlateau(): Long{

        //TODO: SQL EXCEPTION LATEN OPVANGEN BIJ VIEW METHODE DIE DEZE METHODE GEBRUIKT!!

        var result = 0L

        val serialNumbersRegex = Constants.SIX_NUMBERS_REGEX

        val plateauSerialNumbers = plateauSerialNumber.substring(3)
        val vauxSerialNumbers = vauxSerialNumber.substring(2)

        // check if filled in value conforms to a Float. If not, throw exception and inform user

        if (serialNumbersRegex.matches(plateauSerialNumbers)
            && (serialNumbersRegex.matches(vauxSerialNumbers) || vauxSerialNumbers == "")
            && (height == "" || height.matches(Regex("\\d+")))) {

            /*

              the six characters that come after the "VX" of the Vaux serial number, have to be a sequence of exactly six numbers, or,
              if no characters come after the "VX" prefix for the Vaux serial number, instead of six (meaning the user didn't fill in the field),
              AND the six characters that come after the "PLU" of the plateau serial number, are a sequence of exactly six numbers, the program
              still enters into the if statement here.

              - the validity of the height, specifically whether or not the String value of the height can be converted to Float,
                has already been tested. If however, the height is less than 0 or greater than 2.9, it will be rejected by the database,
                because a constraint for the minimum and maximum height of the plateau, is set by CHECK constraints in the database.

            */

            if (vauxSerialNumber != "VX") {

                vauxSerialNumber = "'$vauxSerialNumber'"

            } else {

                vauxSerialNumber = "NULL"

            }

            if (height == "") {

                height = "NULL"

            }

            val sqlValuesString = "('$plateauSerialNumber', $vauxSerialNumber, $height)"

            println(sqlValuesString)

            val cursor = db.rawQuery("INSERT INTO ${VauxDataContract.Plateaus.TABLE_NAME} VALUES $sqlValuesString", null)

            if (cursor != null) {
                cursor.close()
            }

            Log.d("SQL statement submission result (create)", result.toString())

        }

        else {

            throw Exception()

        }

        return result

    }

    fun setUpdatedHeightAndVaux(newHeight: Float, newVauxSerialNumber: String){

        val contentValues = ContentValues().apply {
            put(VauxDataContract.Plateaus.COLUMN_NAME_VAUX_SERIAL_NUMBER, newVauxSerialNumber)
            put(VauxDataContract.Plateaus.COLUMN_NAME_HEIGHT, newHeight)
        }

        val result = db.update(
            VauxDataContract.Plateaus.TABLE_NAME,
            contentValues,
            "${VauxDataContract.Plateaus._ID} = ${plateauSerialNumber}",
            null)

        Log.d("SQL statement submission result (update)", result.toString())

    }

    fun deleteEntry(){

        val db = VauxDataDBHelper(context).readableDatabase

        val result = db.delete(
            VauxDataContract.Plateaus.TABLE_NAME,
            "${VauxDataContract.Plateaus._ID} = '${plateauSerialNumber}'",
            null)

        Log.d("SQL statement submission result (delete)", result.toString())

    }

}
