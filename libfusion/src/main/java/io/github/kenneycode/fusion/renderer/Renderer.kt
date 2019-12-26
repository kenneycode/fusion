package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.texture.Texture

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
     * @param texture 输入texture
     *
     */
    fun setInput(texture: Texture)

    /**
     *
     * 设置多个输入
     *
     * @param textures texture list
     *
     */
    fun setInput(textures: List<Texture>)

    /**
     *
     * 设置输出
     *
     * @param texture texture
     *
     */
    fun setOutput(texture: Texture?)

    /**
     *
     * 获取输出
     *
     * @return 输出texture
     *
     */
    fun getOutput(): Texture?

    /**
     *
     * 渲染
     *
     */
    fun render()

    /**
     *
     * 释放资源
     *
     */
    fun release()

}
