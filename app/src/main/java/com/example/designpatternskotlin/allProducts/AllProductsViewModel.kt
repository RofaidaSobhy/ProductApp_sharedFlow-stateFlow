package com.example.designpatternskotlin.allProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

class AllProductsViewModel(private val repo : ProductRepository) : ViewModel() {
    private val mutableProducts = MutableStateFlow<Response>(Response.Loading)
    val products = mutableProducts.asStateFlow()

    private val mutableMessage= MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun getProducts(){
        viewModelScope.launch (Dispatchers.IO) {
            try {

                val result = repo.getAllProducts(true)
                if (result != null) {
                    //val products : List<Product> = result
                    result
                        .catch { ex ->
                            mutableProducts.value = Response.Failure(ex)
                            mutableMessage.emit("Error From API: ${ex.message}")
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

    fun addToFavorites(product: Product?){
        viewModelScope.launch (Dispatchers.IO) {
            if (product != null) {

                try {
                    val result = repo.addProduct(product)
                    if (result > 0) {
                        mutableMessage.emit("Added Successfully!")
                    } else {
                        mutableMessage.emit("Product is Already in Favorites!")
                    }
                } catch (ex: Exception) {
                    mutableMessage.emit("Couldn't Add Product, ${ex.message}")
                }

            } else {
                mutableMessage.emit("Couldn't Add product, Missing data")
            }
        }
    }

}

class AllProductsFactory(private val repo: ProductRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return AllProductsViewModel(repo) as T
    }
}