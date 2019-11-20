package io.github.kenneycode.fusion.parameter

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader参数基类
 *
 */

abstract class Parameter(val key : String) {

    protected var location: Int = -1

    /**
     *
     * 绑定attribute参数
     *
     * @param program GL Program
     *
     */
    open fun bindAttribute(program: Int) {}

    /**
     *
     * 绑定uniform参数
     *
     * @param program GL Program
     *
     */
    open fun bindUniform(program: Int) {}

    /**
     *
     * 更新参数
     *
     * @param value 新值
     *
     */
    open fun updateValue(value: Any) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Parameter
        if (key != other.key) {
            return false
        }
        if (location != other.location) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + location
        return result
    }


}
