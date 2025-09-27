package com.vistony.app.Screen.Generic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.vistony.app.ViewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title:String,
    color: Color = Color(0xFF0957c3),
    colorContent: Color = Color.White,
    navController: NavController,
    onMenuClick: () -> Unit,
    onBottomMenuClick: (() -> Unit)? = null,
    viewModel: LoginViewModel
) {
    TopAppBar(
        title = { Text(text = title, color = colorContent) },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = colorContent)
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.onResetStateLogin()
                navController.navigate("login")
            }) {
                Text(text = "Salir", color = colorContent)
            }
            if (title.isEmpty() && onBottomMenuClick != null) {
                IconButton(onClick = { onBottomMenuClick() }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Abrir menú inferior", tint = colorContent)
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = color)
    )
}