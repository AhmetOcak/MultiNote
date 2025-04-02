package com.ahmetocak.multinote.utils

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import com.ahmetocak.multinote.model.NoteTag

@Composable
fun LazyStaggeredGridState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }
}

fun NoteTag.toPublicName(): String {
    return when (this) {
        NoteTag.DAILY -> "Daily"
        NoteTag.BUSINESS -> "Business"
        NoteTag.EDUCATION -> "Education"
        NoteTag.NONE -> "No tag"
    }
}