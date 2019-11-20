package io.github.kenneycode.fusion.framebuffer

import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.fusion.common.Ref

import android.opengl.GLES20.GL_COLOR_ATTACHMENT0
import android.opengl.GLES20.GL_FRAMEBUFFER
import android.opengl.GLES20.GL_RGBA
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_UNSIGNED_BYTE
import android.opengl.GLES20.glBindFramebuffer
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glFramebufferTexture2D
import android.opengl.GLES20.glTexImage2D
import android.opengl.GLES20.glViewport

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * FrameBuffer类，可以做为一帧渲染的输入/输出
 *
 */

class FrameBuffer : Ref() {

    var width = 0
    var height = 0
    var texture = 0
    var frameBuffer = 0

    /**
     *
     * 绑定此FrameBuffer，绑定后将渲染到此FrameBuffer上
     *
     * @param width 宽度
     * @param height 高度
     * @param externalTexture 外部纹理，当指定外部纹理时，外部纹理会附着到此FrameBuffer上
     *
     */
    fun bind(width: Int = 0, height: Int = 0, externalTexture: Int = -1) {
        if (width != 0 && height != 0) {
            if (externalTexture == 0) {
                if (frameBuffer != 0) {
                    GLUtil.deleteFrameBuffer(frameBuffer)
                }
                if (texture != 0) {
                    GLUtil.deleteTexture(texture)
                }
            } else {
                if (frameBuffer == 0) {
                    frameBuffer = GLUtil.createFrameBuffer()
                }
                if (externalTexture != -1 && externalTexture != texture) {
                    GLUtil.deleteTexture(texture)
                    texture = 0
                }
                if (width != this.width || height != this.height || texture == 0) {
                    this.width = width
                    this.height = height
                    if (texture == 0) {
                        if (externalTexture > 0) {
                            texture = externalTexture
                        } else {
                            texture = GLUtil.createTexture()
                        }
                        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer)
                        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                                texture, 0)
                    }
                    glBindTexture(GL_TEXTURE_2D, texture)
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
                    glBindTexture(GL_TEXTURE_2D, 0)
                }
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer)
        glViewport(0, 0, width, height)
    }

}
