package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.database.SQLException
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateIndicators
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateTextField
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardTextField
import com.example.barcodescannerdemo.models.Vaux

class AddOrEditVauxComposable:
    BottomButtons,
    GeodeticCoordinateIndicators,
    StandardTextField,
    GeodeticCoordinateTextField,
    IndicationBox
{

    @Composable
    fun Composable(currentPlusCode: String = "",
                   currentVaux: String = "",
                   currentLatitude: String = "",
                   currentLongitude: String = "",
                   currentNumPlateaus: String = "",
                   currentIsAttached: Boolean = false){

        val context = LocalContext.current
        val activity = context.findActivity()
        val intent = activity?.intent

        val retrievedPlusCode = intent?.getStringExtra("plus_code") ?: ""
        val retrievedLatitude = intent?.getStringExtra("latitude") ?: ""
        val retrievedLongitude = intent?.getStringExtra("longitude") ?: ""
        val retrievedNumPlateaus = intent?.getStringExtra("num_plateaus") ?: ""
        val retrievedIsAttached = intent?.getBooleanExtra("is_attached", false) ?: false

        var inEditMode = false

        /*

        - boolean value that checks whether or not the user selected the 'Edit' button
          on the previous screen

        */

        if (
            retrievedPlusCode != ""
            && retrievedLatitude != ""
            && retrievedLongitude != ""
            && retrievedNumPlateaus != "") {

            inEditMode = true

        }

        var vauxSerialNumberTextFieldOrTextText by rememberSaveable{
            mutableStateOf(intent?.getStringExtra("vaux_serial_number") ?: "")
        }

        var latitudeTextFieldText by rememberSaveable{
            mutableStateOf(retrievedLatitude)
        }

        var longitudeTextFieldText by rememberSaveable{
            mutableStateOf(retrievedLongitude)
        }

        var numberOfPlateausTextFieldText by rememberSaveable{
            mutableStateOf(retrievedNumPlateaus)
        }

        var plusCodeTextFieldText by rememberSaveable{
            mutableStateOf(retrievedPlusCode)
        }

        var isAttachedStatus by rememberSaveable {
            mutableStateOf(retrievedIsAttached)
        }

        // state-managed variables for all dynamic information within this screen

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){

                Button(onClick = {}){
                    Text("Scan")
                }

            }


            if (!inEditMode) {

                /*

                  - code to run when the user did not click the 'Edit' button in the previous screen
                    (and clicked the 'Add new' button instead)

                */

                Text("Vaux")

                TextField(

                    value = vauxSerialNumberTextFieldOrTextText,
                    onValueChange = { it: String ->

                        vauxSerialNumberTextFieldOrTextText = it

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),

                    label = {

                        Text("Vaux serial number")

                    },
                    leadingIcon = {

                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.BorderAll),
                            contentDescription = "Vaux"
                        )

                    },

                    prefix = {Text(text = "VX")}
                )
            } else {

                /*

                  - code to run when the user clicked the 'Edit' button in the previous screen

                */

                Text("current Vaux: $vauxSerialNumberTextFieldOrTextText")

            }

            Text("Plus Code")

            IndicationBoxComposable(
                content = {
                    // Komt hierboven een error?
                    // Gelieve IndicationBox te verwijderen als implementerende interface ->
                    // clean project ->
                    // opnieuw toevoegen als implementerende interface ->
                    // clean project. Dit zou het probleem moeten verhelpen
                    StandardTextFieldComposable(
                        label = "Plus Code",
                        onTextFieldChange = {plusCodeTextFieldText = it},
                        text = plusCodeTextFieldText,
                    )

                }

            )

            GeodeticCoordinateIndicatorsComposable(
                latitudeContent = {

                    GeodeticCoordinateTextFieldComposable(
                        coordinateTypeString = "Latitude",
                        text = latitudeTextFieldText)
                    {
                        latitudeTextFieldText = it
                    }

                }) {

                GeodeticCoordinateTextFieldComposable(
                    coordinateTypeString = "Longitude",
                    text = longitudeTextFieldText)
                {
                    longitudeTextFieldText = it
                }

                // TextFields (EditTexts) for editing latitude and longitude

            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly){

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround){

                    Text("number of plateaus", modifier = Modifier.padding(bottom = 10.dp))

                    IndicationBoxComposable(
                        content = {
                            // Komt hierboven een error?
                            // Gelieve IndicationBox te verwijderen als implementerende interface ->
                            // clean project ->
                            // opnieuw toevoegen als implementerende interface ->
                            // clean project. Dit zou het probleem moeten verhelpen

                            TextField(

                                value = numberOfPlateausTextFieldText,
                                onValueChange = { it: String ->

                                    numberOfPlateausTextFieldText = it

                                },

                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                ),

                                label = { Text("Number") },

                                )
                                  },
                        75,
                        75,
                        75

                    )

                }

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround){

                    Text("Safety attached", modifier = Modifier.padding(bottom = 10.dp))

                    Switch(checked = isAttachedStatus, onCheckedChange = {isAttachedStatus = it})

                }

            }

            ConfirmButtonComposable {
                onClickConfirmButtonClicked(
                    activity,
                    context,
                    plusCodeTextFieldText,
                    "VX$vauxSerialNumberTextFieldOrTextText",
                    latitudeTextFieldText,
                    longitudeTextFieldText,
                    numberOfPlateausTextFieldText,
                    isAttachedStatus,
                    inEditMode
                )
            }

        }

    }

    private fun onClickConfirmButtonClicked(
        activity: Activity?,
        context: Context,
        plusCode: String,
        vauxSerialNumber: String,
        latitude: String,
        longitude: String,
        numPlateaus: String,
        isAttached: Boolean,
        inEditMode: Boolean){

        if (!inEditMode) {

                /*

                  - code to run when the user did not click the 'Edit' button in the previous screen
                    (and clicked the 'Add new' button instead)

                */

            try {

                Vaux(
                    context,
                    plusCode,
                    vauxSerialNumber,
                    latitude,
                    longitude,
                    numPlateaus,
                    isAttached).addNewVaux()

                // attempt to add pallet to database

                activity?.finish()

            } catch (se: SQLException) {

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Error")

                builder.setMessage(se.message)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }.show()

                // if something went wrong (SQLException), inform user by showing AlertDialog

            }

            catch (e: Exception) {

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage(e.message)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }.show()

                // if something went wrong (Exception), inform user by showing AlertDialog

            }

        } else if (inEditMode) {

            /*

             - code to run when the user clicked the 'Edit' button in the previous screen

            */

            try {

                Vaux(
                    context,
                    plusCode,
                    vauxSerialNumber,
                    latitude,
                    longitude,
                    numPlateaus,
                    isAttached).setAllNewValuesExceptVauxSerialNumber()

                // attempt to add pallet to database

                Toast.makeText(context, "successfully updated pallet", Toast.LENGTH_SHORT).show()

                activity?.finish()

                // success resultcode? -> inform user and go back to previous screen

            }

            catch (e: Exception) {

                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage(e.message)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }.show()

                // if something went wrong (Exception), inform user by showing AlertDialog

            }

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