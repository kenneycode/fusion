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

    var program = 0

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
     * 将参数绑定到此GL Program
     *
     * @param parameters 要绑定的参数
     *
     */
    fun bindParameters(parameters: Collection<Parameter>) {
        glUseProgram(program)
        parameters.forEach { p ->
            p.bind(program)
        }
    }

    /**
     *
     * 减少引用计数，当引用计数为0时放回GLProgramCache
     *
     */
    override fun decreaseRef() {
        super.decreaseRef()
        if (refCount == 0) {
            GLProgramPool.releaseGLProgram(this)
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
