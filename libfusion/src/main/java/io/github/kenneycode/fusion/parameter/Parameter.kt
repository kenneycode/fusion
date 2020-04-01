package io.github.kenneycode.fusion.parameter

import io.github.kenneycode.fusion.common.Constants

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

    protected var location: Int = Constants.INVALID_LOCATION

    /**
     *
     * 绑定参数，外部的调用入口
     *
     * @param program GL Program
     *
     */
    abstract fun bind(program: Int)

    /**
     *
     * 绑定参数，给子类的回调
     *
     * @param location 参数location
     *
     */
    protected abstract fun onBind(location: Int)

    protected open fun getValue(): Any { return 0 }

    /**
     *
     * 更新参数
     *
     * @param value 新值
     *
     */
    open fun update(value: Any) {}

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
