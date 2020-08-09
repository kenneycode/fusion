package io.github.kenneycode.fusion.renderer

import android.opengl.GLES20
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.framebuffer.FrameBufferPool
import io.github.kenneycode.fusion.texture.TexturePool

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 缩放renderer
 *
 */

class ScaleRenderer : SimpleRenderer() {

    // 缩放比例
    private var scale = 1.0f

    override fun bindOutput() {
        val outputWidth = (scale * input.first().width).toInt()
        val outputHeight = (scale * input.first().height).toInt()
        getOutput()?.let { output ->
            val frameBuffer = FrameBufferPool.obtainFrameBuffer()
            if (output.width != outputWidth || output.height != outputHeight) {
                output.decreaseRef()
                val texture = TexturePool.obtainTexture(outputWidth, outputHeight).apply {
                    setOutput(this)
                }
                frameBuffer.attachTexture(texture)
            } else {
                frameBuffer.attachTexture(output)
            }
            frameBuffer.bind()
            glCheck { GLES20.glViewport(0, 0, output.width, output.height) }
        }
    }

    fun setScale(scale: Float) {
        this.scale = scale
    }

}