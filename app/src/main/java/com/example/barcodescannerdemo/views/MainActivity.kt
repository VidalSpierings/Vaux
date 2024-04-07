package com.example.barcodescannerdemo.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.barcodescannerdemo.ComposableTestClass
import com.example.barcodescannerdemo.composables.EditPlateauComposable
import com.example.barcodescannerdemo.miscellaneous.MenuItem
import com.example.barcodescannerdemo.MyCaptureActivity
import com.example.barcodescannerdemo.composables.MenuItemsComposable
import com.example.barcodescannerdemo.database.VauxDataDBHelper
import com.example.barcodescannerdemo.ui.theme.AppTheme

class MainActivity : ComponentActivity(), ComposableTestClass {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VauxDataDBHelper(baseContext).readableDatabase

        setContent{

            AppTheme {

                MenuItemsComposable().Composable(isAdmin = true)
                
            }

        }

    }

@Composable fun BaseContainer(isAdmin: Boolean){

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)) {}

    MenuItemsContainer(isAdmin = isAdmin)

}

@Composable
fun MenuItemsContainer(isAdmin: Boolean){
    // (Please note that it is a convention to start Composable Object functions with a capital letter)

    val context = LocalContext.current

    val menuOptionsList = listOf(
        MenuItem("Total number of Vauxes", Intent(context, MyCaptureActivity::class.java)),
        MenuItem("Total number of Vauxes for location", Intent(context, MyCaptureActivity::class.java)),
        MenuItem("Plateaus", Intent(context, MyCaptureActivity::class.java)),
        MenuItem("Pallets", Intent(context, MyCaptureActivity::class.java)),
        MenuItem("Vauxes", Intent(context, MyCaptureActivity::class.java))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)) {

        items(if (isAdmin) 5 else 4){item ->

            // if the user is an admin, show additional admin option, if not, show only the regular options

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .background(Color.White)
                    .clickable {
                        Toast
                            .makeText(context, menuOptionsList[item].itemName, Toast.LENGTH_LONG)
                            .show()
                    }
                ){

                Text(
                    modifier = Modifier.padding(5.dp),
                    text = menuOptionsList[item].itemName,
                    color = Color.Gray,
                    fontSize = 17.5.sp)
            }

        }

    }

}

@Composable
fun BaseContainerAmounts(amount: Int, descriptionText: String = "the total number of Vauxes is:"){
    // (Please note that it is a convention to start Composable Object functions with a capital letter)
    // the second parameter is optional. If the chosen to not submit a second parameter, the standard value in between the "" will be used

    var textFieldText by remember{
        mutableStateOf("")
    }

    // use a state-management oriented variable to manage the correct functioning of the TextField. The TextField starts with empty text

    Box(modifier = Modifier.fillMaxSize()){

        if (!descriptionText.equals("the total number of Vauxes is:")) {

            OutlinedTextField(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 35.dp),
                value = textFieldText,
                onValueChange = {it: String -> textFieldText = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
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

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally){

            Text(
                text = descriptionText,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            IndicationBox(text = amount.toString())

        }

    }

}

@Composable
fun BaseContainerPlateaus(height: String = "unspecified"){

    var currentlySelectedVaux by remember{
        mutableStateOf("unspecified")
    }

    var plateauSerialNumberTextFieldText by remember{
        mutableStateOf("")
    }

    var currentPlateau by remember{
        mutableStateOf("unspecified")
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround){

        Text("Current: $currentPlateau")

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 35.dp),
            value = plateauSerialNumberTextFieldText,
            onValueChange = { it: String ->

                plateauSerialNumberTextFieldText = it

                            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = {

                Text("Plateau serial number")

            },
            leadingIcon = {

                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.Square),
                    contentDescription = "Plateau"
                )

            },

            prefix = {Text(text = "PLU")}
        )

        Text("This plateau belongs to the Vaux serial number: ")

        IndicationBox(text = currentlySelectedVaux)

        Text("The current height of this plateau is: ")
        
        IndicationBox(text = height)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Button(

                onClick = { /*TODO*/ },) {

                Text("Edit")

            }

            Button(

                onClick = { /*TODO*/ },) {

                Text("Add new")

            }

            Button(

                onClick = { /*TODO*/ },) {

                Text("Delete")

            }

        }

    }

}

@Composable
fun BaseContainerPallets(){

    var currentlySelectedVaux by remember{
        mutableStateOf("unspecified")
    }

    var palletSerialNumberTextFieldText by remember{
        mutableStateOf("")
    }

    var currentPallet by remember{
        mutableStateOf("unspecified")
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround){

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 35.dp),
            value = palletSerialNumberTextFieldText,
            onValueChange = { it: String ->

                palletSerialNumberTextFieldText = it

            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            label = {

                Text("Pallet serial number")

            },
            leadingIcon = {

                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.GridOn),
                    contentDescription = "Pallet"
                )

            },

            prefix = {Text(text = "WDN")}
        )

        Text("Pallet serial number")

        IndicationBox(text = currentPallet)

        Text("Vaux serial number")

        IndicationBox(text = currentlySelectedVaux)

        CoordinateIndicators("35.33333", "22.22222")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Button(

                onClick = { /*TODO*/ },) {

                Text("Edit")

            }

            Button(

                onClick = { /*TODO*/ },) {

                Text("Add new")

            }

            Button(

                onClick = { /*TODO*/ },) {

                Text("Delete")

            }

        }

    }

}

@Composable
fun IndicationBox(text: String, maxWidth: Int = 1000, minWidth: Int = 300, height: Int = 150, fontSize: Int = 40){

    Box(
        modifier = Modifier
            .background(Color.Gray)
            .height(height.dp)
            .widthIn(maxWidth.dp, minWidth.dp),
        contentAlignment = Alignment.Center
    ){

        Text(
            text = text,
            fontSize = fontSize.sp
        )

    }

}

@Composable
fun CoordinateIndicators(latitudeString: String, longitude: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column() {
            Text("Latitude")
            IndicationBox(text = latitudeString, 400, 125, 75, 20)
        }

        Column {

            Column {
                Text("Longitude")
                IndicationBox(text = longitude, 400, 125, 75, 20)
            }

        }

    }

}
}