package com.vistony.app.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor():ViewModel() {
    var turno by mutableStateOf("")
    var ot by mutableStateOf("")
    var description by mutableStateOf("")
    var um by mutableStateOf("")
    var cantidad by mutableStateOf("")
    var linea by mutableStateOf("")
    var operador by mutableStateOf("")
    var fecha by mutableStateOf("")
}