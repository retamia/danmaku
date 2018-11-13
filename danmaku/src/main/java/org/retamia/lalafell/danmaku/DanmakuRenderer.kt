package org.retamia.lalafell.danmaku

import android.content.Context
import android.graphics.PointF
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import org.retamia.lalafell.danmaku.model.Danmaku
import org.retamia.lalafell.danmaku.sprite.DanmakuSprite
import org.retamia.lalafell.danmaku.sprite.DanmakuSpriteSpawn
import org.retamia.lalafell.danmaku.util.TextureHelper
import org.retamia.lalafell.danmaku.util.nextInt
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class DanmakuRenderer (private val context: Context) : GLSurfaceView.Renderer {

    companion object {
        const val TAG = "DanmakuRenderer"
    }

    private val projectionMatrix = FloatArray(16)
    private var textureId: Int = -1

    private var startTime = SystemClock.uptimeMillis()

    private var sprites = mutableListOf<DanmakuSprite>()
    private val spawns: List<DanmakuSpriteSpawn>
    private val spawnCount = 24

    init {
        spawns = List(spawnCount) { index ->
            DanmakuSpriteSpawn(context, PointF(0.0f, 0.0f))
        }
    }

    override fun onDrawFrame(p0: GL10?) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val updateTime = SystemClock.uptimeMillis()
        val deltaTime = (updateTime - startTime).toFloat() / 1000
        startTime = updateTime

        drawDanmaku(deltaTime)
    }

    override fun onSurfaceChanged(gl10: GL10 , width: Int, height: Int) {

        DanmakuEnv.border.left = 0
        DanmakuEnv.border.top = 0
        DanmakuEnv.border.right = width
        DanmakuEnv.border.bottom = height

        glViewport(0, 0, width, height)
        Matrix.orthoM(projectionMatrix, 0,
                0.0f,
                width.toFloat(),
                0.0f,
                height.toFloat(), 0.0f, 10.0f)

        startTime = SystemClock.uptimeMillis()

        val scopeHeight = height * 3 / 4
        val safeHeight = height - scopeHeight
        val spawnHeight = scopeHeight / spawnCount

        spawns.forEachIndexed { index, spawn ->
            spawn.position.x = DanmakuEnv.border.width().toFloat()
            spawn.position.y = spawnHeight.toFloat() * (spawnCount - index) + safeHeight
        }

        //onTest()
    }

    override fun onSurfaceCreated(gl10: GL10, eglConfig: EGLConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // 启用混合模式
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    fun addDanmaku(danmaku: Danmaku) {
        val randomIndex = Random().nextInt(0, spawnCount - 1)
        sprites.addAll(spawns[randomIndex].spawn(danmaku))
    }

    private fun onTest() {
        sprites.addAll(spawns[5].spawn(Danmaku(
                "央视网消息:全球首个以进口 为主题的国家级博览会一一中国国际进口博览会, 的2800多家企业、国内外超过15万采购商将齐聚 11月5日将在上海举办,来自130多个国家和地区 上海,共赴这场史无前例的贸易盛宴。 改革开放40年来,中国开放不断扩大,从未停下 脚步。党的十八大以来,以习近平同志为核心的党 中央总揽战略全局,坚持对外开放基本国策,推进 对外开放理论和实践创新,对外开放",
                14,
                5
        )))
    }

    fun clearAllDanmaku() {
        sprites.clear()
    }

    fun release() {
        // bitmap 在Android 2.3后 不用去手动调用 recycle
    }

    private fun drawDanmaku(deltaTime: Float) {

        val it = sprites.iterator()

        while (it.hasNext()) {
            val danmakuSprite = it.next()

            danmakuSprite.useProgram()

            if (textureId == -1) {
                textureId = TextureHelper.loadTexture(danmakuSprite.fontBitmap)
            } else {
                TextureHelper.setTexutreData(textureId, danmakuSprite.fontBitmap)
            }

            danmakuSprite.setUniforms(projectionMatrix, deltaTime, textureId)
            danmakuSprite.draw()

            if (!danmakuSprite.isVisible()) {
                it.remove()
            }
        }
    }
}