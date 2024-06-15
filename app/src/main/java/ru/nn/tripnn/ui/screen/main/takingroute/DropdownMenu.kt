package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import ru.nn.tripnn.ui.common.shadow
import ru.nn.tripnn.ui.screen.main.takingroute.MenuPosition.Horizontal
import ru.nn.tripnn.ui.screen.main.takingroute.MenuPosition.Vertical
import ru.nn.tripnn.ui.theme.TripNnTheme

@Composable
fun DropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(focusable = true),
    borderRadius: Dp,
    containerColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = expanded

    if (expandedState.currentState || expandedState.targetState) {
        val density = LocalDensity.current
        val popupPositionProvider = remember(offset, density) {
            DropdownMenuPositionProvider(offset, density)
        }

        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties
        ) {
            DropdownMenuContent(
                scrollState = scrollState,
                modifier = modifier,
                content = content,
                borderRadius = borderRadius,
                containerColor = containerColor
            )
        }
    }
}

@Composable
internal fun DropdownMenuContent(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
    borderRadius: Dp,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .shadow(color = TripNnTheme.colorScheme.shadow, borderRadius = borderRadius, blurRadius = 8.dp)
            .clip(shape = RoundedCornerShape(borderRadius))
            .background(containerColor)
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .verticalScroll(scrollState),
            content = content
        )
    }
}


@Immutable
internal data class DropdownMenuPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPositionCalculated: (anchorBounds: IntRect, menuBounds: IntRect) -> Unit = { _, _ -> }
) : PopupPositionProvider {
    // Horizontal position
    // Horizontal position
    private val startToAnchorStart: Horizontal = MenuPosition.startToAnchorStart()
    private val endToAnchorEnd: Horizontal = MenuPosition.endToAnchorEnd()
    private val leftToWindowLeft: Horizontal = MenuPosition.leftToWindowLeft()
    private val rightToWindowRight: Horizontal = MenuPosition.rightToWindowRight()

    // Vertical position
    // Vertical position
    private val topToAnchorBottom: Vertical = MenuPosition.topToAnchorBottom()
    private val bottomToAnchorTop: Vertical = MenuPosition.bottomToAnchorTop()
    private val centerToAnchorTop: Vertical = MenuPosition.centerToAnchorTop()
    private val topToWindowTop: Vertical = MenuPosition.topToWindowTop()
    private val bottomToWindowBottom: Vertical = MenuPosition.bottomToWindowBottom()

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val xCandidates = listOf(
            startToAnchorStart,
            endToAnchorEnd,
            if (anchorBounds.center.x < windowSize.width / 2) {
                leftToWindowLeft
            } else {
                rightToWindowRight
            }
        ).fastMap {
            it.position(
                anchorBounds = anchorBounds,
                windowSize = windowSize,
                menuWidth = popupContentSize.width,
                layoutDirection = layoutDirection
            )
        }
        val x = xCandidates.fastFirstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: xCandidates.last()

        val yCandidates = listOf(
            topToAnchorBottom,
            bottomToAnchorTop,
            centerToAnchorTop,
            if (anchorBounds.center.y < windowSize.height / 2) {
                topToWindowTop
            } else {
                bottomToWindowBottom
            }
        ).fastMap {
            it.position(
                anchorBounds = anchorBounds,
                windowSize = windowSize,
                menuHeight = popupContentSize.height
            )
        }
        val y = yCandidates.fastFirstOrNull {
            it >= 0 &&
                    it + popupContentSize.height <= windowSize.height
        } ?: yCandidates.last()

        val menuOffset = IntOffset(x, y)
        onPositionCalculated(
            /* anchorBounds = */anchorBounds,
            /* menuBounds = */IntRect(offset = menuOffset, size = popupContentSize)
        )
        return menuOffset
    }
}

@Stable
internal object MenuPosition {

    @Stable
    fun interface Vertical {
        fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuHeight: Int,
        ): Int
    }

    @Stable
    fun interface Horizontal {
        fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuWidth: Int,
            layoutDirection: LayoutDirection,
        ): Int
    }

    fun startToAnchorStart(offset: Int = 0): Horizontal =
        AnchorAlignmentOffsetPosition.Horizontal(
            menuAlignment = Alignment.Start,
            anchorAlignment = Alignment.Start,
            offset = offset,
        )

    fun endToAnchorEnd(offset: Int = 0): Horizontal =
        AnchorAlignmentOffsetPosition.Horizontal(
            menuAlignment = Alignment.End,
            anchorAlignment = Alignment.End,
            offset = offset,
        )

    fun leftToWindowLeft(margin: Int = 0): Horizontal =
        WindowAlignmentMarginPosition.Horizontal(
            alignment = AbsoluteAlignment.Left,
            margin = margin,
        )

    fun rightToWindowRight(margin: Int = 0): Horizontal =
        WindowAlignmentMarginPosition.Horizontal(
            alignment = AbsoluteAlignment.Right,
            margin = margin,
        )

    fun topToAnchorBottom(offset: Int = 0): Vertical =
        AnchorAlignmentOffsetPosition.Vertical(
            menuAlignment = Alignment.Top,
            anchorAlignment = Alignment.Bottom,
            offset = offset,
        )

    fun bottomToAnchorTop(offset: Int = 0): Vertical =
        AnchorAlignmentOffsetPosition.Vertical(
            menuAlignment = Alignment.Bottom,
            anchorAlignment = Alignment.Top,
            offset = offset,
        )

    fun centerToAnchorTop(offset: Int = 0): Vertical =
        AnchorAlignmentOffsetPosition.Vertical(
            menuAlignment = Alignment.CenterVertically,
            anchorAlignment = Alignment.Top,
            offset = offset,
        )

    fun topToWindowTop(margin: Int = 0): Vertical =
        WindowAlignmentMarginPosition.Vertical(
            alignment = Alignment.Top,
            margin = margin,
        )

    fun bottomToWindowBottom(margin: Int = 0): Vertical =
        WindowAlignmentMarginPosition.Vertical(
            alignment = Alignment.Bottom,
            margin = margin,
        )
}

@Immutable
internal object AnchorAlignmentOffsetPosition {

    @Immutable
    data class Horizontal(
        private val menuAlignment: Alignment.Horizontal,
        private val anchorAlignment: Alignment.Horizontal,
        private val offset: Int,
    ) : MenuPosition.Horizontal {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuWidth: Int,
            layoutDirection: LayoutDirection,
        ): Int {
            val anchorAlignmentOffset = anchorAlignment.align(
                size = 0,
                space = anchorBounds.width,
                layoutDirection = layoutDirection,
            )
            val menuAlignmentOffset = -menuAlignment.align(
                size = 0,
                space = menuWidth,
                layoutDirection,
            )
            val resolvedOffset = if (layoutDirection == LayoutDirection.Ltr) offset else -offset
            return anchorBounds.left + anchorAlignmentOffset + menuAlignmentOffset + resolvedOffset
        }
    }

    @Immutable
    data class Vertical(
        private val menuAlignment: Alignment.Vertical,
        private val anchorAlignment: Alignment.Vertical,
        private val offset: Int,
    ) : MenuPosition.Vertical {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuHeight: Int,
        ): Int {
            val anchorAlignmentOffset = anchorAlignment.align(
                size = 0,
                space = anchorBounds.height,
            )
            val menuAlignmentOffset = -menuAlignment.align(
                size = 0,
                space = menuHeight,
            )
            return anchorBounds.top + anchorAlignmentOffset + menuAlignmentOffset + offset
        }
    }
}

@Immutable
internal object WindowAlignmentMarginPosition {
    @Immutable
    data class Horizontal(
        private val alignment: Alignment.Horizontal,
        private val margin: Int,
    ) : MenuPosition.Horizontal {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuWidth: Int,
            layoutDirection: LayoutDirection,
        ): Int {
            if (menuWidth >= windowSize.width - 2 * margin) {
                return Alignment.CenterHorizontally.align(
                    size = menuWidth,
                    space = windowSize.width,
                    layoutDirection = layoutDirection,
                )
            }
            val x = alignment.align(
                size = menuWidth,
                space = windowSize.width,
                layoutDirection = layoutDirection,
            )
            return x.coerceIn(margin, windowSize.width - margin - menuWidth)
        }
    }

    @Immutable
    data class Vertical(
        private val alignment: Alignment.Vertical,
        private val margin: Int,
    ) : MenuPosition.Vertical {
        override fun position(
            anchorBounds: IntRect,
            windowSize: IntSize,
            menuHeight: Int,
        ): Int {
            if (menuHeight >= windowSize.height - 2 * margin) {
                return Alignment.CenterVertically.align(
                    size = menuHeight,
                    space = windowSize.height,
                )
            }
            val y = alignment.align(
                size = menuHeight,
                space = windowSize.height,
            )
            return y.coerceIn(margin, windowSize.height - margin - menuHeight)
        }
    }
}