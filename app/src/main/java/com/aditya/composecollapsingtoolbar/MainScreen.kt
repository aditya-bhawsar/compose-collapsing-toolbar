package com.aditya.composecollapsingtoolbar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aditya.composecollapsingtoolbar.components.MainScreenFab
import com.aditya.composecollapsingtoolbar.components.MainScreenList
import com.aditya.composecollapsingtoolbar.components.MainScreenTopBar

@ExperimentalAnimationApi
@Composable
fun MainScreen() {

    val topbarHeight = 64.dp
    val topbarHeightPx = with(LocalDensity.current) { topbarHeight.roundToPx().toFloat() }
    var topbarOffsetState by remember { mutableStateOf(0f) }
    var showText by remember { mutableStateOf(true) }
    val topbarOffsetHeightAnim by animateFloatAsState(
        targetValue = topbarOffsetState,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearOutSlowInEasing
        )
    )

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val change = available.y
                val changedOffset = topbarOffsetState + change
                topbarOffsetState = changedOffset.coerceIn(-topbarHeightPx, 0f)
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (consumed.y < -15f) {
                    showText = false
                }
                if (consumed.y > 15f) {
                    showText = true
                }
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        MainScreenList(topbarHeight = topbarHeight)

        MainScreenTopBar(
            topbarHeight = topbarHeight,
            topbarOffsetHeightAnim = topbarOffsetHeightAnim
        )

        MainScreenFab(textVisible = showText)
    }
}
