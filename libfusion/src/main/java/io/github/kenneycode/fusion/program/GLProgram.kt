package io.github.kenneycode.fusion.program

import io.github.kenneycode.fusion.common.Ref
import io.github.kenneycode.fusion.common.Shader
import io.github.kenneycode.fusion.parameter.Parameter

import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glCompileShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glCreateShader
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glIsProgram
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glShaderSource
import android.opengl.GLES20.glUseProgram

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * GL Program类，可通过GLProgramCache获取
 *
 */

class GLProgram(val shader: Shader) : Ref() {

    private var program = 0

    /**
     *
     * 初始化方法，重复调用不会初始化多次
     *
     */
    fun init() {

        if (!glIsProgram(program)) {

            program = glCreateProgram()

            val vertexShader = glCreateShader(GL_VERTEX_SHADER)
            val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)

            glShaderSource(vertexShader, shader!!.vertexShader)
            glShaderSource(fragmentShader, shader!!.fragmentShader)

            glCompileShader(vertexShader)
            glCompileShader(fragmentShader)

            glAttachShader(program, vertexShader)
            glAttachShader(program, fragmentShader)

            glLinkProgram(program)

            glDeleteShader(vertexShader)
            glDeleteShader(fragmentShader)

        }

    }

    /**
     *
     * 将attribute参数绑定到此GL Program
     *
     * @param attributes 要绑定的attribute参数
     *
     */
    fun bindAttribute(attributes: Set<Parameter>) {
        glUseProgram(program)
        attributes.forEach { p ->
            p.bindAttribute(program)
        }
    }

    /**
     *
     * 将uniform参数绑定到此GL Program
     *
     * @param uniforms 要绑定的uniform参数
     *
     */
    fun bindUniform(uniforms: Set<Parameter>) {
        glUseProgram(program)
        uniforms.forEach { p ->
            p.bindUniform(program)
        }
    }

    /**
     *
     * 减少引用计数，当引用计数为0时放回GLProgramCache
     *
     */
    override fun releaseRef() {
        super.releaseRef()
        if (refCount == 0) {
            GLProgramCache.releaseGLProgram(this)
        }
    }

    /**
     *
     * 释放资源
     *
     */
    fun release() {
        if (glIsProgram(program)) {
            glDeleteProgram(program)
        }
    }

}
