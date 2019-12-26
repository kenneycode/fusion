package io.github.kenneycode.fusion.renderer

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
    var scale = 1.0f

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
        }
    }

}