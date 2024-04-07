package com.example.barcodescannerdemo.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
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
import com.example.barcodescannerdemo.composable_blueprints.IndicationBox
import com.example.barcodescannerdemo.composable_blueprints.StandardText
import com.example.barcodescannerdemo.models.TotalNumberOfVauxes
import com.example.barcodescannerdemo.models.TotalVauxesForLocation

class AmountsComposable: IndicationBox, StandardText {

    @Composable
    fun Composable(amount: String = "", descriptionText: String = ""){
        // (Please note that it is a convention to start Composable Object functions with a capital letter)
        // the second parameter is optional. If the chosen to not submit a second parameter, the standard value in between the "" will be used

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val keyboard = LocalSoftwareKeyboardController.current

        var textFieldText by rememberSaveable{
            mutableStateOf("")
        }

        var displayedAmount by rememberSaveable {
            mutableStateOf(amount)
        }

        // state-managed variables for all dynamic information within this screen

        Box(modifier = Modifier.fillMaxSize()){

            if (descriptionText.equals("The total number of Vauxes for this location is:")) {

                TextField(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 35.dp),
                    value = textFieldText,
                    onValueChange = {it: String -> textFieldText = it},
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),

                    keyboardActions = KeyboardActions(
                        onSearch = {
                            displayedAmount = TotalVauxesForLocation(context, textFieldText).getTotallVauxesForLocation().toString()
                            textFieldText = ""
                            focusManager.clearFocus()
                            keyboard?.hide()
                        }
                    ),

                        label = {
                        Text("Plus Code")
                    },
                    leadingIcon = {

                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Place),
                            contentDescription = "Location"
                        )

                    }

                )

            }

            // show search field if user selected the 'amounts for location' screen.

            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally){

                Text(
                    text = descriptionText,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                IndicationBoxComposable(
                    content = {StandardTextComposable(text = displayedAmount)}
                )


            }

        }

    }

}