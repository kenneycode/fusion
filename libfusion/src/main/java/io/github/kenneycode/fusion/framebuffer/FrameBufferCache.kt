package io.github.kenneycode.fusion.framebuffer

import java.util.HashMap
import java.util.LinkedList
import io.github.kenneycode.fusion.common.Size

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * FrameBuffer缓存池
 *
 */

object FrameBufferCache {

    private val cache = mutableMapOf<Long, MutableMap<Size, MutableList<FrameBuffer>>>()

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
    fun obtainFrameBuffer(width: Int = 0, height: Int = 0): FrameBuffer {
        val tid = Thread.currentThread().id
        if (!cache.containsKey(tid)) {
            cache[tid] = HashMap()
        }
        val size = Size(width, height)
        if (!cache[tid]!!.containsKey(size)) {
            cache[tid]!![size] = mutableListOf()
        }
        if (cache[tid]!![size]!!.isEmpty()) {
            cache[tid]!![size]!!.add(FrameBuffer())
        } else {
            cache[tid]!![size]!!.first().addRef()
        }
        return cache[tid]!![size]!!.removeAt(0)
    }

    /**
     *
     * 向cache中归还FrameBuffer
     *
     * @param frameBuffer 归还的FrameBuffer
     *
     */
    fun releaseFrameBuffer(frameBuffer: FrameBuffer) {
        val tid = Thread.currentThread().id
        if (cache.containsKey(tid)) {
            val size = Size(frameBuffer.width, frameBuffer.height)
            cache[tid]!![size]!!.add(frameBuffer)
        }
    }

}

