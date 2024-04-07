package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.barcodescannerdemo.MyCaptureActivity
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateIndicators
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateText
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.database.VauxDataContract
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.models.Pallet
import com.example.barcodescannerdemo.models.Vaux
import com.example.barcodescannerdemo.views.AddVauxView
import com.example.barcodescannerdemo.views.EditVauxView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class VauxComposable:
    BottomButtons,
    IndicationBox,
    GeodeticCoordinateIndicators,
    StandardText,
    GeodeticCoordinateText{

    @Composable
    fun Composable() {

        var plusCodeText by rememberSaveable {
            mutableStateOf("")
        }

        var vauxSerialNumberText by rememberSaveable {
            mutableStateOf("")
        }

        var latitudeText by rememberSaveable {
            mutableStateOf("")
        }

        var longitudeText by rememberSaveable {
            mutableStateOf("")
        }

        var numPlateausText by rememberSaveable {
            mutableStateOf("")
        }

        var isAttached by rememberSaveable {
            mutableStateOf(false)
        }

        var vauxSerialNumberTextFieldText by rememberSaveable {
            mutableStateOf("")
        }

        // state-managed variables for all dynamic information within this screen

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current
        val prefixValue = "VX"

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

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically){

                TextField(

                    value = vauxSerialNumberTextFieldText,
                    onValueChange = { it: String ->

                        vauxSerialNumberTextFieldText = it

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),

                    keyboardActions = KeyboardActions(
                        onSearch = {

                            try {

                                val vauxData = Vaux(context,
                                    vauxSerialNumber = prefixValue + vauxSerialNumberTextFieldText,
                                ).getVauxes(cursor)

                                plusCodeText = vauxData.plusCode
                                vauxSerialNumberText = vauxData.vauxSerialNumber
                                latitudeText = vauxData.latitude
                                longitudeText = vauxData.longitude
                                numPlateausText = vauxData.numPlateaus
                                isAttached = vauxData.isAttached

                                focusManager.clearFocus()
                                keyboard?.hide()

                            } catch (e: Exception) {

                                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                                builder.setTitle("Error")

                                builder.setMessage(e.message)
                                builder.setPositiveButton("Ok") { dialog, _ ->
                                    run {
                                        dialog.dismiss()
                                    }
                                }.show()
                            }

                        }),

                    label = {

                        Text("Vaux serial number")

                    },
                    leadingIcon = {

                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.BorderAll),
                            contentDescription = "Vaux"
                        )

                    },

                    prefix = {Text(text = prefixValue)}
                )

                Button(onClick = {

                }
                ){
                    Text("Scan")
                }

                //TODO: INDIEN TIJD OVER, GOOGLE BARCODESCANNER IMPLEMENTEREN

            }

            Text("Vaux")

            IndicationBoxComposable(content = {StandardTextComposable(text = vauxSerialNumberText)})

            Text("Plus Code")

            IndicationBoxComposable(content = {StandardTextComposable(text = plusCodeText)})

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
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly){

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround){

                    Text("number of plateaus", modifier = Modifier.padding(bottom = 10.dp))

                    IndicationBoxComposable(
                        content = {
                            StandardTextComposable(
                            text = numPlateausText,
                            fontSize = 20
                        )},
                        50,
                        50,
                        50
                    )

                }

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround){

                    Text("Safety attached", modifier = Modifier.padding(bottom = 10.dp))

                    Switch(checked = isAttached, enabled = false, onCheckedChange = {})

                }

            }

            BottomButtonsComposable(
                context = context,
                editIntent = Intent(context, EditVauxView::class.java).apply {
                    putExtra("plus_code", plusCodeText)
                    putExtra("vaux_serial_number", vauxSerialNumberText)
                    putExtra("latitude", latitudeText)
                    putExtra("longitude", longitudeText)
                    putExtra("num_plateaus", numPlateausText)
                    putExtra("is_attached", isAttached)
                },
                addIntent = Intent(context, AddVauxView::class.java),
                isSelected = (plusCodeText != ""
                        && vauxSerialNumberText != ""
                        && latitudeText != ""
                        && longitudeText != ""
                        && numPlateausText != ""
                        )
            ) {

                deleteOnClick(activity, context, vauxSerialNumberText)

                // the block for the delete functionality

            }

        }

    }

    private fun deleteOnClick(activity: Activity?, context: Context, vauxSerialNumbers: String){

        if (vauxSerialNumbers != "") {

            try {

                Vaux(context, vauxSerialNumber = vauxSerialNumbers).deleteEntry()

                Toast.makeText(context, "successfully deleted pallet", Toast.LENGTH_SHORT).show()

                activity?.finish()

            } catch (e: Exception){

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Error")

                builder.setMessage(e.message)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }.show()

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