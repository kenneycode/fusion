package io.github.kenneycode.fusion.inputsource

import android.graphics.Bitmap
import android.opengl.GLES20.GL_TEXTURE_2D

import io.github.kenneycode.fusion.context.GLContextPool
import io.github.kenneycode.fusion.process.RenderPipeline
import io.github.kenneycode.fusion.texture.Texture
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

class FusionImage(private val image: Bitmap) : RenderPipeline.Input {

    override fun onInit() {
    }

    override fun onUpdate(data: MutableMap<String, Any>) {
    }

    override fun getInputTexture(): Texture {
        val buffer = ByteBuffer.allocate(image.width * image.height * 4)
        image.copyPixelsToBuffer(buffer)
        buffer.position(0)
        return TexturePool.obtainTexture(image.width, image.height).apply {
            retain = true
            setData(buffer)
        }
    }

}