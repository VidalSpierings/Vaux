package com.example.barcodescannerdemo.composable_blueprints

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface GeodeticCoordinateIndicators: IndicationBox, StandardText {

    @Composable
    fun GeodeticCoordinateIndicatorsComposable(
        latitudeContent: @Composable () -> Unit,
        longitudeContent: @Composable () -> Unit, ){

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Column{

                latitudeContent()

            }

            Column{

                longitudeContent()

            }

        }

    }

    /*

    - Box that shows the info for either a latitude or a longitude.
      Could be a TextField (EditText) or a Text (TextView)

    */

}