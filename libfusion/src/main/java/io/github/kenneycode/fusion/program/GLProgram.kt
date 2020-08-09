package io.github.kenneycode.fusion.program

import android.opengl.GLES20.*
import android.util.Log
import io.github.kenneycode.fusion.common.Ref
import io.github.kenneycode.fusion.common.Shader
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.parameter.Parameter

import io.github.kenneycode.fusion.util.Util

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

            var vertexShader = GL_NONE
            glCheck { vertexShader = glCreateShader(GL_VERTEX_SHADER) }
            var fragmentShader = GL_NONE
            glCheck { fragmentShader = glCreateShader(GL_FRAGMENT_SHADER) }

            glCheck { glShaderSource(vertexShader, shader!!.vertexShader) }
            glCheck { glShaderSource(fragmentShader, shader!!.fragmentShader) }

            glCheck { glCompileShader(vertexShader) }
            glCheck {
                glGetShaderInfoLog(vertexShader).let {
                    if (it.isNotEmpty()) {
                        Log.e("debug", "vertex shader info log = $it")
                    }
                }
            }

            glCheck { glCompileShader(fragmentShader) }
            glCheck {
                glGetShaderInfoLog(fragmentShader).let {
                    if (it.isNotEmpty()) {
                        Log.e("debug", "fragment shader info log = $it")
                    }
                }
            }

            glCheck { glAttachShader(program, vertexShader) }
            glCheck { glAttachShader(program, fragmentShader) }

            glCheck { glLinkProgram(program) }
            glCheck {
                glGetProgramInfoLog(program).let {
                    if (it.isNotEmpty()) {
                        Log.e("debug", "program info log = $it")
                    }
                }
            }

            glCheck { glDeleteShader(vertexShader) }
            glCheck { glDeleteShader(fragmentShader) }

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
