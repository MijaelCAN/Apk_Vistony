package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
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
    suportingContent: String="",
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

    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingRes)
        ,shape = RoundedCornerShape(8.dp)
        //, border = BorderStroke(1.dp, Color.LightGray) // evita que tape el fondo del Surface
        , elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        if (isUsedLeadingContent&&isUsedTrailingContent) {
            ListItem(
                overlineContent = {
                    Text(
                        text = overLineContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.background(Color.Transparent), // evita que tape el fondo del Surface
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = suportingContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingContent = {
                    trailingContent()
                },
                leadingContent = {
                    leadingContent()
                }
            )
        } else if( !isUsedLeadingContent && isUsedTrailingContent) {
            ListItem(
                overlineContent = {
                    Text(
                        text = overLineContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.background(Color.Transparent), // evita que tape el fondo del Surface
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = suportingContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.background(Color.Transparent), // evita que tape el fondo del Surface
                headlineContent = {
                    Text(
                        text = headLineContent,
                        style = MaterialTheme.typography.titleMedium,
                        //maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = suportingContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
            )
        }
    }
}