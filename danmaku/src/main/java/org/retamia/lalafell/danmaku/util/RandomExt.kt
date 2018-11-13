package org.retamia.lalafell.danmaku.util

import java.util.*

fun Random.nextInt(min: Int, max: Int): Int {
    return nextInt((max - min) + 1) + min
}