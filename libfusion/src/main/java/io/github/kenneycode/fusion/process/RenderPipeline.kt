package io.github.kenneycode.fusion.process

import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.context.SimpleGLContext
import io.github.kenneycode.fusion.input.InputReceiver
import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.texture.TexturePool

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 渲染管线
 *
 */

class RenderPipeline private constructor() : InputReceiver {

    companion object {

        fun input(input: Input): RenderPipeline {
            return RenderPipeline().apply {
                input.setInputReceiver(this)
                this.input = input
            }
        }

    }

    private var glContext: GLContext? = null
    private lateinit var input: Input
    private lateinit var output: Output
    private lateinit var renderer: Renderer

    fun renderWith(renderer: Renderer): RenderPipeline {
        this.renderer = renderer
        return this
    }

    fun useContext(glContext: GLContext): RenderPipeline {
        this.glContext = glContext
        return this
    }

    fun output(output: Output): RenderPipeline {
        this.output = output
        return this
    }

    fun init() {
        input.onInit()
        renderer.init()
        output.onInit()
    }

    fun update(data: MutableMap<String, Any> = mutableMapOf()) {
        input.onUpdate(data)
        renderer.update(data)
        output.onUpdate(data)
    }

    fun render(input: Texture) {
        val outputTexture = TexturePool.obtainTexture(input.width, input.height)
        renderer.setInput(input)
        renderer.setOutput(outputTexture)
        renderer.render()
        renderer.getOutput()?.let {
            output.onReceiveOutputTexture(it)
        }
        outputTexture.decreaseRef()
    }

    fun start() {
        executeOnGLContext {
            init()
            input.start()
        }
    }

    override fun onInputReady(input: Texture, data: MutableMap<String, Any>) {
        update(data)
        render(input)
    }

    private fun executeOnGLContext(task: () -> Unit) {
        if (glContext == null) {
            glContext = SimpleGLContext()
        }
        glContext?.runOnGLContext {
            task()
        }
    }

    fun release() {
        executeOnGLContext {
            input.onRelease()
            output.onRelease()
            renderer.release()
            glContext?.release()
        }
    }

    interface Input {

        fun start()
        fun setInputReceiver(inputReceiver: InputReceiver)
        fun onInit()
        fun onUpdate(data: MutableMap<String, Any>)
        fun onRelease()

    }

    interface Output {

        fun onInit()
        fun onReceiveOutputTexture(texture: Texture)
        fun onUpdate(data: MutableMap<String, Any>)
        fun onRelease()

    }

}