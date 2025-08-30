package com.vistony.app.Screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.R
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Login2() {
    var usuario by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

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
    val cardHeight = Dimensions.getCardHeight(windowSize.widthSizeClass)
    val cardPadding = Dimensions.getCardPadding(windowSize.widthSizeClass)


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
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = ""
            )
            Text(text = "Bienvenido a la APP", color = Color.Gray, fontSize = bodyFontSize.sp)
            Text(text = "PRODUCCIÓN", fontSize = titleFontSize.sp)
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(textFieldHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                value = usuario,
                placeholder = {
                    Text(
                        text = "Enter username",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = bodyFontSize.sp
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
                )
            )
            Spacer(modifier = Modifier.height(padding_res/2))
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
                )
            )
            Spacer(modifier = Modifier.height(padding_res))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(0.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC6A68),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign in", fontSize = bodyFontSize.sp )
            }
        }
    }
}


/*
@Composable
@Preview
fun Prueba() {
    Login2()
}*/