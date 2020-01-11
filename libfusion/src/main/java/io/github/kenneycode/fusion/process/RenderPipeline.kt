package io.github.kenneycode.fusion.process

import io.github.kenneycode.fusion.context.GLContext
import io.github.kenneycode.fusion.context.SimpleGLContext
import io.github.kenneycode.fusion.renderer.Renderer
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.texture.TexturePool

class RenderPipeline private constructor() {

    companion object {

        fun input(input: Input): RenderPipeline {
            return RenderPipeline().apply {
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
        executeOnGLContext {
            input.onInit()
            renderer.init()
            output.onInit()
        }
    }

    fun update(data: MutableMap<String, Any> = mutableMapOf()) {
        executeOnGLContext {
            input.onUpdate(data)
            renderer.update(data)
            output.onUpdate(data)
        }
    }

    fun render() {
        executeOnGLContext {
            val inputTexture = input.getInputTexture()
            val outputTexture = TexturePool.obtainTexture(inputTexture.width, inputTexture.height)
            renderer.setInput(inputTexture)
            renderer.setOutput(outputTexture)
            renderer.render()
            renderer.getOutput()?.let {
                output.onReceiveOutputTexture(it)
            }
            outputTexture.decreaseRef()
        }
    }

    private fun executeOnGLContext(task: () -> Unit) {
        if (glContext == null) {
            glContext = SimpleGLContext()
        }
        glContext?.runOnGLContext {
            task()
        }
    }

    interface Input {

        fun onInit()
        fun onUpdate(data: MutableMap<String, Any>)
        fun getInputTexture(): Texture

    }

    interface Output {

        fun onInit()
        fun onReceiveOutputTexture(texture: Texture)
        fun onUpdate(data: MutableMap<String, Any>)

    }

}