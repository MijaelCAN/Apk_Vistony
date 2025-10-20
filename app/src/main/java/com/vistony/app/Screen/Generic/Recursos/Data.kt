package com.vistony.app.Screen.Generic.Recursos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material.icons.sharp.Help
import androidx.compose.material.icons.sharp.Inventory
import androidx.compose.material.icons.sharp.PersonalInjury
import androidx.compose.material.icons.sharp.WaterDrop
import com.vistony.app.Entidad.FailureType

class Data {
    val optionsFalla = listOf(
        FailureType(id = 1, name = "Electrica") to Icons.Default.ElectricBolt,
        FailureType(id = 2, name = "Mecanica") to Icons.Default.Build,
        FailureType(id = 3, name = "Neumatico") to Icons.Default.TireRepair,
        FailureType(id = 4, name = "Operacional") to Icons.Sharp.PersonalInjury,
        FailureType(id = 5, name = "Hidraulico") to Icons.Sharp.WaterDrop,
        FailureType(id = 6, name = "Material") to Icons.Sharp.Inventory,
        FailureType(id = 7, name = "Otro") to Icons.Sharp.Help,
    )
}