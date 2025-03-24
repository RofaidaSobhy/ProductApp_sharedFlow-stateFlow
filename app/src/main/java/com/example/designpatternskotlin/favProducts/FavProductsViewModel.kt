package com.example.designpatternskotlin.favProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.designpatternskotlin.allProducts.AllProductsViewModel
import com.example.designpatternskotlin.data.models.Product
import com.example.designpatternskotlin.data.repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavProductsViewModel (private val repo : ProductRepository) : ViewModel(){
    private val mutableProducts: MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    val products : LiveData<List<Product>> = mutableProducts

    private val mutableMessage: MutableLiveData<String> = MutableLiveData<String>()
    val message:LiveData<String> = mutableMessage

    fun getProducts(){
        try{
            viewModelScope.launch (Dispatchers.IO){
                val result = repo.getAllProducts(false)
                if(result != null){
                    //val products : List<Product> = result
                    result
                        .collect{
                            mutableProducts.postValue(it)
                        }

                }else{
                    mutableMessage.postValue("Please try again later")
                }
            }
        }catch (ex:Exception){
            mutableMessage.postValue("An error occurred, ${ex.message} ")
        }

    }

    fun removeFromFavorites(product: Product?){
        if(product!=null){
            viewModelScope.launch (Dispatchers.IO){
                try{
                    val result = repo.removeProduct(product)
                    if(result > 0)
                    {
                        mutableMessage.postValue("removed Successfully!")
                       // getProducts()
                    }else{
                        mutableMessage.postValue("Product is not exist in Favorites!")
                    }
                }catch (ex : Exception){
                    mutableMessage.postValue("Couldn't remove Product, ${ex.message}")
                }
            }
        }else{
            mutableMessage.postValue("Couldn't remove product, Missing data")
        }

    }

}

class FavProductsFactory(private val repo: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return FavProductsViewModel(repo) as T
    }
}