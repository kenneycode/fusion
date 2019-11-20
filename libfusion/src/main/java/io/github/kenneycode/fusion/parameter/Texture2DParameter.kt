package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1i

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader uniform sampler2D数组参数
 *
 */

class Texture2DParameter(key: String, private var value: Int) : Parameter(key) {

    override fun bindUniform(program: Int) {
        if (location < 0) {
            location = glGetUniformLocation(program, key)
        }
        glBindTexture(GL_TEXTURE_2D, value)
        glUniform1i(location, 0)
    }

}
