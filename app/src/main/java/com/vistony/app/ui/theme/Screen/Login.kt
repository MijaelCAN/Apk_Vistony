package com.vistony.app.ui.theme.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vistony.app.R
import com.vistony.app.ViewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0B4FAF)
            ),
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
        Box(
            modifier = Modifier
                .width(500.dp)
                .height(600.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(color = Color.Red)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(540.dp)// Tamaño del círculo
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(bottomStart = 150.dp, bottomEnd = 150.dp)
                    )
                //.offset(x = 0.dp, y = (-100).dp) // Puedes ajustar el desplazamiento aquí
            )
            Body(navController, viewModel)
        }

    }
}

@Composable
fun Titulo() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Iniciar Sesión",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        ),
    )
    /*Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {

    }*/
}

@ExperimentalMaterial3Api
@Composable
fun Body(navController: NavHostController, viewModel: LoginViewModel) {
    var user by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var passVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(width = 400.dp, height = 150.dp),
            painter = painterResource(id = R.drawable.vistony),
            contentDescription = "Logo"
        )
        Titulo()
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = user,
            onValueChange = {
                user = it.filter { char -> char.isLetterOrDigit() }
            },
            label = { Text(text = "Usuario") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color(0xFFA2A2A2),
                cursorColor = Color(0xFF0054A3),
                unfocusedTextColor = Color.Black,
                //textColor = Color.Black,
                unfocusedBorderColor = Color(0xFFA2A2A2),
                focusedBorderColor = Color(0xFF0054A3)
            ),
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "User", tint = Color(0xFFA2A2A2))
            },
            maxLines = 1
        )
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text(text = "Contraseña") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color(0xFFA2A2A2),
                cursorColor = Color(0xFF0054A3),
                //textColor = Color.Black,
                unfocusedBorderColor = Color(0xFFA2A2A2),
                focusedBorderColor = Color(0xFF0054A3)
            ),
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "User", tint = Color(0xFFA2A2A2))
            },
            trailingIcon = {
                val image = if (passVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Contraseña",
                        tint = Color(0xFFA2A2A2)
                    )
                }
            },
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { checked -> rememberMe = checked }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Recordar mis credenciales")
        }*/
        Spacer(modifier = Modifier.height(25.dp))
        Boton(user, pass, navController, viewModel)
    }
}

@Composable
fun Boton(user: String, pass: String, navController: NavHostController, viewModel: LoginViewModel) {
    var stateButton by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val loginState = viewModel._loginstate

    LaunchedEffect(user, pass) {
        stateButton = user.isNotEmpty() && pass.isNotEmpty()
    }

    LaunchedEffect(loginState) {
        if (loginState.state) {
            navController.navigate("home/$user")
        } else {
            errorMessage = loginState.message ?: ""
        }
    }

    val buttonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = if (stateButton) Color(0xFF0054A3) else Color(0XFF9C9B9B),
        contentColor = if (stateButton) Color(0xFFC9C9C9) else Color.White,
        disabledContentColor = Color.White,
        disabledContainerColor = Color(0XFF9C9B9B)
    )

    ElevatedButton(
        modifier = Modifier
            .width(200.dp)
            .height(58.dp),
        enabled = stateButton,
        onClick = {
            viewModel.validar(user, pass)
            if (viewModel._loginstate.state) {
                navController.navigate("home/$user")
            }
        },
        colors = buttonColors,
        shape = RoundedCornerShape(18.dp)
    ) {
        Icon(Icons.Filled.Login, contentDescription = "Login")
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = "INICIAR SESIÓN")
    }
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}