package org.retamia.lalafell.danmaku.parser

import org.retamia.lalafell.danmaku.model.Danmaku
import org.retamia.lalafell.danmaku.util.nextInt
import java.util.*

class TextParser {

    fun parse(source: String) : Danmaku {
        return Danmaku(source.replace('\n', ' '), 14, Random().nextInt(3, 6))
    }
}