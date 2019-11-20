package io.github.kenneycode.fusion.outputtarget

import io.github.kenneycode.fusion.framebuffer.FrameBuffer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染输出目标接口，实现此接口的类都可以作为渲染输出目标
 *
 */

interface OutputTarget {

    /**
     *
     * 初始化回调
     *
     */
    fun onInit()

    /**
     *
     * 更新数据回调
     *
     * @param data 传入的数据
     *
     */
    fun onUpdate(data: Map<String, Any>)

    /**
     *
     * 通知渲染输出目标输入已经准备好了
     *
     * @param frameBuffers 输入FrameBuffer
     *
     */
    fun onInputReady(frameBuffers: List<FrameBuffer>)

}
