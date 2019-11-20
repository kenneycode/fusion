package io.github.kenneycode.fusion.common

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

        val COMMON_VERTEX_SHADER = "precision mediump float;\n" +
                "attribute vec4 a_position;\n" +
                "void main() {\n" +
                "    gl_Position = a_position;\n" +
                "}"

        val COMMON_FRAGMENT_SHADER = "precision mediump float;\n" +
                "void main() {\n" +
                "    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                "}"

        val COMMON_VERTEX_SHADER_2 = "precision mediump float;\n" +
                "attribute vec4 a_position;\n" +
                "attribute vec2 a_textureCoordinate;\n" +
                "varying vec2 v_textureCoordinate;\n" +
                "void main() {\n" +
                "    v_textureCoordinate = a_textureCoordinate;\n" +
                "    gl_Position = a_position;\n" +
                "}"

        val COMMON_FRAGMENT_SHADER_2 = "precision mediump float;\n" +
                "varying vec2 v_textureCoordinate;\n" +
                "uniform sampler2D u_texture;\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
                "}"


        val COMMON_VERTEX = floatArrayOf(-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f)
        val COMMON_VERTEX_FLIP_X = floatArrayOf(1f, -1f, 1f, 1f, -1f, 1f, 1f, -1f, -1f, 1f, -1f, -1f)
        val COMMON_VERTEX_FLIP_Y = floatArrayOf(-1f, 1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f)
        val COMMON_VERTEX_FLIP_XY = floatArrayOf(1f, 1f, 1f, -1f, -1f, -1f, 1f, 1f, -1f, -1f, -1f, 1f)
        val COMMON_TEXTURE_COORDINATE = floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 1f, 0f)

        val POSITION_PARAM_KEY = "a_position"
        val TEXTURE_COORDINATE_PARAM_KEY = "a_textureCoordinate"

    }
}
