package com.example.barcodescannerdemo.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.barcodescannerdemo.composable_blueprints.BottomButtons
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardTextField

class EditPlateauComposable: BottomButtons, IndicationBox, StandardTextField {

    @Composable
    fun Composable(currentPlateau: String,
                   currentPlateauHeight: String = "",
                   currentVaux: String = ""){


        var heightTextFieldText by rememberSaveable{
            mutableStateOf(currentPlateauHeight)
        }

        var vauxSerialNumberTextFieldText by rememberSaveable{
            mutableStateOf(currentVaux)
        }

        // state-managed variables for all dynamic information within this screen

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround){

            Text("Current: $currentPlateau")

            Text("This plateau belongs to the Vaux serial number: ")

            IndicationBoxComposable(
                content = {
                    // Komt hierboven een error?
                    // Gelieve IndicationBox te verwijderen als implementerende interface ->
                    // clean project ->
                    // opnieuw toevoegen als implementerende interface ->
                    // clean project. Dit zou het probleem moeten verhelpen
                    StandardTextFieldComposable(
                        label = "Vaux Serial Number",
                        onTextFieldChange = {vauxSerialNumberTextFieldText = it},
                        text = vauxSerialNumberTextFieldText,
                    )
                }

            )

            Text("The current height of this plateau is: ")

            IndicationBoxComposable(
                content = {
                    // Komt hierboven een error?
                    // Gelieve IndicationBox te verwijderen als implementerende interface ->
                    // clean project ->
                    // opnieuw toevoegen als implementerende interface ->
                    // clean project. Dit zou het probleem moeten verhelpen
                    StandardTextFieldComposable(
                        label = "Plateau height",
                        onTextFieldChange = {heightTextFieldText = it},
                        text = heightTextFieldText,
                    )
                }

            )

            ConfirmButtonComposable({})

        }

    }

}