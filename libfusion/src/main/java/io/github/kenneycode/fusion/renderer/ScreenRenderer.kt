package io.github.kenneycode.fusion.renderer

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

class ScreenRenderer : SimpleRenderer() {

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    fun setDisplaySize(width: Int, height: Int) {
        this.displayWidth = width
        this.displayHeight = height
    }

    override fun bindOutput() {
        if (outputFrameBuffer == null) {
            outputFrameBuffer = FrameBufferCache.obtainFrameBuffer().apply {
                bind()
            }
        }
        glViewport(0, 0, displayWidth, displayHeight)
    }

}