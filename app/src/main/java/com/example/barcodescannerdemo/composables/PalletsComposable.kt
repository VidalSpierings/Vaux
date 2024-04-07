package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateIndicators
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateText
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.models.Pallet
import com.example.barcodescannerdemo.views.AddPalletView
import com.example.barcodescannerdemo.views.EditPalletView

class PalletsComposable:
    BottomButtons,
    IndicationBox,
    GeodeticCoordinateIndicators,
    StandardText,
    GeodeticCoordinateText{

    @Composable
    fun Composable(){

        var vauxText by rememberSaveable{
            mutableStateOf("unspecified")
        }

        var palletSerialNumberTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var palletText by rememberSaveable{
            mutableStateOf("unspecified")
        }

        var latitudeText by rememberSaveable{
            mutableStateOf("")
        }

        var longitudeText by rememberSaveable{
            mutableStateOf("")
        }

        // state-managed variables for all dynamic information within this screen

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current
        val prefixValue = "WDN"

        val activity = context.findActivity()

        val cursor: Cursor = VauxDataDBHelper(context)
            .readableDatabase.rawQuery(
                "SELECT * FROM ${VauxDataContract.TotalNumberOfVauxes.VIEW_NAME};", null
            )
        cursor.close()

        // prepare unused cursor

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            // Column that contains all content shown on the screen

            TextField(
                modifier = Modifier
                    .padding(top = 35.dp),
                value = palletSerialNumberTextFieldText,
                onValueChange = { it: String ->

                    palletSerialNumberTextFieldText = it

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),

                keyboardActions = KeyboardActions(
                    onSearch = {

                        try {

                            val palletData = Pallet(context,
                                prefixValue + palletSerialNumberTextFieldText,
                            ).getPallets(cursor)

                            vauxText = palletData.vauxSerialNumber
                            palletText = palletData.palletSerialNumber
                            latitudeText = palletData.latitude
                            longitudeText = palletData.longitude

                            /*

                            * retrieve info for entered pallet serial number when user presses
                              the magnifying glass on the virtual keyboard

                            */

                            palletSerialNumberTextFieldText = ""
                            focusManager.clearFocus()
                            keyboard?.hide()

                            // delete entered text, hide keyboard and remove focus when data is retrieved

                        } catch (e: Exception) {

                            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                            builder.setTitle("Error")

                            builder.setMessage(e.message)
                            builder.setPositiveButton("Ok") { dialog, _ ->
                                run {
                                    dialog.dismiss()
                                }
                            }.show()

                            // show alert dialog to user when something goes wrong

                        }

                }),

                label = {

                    Text("Pallet serial number")

                },
                leadingIcon = {

                    Icon(
                        painter = rememberVectorPainter(image = Icons.Outlined.GridOn),
                        contentDescription = "Pallet"
                    )

                },

                prefix = { Text(text = prefixValue) }
            )

            // TextField (EditText) for searching pallets

            Text("Pallet serial number")

            IndicationBoxComposable(
                content = {StandardTextComposable(text = palletText)}
            )

            Text("Vaux serial number")

            IndicationBoxComposable(
                content = {StandardTextComposable(text = vauxText)}
            )

            GeodeticCoordinateIndicatorsComposable(
                latitudeContent = {
                    GeodeticCoordinateTextComposable(
                    coordinateTypeString = "Latitude",
                    text = latitudeText
                )},
                longitudeContent = {
                    GeodeticCoordinateTextComposable(
                    coordinateTypeString = "Latitude",
                    text = longitudeText
                    )
                }
            )

            // show info for currently selected pallet

            BottomButtonsComposable(
                context = context,
                editIntent = Intent(context, EditPalletView::class.java).apply {
                    putExtra("pallet_serial_number", palletText)
                    putExtra("vaux_serial_number", vauxText)
                    putExtra("latitude", latitudeText)
                    putExtra("longitude", longitudeText)
                },
                addIntent = Intent(context, AddPalletView::class.java),
                isSelected = (palletText != "unspecified" && vauxText != "unspecified"),
                deleteOnClick = {deleteOnClick(activity, context, palletText, prefixValue)}
                //No 'equals()' required in Kotlin
            )

            // buttons to edit and delete current pallet, or add a completely new one

        }

    }

    private fun deleteOnClick(activity: Activity?, context: Context, palletSerialNumber: String, prefixValue: String){

        if (palletSerialNumber != "unspecified") {

            try {

                Pallet(context, palletSerialNumber).deleteEntry()

                Toast.makeText(context, "successfully deleted pallet", Toast.LENGTH_SHORT).show()

                activity?.finish()

                // go back to previous screen when deletion was committed successfully

            } catch (e: Exception){

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Error")

                builder.setMessage(e.message)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }.show()

                // show Alert Dialog when screen deletion was not committed successfully

            }

        } else {

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Error")

            builder.setMessage("Can't delete current selection")
            builder.setPositiveButton("Ok") { dialog, _ ->
                run {
                    dialog.dismiss()
                }
            }.show()

            // inform user that delete action cannot be performed when there is no active selection

        }

    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    /*

    * function to access current context and activity from activity that currently uses
      the Composable within this class

    */

}