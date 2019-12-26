package io.github.kenneycode.fusion.framebuffer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * FrameBuffer缓存池
 *
 */

object FrameBufferPool {

    private val cache = mutableListOf<FrameBuffer>()

    /**
     *
     * 获取frame buffer
     *
     * @return frame buffer
     *
     */
    fun obtainFrameBuffer(): FrameBuffer {
        if (cache.isEmpty()) {
            cache.add(FrameBuffer())
        }
        return cache.removeAt(0)
    }

    /**
     *
     * 向cache中归还frame buffer
     *
     * @param frameBuffer 归还的frame buffer
     *
     */
    fun releaseFrameBuffer(frameBuffer: FrameBuffer) {
        cache.add(frameBuffer)
    }

    fun release() {
        cache.forEach {
            it.release()
        }
    }

}

