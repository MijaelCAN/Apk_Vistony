package com.vistony.app.clean.presentation.view.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardM3(
    contentBody: @Composable () -> Unit = {},
    contentActions: @Composable () -> Unit = {},
    textDivider: String = "Actions",
    isUsedTextDivider: Boolean = true
) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .clickable {},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White),
        elevation = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(Color.White.copy(alpha = 0.44f)),
        ) {
            //CardMenuOptions()
            CardBody(contentBody = contentBody)
            CardMenuActions(contentActions = contentActions,textDivider,isUsedTextDivider)
        }
    }
}

@Composable
fun CardMenuActions(contentActions: @Composable () -> Unit, textDivider: String, isUsedTextDivider : Boolean = true) {
    if(isUsedTextDivider) {
        TextWithDivider(text = textDivider)
    }
    contentActions()
}

@Composable
fun CardBody(contentBody: @Composable () -> Unit) {
    contentBody()
}

