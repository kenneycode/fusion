package io.github.kenneycode.fusion.renderer

import android.opengl.GLES20
import io.github.kenneycode.fusion.framebuffer.FrameBufferCache

import android.opengl.GLES20.glViewport

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

    companion object {

        const val KEY_DISPLAY_WIDTH = "KEY_DISPLAY_WIDTH"
        const val KEY_DISPLAY_HEIGHT = "KEY_DISPLAY_HEIGHT"

    }

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    fun setDisplaySize(width: Int, height: Int) {
        displayWidth = width
        displayHeight = height
    }

    override fun update(data: MutableMap<String, Any>): Boolean {
        setDisplaySize(data[KEY_DISPLAY_WIDTH] as Int, data[KEY_DISPLAY_HEIGHT] as Int)
        return true
    }

    override fun bindOutput() {
        if (outputFrameBuffer == null) {
            outputFrameBuffer = FrameBufferCache.obtainFrameBuffer()
        }
        outputFrameBuffer!!.bind()
        glViewport(0, 0, displayWidth, displayHeight)
    }

}