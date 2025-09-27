package com.vistony.app.Screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vistony.app.BuildConfig
import com.vistony.app.Entidad.UserState
import com.vistony.app.R
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.ViewModel.EstadoLogin
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Login2(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
    userState: UserState
) {
    var usuario by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val titleFontSize = Dimensions.getTitleFontSize(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    val imageSize = Dimensions.getImageSize(windowSize.widthSizeClass)

    var stateButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()

    val userData by viewModel.userData.collectAsState()

    LaunchedEffect(usuario, pass) {
        stateButton = usuario.isNotEmpty() && pass.isNotEmpty()
    }
    /*LaunchedEffect(loginState) {
        if (loginState.state) {
            navController.navigate("listaInsp/$usuario")
        } else {
            errorMessage = loginState.message ?: ""
        }
    }*/
    // Efecto para redireccionar según el rol
    /*LaunchedEffect(userData?.role) {
        Log.d("Login2", "userState: $userState")
        userData?.let { user ->
            when (user.role.lowercase()) {
                "admin", "supervisor" -> {
                    // Redireccionar a dashboard administrativo
                    navController.navigate("dashboardAdmin/${user.id}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "mantenimiento" -> {
                    // Redireccionar a lista de inspecciones
                    navController.navigate("paradaMantenimiento") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "producción" -> {
                    // Redireccionar a lista de paradas
                    navController.navigate("listaParada/${user.id}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    // Rol no reconocido, redireccionar por defecto
                    navController.navigate("listaInsp/${user.id}") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }*/

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6E9F1))

    ) {
        val screenWidth = maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_res)
                //.border(2.dp, Color.White, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.44f)),
                //.graphicsLayer { alpha = 0.44f },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(imageSize)
                    .padding(bottom = 16.dp),
                painter = painterResource(id = R.mipmap.operity_core),
                contentDescription = ""
            )
            Text(text = "Bienvenido al centro de tu", color = Color.Gray, fontSize = bodyFontSize.sp)
            Text(text = "OPERACIÓN", fontSize = titleFontSize.sp)
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(textFieldHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                value = usuario,
                label = {
                    Text(
                        text = "Usuario",
                        color = if( usuario.isNotEmpty())Color.Gray else Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = if( usuario.isNotEmpty()) 12.sp else bodyFontSize.sp
                    )
                },
                onValueChange = { usuario = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            )
            Spacer(modifier = Modifier.height(padding_res/2))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(textFieldHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(12.dp)),
                value = pass,
                label = {
                    Text(
                        text = "Contraseña",
                        color = if( pass.isNotEmpty())Color.Gray else Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = if( pass.isNotEmpty()) 12.sp else bodyFontSize.sp
                    )
                },
                onValueChange = { pass = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Gray
                ),
                trailingIcon = {
                    val image = if (passVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Contraseña",
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.validar(usuario,pass)
                        showDialog = true
                    }
                )
            )
            Spacer(modifier = Modifier.height(padding_res))
            Button(
                enabled = stateButton,
                onClick = {
                    viewModel.validar(usuario,pass)
                    showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(0.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    //containerColor = Color(0xFFFC6A68),
                    //containerColor = Color(0xFFD6001C),
                    containerColor = Color(0xFF01398D),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Iniciar Sesion", fontSize = bodyFontSize.sp )
            }
            Spacer(modifier = Modifier.height(padding_res))
            Text(text = "v${BuildConfig.VERSION_NAME}", color = Color.Gray, fontSize = bodyFontSize.sp)
            /*Image(
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 16.dp),
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = ""
            )*/

            when(isLoading){
                EstadoLogin.Cargando-> {
                    CustomAlertDialog(
                        showDialog = showDialog,
                        title = "Cargando",
                        message = "Validando...",
                        confirmButtonText = "",
                        dismissButtonText = null,
                        onDismiss = {showDialog = false}
                    )
                }
                EstadoLogin.Exitoso -> {
                    userData?.let { user ->
                        when (user.role.lowercase()) {
                            "admin", "supervisor" -> {
                                navController.navigate("dashboardAdmin/${user.id}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            "mantenimiento" -> {
                                navController.navigate("paradaMantenimiento") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            "producción" -> {
                                navController.navigate("listaParada/${user.id}"){
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            else -> {
                                navController.navigate("listaInsp/${user.id}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    }
                }
                is EstadoLogin.Error -> {
                    CustomAlertDialog(
                        showDialog = showDialog,
                        title = "Error",
                        message = (isLoading as EstadoLogin.Error).mensaje,
                        confirmButtonText = "OK",
                        dismissButtonText = null,
                        //onConfirm = { viewModel.actualizarEstadoLogin(EstadoLogin.Idle) },
                        onDismiss = {
                            showDialog = false
                            //viewModel.actualizarEstadoLogin(EstadoLogin.Idle)
                        },
                        dialogType = DialogType.ERROR,
                    )
                }
                else -> {}
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Desarrollado por:",
                color = Color.Gray,
                fontSize = 10.sp,
            )
            Text(
                text = "© 2025 Vistony S.A.C.",
                color = Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}