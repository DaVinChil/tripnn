package ru.nn.tripnn.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.nn.tripnn.ui.common.card.CARD_WIDTH
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteCarousel(
    modifier: Modifier = Modifier,
    count: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    pageSpacing: Dp = 0.dp,
    key: (index: Int) -> Any,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    if (count == 0) return

    val state = rememberPagerState(pageCount = { 10000 }, initialPage = 5000)
    val flingBehavior = PagerDefaults.flingBehavior(
        state = state,
        pagerSnapDistance = PagerSnapDistance.atMost(10),
    )
    val scaleFactor = 3f

    HorizontalPager(
        modifier = modifier,
        state = state,
        pageSpacing = pageSpacing,
        pageSize = pageSize,
        contentPadding = contentPadding,
        flingBehavior = flingBehavior,
        key = { key(it % count) }
    ) { index ->
        val scale = if (state.currentPage == index) {
            1 - state.currentPageOffsetFraction.absoluteValue / scaleFactor
        } else if ((state.currentPage - index).absoluteValue == 1 &&
            (state.currentPage < index && state.currentPageOffsetFraction > 0 ||
                    state.currentPage > index && state.currentPageOffsetFraction < 0)
        ) {
            (scaleFactor - 1) / scaleFactor + state.currentPageOffsetFraction.absoluteValue / scaleFactor
        } else {
            (scaleFactor - 1) / scaleFactor
        }

        val offset = (CARD_WIDTH - CARD_WIDTH * scale) / 2

        Box(
            modifier = Modifier
                .offset(
                    x = offset * if (state.currentPage < index) -1
                    else if (state.currentPage > index) 1
                    else if (state.currentPageOffsetFraction <= 0) -1
                    else 1
                )
                .scale(scale = scale)
        ) {
            pageContent(index % count)
        }
    }
}