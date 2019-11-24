package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.framebuffer.FrameBuffer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染器接口
 *
 */

interface Renderer {

    /**
     *
     * 初始化
     *
     */
    fun init()

    /**
     *
     * 更新数据
     *
     * @param data 传入的数据
     *
     * @return 是否执行当次渲染
     *
     */
    fun update(data: MutableMap<String, Any>): Boolean

    /**
     *
     * 设置单个输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    fun setInput(frameBuffer: FrameBuffer)

    /**
     *
     * 设置多个输入
     *
     * @param frameBuffers 输入FrameBuffer list
     *
     */
    fun setInput(frameBuffers: List<FrameBuffer>)

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    fun setOutput(frameBuffer: FrameBuffer)

    /**
     *
     * 设置输出宽高
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    fun setOutputSize(width: Int, height: Int)

    /**
     *
     * 渲染
     *
     * @return 渲染结果FrameBuffer
     *
     */
    fun render(): FrameBuffer

    /**
     *
     * 释放资源
     *
     */
    fun release()

}
