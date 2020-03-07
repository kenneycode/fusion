package io.github.kenneycode.fusion.process

import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.texture.TexturePool
import io.github.kenneycode.fusion.util.Util
import java.util.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染过程管理类，将Renderer按指定规则连接成Graph，并执行渲染过程
 *
 */

class RenderGraph : Renderer {

    private var input = mutableListOf<Texture>()
    private var output: Texture? = null
    private val rendererNodeMap = HashMap<String, Node>()
    private val startNodes = mutableListOf<Node>()

    /**
     *
     * 添加renderer
     *
     * @param renderer  根Renderer
     *
     * @return 返回此RenderGraph
     *
     */
    fun addRenderer(renderer: Renderer, id: String = Util.genId(renderer)): RenderGraph {
        rendererNodeMap[id] = Node(renderer).also {
            startNodes.add(it)
        }
        return this
    }

    /**
     *
     * 为一个Renderer连接一个后续Renderer
     *
     * @param pre   前一个Renderer id
     * @param next  后一个Renderer id
     *
     * @return 返回此RenderGraph
     *
     */
    fun connectRenderer(preId: String, nextId: String): RenderGraph {
        val preNode = rendererNodeMap[preId]!!
        val nextNode = rendererNodeMap[nextId]!!
        preNode.addNext(nextNode)
        nextNode.layer = if (preNode.layer + 1 > nextNode.layer) {
            preNode.layer + 1
        } else {
            nextNode.layer
        }
        startNodes.remove(nextNode)
        return this
    }

    /**
     *
     * 初始化，会对Graph中所有Node都调用其初始化方法
     *
     */
    override fun init() {
        layerTraversal(startNodes) { node ->
            node.renderer.init()
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
        layerTraversal(startNodes) { node ->
            node.renderer.update(data)
        }
        return true
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
     *
     */
    override fun render() {
        output = renderByLayer(input)
    }

    /**
     *
     * 按层序渲染
     *
     * @param input 输入textures
     *
     * @return 输出texture
     *
     */
    private fun renderByLayer(input: List<Texture>): Texture? {
        var intermediateOutput: Texture? = null
        input.forEach { it.increaseRef() }
        startNodes.forEach { it.input.addAll(input) }
        layerTraversal(startNodes) { node ->
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
            node.input.forEach {
                it.decreaseRef()
            }
            node.input.clear()
            intermediateOutput?.let { outputTexture ->
                if (node.nextNodes.isNotEmpty()) {
                    outputTexture.increaseRef(node.nextNodes.size - 1)
                    node.nextNodes.forEach { nextNode ->
                        nextNode.input.add(outputTexture)
                    }
                }
            }
        }
        return intermediateOutput
    }

    /**
     *
     * 层序遍历
     *
     * @param rootNode 根node
     * @param nodeProcessor 节点处理器
     *
     */
    private fun layerTraversal(startNodes: MutableList<Node>, nodeProcessor: (node: Node) -> Unit) {
        val traversalQueue = LinkedList<Node>()
        val queuedNodes = mutableSetOf<Node>()
        val layerNodes = mutableListOf<MutableList<Node>>()
        traversalQueue.addAll(startNodes)
        while (!traversalQueue.isEmpty()) {
            val node = traversalQueue.removeFirst()
            if (node.layer >= layerNodes.size) {
                layerNodes.add(mutableListOf())
            }
            layerNodes[node.layer].add(node);
            node.nextNodes.forEach { nextNode ->
                if (!queuedNodes.contains(nextNode)) {
                    traversalQueue.addLast(nextNode)
                    queuedNodes.add(nextNode)
                }
            }
        }
        layerNodes.forEach { layer ->
            layer.forEach { node ->
                nodeProcessor(node)
            }
        }
    }

    fun getInput(): List<Texture> {
        return input
    }

    /**
     *
     * 释放资源
     *
     */
    override fun release() {
        layerTraversal(startNodes) { node ->
            node.renderer.release()
        }
    }

    /**
     *
     * Graph Node
     *
     */
    private open inner class Node(val renderer: Renderer, var layer: Int = 0) {

        var needRender = true
        var input = mutableListOf<Texture>()
        var nextNodes= mutableListOf<Node>()

        fun addNext(nextNode: Node) {
            nextNodes.add(nextNode)
        }

    }

}