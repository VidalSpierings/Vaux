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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderAll
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardTextField
import com.example.barcodescannerdemo.models.Pallet
import com.example.barcodescannerdemo.models.Plateaus

class AddPlateauComposable: BottomButtons, IndicationBox, StandardTextField {

    @Composable
    fun Composable(){

        var plateauTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var vauxTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var heightTextFieldText by rememberSaveable{
            mutableStateOf("")
        }

        // state-managed variables for all dynamic information within this screen

        val context = LocalContext.current
        val activity = context.findActivity()

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Text("Plateau: ")

            IndicationBoxComposable(
                content = {
                    TextField(
                        modifier = Modifier
                            .padding(top = 35.dp),
                        value = plateauTextFieldText,
                        onValueChange = { it: String ->

                            plateauTextFieldText = it

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),

                        label = {

                            Text("Plateau serial number")

                        },
                        leadingIcon = {

                            Icon(
                                painter = rememberVectorPainter(image = Icons.Outlined.BorderAll),
                                contentDescription = "Plateau"
                            )

                        },

                        prefix = { Text(text = "PLU") }
                    )
                }

            )

            Text("Vaux: ")

            IndicationBoxComposable(
                content = {
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

            Text("Plateau height: ")

            IndicationBoxComposable(
                content = {
                    TextField(

                        value = heightTextFieldText,
                        onValueChange = { it: String ->

                            heightTextFieldText = it

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        label = {

                            Text("Height")

                        },
                        leadingIcon = {

                            Icon(
                                painter = rememberVectorPainter(image = Icons.Outlined.Height),
                                contentDescription = "Height"
                            )

                        },

                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.End
                        ),

                        suffix = {Text(text = "m")}
                    )
                }

            )

            ConfirmButtonComposable {

                onConfirmedButtonClicked(
                    activity,
                    context,
                    plateauTextFieldText,
                    vauxTextFieldText,
                    heightTextFieldText)

            }

        }

    }

    private fun onConfirmedButtonClicked(
        activity: Activity?,
        context: Context,
        plateauSerialNumber: String,
        vauxSerialNumber: String,
        height: String){

        var result = 0L

        try {
            result = Plateaus(
                context,
                "PLU$plateauSerialNumber",
                "VX$vauxSerialNumber",
                height
            ).addNewPlateau()

        } catch (se: SQLException) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Error")

            builder.setMessage(se.message)
            builder.setPositiveButton("Ok") { dialog, _ ->
                run {
                    dialog.dismiss()
                }
            }.show()
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
        }

        println("$result")

        if (result >= 0L) {

            Toast.makeText(context, "added plateau", Toast.LENGTH_LONG).show()

            activity?.finish()

        } else if (result == -1L) {
            Toast.makeText(context, "failed to add plateau", Toast.LENGTH_LONG).show()
        }

    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

}