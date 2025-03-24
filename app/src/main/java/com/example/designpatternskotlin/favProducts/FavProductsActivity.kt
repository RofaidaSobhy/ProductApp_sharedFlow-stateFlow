package com.example.designpatternskotlin.favProducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.designpatternskotlin.allProducts.AllProductsFactory
import com.example.designpatternskotlin.allProducts.AllProductsViewModel
import com.example.designpatternskotlin.data.local.ProductDatabase
import com.example.designpatternskotlin.data.local.ProductLocalDataSourceImpl
import com.example.designpatternskotlin.data.models.Product
import com.example.designpatternskotlin.data.remote.ProductsRemoteDataSourceImpl
import com.example.designpatternskotlin.data.remote.RetrofitHelper
import com.example.designpatternskotlin.data.repo.ProductRepositoryImpl
import com.example.designpatternskotlin.favProducts.ui.theme.DesignPatternsKotlinTheme

class FavProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FavProductsScreen(
                ViewModelProvider(this, FavProductsFactory(
                    ProductRepositoryImpl.getInstance(
                        ProductsRemoteDataSourceImpl(RetrofitHelper.apiService)
                        , ProductLocalDataSourceImpl(ProductDatabase.getInstance(this).getProductDao())
                    )
                )
                ).get(FavProductsViewModel::class.java)
            )
        }
    }
}


//@Preview
@Composable
private fun FavProductsScreen(viewModel: FavProductsViewModel) {
    viewModel.getProducts()
    val productsState = viewModel.products.observeAsState()
    val messageState = viewModel.message.observeAsState()
    val snackBarHostState = remember{ SnackbarHostState() }
    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ){ contentPadding ->
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
            , verticalArrangement = Arrangement.Center
        ){
            LazyColumn {
                items(productsState.value?.size ?: 0){
                        it : Int ->

                    ProductRow(productsState.value?.get(it)
                        ,  "Remove"
                        , {
                            viewModel.removeFromFavorites(productsState.value?.get(it))
                           // viewModel.getProducts()
                        }
                    )


                }
            }
            LaunchedEffect(messageState.value) {
                if(!messageState.value.isNullOrBlank()){
                    snackBarHostState.showSnackbar(
                        message = messageState.value.toString()
                        , duration = SnackbarDuration.Short
                    )
                }
            }
        }

    }
}

//@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProductRow(product: Product?, actionName:String, action: () -> Unit ) {
    Row (modifier = Modifier
        .border(BorderStroke(width=2.dp, color = Color.Gray))
        .fillMaxWidth()

    ){
        if(product!=null){
            GlideImage(
                model = product.thumbnail,
                contentDescription = "image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

            Column (
                horizontalAlignment = Alignment.CenterHorizontally
                , modifier = Modifier.fillMaxSize()
                    .align(alignment = Alignment.CenterVertically)



            ){

                Text(
                    text=product.title
                    , color= Color.Red, fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Light
                )

                Text(
                    text=product.price.toString(), color= Color.Black, fontSize = 12.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal
                )

                Button(action){
                    Text(actionName)
                }

            }
        }

    }
}
