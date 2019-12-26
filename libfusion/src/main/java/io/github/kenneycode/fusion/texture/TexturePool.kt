package io.github.kenneycode.fusion.texture

import android.opengl.GLES20.GL_TEXTURE_2D
import java.util.HashMap
import io.github.kenneycode.fusion.common.Size
import io.github.kenneycode.fusion.util.Util
import org.w3c.dom.Text

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Texture缓存池
 *
 */

object TexturePool {

    private val cache = mutableMapOf<Int, MutableMap<Size, MutableList<Texture>>>()

    /**
     *
     * 获取指定尺寸的FrameBuffer，如果cache中不存在，会创建
     *
     * @param width 宽度
     * @param height 高度
     *
     * @return 对应尺寸的FrameBuffer
     *
     */
    fun obtainTexture(width: Int, height: Int, type: Int = GL_TEXTURE_2D): Texture {
        Util.assert(width > 0 && height > 0)
        if (!cache.containsKey(type)) {
            cache[type] = mutableMapOf()
        }
        val size = Size(width, height)
        if (!cache[type]!!.containsKey(size)) {
            cache[type]!![size] = mutableListOf()
        }
        if (cache[type]!![size]!!.isEmpty()) {
            cache[type]!![size]!!.add(Texture(width, height, type).apply {
                init()
            })
        } else {
            cache[type]!![size]!!.first().increaseRef()
        }
        return cache[type]!![size]!!.removeAt(0)
    }

    /**
     *
     * 向cache中归还FrameBuffer
     *
     * @param texture 归还的texture
     *
     */
    fun releaseTexture(texture: Texture) {
        cache[texture.type]?.get(Size(texture.width, texture.height))?.add(texture)
    }

    fun release() {
        cache.entries.forEach {
            it.value.entries.forEach { textures ->
                textures.value.forEach { texture ->
                    texture.release()
                }
            }
        }
    }

}

