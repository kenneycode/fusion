package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.framebuffer.FrameBufferCache

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
        val outputWidth = (scale * (specifiedOutputWidth.takeIf { it > 0 } ?: inputFrameBuffers.first().width)).toInt()
        val outputHeight = (scale * (specifiedOutputHeight.takeIf { it > 0 } ?: inputFrameBuffers.first().height)).toInt()
        outputFrameBuffer = outputFrameBuffer ?: FrameBufferCache.obtainFrameBuffer(outputWidth, outputHeight).apply {
            bind(outputWidth, outputHeight)
        }
    }

}