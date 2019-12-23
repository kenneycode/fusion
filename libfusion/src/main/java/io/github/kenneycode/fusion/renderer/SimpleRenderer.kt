package io.github.kenneycode.fusion.renderer


import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.Shader
import io.github.kenneycode.fusion.framebuffer.FrameBuffer
import io.github.kenneycode.fusion.framebuffer.FrameBufferCache
import io.github.kenneycode.fusion.program.GLProgram
import io.github.kenneycode.fusion.program.GLProgramCache

import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnable
import io.github.kenneycode.fusion.parameter.*
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 简单渲染器，可继承此类进行扩展
 *
 */

open class SimpleRenderer(vertexShader: String = Constants.MVP_VERTEX_SHADER, fragmentShader: String = Constants.SIMPLE_FRAGMENT_SHADER) : GLRenderer {

    private val shader = Shader(vertexShader, fragmentShader)
    private lateinit var glProgram: GLProgram
    private val parameters = HashMap<String, Parameter>()
    private var vertexCount = 0
    protected val inputFrameBuffers = mutableListOf<FrameBuffer>()
    protected var outputFrameBuffer: FrameBuffer? = null
    protected var specifiedOutputWidth: Int = 0
    protected var specifiedOutputHeight: Int = 0

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
        setParameter(key, value) {
            FloatArrayParameter(key, value, componentCount)
        }
    }

    /**
     *
     * 设置顶点坐标，默认是二维坐标
     *
     * @param positions 顶点坐标数组
     *
     */
    override fun setPositions(positions: FloatArray, componentCount: Int) {
        setAttributeFloats(Constants.POSITION_PARAM_KEY, positions, componentCount)
        vertexCount = positions.size / componentCount
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
        setParameter(key, value) {
            Texture2DParameter(key, value)
        }
    }

    /**
     *
     * 设置OES纹理参数
     *
     * @param key 纹理参数名
     * @param value 纹理id
     *
     */
    override fun setUniformOESTexture(key: String, value: Int) {
        setParameter(key, value) {
            OESTextureParameter(key, value)
        }
    }

    /**
     *
     * 设置4*4 Matrix参数
     *
     * @param key 纹理参数名
     * @param value 4*4 Matrix
     *
     */
    override fun setUniformMat4(key: String, value: FloatArray) {
        setParameter(key, value) {
            Mat4Parameter(key, value)
        }
    }

    /**
     *
     * 设置MVP
     *
     * @param value 4*4 MVP Matrix
     *
     */
    override fun setMVPMatrix(value: FloatArray) {
        setUniformMat4(Constants.MVP_MATRIX_PARAM_KEY,  value)
    }

    /**
     *
     * 设置参数，如果参数已存在则更新，不存在则创建
     *
     * @param key 参数名
     * @param value 参数值
     * @param parameterCreator 参数创建器
     *
     */
    private fun setParameter(key: String, value: Any, parameterCreator: () -> Parameter) {
        parameters[key]?.update(value) ?: let {
            parameters[key] = parameterCreator()
        }
    }

    /**
     *
     * 设置渲染翻转
     *
     * @param flipX 水平翻转
     * @param flipY 垂直翻转
     *
     */
    override fun setFlip(flipX: Boolean, flipY: Boolean) {
        if (!flipX && !flipY) {
            setPositions(Constants.SIMPLE_VERTEX)
        } else if (flipX && !flipY) {
            setPositions(Constants.SIMPLE_VERTEX_FLIP_X)
        } else if (!flipX && flipY) {
            setPositions(Constants.SIMPLE_VERTEX_FLIP_Y)
        } else {
            setPositions(Constants.SIMPLE_VERTEX_FLIP_XY)
        }
    }

    /**
     *
     * 绑定输入
     *
     */
    override fun bindInput() {
        if (inputFrameBuffers.isNotEmpty()) {
            var textureIndex = '0'
            var i = 0
            while (i < inputFrameBuffers.size) {
                var textureKey = "u_texture"
                if (i > 0) {
                    textureKey += textureIndex
                }
                setUniformTexture2D(textureKey, inputFrameBuffers[i].texture)
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
        val outputWidth = if (specifiedOutputWidth > 0) specifiedOutputWidth else inputFrameBuffers.first().width
        val outputHeight = if (specifiedOutputHeight > 0) specifiedOutputHeight else inputFrameBuffers.first().height
        outputFrameBuffer = FrameBufferCache.obtainFrameBuffer(outputWidth, outputHeight).apply {
            bind(outputWidth, outputHeight)
        }
    }

    /**
     *
     * 绑定参数
     *
     */
    override fun bindParameters() {
        checkDefaultParameters()
        glProgram.bindParameters(parameters.values)
    }

    /**
     *
     * 给一些预定的参数设置默认值
     *
     */
    private fun checkDefaultParameters() {
        if (GLUtil.hasAttribute(glProgram.program, Constants.POSITION_PARAM_KEY) && !parameters.containsKey(Constants.POSITION_PARAM_KEY)) {
            setPositions(Constants.SIMPLE_VERTEX)
        }
        if (GLUtil.hasAttribute(glProgram.program, Constants.TEXTURE_COORDINATE_PARAM_KEY) && !parameters.containsKey(Constants.TEXTURE_COORDINATE_PARAM_KEY)) {
            setTextureCoordinates(Constants.SIMPLE_TEXTURE_COORDINATE)
        }
        if (GLUtil.hasUniform(glProgram.program, Constants.MVP_MATRIX_PARAM_KEY) && !parameters.containsKey(Constants.MVP_MATRIX_PARAM_KEY)) {
            setMVPMatrix(Constants.IDENTITY_MATRIX)
        }
    }

    /**
     *
     * 执行渲染（draw call）
     *
     */
    private fun performRendering() {
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
        inputFrameBuffers.apply {
            forEach { frameBuffer ->
                frameBuffer.decreaseRef()
            }
            clear()
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
        this.inputFrameBuffers.addAll(frameBuffers)
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
        bindParameters()
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
        glProgram.decreaseRef()
    }

}
