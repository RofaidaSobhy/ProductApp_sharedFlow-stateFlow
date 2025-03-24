package com.example.designpatternskotlin.favProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.designpatternskotlin.allProducts.AllProductsViewModel
import com.example.designpatternskotlin.data.models.Product
import com.example.designpatternskotlin.data.models.Response
import com.example.designpatternskotlin.data.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavProductsViewModel (private val repo : ProductRepository) : ViewModel(){
    private val mutableProducts = MutableStateFlow<Response>(Response.Loading)
    val products = mutableProducts.asStateFlow()

    private val mutableMessage= MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun getProducts(){
        viewModelScope.launch (Dispatchers.IO) {

            try {
                val result = repo.getAllProducts(false)
                if (result != null) {
                    //val products : List<Product> = result
                    result
                        .catch { ex ->
                            mutableProducts.value = Response.Failure(ex)
                            mutableMessage.emit("Error From Local: ${ex.message}")
                        }
                        .collect {
                            mutableProducts.value = Response.Success(it)
                        }

                } else {
                    mutableMessage.emit("Please try again later")
                }

            } catch (ex: Exception) {
                mutableProducts.value = Response.Failure(ex)
                mutableMessage.emit("An error occurred, ${ex.message} ")
            }
        }
    }

    fun removeFromFavorites(product: Product?){
        viewModelScope.launch (Dispatchers.IO) {

            if (product != null) {
                try {
                    val result = repo.removeProduct(product)
                    if (result > 0) {
                        mutableMessage.emit("removed Successfully!")
                        // getProducts()
                    } else {
                        mutableMessage.emit("Product is not exist in Favorites!")
                    }
                } catch (ex: Exception) {
                    mutableMessage.emit("Couldn't remove Product, ${ex.message}")
                }

            } else {
                mutableMessage.emit("Couldn't remove product, Missing data")
            }
        }

    }

}

class FavProductsFactory(private val repo: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return FavProductsViewModel(repo) as T
    }
}