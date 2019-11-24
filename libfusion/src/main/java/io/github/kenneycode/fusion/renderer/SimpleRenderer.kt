package io.github.kenneycode.fusion.renderer

import java.util.ArrayList
import java.util.HashSet

import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.Shader
import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.framebuffer.FrameBufferCache
import io.github.kenneycode.fusion.parameter.FloatArrayParameter
import io.github.kenneycode.fusion.parameter.Parameter
import io.github.kenneycode.fusion.parameter.Texture2DParameter
import io.github.kenneycode.fusion.program.GLProgram
import io.github.kenneycode.fusion.program.GLProgramCache

import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnable

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 简单渲染器，可继承此类进行扩展
 *
 */

open class SimpleRenderer(vertexShader: String = Constants.COMMON_VERTEX_SHADER_2, fragmentShader: String = Constants.COMMON_FRAGMENT_SHADER_2) : GLRenderer {

    private val shader = Shader(vertexShader, fragmentShader)
    protected var outputFrameBuffer: FrameBuffer? = null
    private lateinit var glProgram: GLProgram
    private val attributes = HashSet<Parameter>()
    private val uniforms = HashSet<Parameter>()
    private var input: List<FrameBuffer>? = null
    private var specifiedOutputWidth: Int = 0
    private var specifiedOutputHeight: Int = 0
    private var vertexCount = 0

    /**
     *
     * 初始化
     *
     */
    override fun init() {
        glProgram = GLProgramCache.obtainGLProgram(shader).apply {
            init()
        }
        initParameter()
    }

    /**
     *
     * 初始化参数
     *
     */
    override fun initParameter() {
        setPositions(Constants.COMMON_VERTEX)
        setTextureCoordinates(Constants.COMMON_TEXTURE_COORDINATE)
    }

    /**
     *
     * 设置attribute float数组
     *
     * @param key attribute参数名
     * @param value float数组
     * @param componentCount 每个顶点的成份数（一维，二维..）
     *
     */
    override fun setAttributeFloats(key: String, value: FloatArray, componentCount: Int) {
        val parameter = findParameter(attributes, key)
        parameter?.updateValue(value)?: attributes.add(FloatArrayParameter(key, value, componentCount))
    }

    /**
     *
     * 设置顶点坐标，默认是二维坐标
     *
     * @param positions 顶点坐标数组
     *
     */
    override fun setPositions(positions: FloatArray) {
        setAttributeFloats(Constants.POSITION_PARAM_KEY, positions, 2)
        vertexCount = positions.size / 2
    }

    /**
     *
     * 设置纹理坐标
     *
     * @param textureCoordinates 纹理坐标数组
     *
     */
    override fun setTextureCoordinates(textureCoordinates: FloatArray) {
        setAttributeFloats(Constants.TEXTURE_COORDINATE_PARAM_KEY, textureCoordinates, 2)
    }

    /**
     *
     * 设置纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    override fun setUniformTexture2D(key: String, value: Int) {
        val parameter = findParameter(uniforms, key)
        parameter?.updateValue(value) ?: uniforms.add(Texture2DParameter(key, value))
    }

    /**
     *
     * 绑定输入
     *
     */
    override fun bindInput() {
        if (input!!.isNotEmpty()) {
            var textureIndex = '0'
            var i = 0
            while (i < input!!.size) {
                var textureKey = "u_texture"
                if (i > 0) {
                    textureKey += textureIndex
                }
                setUniformTexture2D(textureKey, input!![i].texture)
                ++i
                ++textureIndex
            }
        }
    }

    /**
     *
     * 绑定输出
     *
     */
    override fun bindOutput() {
        val outputWidth = if (specifiedOutputWidth > 0) specifiedOutputWidth else input!![0].width
        val outputHeight = if (specifiedOutputHeight > 0) specifiedOutputHeight else input!![0].height
        if (outputFrameBuffer == null) {
            outputFrameBuffer = FrameBufferCache.obtainFrameBuffer(outputWidth, outputHeight)
        }
        outputFrameBuffer!!.bind(outputWidth, outputHeight)
    }

    /**
     *
     * 执行渲染（draw call）
     *
     */
    private fun performRendering() {
        glProgram.bindAttribute(attributes)
        glProgram.bindUniform(uniforms)
        glEnable(GL_BLEND)
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
        glDisable(GL_BLEND)
    }

    /**
     *
     * 解绑输入
     *
     */
    override fun unBindInput() {
        input?.forEach {  frameBuffer ->
            frameBuffer.releaseRef()
        }
    }

    /**
     *
     * 更新数据
     *
     * @param data 传入的数据
     *
     * @return 是否执行当次渲染
     *
     */
    override fun update(data: MutableMap<String, Any>): Boolean {
        return true
    }

    /**
     *
     * 设置单个输入
     *
     * @param frameBuffer 输入FrameBuffer
     *
     */
    override fun setInput(frameBuffer: FrameBuffer) {
        setInput(listOf(frameBuffer))
    }

    /**
     *
     * 设置多个输入
     *
     * @param frameBuffers 输入FrameBuffer list
     *
     */
    override fun setInput(frameBuffers: List<FrameBuffer>) {
        this.input = frameBuffers
    }

    /**
     *
     * 设置输出
     *
     * @param frameBuffer 输出FrameBuffer
     *
     */
    override fun setOutput(frameBuffer: FrameBuffer) {
        this.outputFrameBuffer = frameBuffer
    }

    /**
     *
     * 设置输出宽高
     *
     * @param width 宽度
     * @param height 高度
     *
     */
    override fun setOutputSize(width: Int, height: Int) {
        this.specifiedOutputWidth = width
        this.specifiedOutputHeight = height
    }

    /**
     *
     * 查找参数
     *
     * @param parameters 参数集合
     * @param key 参数名
     *
     * @return 对应的参数Paramter类，若找不到返回null
     *
     */
    private fun findParameter(parameters: Set<Parameter>, key: String): Parameter? {
        return parameters.find {
            it.key == key
        }
    }

    /**
     *
     * 渲染
     *
     * @return 渲染结果FrameBuffer
     *
     */
    override fun render(): FrameBuffer {
        bindInput()
        bindOutput()
        performRendering()
        unBindInput()
        return outputFrameBuffer!!
    }

    /**
     *
     * 释放资源
     *
     */
    override fun release() {
        glProgram.releaseRef()
    }

}
