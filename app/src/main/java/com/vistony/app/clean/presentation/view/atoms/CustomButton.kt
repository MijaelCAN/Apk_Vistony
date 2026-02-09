package com.vistony.app.clean.presentation.view.atoms

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vistony.app.R
import com.vistony.salesforce.kotlin.view.Atoms.theme.RedVistony

@Composable
fun RowScope.ButtonView(
    description:String
    ,OnClick:() ->Unit
    ,status: Boolean=false
    ,IconActive:Boolean=false
    ,context: Context
    ,backGroundColor: Color = RedVistony
    ,textColor: Color = Color.White,
) {
    val lblDisableButton = stringResource(R.string.button_disabled)
    Box(
        modifier = Modifier
            //.size(200.dp)
            .weight(1f)
            .height(50.dp)
            //.fillMaxWidth()

            .background(
                if(status){backGroundColor}else{Color.Gray} , RoundedCornerShape(4.dp)
            )
            .clickable {
                if (status) {
                    OnClick()
                } else {
                    Toast
                        .makeText(
                            context,
                            lblDisableButton,
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            },
        contentAlignment = Alignment.Center,


        ) {
        Row()
        {
            if (IconActive) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_arrow_back_white_24dp),
                    tint = Color.White,
                    contentDescription = null
                )
            }
            TableCell(
                text = description,
                color = textColor,
                title = true,
                weight = 1f,
                textAlign = TextAlign.Center
            )
        }
    }
}
