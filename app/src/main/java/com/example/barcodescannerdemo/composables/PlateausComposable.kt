package com.example.barcodescannerdemo.composables

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
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
import androidx.compose.material.icons.outlined.Square
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
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.models.Pallet
import com.example.barcodescannerdemo.models.Plateaus
import com.example.barcodescannerdemo.views.AddPlateauView
import com.example.barcodescannerdemo.views.EditPalletView
import com.example.barcodescannerdemo.views.EditPlateauView

class PlateausComposable: BottomButtons, IndicationBox, StandardText {

    @Composable
    fun Composable(){

        var currentlySelectedVaux by rememberSaveable{
            mutableStateOf("unspecified")
        }

        var plateauSerialNumberTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var currentPlateau by rememberSaveable{
            mutableStateOf("unspecified")
        }

        var currentHeight by rememberSaveable{
            mutableStateOf("unspecified")
        }

        // state-managed variables for all dynamic information within this screen

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current
        val prefixValue = "PLU"

        val activity = context.findActivity()

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Text("Current: $currentPlateau")

            TextField(
                modifier = Modifier
                    .padding(top = 35.dp),
                value = plateauSerialNumberTextFieldText,
                onValueChange = { it: String ->

                    plateauSerialNumberTextFieldText = it

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),

                keyboardActions = KeyboardActions(
                    onSearch = {

                        try {

                            val plateauData = Plateaus(context, "PLU$plateauSerialNumberTextFieldText").getPlateaus()

                            currentPlateau = plateauData.plateauSerialNumber
                            currentlySelectedVaux = plateauData.vauxSerialNumber ?: "unspecified"
                            currentHeight = plateauData.height ?: "unspecified"

                            plateauSerialNumberTextFieldText = ""
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

                    Text("Plateau serial number")

                },
                leadingIcon = {

                    Icon(
                        painter = rememberVectorPainter(image = Icons.Outlined.BorderAll),
                        contentDescription = "Plateau"
                    )

                },

                prefix = { Text(text = prefixValue) }
            )

            Text("This plateau belongs to the Vaux serial number: ")

            IndicationBoxComposable(
                content = {StandardTextComposable(text = currentlySelectedVaux)}
            )

            Text("The current height of this plateau (in meters) is: ")

            IndicationBoxComposable(
                content = {StandardTextComposable(text = currentHeight)}
            )

            /*

            BottomButtonsComposable(
                context = context,
                editIntent = Intent(context, EditPlateauView::class.java).apply {
                    putExtra("plateau_serial_number", currentPlateau)
                    putExtra("vaux_serial_number", currentlySelectedVaux)
                    putExtra("height", currentHeight)
                },
                addIntent = Intent(context, AddPlateauView::class.java),
                isSelected = (currentPlateau != "unspecified" && currentlySelectedVaux != "unspecified")
            ) { deleteOnClick(activity, context, currentPlateau, prefixValue) }

            */
        }

    }

    private fun deleteOnClick(activity: Activity?, context: Context, plateauSerialNumber: String, prefixValue: String){

        if (plateauSerialNumber != "unspecified") {

            try {

                Plateaus(context, plateauSerialNumber).deleteEntry()

                Toast.makeText(context, "successfully deleted plateau", Toast.LENGTH_SHORT).show()

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

}