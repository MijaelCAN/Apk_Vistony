package com.vistony.app.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.CodigoRequest
import com.vistony.app.Entidad.ProductoResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTViewModel @Inject constructor(): ViewModel() {
    private val otService = RetrofitInstance.otService

    var _otState by mutableStateOf(ProductoResponseState())
        private set

    fun getCodigoBarra(codigo:String){
        viewModelScope.launch {
            try {
                val response = otService.getCodigoBarra(CodigoRequest(codigo))
                if(response.statusCode==200){
                    _otState = ProductoResponseState(state = true, productoResponse = response)
                }else{
                    _otState = ProductoResponseState(state = false)
                }

            }catch (e:Exception){
                _otState = ProductoResponseState(state = false)
            }
        }
    }

}

data class ProductoResponseState(
    val state: Boolean = false,
    val productoResponse: ProductoResponse = ProductoResponse(),
    val message: String? = null
)