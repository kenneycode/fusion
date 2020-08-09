package io.github.kenneycode.fusion.util

import android.graphics.Bitmap
import android.opengl.GLES11Ext
import android.opengl.GLES20.*
import java.nio.ByteBuffer
import android.opengl.Matrix
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.texture.Texture

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
        fun createTexture(type: Int = GL_TEXTURE_2D): Int {
            val textures = IntArray(1)
            glCheck { glGenTextures(1, textures, 0) }
            glCheck { glBindTexture(type, textures[0]) }
            glCheck { glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
            glCheck { glBindTexture(type, 0) }
            return textures[0]
        }

        /**
         *
         * 创建一个OES纹理
         *
         * @return 纹理id
         */
        fun createOESTexture() : Int {
            val textures = IntArray(1)
            glCheck { glGenTextures(textures.size, textures, 0) }
            glCheck { glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]) }
            glCheck { glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
            glCheck { glBindTexture(GL_TEXTURE_2D, 0) }
            return textures[0]
        }

        /**
         *
         * 删除一个纹理
         *
         * @param texture 纹理id
         */
        fun deleteTexture(texture: Int) {
            glCheck { glDeleteTextures(1, intArrayOf(texture), 0) }
        }

        /**
         *
         * 创建一个FrameBuffer
         *
         * @return frame buffer id
         */
        fun createFrameBuffer(): Int {
            val frameBuffer = IntArray(1)
            glCheck { glGenFramebuffers(1, frameBuffer, 0) }
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
            glCheck { glDeleteFramebuffers(1, temp, 0) }
        }

        fun bitmap2Texture(bitmap: Bitmap): Int {
            val texture = createTexture(GL_TEXTURE_2D)
            glCheck { glBindTexture(GL_TEXTURE_2D, texture) }
            val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
            bitmap.copyPixelsToBuffer(b)
            b.position(0)
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
            glCheck { glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA, bitmap.width,
                    bitmap.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, b) }
            return texture
        }

        fun bitmap2Texture(bitmap: Bitmap, flipY: Boolean = false): Int {
            val texture = createTexture(GL_TEXTURE_2D)
            glCheck { glBindTexture(GL_TEXTURE_2D, texture) }
            val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
            if (flipY) {
                BitmapUtil.flipBitmap(bitmap, false, true)
            } else {
                bitmap
            }.copyPixelsToBuffer(b)
            b.position(0)
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
            glCheck { glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA, bitmap.width,
                    bitmap.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, b) }
            return texture
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
            glCheck { glBindTexture(GL_TEXTURE_2D, texture) }
            val b = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
            bitmap.copyPixelsToBuffer(b)
            b.position(0)
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
            glCheck { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
            glCheck { glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA, bitmap.width,
                    bitmap.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, b) }
        }

        /**
         *
         * 将纹理转换为bitmap
         *
         * @param texture 纹理
         *
         */
        fun texture2Bitmap(texture: Texture): Bitmap {
            return texture2Bitmap(texture.texture, texture.width, texture.height)
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
            glCheck { glGenFramebuffers(frameBuffers.size, frameBuffers, 0) }
            glCheck { glBindTexture(GL_TEXTURE_2D, texture) }
            glCheck { glBindFramebuffer(GL_FRAMEBUFFER, frameBuffers[0]) }
            glCheck { glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0) }
            glCheck { glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer) }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            buffer.position(0)
            bitmap.copyPixelsFromBuffer(buffer)
            glCheck { glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 0, 0) }
            glCheck { glBindFramebuffer(GL_FRAMEBUFFER, 0) }
            glCheck { glDeleteFramebuffers(frameBuffers.size, frameBuffers, 0) }
            return bitmap
        }

        /**
         *
         * 将OES纹理转换为bitmap
         *
         * @param texture 纹理id
         * @param width 宽度
         * @param height 高度
         *
         */
        fun oesTexture2Bitmap(texture: Int, width: Int, height: Int): Bitmap {
            val buffer = ByteBuffer.allocate(width * height * 4)
            val frameBuffers = IntArray(1)
            glCheck { glGenFramebuffers(frameBuffers.size, frameBuffers, 0) }
            glCheck { glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture) }
            glCheck { glBindFramebuffer(GL_FRAMEBUFFER, frameBuffers[0]) }
            glCheck { glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture, 0) }
            glCheck { glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer) }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            buffer.position(0)
            bitmap.copyPixelsFromBuffer(buffer)
            glCheck { glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0, 0) }
            glCheck { glBindFramebuffer(GL_FRAMEBUFFER, 0) }
            glCheck { glDeleteFramebuffers(frameBuffers.size, frameBuffers, 0) }
            return bitmap
        }

        fun createMVPMatrix(
                translateX: Float = 0f, translateY: Float = 0f, translateZ: Float = 0f,
                rotateX: Float = 0f, rotateY: Float = 0f, rotateZ: Float = 0f,
                scaleX: Float = 1f, scaleY: Float = 1f, scaleZ: Float = 1f,
                cameraPositionX: Float = 0f, cameraPositionY: Float = 0f, cameraPositionZ: Float = 10f,
                lookAtX: Float = 0f, lookAtY: Float = 0f, lookAtZ: Float = 0f,
                cameraUpX: Float = 0f, cameraUpY: Float = 1f, cameraUpZ: Float = 0f,
                nearPlaneLeft: Float = -1f, nearPlaneTop: Float = 1f, nearPlaneRight: Float = 1f, nearPlaneBottom: Float = -1f,
                nearPlane: Float = 5f,
                farPlane: Float = 15f
        ): FloatArray {
            val modelMatrix = createModelMatrix(
                    translateX, translateY, translateZ,
                    rotateX, rotateY, rotateZ,
                    scaleX, scaleY, scaleZ
            )
            val viewMatrix = createViewMatrix(
                    cameraPositionX, cameraPositionY, cameraPositionZ,
                    lookAtX, lookAtY, lookAtZ,
                    cameraUpX, cameraUpY, cameraUpZ
            )
            val projectionMatrix = createProjectionMatrix(
                    nearPlaneLeft, nearPlaneTop, nearPlaneRight, nearPlaneBottom,
                    nearPlane,
                    farPlane
            )
            return Constants.IDENTITY_MATRIX.apply {
                Matrix.multiplyMM(this, 0, viewMatrix, 0, modelMatrix, 0)
                Matrix.multiplyMM(this, 0, projectionMatrix, 0, this, 0)
            }
        }

        fun createModelMatrix(
                translateX: Float, translateY: Float, translateZ: Float,
                rotateX: Float, rotateY: Float, rotateZ: Float,
                scaleX: Float, scaleY: Float, scaleZ: Float
        ): FloatArray {
            val translateMatrix = Constants.IDENTITY_MATRIX
            val rotateMatrix = Constants.IDENTITY_MATRIX
            val scaleMatrix = Constants.IDENTITY_MATRIX
            Matrix.translateM(translateMatrix, 0, translateX, translateY, translateZ)
            Matrix.rotateM(rotateMatrix, 0, rotateX, 1f, 0f, 0f)
            Matrix.rotateM(rotateMatrix, 0, rotateY, 0f, 1f, 0f)
            Matrix.rotateM(rotateMatrix, 0, rotateZ, 0f, 0f, 1f)
            Matrix.scaleM(scaleMatrix, 0, scaleX, scaleY, scaleZ)
            return Constants.IDENTITY_MATRIX.apply {
                Matrix.multiplyMM(this, 0, rotateMatrix, 0, scaleMatrix, 0)
                Matrix.multiplyMM(this, 0, this, 0, translateMatrix, 0)
            }
        }

        fun createViewMatrix(
                cameraPositionX: Float, cameraPositionY: Float, cameraPositionZ: Float,
                lookAtX: Float, lookAtY: Float, lookAtZ: Float,
                cameraUpX: Float, cameraUpY: Float, cameraUpZ: Float
        ): FloatArray {
            return Constants.IDENTITY_MATRIX.apply {
                Matrix.setLookAtM(
                        this,
                        0,
                        cameraPositionX, cameraPositionY, cameraPositionZ,
                        lookAtX, lookAtY, lookAtZ,
                        cameraUpX, cameraUpY, cameraUpZ
                )
            }
        }

        fun createProjectionMatrix(
                nearPlaneLeft: Float, nearPlaneTop: Float, nearPlaneRight: Float, nearPlaneBottom: Float,
                nearPlane: Float,
                farPlane: Float
        ): FloatArray {
            return Constants.IDENTITY_MATRIX.apply {
                Matrix.frustumM(
                        this,
                        0,
                        nearPlaneLeft, nearPlaneRight, nearPlaneBottom, nearPlaneTop,
                        nearPlane,
                        farPlane
                )
            }
        }

        fun hasAttribute(program: Int, attributeName: String): Boolean {
            var ret = false
            glCheck { ret = glGetAttribLocation(program, attributeName) >= 0 }
            return ret
        }

        fun hasUniform(program: Int, attributeName: String): Boolean {
            var ret = false
            glCheck { ret = glGetUniformLocation(program, attributeName) >= 0 }
            return ret
        }

    }

}
