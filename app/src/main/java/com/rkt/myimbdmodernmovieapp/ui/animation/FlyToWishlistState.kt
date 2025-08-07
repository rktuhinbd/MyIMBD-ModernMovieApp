package com.rkt.myimbdmodernmovieapp.ui.animation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class FlyToWishlistState {
    var startOffset by mutableStateOf<Offset?>(null)
    var endOffset by mutableStateOf<Offset?>(null)
    var isAnimating by mutableStateOf(false)
}