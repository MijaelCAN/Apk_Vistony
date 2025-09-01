package com.vistony.app.clean.presentation.view.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.clean.presentation.view.organisms.ManuFacturingOrderSectionMain
import kotlinx.coroutines.launch

@Composable
fun ManuFacturingOrderTemplate(
    navController: NavHostController,
    id: String,
    userState: UserState
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id = id, userState =  userState) }
    ) {
        Scaffold(
            topBar = {
                TopBar("Orden de Fabricación", navController = navController, onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                paddingValues ->
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE6E9F1))
                        .padding(paddingValues)

                ) {
                    ManuFacturingOrderSectionMain()
                }
            }
        )
    }
}