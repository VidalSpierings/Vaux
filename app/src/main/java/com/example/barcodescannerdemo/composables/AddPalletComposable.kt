package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.database.SQLException
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material3.Icon
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
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateIndicators
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateTextField
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.models.Pallet

class AddPalletComposable:
    BottomButtons,
    IndicationBox,
    StandardText,
    GeodeticCoordinateTextField,
    GeodeticCoordinateIndicators{

    @Composable
    fun Composable(){

        val context = LocalContext.current
        val activity = context.findActivity()

        var vauxTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var palletTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var latitudeTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var longitudeTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        // state-managed variables for all dynamic information within this screen

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            // column that contains all content for this screen

            Text("Pallet serial number")

            IndicationBoxComposable(
                // Komt hierboven een error?
                // Gelieve IndicationBox te verwijderen als implementerende interface ->
                // clean project ->
                // opnieuw toevoegen als implementerende interface ->
                // clean project. Dit zou het probleem moeten verhelpen
                content = {

                    IndicationBoxComposable(content = {

                        TextField(

                            value = palletTextFieldText,
                            onValueChange = { it: String ->

                                palletTextFieldText = it

                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            label = {

                                Text("Pallet serial number")

                            },
                            leadingIcon = {

                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.BorderAll),
                                    contentDescription = "Vaux"
                                )

                            },

                            prefix = { Text(text = "WDN") }
                        )

                    }

                        // show box with TextField where pallet serial number can be added

                    )
                }


            )

            Text("Vaux serial number")

            IndicationBoxComposable(
                // Komt hierboven een error?
                // Gelieve IndicationBox te verwijderen als implementerende interface ->
                // clean project ->
                // opnieuw toevoegen als implementerende interface ->
                // clean project. Dit zou het probleem moeten verhelpen
                content = {

                    IndicationBoxComposable(content = {

                        TextField(

                            value = vauxTextFieldText,
                            onValueChange = { it: String ->

                                vauxTextFieldText = it

                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
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

                            prefix = { Text(text = "VX") }
                        )

                    }
                    )
                }

            )

            // show box with TextField where vaux serial number can be added

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

            }

            // show two boxes where, respectively, the latitude and longitude can be added

            ConfirmButtonComposable {
                onClickConfirmButtonClicked(
                    activity,
                    context,
                    palletTextFieldText,
                    vauxTextFieldText,
                    latitudeTextFieldText,
                    longitudeTextFieldText
                )
            }

        }

    }

    private fun onClickConfirmButtonClicked(
        activity: Activity?,
        context: Context,
        palletSerialNumber: String,
        vauxSerialNumber: String,
        latitude: String,
        longitude: String){

        var result = 0L

        try {
            Pallet(
                context,
                "WDN$palletSerialNumber",
                "VX$vauxSerialNumber",
                latitude,
                longitude
            ).addNewPallet()

            // attempt to add pallet to database

            Toast.makeText(context, "added pallet", Toast.LENGTH_LONG).show()

            activity?.finish()

            // success? -> inform user and go back to previous screen

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

            Toast.makeText(context, "failed to add pallet", Toast.LENGTH_LONG).show()

            // non-success resultcode? -> inform user

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

        println("$result")

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