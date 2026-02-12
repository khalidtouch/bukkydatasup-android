package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.design.exts.PoshColumn
import kotlinx.coroutines.delay

@Composable
fun PoshTextGroup(
    modifier: Modifier = Modifier,
    items: List<String>,
) {
    val contentColor = MaterialTheme.colorScheme.onTertiary
    val textStyle = MaterialTheme.typography.headlineLarge
    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedIndex) {
        delay(2000)
        if(selectedIndex >= (items.size - 1)) {
            selectedIndex = 0
        } else {
            selectedIndex++
        }
    }

    PoshColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items.forEachIndexed { index, s ->
            val selected = index == selectedIndex
            val color = if(selected) contentColor else contentColor.copy(0.2f)
            val weight = if(selected) FontWeight.Bold else FontWeight.Normal

            Text(
                text = s,
                style = textStyle.copy(color, fontWeight = weight),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}