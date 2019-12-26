package io.github.kenneycode.fusion.process

import io.github.kenneycode.fusion.common.FusionGLView
import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.outputtarget.OutputTarget
import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.texture.TexturePool
import java.util.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染过程管理类，将Renderer/OutputTarget按指定规则连接成Graph，并执行渲染过程
 *
 */

class RenderGraph(private val rootRenderer: Renderer) : Renderer {

    var outputTargetGLContext: GLContext? = null
    private val LAYER_SEPERATOR = null
    private var input = mutableListOf<Texture>()
    private var output: Texture? = null
    private val rendererNodeMap = HashMap<Renderer, Node>()
    private val rootNode: RendererNode = RendererNode(rootRenderer).apply {
        rendererNodeMap[rootRenderer] = this
    }

    /**
     *
     * 初始化，会对Graph中所有Node都调用其初始化方法
     *
     */
    override fun init() {
        val traversalQueue = LinkedList<Node>()
        traversalQueue.addLast(rootNode)
        while (!traversalQueue.isEmpty()) {
            val node = traversalQueue.removeFirst()
            when (node) {
                is RendererNode -> {
                    node.renderer.init()
                }
                is OutputTargetNode -> {
                    node.outputTarget.onInit()
                }
            }
            traversalQueue.addAll(node.nextNodes)
        }
    }

    /**
     *
     * 更新数据，会对Graph中所有Node都调用其更新数据的方法
     *
     * @param data 数据
     *
     * @return 是否需要执行当前渲染
     *
     */
    override fun update(data: MutableMap<String, Any>): Boolean {
        val traversalQueue = LinkedList<Node>()
        traversalQueue.addLast(rootNode)
        while (!traversalQueue.isEmpty()) {
            val node = traversalQueue.removeFirst()
            when (node) {
                is RendererNode -> {
                    node.needRender = node.renderer.update(data)
                }
                is OutputTargetNode -> {
                    node.outputTarget.onUpdate(data)
                }
            }
            traversalQueue.addAll(node.nextNodes)
        }
        return true
    }

    /**
     *
     * 为一个Renderer添加一个后续Renderer
     *
     * @param pre   前一个Renderer
     * @param next  后一个Renderer
     *
     * @return 返回此RenderGraph
     *
     */
    fun addNextRenderer(pre: Renderer, next: Renderer): RenderGraph {
        val preNode = rendererNodeMap[pre]!!
        if (!rendererNodeMap.containsKey(next)) {
            rendererNodeMap[next] = RendererNode(next, preNode.layer + 1)
        }
        rendererNodeMap[next]?.let { nextNode ->
            preNode.addNext(nextNode)
            nextNode.layer = if (preNode.layer + 1 > nextNode.layer) {
                preNode.layer + 1
            } else {
                nextNode.layer
            }
        }
        return this
    }

    /**
     *
     * 为一个Renderer添加一个后续OutputTarget
     *
     * @param pre   前一个Renderer
     * @param next  后一个OutputTarget
     *
     */
    fun addOutputTarget(pre: Renderer, next: OutputTarget) {
        val preNode = rendererNodeMap[pre]!!
        val nextNode = OutputTargetNode(next)
        preNode.addNext(nextNode)
        nextNode.layer = if (preNode.layer + 1 > nextNode.layer) {
            preNode.layer + 1
        } else {
            nextNode.layer
        }
        when (next) {
            is FusionGLView -> {
                outputTargetGLContext = next
            }
        }
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    override fun setInput(texture: Texture) {
        setInput(listOf(texture))
    }

    /**
     *
     * 设置输入
     *
     * @param frameBuffers 输入FrameBuffer
     */
    override fun setInput(textures: List<Texture>) {
        input.apply {
            clear()
            addAll(textures)
        }
    }

    override fun getOutput(): Texture? {
        return output
    }

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    override fun setOutput(texture: Texture?) {
        output = texture
    }

    /**
     *
     * 执行渲染
     *
     * @return 输出FrameBuffer
     */
    override fun render() {
        output = performTraversal(input)
    }

    private fun performTraversal(input: List<Texture>): Texture? {
        var intermediateOutput: Texture? = null
        (rootNode.input).addAll(input)
        val traversalQueue = LinkedList<Node?>()
        traversalQueue.addLast(rootNode)
        traversalQueue.addLast(LAYER_SEPERATOR)
        var currentLayer = 0
        while (!traversalQueue.isEmpty()) {
            val node = traversalQueue.removeFirst()
            if (node == LAYER_SEPERATOR) {
                ++currentLayer
                continue
            }
            if (node.layer != currentLayer) {
                continue
            }
            when (node) {
                is RendererNode -> {
                    node.renderer.setInput(node.input)
                    node.renderer.setOutput(
                        if (node.nextNodes.isEmpty()) {
                            output
                        } else {
                            TexturePool.obtainTexture(node.input.first().width, node.input.first().height)
                        }
                    )
                    node.renderer.render()
                    intermediateOutput = node.renderer.getOutput()
                }
                is OutputTargetNode -> {
                    node.outputTarget.onInputReady(node.input)
                }
            }
            node.input.clear()
            intermediateOutput?.let {
                if (node.nextNodes.isNotEmpty()) {
                    it.increaseRef(node.nextNodes.size - 1)
                }
                node.nextNodes.forEach { nextNode ->
                    nextNode.input.add(it)
                    traversalQueue.addLast(nextNode)
                }
            }
            traversalQueue.addLast(LAYER_SEPERATOR)
        }
        return intermediateOutput
    }

    /**
     *
     * 释放资源
     *
     */
    override fun release() {
        val traversalQueue = LinkedList<Node>()
        traversalQueue.addLast(rootNode)
        while (!traversalQueue.isEmpty()) {
            val node = traversalQueue.removeFirst()
            if (node is RendererNode) {
                node.renderer.release()
            }
            traversalQueue.addAll(node.nextNodes)
        }
    }

    /**
     *
     * Graph Node基类
     *
     */
    private open inner class Node(var layer: Int = 0) {

        var needRender = true
        var input = mutableListOf<Texture>()
        var nextNodes= mutableListOf<Node>()

        fun addNext(nextNode: Node) {
            nextNodes.add(nextNode)
        }

    }

    /**
     *
     * 渲染器Node类
     *
     */
    private inner class RendererNode(val renderer: Renderer, layer: Int = 0) : Node(layer)

    /**
     *
     * 输出目标Node类
     *
     */
    private inner class OutputTargetNode(val outputTarget: OutputTarget, layer: Int = 0) : Node(layer)

}