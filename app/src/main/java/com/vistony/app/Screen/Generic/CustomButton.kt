package com.vistony.app.Screen.Generic

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.vistony.app.Entidad.FailureType

@Composable
fun CustomSearchText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.LightGray,
    contentColor: Color = Color.LightGray,
    containerColor: Color = Color.Transparent,
    searchIcon: ImageVector = Icons.Default.Search,
    enabled: Boolean = true,
    placeholderText: String = "Buscar..."
) {
    // Estado para controlar si el campo tiene foco
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(45.dp)
            .defaultMinSize(minWidth = 64.dp)
            .background(containerColor, shape = RoundedCornerShape(20.dp))
            .border(
                width = 0.5.dp,
                color = if (isFocused) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = searchIcon,
                contentDescription = "Search Icon",
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = contentColor,
                        fontSize = 14.sp
                    ),
                    enabled = enabled,
                    cursorBrush = SolidColor(contentColor)
                )
                if (text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        color = contentColor.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ImagePickerRow(
    //images: List<Uri>, // o List<ImageUri> o List<String> según cómo manejes las imágenes
    images: SnapshotStateList<Uri>, // o List<ImageUri> o List<String> según cómo manejes las imágenes
    onAddClick: () -> Unit,
    onRemoveImage: (Uri) -> Unit,
    modifier: Modifier = Modifier,
    itemSize: Dp = 64.dp,
    cornerRadius: Dp = 12.dp,
    selectedBackgroundColor: Color = Color(0xFF01398D),//Color(0xFFFC6A68),
    unselectedBackgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    onImageClick: ((Int) -> Unit)? = null
) {

    val context = LocalContext.current
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón para agregar imagen
        Box(
            modifier = Modifier
                .size(itemSize)
                .background(
                    if(enabled) unselectedBackgroundColor else Color.LightGray.copy(alpha = 0.3f),
                    RoundedCornerShape(cornerRadius)
                )
                .border(1.dp, if(enabled) Color.LightGray.copy(alpha = 0.3f) else Color.LightGray, RoundedCornerShape(cornerRadius))
                .clickable (
                    enabled = enabled,
                    onClick = { onAddClick() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar imagen",
                tint = if(enabled) Color.LightGray.copy(alpha = 0.3f) else Color.LightGray,
                modifier = Modifier.size(itemSize * 0.5f)
            )
        }

        // Mostrar imágenes agregadas
        images.forEachIndexed { index, uri ->
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(RoundedCornerShape(cornerRadius))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(cornerRadius))
                    .then(
                        if (onImageClick != null)
                            Modifier.clickable { onImageClick(index) }
                        else Modifier
                    )
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Botón para eliminar imagen
                if(enabled){
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color(0x66606060), CircleShape)
                            .align(Alignment.TopEnd)
                            .clickable { onRemoveImage(uri) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar imagen",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun IconTextButton(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    cornerRadius: Dp,
    selectedBackgroundColor: Color =  Color(0xFF01398D),//Color(0xFFFC6A68),
    unselectedBackgroundColor: Color = Color.Transparent,
    selectedContentColor: Color = Color.White,
    unselectedContentColor: Color = Color.LightGray
) {
    val backgroundColor = if (selected) selectedBackgroundColor else unselectedBackgroundColor
    val contentColor = if (selected) selectedContentColor else unselectedContentColor

    Column(
        modifier = modifier
            .width(size)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius))
                .border(
                    width = 1.dp,
                    color = if (selected) selectedBackgroundColor else Color.LightGray,
                    shape = RoundedCornerShape(cornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(size * 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = if (selected) Color(0xFF01398D) else Color.LightGray,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}



@Composable
fun ThinOutlinedButton(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //val borderColor = if (selected) Color(0xFFFC6A68) else Color.LightGray
    //val containerColor = if (selected) Color(0xFFFC6A68) else Color.Transparent
    val borderColor = if (selected) Color(0xFF01398D) else Color.LightGray
    val containerColor = if (selected) Color(0xFF01398D) else Color.Transparent
    val contentColor = if (selected) Color.White  else Color.LightGray

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(32.dp) // altura baja, delgada
            .defaultMinSize(minWidth = 64.dp), // ancho mínimo
        shape = RoundedCornerShape(20.dp), // esquinas redondeadas
        border = BorderStroke(0.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp), // padding horizontal y vertical pequeño
        elevation = null // sin sombra para mantener minimalista
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FilterButtonsRow(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        options.forEach { option ->
            ThinOutlinedButton(
                text = option,
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) }
            )
        }
    }
}

@Composable
fun FilterBoxsRow(
    options: List<Pair<FailureType, ImageVector>>,
    selectedOption: FailureType,
    onOptionSelected: (FailureType) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    cornerRadius: Dp = 12.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        options.forEachIndexed { index, (option, icon)  ->
            IconTextButton(
                    icon = icon,
                text = option.name,
                size = size,
                cornerRadius = cornerRadius,
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) }
            )
            if (index < options.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun CustomButton2() {

    var selected by remember { mutableStateOf("Todos") }
    //val options = listOf("Todos", "Iniciado", "Finalizado", "Cerrado")
    var searchText by remember { mutableStateOf("") }

    /*CustomSearchText(
        text = searchText,
        onTextChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        searchIcon = Icons.Default.Search,
        placeholderText = "Buscar..."
    )*/
    /*val options = listOf(
        "Home" to Icons.Default.Home,
        "Settings" to Icons.Default.Settings,
        "Profile" to Icons.Default.Person
    )

    var selectedOption by remember { mutableStateOf("Home") }

    FilterBoxsRow(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    )*/
    //var images by remember { mutableStateOf(listOf<Uri>()) }
    val images = remember { mutableStateListOf<Uri>() }

    ImagePickerRow(
        images = images,
        onAddClick = {
            // Aquí abres galería o cámara y agregas la imagen a la lista
            // Por ejemplo, simular agregando una imagen dummy:
            // images = images + someImageBitmap
        },
        onRemoveImage = { uri ->
            images.remove(uri)
        }
    )


}