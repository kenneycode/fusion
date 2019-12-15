package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.framebuffer.FrameBufferCache

import android.opengl.GLES20.glViewport
import io.github.kenneycode.fusion.common.DataKeys

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 显示渲染器，渲染到0号FrameBuffer，通常是屏幕
 *
 */

class DisplayRenderer : SimpleRenderer() {

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    fun setDisplaySize(width: Int, height: Int) {
        displayWidth = width
        displayHeight = height
    }

    override fun update(data: MutableMap<String, Any>): Boolean {
        setDisplaySize(data[DataKeys.KEY_DISPLAY_WIDTH] as Int, data[DataKeys.KEY_DISPLAY_HEIGHT] as Int)
        return true
    }

    override fun bindOutput() {
        outputFrameBuffer = (outputFrameBuffer ?: FrameBufferCache.obtainFrameBuffer()).apply {
            bind()
        }
        glViewport(0, 0, displayWidth, displayHeight)
    }

}