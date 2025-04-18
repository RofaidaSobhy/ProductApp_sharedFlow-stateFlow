package com.example.designpatternskotlin.favProducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.designpatternskotlin.allProducts.AllProductsFactory
import com.example.designpatternskotlin.allProducts.AllProductsViewModel
import com.example.designpatternskotlin.data.local.ProductDatabase
import com.example.designpatternskotlin.data.local.ProductLocalDataSourceImpl
import com.example.designpatternskotlin.data.models.Product
import com.example.designpatternskotlin.data.models.Response
import com.example.designpatternskotlin.data.remote.ProductsRemoteDataSourceImpl
import com.example.designpatternskotlin.data.remote.RetrofitHelper
import com.example.designpatternskotlin.data.repo.ProductRepositoryImpl
import com.example.designpatternskotlin.favProducts.ui.theme.DesignPatternsKotlinTheme
import kotlinx.coroutines.flow.filter

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
    val productsState by viewModel.products.collectAsStateWithLifecycle()
    val snackBarHostState = remember{SnackbarHostState()}

    LaunchedEffect(Unit) {
        viewModel.message
            .filter { message -> message.isNotBlank() }
            .collect{
                    message ->
                snackBarHostState.showSnackbar(
                    message = message
                    , duration = SnackbarDuration.Short
                )
            }
    }

    when(productsState) {
        is Response.Loading -> {
            LoadingIndicator()
        }

        is Response.Success -> {
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
                        items((productsState as Response.Success).data?.size ?: 0){
                                it : Int ->

                            ProductRow((productsState as Response.Success).data?.get(it)
                                ,  "Remove"
                                , {
                                    viewModel.removeFromFavorites((productsState as Response.Success).data?.get(it))

                                }
                            )


                        }
                    }

                }

            }

        }
        is Response.Failure -> {
            Text(
                text = "Sorry, we can't show the products now",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                fontSize = 22.sp


            )
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

@Preview
@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ){
        CircularProgressIndicator()
    }

}