package io.github.kenneycode.fusion.parameter

import java.nio.ByteBuffer
import java.nio.ByteOrder

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glVertexAttribPointer

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader float数组参数
 *
 */

class FloatArrayParameter(key: String, private var value: FloatArray, private val componentCount: Int = 2) : AttributeParameter(key) {

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
