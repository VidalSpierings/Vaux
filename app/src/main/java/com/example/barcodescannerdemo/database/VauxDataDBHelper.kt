package com.example.barcodescannerdemo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.barcodescannerdemo.database.VauxDataContract.Vaux
import com.example.barcodescannerdemo.database.VauxDataContract.Pallet
import com.example.barcodescannerdemo.database.VauxDataContract.Plateaus
import com.example.barcodescannerdemo.database.VauxDataContract.TotalNumberOfVauxes
import com.example.barcodescannerdemo.database.VauxDataContract.TotalVauxesForLocation

class VauxDataDBHelper(context: Context, databaseName: String = "VauxData.db", databaseVersion: Int = 1)
    : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

        // classes in Kotlin are final, unless prefixed with 'open'

        companion object {

            private val ENFORCE_STRICT_TYPES = "PRAGMA strict_types = ON;"

            private val CREATE_TABLE_VAUXES_SERIAL_NUMBERS_FOR_LOCATIONS =
                "CREATE TABLE '${Vaux.TABLE_NAME}' ("+
                    "'${Vaux.COLUMN_NAME_PLUS_CODE}' TEXT NOT NULL, " +
                    "'${Vaux._ID}' TEXT NOT NULL, " +
                    "'${Vaux.COLUMN_NAME_LATITUDE}' REAL NOT NULL, " +
                    "'${Vaux.COLUMN_NAME_LONGITUDE}' REAL NOT NULL, " +
                    "'${Vaux.COLUMN_NAME_NUM_PLATEAUS}' INTEGER NOT NULL CHECK(${Vaux.COLUMN_NAME_NUM_PLATEAUS} >= 0), " +
                    "'${Vaux.COLUMN_NAME_IS_ATTACHED}' INTEGER NOT NULL CHECK (${Vaux.COLUMN_NAME_IS_ATTACHED} IN (0, 1)), " +
                    "PRIMARY KEY ('${Vaux._ID}')" +
                    ");"

            /*

            - in latitude and longitude, any values that end with a 0, will be deappended when
              entered into the database. Despite this fact, the correct syntax  for a submitted
              latitude and longitude will stay intact.

            for example:

            the user enters in '51.707130' for a latitude coordinate. The value will be saved as
            '51.70713' in the database. Despite the fact that the '0' is not present in the database,
            these two coordinates are the exact same with the Geodetic coordination system

            */

            private val CREATE_TABLE_Y_AXIS_HEIGHT_FOR_VAUX_PLATEAUS =
                "CREATE TABLE '${Plateaus.TABLE_NAME}' ( "+
                    "'${Plateaus._ID}' TEXT NOT NULL, " +
                    "'${Plateaus.COLUMN_NAME_VAUX_SERIAL_NUMBER}' TEXT, " +
                    "'${Plateaus.COLUMN_NAME_HEIGHT}' REAL CHECK(${Plateaus.COLUMN_NAME_HEIGHT} >= 0 AND ${Plateaus.COLUMN_NAME_HEIGHT} <= 2.90 AND ${Plateaus.COLUMN_NAME_HEIGHT} == ROUND(${Plateaus.COLUMN_NAME_HEIGHT}, 2)), " +
                    "PRIMARY KEY ('${Plateaus._ID}'), " +
                    "FOREIGN KEY (${Plateaus.COLUMN_NAME_VAUX_SERIAL_NUMBER}) REFERENCES ${Vaux.TABLE_NAME}(${Vaux._ID}) " +
                    "ON DELETE SET NULL ON UPDATE CASCADE" +
                    ");"

            //(ON DELETE AND ON UPDATE CLAUSES WORK)

            private val CREATE_TABLE_VAUXES_PALLETS_COORDINATES =
                "CREATE TABLE '${Pallet.TABLE_NAME}' ("+
                    "'${Pallet._ID}' TEXT NOT NULL, " +
                    "'${Pallet.COLUMN_NAME_VAUX_SERIAL_NUMBER}' TEXT NOT NULL, " +
                    "'${Pallet.COLUMN_NAME_LATITUDE}' REAL NOT NULL, " +
                    "'${Pallet.COLUMN_NAME_LONGITUDE}' REAL NOT NULL, " +
                    "PRIMARY KEY ('${Pallet._ID}'), " +
                    "FOREIGN KEY (${Pallet.COLUMN_NAME_VAUX_SERIAL_NUMBER}) REFERENCES ${Vaux.TABLE_NAME}(${Vaux._ID}) " +
                    "ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");"

            //(ON DELETE AND ON UPDATE CLAUSES WORK)

            /*

            - in latitude and longitude, any values that end with a 0, will be deappended when
              entered into the database. Despite this fact, the correct syntax  for a submitted
              latitude and longitude will stay intact.

            for example:

            the user enters in '51.707130' for a latitude coordinate. The value will be saved as
            '51.70713' in the database. Despite the fact that the '0' is not present in the database,
            these two coordinates are the exact same with the Geodetic coordination system

            */

            private val CREATE_VIEW_TOTAL_NUMBER_VAUXES =
                "CREATE VIEW '${TotalNumberOfVauxes.VIEW_NAME}' AS SELECT IFNULL(COUNT(0), 0) AS '${TotalNumberOfVauxes._ID}' FROM vauxes_serial_numbers_for_locations;"

            private val CREATE_VIEW_NUMBER_OF_VAUXES_IN_LOCATIONS =
                "CREATE VIEW '${TotalVauxesForLocation.VIEW_NAME}' AS SELECT ${Vaux.COLUMN_NAME_PLUS_CODE} AS '${TotalVauxesForLocation._ID}', " +
                    "COUNT(${Vaux.TABLE_NAME}.${Vaux._ID}) AS '${TotalVauxesForLocation.COLUMN_NAME_NUM_VAUXES}' FROM ${Vaux.TABLE_NAME} " +
                    "GROUP BY ${Vaux.TABLE_NAME}.${Vaux.COLUMN_NAME_PLUS_CODE};"

        }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_VAUXES_SERIAL_NUMBERS_FOR_LOCATIONS)
        db?.execSQL(CREATE_TABLE_Y_AXIS_HEIGHT_FOR_VAUX_PLATEAUS)
        db?.execSQL(CREATE_TABLE_VAUXES_PALLETS_COORDINATES)
        db?.execSQL(CREATE_VIEW_TOTAL_NUMBER_VAUXES)
        db?.execSQL(CREATE_VIEW_NUMBER_OF_VAUXES_IN_LOCATIONS)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db?.execSQL(ENFORCE_STRICT_TYPES)
        db?.setForeignKeyConstraintsEnabled(true)
        super.onConfigure(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}