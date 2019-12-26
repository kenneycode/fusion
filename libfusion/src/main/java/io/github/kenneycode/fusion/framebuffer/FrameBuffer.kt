package io.github.kenneycode.fusion.framebuffer

import android.opengl.GLES11Ext
import android.opengl.GLES20.*
import io.github.kenneycode.fusion.common.Ref

import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.fusion.util.Util

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

    var frameBuffer = GL_NONE
    var attachedTexture: Texture? = null

    /**
     *
     * 将texture attach到此frame buffer上
     *
     * @param texture 要attach的texture
     *
     */
    fun attachTexture(texture: Texture) {
        attachedTexture?.decreaseRef()
        attachedTexture = texture
        if (!glIsFramebuffer(frameBuffer)) {
            frameBuffer = GLUtil.createFrameBuffer()
        }
        bind()
        Util.assert(texture.width > 0 && texture.height > 0 && (texture.type == GL_TEXTURE_2D || texture.type == GLES11Ext.GL_TEXTURE_EXTERNAL_OES) && glIsTexture(texture.texture) && frameBuffer > 0)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.type, texture.texture, 0)
        GLUtil.checkGLError()
    }

    /**
     *
     * 绑定此frame buffer，绑定后将渲染到此frame buffer上
     *
     */
    fun bind() {
        Util.assert(frameBuffer > GL_NONE)
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer)
        Util.assert(glIsFramebuffer(frameBuffer))
    }

    /**
     *
     * 解绑定此frame buffer，绑定后将渲染到此frame buffer上
     *
     */
    fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, GL_NONE)
    }

    /**
     *
     * 减少引用计数，当引用计数为0时放回frame buffer cache
     *
     */
    override fun decreaseRef() {
        super.decreaseRef()
        if (refCount <= 0) {
            FrameBufferPool.releaseFrameBuffer(this)
        }
    }

    /**
     *
     * 释放资源
     *
     */
    fun release() {
        GLUtil.deleteFrameBuffer(frameBuffer)
    }

}
