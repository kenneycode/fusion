package io.github.kenneycode.fusion.inputsource

import android.graphics.Bitmap

import io.github.kenneycode.fusion.context.GLContextPool
import io.github.kenneycode.fusion.framebuffer.FrameBufferCache
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 图片输入源
 *
 */

class FusionImageSource(private val image: Bitmap) : InputSource() {

    /**
     *
     * 开始处理
     *
     * @param data 传入的数据
     */
    fun process(data: MutableMap<String, Any> = mutableMapOf()) {
        GLContextPool.obtainGLContext(this)?.let { glContext ->
            glContext.runOnGLContext {
                val imageTexture = GLUtil.createTexture()
                GLUtil.bitmap2Texture(imageTexture, image)
                val frameBuffer = FrameBufferCache.obtainFrameBuffer().apply {
                    width = image.width
                    height = image.height
                    texture = imageTexture
                }
                notifyInit()
                notifyInputReady(data, frameBuffer)
            }
        }
    }

}