package com.example.designpatternskotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.designpatternskotlin.allProducts.AllProductsActivity
import com.example.designpatternskotlin.allProducts.AllProductsFactory
import com.example.designpatternskotlin.favProducts.FavProductsActivity
import com.example.designpatternskotlin.ui.theme.DesignPatternsKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}

@Preview
@Composable
private fun HomeScreen() {
    val context = LocalContext.current
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ){
        Button(
            {
                val intent =Intent(context,AllProductsActivity::class.java)
                context.startActivity(intent)
            }
        ){
          Text("Show Products")
        }

        Button(
            {
                val intent =Intent(context, FavProductsActivity::class.java)
                context.startActivity(intent)
            }
        ){
            Text("Favorites")
        }
    }
}

