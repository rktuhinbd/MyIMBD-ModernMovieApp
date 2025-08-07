package com.rkt.myimbdmodernmovieapp.ui.animation

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned

fun Modifier.flyToWishlistSource(
    flyState: FlyToWishlistState
) = this.onGloballyPositioned { layoutCoordinates ->
    val position = layoutCoordinates.localToWindow(Offset.Zero)
    flyState.startOffset = position
}

fun Modifier.flyToWishlistTarget(
    flyState: FlyToWishlistState
) = this.onGloballyPositioned { layoutCoordinates ->
    val position = layoutCoordinates.localToWindow(Offset.Zero)
    flyState.endOffset = position
}
