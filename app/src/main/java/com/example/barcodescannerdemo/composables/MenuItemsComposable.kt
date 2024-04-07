package com.example.barcodescannerdemo.composables

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barcodescannerdemo.miscellaneous.MenuItem
import com.example.barcodescannerdemo.MyCaptureActivity
import com.example.barcodescannerdemo.views.PalletView
import com.example.barcodescannerdemo.views.PlateausView
import com.example.barcodescannerdemo.views.TotalNumberOfVauxesView
import com.example.barcodescannerdemo.views.TotalVauxesForLocationView
import com.example.barcodescannerdemo.views.VauxView

class MenuItemsComposable {

    @Composable
    fun Composable(isAdmin: Boolean){
        // (Please note that it is a convention to start Composable Object functions with a capital letter)

        val context = LocalContext.current

        val menuOptionsList = listOf(
            MenuItem("Total number of Vauxes", Intent(context, TotalNumberOfVauxesView::class.java)),
            MenuItem("Total number of Vauxes for location", Intent(context, TotalVauxesForLocationView::class.java)),
            MenuItem("Plateaus", Intent(context, PlateausView::class.java)),
            MenuItem("Pallets", Intent(context, PalletView::class.java)),
            MenuItem("Vauxes", Intent(context, VauxView::class.java))
        )

        /*

        // all options that can be chosen by the user
          (admin is the only type of user that can view the 'Total number of Vauxes' screen)

        */

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

                            context.startActivity(menuOptionsList[item].intentToInitiate)

                        }

                    // start appropriate activity when button is pressed

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

}