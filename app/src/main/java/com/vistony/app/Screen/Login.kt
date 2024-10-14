package com.vistony.app.Screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vistony.app.R
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.ViewModel.EstadoLogin
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isTablet = isTablet(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B4FAF))
            .systemBarsPadding()
            .imePadding()
        /*horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center*/
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier=Modifier.fillMaxWidth()
        ){
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.mipmap.tanques),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        // Ajusta el tamaño del contenedor según el tamaño de la pantalla
        val containerSize = if (isTablet) 600.dp else 300.dp
        val cournerZise = containerSize * 0.1f
        Box(
            modifier = Modifier
                .width(containerSize)
                .height(containerSize * 1.2f)
                .clip(RoundedCornerShape(cournerZise))
                .background(color = Color.Red)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(containerSize * 1.1f)// Tamaño del círculo
                    .background(
                        Color(0xFFE6E9F1),
                        shape = RoundedCornerShape(
                            bottomStart = cournerZise,
                            bottomEnd = cournerZise
                        )
                    )
            )
            Body(navController, viewModel,containerSize)
        }

    }
}
fun isTablet(context: Context): Boolean {
    // Obtener las dimensiones de la pantalla
    val metrics = context.resources.displayMetrics
    val widthInches = metrics.widthPixels / metrics.xdpi
    val heightInches = metrics.heightPixels / metrics.ydpi

    // Calcular el tamaño diagonal en pulgadas
    val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

    // Considerar un dispositivo como tablet si la diagonal es mayor o igual a 7.0 pulgadas
    return diagonalInches >= 7.0
}

@Composable
fun Titulo(titleSize: Dp) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Iniciar Sesión",
        style = TextStyle(
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            fontSize = titleSize.value.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalMaterial3Api
@Composable
fun Body(navController: NavHostController, viewModel: LoginViewModel, containerSize: Dp) {

    /*val imageSize = containerSize * 0.4f
    val titleSize = containerSize * 0.1f
    val fieldSize = containerSize * 0.13f
    val buttonSize = containerSize * 0.13f*/
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val buttonWidth = Dimensions.getButtonWidth(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val titleFontSize = Dimensions.getTitleFontSize(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    val imageSize = Dimensions.getImageSize(windowSize.widthSizeClass)

    var user by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var passVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = containerSize * 0.13f, vertical = containerSize * 0.1f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.44f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Image(
            modifier = Modifier.size(width = imageSize, height = imageSize * 0.4f),
            painter = painterResource(id = R.drawable.vistony),
            contentDescription = "Logo"
        )*/
        Image(
            modifier = Modifier
                .size(imageSize)
                .padding(bottom = 16.dp),
            painter = painterResource(id = R.mipmap.logo),
            contentDescription = ""
        )
        Titulo(titleFontSize.dp)
        Spacer(modifier = Modifier.height(containerSize * 0.05f))
        /*OutlinedTextField(
            modifier = Modifier.height(textFieldHeight).fillMaxWidth(),
            value = user,
            onValueChange = {
                user = it.filter { char -> char.isLetterOrDigit() }
            },
            label = { Text(text = "Usuario",style = MaterialTheme.typography.labelMedium) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "User", tint = MaterialTheme.colorScheme.onSurface)
            },
            singleLine = true
        )*/
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(textFieldHeight)
                .padding(horizontal = padding_res)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
            value = user,
            placeholder = {
                Text(
                    text = "Enter username",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = bodyFontSize.sp
                )
            },
            onValueChange = { user = it.filter { char -> char.isLetterOrDigit() } },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray
            ),
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "User", tint = Color.Gray)
            },
        )
        Spacer(modifier = Modifier.height(containerSize * 0.05f))
        /*OutlinedTextField(
            modifier = Modifier.height(textFieldHeight).fillMaxWidth(),
            value = pass,
            onValueChange = { pass = it },
            label = { Text(text = "Contraseña", style = MaterialTheme.typography.labelMedium) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "User", tint = MaterialTheme.colorScheme.onSurface)
            },
            trailingIcon = {
                val image = if (passVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Contraseña",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation()
        )*/
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(textFieldHeight)
                .padding(horizontal = padding_res)
                .clip(RoundedCornerShape(12.dp)),
            value = pass,
            placeholder = {
                Text(
                    text = "password",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = bodyFontSize.sp
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
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "User", tint = Color.Gray)
            },
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
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(containerSize * 0.05f))
        Boton(user, pass, navController, viewModel,buttonHeight)
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Boton(
    user: String,
    pass: String,
    navController: NavHostController,
    viewModel: LoginViewModel,
    buttonSize: Dp
) {
    var stateButton by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val loginState = viewModel._loginstate
    var showDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(user, pass) {
        stateButton = user.isNotEmpty() && pass.isNotEmpty()
    }

    LaunchedEffect(loginState) {
        if (loginState.state) {
            navController.navigate("listaInsp/$user")
        } else {
            errorMessage = loginState.message ?: ""
        }
    }
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    val buttonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = if (stateButton) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        contentColor = if (stateButton) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )

    /*ElevatedButton(
        modifier = Modifier
            .height(buttonSize)
            .fillMaxWidth(),
        enabled = stateButton,
        onClick = {
            viewModel.validar(user, pass)
            if (viewModel._loginstate.state) {
                navController.navigate("home/$user")
            }
        },
        colors = buttonColors,
        shape = MaterialTheme.shapes.medium // Utiliza la forma definida en el tema
    ) {
        Icon(Icons.Filled.Login, contentDescription = "Login", tint = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = "INICIAR SESIÓN", style = MaterialTheme.typography.bodyMedium)
    }*/
    Button(
        enabled = stateButton,
        onClick = {
        /*TODO*/
            viewModel.validar(user, pass)
            showDialog = true
            if (viewModel._loginstate.state) {
                navController.navigate("home/$user")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .padding(horizontal = padding_res)
            .clip(RoundedCornerShape(0.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,//Color(0xFFFC6A68),
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Filled.Login, contentDescription = "Login", tint = Color.White)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = "INICIAR SESIÓN", fontSize = bodyFontSize.sp )
    }

    /*if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }*/
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
            /*CustomAlertDialog(
                showDialog = showDialog,
                title = "Envio Exitoso",
                message = loginState.loginResponse.data,
                icon = Icons.Default.Check,
                confirmButtonText = "OK",
                dismissButtonText = null,
                onConfirm = { viewModel.actualizarEstadoLogin(EstadoLogin.Idle) },
                onDismiss = {
                    showDialog = false
                    viewModel.actualizarEstadoLogin(EstadoLogin.Idle)
                    navController.navigate("listaInsp/$id")
                },
                dialogType = DialogType.SUCCESS,
            )*/
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