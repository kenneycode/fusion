package io.github.kenneycode.fusion.renderer

import android.opengl.GLES20
import android.opengl.GLES20.*
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool

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

class DisplayRenderer(vertexShader: String = Constants.MVP_VERTEX_SHADER, fragmentShader: String = Constants.SIMPLE_FRAGMENT_SHADER) : SimpleRenderer(vertexShader, fragmentShader) {

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
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, displayWidth, displayHeight)
    }

}