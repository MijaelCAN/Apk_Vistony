package com.vistony.app.clean.presentation.view.templates

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.clean.presentation.view.organisms.ManuFacturingOrderSectionMain
import com.vistony.salesforce.kotlin.view.Atoms.theme.VistonyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManuFacturingOrderTemplate(
    navController: NavHostController,
    id: String,
    userState: UserState
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val loginViewModel = hiltViewModel<LoginViewModel>(LocalContext.current as ComponentActivity)
    VistonyTheme() {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CustomDrawer(
                    navController = navController,
                    id = id,
                    userState = userState
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        "Orden de Fabricación"
                        , navController = navController
                        , onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                        onBottomMenuClick = {
                            scope.launch {
                                if (bottomSheetState.isVisible) {
                                    bottomSheetState.hide()
                                } else {
                                    bottomSheetState.show()
                                }
                            }
                        },
                        viewModel = loginViewModel
                     )
                },
                modifier = Modifier.fillMaxSize(),
                content = { paddingValues ->
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
}