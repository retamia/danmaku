package org.retamia.lalafell.danmaku

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import org.retamia.lalafell.danmaku.parser.TextParser


class DanmakuView : GLSurfaceView {

    private var renderer: DanmakuRenderer


    constructor(context: Context, attr: AttributeSet) : super(context, attr) {

        renderer = DanmakuRenderer(context)


        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        setRenderer(renderer)

        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun start() {
        renderMode = RENDERMODE_CONTINUOUSLY
        requestRender()
    }

    fun stop() {
        renderMode = RENDERMODE_WHEN_DIRTY
        renderer.clearAllDanmaku()
        requestRender()
    }

    fun shootDanmaku(source: String, danmakuParser: TextParser) {
        queueEvent {
            renderer.addDanmaku(danmakuParser.parse(source))
        }

    }

    fun release() {
        renderer.release()
    }
}