package com.vistony.app.clean.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.clean.domain.model.ApprobationDefaults
import com.vistony.app.clean.domain.model.DensityGroupResponseModel
import com.vistony.app.clean.domain.model.DensityResponseModel
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.model.ReasonForRejectionsResponseModel
import com.vistony.app.clean.domain.usecases.GetManufacturingOrderUseCase
import com.vistony.app.clean.domain.usecases.GetReasonForRejectionsUseCase
import com.vistony.app.clean.domain.usecases.RecalculateDensityUseCase
import com.vistony.app.clean.domain.usecases.UpdateApprovalStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.code
import kotlin.invoke
import kotlin.sequences.ifEmpty

@HiltViewModel
class ManufacturingOrderViewModel @Inject constructor(
    private val getManufacturingOrderUseCase: GetManufacturingOrderUseCase,
    private val recalculateDensityUseCase: RecalculateDensityUseCase,
    private val updateApprovalStatusUseCase: UpdateApprovalStatusUseCase,
    private val getReasonForRejectionsUseCase: GetReasonForRejectionsUseCase
): ViewModel() {
    private val _manufacturingOrderResponseModel = MutableStateFlow(ManufacturingOrderResponseModel())
    val manufacturingOrderResponseModel: StateFlow<ManufacturingOrderResponseModel> get() = _manufacturingOrderResponseModel
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

    private val _statusAprobationHeader1 = MutableStateFlow("")
    val statusAprobationHeader1: StateFlow<String> get() = _statusAprobationHeader1

    private val _docNum = MutableStateFlow("")
    val docNum: StateFlow<String> get() = _docNum

    private val _density = MutableStateFlow("")
    val density: StateFlow<String> get() = _density

    private val _lineNum = MutableStateFlow("")
    val lineNum: StateFlow<String> get() = _lineNum

    private val _optimalWeight = MutableStateFlow("0")
    val optimalWeight: StateFlow<String> get() = _optimalWeight

    private val _maximunWeight = MutableStateFlow("0")
    val maximunWeight: StateFlow<String> get() = _maximunWeight

    private val _isLoadingBody = MutableStateFlow(false)
    val isLoadingBody: StateFlow<Boolean> get() = _isLoadingBody

    private val _isLoadingBodyDetail = MutableStateFlow(false)
    val isLoadingBodyDetail: StateFlow<Boolean> get() = _isLoadingBodyDetail

    private val _isStatusApprobationContainer1 = MutableStateFlow(true)
    val isStatusApprobationContainer1: StateFlow<Boolean> get() = _isStatusApprobationContainer1

    private val _isStatusApprobationContainer2 = MutableStateFlow(false)
    val isStatusApprobationContainer2: StateFlow<Boolean> get() = _isStatusApprobationContainer2

    private val _isStatusApprobationContainer3 = MutableStateFlow(false)
    val isStatusApprobationContainer3: StateFlow<Boolean> get() = _isStatusApprobationContainer3

    val _statusCorrection = MutableStateFlow("Pendiente")
    val statusCorrection: StateFlow<String> get() = _statusCorrection

    val _statusDesaprobation = MutableStateFlow("Pendiente")
    val statusDesaprobation: StateFlow<String> get() = _statusDesaprobation

    private val _reasonForRejectionsResponseModel = MutableStateFlow(ReasonForRejectionsResponseModel())
    val reasonForRejectionsResponseModel: StateFlow<ReasonForRejectionsResponseModel> get() = _reasonForRejectionsResponseModel

    val _reasonDesaprobation = MutableStateFlow("")
    val reasonDesaprobation: StateFlow<String> get() = _reasonDesaprobation

    val _observation = MutableStateFlow("")
    val observation: StateFlow<String> get() = _observation

    val _isVisibleObservation = MutableStateFlow(false)
    val isVisibleObservation: StateFlow<Boolean> get() = _isVisibleObservation

    val _densityGroupResponseModel = MutableStateFlow(DensityGroupResponseModel())
    val densityGroupResponseModel: StateFlow<DensityGroupResponseModel> get() = _densityGroupResponseModel


    fun setIsVisibleObservation(newValue: Boolean) {
        _isVisibleObservation.value = newValue
    }

    fun onObservationChange(newValue: String) {
        _observation.value = newValue
    }

    fun onReasonDesaprobationChange(newValue: String) {
        _reasonDesaprobation.value = newValue
    }

    fun onStatusCorrectionChange(newValue: String) {
        _statusCorrection.value = newValue
    }

    fun onStatusDesaprobationChange(newValue: String) {
        _statusDesaprobation.value = newValue
    }

    fun statusApprobationContainer1Change(newValue: Boolean) {
        _isStatusApprobationContainer1.value = newValue
    }

    fun statusApprobationContainer2Change(newValue: Boolean) {
        _isStatusApprobationContainer2.value = newValue
    }

    fun statusApprobationContainer3Change(newValue: Boolean) {
        _isStatusApprobationContainer3.value = newValue
    }


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

// Kotlin
fun onStatusAprobationHeader1Change(newValue: String, approvalLine: String, docNum: String) {
    Log.e("REOS", "onStatusAprobationHeader1Change-newValue: $newValue")
    Log.e("REOS", "onStatusAprobationHeader1Change-approvalLine: $approvalLine")
    Log.e("REOS", "onStatusAprobationHeader1Change-docNum: $docNum")
    Log.e("REOS", "onStatusAprobationHeader1Change-density: ${_density.value}")
    Log.e("REOS", "onStatusAprobationHeader1Change-optimalWeight: ${_optimalWeight.value}")
    Log.e("REOS", "onStatusAprobationHeader1Change-maximunWeight: ${_maximunWeight.value}")

    _statusAprobationHeader1.value = newValue

    // Buscar aprobación de forma segura
    val approval = ApprobationDefaults.DEFAULT_APPROBATIONS_HEADER.firstOrNull { it.name == newValue }
    if (approval == null) {
        Log.e("REOS", "onStatusAprobationHeader1Change: aprobación '$newValue' no encontrada. Abortando.")
        return
    }

    // Proteger acceso a motivos de rechazo
    val reasonSelectedName = _reasonDesaprobation.value
    val reasonSelected = _reasonForRejectionsResponseModel.value.data.firstOrNull { it.name == reasonSelectedName }
    val reasonCode = reasonSelected?.code ?: -1

    viewModelScope.launch {
        try {
            _isLoadingBodyDetail.value = true
            // Corregir nombres de parámetros según los errores
            updateApprovalStatusUseCase.invoke(
                docNum = docNum,
                approvalLine = approvalLine,                           // Cambiar 'line' por 'approvalLine'
                approvalStatus = if(newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals("Rechazado")) "N"  else if (newValue.equals("No Conforme")) "N" else "*",
                density = _density.value.ifEmpty { "0" },
                optimalWeight = _optimalWeight.value.ifEmpty { "0" },
                maximunWeight = _maximunWeight.value.ifEmpty { "0" },
                //estadoAprobacionCorreccion = _statusCorrection.value,   // Agregar parámetro faltante
                //estadoAprobacionDesaprobadoCalidad = _statusDesaprobation.value, // Agregar parámetro faltante
                estadoAprobacionCorreccion=if(_statusCorrection.value.equals("Pendiente")) "*" else if (_statusCorrection.value.equals("Aprobado")) "S" else if (_statusCorrection.value.equals("Rechazado")) "N" else if (_statusCorrection.value.equals("Si")) "S" else if (_statusCorrection.value.equals("No")) "N" else "*",
                estadoAprobacionDesaprobadoCalidad=if(_statusDesaprobation.value.equals("Pendiente")) "*" else if (_statusDesaprobation.value.equals("Aprobado")) "S" else if (_statusDesaprobation.value.equals("Rechazado")) "N" else "*",
                motivoCorreccion = reasonCode.toString(),                  // Agregar parámetro faltante (nombre del motivo)
                //rejectionReasonCode = reasonCode                        // Mantener código del motivo
                observations = _observation.value
            )
            // Refrescar datos
            //getManufacturingOrder(docNum)

            if(newValue.equals("No Conforme")){
                Log.e("REOS", "onStatusAprobationHeader1Change-No Conforme selected")
                //_isVisibleObservation.value=true
                _statusAprobbation1.value="Rechazado"
                _statusAprobbation2.value="Rechazado"
                _statusAprobbation3.value="Rechazado"
                saveStatusAprobationDetail()
            }else {
                Log.e("REOS", "onStatusAprobationHeader1Change-Other status selected")
                _manufacturingOrderResponseModel.value.data.firstOrNull()?.detail?.forEach {
                    updateApprovalStatusUseCase(
                        it.batchName,
                        density.value,
                        if (newValue.equals("Pendiente")) "*" else if (newValue.equals("Aprobado")) "S" else if (newValue.equals(
                                "Rechazado"
                            )
                        ) "N" else "*",
                        "Linea1",
                        optimalWeight.value,
                        maximunWeight.value,
                        "",
                        "",
                        "", ""
                    )
                }
            }


        } catch (e: Exception) {
            Log.e("REOS", "onStatusAprobationHeader1Change-error: ${e.message}", e)
        } finally {
            _isLoadingBodyDetail.value = false
        }
    }
}



    fun onStatusAprobbation1Change(newValue: String,approvalLine: String) {
        _statusAprobbation1.value = newValue
    }

    fun onStatusAprobbation2Change(newValue: String,approvalLine: String) {
        _statusAprobbation2.value = newValue
        viewModelScope.launch {
        }
    }

    fun onStatusAprobbation3Change(newValue: String,approvalLine: String) {
        _statusAprobbation3.value = newValue
        viewModelScope.launch {
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
                _manufacturingOrderResponseModel.value.data.firstOrNull()?.let { order ->
                    Log.e("REOS","getManufacturingOrder-order.approbationName1: "+order.approbationName1)
                    Log.e("REOS","getManufacturingOrder-order.correction: "+order.correction)
                    _statusAprobationHeader1.value = order.approbationName1
                    _statusCorrection.value=if(order.correction.equals("NO")) "No" else if (order.correction.equals("SI")) "Si" else "No"
                    _reasonDesaprobation.value=order.reason
                    _statusDesaprobation.value=if(order.qualityDisapproved.equals("N")) "Rechazado" else if (order.qualityDisapproved.equals("S")) "Aprobado" else "Pendiente"
                    _density.value=order.density
                    _observation.value=order.observations
                }
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
                _manufacturingOrderResponseModel.value.data.firstOrNull()?.let { order ->
                    Log.e("REOS","recalculateDensityUseCase-order.approbationName1: "+order.approbationName1)
                    Log.e("REOS","recalculateDensityUseCase-order.correction: "+order.correction)
                    _statusAprobationHeader1.value = order.approbationName1
                    //_statusCorrection.value=if(order.correction.equals("NO")) "Rechazado" else if (order.correction.equals("SI")) "Aprobado" else "Pendiente"
                    _statusCorrection.value=if(order.correction.equals("NO")) "No" else if (order.correction.equals("SI")) "Si" else "No"
                    _reasonDesaprobation.value=order.reason
                    _statusDesaprobation.value=if(order.qualityDisapproved.equals("N")) "Rechazado" else if (order.qualityDisapproved.equals("S")) "Aprobado" else "Pendiente"
                    _observation.value=order.observations
                }
            } finally {
                _isLoadingBodyDetail.value = false
            }
        }
    }

    fun saveStatusAprobation(){
        Log.e("REOS","saveStatusAprobation-docNum: "+docNum.value)
        Log.e("REOS","saveStatusAprobation-density: "+density.value)
        Log.e("REOS","saveStatusAprobation-optimalWeight: "+optimalWeight.value)
        Log.e("REOS","saveStatusAprobation-maximunWeight: "+maximunWeight.value)
        viewModelScope.launch {
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ){
                    Log.e("REOS","saveStatusAprobation-entra linea 1")
                updateApprovalStatusUseCase(
                    docNum.value,
                    density.value,
                    if(statusAprobbation1.value.equals("Pendiente")) "*" else if (statusAprobbation1.value.equals("Aprobado")) "S" else if (statusAprobbation1.value.equals("Rechazado")) "N" else "*",
                    "Linea1",
                    optimalWeight.value,
                    maximunWeight.value,
                    "",
                    "",
                "",
                    ""

                )
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                    updateApprovalStatusUseCase(
                        docNum.value,
                        density.value,
                        if (statusAprobbation2.value.equals("Pendiente")) "*" else if (statusAprobbation2.value.equals("Aprobado")) "S" else if (statusAprobbation2.value.equals("Rechazado")) "N" else "*",
                        "Linea2",
                        optimalWeight.value,
                        maximunWeight.value,
                        "",
                        "",
                        "",
                        ""

                    )
                }
                if(!docNum.value.equals("")&&!density.value.equals("")&&!optimalWeight.value.equals("")&&!maximunWeight.value.equals("") ) {
                        updateApprovalStatusUseCase(
                            docNum.value,
                            density.value,
                            if (statusAprobbation3.value.equals("Pendiente")) "*" else if (statusAprobbation3.value.equals("Aprobado")) "S" else if (statusAprobbation3.value.equals("Rechazado")) "N" else "*",
                            "Linea3",
                            optimalWeight.value,
                            maximunWeight.value,
                            "",
                            "",
                            "",
                            ""

                        )
                    }
            }
        }
    }

    fun getReasonForRejections() {
        viewModelScope.launch {
            _isLoadingBody.value = true // Iniciar carga
            Log.e("REOS","getManufacturingOrder-orderCode: "+orderCode)
            try {
                _reasonForRejectionsResponseModel.value = getReasonForRejectionsUseCase()
            } finally {
                _isLoadingBody.value = false // Finalizar carga
            }
        }
    }

    fun saveStatusAprobationDetail(){
        Log.e("REOS","saveStatusAprobationDetail-docNum: "+docNum.value)
        Log.e("REOS","saveStatusAprobationDetail-density: "+density.value)
        Log.e("REOS","saveStatusAprobationDetail-optimalWeight: "+optimalWeight.value)
        Log.e("REOS","saveStatusAprobationDetail-maximunWeight: "+maximunWeight.value)
        viewModelScope.launch {
            _manufacturingOrderResponseModel.value.data.firstOrNull()?.detail?.forEach {
                updateApprovalStatusUseCase(
                    it.batchName,
                    density.value,
                    if(statusAprobbation1.value.equals("Pendiente")) "*" else if (statusAprobbation1.value.equals("Aprobado")) "S" else if (statusAprobbation1.value.equals("Rechazado")) "N" else "*",
                    "Linea1",
                    optimalWeight.value,
                    maximunWeight.value,
                    "",
                    "",
                    "",
                    ""
                )
                updateApprovalStatusUseCase(
                    it.batchName,
                    density.value,
                    if (statusAprobbation2.value.equals("Pendiente")) "*" else if (statusAprobbation2.value.equals("Aprobado")) "S" else if (statusAprobbation2.value.equals("Rechazado")) "N" else "*",
                    "Linea2",
                    optimalWeight.value,
                    maximunWeight.value,
                    "",
                    "",
                    "",
                    ""
                )
                updateApprovalStatusUseCase(
                    it.batchName,
                    density.value,
                    if (statusAprobbation3.value.equals("Pendiente")) "*" else if (statusAprobbation3.value.equals("Aprobado")) "S" else if (statusAprobbation3.value.equals("Rechazado")) "N" else "*",
                    "Linea3",
                    optimalWeight.value,
                    maximunWeight.value,
                    "",
                    "",
                    "",
                    ""
                )
            }
        }
    }

}