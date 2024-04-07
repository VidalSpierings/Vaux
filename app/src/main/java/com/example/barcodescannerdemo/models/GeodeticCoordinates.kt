package com.example.barcodescannerdemo.models

import android.database.Cursor

abstract class GeodeticCoordinates {

    abstract fun getLatitude(cursor: Cursor): String

    abstract fun getLongitude(cursor: Cursor): String

}