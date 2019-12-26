package io.github.kenneycode.fusion.inputsource

import io.github.kenneycode.fusion.context.GLContextPool
import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.process.RenderGraph
import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 输入源基类
 *
 */

abstract class InputSource {

    var renderers: MutableList<Renderer> = mutableListOf()

    /**
     *
     * 添加渲染器
     *
     * @param renderer 渲染器
     */
    fun addRenderer(renderer: Renderer) {
        renderers.add(renderer)
        when (renderer) {
            is RenderGraph -> {
                renderer.outputTargetGLContext?.let {
                    GLContextPool.setGLContextForInputSource(this, it)
                }
            }
            is RenderChain -> {
                renderer.outputTargetGLContext?.let {
                    GLContextPool.setGLContextForInputSource(this, it)
                }
            }
        }
    }

    /**
     *
     * 通知初始化
     *
     */
    fun notifyInit() {
        renderers.forEach { target ->
            target.init()
        }
    }

    /**
     *
     * 通知输入已准备好
     *
     * @param data 传入的数据
     * @param frameBuffer 输入FrameBuffer
     */
    fun notifyInputReady(data: MutableMap<String, Any>, texture: Texture) {
        notifyInputReady(data, listOf(texture))
    }

    /**
     *
     * 通知输入已准备好
     *
     * @param data 传入的数据
     * @param frameBuffers 输入FrameBuffer数组
     */
    fun notifyInputReady(data: MutableMap<String, Any>, textures: List<Texture>) {
        renderers.forEach { target ->
            target.setInput(textures)
            target.update(data)
            target.render()
        }
    }

    /**
     *
     * 是否原地执行GL调用
     *
     * @return  是否原地执行GL调用
     */
    fun runGLCommandInPlace(): Boolean? {
        return false
    }

}