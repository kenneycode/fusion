package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer
import io.github.kenneycode.fusion.util.Util

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader attribute参数
 *
 */

class AttributeParameter(key: String, private var value: FloatArray, private val componentCount: Int = 2) : Parameter(key) {

    /**
     *
     * 绑定参数
     *
     * @param program GL Program
     *
     */
    override fun bind(program: Int) {
        if (location < 0) {
            location = GLES20.glGetAttribLocation(program, key)
        }
        Util.assert(location >= 0)
        onBind(location)
    }

    override fun onBind(location: Int) {
        val vertexBuffer = ByteBuffer.allocateDirect(value.size * java.lang.Float.SIZE / 8)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .apply {
                    put(value)
                    position(0)
                }
        glVertexAttribPointer(location, componentCount, GL_FLOAT, false, 0, vertexBuffer)
        glEnableVertexAttribArray(location)
    }

    override fun update(value: Any) {
        (value as? FloatArray)?.let {
            this.value = it
        }
    }

}
