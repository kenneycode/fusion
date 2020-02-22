package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader float数组参数
 *
 */

class FloatArrayParameter(key: String, private var value: FloatArray) : UniformParameter(key) {

    override fun onBind(location: Int) {
        glUniform1fv(location, value.size, value, 0)
    }

    override fun update(value: Any) {
        (value as? FloatArray)?.let {
            this.value = it
        }
    }

}
