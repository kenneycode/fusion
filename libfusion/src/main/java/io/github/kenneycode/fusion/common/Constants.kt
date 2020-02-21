package io.github.kenneycode.fusion.common

import android.opengl.Matrix

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 常用常量
 *
 */

class Constants {

    companion object {

        val SIMPLE_VERTEX_SHADER =
                "precision mediump float;\n" +
                "attribute vec4 a_position;\n" +
                "attribute vec2 a_textureCoordinate;\n" +
                "varying vec2 v_textureCoordinate;\n" +
                "void main() {\n" +
                "    v_textureCoordinate = a_textureCoordinate;\n" +
                "    gl_Position = a_position;\n" +
                "}"

        val MVP_VERTEX_SHADER =
            "precision mediump float;\n" +
            "attribute vec4 a_position;\n" +
            "attribute vec2 a_textureCoordinate;\n" +
            "uniform mat4 u_mvpMatrix;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "void main() {\n" +
            "    v_textureCoordinate = a_textureCoordinate;\n" +
            "    gl_Position = u_mvpMatrix * a_position;\n" +
            "}"

        val SIMPLE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "uniform sampler2D u_texture;\n" +
            "void main() {\n" +
            "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
            "}"

        val OES_VERTEX_SHADER =
            "precision mediump float;\n" +
            "attribute vec4 a_position;\n" +
            "attribute vec4 a_textureCoordinate;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "uniform mat4 u_stMatrix;\n" +
            "void main() {\n" +
            "    v_textureCoordinate = (u_stMatrix * a_textureCoordinate).xy;\n" +
            "    gl_Position = a_position;\n" +
            "}"

        val OES_FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "uniform samplerExternalOES u_texture;\n" +
            "void main() {\n" +
            "   gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
            "}"

        val LUT_FRAGMENT_SHADER =
            "precision highp float;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_lutTexture;\n" +
            "varying vec2 v_textureCoordinate;\n" +
            "uniform float strength;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 color = texture2D(u_texture, v_textureCoordinate);\n" +
            "    float blueColor = color.b * 63.0;\n" +
            "    \n" +
            "    vec2 quad1;\n" +
            "    quad1.y = floor(floor(blueColor) / 8.0);\n" +
            "    quad1.x = floor(blueColor) - (quad1.y * 8.0);\n" +
            "    \n" +
            "    vec2 quad2;\n" +
            "    quad2.y = floor(ceil(blueColor) / 8.0);\n" +
            "    quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n" +
            "    \n" +
            "    vec2 texPos1;\n" +
            "    texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * color.r);\n" +
            "    texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * color.g);\n" +
            "    \n" +
            "    vec2 texPos2;\n" +
            "    texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * color.r);\n" +
            "    texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * color.g);\n" +
            "    \n" +
            "    vec4 newColor1 = texture2D(u_lutTexture, texPos1);\n" +
            "    vec4 newColor2 = texture2D(u_lutTexture, texPos2);\n" +
            "    \n" +
            "    vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
            "    newColor = mix(color, vec4(newColor.rgb, color.w), strength);\n" +
            "    gl_FragColor = newColor;\n" +
            "}"

        val SIMPLE_VERTEX = floatArrayOf(-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f)
        val SIMPLE_VERTEX_FLIP_X = floatArrayOf(1f, -1f, 1f, 1f, -1f, 1f, 1f, -1f, -1f, 1f, -1f, -1f)
        val SIMPLE_VERTEX_FLIP_Y = floatArrayOf(-1f, 1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f)
        val SIMPLE_VERTEX_FLIP_XY = floatArrayOf(1f, 1f, 1f, -1f, -1f, -1f, 1f, 1f, -1f, -1f, -1f, 1f)
        val SIMPLE_TEXTURE_COORDINATE = floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 1f, 0f)
        val IDENTITY_MATRIX: FloatArray
            get() = FloatArray(16).apply {
                        Matrix.setIdentityM(this, 0)
                    }

        val POSITION_PARAM_KEY = "a_position"
        val TEXTURE_COORDINATE_PARAM_KEY = "a_textureCoordinate"
        val MVP_MATRIX_PARAM_KEY = "u_mvpMatrix"

        val INVALID_LOCATION = -1

    }
}
