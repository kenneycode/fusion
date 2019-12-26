package io.github.kenneycode.fusion.inputsource

import android.graphics.Bitmap
import android.opengl.GLES20.GL_TEXTURE_2D

import io.github.kenneycode.fusion.context.GLContextPool
import io.github.kenneycode.fusion.texture.TexturePool
import java.nio.ByteBuffer

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
     *
     */
    fun process(data: MutableMap<String, Any> = mutableMapOf()) {
        GLContextPool.obtainGLContext(this)?.let { glContext ->
            glContext.runOnGLContext {
                val buffer = ByteBuffer.allocate(image.width * image.height * 4)
                image.copyPixelsToBuffer(buffer)
                buffer.position(0)
                val texture = TexturePool.obtainTexture(image.width, image.height).apply {
                    retain = true
                    setData(buffer)
                }
                notifyInit()
                notifyInputReady(data, texture)
            }
        }
    }

}