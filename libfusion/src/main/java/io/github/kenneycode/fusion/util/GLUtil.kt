package io.github.kenneycode.fusion.util

import android.graphics.Bitmap
import android.opengl.GLES11Ext

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
import android.opengl.GLES30
import android.opengl.Matrix
import io.github.kenneycode.fusion.common.Constants

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
         * 创建一个OES纹理
         *
         * @return 纹理id
         */
        fun createOESTexture() : Int {
            val textures = IntArray(1)
            GLES30.glGenTextures(textures.size, textures, 0)
            GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0])
            GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
            GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
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
            glGenFramebuffers(frameBuffers.size, frameBuffers, 0)
            glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture)
            glBindFramebuffer(GL_FRAMEBUFFER, frameBuffers[0])
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture, 0)
            glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            buffer.position(0)
            bitmap.copyPixelsFromBuffer(buffer)
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0, 0)
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

    }

}
