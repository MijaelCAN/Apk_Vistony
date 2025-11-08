package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vistony.app.ui.theme.theme.Dimensions
import com.vistony.app.ui.theme.theme.onPrimaryLight

@Composable
fun <T, K> GroupedLazyColumn(
    items: List<T>,
    groupBy: (T) -> K,
    headerContent: @Composable (K) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val groupedItems = items.groupBy(groupBy)

    LazyColumn {
        groupedItems.forEach { (groupKey, itemsForGroup) ->
            item {
                headerContent(groupKey)
            }
            items(itemsForGroup) { item ->
                itemContent(item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ListItemM3(
    headLineContent: String="",
    suportingContent: @Composable () -> Unit = {},
    trailingContentClick: () -> Unit,
    modifier: Modifier = Modifier,
    overLineContent:String="",
    leadingContet:String="",
    isUsedLeadingContent: Boolean = true,
    isUsedTrailingContent: Boolean = true,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val paddingRes = Dimensions.getPadding(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    /*Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingRes).background(Color.Transparent) // evita que tape el fondo del Surface
        ,shape = RoundedCornerShape(8.dp)
        //, border = BorderStroke(1.dp, Color.LightGray) // evita que tape el fondo del Surface
        , elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        , colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.44f), contentColor = Color.Black)

    ){*/
        if (isUsedLeadingContent&&isUsedTrailingContent) {
            ListItem(
                overlineContent = {
                    Text(
                        text = overLineContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        //color = Color.Black

                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color(0xFFF7F8FB),
                    headlineColor = MaterialTheme.colorScheme.onSurface,
                    supportingColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    overlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = paddingRes).background(Color.LightGray),
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                    )
                },
                supportingContent = {
                    suportingContent()
                },
                trailingContent = {
                    trailingContent()
                },
                leadingContent = {
                    leadingContent()
                },

            )
        } else if( !isUsedLeadingContent && isUsedTrailingContent) {
            ListItem(
                overlineContent = {
                    Text(
                        text = overLineContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                        //color = Color.Black
                    )
                },
                modifier = Modifier.padding(horizontal = paddingRes).background(Color.LightGray), // evita que tape el fondo del Surface // evita que tape el fondo del Surface
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color(0xFFF7F8FB),
                    headlineColor = MaterialTheme.colorScheme.onSurface,
                    supportingColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    overlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                ),
                supportingContent = {
                    suportingContent()
                },
                trailingContent = {
                    trailingContent()
                },
            )
        } else if( !isUsedLeadingContent && !isUsedTrailingContent) {
            ListItem(
                overlineContent = {
                    Text(
                        text = overLineContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                        //color = Color.Black
                    )
                },
                modifier = Modifier.padding(horizontal = paddingRes).background(Color.LightGray), // evita que tape el fondo del Surface
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        //maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    /*Text(
                        text = suportingContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )*/
                    suportingContent()
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color(0xFFF7F8FB),
                    headlineColor = MaterialTheme.colorScheme.onSurface,
                    supportingColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    overlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    //}
}