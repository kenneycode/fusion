package io.github.kenneycode.fusion.process

import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.outputtarget.OutputTarget
import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染过程管理类，将Renderer/OutputTarget按单链的方式连接，并执行渲染过程
 *
 */

class RenderChain(rootRenderer: Renderer) : Renderer {

    private val renderGraph = RenderGraph(rootRenderer)
    private var tailRenderer = rootRenderer
    var outputTargetGLContext: GLContext?
        get() = renderGraph.outputTargetGLContext
        set(value) {
            renderGraph.outputTargetGLContext = value
        }

    /**
     *
     * 初始化，会对chain中所有Node都调用其初始化方法
     *
     */
    override fun init() {
        renderGraph.init()
    }

    /**
     *
     * 更新数据，会对chain中所有Node都调用其更新数据的方法
     *
     * @param data 数据
     *
     * @return 是否需要执行当前渲染
     *
     */
    override fun update(data: MutableMap<String, Any>): Boolean {
        return renderGraph.update(data)
    }

    /**
     *
     * 添加后一个Renderer
     *
     * @param next 后一个Renderer
     *
     * @return 返回此RenderChain
     *
     */
    fun addNextRenderer(next: Renderer): RenderChain {
        renderGraph.addNextRenderer(tailRenderer, next)
        tailRenderer = next
        return this
    }

    /**
     *
     * 为最后一个Renderer添加一个后续OutputTarget
     *
     * @param next OutputTarget
     *
     */
    fun setOutputTarget(next: OutputTarget) {
        renderGraph.addOutputTarget(tailRenderer, next)
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    override fun setInput(texture: Texture) {
        renderGraph.setInput(texture)
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffers 输入FrameBuffer
     */
    override fun setInput(textures: List<Texture>) {
        renderGraph.setInput(textures)
    }

    override fun getOutput(): Texture? {
        return renderGraph.getOutput()
    }

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    override fun setOutput(texture: Texture?) {
        renderGraph.setOutput(texture)
    }

    /**
     *
     * 执行渲染
     *
     * @return 输出FrameBuffer
     */
    override fun render() {
        return renderGraph.render()
    }

    /**
     *
     * 释放资源
     *
     */
    override fun release() {
        renderGraph.release()
    }

}