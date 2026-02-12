package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kxtdev.bukkydatasup.common.models.Advertisement
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoshAd(
    ads: List<Advertisement>
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        val pagerState = rememberPagerState(pageCount = {ads.size})
        val delayMs = 3000L
        val maxWidth = LocalConfiguration.current.screenWidthDp.dp
        val width = maxWidth - 72.dp

        LaunchedEffect(pagerState,ads.size) {
            while (ads.isNotEmpty()) {
                delay(delayMs)
                val nextPage = (pagerState.currentPage + 1) % ads.size
                pagerState.scrollToPage(nextPage)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.width(maxWidth),
            pageSize = PageSize.Fixed(width)
        ) { page ->
            PoshAdImage(ad = ads[page], width)
        }

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .wrapContentHeight()
                .width(maxWidth)
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.End
        ) {
            val background = MaterialTheme.colorScheme.primaryContainer.copy(0.2f)
            val selectedBackground = MaterialTheme.colorScheme.primaryContainer

            repeat(pagerState.pageCount) { iteration ->
                val selected = iteration == pagerState.currentPage
                val bg by animateColorAsState(targetValue = if (selected) selectedBackground else background,
                    label = "colorAnimation"
                )

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(bg)
                        .size(6.dp)
                )
            }
        }
    }
}


@Composable
private fun PoshAdImage(
    ad: Advertisement,
    width: Dp,
) {
    val shape = RoundedCornerShape(12.dp)
    val height = 102.dp
    val padding = 8.dp
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ad.formattedImage)
            .crossfade(true)
            .build()
    )

    Box(
        Modifier
            .width(width)
            .height(height)
            .clip(shape)
            .padding(start = padding, end = padding),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = ad.description.orEmpty(),
            modifier = Modifier
                .width(width)
                .clip(shape)
                .height(height),
            contentScale = ContentScale.FillWidth
        )
    }

}

@ThemePreviews
@Composable
private fun PoshAdImagePreviews() {
    MainAppTheme {
        PoshAdImage(
            Advertisement(
                id = 1,
                image = "http://res.cloudinary.com/da8ylxwqa/image/upload/v1750153288/damgtuhasrvbekfcj5g5.png",
                description = "step up"
            ),
            width = 220.dp,
        )
    }
}

@ThemePreviews
@Composable
private fun PoshAdPreviews() {
    MainAppTheme {
        PoshAd(listOf())
    }
}