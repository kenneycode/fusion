package io.github.kenneycode.fusion.texture

import android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES
import android.opengl.GLES20.*
import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.fusion.common.Ref
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.util.Util
import java.nio.Buffer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Texture封装类
 *
 */

class Texture(val width: Int, val height: Int, val type: Int = GL_TEXTURE_2D, var texture: Int = GL_NONE) : Ref() {

    var retain = false

    fun init() {
        if (texture == GL_NONE) {
            texture = GLUtil.createTexture(type)
            glCheck { glBindTexture(type, texture) }
            glCheck { glTexImage2D(type, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null) }
            glCheck { glBindTexture(type, 0) }
        }
    }

    /**
     *
     * 绑定此FrameBuffer，绑定后将渲染到此FrameBuffer上
     *
     * @param width 宽度
     * @param height 高度
     * @param externalTexture 外部纹理，当指定外部纹理时，外部纹理会附着到此FrameBuffer上
     *
     */
    fun setData(data: Buffer) {
        Util.assert(width > 0 && height > 0 && (type == GL_TEXTURE_2D || type == GL_TEXTURE_EXTERNAL_OES) && texture != GL_NONE, "texture error")
        glCheck { glBindTexture(type, texture) }
        glCheck { glTexImage2D(type, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data) }
        glCheck { glBindTexture(type, 0) }
    }

    /**
     *
     * 减少引用计数，当引用计数为0时放回FrameBufferCache
     *
     */
    override fun decreaseRef() {
        if (!retain) {
            super.decreaseRef()
            if (refCount <= 0) {
                TexturePool.releaseTexture(this)
            }
        }
    }

    fun release() {
        if (texture != GL_NONE) {
            GLUtil.deleteTexture(texture)
            texture = 0
        }
    }

}
