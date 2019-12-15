package io.github.kenneycode.fusion.common

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader包装类，包括vertex shader和fragment shader
 *
 */

class Shader constructor(var vertexShader: String = Constants.SIMPLE_VERTEX_SHADER, var fragmentShader: String = Constants.SIMPLE_FRAGMENT_SHADER) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Shader
        if (vertexShader != other.vertexShader) {
            return false
        }
        if (fragmentShader != other.fragmentShader) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = vertexShader.hashCode()
        result = 31 * result + fragmentShader.hashCode()
        return result
    }

}
