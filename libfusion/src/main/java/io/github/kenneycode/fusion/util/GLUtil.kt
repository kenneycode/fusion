package io.github.kenneycode.fusion.util

import android.graphics.Bitmap

import java.nio.ByteBuffer

import io.github.kenneycode.fusion.framebuffer.FrameBuffer

import android.opengl.GLES20.GL_CLAMP_TO_EDGE
import android.opengl.GLES20.GL_COLOR_ATTACHMENT0
import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_RGBA
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.GL_TEXTURE_WRAP_S
import android.opengl.GLES20.GL_TEXTURE_WRAP_T
import android.opengl.GLES20.GL_UNSIGNED_BYTE
import android.opengl.GLES20.glBindFramebuffer
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glDeleteFramebuffers
import android.opengl.GLES20.glDeleteTextures
import android.opengl.GLES20.glFramebufferTexture2D
import android.opengl.GLES20.glGenFramebuffers
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glIsTexture
import android.opengl.GLES20.glReadPixels
import android.opengl.GLES20.glTexImage2D
import android.opengl.GLES20.glTexParameteri

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * GL util类
 *
 */

class GLUtil {

    companion object {

        /**
         *
         * 创建一个纹理
         *
         * @return 纹理id
         */
        fun createTexture(): Int {
            val textures = IntArray(1)
            glGenTextures(1, textures, 0)
            glBindTexture(GL_TEXTURE_2D, textures[0])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glBindTexture(GL_TEXTURE_2D, 0)
            return textures[0]
        }

        /**
         *
         * 删除一个纹理
         *
         * @param texture 纹理id
         */
        fun deleteTexture(texture: Int) {
            if (glIsTexture(texture)) {
                val temp = intArrayOf(texture)
                glDeleteTextures(1, temp, 0)
            }
        }

        /**
         *
         * 创建一个FrameBuffer
         *
         * @return frame buffer id
         */
        fun createFrameBuffer(): Int {
            val frameBuffer = IntArray(1)
            glGenFramebuffers(1, frameBuffer, 0)
            return frameBuffer[0]
        }

        /**
         *
         * 删除一个FrameBuffer
         *
         * @param frameBuffer frame buffer id
         */
        fun deleteFrameBuffer(frameBuffer: Int) {
            val temp = intArrayOf(frameBuffer)
            glDeleteFramebuffers(1, temp, 0)
        }

        /**
         *
         * 将bitmap转换为纹理
         *
         * @param texture 纹理id
         * @param bitmap bitmap
         *
         */
        fun bitmap2Texture(texture: Int, bitmap: Bitmap) {
            glBindTexture(GL_TEXTURE_2D, texture)
            val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
            bitmap.copyPixelsToBuffer(b)
            b.position(0)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA, bitmap.width,
                    bitmap.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, b)
        }

        /**
         *
         * 将纹理转换为bitmap
         *
         * @param texture 纹理id
         * @param width 宽度
         * @param height 高度
         *
         */
        fun texture2Bitmap(texture: Int, width: Int, height: Int): Bitmap {
            val buffer = ByteBuffer.allocate(width * height * 4)
            val frameBuffers = IntArray(1)
            glGenFramebuffers(frameBuffers.size, frameBuffers, 0)
            glBindTexture(GL_TEXTURE_2D, texture)
            glBindFramebuffer(GL_FRAMEBUFFER, frameBuffers[0])
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0)
            glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            buffer.position(0)
            bitmap.copyPixelsFromBuffer(buffer)
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 0, 0)
            glBindFramebuffer(GL_FRAMEBUFFER, 0)
            glDeleteFramebuffers(frameBuffers.size, frameBuffers, 0)
            return bitmap
        }

        /**
         *
         * 将frame buffer转换为bitmap
         *
         * @param frameBuffer frame buffer id
         *
         * @return frame buffer附着的texture对应的bitmap
         *
         */
        fun frameBuffer2Bitmap(frameBuffer: FrameBuffer): Bitmap {
            return texture2Bitmap(
                    frameBuffer.texture,
                    frameBuffer.width,
                    frameBuffer.height
            )
        }

    }

}
