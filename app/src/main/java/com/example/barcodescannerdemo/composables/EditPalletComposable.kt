package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material.icons.outlined.GridOn
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateIndicators
import com.example.barcodescannerdemo.composable_blueprints.GeodeticCoordinateTextField
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.models.Pallet

class EditPalletComposable:
    BottomButtons,
    IndicationBox,
    StandardText,
    GeodeticCoordinateTextField,
    GeodeticCoordinateIndicators{

    @Composable
    fun Composable(){

        val context = LocalContext.current
        val activity = context.findActivity()
        val intent = activity?.intent

        /*

        - retrieve intent of current activity, and get extra values from previous activity.
          This approach ensures that a complex approach, using a NavigationHost, hasn't to be applied

        */

        var vauxTextFieldText by rememberSaveable{
            mutableStateOf(intent?.getStringExtra("vaux_serial_number") ?: "unspecified")
        }

        var palletTextFieldText by rememberSaveable{
            mutableStateOf(intent?.getStringExtra("pallet_serial_number") ?: "unspecified")
        }

        var latitudeTextFieldText by rememberSaveable{
            mutableStateOf(intent?.getStringExtra("latitude") ?: "")
        }

        var longitudeTextFieldText by rememberSaveable{
            mutableStateOf(intent?.getStringExtra("longitude") ?: "")
        }

        // state-managed variables for all dynamic information within this screen

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Text("Pallet serial number")

            IndicationBoxComposable(content = {

                StandardTextComposable(text = palletTextFieldText)

            }

            )

            /*

                - show pallet serial number as Text (TextView) (not as a TextField (EditText),
                  because pallet serial number cannot be edited)

            */

            Text("Vaux serial number")

            IndicationBoxComposable(content = {

                StandardTextComposable(text = vauxTextFieldText)

            }
            )

            /*

                - show vaux serial number as Text (TextView) (not as a TextField (EditText),
                  because vaux serial number cannot be edited)

            */

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

            /*

            - show latitude and longitude in TextFields (EditTexts) pre-loaded
              with the latitude and longitude from the previous screen.

            */

            ConfirmButtonComposable {
                try {
                    onClickConfirmButton(
                        activity,
                        context,
                        palletTextFieldText,
                        latitudeTextFieldText,
                        longitudeTextFieldText
                    )

                    // attempt to submit edited info to database

                } catch (e: Exception) {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Error")

                    builder.setMessage(e.message)
                    builder.setPositiveButton("Ok") { dialog, _ ->
                        run {
                            dialog.dismiss()
                        }
                    }.show()

                    // if edited info could not be submitted to database, inform user by showing an AlertDialog

                }
            }

        }

    }

    private fun onClickConfirmButton(
        activity: Activity?,
        context: Context,
        palletSerialNumber: String,
        newLatitude: String,
        newLongitude: String){

        var result = 2

        try {

            result = Pallet(context, palletSerialNumber).setNewCoordinates(newLatitude, newLongitude)

            println(result)

            Toast.makeText(context, "updated pallet. Please search again for the pallet to see the updated data", Toast.LENGTH_LONG).show()

            activity?.finish()

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

        /*

        if (result == 0) {

            Toast.makeText(context, "failed to update pallet: Did you enter valid coordinates?", Toast.LENGTH_LONG).show()

        } else if (result == 1) {

        }

        */

    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

}