package com.vistony.app.clean.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.usecases.GetManufacturingOrderUseCase
import com.vistony.app.clean.domain.usecases.RecalculateDensityUseCase
import com.vistony.app.clean.domain.usecases.UpdateApprovalStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManufacturingOrderViewModel @Inject constructor(
    private val getManufacturingOrderUseCase: GetManufacturingOrderUseCase,
    private val recalculateDensityUseCase: RecalculateDensityUseCase,
    private val updateApprovalStatusUseCase: UpdateApprovalStatusUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _manufacturingOrderResponseModel = MutableStateFlow(ManufacturingOrderResponseModel())
    val manufacturingOrderResponseModel: StateFlow<ManufacturingOrderResponseModel> get() = _manufacturingOrderResponseModel

    private val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    // Función para guardar credenciales
    fun saveCredentials(username: String, password: String, remember: Boolean) {
        if (remember) {
            sharedPreferences.edit()
                .putString("saved_username", username)
                .putString("saved_password", password)
                .putBoolean("remember_credentials", true)
                .apply()
        } else {
            clearSavedCredentials()
        }
    }

    // Función para recuperar credenciales guardadas
    fun getSavedCredentials(): Triple<String, String, Boolean> {
        val username = sharedPreferences.getString("saved_username", "") ?: ""
        val password = sharedPreferences.getString("saved_password", "") ?: ""
        val remember = sharedPreferences.getBoolean("remember_credentials", false)
        return Triple(username, password, remember)
    }

    // Función para limpiar credenciales guardadas
    private fun clearSavedCredentials() {
        sharedPreferences.edit()
            .remove("saved_username")
            .remove("saved_password")
            .putBoolean("remember_credentials", false)
            .apply()
    }

    private val _orderCode = MutableStateFlow("")
    val orderCode: StateFlow<String> get() = _orderCode

    private val _isVisibleDialogEditPacking = MutableStateFlow( false)
    val isVisibleDialogEditPacking: StateFlow<Boolean> get() = _isVisibleDialogEditPacking

    private val _statusAprobbation1 = MutableStateFlow( "")
    val statusAprobbation1: StateFlow<String> get() = _statusAprobbation1

    private val _statusAprobbation2 = MutableStateFlow( "")
    val statusAprobbation2: StateFlow<String> get() = _statusAprobbation2

    private val _statusAprobbation3 = MutableStateFlow( "")
    val statusAprobbation3: StateFlow<String> get() = _statusAprobbation3

    private val _statusAprobationHeader1 = MutableStateFlow("Pendiente")
    val statusAprobationHeader1: StateFlow<String> get() = _statusAprobationHeader1

    private val _docNum = MutableStateFlow("")
    val docNum: StateFlow<String> get() = _docNum

    private val _density = MutableStateFlow("")
    val density: StateFlow<String> get() = _density

    private val _lineNum = MutableStateFlow("")
    val lineNum: StateFlow<String> get() = _lineNum

    private val _optimalWeight = MutableStateFlow("")
    val optimalWeight: StateFlow<String> get() = _optimalWeight

    private val _maximunWeight = MutableStateFlow("")
    val maximunWeight: StateFlow<String> get() = _maximunWeight

    private val _isLoadingBody = MutableStateFlow(false)
    val isLoadingBody: StateFlow<Boolean> get() = _isLoadingBody

    private val _isLoadingBodyDetail = MutableStateFlow(false)
    val isLoadingBodyDetail: StateFlow<Boolean> get() = _isLoadingBodyDetail


    fun onOptimalWeightChange(newValue: String) {
        _optimalWeight.value = newValue
    }

    fun onMaximunWeightChange(newValue: String) {
        _maximunWeight.value = newValue
    }

    fun onLineNumChange(newValue: String) {
        _lineNum.value = newValue
    }

    fun onDensityChange(newValue: String) {
        _density.value = newValue
    }

    fun onDocNumChange(newValue: String) {
        _docNum.value = newValue
    }


    fun onStatusAprobationHeader1Change(newValue: String,approvalLine: String,docNum:String) {
        Log.e("REOS","onStatusAprobationHeader1Change-newValue: "+newValue)
        Log.e("REOS","onStatusAprobationHeader1Change-approvalLine: "+approvalLine)
        Log.e("REOS","onStatusAprobationHeader1Change-docNum: "+docNum)
        Log.e("REOS","onStatusAprobationHeader1Change-density: "+density.value)
        Log.e("REOS","onStatusAprobationHeader1Change-optimalWeight: "+optimalWeight.value)
        Log.e("REOS","onStatusAprobationHeader1Change-maximunWeight: "+maximunWeight.value)
        _statusAprobationHeader1.value = newValue
        viewModelScope.launch {
            //_isLoadingBodyDetail.value = true
            if(!docNum.equals("")&&!density.value.equals("")&&!approvalLine.equals("")
                &&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("")
                ){
                updateApprovalStatusUseCase(
                    docNum,
                    density.value,
                    if(newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N" else "*",
                    approvalLine,
                    optimalWeight.value,
                    maximunWeight.value
                )
                _manufacturingOrderResponseModel.value.data.firstOrNull()?.detail?.forEach {
                    updateApprovalStatusUseCase(
                        it.batchName,
                        density.value,
                        if(newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N" else "*",
                        "Linea1",
                        optimalWeight.value,
                        maximunWeight.value
                    )
                }
            }
        }
    }

    fun onStatusAprobbation1Change(newValue: String,approvalLine: String) {
        _statusAprobbation1.value = newValue
        /*viewModelScope.launch {
            if(!docNum.value.equals("")&&!density.value.equals("")&&!approvalLine.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ){
                updateApprovalStatusUseCase(
                    docNum.value,
                    density.value,
                    if(newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N" else "*",
                    approvalLine,
                    optimalWeight.value,
                    maximunWeight.value

                )
            }
        }*/
    }

    fun onStatusAprobbation2Change(newValue: String,approvalLine: String) {
        _statusAprobbation2.value = newValue
        viewModelScope.launch {
            /*if(!docNum.value.equals("")&&!density.value.equals("")&&!approvalLine.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                updateApprovalStatusUseCase(
                    docNum.value,
                    density.value,
                    if (newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N" else "*",
                    approvalLine,
                    optimalWeight.value,
                    maximunWeight.value

                )
            }*/
        }
    }

    fun onStatusAprobbation3Change(newValue: String,approvalLine: String) {
        _statusAprobbation3.value = newValue
        viewModelScope.launch {
            /*if(!docNum.value.equals("")&&!density.value.equals("")&&!approvalLine.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                updateApprovalStatusUseCase(
                    docNum.value,
                    density.value,
                    if (newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N" else "*",
                    approvalLine,
                    optimalWeight.value,
                    maximunWeight.value

                )
            }*/
        }
    }

    fun onVisibleDialogEditPackingChange(newValue: Boolean) {
        _isVisibleDialogEditPacking.value = newValue
    }

    fun onOrderCodeChange(newValue: String) {
        _orderCode.value = newValue
    }

    fun getManufacturingOrder(orderCode: String) {
        viewModelScope.launch {
            _isLoadingBody.value = true // Iniciar carga
            Log.e("REOS","getManufacturingOrder-orderCode: "+orderCode)
            try {
                _manufacturingOrderResponseModel.value = getManufacturingOrderUseCase(orderCode)
            } finally {
                _isLoadingBody.value = false // Finalizar carga
            }
        }
    }

    fun getCalculateDensity(orderCode: String, density: String) {
        viewModelScope.launch {
            _isLoadingBodyDetail.value = true
            try {
                _manufacturingOrderResponseModel.value = recalculateDensityUseCase(orderCode, density)
            } finally {
                _isLoadingBodyDetail.value = false
            }
        }
    }

    fun saveStatusAprobation(){
        viewModelScope.launch {
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ){
                updateApprovalStatusUseCase(
                    docNum.value,
                    density.value,
                    if(statusAprobbation1.value.equals("Pendiente")) "*" else if (statusAprobbation1.value.equals("Aprobado")) "S" else if (statusAprobbation1.value.equals("Rechazado")) "N" else "*",
                    "Linea1",
                    optimalWeight.value,
                    maximunWeight.value

                )
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                    updateApprovalStatusUseCase(
                        docNum.value,
                        density.value,
                        if (statusAprobbation2.value.equals("Pendiente")) "*" else if (statusAprobbation2.value.equals("Aprobado")) "S" else if (statusAprobbation2.value.equals("Rechazado")) "N" else "*",
                        "Linea2",
                        optimalWeight.value,
                        maximunWeight.value

                    )
                }
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                        updateApprovalStatusUseCase(
                            docNum.value,
                            density.value,
                            if (statusAprobbation3.value.equals("Pendiente")) "*" else if (statusAprobbation3.value.equals("Aprobado")) "S" else if (statusAprobbation3.value.equals("Rechazado")) "N" else "*",
                            "Linea3",
                            optimalWeight.value,
                            maximunWeight.value

                        )
                    }
            }
        }
    }

}