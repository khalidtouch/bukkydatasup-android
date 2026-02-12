package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.navigation.TopLevelDestinations
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.PurplishBlue10


@Composable
fun PoshNavigationBar(
    modifier: Modifier = Modifier,
    selectedItemIndex: Int,
    onClick: (index: Int) -> Unit,
    destinations: List<TopLevelDestinations> = TopLevelDestinations.entries,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(
        indicatorColor = Color.Transparent,
        selectedTextColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedTextColor = MaterialTheme.colorScheme.outline,
        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.outline
    ),
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val shape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp,
        bottomStart = 0.dp, bottomEnd = 0.dp,
    )

    NavigationBar(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, shape),
        containerColor = containerColor,
    ) {
        destinations.forEachIndexed { index, destination ->
            val selected = index == selectedItemIndex

            NavigationBarItem(
                colors = colors,
                selected = selected,
                onClick = { onClick(index) },
                icon = {
                    Icon(
                        painterResource(id = destination.icon),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = destination.title,
                    )
                }
            )
        }
    }
}

@Composable
fun PoshPagingNavigationBar(
    modifier: Modifier = Modifier,
    currentPage: Int?,
    totalPages: Long,
    onPrev: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
) {
    var isPrevEnabled by remember { mutableStateOf(false) }
    var isNextEnabled by remember { mutableStateOf(false) }
    
    val buttonContainerColor = MaterialTheme.colorScheme.onPrimary.copy(0.2f)
    val buttonContentColor = PurplishBlue10
    val colors = IconButtonDefaults.iconButtonColors(
        containerColor = buttonContainerColor,
        contentColor = buttonContentColor,
    )
    val invisibleColors = IconButtonDefaults.iconButtonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
    )
    val textStyle = MaterialTheme.typography.labelLarge
    
    val calculatePrevPage: (Int?, Long) -> Boolean = { current, total ->
        current != null && current > 1 && total > 1 && current <= total
    }

    val calculateNextPage: (Int?, Long) -> Boolean = { current, total ->
        current != null && total > 1 && current < total
    }

    LaunchedEffect(currentPage, totalPages) {
        isPrevEnabled = calculatePrevPage.invoke(currentPage, totalPages)
        isNextEnabled = calculateNextPage.invoke(currentPage, totalPages)
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        Row(
            modifier
                .padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            onPrev?.let { p ->
                IconButton(
                    colors = if(isPrevEnabled) colors else invisibleColors,
                    onClick = p,
                    enabled = isPrevEnabled,
                ){
                    Icon(
                        painterResource(id = PoshIcon.ArrowLeft),
                        contentDescription = null,
                        tint = if(isPrevEnabled) colors.contentColor else Color.Transparent,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            currentPage?.let { current ->
                Spacer(Modifier.width(4.dp))
                Box(
                    Modifier
                        .border(
                            1.dp,
                            colors.contentColor,
                            MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$current/$totalPages",
                        style = textStyle.copy(
                            colors.contentColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
            }
            onNext?.let { n ->
                IconButton(
                    colors = if(isNextEnabled) colors else invisibleColors,
                    onClick = n,
                    enabled = isNextEnabled,
                ){
                    Icon(
                        painterResource(id = PoshIcon.ArrowRight),
                        contentDescription = null,
                        tint = if(isNextEnabled) colors.contentColor else Color.Transparent,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}


@Composable
@Preview
private fun PoshNavigationBarPreviews() {
    MainAppTheme {
        PoshNavigationBar(
            modifier = Modifier,
            selectedItemIndex = 0,
            onClick = {},
            destinations = TopLevelDestinations.entries,
        )
    }
}


@Composable
@Preview
private fun PoshPagingNavigationBarPreview() {
    MainAppTheme {
        PoshPagingNavigationBar(
            currentPage = 2,
            totalPages = 20,
            onPrev = {},
            onNext = {}
        )
    }
}

